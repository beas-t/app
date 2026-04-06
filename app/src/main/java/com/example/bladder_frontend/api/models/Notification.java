package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("is_read")
    private boolean isRead;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }
}
