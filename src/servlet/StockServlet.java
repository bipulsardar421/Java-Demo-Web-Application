package servlet;

import handler.response_handler.ResponseHandler;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.StockDao;
import dao.interfaces.login.StockInterface;
import dto.stock.stockDto;
import handler.resultset_handler.JsonResultset;
import java.sql.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/stock/*")
public class StockServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(StockServlet.class.getName());
    private final StockInterface stInt = new StockDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = Optional.ofNullable(request.getPathInfo()).orElse("/view");
        response.setContentType("application/json");

        try {
            switch (path) {
                case "/view" ->
                    response.getWriter().println(stInt.getAll());

                case "/get" ->
                    handleGetStock(request, response);

                case "/add", "/update" ->
                    handleAddOrUpdateStock(request, response, path.equals("/update"));
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

    private void handleGetStock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Missing 'id' parameter");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            stockDto stdto = stInt.get(id);
            response.getWriter().println(JsonResultset.convertToJson(stdto.getData()));
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid 'id' format");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
        }
    }

    private void handleAddOrUpdateStock(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws IOException {
        try {
            int id = isUpdate ? Integer.parseInt(request.getParameter("id")) : 0;
            String pname = Optional.ofNullable(request.getParameter("product_name")).orElseThrow(() -> new IllegalArgumentException("Missing 'product_name'"));
            int qty = Integer.parseInt(request.getParameter("quantity"));
            int rate = Integer.parseInt(request.getParameter("rate"));
            String img = Optional.ofNullable(request.getParameter("product_image")).orElse("");
            Date recievedDate = Date.valueOf(request.getParameter("recieved_date"));

            stockDto stock = new stockDto(id, pname, qty, rate, recievedDate, img, "active");
            int result = isUpdate ? stInt.update(stock) : stInt.insert(stock);

            if (result > 0) {
                ResponseHandler.sendJsonResponse(response, "Success", isUpdate ? "Updated Successfully" : "Added Successfully");
            } else {
                ResponseHandler.sendJsonResponse(response, "Error", "Operation Failed");
            }
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid number format in request parameters");
        } catch (IllegalArgumentException | SQLException e) {
            ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
        }
    }
}
