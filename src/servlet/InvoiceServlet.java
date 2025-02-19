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

import dao.InvoiceDao;
import dao.interfaces.InvoiceInterface;
import handler.response_handler.ResponseHandler;

@MultipartConfig()
@WebServlet("/invoice/*")
public class InvoiceServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(InvoiceServlet.class.getName());
    @SuppressWarnings("unused")
    private static final InvoiceInterface invInterface = new InvoiceDao();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/invoice");
        res.setContentType("application/json");
        try {
            switch (path) {

                case "/invoice" ->
                    InvoiceServlet.getInvoice(req, res);
                case "/gen-invoice" ->
                    InvoiceServlet.genInvoice(req, res);
                case "/add" ->
                    InvoiceServlet.addInvoice(req, res);
                case "/update" ->
                    InvoiceServlet.updateInvoiceServlet(req, res);
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
        int searchInvoice = Integer.parseInt(req.getParameter("search"));
        try {
            InvoiceDao invoiceDao = new InvoiceDao();
             res.getWriter().println(invoiceDao.search(searchInvoice));
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    

    private static void genInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ResponseHandler.sendJsonResponse(res, "test", "works");
    }

    private static void addInvoice(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ResponseHandler.sendJsonResponse(res, "test", "works");
    }

    private static void updateInvoiceServlet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        ResponseHandler.sendJsonResponse(res, "test", "works");
    }

}
