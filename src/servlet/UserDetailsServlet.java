package servlet;

import handler.fileUpload_handler.UploadHandler;
import handler.request_handler.RequestHandler;
import handler.response_handler.ResponseHandler;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.JSONArray;

import dao.UserDetailsDao;
import dao.interfaces.UserDetailsInterface;
import dto.user_details.UserDetailsDto;
import handler.resultset_handler.JsonResultset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
@WebServlet("/userdetails/*")
public class UserDetailsServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "users";

    private static final Logger LOGGER = Logger.getLogger(UserDetailsServlet.class.getName());
    private final UserDetailsInterface userDao = new UserDetailsDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session.getAttribute("user") == null) {
            return;
        }

        String path = Optional.ofNullable(request.getPathInfo()).orElse("/view");
        response.setContentType("application/json");

        try {
            switch (path) {
                case "/view" ->
                    response.getWriter().println(userDao.getAll());
                case "/get" ->
                    handleGetUser(request, response);
                case "/add", "/update" ->
                    handleAddOrUpdateUser(request, response, path.equals("/update"));
                case "/delete" ->
                    handleDeleteUser(request, response);
                case "/checkUserName" -> checkUserName(request, response);
                case "/checkPhone" -> checkPhone(request, response);

                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(response, "error", "Invalid Request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error processing request", e);
            ResponseHandler.sendJsonResponse(response, "error", "Internal Server error");
        }
    }

    private void checkUserName(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        String param = request.getParameter("name");
        UserDetailsDao ud = new UserDetailsDao();
        response.getWriter().println(ud.checkUsername(param));
    }

    private void checkPhone(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        String param = request.getParameter("name");
        UserDetailsDao ud = new UserDetailsDao();
        response.getWriter().println(ud.checkPhone(param));
    }

    private void handleGetUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            ResponseHandler.sendJsonResponse(response, "error", "Missing 'id' parameter");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            UserDetailsDto user = userDao.get(id);
            if (user.getRs() != null && user.getRs().isBeforeFirst()) {
                JSONArray jsonResult = JsonResultset.convertToJson(user.getRs());
                System.out.println(jsonResult);
                response.getWriter().println(jsonResult);
            } else {
                ResponseHandler.sendJsonResponse(response, "empty", "New User", "code", "xvg1890");
            }
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "error", "Invalid 'id' format");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(response, "error", e.getMessage());
        }
    }

    private void handleAddOrUpdateUser(HttpServletRequest request, HttpServletResponse response, boolean isUpdate)
        throws IOException, ServletException {
    try {
        System.out.println("handleAddOrUpdateUser called. isUpdate: " + isUpdate);

        int id = isUpdate ? Integer.parseInt(request.getParameter("id")) : 0;
        System.out.println("Parsed user ID: " + id);

        Object userIdObj = request.getAttribute("user_id");
        int userId = 0;
        if (userIdObj != null) {
            userId = Integer.parseInt(userIdObj.toString());
        }
        System.out.println("User ID from request attribute: " + userId);

        String username = Optional.ofNullable(request.getParameter("name"))
                .orElseThrow(() -> new IllegalArgumentException("Missing 'user_name'"));
        System.out.println("Parsed username: " + username);

        String phone = request.getParameter("phone");
        System.out.println("Parsed phone: " + phone);

        String address = Optional.ofNullable(request.getParameter("address")).orElse("");
        System.out.println("Parsed address: " + address);

        String image = null;
        
        if (RequestHandler.isMultipart(request)) {
            System.out.println("Request is multipart. Checking for image upload.");
            Part filePart = request.getPart("image");

            if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null) {
                System.out.println("Image file detected: " + filePart.getSubmittedFileName());

                if (isUpdate) {
                    UserDetailsDao sd = new UserDetailsDao();
                    String imageName = sd.getImageName(id);
                    System.out.println("Existing image: " + imageName);
                    UploadHandler.deleteFile(imageName, getServletContext());
                }
                image = UploadHandler.uploadFile(UPLOAD_DIR, request, getServletContext());
                System.out.println("Uploaded new image: " + image);
            } else {
                String existingImage = request.getParameter("image");
                if (existingImage != null && !existingImage.isEmpty()) {
                    image = existingImage;
                    System.out.println("Using existing image: " + image);
                }
            }
        } else {
            image = request.getParameter("image");
            System.out.println("Non-multipart request. Using existing image: " + image);
        }

        String status = Optional.ofNullable(request.getParameter("status")).orElse("active");
        System.out.println("Parsed status: " + status);

        UserDetailsDto user = new UserDetailsDto(id, userId, username, phone, address, image, status,
                Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("User object created: " + user);

        int result = isUpdate ? userDao.update(user) : userDao.insert(user);
        System.out.println("Database operation result: " + result);

        if (result > 0) {
            System.out.println(isUpdate ? "User updated successfully" : "User added successfully");
            ResponseHandler.sendJsonResponse(response, "success",
                    isUpdate ? "Updated Successfully" : "Added Successfully");
        } else {
            System.out.println("Database operation failed");
            ResponseHandler.sendJsonResponse(response, "error", "Operation Failed");
        }
    } catch (NumberFormatException e) {
        System.out.println("Number format exception: " + e.getMessage());
        ResponseHandler.sendJsonResponse(response, "error", "Invalid number format in request parameters");
    } catch (IllegalArgumentException | SQLException e) {
        System.out.println("Exception occurred: " + e.getMessage());
        ResponseHandler.sendJsonResponse(response, "error", e.getMessage());
    }
}

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            ResponseHandler.sendJsonResponse(response, "error", "Missing 'id' parameter");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            UserDetailsDto user = new UserDetailsDto(id, 0, "", "", "", "", "inactive",
                    Timestamp.valueOf(LocalDateTime.now()));
            int result = userDao.delete(user);

            if (result > 0) {
                ResponseHandler.sendJsonResponse(response, "success", "User Deleted Successfully");
            } else {
                ResponseHandler.sendJsonResponse(response, "error", "Delete Operation Failed");
            }
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "error", "Invalid 'id' format");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(response, "error", e.getMessage());
        }
    }
}
