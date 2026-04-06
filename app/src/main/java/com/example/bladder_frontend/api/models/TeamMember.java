package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TeamMember implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("staff_id")
    private String staffId;

    @SerializedName("role")
    private String role;

    @SerializedName("status")
    private String status;

    @SerializedName("years_of_experience")
    private int yearsOfExperience;

    @SerializedName("total_scans")
    private int totalScans;

    @SerializedName("accuracy_pct")
    private int accuracyPct;

    @SerializedName("total_patients")
    private int totalPatients;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("location")
    private String location;

    // Getters
    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public int getTotalScans() {
        return totalScans;
    }

    public int getAccuracyPct() {
        return accuracyPct;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }
}
