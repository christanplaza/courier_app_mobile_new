package com.example.courierappmobile;

public class OrderModelClass {
    String id;
    String description;
    String note;
    String status;
    String datePlaced;
    String address;
    String pickupAddress;

    public OrderModelClass(String id, String description, String note, String status, String datePlaced, String address, String pickupAddress) {
        this.id = id;
        this.description = description;
        this.note = note;
        this.status = status;
        this.datePlaced = datePlaced;
        this.address = address;
        this.pickupAddress = pickupAddress;
    }

    public OrderModelClass() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
}
