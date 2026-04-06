package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("id")
    private int id;

    @SerializedName("patient_name")
    private String patientName;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("reason")
    private String reason;

    @SerializedName("duration_minutes")
    private int durationMinutes;

    @SerializedName("appointment_type")
    private String appointmentType;

    @SerializedName("location")
    private String location;

    @SerializedName("patient")
    private Integer patient;

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReason() {
        return reason;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public String getLocation() {
        return location;
    }

    public Integer getPatient() {
        return patient;
    }
}
