package request_handler.login;
import controller.JdbcApp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;

public class RequestHandlerLogin {


    // @Override
    // public LoginDto getByName(String name) throws SQLException {
    //     Connection con = JdbcApp.getConnection();
    //     LoginDto ld = null;
    //     String qry = "Select id, username, password from login where username = ?";
    //     PreparedStatement ps = con.prepareStatement(qry);
    //     ps.setString(1, name);
    //     ResultSet rs = ps.executeQuery();
    //     if (rs.next()) {
    //         int oid = rs.getInt("id");
    //         String uname = rs.getString("username");
    //         String pwd = rs.getString("password");
    //         ld = new LoginDto(oid, pwd, uname);
    //     }
    //     return ld;
    // }
}


