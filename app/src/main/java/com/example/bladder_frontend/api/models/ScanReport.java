package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ScanReport implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("report_id")
    private String reportId;

    @SerializedName("patient_name")
    private String patientName;

    @SerializedName("scan_date")
    private String scanDate;

    @SerializedName("volume")
    private String volume;

    @SerializedName("status")
    private String status;

    @SerializedName("notes")
    private String notes;

    @SerializedName("ai_details")
    private AiDetails aiDetails;

    @SerializedName("patient")
    private Patient patient;

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getReportId() {
        return reportId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getScanDate() {
        return scanDate;
    }

    public String getVolume() {
        return volume;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public AiDetails getAiDetails() {
        return aiDetails;
    }

    public static class AiDetails implements Serializable {
        @SerializedName("class")
        private String className;

        @SerializedName("confidence")
        private float confidence;

        @SerializedName("status")
        private String status;

        @SerializedName("volume")
        private String volume;

        @SerializedName("level")
        private String level;

        public float getConfidence() {
            return confidence;
        }
        
        public String getClassName() {
            return className;
        }
    }
}
