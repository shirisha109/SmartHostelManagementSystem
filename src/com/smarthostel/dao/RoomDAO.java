package com.smarthostel.dao;

import com.smarthostel.database.DatabaseConnection;
import com.smarthostel.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public boolean addRoom(Room room) {
        if (!isValidRoom(room)) {
            return false;
        }

        String sql = "INSERT INTO rooms (room_number, block, capacity, occupied_beds) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getBlock());
            ps.setInt(3, room.getCapacity());
            ps.setInt(4, room.getOccupiedBeds());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRoom(Room room) {
        if (!isValidRoom(room)) {
            return false;
        }

        String sql = "UPDATE rooms SET block = ?, capacity = ?, occupied_beds = ? WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getBlock());
            ps.setInt(2, room.getCapacity());
            ps.setInt(3, room.getOccupiedBeds());
            ps.setInt(4, room.getRoomNumber());

            boolean updated = ps.executeUpdate() > 0;
            if (!updated) {
                System.out.println("Room number " + room.getRoomNumber() + " was not found.");
            }
            return updated;

        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRoom(int roomNumber) {
        if (roomNumber <= 0) {
            System.out.println("Room number must be a positive whole number.");
            return false;
        }

        String sql = "DELETE FROM rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            boolean deleted = ps.executeUpdate() > 0;
            if (!deleted) {
                System.out.println("Room number " + roomNumber + " was not found.");
            }
            return deleted;

        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    public List<Room> viewAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_number, block, capacity, occupied_beds FROM rooms";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_number"),
                        rs.getString("block"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied_beds")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }

        return rooms;
    }

    public boolean allocateRoom(int roomNumber) {
        String sql = "UPDATE rooms SET occupied_beds = occupied_beds + 1 "
                + "WHERE room_number = ? AND occupied_beds < capacity";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error allocating room: " + e.getMessage());
            return false;
        }
    }

    public boolean vacateRoom(int roomNumber) {
        String sql = "UPDATE rooms SET occupied_beds = occupied_beds - 1 WHERE room_number = ? AND occupied_beds > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error vacating room: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidRoom(Room room) {
        if (room.getRoomNumber() <= 0) {
            System.out.println("Room number must be a positive whole number.");
            return false;
        }
        if (room.getBlock() == null || room.getBlock().trim().isEmpty()) {
            System.out.println("Room block cannot be empty.");
            return false;
        }
        if (room.getCapacity() <= 0) {
            System.out.println("Room capacity must be greater than zero.");
            return false;
        }
        if (room.getOccupiedBeds() < 0 || room.getOccupiedBeds() > room.getCapacity()) {
            System.out.println("Occupied beds must be between 0 and the room capacity.");
            return false;
        }
        return true;
    }
}
