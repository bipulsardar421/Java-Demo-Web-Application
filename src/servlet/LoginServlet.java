package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.LoginDao;
import dao.interfaces.login.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import handler.response_handler.ResponseHandler;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();

        String uname = req.getParameter("username");
        String pwd = req.getParameter("password");

        if (uname == null || pwd == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing username or password");
            return;
        }

        try {
            LoginInterface loginInterface = new LoginDao();
            LoginDto user = loginInterface.getByName(uname.trim());

            if (user != null && LoginHelper.compareHash(pwd.trim(), user.getPassword())) {
                String role = user.getRole().equalsIgnoreCase("admin") ? "admin" : "vendor";
                session.setAttribute("role", role);
                ResponseHandler.sendJsonResponse(res, "success", "Login Successful as " + role);
            } else {
                ResponseHandler.sendJsonResponse(res, "fail", "Invalid credentials");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

}
