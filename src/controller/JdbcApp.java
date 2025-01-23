package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcApp {

    static String url = "jdbc:mysql://localhost:3306/tt";
    static String user = "root";
    static String password = "root";

    public static void dbConnection() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("DB Connected");
            }
        } catch (SQLException e) {
            System.err.println("Error details: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        dbConnection();
    }
}
