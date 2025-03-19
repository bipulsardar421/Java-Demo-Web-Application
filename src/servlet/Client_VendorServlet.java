package servlet;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ClientDao;
import dao.VendorDao;
import dao.interfaces.ClientInterface;
import dao.interfaces.VendorInterface;
import handler.response_handler.ResponseHandler;

@MultipartConfig()
@WebServlet("/users/*")

public class Client_VendorServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Client_VendorServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = Optional.ofNullable(request.getPathInfo()).orElse("/view");
        response.setContentType("application/json");
        ClientInterface cli = new ClientDao();
        VendorInterface vi = new VendorDao();
        try {
            switch (path) {
                case "/clients/view" ->
                    response.getWriter().println(cli.getAll());
                case "/vendors/view" ->
                    response.getWriter().println(vi.getAll());
                case "/client/add", "/client/update" ->
                    handleAddOrUpdateClient(request, response, path.equals("/update"));
                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(response, "Error", "Invalid Request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            ResponseHandler.sendJsonResponse(response, "Error", "Internal Server Error");
        }
    }

    public void handleAddOrUpdateClient(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
            throws ServletException, IOException {
    }
}
