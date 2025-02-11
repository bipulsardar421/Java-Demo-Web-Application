package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import controller.JdbcApp;
import dao.interfaces.login.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import resultset_handler.JsonResultset;

public class LoginDao implements LoginInterface {

    @Override
    public LoginDto getByName(String name) throws SQLException {
        Connection con = JdbcApp.getConnection();
        LoginDto ld = null;
        String qry = "Select id, username, password, role from login where username = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int oid = rs.getInt("id");
            String uname = rs.getString("username");
            String pwd = rs.getString("password");
            String role = rs.getString("role");
            ld = new LoginDto(oid, uname, pwd, role);
        }
        return ld;
    }

    @Override
    public JSONArray get(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "Select * from login";
        PreparedStatement ps = con.prepareStatement(qry);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    @Override
    public int save(LoginDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(LoginDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "insert into login (username, password, role) value (?,?,?)";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, t.getUsername());
        ps.setString(2, LoginHelper.hashString(t.getPassword()));
        ps.setString(3, t.getRole());
        int result = ps.executeUpdate();
        return result;
    }

    @Override
    public int update(LoginDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(LoginDto t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
