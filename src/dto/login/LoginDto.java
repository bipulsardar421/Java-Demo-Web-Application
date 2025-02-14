package dto.login;

import handler.resultset_handler.JsonResultset;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDto {

    int id;
    String username;
    String password;
    String role;

    public LoginDto(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public LoginDto(ResultSet rs) {
        try {
            JsonResultset.convertToJson(rs);
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "LoginDto [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
    }

}
