package handler.fileUpload_handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class UploadHandler {

    public static String uploadFile(String UPLOAD_DIR, HttpServletRequest request, ServletContext servletContext)
            throws ServletException, IOException {
        Part filePart = request.getPart("image");
        if (filePart == null || filePart.getSubmittedFileName().isEmpty()) {
            return "No file uploaded!";
        }
        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        String uploadPath = servletContext.getRealPath("/") + "images/" + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File file = new File(uploadPath, uniqueFileName);
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, file.toPath());
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }
        String contextPath = request.getContextPath();
        String domain = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            domain += ":" + request.getServerPort();
        }
        String fileUrl = domain + contextPath + "/images/" + UPLOAD_DIR + "/" + uniqueFileName;
        return fileUrl;
    }
    public static String deleteFile(String UPLOAD_DIR, String fileName, ServletContext servletContext) {
        String filePath = servletContext.getRealPath("/") + "images/" + UPLOAD_DIR + "/" + fileName;
        File file = new File(filePath);
    
        if (file.exists()) {
            if (file.delete()) {
                return "File deleted successfully: " + fileName;
            } else {
                return "Failed to delete file: " + fileName;
            }
        } else {
            return "File not found: " + fileName;
        }
    }
    

}
