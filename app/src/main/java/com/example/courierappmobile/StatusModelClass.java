package com.example.courierappmobile;

public class StatusModelClass {
    String id;
    String jobOrderID;
    String status;
    String statusMessage;
    String date;
    String time;

    public StatusModelClass(String id, String jobOrderID, String status, String statusMessage, String date, String time) {
        this.id = id;
        this.jobOrderID = jobOrderID;
        this.status = status;
        this.statusMessage = statusMessage;
        this.date = date;
        this.time = time;
    }

    public StatusModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobOrderID() {
        return jobOrderID;
    }

    public void setJobOrderID(String jobOrderID) {
        this.jobOrderID = jobOrderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
