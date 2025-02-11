package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.LoginDao;
import dao.interfaces.login.LoginInterface;
import dto.login.LoginDto;
import handler.response_handler.ResponseHandler;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
    
        String uname = req.getParameter("username");
        String pwd = req.getParameter("password");
        String role = req.getParameter("role");
    
        if (uname == null || pwd == null || role == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing required fields");
            return;
        }
    
        try (PrintWriter out = res.getWriter()) {
            LoginInterface loginInterface = new LoginDao();
            LoginDto existingUser = loginInterface.getByName(uname);
    
            if (existingUser == null) {
                LoginDto newUser = new LoginDto(0, uname.trim(), pwd.trim(), role.trim());
                int insertStatus = loginInterface.insert(newUser);
    
                if (insertStatus > 0) {
                    ResponseHandler.sendJsonResponse(res, "success", "Account Created");
                } else {
                    ResponseHandler.sendJsonResponse(res, "fail", "Account Not Created");
                }
            } else {
                ResponseHandler.sendJsonResponse(res, "duplicate", "Account Already Exists");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "Database Error: " + e.getMessage());
        }
    }
    
}
