package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainingModule {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("description")
    private String description;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("takeaways")
    private List<String> takeaways;

    @SerializedName("duration_minutes")
    private int durationMinutes;

    @SerializedName("is_completed")
    private boolean isCompleted;

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public List<String> getTakeaways() {
        return takeaways;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
