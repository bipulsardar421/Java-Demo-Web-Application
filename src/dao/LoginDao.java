package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;

import controller.JdbcApp;
import dao.interfaces.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import handler.resultset_handler.JsonResultset;

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
    public JSONArray getAll() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "Select * from login";
        PreparedStatement ps = con.prepareStatement(qry);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    @Override
    public int insert(LoginDto t) throws SQLException {
        String qry = "INSERT INTO login (username, password, role) VALUES (?, ?, ?)";
        int generatedId = -1;
        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getUsername());
            ps.setString(2, LoginHelper.hashString(t.getPassword()));
            ps.setString(3, t.getRole());
            int result = ps.executeUpdate();
            if (result > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    public int otp(int user_id, String otp) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into otpvalidation(user_id, otp) value (?,?)");
        ps.setInt(1, user_id);
        ps.setString(2, otp);
        return ps.executeUpdate();
    }

    public boolean checkOtp(int user_id, String otp) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("select status from otpvalidation where user_id = ? and otp = ?");
        ps.setInt(1, user_id);
        ps.setString(2, otp);
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getString("status").equals("active")) {
            return true;
        } else
            return false;
    }

    public boolean deleteLoginOtp(int user_id) throws SQLException {
        String selectQuery = "SELECT id, otp FROM otpvalidation WHERE user_id = ? AND status = 'active'";
        String updateQuery = "UPDATE otpvalidation SET status = 'inactive' WHERE id = ?";
        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(selectQuery)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String otp = rs.getString("otp");
                    if (otp != null) {
                        try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                            updatePs.setInt(1, rs.getInt("id"));

                            updatePs.executeUpdate();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int signUpOtp(String uname, String otp) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into signupvalidation (email, otp) value (?,?)");
        ps.setString(1, uname);
        ps.setString(2, otp);
        return ps.executeUpdate();
    }

    public boolean checkSignUpOtp(String uname, String otp) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("select status from signupvalidation where email = ? and otp = ?");
        ps.setString(1, uname);
        ps.setString(2, otp);
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getString("status").equals("active")) {
            return true;
        } else
            return false;
    }

    public boolean deleteSignUpOtp(String uname, String otpSecret) throws SQLException {
        String selectQuery = "SELECT id, otp FROM signupvalidation WHERE email = ? AND status = 'active'";
        String updateQuery = "UPDATE signupvalidation SET status = 'inactive' WHERE id = ?";
        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(selectQuery)) {
            ps.setString(1, uname);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("coming");
                if (rs.next()) {
                    String otp = rs.getString("otp");
                    if (otp != null) {
                        try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                            System.out.println("delete");
                            updatePs.setInt(1, rs.getInt("id"));
                            updatePs.executeUpdate();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int update(LoginDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        PreparedStatement ps = con.prepareStatement("update login set password = ? where id = ?");
        ps.setString(1, t.getPassword());
        ps.setInt(2, t.getId());
        int result = ps.executeUpdate();
        if (result > 0) {
            return result;
        } else
            return 0;
    }

    @Override
    public int save(LoginDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public int delete(LoginDto t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public LoginDto get(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public ResultSet getUsersData(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "select l.id as uid, u.user_name as name, l.username as email,  l.role as role from user_details u join login l on u.user_id = l.id where l.id not in (?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        return rs;
    }

    @Override
    public int[] editUserRole(JSONArray jdata) throws SQLException {
        String query = "UPDATE login SET role=? WHERE id=?";
        Connection con = null;
        try {
            con = JdbcApp.getConnection(); 
            con.setAutoCommit(false);
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                for (int i = 0; i < jdata.length(); i++) {
                    pstmt.setString(1, jdata.getJSONObject(i).getString("role"));
                    pstmt.setInt(2, jdata.getJSONObject(i).getInt("id"));
                    pstmt.addBatch();
                }
                int[] updateCounts = pstmt.executeBatch();
                con.commit();
                return updateCounts;
            }
        } catch (SQLException e) {
            if (con != null) {
                con.rollback();
            }
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

}
