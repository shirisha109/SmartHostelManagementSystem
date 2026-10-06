package com.smarthostel.main;

import com.smarthostel.model.*;
import java.util.*;
import java.util.regex.*;

/** Small JSON helper for the simple, flat request bodies used by this project. */
final class Json {
    private Json() { }
    static Map<String,String> read(String json) {
        Map<String,String> values = new HashMap<>();
        if (json == null) return values;
        Matcher m = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:\\s*(\\\"(?:\\\\.|[^\\\"])*\\\"|-?\\d+|true|false|null)").matcher(json);
        while (m.find()) { String v=m.group(2); values.put(m.group(1), v.startsWith("\"") ? unquote(v) : v.equals("null") ? "" : v); }
        return values;
    }
    static String object(Object... pairs) { StringBuilder s=new StringBuilder("{"); for(int i=0;i<pairs.length;i+=2){if(i>0)s.append(',');s.append(quote(String.valueOf(pairs[i]))).append(':');Object v=pairs[i+1];s.append(v instanceof Number||v instanceof Boolean? v:quote(String.valueOf(v)));} return s.append('}').toString(); }
    static String quote(String s) { return "\""+(s==null?"":s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n").replace("\r",""))+"\""; }
    private static String unquote(String s) { return s.substring(1,s.length()-1).replace("\\n","\n").replace("\\\"","\"").replace("\\\\","\\"); }
    static String student(Student s) { return object("id",s.getId(),"name",s.getName(),"phone",s.getPhone(),"address",s.getAddress(),"gender",s.getGender(),"branch",s.getBranch(),"year",s.getYear(),"parentPhone",s.getParentPhone(),"hostelBlock",s.getHostelBlock(),"roomNumber",s.getRoomNumber()); }
    static String students(List<Student> list) { StringBuilder s=new StringBuilder("["); for(Student x:list){if(s.length()>1)s.append(',');s.append(student(x));} return s.append(']').toString(); }
    static String room(Room r) { return object("roomNumber",r.getRoomNumber(),"block",r.getBlock(),"capacity",r.getCapacity(),"occupiedBeds",r.getOccupiedBeds(),"status",r.getOccupiedBeds()<r.getCapacity()?"Available":"Full"); }
    static String rooms(List<Room> list) { StringBuilder s=new StringBuilder("["); for(Room x:list){if(s.length()>1)s.append(',');s.append(room(x));} return s.append(']').toString(); }
    static String complaint(Complaint c) { return object("complaintId",c.getComplaintId(),"studentId",c.getStudentId(),"category",c.getCategory(),"description",c.getDescription(),"date",c.getDate(),"status",c.getStatus()); }
    static String complaints(List<Complaint> list) { StringBuilder s=new StringBuilder("["); for(Complaint x:list){if(s.length()>1)s.append(',');s.append(complaint(x));} return s.append(']').toString(); }
}
