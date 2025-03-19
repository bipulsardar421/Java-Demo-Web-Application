package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.LoginDao;
import dao.interfaces.LoginInterface;
import dto.login.LoginDto;
import handler.mailSender_handler.MailSenderHandler;
import handler.response_handler.ResponseHandler;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

@WebServlet("/signup/*")
public class SignUpServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SignUpServlet.class.getName());
    private static final LoginInterface loginInterface = new LoginDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/register");
        res.setContentType("application/json");

        try {
            switch (path) {
                case "/register" -> Signup(req, res);
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

    private void Signup(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        if (email == null || password == null || role == null || role.trim().isEmpty() || email.trim().isEmpty()
                || password.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(res, "error", "All fields are required");
            return;
        }
        try {
            if (loginInterface.getByName(email.trim()) != null) {
                ResponseHandler.sendJsonResponse(res, "error", "Email already registered");
                return;
            }
            LoginDto newUser = new LoginDto(0, email.trim(), password, role);
            int userId = loginInterface.insert(newUser);
            if (userId > 0) {
                try {
                    String message = "You can use this credentials to log in : \n" + "Email: " + newUser.getUsername()
                            + "\nPassword: " + newUser.getPassword();
                    MailSenderHandler mail = new MailSenderHandler(email, "Your Account is Created",
                            message);
                    mail.setupServerProperties();
                    mail.draftEmail();
                    mail.sendEmail();
                } catch (MessagingException e) {
                    LOGGER.log(Level.SEVERE, "Error sending email", e);
                }
                req.setAttribute("user_id", userId);
                req.getRequestDispatcher("/userdetails/add").forward(req, res);
            } else {
                ResponseHandler.sendJsonResponse(res, "error", "Internal Server Error");
            }

        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(res, "error", "Database error: " + e.getMessage());
        }
    }

}
