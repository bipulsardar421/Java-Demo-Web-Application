package servlet;

import handler.response_handler.ResponseHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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

@WebServlet("/stock/*")
public class StockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        response.setContentType("application/json");

        StockInterface stInt = new StockDao();

        if (path == null || path.equals("/get")) {

            if (request.getParameter("id") != null) {
                try {
                   stockDto stdto = stInt.get(Integer.parseInt(request.getParameter("id")));
                   response.getWriter().println(JsonResultset.convertToJson(stdto.getData()));

                } catch (Exception e) {
                    ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());

                }
            } else {

            }

        } else if (path.equals("/add")) {
            if (request.getParameterMap().isEmpty()) {
                ResponseHandler.sendJsonResponse(response, "Error", "Got No Parameter");
            } else {
                String pname = request.getParameter("product_name");
                int qty = Integer.parseInt(request.getParameter("quantity"));
                int rate = Integer.parseInt(request.getParameter("rate"));
                String img = request.getParameter("product_image");

                stockDto st = new stockDto(0, pname, qty, rate, "", img, "active");
                int result;
                try {
                    result = stInt.insert(st);
                    if (result > 0) {
                        ResponseHandler.sendJsonResponse(response, "Success", "Added Successfully");
                    }
                } catch (SQLException e) {
                    ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
                }

            }

        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid Request");
        }
    }
}
