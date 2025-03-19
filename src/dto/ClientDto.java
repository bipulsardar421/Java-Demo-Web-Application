package dto;

import java.sql.Date;

public class ClientDto {

    int client_id;
    String client_name;
    String contact_person;
    String address;
    String phone;
    Date created_at;

    public ClientDto(String client_name, String contact_person, String address, String phone) {
        this.client_name = client_name;
        this.contact_person = contact_person;
        this.address = address;
        this.phone = phone;
    }

    public ClientDto(int client_id, String client_name, String contact_person, String address, String phone,
            Date created_at) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.contact_person = contact_person;
        this.address = address;
        this.phone = phone;
        this.created_at = created_at;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

}
