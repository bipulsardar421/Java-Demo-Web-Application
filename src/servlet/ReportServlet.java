package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ReportDao;
import handler.response_handler.ResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

@MultipartConfig()
@WebServlet("/report/*")
public class ReportServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReportServlet.class.getName());
    private ReportDao reportDao;

    @Override
    public void init() {
        // Initialize ReportDao once when servlet starts
        reportDao = new ReportDao();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");

        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");

        try {
            switch (path) {
                case "/generate" -> handleGenerateAllReports(req, res);
                default -> {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(res, "error", "Invalid API Endpoint");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing report", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResponseHandler.sendJsonResponse(res, "error", "Internal Server Error");
        }
    }

    /**
     * Handles generation of all reports (Stock, Vendor, and all Invoice types) in a single JSON object.
     */
    private void handleGenerateAllReports(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            // Extract parameters from request
            String startDate = req.getParameter("startDate"); 
            String endDate = req.getParameter("endDate");

            // Validate parameters
            if (startDate == null || endDate == null) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                ResponseHandler.sendJsonResponse(res, "error", "Missing required parameters: startDate, endDate");
                return;
            }

            // Create a single JSONObject to hold all reports
            JSONObject allReports = new JSONObject();

            // Fetch all reports
            JSONArray stockReport = reportDao.getStockReport(startDate, endDate);
            JSONArray vendorReport = reportDao.getVendorReport(startDate, endDate);
            JSONArray dateWiseInvoiceReport = reportDao.getDateWiseInvoiceReport(startDate, endDate);
            JSONArray clientWiseInvoiceReport = reportDao.getClientWiseInvoiceReport(startDate, endDate);
            JSONArray productWiseInvoiceReport = reportDao.getProductWiseInvoiceReport(startDate, endDate);

            // Add each report to the main JSONObject
            allReports.put("stock_report", stockReport);
            allReports.put("vendor_report", vendorReport);
            allReports.put("invoice_date_report", dateWiseInvoiceReport);
            allReports.put("invoice_client_report", clientWiseInvoiceReport);
            allReports.put("invoice_product_report", productWiseInvoiceReport);
            ResponseHandler.sendJsonResponse(res, "success",  allReports);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while generating reports", e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }
}