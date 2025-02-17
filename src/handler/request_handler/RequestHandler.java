package handler.request_handler;

import javax.servlet.http.HttpServletRequest;

public class RequestHandler {

    public static boolean isMultipart(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }

}
