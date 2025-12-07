package org.example.codingtickets.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");

        if (dbUrl == null) dbUrl = "jdbc:postgresql://localhost:5432/codingtickets";
        else dbUrl = dbUrl.trim(); // <--- Ajoute .trim() ici

        if (dbUser == null) dbUser = "postgres";
        else dbUser = dbUser.trim(); // <--- Ajoute .trim() ici

        if (dbPass == null) dbPass = "postgres";
        else dbPass = dbPass.trim(); // <--- Ajoute .trim() ici

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
}
