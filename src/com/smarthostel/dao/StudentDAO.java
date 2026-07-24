package com.smarthostel.dao;

import com.smarthostel.database.DatabaseConnection;
import com.smarthostel.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (id, name, phone, address, gender, branch, year, parent_phone, hostel_block, room_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getPhone());
            ps.setString(4, student.getAddress());
            ps.setString(5, student.getGender());
            ps.setString(6, student.getBranch());
            ps.setInt(7, student.getYear());
            ps.setString(8, student.getParentPhone());
            ps.setString(9, student.getHostelBlock());
            ps.setInt(10, student.getRoomNumber());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, phone = ?, address = ?, gender = ?, branch = ?, year = ?, parent_phone = ?, hostel_block = ?, room_number = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getPhone());
            ps.setString(3, student.getAddress());
            ps.setString(4, student.getGender());
            ps.setString(5, student.getBranch());
            ps.setInt(6, student.getYear());
            ps.setString(7, student.getParentPhone());
            ps.setString(8, student.getHostelBlock());
            ps.setInt(9, student.getRoomNumber());
            ps.setInt(10, student.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    public Student searchStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("gender"),
                            rs.getString("branch"),
                            rs.getInt("year"),
                            rs.getString("parent_phone"),
                            rs.getString("hostel_block"),
                            rs.getInt("room_number")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error searching student: " + e.getMessage());
        }

        return null;
    }

    public List<Student> viewAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getString("branch"),
                        rs.getInt("year"),
                        rs.getString("parent_phone"),
                        rs.getString("hostel_block"),
                        rs.getInt("room_number")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }

        return students;
    }
}
