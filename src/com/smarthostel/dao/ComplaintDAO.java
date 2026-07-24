package com.smarthostel.dao;

import com.smarthostel.database.DatabaseConnection;
import com.smarthostel.model.Complaint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public boolean registerComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (complaint_id, student_id, category, description, date, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaint.getComplaintId());
            ps.setInt(2, complaint.getStudentId());
            ps.setString(3, complaint.getCategory());
            ps.setString(4, complaint.getDescription());
            ps.setString(5, complaint.getDate());
            ps.setString(6, complaint.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error registering complaint: " + e.getMessage());
            return false;
        }
    }

    public List<Complaint> viewAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT complaint_id, student_id, category, description, date, status FROM complaints";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                complaints.add(new Complaint(
                        rs.getInt("complaint_id"),
                        rs.getInt("student_id"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading complaints: " + e.getMessage());
        }

        return complaints;
    }

    public Complaint searchComplaintById(int complaintId) {
        String sql = "SELECT complaint_id, student_id, category, description, date, status "
                + "FROM complaints WHERE complaint_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Complaint(
                            rs.getInt("complaint_id"),
                            rs.getInt("student_id"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getString("date"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching complaint: " + e.getMessage());
        }

        return null;
    }

    public boolean updateComplaintStatus(int complaintId, String status) {
        String sql = "UPDATE complaints SET status = ? WHERE complaint_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, complaintId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating complaint status: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteComplaint(int complaintId) {
        String sql = "DELETE FROM complaints WHERE complaint_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting complaint: " + e.getMessage());
            return false;
        }
    }
}
