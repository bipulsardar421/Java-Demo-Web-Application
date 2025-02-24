package dao;

import dao.interfaces.InvoiceInterface;
import dao.interfaces.InvoiceItemInterface;
import dto.invoice.InvoiceDto;
import handler.resultset_handler.JsonResultset;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;

public class InvoiceDao implements InvoiceInterface, InvoiceItemInterface<InvoiceDto> {

    public JSONArray getWithUserId(int userId) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,  userId );
            try (ResultSet rs = stmt.executeQuery()) {
                JSONArray invoiceJsonArray = JsonResultset.convertToJson(rs);
                for (int i = 0; i < invoiceJsonArray.length(); i++) {
                    JSONObject invoiceObject = invoiceJsonArray.getJSONObject(i);
                    int invoiceId = invoiceObject.getInt("invoice_id");
                    JSONArray items = invoiceRecord(invoiceId);
                    invoiceObject.put("items", items);
                }
                
                return invoiceJsonArray;
            }
        }
    }

    public JSONArray invoiceRecord(int invoiceId) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice_item WHERE invoice_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                return JsonResultset.convertToJson(rs);
            }
        }
    }

    @Override
    public InvoiceDto get(int id) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new InvoiceDto(rs);
                }
            }
        }
        return null;
    }

    @Override
    public JSONArray getAll() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                return JsonResultset.convertToJson(rs);
            }
        }
        return null;
    }

    @Override
    public int save(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "INSERT INTO invoice (customer_name, customer_contact, invoice_date, total_amount, discount, tax, grand_total, payment_status, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, invoice.getCustomerName());
            stmt.setInt(2, invoice.getCustomer_contact());
            stmt.setDate(3, invoice.getInvoiceDate());
            stmt.setDouble(4, invoice.getTotalAmount());
            stmt.setDouble(5, invoice.getDiscount());
            stmt.setDouble(6, invoice.getTax());
            stmt.setDouble(7, invoice.getGrand_total());
            stmt.setString(8, invoice.getPayment_status());
            stmt.setString(9, invoice.getPaymentMethod());
            stmt.setString(10, invoice.getNotes());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public int insert(InvoiceDto invoice) throws SQLException {
        return save(invoice);
    }

    @Override
    public int update(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "UPDATE invoice SET customer_name = ?, customer_contact = ?, invoice_date = ?, total_amount = ?, discount = ?, tax = ?, grand_total = ?, payment_status = ?, payment_method = ?, notes = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, invoice.getCustomerName());
            stmt.setInt(2, invoice.getCustomer_contact());
            stmt.setDate(3, invoice.getInvoiceDate());
            stmt.setDouble(4, invoice.getTotalAmount());
            stmt.setDouble(5, invoice.getDiscount());
            stmt.setDouble(6, invoice.getTax());
            stmt.setDouble(7, invoice.getGrand_total());
            stmt.setString(8, invoice.getPayment_status());
            stmt.setString(9, invoice.getPaymentMethod());
            stmt.setString(10, invoice.getNotes());
            stmt.setInt(11, invoice.getId());
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

    @Override
    public InvoiceDto addInvoiceItems(InvoiceDto d) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addInvoiceItems'");
    }

    @Override
    public InvoiceDto deleteInvoiceItems(InvoiceDto d) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInvoiceItems'");
    }

    @Override
    public InvoiceDto updateInvoiceItems(InvoiceDto d) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInvoiceItems'");
    }

    @Override
    public InvoiceDto getByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByName'");
    }

    @Override
    public ResultSet getInvoiceItems(int invoice_id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInvoiceItems'");
    }
}
