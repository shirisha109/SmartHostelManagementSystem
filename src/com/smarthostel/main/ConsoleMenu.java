package com.smarthostel.main;

import com.smarthostel.model.Complaint;
import com.smarthostel.model.Room;
import com.smarthostel.model.Student;
import com.smarthostel.service.HostelService;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final HostelService hostelService;
    private final Scanner scanner;

    public ConsoleMenu() {
        this.hostelService = new HostelService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n===== Smart Hostel Management System =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Management");
            System.out.println("3. Room Management");
            System.out.println("4. Complaint Management");
            System.out.println("5. Dashboard");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    studentMenu();
                    break;
                case 3:
                    roomMenu();
                    break;
                case 4:
                    complaintMenu();
                    break;
                case 5:
                    showDashboard();
                    break;
                case 6:
                    System.out.println("Thank you for using Smart Hostel Management System.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void adminLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean validLogin = "admin".equals(username) && ("admin123".equals(password) || "admin".equals(password));
        if (validLogin) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void studentMenu() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addStudent() {
        Student student = new Student();
        System.out.print("Enter Student ID: ");
        student.setId(readInt());
        System.out.print("Enter Name: ");
        student.setName(readLine());
        System.out.print("Enter Phone: ");
        student.setPhone(readLine());
        System.out.print("Enter Address: ");
        student.setAddress(readLine());
        System.out.print("Enter Gender: ");
        student.setGender(readLine());
        System.out.print("Enter Branch: ");
        student.setBranch(readLine());
        System.out.print("Enter Year: ");
        student.setYear(readInt());
        System.out.print("Enter Parent Phone: ");
        student.setParentPhone(readLine());
        System.out.print("Enter Hostel Block: ");
        student.setHostelBlock(readLine());
        System.out.print("Enter Room Number: ");
        student.setRoomNumber(readInt());

        boolean success = hostelService.addStudent(student);
        if (success) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Failed to add student.");
        }
    }

    private void viewStudents() {
        List<Student> students = hostelService.viewAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    private void searchStudent() {
        System.out.print("Enter Student ID: ");
        int id = readInt();
        Student student = hostelService.searchStudentById(id);

        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        int id = readInt();
        Student student = hostelService.searchStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter new Name: ");
        student.setName(readLine());
        System.out.print("Enter new Phone: ");
        student.setPhone(readLine());
        System.out.print("Enter new Address: ");
        student.setAddress(readLine());
        System.out.print("Enter new Gender: ");
        student.setGender(readLine());
        System.out.print("Enter new Branch: ");
        student.setBranch(readLine());
        System.out.print("Enter new Year: ");
        student.setYear(readInt());
        System.out.print("Enter new Parent Phone: ");
        student.setParentPhone(readLine());
        System.out.print("Enter new Hostel Block: ");
        student.setHostelBlock(readLine());
        System.out.print("Enter new Room Number: ");
        student.setRoomNumber(readInt());

        boolean success = hostelService.updateStudent(student);
        if (success) {
            System.out.println("Student updated successfully.");
        } else {
            System.out.println("Failed to update student.");
        }
    }

    private void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int id = readInt();
        boolean success = hostelService.deleteStudent(id);

        if (success) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Failed to delete student.");
        }
    }

    private void roomMenu() {
        while (true) {
            System.out.println("\n--- Room Management ---");
            System.out.println("1. Add Room");
            System.out.println("2. View Rooms");
            System.out.println("3. Allocate Room");
            System.out.println("4. Vacate Room");
            System.out.println("5. Update Room");
            System.out.println("6. Delete Room");
            System.out.println("7. Back");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    addRoom();
                    break;
                case 2:
                    viewRooms();
                    break;
                case 3:
                    allocateRoom();
                    break;
                case 4:
                    vacateRoom();
                    break;
                case 5:
                    updateRoom();
                    break;
                case 6:
                    deleteRoom();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addRoom() {
        Room room = new Room();
        System.out.print("Enter Room Number: ");
        room.setRoomNumber(readInt());
        System.out.print("Enter Block: ");
        room.setBlock(readLine());
        System.out.print("Enter Capacity: ");
        room.setCapacity(readInt());
        System.out.print("Enter Occupied Beds: ");
        room.setOccupiedBeds(readInt());

        boolean success = hostelService.addRoom(room);
        if (success) {
            System.out.println("Room added successfully.");
        } else {
            System.out.println("Failed to add room.");
        }
    }

    private void viewRooms() {
        List<Room> rooms = hostelService.viewAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (Room room : rooms) {
                System.out.println(room);
            }
        }
    }

    private void allocateRoom() {
        System.out.print("Enter Room Number to allocate: ");
        int roomNumber = readInt();
        boolean success = hostelService.allocateRoom(roomNumber);

        if (success) {
            System.out.println("Room allocated successfully.");
        } else {
            System.out.println("Failed to allocate room.");
        }
    }

    private void vacateRoom() {
        System.out.print("Enter Room Number to vacate: ");
        int roomNumber = readInt();
        boolean success = hostelService.vacateRoom(roomNumber);

        if (success) {
            System.out.println("Room vacated successfully.");
        } else {
            System.out.println("Failed to vacate room.");
        }
    }

    private void updateRoom() {
        System.out.print("Enter Room Number to update: ");
        int roomNumber = readInt();
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        System.out.print("Enter new Block: ");
        room.setBlock(readLine());
        System.out.print("Enter new Capacity: ");
        room.setCapacity(readInt());
        System.out.print("Enter new Occupied Beds: ");
        room.setOccupiedBeds(readInt());

        boolean success = hostelService.updateRoom(room);
        if (success) {
            System.out.println("Room updated successfully.");
        } else {
            System.out.println("Failed to update room.");
        }
    }

    private void deleteRoom() {
        // Ask for the room number and delete the matching room.
        System.out.print("Enter Room Number to delete: ");
        int roomNumber = readInt();
        boolean success = hostelService.deleteRoom(roomNumber);

        if (success) {
            System.out.println("Room deleted successfully.");
        } else {
            System.out.println("Failed to delete room.");
        }
    }

    private void complaintMenu() {
        while (true) {
            System.out.println("\n--- Complaint Management ---");
            System.out.println("1. Register Complaint");
            System.out.println("2. View Complaints");
            System.out.println("3. Search Complaint");
            System.out.println("4. Update Complaint Status");
            System.out.println("5. Delete Complaint");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    registerComplaint();
                    break;
                case 2:
                    viewComplaints();
                    break;
                case 3:
                    searchComplaint();
                    break;
                case 4:
                    updateComplaintStatus();
                    break;
                case 5:
                    deleteComplaint();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerComplaint() {
        Complaint complaint = new Complaint();
        System.out.print("Enter Complaint ID: ");
        complaint.setComplaintId(readInt());
        System.out.print("Enter Student ID: ");
        complaint.setStudentId(readInt());
        System.out.print("Enter Category: ");
        complaint.setCategory(readLine());
        System.out.print("Enter Description: ");
        complaint.setDescription(readLine());
        System.out.print("Enter Date: ");
        complaint.setDate(readLine());
        System.out.print("Enter Status: ");
        complaint.setStatus(readLine());

        boolean success = hostelService.registerComplaint(complaint);
        if (success) {
            System.out.println("Complaint registered successfully.");
        } else {
            System.out.println("Failed to register complaint.");
        }
    }

    private void viewComplaints() {
        List<Complaint> complaints = hostelService.viewAllComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
        } else {
            for (Complaint complaint : complaints) {
                System.out.println(complaint);
            }
        }
    }

    private void searchComplaint() {
        System.out.print("Enter Complaint ID: ");
        int complaintId = readInt();
        Complaint complaint = hostelService.searchComplaintById(complaintId);

        if (complaint != null) {
            System.out.println(complaint);
        } else {
            System.out.println("Complaint not found.");
        }
    }

    private void updateComplaintStatus() {
        System.out.print("Enter Complaint ID: ");
        int complaintId = readInt();
        System.out.print("Enter new Status: ");
        String status = readLine();

        boolean success = hostelService.updateComplaintStatus(complaintId, status);
        if (success) {
            System.out.println("Complaint status updated successfully.");
        } else {
            System.out.println("Failed to update complaint status.");
        }
    }

    private void deleteComplaint() {
        System.out.print("Enter Complaint ID to delete: ");
        int complaintId = readInt();
        boolean success = hostelService.deleteComplaint(complaintId);

        if (success) {
            System.out.println("Complaint deleted successfully.");
        } else {
            System.out.println("Failed to delete complaint.");
        }
    }

    private void showDashboard() {
        System.out.println("\n--- Dashboard ---");
        System.out.println("Total Students: " + hostelService.getTotalStudents());
        System.out.println("Total Rooms: " + hostelService.getTotalRooms());
        System.out.println("Available Rooms: " + hostelService.getAvailableRooms());
        System.out.println("Pending Complaints: " + hostelService.getPendingComplaints());
    }

    private int readInt() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter again: ");
            }
        }
    }

    private String readLine() {
        return scanner.nextLine();
    }
}
