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
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import dao.InvoiceDao;
import dao.interfaces.InvoiceInterface;
import dto.invoice.InvoiceDto;
import handler.response_handler.ResponseHandler;

@MultipartConfig()
@WebServlet("/invoice/*")
public class InvoiceServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(InvoiceServlet.class.getName());
    private static final InvoiceInterface invInterface = new InvoiceDao();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/invoice");
        res.setContentType("application/json");
        try {
            switch (path) {

                case "/invoice" ->
                    InvoiceServlet.getInvoice(req, res);
                case "/iamAdmin" ->
                    InvoiceServlet.getAll(req, res);
                case "/gen-invoice" ->
                    InvoiceServlet.genInvoice(req, res);
                case "/add" ->
                    InvoiceServlet.addInvoice(req, res);
                case "/update" ->
                    InvoiceServlet.updateInvoiceServlet(req, res);
                case "/search" ->
                    InvoiceServlet.searchInvoiceServlet(req, res);
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

    private static void getInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException {
        long phone = Long.parseLong(req.getParameter("phone"));
        try {
            res.getWriter().println(invInterface.getWithUserId(phone));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getAll(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }
        try {
            res.getWriter().println(invInterface.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void genInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ResponseHandler.sendJsonResponse(res, "test", "works");
    }

    private static void addInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String jsonObject = req.getParameter("cartData");
        String customerName = req.getParameter("customer_name");
        long customerContact = Long.parseLong(req.getParameter("customer_contact"));
        Date invoiceDate = Date.valueOf(req.getParameter("invoice_date"));
        double totalAmount = Double.parseDouble(req.getParameter("total_amount"));
        double discount = Double.parseDouble(req.getParameter("discount"));
        double tax = Double.parseDouble(req.getParameter("tax"));
        double grandTotal = Double.parseDouble(req.getParameter("grand_total"));
        String paymentStatus = req.getParameter("payment_status");
        String paymentMethod = req.getParameter("payment_method");
        String notes = req.getParameter("notes");

        InvoiceDto invDto = new InvoiceDto(0, customerName, customerContact, invoiceDate, totalAmount,
                discount, tax, grandTotal, paymentStatus, paymentMethod, notes);
        try {
            int id = invInterface.save(invDto);
            insertInvoiceItem(jsonObject, id);
            ResponseHandler.sendJsonResponse(res, "success", "Invoice Generated");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Server Error, Please try again!");
            e.printStackTrace();
        }

    }

    private static void updateInvoiceServlet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        ResponseHandler.sendJsonResponse(res, "test", "works");
    }

    private static void searchInvoiceServlet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        long phone = Long.parseLong(req.getParameter("phone"));
        long searchParam = Long.parseLong(req.getParameter("search"));
        try {
            res.getWriter().println(invInterface.search(phone, searchParam));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertInvoiceItem(String JsonString, int invoiceId) throws SQLException {
        try {
            JSONArray ja = new JSONArray(JsonString);
            InvoiceDao dao = new InvoiceDao();
            dao.addInvoiceItems(ja, invoiceId);
        } catch (Exception f) {
            f.printStackTrace();
        }

    }

}
