package com.smarthostel.database;

import com.smarthostel.config.EnvConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/smart_hostel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String URL = getConfigurationValue("MYSQL_URL", DEFAULT_URL);
    private static final String USERNAME = getConfigurationValue("MYSQL_USERNAME", "root");
    private static final String PASSWORD = EnvConfig.get("MYSQL_PASSWORD");

    private static String getConfigurationValue(String name, String defaultValue) {
        String value = EnvConfig.get(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to the project classpath.", e);
        }

        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException(
                    "Unable to connect to MySQL. Verify that MySQL is running, run database.sql, "
                            + "and set MYSQL_URL, MYSQL_USERNAME, and MYSQL_PASSWORD if your local credentials differ.",
                    e);
        }
    }
}
