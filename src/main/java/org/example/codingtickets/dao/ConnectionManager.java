package org.example.codingtickets.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL non trouvé", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");

        if (dbUrl == null) dbUrl = "jdbc:postgresql://localhost:5432/codingtickets";
        else dbUrl = dbUrl.trim();

        if (dbUser == null) dbUser = "postgres";
        else dbUser = dbUser.trim();

        if (dbPass == null) dbPass = "postgres";
        else dbPass = dbPass.trim();

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
}
