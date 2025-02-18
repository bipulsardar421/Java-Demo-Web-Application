package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.annotation.MultipartConfig;
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

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

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
                case "/gen-otp" ->
                    LoginServlet.GenOtp(req, res);
                case "/match-otp" ->
                    LoginServlet.MatchOtp(req, res);
                case "/change-pwd" ->
                    LoginServlet.ChangePwd(req, res);
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

    private static void GenOtp(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uname = req.getParameter("username");
        String sub = "Forgot Password OTP";

        if (uname == null || uname.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing username");
            return;

        }

        try {
            LoginDto ld = loginInterface.getByName(uname.trim());
            String storedUsername = Optional.ofNullable(ld).map(LoginDto::getUsername).orElse("");
            if (uname.equalsIgnoreCase(storedUsername)) {
                int user_id = ld.getId();
                Random rc = new Random();
                String otp = 100000 + rc.nextInt(900000) + "";

                LoginDao dao = new LoginDao();
                try {
                    int p = dao.otp(user_id, otp);
                    if (p > 0) {
                        MailSenderHandler mail = new MailSenderHandler(uname, sub,
                                "Hi, your OTP is ...\n" + otp.trim());
                        mail.setupServerProperties();
                        mail.draftEmail();
                        mail.sendEmail();
                        ResponseHandler.sendJsonResponse(res, "success", "mail_sent");
                    }
                } catch (SQLException e) {
                    ResponseHandler.sendJsonResponse(res, "error", e.getMessage());
                }
            } else {
                ResponseHandler.sendJsonResponse(res, "error", uname + " not found!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        } catch (AddressException e) {
            LOGGER.log(Level.SEVERE, "Invalid email address", e);
            ResponseHandler.sendJsonResponse(res, "error", "Invalid email address format");
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Email sending failed", e);
            ResponseHandler.sendJsonResponse(res, "error", "Failed to send email. Please try again later.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            ResponseHandler.sendJsonResponse(res, "error", "An unexpected error occurred");
        }
    }

    private static void MatchOtp(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        session.setMaxInactiveInterval(50 * 60);
        int user_id = Integer.parseInt(req.getParameter("uid"));
        String otp = req.getParameter("otp");
        LoginDao ldao = new LoginDao();
        try {
            if (ldao.checkOtp(user_id, otp)) {
                session.setAttribute("secret", otp);
                session.setAttribute("id", user_id);
                ResponseHandler.sendJsonResponse(res, "Success", "ok");

            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid 'OTP' entered");
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "err: " + e.getMessage());
        }
    }

    private static void ChangePwd(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        LoginDao dao = new LoginDao();
        String secret = (String) session.getAttribute("secret");
        int userId = (int) session.getAttribute("id");
        if (secret == null || userId == 0) {
            ResponseHandler.sendJsonResponse(res, "error", "Session data not found");
            return;
        }
        System.out.println(secret);
        System.out.println(userId);
        try {
            System.out.println(dao.getOtp(userId, secret));
            if (dao.getOtp(userId, secret)) {
                ResponseHandler.sendJsonResponse(res, "error", "Session data modified, try again!");
                return;
            }
            String newPwd = req.getParameter("new_pwd");
            if (newPwd == null) {
                ResponseHandler.sendJsonResponse(res, "error", "Passwords do not match!");
                return;
            }
            LoginDto ldto = new LoginDto(userId, "", LoginHelper.hashString(newPwd), "");
            int result = dao.update(ldto);

            if (result > 0) {
                ResponseHandler.sendJsonResponse(res, "Success", "Password updated successfully!");
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Failed to update password.");
            }

        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

}
