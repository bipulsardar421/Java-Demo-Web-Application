package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;

import controller.JdbcApp;
import dao.interfaces.ReportInterface;
import handler.resultset_handler.JsonResultset;

public class ReportDao implements ReportInterface {

    @Override
    public JSONArray getReports(Date startDate, Date endDate, String role, int userId) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String sql = "";

        switch (role.toLowerCase()) {
            case "client" -> sql = getClientReportQuery();
            case "vendor" -> sql = getVendorReportQuery();
            case "admin" -> sql = getAdminReportQuery();
        }

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, startDate);
        ps.setDate(2, endDate);
        if (!role.equalsIgnoreCase("admin")) {
            ps.setInt(3, userId);
        }

        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    private String getClientReportQuery() {
        return """
                    SELECT i.invoice_id, i.invoice_date, p.product_name, ii.quantity, ii.total_price
                    FROM invoice i
                    JOIN invoice_item ii ON i.invoice_id = ii.invoice_id
                    JOIN stock p ON ii.product_id = p.product_id
                    JOIN user_details u ON i.customer_name = u.user_name
                    WHERE i.invoice_date BETWEEN ? AND ?
                    AND u.user_id = ?
                """;
    }

    private String getVendorReportQuery() {
        return """
                    SELECT p.product_name, ii.quantity, ii.total_price, i.invoice_date
                    FROM invoice_item ii
                    JOIN invoice i ON ii.invoice_id = i.invoice_id
                    JOIN stock p ON ii.product_id = p.product_id
                    WHERE p.vendor_id = ?
                    AND i.invoice_date BETWEEN ? AND ?
                """;
    }

    private String getAdminReportQuery() {
        return """
                    SELECT i.invoice_id, u.user_name AS customer_name, p.product_name, ii.quantity, ii.total_price, i.invoice_date
                    FROM invoice i
                    JOIN invoice_item ii ON i.invoice_id = ii.invoice_id
                    JOIN stock p ON ii.product_id = p.product_id
                    JOIN user_details u ON i.customer_name = u.user_name
                    WHERE i.invoice_date BETWEEN ? AND ?
                """;
    }
}
