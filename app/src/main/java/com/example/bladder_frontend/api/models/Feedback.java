package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("feedback_type")
    private String feedbackType;

    @SerializedName("message")
    private String message;

    @SerializedName("email")
    private String email;

    public Feedback(String feedbackType, String message, String email) {
        this.feedbackType = feedbackType;
        this.message = message;
        this.email = email;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
