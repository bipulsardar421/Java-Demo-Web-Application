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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter out = res.getWriter();
        String uname = req.getParameter("username") + "";
        String pwd = req.getParameter("password") + "";
        try {
            LoginInterface loginInterface = new LoginDao();
            LoginDto dataTransObj = loginInterface.getByName(uname);
            if (LoginHelper.compareHash(pwd, dataTransObj.getPassword())) {
                if (dataTransObj.getRole().equalsIgnoreCase("admin")) {
                    session.setAttribute("role", "admin");
                }else{
                    session.setAttribute("role", "vendor");
                }
            } else {
                out.println("Not Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
