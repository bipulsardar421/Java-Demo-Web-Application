package dto;

import java.sql.Date;

public class VendorDto {
    int vendor_id;
    String vendor_name;
    String contact_person;
    String address;
    String phone;
    Date created_at;
    
    public VendorDto(String vendor_name, String contact_person, String address, String phone) {
        this.vendor_name = vendor_name;
        this.contact_person = contact_person;
        this.address = address;
        this.phone = phone;
    }
    public VendorDto(int vendor_id, String vendor_name, String contact_person, String address, String phone,
            Date created_at) {
        this.vendor_id = vendor_id;
        this.vendor_name = vendor_name;
        this.contact_person = contact_person;
        this.address = address;
        this.phone = phone;
        this.created_at = created_at;
    }
    public int getVendor_id() {
        return vendor_id;
    }
    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }
    public String getVendor_name() {
        return vendor_name;
    }
    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
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
