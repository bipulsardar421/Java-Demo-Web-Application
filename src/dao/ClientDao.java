package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;
import dao.interfaces.ClientInterface;
import dto.ClientDto;

public class ClientDao implements ClientInterface {

    @Override
    public ClientDto getByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClientDto client = null;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM clients WHERE client_name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                client = new ClientDto(rs.getInt("client_id"), rs.getString("client_name"),
                        rs.getString("contact_person"), rs.getString("address"), rs.getString("phone"),
                        rs.getDate("created_at"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return client;
    }

    @Override
    public ClientDto get(int id) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ClientDto client = null;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM clients WHERE client_id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                client = new ClientDto(rs.getInt("client_id"), rs.getString("client_name"),
                        rs.getString("contact_person"), rs.getString("address"), rs.getString("phone"),
                        rs.getDate("created_at"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return client;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray clientsArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM clients");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject clientJson = new JSONObject();
                clientJson.put("client_id", rs.getInt("client_id"));
                clientJson.put("client_name", rs.getString("client_name"));
                clientJson.put("contact_person", rs.getString("contact_person"));
                clientJson.put("address", rs.getString("address"));
                clientJson.put("phone", rs.getString("phone"));
                clientJson.put("created_at", rs.getDate("created_at"));
                clientsArray.put(clientJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return clientsArray;
    }

    @Override
    public int save(ClientDto t) throws SQLException {
        if (t.getClient_id() == 0) { // New client, insert
            return insert(t);
        } else { // Existing client, update
            return update(t);
        }
    }

    @Override
    public int insert(ClientDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO clients (client_name, contact_person, address, phone) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getClient_name());
            ps.setString(2, t.getContact_person());
            ps.setString(3, t.getAddress());
            ps.setString(4, t.getPhone());
            rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    t.setClient_id(generatedKeys.getInt(1)); // Update DTO with generated ID
                }
            }
        } finally {
            if (generatedKeys != null) generatedKeys.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return rowsAffected;
    }

    @Override
    public int update(ClientDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement(
                "UPDATE clients SET client_name = ?, contact_person = ?, address = ?, phone = ? WHERE client_id = ?");
            ps.setString(1, t.getClient_name());
            ps.setString(2, t.getContact_person());
            ps.setString(3, t.getAddress());
            ps.setString(4, t.getPhone());
            ps.setInt(5, t.getClient_id());
            rowsAffected = ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return rowsAffected;
    }

    @Override
    public int delete(ClientDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("DELETE FROM clients WHERE client_id = ?");
            ps.setInt(1, t.getClient_id());
            rowsAffected = ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return rowsAffected;
    }
}