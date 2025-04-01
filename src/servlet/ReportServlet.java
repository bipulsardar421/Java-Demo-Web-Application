package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ReportDao;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDao reportDao;

    @Override
    public void init() throws ServletException {
        reportDao = new ReportDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read JSON payload
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        try {
            String rawPayload = sb.toString();
            System.out.println("Raw Payload: " + rawPayload); // Debug log

            JSONObject jsonRequest = new JSONObject(rawPayload);
            String reportType = jsonRequest.optString("reportType", null);

            if (reportType == null || reportType.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Report type is required\"}");
                return;
            }

            JSONArray result = null;

            switch (reportType.toLowerCase()) {
                case "stock":
                    String productName = jsonRequest.optString("productName", null);
                    String date = jsonRequest.optString("date", null);
                    result = reportDao.getStockReport(productName, date);
                    break;

                case "vendor":
                    String vendorName = jsonRequest.optString("vendorName", null);
                    result = reportDao.getVendorReport(vendorName);
                    break;

                case "invoice":
                    String invoiceDate = jsonRequest.optString("date", null);
                    String clientName = jsonRequest.optString("clientName", null);
                    String invoiceProductName = jsonRequest.optString("productName", null);
                    result = reportDao.getInvoiceReport(invoiceDate, clientName, invoiceProductName);
                    break;

                case "allstock":
                    result = reportDao.getAllStock();
                    break;

                case "allvendorssupplied":
                    result = reportDao.getAllVendorsSupplied();
                    break;

                case "customersbydaterange":
                    String startDate = jsonRequest.optString("startDate", null);
                    String endDate = jsonRequest.optString("endDate", null);
                    if (startDate == null || endDate == null) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.print("{\"error\": \"Start date and end date are required for this report\"}");
                        return;
                    }
                    result = reportDao.getCustomersByDateRange(startDate, endDate);
                    break;

                case "mostsoldproduct":
                    String mostSoldStartDate = jsonRequest.optString("startDate", null);
                    String mostSoldEndDate = jsonRequest.optString("endDate", null);
                    result = reportDao.getMostSoldProduct(mostSoldStartDate, mostSoldEndDate);
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid report type: " + reportType + "\"}");
                    return;
            }

            out.print(result != null ? result.toString() : "[]");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error: " + e.getMessage() + "\"}");
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid request payload: " + e.getMessage() + "\"}");
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().print("{\"error\": \"GET method not supported. Use POST instead.\"}");
    }
}