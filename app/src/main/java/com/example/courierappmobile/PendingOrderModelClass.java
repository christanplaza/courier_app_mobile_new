package com.example.courierappmobile;

public class PendingOrderModelClass {
    String id;
    String description;
    String note;
    String status;
    String datePlaced;
    String customerName;
    String customerContactNumber;
    String customerAddress;
    String customerPickupAddress;
    String customerEmail;

    public PendingOrderModelClass(String id, String description, String note, String status, String datePlaced, String customerName, String customerContactNumber, String customerAddress, String customerPickupAddress, String customerEmail) {
        this.id = id;
        this.description = description;
        this.note = note;
        this.status = status;
        this.datePlaced = datePlaced;
        this.customerName = customerName;
        this.customerContactNumber = customerContactNumber;
        this.customerAddress = customerAddress;
        this.customerPickupAddress = customerPickupAddress;
        this.customerEmail = customerEmail;
    }

    public PendingOrderModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(String datePlaced) {
        this.datePlaced = datePlaced;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPickupAddress() {
        return customerPickupAddress;
    }

    public void setCustomerPickupAddress(String customerPickupAddress) {
        this.customerPickupAddress = customerPickupAddress;
    }
}
