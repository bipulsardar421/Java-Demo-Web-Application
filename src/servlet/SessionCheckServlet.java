package servlet;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import handler.response_handler.ResponseHandler;

@WebServlet("/auth/session/*")
public class SessionCheckServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(SessionCheckServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/check");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        try {
            switch (path) {
                case "/check" -> checkSession(req, res);
                case "/getId" -> getUserId(req, res);
                case "/isNew" -> getIsNew(req, res);
                case "/logout" -> logoutSession(req, res);
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

    private static void checkSession(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String role = (String) session.getAttribute("role");
            ResponseHandler.sendJsonResponse(res, "true", "authenticated", "role", role);
        } else {
            ResponseHandler.sendJsonResponse(res, "false", "not_authenticated");
        }
    }

    private static void getUserId(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            int user_id = (int) session.getAttribute("userId");
            ResponseHandler.sendJsonResponse(res, "true", "authenticated", "user_id", user_id + "");
        } else {
            ResponseHandler.sendJsonResponse(res, "false", "not_authenticated");
        }
    }

    private static void getIsNew(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("isNew") != null) {
            Object isNewObj = session.getAttribute("isNew");
            LOGGER.info("isNew type: " + isNewObj.getClass().getName() + ", value: " + isNewObj);
            boolean isNew = (boolean) session.getAttribute("isNew");
            ResponseHandler.sendJsonResponse(res, "true", "authenticated", "isNew", isNew + "");
        } else {
            ResponseHandler.sendJsonResponse(res, "false", "not_authenticated");
        }
    }
    private static void logoutSession(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
            ResponseHandler.sendJsonResponse(res, "success", "Logged out successfully");
        } else {
            ResponseHandler.sendJsonResponse(res, "error", "No active session found");
        }
    }
}
