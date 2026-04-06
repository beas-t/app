package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Patient implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("patient_id")
    private String patientId;

    @SerializedName("name")
    private String name;

    @SerializedName("age")
    private int age;

    @SerializedName("gender")
    private String gender;

    @SerializedName("condition")
    private String condition;

    @SerializedName("status")
    private String status;

    @SerializedName("last_scan")
    private String lastScan;

    @SerializedName("last_scan_date")
    private String lastScanDate;

    @SerializedName("scan_count")
    private int scanCount;

    @SerializedName("is_archived")
    private boolean isArchived;

    public int getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCondition() {
        return condition;
    }

    public String getStatus() {
        return status;
    }

    public String getLastScan() {
        return lastScan;
    }

    public String getLastScanDate() {
        return lastScanDate;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
