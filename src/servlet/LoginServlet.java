package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.LoginDao;
import dao.interfaces.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import handler.mailSender_handler.MailSenderHandler;
import handler.response_handler.ResponseHandler;

@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final static LoginInterface loginInterface = new LoginDao();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/login");
        res.setContentType("application/json");
        try {
            switch (path) {

                case "/login" ->
                    LoginServlet.Login(req, res);
                case "/forget-password" ->
                    LoginServlet.ForgetPwd(req, res);
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

    private static void Login(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();

        String uname = req.getParameter("username");
        String pwd = req.getParameter("password");

        if (uname == null || pwd == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing username or password");
            return;
        }

        try {
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

    private static void ForgetPwd(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, AddressException, MessagingException {
        String uname = req.getParameter("username");
        System.out.println(uname);
        String sub = "Forgot Password OTP";
        if (uname == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing username");
            return;
        }
        try {
            LoginDto ld = loginInterface.getByName(uname.trim());
            if (uname.equalsIgnoreCase(ld.getUsername())) {
                MailSenderHandler mail = new MailSenderHandler(uname, sub, "hii");
                mail.setupServerProperties();
                mail.draftEmail();
                mail.sendEmail();
            } else {
                ResponseHandler.sendJsonResponse(res, "Error", uname + " Not Found!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

}
