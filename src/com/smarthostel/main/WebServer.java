package com.smarthostel.main;

import com.smarthostel.database.DatabaseConnection;
import com.smarthostel.model.Complaint;
import com.smarthostel.model.Room;
import com.smarthostel.model.Student;
import com.smarthostel.service.AuthService;
import com.smarthostel.service.HostelService;
import com.smarthostel.config.EnvConfig;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;

/** JDK-only web server with shared admin/student sessions. */
public final class WebServer {
    private static final HostelService hostel = new HostelService();
    private static final Path FRONTEND = Path.of("frontend").toAbsolutePath().normalize();
    private WebServer() { }

    public static void start() throws IOException {
        String configuredPort = EnvConfig.get("PORT");
        int port = configuredPort.isBlank() ? 8080 : Integer.parseInt(configuredPort);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", WebServer::api);
        server.createContext("/", WebServer::staticFile);
        server.setExecutor(Executors.newCachedThreadPool()); server.start();
        System.out.println("Smart Hostel Management System is running at http://localhost:" + port);
    }

    private static void api(HttpExchange ex) throws IOException {
        try {
            String path = ex.getRequestURI().getPath().substring(4), method = ex.getRequestMethod();
            Map<String,String> body = Json.read(readBody(ex));
            if (path.equals("/login") && method.equals("POST")) { adminLogin(ex, body); return; }
            if (path.equals("/student/login") && method.equals("POST")) { studentLogin(ex, body); return; }
            if (path.equals("/logout") && method.equals("POST")) { AuthService.remove(token(ex)); send(ex,200,Json.object("message","Logged out")); return; }
            if (path.equals("/student/me") && method.equals("GET")) { studentProfile(ex); return; }
            if (path.equals("/student/room") && method.equals("GET")) { studentRoom(ex); return; }
            if (path.equals("/student/complaints")) { studentComplaints(ex,method,body); return; }
            if (path.equals("/ai/chat") && method.equals("POST")) { aiChat(ex,body); return; }
            if (path.equals("/ai/complaint-analysis") && method.equals("POST")) { requireUser(ex); send(ex,200,Ai.analysis(value(body,"description"))); return; }
            AuthService.Session admin = requireAdmin(ex);
            if (path.equals("/dashboard") && method.equals("GET")) { dashboard(ex); return; }
            if (path.equals("/students")) { students(ex,method,body); return; }
            if (path.startsWith("/students/")) { studentById(ex,method,body,path); return; }
            if (path.equals("/rooms")) { rooms(ex,method,body); return; }
            if (path.startsWith("/rooms/")) { roomAction(ex,method,body,path); return; }
            if (path.equals("/complaints")) { complaints(ex,method,body); return; }
            if (path.startsWith("/complaints/")) { complaintById(ex,method,body,path); return; }
            send(ex,404,Json.object("error","API endpoint not found"));
        } catch (SecurityException e) { send(ex,401,Json.object("error",e.getMessage())); }
          catch (IllegalArgumentException e) { send(ex,400,Json.object("error",e.getMessage())); }
          catch (Exception e) { e.printStackTrace(); send(ex,500,Json.object("error","Server error: "+e.getMessage())); }
    }

