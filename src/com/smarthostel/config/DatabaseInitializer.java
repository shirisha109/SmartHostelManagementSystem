package com.smarthostel.config;

import com.smarthostel.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** Applies the small backwards-compatible migration required for student login. */
public final class DatabaseInitializer {
    private DatabaseInitializer() { }
    public static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection(); Statement statement = connection.createStatement()) {
            boolean passwordColumnExists = false;
            try (ResultSet columns = connection.getMetaData().getColumns(connection.getCatalog(), null, "students", "password")) {
                passwordColumnExists = columns.next();
            }
            if (!passwordColumnExists) statement.execute("ALTER TABLE students ADD COLUMN password VARCHAR(100) NULL");
            statement.executeUpdate("INSERT IGNORE INTO rooms (room_number, block, capacity, occupied_beds) VALUES (901, 'Demo', 2, 1)");
            statement.executeUpdate("INSERT IGNORE INTO students (id, name, phone, address, gender, branch, year, parent_phone, hostel_block, room_number, password) VALUES (900001, 'Demo Student', '9000000001', 'Campus Hostel', 'Other', 'CSE', 1, '9000000001', 'Demo', 901, 'student123')");
        } catch (Exception e) {
            throw new IllegalStateException("Student login database setup failed: " + e.getMessage(), e);
        }
    }
}
