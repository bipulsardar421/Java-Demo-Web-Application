package servlet;

import java.io.IOException;
import java.sql.ResultSet;
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

import org.json.JSONArray;

import dao.LoginDao;
import dao.interfaces.LoginInterface;
import dto.login.LoginDto;
import helper.LoginHelper;
import handler.mailSender_handler.MailSenderHandler;
import handler.response_handler.ResponseHandler;
import handler.resultset_handler.JsonResultset;

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
                case "/login" -> LoginServlet.Login(req, res);
                case "/gen-otp" -> LoginServlet.GenOtp(req, res);
                case "/match-otp" -> LoginServlet.MatchOtp(req, res);
                case "/change-pwd" -> LoginServlet.ChangePwd(req, res);
                case "/get-users" -> LoginServlet.getUsers(req, res);
                case "/edit-role" -> LoginServlet.editUserRole(req, res);
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
                session.setAttribute("username", uname);
                session.setAttribute("userId", user.getId());
                GenOtp(req, res);
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid credentials");
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private static void GenOtp(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uname = req.getParameter("username");
        String operation = req.getParameter("operation");
        LoginDao dao = new LoginDao();
        if (uname == null || operation == null || uname.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing username or operation type");
            return;
        }

        try {
            LoginDto user = loginInterface.getByName(uname.trim());
            if (user == null) {
                ResponseHandler.sendJsonResponse(res, "error", "User not found");
                return;
            }

            int user_id = user.getId();
            dao.deleteLoginOtp(user_id);
            String otp = String.valueOf(100000 + new Random().nextInt(900000));
            int otpSaved = dao.otp(user_id, otp);

            if (otpSaved > 0) {
                MailSenderHandler mail = new MailSenderHandler(uname, "OTP Verification",
                        "Your OTP is: " + otp);
                mail.setupServerProperties();
                mail.draftEmail();
                mail.sendEmail();

                ResponseHandler.sendJsonResponse(res, "success", "OTP sent successfully");
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Failed to generate OTP");
            }
        } catch (SQLException | MessagingException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Error: " + e.getMessage());
        }
    }

    private static void MatchOtp(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        String uname = req.getParameter("username");
        String otp = req.getParameter("otp");
        String operation = req.getParameter("operation");

        if (uname == null || otp == null || operation == null) {
            ResponseHandler.sendJsonResponse(res, "error", "Missing required parameters");
            return;
        }

        try {
            LoginDto user = loginInterface.getByName(uname.trim());
            if (user == null) {
                ResponseHandler.sendJsonResponse(res, "error", "User not found");
                return;
            }

            LoginDao dao = new LoginDao();
            boolean isOtpValid = dao.checkOtp(user.getId(), otp);

            if (isOtpValid) {
                session.setAttribute("authenticated", true);
                session.setAttribute("userId", user.getId());
                session.setAttribute("user", user.getUsername());
                session.setAttribute("role", user.getRole());

                dao.deleteLoginOtp(user.getId());
                if ("login".equalsIgnoreCase(operation)) {
                    ResponseHandler.sendJsonResponse(res, "success", "Login successful!", "role", user.getRole());
                } else if ("forgot-password".equalsIgnoreCase(operation)) {
                    ResponseHandler.sendJsonResponse(res, "success", "OTP verified! Proceed with password reset.");
                }
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Invalid OTP");
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private static void ChangePwd(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        boolean isAuthenticated = Boolean.TRUE.equals(session.getAttribute("authenticated"));

        if (userId == null || !isAuthenticated) {
            ResponseHandler.sendJsonResponse(res, "error", "Session expired or unauthorized");
            return;
        }

        String newPwd = req.getParameter("new_pwd");
        if (newPwd == null || newPwd.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "New password is required");
            return;
        }

        try {
            LoginDao dao = new LoginDao();
            LoginDto user = new LoginDto(userId, "", LoginHelper.hashString(newPwd), "");

            int result = dao.update(user);
            if (result > 0) {
                ResponseHandler.sendJsonResponse(res, "success", "Password updated successfully");
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Failed to update password");
            }
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

    private static void getUsers(HttpServletRequest req, HttpServletResponse res) throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId != id) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }
        try {
            ResultSet rs = loginInterface.getUsersData(id);
            if (!rs.isBeforeFirst()) {
                ResponseHandler.sendJsonResponse(res, "error", "No users found.");
                return;
            }
            res.getWriter().println(JsonResultset.convertToJson(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "An error occurred while fetching user data.");
        } catch (Exception e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "Unexpected error occurred.");
        }
    }

    private static void editUserRole(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String jsonData = req.getParameter("data");
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"error\": \"Unauthorized access\"}");
            return;
        }
        try {
            JSONArray ja = new JSONArray(jsonData);
            int result[] = loginInterface.editUserRole(ja);
            if (result.length > 0) {
                ResponseHandler.sendJsonResponse(res, "success", "User role updated successfully.");
                return;
            }
            ResponseHandler.sendJsonResponse(res, "error", "No users found.");
        } catch (SQLException e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "An error occurred while fetching user data.");
        } catch (Exception e) {
            e.printStackTrace();
            ResponseHandler.sendJsonResponse(res, "error", "Unexpected error occurred.");
        }
    }
}
