package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcApp {

    static String url = "jdbc:mysql://localhost:3306/bipul";
    static String user = "root";
    static String password = "root";

    private JdbcApp() {

    }

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

}
