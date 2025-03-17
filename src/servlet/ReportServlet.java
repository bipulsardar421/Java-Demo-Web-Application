package servlet;

import java.io.IOException;
import java.sql.Date;
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
import dao.interfaces.ReportInterface;
import handler.response_handler.ResponseHandler;

@MultipartConfig()
@WebServlet("/report/*")
public class ReportServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReportServlet.class.getName());
    private static final ReportInterface reportDao = new ReportDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");

        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");

        try {
            switch (path) {
                case "/generate" -> handleGenerateReport(req, res);
                default -> {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(res, "error", "Invalid API Endpoint");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing report", e);
            ResponseHandler.sendJsonResponse(res, "error", "Internal Server Error");
        }
    }

    /**
     * Handles report generation for Client, Vendor, and Admin.
     */
    private void handleGenerateReport(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Date startDate = Date.valueOf(req.getParameter("start"));
            Date endDate = Date.valueOf(req.getParameter("end"));
            String role = Optional.ofNullable(req.getParameter("role")).orElse("");
            int userId = Integer.parseInt(req.getParameter("userId"));

            if (!isValidRole(role)) {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid User Role");
                return;
            }

            var reportData = reportDao.getReports(startDate, endDate, role, userId);
            ResponseHandler.sendJsonResponse(res, "success", reportData);

        } catch (IllegalArgumentException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Invalid Date Format");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while fetching report", e);
            ResponseHandler.sendJsonResponse(res, "error", "Server Issue");
        }
    }

    /**
     * Validates the user role.
     */
    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("client") ||
               role.equalsIgnoreCase("vendor") ||
               role.equalsIgnoreCase("admin");
    }
}
