package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;
import dao.interfaces.VendorInterface;
import dto.VendorDto;

public class VendorDao implements VendorInterface {

    @Override
    public VendorDto getByName(String name) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendorDto vendor = null;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM vendors WHERE vendor_name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                vendor = new VendorDto(rs.getInt("vendor_id"), rs.getString("vendor_name"),
                        rs.getString("contact_person"), rs.getString("address"), rs.getString("phone"),
                        rs.getDate("created_at"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return vendor;
    }

    @Override
    public VendorDto get(int id) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        VendorDto vendor = null;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM vendors WHERE vendor_id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                vendor = new VendorDto(rs.getInt("vendor_id"), rs.getString("vendor_name"),
                        rs.getString("contact_person"), rs.getString("address"), rs.getString("phone"),
                        rs.getDate("created_at"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return vendor;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray vendorsArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("SELECT * FROM vendors");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject vendorJson = new JSONObject();
                vendorJson.put("vendor_id", rs.getInt("vendor_id"));
                vendorJson.put("vendor_name", rs.getString("vendor_name"));
                vendorJson.put("contact_person", rs.getString("contact_person"));
                vendorJson.put("address", rs.getString("address"));
                vendorJson.put("phone", rs.getString("phone"));
                vendorJson.put("created_at", rs.getDate("created_at"));
                vendorsArray.put(vendorJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return vendorsArray;
    }

    @Override
    public int save(VendorDto t) throws SQLException {
        if (t.getVendor_id() == 0) { // New vendor, insert
            return insert(t);
        } else { // Existing vendor, update
            return update(t);
        }
    }

    @Override
    public int insert(VendorDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO vendors (vendor_name, contact_person, address, phone) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getVendor_name());
            ps.setString(2, t.getContact_person());
            ps.setString(3, t.getAddress());
            ps.setString(4, t.getPhone());
            rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    t.setVendor_id(generatedKeys.getInt(1)); // Update DTO with generated ID
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
    public int update(VendorDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement(
                "UPDATE vendors SET vendor_name = ?, contact_person = ?, address = ?, phone = ? WHERE vendor_id = ?");
            ps.setString(1, t.getVendor_name());
            ps.setString(2, t.getContact_person());
            ps.setString(3, t.getAddress());
            ps.setString(4, t.getPhone());
            ps.setInt(5, t.getVendor_id());
            rowsAffected = ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return rowsAffected;
    }

    @Override
    public int delete(VendorDto t) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        int rowsAffected = 0;
        try {
            con = JdbcApp.getConnection();
            ps = con.prepareStatement("DELETE FROM vendors WHERE vendor_id = ?");
            ps.setInt(1, t.getVendor_id());
            rowsAffected = ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return rowsAffected;
    }
}