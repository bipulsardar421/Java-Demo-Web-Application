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
    private static final Logger LOGGER = Logger.getLogger(InvoiceServlet.class.getName());
    private static final ReportInterface report = new ReportDao();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/report");
        res.setContentType("application/json");
        try {
            switch (path) {

                case "/report" ->
                    ReportServlet.getReport(req, res);
                default -> {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(res, "Error", "Invalid Request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            ResponseHandler.sendJsonResponse(res, "Error", "Internal Server Error");
        }

    }

    private static void getReport(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Date startDate = Date.valueOf(req.getParameter("start"));
        Date endDate = Date.valueOf(req.getParameter("end"));
        try {
            res.getWriter().println(report.getCombinedReport(startDate, endDate));
        } catch (SQLException e) {
            System.out.println(e);
            res.getWriter().println("");
            ResponseHandler.sendJsonResponse(res, "error", "Server Issue");
        }
    }
}
