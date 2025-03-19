package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.ClientDao;
import dao.VendorDao;
import dao.interfaces.ClientInterface;
import dao.interfaces.VendorInterface;
import dto.ClientDto;
import dto.VendorDto;
import handler.response_handler.ResponseHandler;

@MultipartConfig()
@WebServlet("/users/*")
public class Client_VendorServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Client_VendorServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = Optional.ofNullable(request.getPathInfo()).orElse("/view");
        response.setContentType("application/json");
        ClientInterface cli = new ClientDao();
        VendorInterface vi = new VendorDao();

        try {
            switch (path) {
                // Client Routes
                case "/clients/view":
                    ResponseHandler.sendJsonResponse(response, "Success", cli.getAll());
                    break;
                case "/clients/getById":
                    handleGetClientById(request, response, cli);
                    break;
                case "/clients/getByName":
                    handleGetClientByName(request, response, cli);
                    break;
                case "/clients/add":
                    handleAddClient(request, response, cli);
                    break;
                case "/clients/update":
                    handleUpdateClient(request, response, cli);
                    break;
                case "/clients/delete":
                    handleDeleteClient(request, response, cli);
                    break;

                // Vendor Routes
                case "/vendors/view":
                    ResponseHandler.sendJsonResponse(response, "Success", vi.getAll());
                    break;
                case "/vendors/getById":
                    handleGetVendorById(request, response, vi);
                    break;
                case "/vendors/getByName":
                    handleGetVendorByName(request, response, vi);
                    break;
                case "/vendors/add":
                    handleAddVendor(request, response, vi);
                    break;
                case "/vendors/update":
                    handleUpdateVendor(request, response, vi);
                    break;
                case "/vendors/delete":
                    handleDeleteVendor(request, response, vi);
                    break;

                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    ResponseHandler.sendJsonResponse(response, "Error", "Invalid Request");
                    break;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error processing request", e);
            ResponseHandler.sendJsonResponse(response, "Error", "Database Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing request", e);
            ResponseHandler.sendJsonResponse(response, "Error", "Internal Server Error");
        }
    }

    // Client Handlers
    private void handleGetClientById(HttpServletRequest request, HttpServletResponse response, ClientInterface cli)
            throws IOException, SQLException {
        String idStr = request.getParameter("client_id");
        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client ID is required");
            return;
        }
        int id = Integer.parseInt(idStr);
        ClientDto client = cli.get(id);
        if (client != null) {
            JSONObject clientJson = new JSONObject();
            clientJson.put("client_id", client.getClient_id());
            clientJson.put("client_name", client.getClient_name());
            clientJson.put("contact_person", client.getContact_person());
            clientJson.put("address", client.getAddress());
            clientJson.put("phone", client.getPhone());
            clientJson.put("created_at", client.getCreated_at());
            ResponseHandler.sendJsonResponse(response, "Success", clientJson.toString(), "data", clientJson.toString());
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Client not found");
        }
    }

    private void handleGetClientByName(HttpServletRequest request, HttpServletResponse response, ClientInterface cli)
            throws IOException, SQLException {
        String name = request.getParameter("client_name");
        if (name == null || name.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client name is required");
            return;
        }
        ClientDto client = cli.getByName(name);
        if (client != null) {
            JSONObject clientJson = new JSONObject();
            clientJson.put("client_id", client.getClient_id());
            clientJson.put("client_name", client.getClient_name());
            clientJson.put("contact_person", client.getContact_person());
            clientJson.put("address", client.getAddress());
            clientJson.put("phone", client.getPhone());
            clientJson.put("created_at", client.getCreated_at());
            ResponseHandler.sendJsonResponse(response, "Success", clientJson.toString(), "data", clientJson.toString());
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Client not found");
        }
    }

    private void handleAddClient(HttpServletRequest request, HttpServletResponse response, ClientInterface cli)
            throws IOException, SQLException {
        String clientName = request.getParameter("client_name");
        String contactPerson = request.getParameter("contact_person");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        if (clientName == null || clientName.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client name is required");
            return;
        }

        ClientDto client = new ClientDto(clientName, contactPerson, address, phone);
        int rowsAffected = cli.insert(client);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Client added successfully", "client_id",
                    String.valueOf(client.getClient_id()));
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to add client");
        }
    }

    private void handleUpdateClient(HttpServletRequest request, HttpServletResponse response, ClientInterface cli)
            throws IOException, SQLException {
        String idStr = request.getParameter("client_id");
        String clientName = request.getParameter("client_name");
        String contactPerson = request.getParameter("contact_person");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client ID is required");
            return;
        }
        if (clientName == null || clientName.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client name is required");
            return;
        }

        int id = Integer.parseInt(idStr);
        ClientDto client = cli.get(id);
        if (client == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client not found");
            return;
        }

        client.setClient_name(clientName);
        client.setContact_person(contactPerson);
        client.setAddress(address);
        client.setPhone(phone);

        int rowsAffected = cli.update(client);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Client updated successfully");
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to update client");
        }
    }

    private void handleDeleteClient(HttpServletRequest request, HttpServletResponse response, ClientInterface cli)
            throws IOException, SQLException {
        String idStr = request.getParameter("client_id");
        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client ID is required");
            return;
        }
        int id = Integer.parseInt(idStr);
        ClientDto client = cli.get(id);
        if (client == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Client not found");
            return;
        }

        int rowsAffected = cli.delete(client);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Client deleted successfully");
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to delete client");
        }
    }

    // Vendor Handlers
    private void handleGetVendorById(HttpServletRequest request, HttpServletResponse response, VendorInterface vi)
            throws IOException, SQLException {
        String idStr = request.getParameter("vendor_id");
        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor ID is required");
            return;
        }
        int id = Integer.parseInt(idStr);
        VendorDto vendor = vi.get(id);
        if (vendor != null) {
            JSONObject vendorJson = new JSONObject();
            vendorJson.put("vendor_id", vendor.getVendor_id());
            vendorJson.put("vendor_name", vendor.getVendor_name());
            vendorJson.put("contact_person", vendor.getContact_person());
            vendorJson.put("address", vendor.getAddress());
            vendorJson.put("phone", vendor.getPhone());
            vendorJson.put("created_at", vendor.getCreated_at());
            ResponseHandler.sendJsonResponse(response, "Success", vendorJson.toString(), "data", vendorJson.toString());
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor not found");
        }
    }

    private void handleGetVendorByName(HttpServletRequest request, HttpServletResponse response, VendorInterface vi)
            throws IOException, SQLException {
        String name = request.getParameter("vendor_name");
        if (name == null || name.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor name is required");
            return;
        }
        VendorDto vendor = vi.getByName(name);
        if (vendor != null) {
            JSONObject vendorJson = new JSONObject();
            vendorJson.put("vendor_id", vendor.getVendor_id());
            vendorJson.put("vendor_name", vendor.getVendor_name());
            vendorJson.put("contact_person", vendor.getContact_person());
            vendorJson.put("address", vendor.getAddress());
            vendorJson.put("phone", vendor.getPhone());
            vendorJson.put("created_at", vendor.getCreated_at());
            ResponseHandler.sendJsonResponse(response, "Success", vendorJson.toString(), "data", vendorJson.toString());
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor not found");
        }
    }

    private void handleAddVendor(HttpServletRequest request, HttpServletResponse response, VendorInterface vi)
            throws IOException, SQLException {
        String vendorName = request.getParameter("vendor_name");
        String contactPerson = request.getParameter("contact_person");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        if (vendorName == null || vendorName.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor name is required");
            return;
        }

        VendorDto vendor = new VendorDto(vendorName, contactPerson, address, phone);
        int rowsAffected = vi.insert(vendor);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Vendor added successfully", "vendor_id",
                    String.valueOf(vendor.getVendor_id()));
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to add vendor");
        }
    }

    private void handleUpdateVendor(HttpServletRequest request, HttpServletResponse response, VendorInterface vi)
            throws IOException, SQLException {
        String idStr = request.getParameter("vendor_id");
        String vendorName = request.getParameter("vendor_name");
        String contactPerson = request.getParameter("contact_person");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");

        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor ID is required");
            return;
        }
        if (vendorName == null || vendorName.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor name is required");
            return;
        }

        int id = Integer.parseInt(idStr);
        VendorDto vendor = vi.get(id);
        if (vendor == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor not found");
            return;
        }

        vendor.setVendor_name(vendorName);
        vendor.setContact_person(contactPerson);
        vendor.setAddress(address);
        vendor.setPhone(phone);

        int rowsAffected = vi.update(vendor);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Vendor updated successfully");
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to update vendor");
        }
    }

    private void handleDeleteVendor(HttpServletRequest request, HttpServletResponse response, VendorInterface vi)
            throws IOException, SQLException {
        String idStr = request.getParameter("vendor_id");
        if (idStr == null || idStr.trim().isEmpty()) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor ID is required");
            return;
        }
        int id = Integer.parseInt(idStr);
        VendorDto vendor = vi.get(id);
        if (vendor == null) {
            ResponseHandler.sendJsonResponse(response, "Error", "Vendor not found");
            return;
        }

        int rowsAffected = vi.delete(vendor);
        if (rowsAffected > 0) {
            ResponseHandler.sendJsonResponse(response, "Success", "Vendor deleted successfully");
        } else {
            ResponseHandler.sendJsonResponse(response, "Error", "Failed to delete vendor");
        }
    }
}