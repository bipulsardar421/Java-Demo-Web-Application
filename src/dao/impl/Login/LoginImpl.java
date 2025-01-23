package dao.impl.login;

import controller.JdbcApp;
import dao.dto.LoginDto;
import dao.interfaces.login.LoginInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoginImpl implements LoginInterface {

    @Override
    public LoginDto get(int id) throws SQLException {

        Connection con = JdbcApp.getConnection();
        LoginDto ld = null;
        String qry = "Select id, username, password from login where id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int oid = rs.getInt("id");
            String uname = rs.getString("username");
            String pwd = rs.getString("password");
            ld = new LoginDto(oid, pwd, uname);
        }
        return ld;
    }

    @Override
    public List<LoginDto> getAll() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public int save(LoginDto loginDto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int insert(LoginDto loginDto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public int update(LoginDto loginDto) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public int delete(LoginDto loginDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
