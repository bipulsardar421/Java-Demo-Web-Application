package handler.response_handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;



public class ResponseHandler {

    public static void sendTextResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/plain");
        try (PrintWriter out = response.getWriter()) {
            out.println(message);
        }
    }

    public static void sendHtmlResponse(HttpServletResponse response, String htmlContent) throws IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println(htmlContent);
        }
    }

    public static void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", status);
        jsonResponse.put("message", message);
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonResponse.toString());
        }
    }

}
