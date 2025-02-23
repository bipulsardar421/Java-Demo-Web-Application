package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.LoginDao;
import dao.interfaces.LoginInterface;
import dto.login.LoginDto;
import handler.mailSender_handler.MailSenderHandler;
import handler.response_handler.ResponseHandler;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

@WebServlet("/signup/*")
public class SignUpServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SignUpServlet.class.getName());
    private final static LoginInterface loginInterface = new LoginDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/register");
        res.setContentType("application/json");

        try {
            switch (path) {
                case "/register" -> Signup(req, res);
                case "/gen-otp" -> GenOtp(req, res);
                case "/match-otp" -> MatchOtp(req, res);
                default -> {
                    res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(res, "error", "Invalid Request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            ResponseHandler.sendJsonResponse(res, "error", "Internal Server Error");
        }
    }

    private void Signup(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = "client";

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "All fields are required");
            return;
        }

        try {
            if (loginInterface.getByName(email.trim()) != null) {
                ResponseHandler.sendJsonResponse(res, "error", "Email already registered");
                return;
            }
            LoginDto newUser = new LoginDto(0, email.trim(), password, role);

            if (GenOtp(req, res)) {
                req.getSession().setAttribute("pendingUser", newUser);
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private boolean GenOtp(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        String email = req.getParameter("email");
        LoginDao dao = new LoginDao();
        String otp="";
        if (email == null || email.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "Email is required");
            return false;
        }

        try {
            dao.deleteSignUpOtp(email, "");
             otp = String.valueOf(100000 + new Random().nextInt(900000));
            int otpSaved = dao.signUpOtp(email, otp);

            if (otpSaved > 0) {
                MailSenderHandler mail = new MailSenderHandler(email, "OTP Verification", "Your OTP is: " + otp);
                mail.setupServerProperties();
                mail.draftEmail();
                mail.sendEmail();
                ResponseHandler.sendJsonResponse(res, "success", "OTP sent successfully");
                return true;
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Failed to generate OTP");
                return false;
            }
        } catch (SQLException | MessagingException e) {
            dao.deleteSignUpOtp(email, otp);
            ResponseHandler.sendJsonResponse(res, "error", "Error: " + e.getMessage());
            return false;
        }
    }

    private void MatchOtp(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        try {
            LoginDao dao = new LoginDao();
            boolean isOtpValid = dao.checkSignUpOtp(email, otp);

            if (isOtpValid) {
                LoginDto pendingUser = (LoginDto) session.getAttribute("pendingUser");
                if (pendingUser == null || !pendingUser.getUsername().equals(email)) {
                    ResponseHandler.sendJsonResponse(res, "error", "Session expired. Please sign up again.");
                    return;
                }
                System.out.println(pendingUser.getPassword());
                int userId = loginInterface.insert(pendingUser);
                if (userId > 0) {
                    session.setAttribute("verified", true);
                    session.setAttribute("userName", email);
                    dao.deleteSignUpOtp(email, otp);
                    ResponseHandler.sendJsonResponse(res, "success", email, "user_id", userId + "");
                } else {
                    ResponseHandler.sendJsonResponse(res, "error", "User registration failed");
                }
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid OTP");
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }
}
