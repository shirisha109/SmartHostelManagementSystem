package com.smarthostel.service;

import com.smarthostel.dao.ComplaintDAO;
import com.smarthostel.dao.RoomDAO;
import com.smarthostel.dao.StudentDAO;
import com.smarthostel.model.Complaint;
import com.smarthostel.model.Room;
import com.smarthostel.model.Student;
import java.util.List;

public class HostelService {

    private final StudentDAO studentDAO;
    private final RoomDAO roomDAO;
    private final ComplaintDAO complaintDAO;

    public HostelService() {
        this.studentDAO = new StudentDAO();
        this.roomDAO = new RoomDAO();
        this.complaintDAO = new ComplaintDAO();
    }

    // Student operations
    public boolean addStudent(Student student) {
        return studentDAO.addStudent(student);
    }

    public boolean updateStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    public Student searchStudentById(int id) {
        return studentDAO.searchStudentById(id);
    }

    public List<Student> viewAllStudents() {
        return studentDAO.viewAllStudents();
    }

    // Room operations
    public boolean addRoom(Room room) {
        return roomDAO.addRoom(room);
    }

    public boolean updateRoom(Room room) {
        return roomDAO.updateRoom(room);
    }

    public boolean deleteRoom(int roomNumber) {
        return roomDAO.deleteRoom(roomNumber);
    }

    public List<Room> viewAllRooms() {
        return roomDAO.viewAllRooms();
    }

    public boolean allocateRoom(int roomNumber) {
        return roomDAO.allocateRoom(roomNumber);
    }

    public boolean vacateRoom(int roomNumber) {
        return roomDAO.vacateRoom(roomNumber);
    }

    // Complaint operations
    public boolean registerComplaint(Complaint complaint) {
        return complaintDAO.registerComplaint(complaint);
    }

    public List<Complaint> viewAllComplaints() {
        return complaintDAO.viewAllComplaints();
    }

    public Complaint searchComplaintById(int complaintId) {
        return complaintDAO.searchComplaintById(complaintId);
    }

    public boolean updateComplaintStatus(int complaintId, String status) {
        return complaintDAO.updateComplaintStatus(complaintId, status);
    }

    public boolean deleteComplaint(int complaintId) {
        return complaintDAO.deleteComplaint(complaintId);
    }

    // Dashboard summary methods
    public int getTotalStudents() {
        return viewAllStudents().size();
    }

    public int getTotalRooms() {
        return viewAllRooms().size();
    }

    public int getAvailableRooms() {
        List<Room> rooms = viewAllRooms();
        int available = 0;

        for (Room room : rooms) {
            if (room.getOccupiedBeds() < room.getCapacity()) {
                available++;
            }
        }

        return available;
    }

    public int getPendingComplaints() {
        List<Complaint> complaints = viewAllComplaints();
        int pending = 0;

        for (Complaint complaint : complaints) {
            if ("Pending".equalsIgnoreCase(complaint.getStatus())) {
                pending++;
            }
        }

        return pending;
    }
}
