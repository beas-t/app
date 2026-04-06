package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashboardData {
    @SerializedName("doctor_name")
    private String doctorName;

    @SerializedName("recent_scans")
    private List<ScanReport> recentScans;

    @SerializedName("upcoming_appointments")
    private List<Appointment> upcomingAppointments;

    @SerializedName("unread_notifications")
    private List<Notification> unreadNotifications;

    @SerializedName("specialty")
    private String specialty;

    @SerializedName("license_number")
    private String licenseNumber;

    @SerializedName("doctor_count")
    private int doctorCount;

    @SerializedName("nurse_count")
    private int nurseCount;

    @SerializedName("technician_count")
    private int technicianCount;

    @SerializedName("patient_count")
    private int patientCount;

    @SerializedName("active_doctors")
    private int activeDoctors;

    @SerializedName("active_nurses")
    private int activeNurses;

    @SerializedName("active_techs")
    private int activeTechs;

    @SerializedName("active_patients")
    private int activePatients;

    @SerializedName("task_count")
    private int taskCount;

    @SerializedName("profile_picture")
    private String profilePicture;

    public String getDoctorName() {
        return doctorName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public int getDoctorCount() {
        return doctorCount;
    }

    public int getNurseCount() {
        return nurseCount;
    }

    public int getTechnicianCount() {
        return technicianCount;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public int getActiveDoctors() {
        return activeDoctors;
    }

    public int getActiveNurses() {
        return activeNurses;
    }

    public int getActiveTechs() {
        return activeTechs;
    }

    public int getActivePatients() {
        return activePatients;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public List<ScanReport> getRecentScans() {
        return recentScans;
    }

    public List<Appointment> getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public List<Notification> getUnreadNotifications() {
        return unreadNotifications;
    }
    @SerializedName("recent_activities")
    private List<RecentActivity> recentActivities;

    public List<RecentActivity> getRecentActivities() {
        return recentActivities;
    }

    public static class RecentActivity {
        @SerializedName("id")
        private String id;
        @SerializedName("type")
        private String type;
        @SerializedName("title")
        private String title;
        @SerializedName("subtitle")
        private String subtitle;
        @SerializedName("relative_time")
        private String relativeTime;

        public String getId() { return id; }
        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public String getRelativeTime() { return relativeTime; }
    }
}
