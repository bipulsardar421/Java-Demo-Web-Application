package dao;

import dao.interfaces.InvoiceInterface;
import dao.interfaces.InvoiceItemInterface;
import dto.invoice.InvoiceDto;
import handler.resultset_handler.JsonResultset;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controller.JdbcApp;

public class InvoiceDao implements InvoiceInterface, InvoiceItemInterface<InvoiceDto> {

    public JSONArray getWithUserId(long phone) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE customer_contact = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, phone);
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

    public JSONArray search(long phone, long searchParam) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT * FROM invoice WHERE customer_contact = ? AND invoice_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, phone);
            stmt.setLong(2, searchParam);
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
        String query = "SELECT i.item_id, i.product_id, s.product_name, s.rate, i.quantity, i.unit_price, i.total_price FROM invoice_item i JOIN stock s ON i.product_id = s.product_id WHERE invoice_id = ?";
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
        try (PreparedStatement stmt = con.prepareStatement(query)) {
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

    @Override
    public int save(InvoiceDto invoice) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "INSERT INTO invoice (customer_name, customer_contact, invoice_date, total_amount, discount, tax, grand_total, payment_status, payment_method, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, invoice.getCustomerName());
            stmt.setString(2, invoice.getCustomer_contact());
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
            stmt.setString(2, invoice.getCustomer_contact());
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
    public int[] addInvoiceItems(JSONArray d, int invoice_id) throws JSONException, SQLException {
        Connection conn = JdbcApp.getConnection();
        String sql = "INSERT INTO invoice_item (product_id, quantity, unit_price, total_price, invoice_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        conn.setAutoCommit(false);
        for (int i = 0; i < d.length(); i++) {
            updateStockDetails(d.getJSONObject(i).getInt("productId"), d.getJSONObject(i).getInt("quantity"));
            stmt.setInt(1, d.getJSONObject(i).getInt("productId"));
            stmt.setInt(2, d.getJSONObject(i).getInt("quantity"));
            stmt.setDouble(3, d.getJSONObject(i).getDouble("price"));
            stmt.setDouble(4, d.getJSONObject(i).getInt("quantity") * d.getJSONObject(i).getDouble("price"));
            stmt.setInt(5, invoice_id);
            stmt.addBatch();
        }

        int result[] = stmt.executeBatch();
        conn.commit();
        return result;
    }

    public static void updateStockDetails(int productId, int quantity) throws SQLException {
        String selectQuery = "SELECT quantity FROM stock WHERE product_id = ?";
        String updateQuery = "UPDATE stock SET quantity = ? WHERE product_id = ?";

        try (Connection conn = JdbcApp.getConnection();
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            selectStmt.setInt(1, productId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity - quantity;

                if (newQuantity < 0) {
                    throw new SQLException("Insufficient stock! Available: " + currentQuantity);
                }
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, productId);
                updateStmt.executeUpdate();
            } else {
                throw new SQLException("Product not found with ID: " + productId);
            }
        }
    }

    public JSONArray vendorBill() {
        JSONArray js  = new JSONArray();;
        String query = "SELECT " +
                "i.item_id, " +
                "i.invoice_id, " +
                "u.user_name AS vendor, " +
                "s.product_name, " +
                "i.quantity, " +
                "main_invoice.customer_name, " +
                "main_invoice.customer_contact " +
                "FROM " +
                "invoice_item i " +
                "JOIN stock s ON s.product_id = i.product_id " +
                "JOIN user_details u ON u.user_id = s.vendor_id " +
                "JOIN invoice main_invoice ON i.invoice_id = main_invoice.invoice_id";
        try (Connection con = JdbcApp.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            js = JsonResultset.convertToJson(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return js;
    }
    public JSONArray getVendorBill(int vendorId) {
        String query = """
                SELECT 
                    i.item_id, 
                    i.invoice_id, 
                    u.user_name AS vendor, 
                    s.product_name, 
                    i.quantity, 
                    main_invoice.customer_name, 
                    main_invoice.customer_contact
                FROM 
                    invoice_item i
                JOIN stock s ON s.product_id = i.product_id
                JOIN user_details u ON u.user_id = s.vendor_id
                JOIN invoice main_invoice ON i.invoice_id = main_invoice.invoice_id
                WHERE s.vendor_id = ?
                """;
    
        try (Connection connection = JdbcApp.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, vendorId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return JsonResultset.convertToJson(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new JSONArray();
        }
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
