package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import helper.LoginHelper;

@WebServlet("/auto-login")
public class AutomatedLoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AutomatedLoginServlet.class.getName());
    private static final LoginInterface loginInterface = new LoginDao();
    private static final String USERNAME = "bipulsardar421@gmail.comm";
    private static final String PASSWORD = "123456";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            // Step 1: Perform login
            performLogin(req, res);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during automated login", e);
            ResponseHandler.sendJsonResponse(res, "error", "Internal Server Error: " + e.getMessage());
        }
    }

    private void performLogin(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(true); // Create a new session
        LOGGER.info("Starting automated login for " + USERNAME);

        try {
            // Fetch user from database
            LoginDto user = loginInterface.getByName(USERNAME);
            if (user == null) {
                ResponseHandler.sendJsonResponse(res, "error", "User not found");
                return;
            }

            // Verify password
            if (!LoginHelper.compareHash(PASSWORD, user.getPassword())) {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid password");
                return;
            }

            // Step 2: Simulate OTP generation and verification
            simulateOtpFlow(req, res, user);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private void simulateOtpFlow(HttpServletRequest req, HttpServletResponse res, LoginDto user) throws IOException {
        HttpSession session = req.getSession();
        LoginDao dao = new LoginDao();

        try {
            // Generate OTP
            int userId = user.getId();
            dao.deleteLoginOtp(userId); // Clear any existing OTP
            String otp = String.valueOf(100000 + new Random().nextInt(900000));
            int otpSaved = dao.otp(userId, otp);
            LOGGER.info("Generated OTP: " + otp + " for user ID: " + userId);

            if (otpSaved <= 0) {
                ResponseHandler.sendJsonResponse(res, "error", "Failed to generate OTP");
                return;
            }

            // Simulate sending OTP (skipping actual email for automation)
            LOGGER.info("Simulating OTP email to " + USERNAME);

            // Verify OTP
            boolean isOtpValid = dao.checkOtp(userId, otp);
            if (!isOtpValid) {
                ResponseHandler.sendJsonResponse(res, "error", "OTP verification failed");
                return;
            }

            // Set session attributes
            boolean isNew = user.getIsNew() == 1;
            session.setAttribute("authenticated", true);
            session.setAttribute("userId", userId);
            session.setAttribute("user", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setAttribute("isNew", isNew);
            dao.deleteLoginOtp(userId); // Clean up OTP
            LOGGER.info("Session set - isNew: " + isNew + ", role: " + user.getRole());

            // Step 3: Check /auth/session/isNew
            checkIsNewStatus(req, res);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during OTP simulation", e);
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private void checkIsNewStatus(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("isNew") == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Session not initialized");
            return;
        }

        Boolean isNew = (Boolean) session.getAttribute("isNew");
        LOGGER.info("Checking isNew from session: " + isNew);

        // Simulate response from /auth/session/isNew
        if (isNew != null) {
            ResponseHandler.sendJsonResponse(res, "true", "authenticated", "isNew", isNew.toString());
        } else {
            ResponseHandler.sendJsonResponse(res, "false", "not_authenticated");
        }
    }
}