package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.xdevapi.JsonArray;

import dao.LoginDao;
import dao.interfaces.login.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        String uname = req.getParameter("username") + "";
        String pwd = req.getParameter("password") + "";
        try {
            LoginInterface loginInterface = new LoginDao();
            LoginDto dataTransObj = loginInterface.getByName(uname);
            if(LoginHelper.compareHash(pwd, dataTransObj.getPassword())){
              out.println("Found");  
            }else{
                out.println("Not Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
