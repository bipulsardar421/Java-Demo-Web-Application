package dbmodel.login;

import dao.dto.LoginDto;
import dao.impl.login.LoginImpl;
import dao.interfaces.login.LoginInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginDBmodel extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String userName =req.getParameter("username")+"";  
        System.out.println(userName);
        try {
            LoginInterface loginInterface = new LoginImpl();
            LoginDto dataTransObj = loginInterface.getByName(userName);  
            PrintWriter out = res.getWriter();
            out.print(dataTransObj+"");  
        } catch (SQLException e) {
            e.printStackTrace();  
        }
    }
    // public static void main(String[] args) throws SQLException {
    //     LoginInterface loginInterface = new LoginImpl();
    //     LoginDto dataTransObj = loginInterface.getByName("b@c.c");
    //     System.out.println(dataTransObj);
    // }
}
