package dto.user_details;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class UserDetailsDto {

    int id;
    int user_id;
    String user_name;
    String phone;
    String address;
    String image;
    String status;
    Timestamp updatedAt;
    ResultSet rs;

    public UserDetailsDto(int id, int user_id, String user_name, String phone, String address, String image, String status,
            Timestamp updatedAt) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public UserDetailsDto(ResultSet rs) {
        this.rs = rs;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
