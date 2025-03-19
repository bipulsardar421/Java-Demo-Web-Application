package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;
import dao.interfaces.UserDetailsInterface;
import dto.user_details.UserDetailsDto;
import handler.resultset_handler.JsonResultset;

public class UserDetailsDao implements UserDetailsInterface {

    @Override
    public UserDetailsDto getByName(String name) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "SELECT * FROM user_details WHERE user_name = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? new UserDetailsDto(rs) : null;
    }

    @Override
    public UserDetailsDto get(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "SELECT  u.id, u.user_id, r.role, u.user_name, u.phone, u.address, u.image FROM user_details u JOIN login r ON u.user_id = r.id WHERE u.user_id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return new UserDetailsDto(rs);
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "SELECT * FROM user_details WHERE status = 'active' and user_id in (select id from login where role = 'nuser' and status ='active')";
        PreparedStatement ps = con.prepareStatement(qry);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    public JSONArray getAllClients() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "SELECT * FROM user_details WHERE status = 'active' and user_id in (select id from login where role = 'client' and status ='active')";
        PreparedStatement ps = con.prepareStatement(qry);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }


    @Override
    public int insert(UserDetailsDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "INSERT INTO user_details (user_name, phone, address, image, status, updatedAt, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, t.getUser_name());
        ps.setString(2, t.getPhone() + "");
        ps.setString(3, t.getAddress());
        ps.setString(4, t.getImage());
        ps.setString(5, t.getStatus());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, t.getUser_id());
        return ps.executeUpdate();
    }

    @Override
    public int update(UserDetailsDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "UPDATE user_details SET user_name = ?, phone = ?, address = ?, image = ?, status = ?, updatedAt = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setString(1, t.getUser_name());
        ps.setString(2, t.getPhone() + "");
        ps.setString(3, t.getAddress());
        ps.setString(4, t.getImage());
        ps.setString(5, t.getStatus());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setInt(7, t.getId());
        return ps.executeUpdate();
    }

    @Override
    public int delete(UserDetailsDto t) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String qry = "UPDATE user_details SET status = 'inactive' WHERE user_id = ?";
        PreparedStatement ps = con.prepareStatement(qry);
        ps.setInt(1, t.getUser_id());
        return ps.executeUpdate();
    }

    public String getImageName(int id) throws SQLException {
        String img = "";
        String query = "SELECT image FROM user_details WHERE id = ?";
        try (Connection con = JdbcApp.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    img = rs.getString("image");
                }
            }
        }
        return img;
    }

    @Override
    public int save(UserDetailsDto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    public JSONArray checkUsername(String username) throws SQLException {
        String query = "SELECT user_name FROM user_details WHERE user_name = ?";
        JSONArray resultArray = new JSONArray();

        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("user_name", rs.getString("user_name"));
                    resultArray.put(obj);
                }
            }
        }
        return resultArray;
    }
    public JSONArray checkPhone(String username) throws SQLException {
        String query = "SELECT phone FROM user_details WHERE phone = ?";
        JSONArray resultArray = new JSONArray();

        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", rs.getString("phone"));
                    resultArray.put(obj);
                }
            }
        }
        return resultArray;
    }
}
