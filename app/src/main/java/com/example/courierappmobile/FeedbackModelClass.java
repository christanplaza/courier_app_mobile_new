package com.example.courierappmobile;

public class FeedbackModelClass {
    String id;
    String rating;
    String feedback;

    public FeedbackModelClass(String id, String rating, String feedback) {
        this.id = id;
        this.rating = rating;
        this.feedback = feedback;
    }

    public FeedbackModelClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
