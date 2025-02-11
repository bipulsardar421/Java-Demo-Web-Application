package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.xdevapi.JsonArray;

import dao.LoginDao;
import dao.interfaces.login.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import response_handler.ResponseHandler;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();
        String uname = req.getParameter("username") + "";
        String pwd = req.getParameter("password") + "";
        String role = req.getParameter("role") + "";
        try {
            LoginInterface loginInterface = new LoginDao();
            LoginDto dataTransObj = loginInterface.getByName(uname);
            if (dataTransObj == null) {
                LoginDto ld = new LoginDto(0, uname, pwd, role);
                int i = loginInterface.insert(ld);
                if (i > 0) {
                    ResponseHandler.sendJsonResponse(res, "success", "Account Created");
                } else {
                    out.println("Not Done");
                }

            } else {
                out.println("fu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
