package dto.invoice;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.List;

public class InvoiceDto {

    private int id;
    private int client_id;

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    private String customerName;
    private String customer_contact;
    private Date invoiceDate;
    private double totalAmount;
    private double discount;
    private double tax;
    private double grand_total;
    private String payment_status;
    private String paymentMethod;
    private String notes;
    private ResultSet data;
    private ResultSet stock;

    private List<InvoiceItemDto> items;

    public InvoiceDto(ResultSet rs) {
        this.data = rs;
    }

    public InvoiceDto(ResultSet data, ResultSet stock) {
        this.data = data;
        this.stock = stock;
    }

    public InvoiceDto(int id, int client_id, String customerName, String customer_contact, Date invoiceDate,
            double totalAmount,
            double discount, double tax, double grand_total, String payment_status, String paymentMethod,
            String notes) {
        this.id = id;
        this.client_id = client_id;
        this.customerName = customerName;
        this.customer_contact = customer_contact;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.tax = tax;
        this.grand_total = grand_total;
        this.payment_status = payment_status;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(double grand_total) {
        this.grand_total = grand_total;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ResultSet getData() {
        return data;
    }

    public void setData(ResultSet data) {
        this.data = data;
    }

    public List<InvoiceItemDto> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemDto> items) {
        this.items = items;
    }

    public ResultSet getStock() {
        return stock;
    }

    public void setStock(ResultSet stock) {
        this.stock = stock;
    }

}
