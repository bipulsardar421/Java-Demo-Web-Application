package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import controller.JdbcApp;
import handler.resultset_handler.JsonResultset;

public class ReportDao {

    // Existing methods (unchanged)
    public JSONArray getStockReport(String productName, String date) throws SQLException {
        Connection con = JdbcApp.getConnection();
        StringBuilder query = new StringBuilder(
            "SELECT product_id, product_name, quantity AS stock_on_hand, rate, recieved_date AS received_date, vendor_id, status " +
            "FROM stock WHERE status = 'active'"
        );
        if (productName != null && !productName.isEmpty()) query.append(" AND product_name LIKE ?");
        if (date != null && !date.isEmpty()) query.append(" AND recieved_date = ?");
        query.append(" ORDER BY recieved_date DESC");

        PreparedStatement ps = con.prepareStatement(query.toString());
        int paramIndex = 1;
        if (productName != null && !productName.isEmpty()) ps.setString(paramIndex++, "%" + productName + "%");
        if (date != null && !date.isEmpty()) ps.setString(paramIndex++, date);

        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    public JSONArray getVendorReport(String vendorName) throws SQLException {
        Connection con = JdbcApp.getConnection();
        StringBuilder query = new StringBuilder(
            "SELECT v.vendor_id, v.vendor_name, v.contact_person, v.phone, " +
            "s.product_id, s.product_name, s.quantity, s.rate, s.recieved_date AS received_date " +
            "FROM vendors v " +
            "LEFT JOIN stock s ON v.vendor_id = s.vendor_id " +
            "WHERE s.status = 'active'"
        );
        if (vendorName != null && !vendorName.isEmpty()) query.append(" AND v.vendor_name LIKE ?");
        query.append(" ORDER BY v.vendor_name, s.recieved_date DESC");

        PreparedStatement ps = con.prepareStatement(query.toString());
        if (vendorName != null && !vendorName.isEmpty()) ps.setString(1, "%" + vendorName + "%");

        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    public JSONArray getInvoiceReport(String date, String clientName, String productName) throws SQLException {
        Connection con = JdbcApp.getConnection();
        StringBuilder query = new StringBuilder(
            "SELECT i.invoice_id, i.invoice_date, c.client_id, c.client_name, i.customer_name, i.customer_contact, " +
            "ii.product_id, s.product_name, ii.quantity, ii.unit_price, ii.total_price, " +
            "i.total_amount, i.discount, i.tax, i.grand_total, i.payment_status, i.payment_method, i.notes " +
            "FROM invoice i " +
            "JOIN clients c ON i.client_id = c.client_id " +
            "JOIN invoice_item ii ON i.invoice_id = ii.invoice_id " +
            "JOIN stock s ON ii.product_id = s.product_id " +
            "WHERE 1=1"
        );
        if (date != null && !date.isEmpty()) query.append(" AND i.invoice_date = ?");
        if (clientName != null && !clientName.isEmpty()) query.append(" AND c.client_name LIKE ?");
        if (productName != null && !productName.isEmpty()) query.append(" AND s.product_name LIKE ?");
        query.append(" ORDER BY i.invoice_date DESC, c.client_name, s.product_name");

        PreparedStatement ps = con.prepareStatement(query.toString());
        int paramIndex = 1;
        if (date != null && !date.isEmpty()) ps.setString(paramIndex++, date);
        if (clientName != null && !clientName.isEmpty()) ps.setString(paramIndex++, "%" + clientName + "%");
        if (productName != null && !productName.isEmpty()) ps.setString(paramIndex++, "%" + productName + "%");

        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    // New methods for additional reports

    // 1. All Stock
    public JSONArray getAllStock() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT product_id, product_name, quantity AS stock_on_hand, rate, recieved_date AS received_date, vendor_id " +
                       "FROM stock WHERE status = 'active' ORDER BY product_name";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    // 2. All Vendors and What They Supplied (Product-wise Totals)
    public JSONArray getAllVendorsSupplied() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT v.vendor_id, v.vendor_name, s.product_id, s.product_name, " +
                       "SUM(s.quantity) AS total_supplied " +
                       "FROM vendors v " +
                       "LEFT JOIN stock s ON v.vendor_id = s.vendor_id " +
                       "WHERE s.status = 'active' " +
                       "GROUP BY v.vendor_id, v.vendor_name, s.product_id, s.product_name " +
                       "ORDER BY v.vendor_name, s.product_name";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    // 3. Customers with Invoices in a Date Range
    public JSONArray getCustomersByDateRange(String startDate, String endDate) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String query = "SELECT COUNT(DISTINCT i.client_id) AS customer_count, " +
                       "c.client_id, c.client_name " +
                       "FROM invoice i " +
                       "JOIN clients c ON i.client_id = c.client_id " +
                       "WHERE i.invoice_date BETWEEN ? AND ? " +
                       "GROUP BY c.client_id, c.client_name " +
                       "ORDER BY c.client_name";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, startDate);
        ps.setString(2, endDate);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    // 4. Most Sold Product (Optionally within a Date Range)
    public JSONArray getMostSoldProduct(String startDate, String endDate) throws SQLException {
        Connection con = JdbcApp.getConnection();
        StringBuilder query = new StringBuilder(
            "SELECT ii.product_id, s.product_name, SUM(ii.quantity) AS total_sold " +
            "FROM invoice_item ii " +
            "JOIN stock s ON ii.product_id = s.product_id " +
            "JOIN invoice i ON ii.invoice_id = i.invoice_id " +
            "WHERE 1=1"
        );
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            query.append(" AND i.invoice_date BETWEEN ? AND ?");
        }
        query.append(" GROUP BY ii.product_id, s.product_name " +
                     "ORDER BY total_sold DESC LIMIT 1");

        PreparedStatement ps = con.prepareStatement(query.toString());
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
        }
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }
}