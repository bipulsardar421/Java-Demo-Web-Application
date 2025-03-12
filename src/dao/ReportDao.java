package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import controller.JdbcApp;
import dao.interfaces.ReportInterface;
import handler.resultset_handler.JsonResultset;

public class ReportDao implements ReportInterface {

    @Override
    public JSONArray generateReport(Date startDate, Date endDate) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String sql = "WITH InvoiceData AS (  SELECT invoice_id  FROM invoice where invoice_date between ? and ?), ItemQuantity AS (  SELECT ii.product_id, SUM(ii.quantity) AS total_quantity  FROM invoice_item ii  JOIN InvoiceData i ON ii.invoice_id = i.invoice_id  GROUP BY ii.product_id), MaxProduct AS (  SELECT product_id, total_quantity  FROM ItemQuantity  ORDER BY total_quantity DESC  LIMIT 1)SELECT   s.product_name,   mp.product_id,   mp.total_quantity FROM stock s JOIN MaxProduct mp ON s.product_id = mp.product_id";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, startDate);
        ps.setDate(2, endDate);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    public JSONObject getCombinedReport(Date startDate, Date endDate) throws SQLException {
        JSONObject combinedResult = new JSONObject();

        JSONArray reportData = generateReport(startDate, endDate);
        JSONArray maxInvoiceData = getMaxGeneratedInvoice(startDate, endDate);
        JSONArray maxCustomerData = getMaxCustomer();

        combinedResult.put("reportData", reportData);
        combinedResult.put("maxCustomerData", maxCustomerData);
        combinedResult.put("maxInvoiceData", maxInvoiceData);

        return combinedResult;
    }

    public JSONArray getMaxCustomer() throws SQLException {
        Connection con = JdbcApp.getConnection();
        String sql = "SELECT customer_name, COUNT(invoice_id) AS invoice_count FROM invoice GROUP BY customer_name ORDER BY invoice_count DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

    public JSONArray getMaxGeneratedInvoice(Date starDate, Date enDate) throws SQLException {
        Connection con = JdbcApp.getConnection();
        String sql = "SELECT invoice_date, COUNT(invoice_id) AS total_invoices \n" + //
                "FROM invoice \n" + //
                "WHERE invoice_date BETWEEN ? AND ? \n" + //
                "GROUP BY invoice_date \n" + //
                "ORDER BY total_invoices DESC \n" + //
                "LIMIT 1;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, starDate);
        ps.setDate(2, enDate);
        ResultSet rs = ps.executeQuery();
        return JsonResultset.convertToJson(rs);
    }

}