    private static void adminLogin(HttpExchange ex, Map<String,String> b) throws IOException {
        boolean valid=false; try(Connection c=DatabaseConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM admin WHERE username=? AND password=?")) { p.setString(1,value(b,"username")); p.setString(2,value(b,"password")); try(ResultSet r=p.executeQuery()){valid=r.next();} } catch(Exception e){throw new IllegalArgumentException("Database login unavailable.");}
        if (!valid) { send(ex,401,Json.object("error","Invalid username or password")); return; }
        String token=AuthService.create("admin",0,"Administrator"); send(ex,200,Json.object("success",true,"role","admin","token",token));
    }
    private static void studentLogin(HttpExchange ex, Map<String,String> b) throws IOException {
        Student student=hostel.authenticateStudent(number(value(b,"studentId")),value(b,"password"));
        if(student==null){send(ex,401,Json.object("error","Invalid student ID or password"));return;}
        String token=AuthService.create("student",student.getId(),student.getName()); send(ex,200,Json.object("success",true,"role","student","token",token,"name",student.getName()));
    }
    private static void studentProfile(HttpExchange ex) throws IOException { AuthService.Session s=requireStudent(ex); Student student=hostel.searchStudentById(s.studentId()); if(student==null)throw new SecurityException("Student account not found"); send(ex,200,Json.student(student)); }
    private static void studentRoom(HttpExchange ex) throws IOException { AuthService.Session s=requireStudent(ex); Student student=hostel.searchStudentById(s.studentId()); Room room=student==null?null:hostel.findRoom(student.getRoomNumber()); if(room==null)send(ex,404,Json.object("error","Room details are not available"));else send(ex,200,Json.room(room)); }
    private static void studentComplaints(HttpExchange ex,String method,Map<String,String> b) throws IOException { AuthService.Session s=requireStudent(ex); if(method.equals("GET")){send(ex,200,Json.complaints(hostel.viewComplaintsByStudent(s.studentId())));return;} if(method.equals("POST")){Complaint c=new Complaint(hostel.nextComplaintId(),s.studentId(),value(b,"category"),value(b,"description"),LocalDate.now().toString(),"Pending");result(ex,hostel.registerComplaint(c),"Complaint submitted");return;}send(ex,405,Json.object("error","Method not allowed")); }

    private static void dashboard(HttpExchange ex)throws IOException{send(ex,200,Json.object("totalStudents",hostel.getTotalStudents(),"totalRooms",hostel.getTotalRooms(),"availableRooms",hostel.getAvailableRooms(),"occupiedRooms",hostel.getOccupiedRooms(),"pendingComplaints",hostel.getPendingComplaints(),"resolvedComplaints",hostel.getResolvedComplaints()));}
    private static void students(HttpExchange ex,String m,Map<String,String>b)throws IOException{if(m.equals("GET")){send(ex,200,Json.students(hostel.viewAllStudents()));return;}if(m.equals("POST")){result(ex,hostel.addStudent(student(b,number(value(b,"id"))),value(b,"password")),"Student added");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static void studentById(HttpExchange ex,String m,Map<String,String>b,String p)throws IOException{int id=number(p.substring(10));if(m.equals("GET")){Student s=hostel.searchStudentById(id);if(s==null)send(ex,404,Json.object("error","Student not found"));else send(ex,200,Json.student(s));return;}if(m.equals("PUT")){result(ex,hostel.updateStudent(student(b,id)),"Student updated");return;}if(m.equals("DELETE")){result(ex,hostel.deleteStudent(id),"Student deleted");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static Student student(Map<String,String>b,int id){return new Student(id,value(b,"name"),value(b,"phone"),value(b,"address"),value(b,"gender"),value(b,"branch"),number(value(b,"year")),value(b,"parentPhone"),value(b,"hostelBlock"),number(value(b,"roomNumber")));}
    private static void rooms(HttpExchange ex,String m,Map<String,String>b)throws IOException{if(m.equals("GET")){send(ex,200,Json.rooms(hostel.viewAllRooms()));return;}if(m.equals("POST")){result(ex,hostel.addRoom(room(b,number(value(b,"roomNumber")))),"Room added");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static void roomAction(HttpExchange ex,String m,Map<String,String>b,String p)throws IOException{String[]bits=p.substring(7).split("/");int id=number(bits[0]);if(bits.length==2&&m.equals("POST")){result(ex,bits[1].equals("allocate")?hostel.allocateRoom(id):hostel.vacateRoom(id),"Room updated");return;}if(m.equals("PUT")){result(ex,hostel.updateRoom(room(b,id)),"Room updated");return;}if(m.equals("DELETE")){result(ex,hostel.deleteRoom(id),"Room deleted");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static Room room(Map<String,String>b,int id){return new Room(id,value(b,"block"),number(value(b,"capacity")),number(value(b,"occupiedBeds")));}
    private static void complaints(HttpExchange ex,String m,Map<String,String>b)throws IOException{if(m.equals("GET")){send(ex,200,Json.complaints(hostel.viewAllComplaints()));return;}if(m.equals("POST")){Complaint c=new Complaint(number(value(b,"complaintId")),number(value(b,"studentId")),value(b,"category"),value(b,"description"),b.getOrDefault("date",LocalDate.now().toString()),b.getOrDefault("status","Pending"));result(ex,hostel.registerComplaint(c),"Complaint created");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static void complaintById(HttpExchange ex,String m,Map<String,String>b,String p)throws IOException{int id=number(p.substring(12));if(m.equals("PUT")){result(ex,hostel.updateComplaintStatus(id,value(b,"status")),"Complaint status updated");return;}if(m.equals("DELETE")){result(ex,hostel.deleteComplaint(id),"Complaint deleted");return;}send(ex,405,Json.object("error","Method not allowed"));}
    private static void aiChat(HttpExchange ex,Map<String,String>b)throws IOException{AuthService.Session s=requireUser(ex);String prefix=s.role().equals("student")?"You are assisting "+s.name()+" (student ID "+s.studentId()+"). ":"";send(ex,200,Ai.answer(prefix+value(b,"message")));}

    private static AuthService.Session requireUser(HttpExchange ex){AuthService.Session s=AuthService.get(token(ex));if(s==null)throw new SecurityException("Please log in first");return s;}
    private static AuthService.Session requireAdmin(HttpExchange ex){AuthService.Session s=requireUser(ex);if(!s.role().equals("admin"))throw new SecurityException("Admin access required");return s;}
    private static AuthService.Session requireStudent(HttpExchange ex){AuthService.Session s=requireUser(ex);if(!s.role().equals("student"))throw new SecurityException("Student access required");return s;}
    private static String token(HttpExchange ex){String h=ex.getRequestHeaders().getFirst("Authorization");return h!=null&&h.startsWith("Bearer ")?h.substring(7):null;}
    private static void result(HttpExchange ex,boolean ok,String message)throws IOException{send(ex,ok?200:400,Json.object(ok?"message":"error",ok?message:"Operation failed. Check the data and database."));}
    private static String value(Map<String,String>b,String n){String s=b.get(n);if(s==null||s.trim().isEmpty())throw new IllegalArgumentException(n+" is required");return s.trim();}
    private static int number(String s){try{return Integer.parseInt(s);}catch(Exception e){throw new IllegalArgumentException("A valid number is required");}}
    private static String readBody(HttpExchange ex)throws IOException{try(InputStream in=ex.getRequestBody()){return new String(in.readAllBytes(),StandardCharsets.UTF_8);}}
    private static void send(HttpExchange ex,int status,String data)throws IOException{byte[]out=data.getBytes(StandardCharsets.UTF_8);ex.getResponseHeaders().set("Content-Type","application/json; charset=utf-8");ex.sendResponseHeaders(status,out.length);ex.getResponseBody().write(out);ex.close();}
    private static void staticFile(HttpExchange ex)throws IOException{String raw=ex.getRequestURI().getPath();if(raw.equals("/"))raw="/index.html";Path f=FRONTEND.resolve(raw.substring(1)).normalize();if(!f.startsWith(FRONTEND)||!Files.isRegularFile(f)){ex.sendResponseHeaders(404,-1);return;}String type=raw.endsWith(".css")?"text/css":raw.endsWith(".js")?"application/javascript":"text/html";byte[]o=Files.readAllBytes(f);ex.getResponseHeaders().set("Content-Type",type+"; charset=utf-8");ex.sendResponseHeaders(200,o.length);ex.getResponseBody().write(o);ex.close();}

    static final class Ai { static String answer(String m){return call("You are a concise, helpful hostel assistant. "+m,false);} static String analysis(String d){return call("Analyze this hostel complaint. Return ONLY JSON with summary, category, priority (LOW, MEDIUM, or HIGH), and suggestedAction. Complaint: "+d,true);} static String call(String prompt,boolean analysis){String key=EnvConfig.get("GENAI_API_KEY");if(key.isBlank())return demo(prompt,analysis);try{String req="{\"contents\":[{\"parts\":[{\"text\":"+Json.quote(prompt)+"}]}]}";String model=EnvConfig.get("GENAI_MODEL");if(model.isBlank())model="gemini-3.5-flash-lite";HttpRequest r=HttpRequest.newBuilder(URI.create("https://generativelanguage.googleapis.com/v1beta/models/"+model+":generateContent?key="+URLEncoder.encode(key,StandardCharsets.UTF_8))).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(req)).build();HttpResponse<String>res=HttpClient.newHttpClient().send(r,HttpResponse.BodyHandlers.ofString());if(res.statusCode()/100!=2)throw new IOException("Gemini API returned HTTP "+res.statusCode());String text=Json.read(res.body()).get("text");if(text==null)throw new IOException("No text returned");return analysis?text:Json.object("reply",text,"mode","live");}catch(Exception e){System.err.println("Gemini request failed; using demo mode: "+e.getMessage());return demo(prompt,analysis);}} static String demo(String input,boolean analysis){return analysis?Json.object("summary",input,"category","General Maintenance","priority","MEDIUM","suggestedAction","Assign to maintenance.","mode","demo"):Json.object("reply","Demo mode is active because Gemini is unavailable.","mode","demo");}}
}
