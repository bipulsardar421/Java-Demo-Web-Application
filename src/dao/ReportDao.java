// package dao;

// import java.sql.Connection;
// import java.sql.Date;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// import org.json.JSONArray;

// import controller.JdbcApp;
// import dao.interfaces.ReportInterface;
// import handler.resultset_handler.JsonResultset;

// public class ReportDao implements ReportInterface {

//     @Override
//     public JSONArray getReports(Date startDate, Date endDate, String role, int userId) throws SQLException {
//         Connection con = JdbcApp.getConnection();
//         String sql = "";

//         switch (role.toLowerCase()) {
//             case "client" -> sql = getClientReportQuery();
//             case "vendor" -> sql = getVendorReportQuery();
//             case "admin" -> sql = getAdminReportQuery();
//         }

//         PreparedStatement ps = con.prepareStatement(sql);
//         ps.setDate(1, startDate);
//         ps.setDate(2, endDate);
//         if (!role.equalsIgnoreCase("admin")) {
//             ps.setInt(3, userId);
//         }

//         ResultSet rs = ps.executeQuery();
//         return JsonResultset.convertToJson(rs);
//     }

//     private String getClientReportQuery() {
//         return """
//                     SELECT i.invoice_id, i.invoice_date, p.product_name, ii.quantity, ii.total_price
//                     FROM invoice i
//                     JOIN invoice_item ii ON i.invoice_id = ii.invoice_id
//                     JOIN stock p ON ii.product_id = p.product_id
//                     JOIN user_details u ON i.customer_name = u.user_name
//                     WHERE i.invoice_date BETWEEN ? AND ?
//                     AND u.user_id = ?
//                 """;
//     }

//     private String getVendorReportQuery() {
//         return """
//                     SELECT p.product_name, ii.quantity, ii.total_price, i.invoice_date
//                     FROM invoice_item ii
//                     JOIN invoice i ON ii.invoice_id = i.invoice_id
//                     JOIN stock p ON ii.product_id = p.product_id
//                     WHERE p.vendor_id = ?
//                     AND i.invoice_date BETWEEN ? AND ?
//                 """;
//     }

//     private String getAdminReportQuery() {
//         return """
//                     SELECT i.invoice_id, u.user_name AS customer_name, p.product_name, ii.quantity, ii.total_price, i.invoice_date
//                     FROM invoice i
//                     JOIN invoice_item ii ON i.invoice_id = ii.invoice_id
//                     JOIN stock p ON ii.product_id = p.product_id
//                     JOIN user_details u ON i.customer_name = u.user_name
//                     WHERE i.invoice_date BETWEEN ? AND ?
//                 """;
//     }
// }

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;

public class ReportDao {

    // 1. Stock Report
    public JSONArray getStockReport(String startDate, String endDate) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray stockArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            String query = "SELECT s.product_id, s.product_name, s.quantity AS stock_on_hand, s.rate, " +
                          "s.recieved_date, v.vendor_name AS supplier " +
                          "FROM stock s " +
                          "LEFT JOIN vendors v ON s.vendor_id = v.vendor_id " +
                          "WHERE s.recieved_date BETWEEN ? AND ? AND s.status = 'active' " +
                          "ORDER BY s.recieved_date, s.product_name";
            ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject stockJson = new JSONObject();
                stockJson.put("product_id", rs.getInt("product_id"));
                stockJson.put("product_name", rs.getString("product_name"));
                stockJson.put("stock_on_hand", rs.getInt("stock_on_hand"));
                stockJson.put("rate", rs.getDouble("rate"));
                stockJson.put("recieved_date", rs.getDate("recieved_date").toString());
                stockJson.put("supplier", rs.getString("supplier"));
                stockArray.put(stockJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return stockArray;
    }

    // 2. Vendor Report
    public JSONArray getVendorReport(String startDate, String endDate) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray vendorArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            String query = "SELECT v.vendor_id, v.vendor_name, v.contact_person, v.phone, " +
                          "s.product_name, s.quantity AS supplied_quantity, s.rate, s.recieved_date " +
                          "FROM vendors v " +
                          "LEFT JOIN stock s ON v.vendor_id = s.vendor_id " +
                          "WHERE s.recieved_date BETWEEN ? AND ? AND s.status = 'active' " +
                          "ORDER BY v.vendor_name, s.recieved_date";
            ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject vendorJson = new JSONObject();
                vendorJson.put("vendor_id", rs.getInt("vendor_id"));
                vendorJson.put("vendor_name", rs.getString("vendor_name"));
                vendorJson.put("contact_person", rs.getString("contact_person"));
                vendorJson.put("phone", rs.getString("phone"));
                vendorJson.put("product_name", rs.getString("product_name"));
                vendorJson.put("supplied_quantity", rs.getInt("supplied_quantity"));
                vendorJson.put("rate", rs.getDouble("rate"));
                vendorJson.put("recieved_date", rs.getDate("recieved_date").toString());
                vendorArray.put(vendorJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return vendorArray;
    }

