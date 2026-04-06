package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class TrainingSummary {
    @SerializedName("completion_percent")
    private int completionPercent;

    @SerializedName("technician_level")
    private String technicianLevel;

    @SerializedName("total_modules")
    private int totalModules;

    @SerializedName("completed_modules")
    private int completedModules;

    // Getters
    public int getCompletionPercent() {
        return completionPercent;
    }

    public String getTechnicianLevel() {
        return technicianLevel;
    }

    public int getTotalModules() {
        return totalModules;
    }

    public int getCompletedModules() {
        return completedModules;
    }
}
