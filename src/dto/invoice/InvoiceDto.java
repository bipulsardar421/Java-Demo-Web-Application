package dto.invoice;

import java.util.Date;
import java.util.List;

public class InvoiceDto {

    private int id;
    private String customerName;
    private Date invoiceDate;
    private double totalAmount;
    private String paymentMethod;
    private String notes;
    private List<InvoiceItemDto> items;

    public InvoiceDto(int id, String customerName, Date invoiceDate, double totalAmount,
            String paymentMethod, String notes, List<InvoiceItemDto> items) {
        this.id = id;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.items = items;
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

    public List<InvoiceItemDto> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemDto> items) {
        this.items = items;
    }

}