    // 3.1. Date-Wise Invoice Report
    public JSONArray getDateWiseInvoiceReport(String startDate, String endDate) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray invoiceArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            String query = "SELECT i.invoice_date, COUNT(i.invoice_id) AS invoice_count, " +
                          "SUM(i.grand_total) AS total_amount, i.payment_status, " +
                          "GROUP_CONCAT(i.invoice_id) AS invoice_ids " +
                          "FROM invoice i " +
                          "WHERE i.invoice_date BETWEEN ? AND ? " +
                          "GROUP BY i.invoice_date, i.payment_status " +
                          "ORDER BY i.invoice_date";
            ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject invoiceJson = new JSONObject();
                invoiceJson.put("invoice_date", rs.getDate("invoice_date").toString());
                invoiceJson.put("invoice_count", rs.getInt("invoice_count"));
                invoiceJson.put("total_amount", rs.getDouble("total_amount"));
                invoiceJson.put("payment_status", rs.getString("payment_status"));
                invoiceJson.put("invoice_ids", rs.getString("invoice_ids"));
                invoiceArray.put(invoiceJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return invoiceArray;
    }

    // 3.2. Client-Wise Invoice Report
    public JSONArray getClientWiseInvoiceReport(String startDate, String endDate) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray invoiceArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            String query = "SELECT c.client_id, c.client_name, COUNT(i.invoice_id) AS invoice_count, " +
                          "SUM(i.grand_total) AS total_amount, i.payment_status, " +
                          "GROUP_CONCAT(i.invoice_id) AS invoice_ids " +
                          "FROM invoice i " +
                          "LEFT JOIN clients c ON i.client_id = c.client_id " +
                          "WHERE i.invoice_date BETWEEN ? AND ? " +
                          "GROUP BY c.client_id, c.client_name, i.payment_status " +
                          "ORDER BY c.client_name";
            ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject invoiceJson = new JSONObject();
                invoiceJson.put("client_id", rs.getInt("client_id"));
                invoiceJson.put("client_name", rs.getString("client_name") != null ? rs.getString("client_name") : "Unknown");
                invoiceJson.put("invoice_count", rs.getInt("invoice_count"));
                invoiceJson.put("total_amount", rs.getDouble("total_amount"));
                invoiceJson.put("payment_status", rs.getString("payment_status"));
                invoiceJson.put("invoice_ids", rs.getString("invoice_ids"));
                invoiceArray.put(invoiceJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return invoiceArray;
    }

    // 3.3. Product-Wise Invoice Report
    public JSONArray getProductWiseInvoiceReport(String startDate, String endDate) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray invoiceArray = new JSONArray();
        try {
            con = JdbcApp.getConnection();
            String query = "SELECT s.product_id, s.product_name, SUM(ii.quantity) AS total_quantity_sold, " +
                          "SUM(ii.total_price) AS total_amount, COUNT(DISTINCT ii.invoice_id) AS invoice_count " +
                          "FROM invoice_item ii " +
                          "JOIN invoice i ON ii.invoice_id = i.invoice_id " +
                          "JOIN stock s ON ii.product_id = s.product_id " +
                          "WHERE i.invoice_date BETWEEN ? AND ? " +
                          "GROUP BY s.product_id, s.product_name " +
                          "ORDER BY s.product_name";
            ps = con.prepareStatement(query);
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject invoiceJson = new JSONObject();
                invoiceJson.put("product_id", rs.getInt("product_id"));
                invoiceJson.put("product_name", rs.getString("product_name"));
                invoiceJson.put("total_quantity_sold", rs.getInt("total_quantity_sold"));
                invoiceJson.put("total_amount", rs.getDouble("total_amount"));
                invoiceJson.put("invoice_count", rs.getInt("invoice_count"));
                invoiceArray.put(invoiceJson);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        return invoiceArray;
    }
}
