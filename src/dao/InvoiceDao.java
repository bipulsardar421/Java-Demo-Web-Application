package dao;

import dao.interfaces.InvoiceInterface;
import dto.invoice.InvoiceDto;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;

public class InvoiceDao implements InvoiceInterface {

    @Override
    public InvoiceDto getByName(String name) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE customer_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInvoiceDto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public InvoiceDto get(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInvoiceDto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = JdbcApp.getConnection();
        JSONArray invoicesArray = new JSONArray();
        String query = "SELECT * FROM invoice";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                JSONObject invoiceJson = new JSONObject();
                invoiceJson.put("id", rs.getInt("id"));
                invoiceJson.put("customer_name", rs.getString("customer_name"));
                invoiceJson.put("total_amount", rs.getDouble("total_amount"));
                invoiceJson.put("payment_method", rs.getString("payment_method"));
                invoiceJson.put("notes", rs.getString("notes"));
                invoiceJson.put("invoice_date", rs.getDate("invoice_date"));
                invoiceJson.put("created_at", rs.getTimestamp("created_at"));
                invoicesArray.put(invoiceJson);
            }
        }
        return invoicesArray;
    }

    @Override
    public int save(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "INSERT INTO invoice (customer_name, total_amount, payment_method, notes, invoice_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, invoice.getCustomerName());
            stmt.setDouble(2, invoice.getTotalAmount());
            stmt.setString(3, invoice.getPaymentMethod());
            stmt.setString(4, invoice.getNotes());
            stmt.setDate(5, Date.valueOf(invoice.getInvoiceDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated ID
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int insert(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        return save(invoice); // Same as save, so we can reuse the same method
    }

    @Override
    public int update(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "UPDATE invoice SET customer_name = ?, total_amount = ?, payment_method = ?, notes = ?, invoice_date = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, invoice.getCustomerName());
            stmt.setDouble(2, invoice.getTotalAmount());
            stmt.setString(3, invoice.getPaymentMethod());
            stmt.setString(4, invoice.getNotes());
            stmt.setDate(5, Date.valueOf(invoice.getInvoiceDate()));
            stmt.setInt(6, invoice.getId());

            return stmt.executeUpdate();
        }
    }

    @Override
    public int delete(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "DELETE FROM invoice WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, invoice.getId());
            return stmt.executeUpdate();
        }
    }

    private InvoiceDto mapRowToInvoiceDto(ResultSet rs) throws SQLException {
        Connection con = JdbcApp.getConnection();
        InvoiceDto invoice = new InvoiceDto();
        invoice.setId(rs.getInt("id"));
        invoice.setCustomerName(rs.getString("customer_name"));
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setNotes(rs.getString("notes"));
        invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
        invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return invoice;
    }
}
