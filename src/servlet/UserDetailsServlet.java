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
import javax.servlet.http.Part;

import dao.StockDao;
import dao.UserDetailsDao;
import dao.interfaces.UserDetailsInterface;
import dto.user_details.UserDetailsDto;
import handler.resultset_handler.JsonResultset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
@WebServlet("/userdetails/*")
public class UserDetailsServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "users";

    private static final Logger LOGGER = Logger.getLogger(UserDetailsServlet.class.getName());
    private final UserDetailsInterface userDao = new UserDetailsDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(response, "Error", "Invalid Request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            ResponseHandler.sendJsonResponse(response, "Error", "Internal Server Error");
        }
    }

    private void handleGetUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Missing 'id' parameter");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            UserDetailsDto user = userDao.get(id);
            response.getWriter().println(JsonResultset.convertToJson(user.getRs()));
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid 'id' format");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
        }
    }

    private void handleAddOrUpdateUser(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws IOException, ServletException {
        try {
            int id = isUpdate ? Integer.parseInt(request.getParameter("id")) : 0;
            int userId = Integer.parseInt(request.getParameter("user_id"));
            String username = Optional.ofNullable(request.getParameter("user_name")).orElseThrow(() -> new IllegalArgumentException("Missing 'user_name'"));
            int phone = Integer.parseInt(request.getParameter("phone"));
            String address = Optional.ofNullable(request.getParameter("address")).orElse("");
            String image = null;
            if (RequestHandler.isMultipart(request)) {
                Part filePart = request.getPart("image");

                if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null) {
                    if (isUpdate) {
                        UserDetailsDao sd = new UserDetailsDao();
                        String imageName = sd.getImageName(id);
                        UploadHandler.deleteFile(imageName, getServletContext());
                    }
                    image = UploadHandler.uploadFile(UPLOAD_DIR, request, getServletContext());
                } else {
                    String existingImage = request.getParameter("image");
                    if (existingImage != null && !existingImage.isEmpty()) {
                        image = existingImage;
                    }
                }
            } else {
                image = request.getParameter("image");
            }
            String status = Optional.ofNullable(request.getParameter("status")).orElse("active");

            UserDetailsDto user = new UserDetailsDto(id, userId, username, phone, address, image, status, Timestamp.valueOf(LocalDateTime.now()));
            int result = isUpdate ? userDao.update(user) : userDao.insert(user);

            if (result > 0) {
                ResponseHandler.sendJsonResponse(response, "Success", isUpdate ? "Updated Successfully" : "Added Successfully");
            } else {
                ResponseHandler.sendJsonResponse(response, "Error", "Operation Failed");
            }
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid number format in request parameters");
        } catch (IllegalArgumentException | SQLException e) {
            ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Missing 'id' parameter");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            UserDetailsDto user = new UserDetailsDto(id, 0, "", 0, "", "", "inactive", Timestamp.valueOf(LocalDateTime.now()));
            int result = userDao.delete(user);

            if (result > 0) {
                ResponseHandler.sendJsonResponse(response, "Success", "User Deleted Successfully");
            } else {
                ResponseHandler.sendJsonResponse(response, "Error", "Delete Operation Failed");
            }
        } catch (NumberFormatException e) {
            ResponseHandler.sendJsonResponse(response, "Error", "Invalid 'id' format");
        } catch (SQLException e) {
            ResponseHandler.sendJsonResponse(response, "Error", e.getMessage());
        }
    }
}
