package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class AnalyticsData {
    @SerializedName("total_patients")
    private int totalPatients;

    @SerializedName("discharge_rate")
    private double dischargeRate;

    @SerializedName("male_count")
    private int maleCount;

    @SerializedName("female_count")
    private int femaleCount;

    @SerializedName("other_count")
    private int otherCount;

    @SerializedName("age_0_18")
    private int age0_18;

    @SerializedName("age_19_30")
    private int age19_30;

    @SerializedName("age_31_50")
    private int age31_50;

    @SerializedName("age_51_70")
    private int age51_70;

    @SerializedName("age_70_plus")
    private int age70Plus;

    @SerializedName("active_patients")
    private int activePatients;

    @SerializedName("retention_rate")
    private double retentionRate;

    @SerializedName("trend_monday")
    private double trendMonday;

    @SerializedName("trend_tuesday")
    private double trendTuesday;

    @SerializedName("trend_wednesday")
    private double trendWednesday;

    @SerializedName("trend_thursday")
    private double trendThursday;

    @SerializedName("trend_friday")
    private double trendFriday;

    @SerializedName("trend_saturday")
    private double trendSaturday;

    @SerializedName("trend_sunday")
    private double trendSunday;

    // Getters
    public int getTotalPatients() { return totalPatients; }
    public double getDischargeRate() { return dischargeRate; }
    public int getMaleCount() { return maleCount; }
    public int getFemaleCount() { return femaleCount; }
    public int getOtherCount() { return otherCount; }
    public int getAge0_18() { return age0_18; }
    public int getAge19_30() { return age19_30; }
    public int getAge31_50() { return age31_50; }
    public int getAge51_70() { return age51_70; }
    public int getAge70Plus() { return age70Plus; }
    public int getActivePatients() { return activePatients; }
    public double getRetentionRate() { return retentionRate; }
    public double getTrendMonday() { return trendMonday; }
    public double getTrendTuesday() { return trendTuesday; }
    public double getTrendWednesday() { return trendWednesday; }
    public double getTrendThursday() { return trendThursday; }
    public double getTrendFriday() { return trendFriday; }
    public double getTrendSaturday() { return trendSaturday; }
    public double getTrendSunday() { return trendSunday; }
}
