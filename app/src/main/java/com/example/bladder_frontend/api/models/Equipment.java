package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class Equipment {
    private String name;
    
    @SerializedName("serial_number")
    private String serialNumber;
    
    private String status;
    
    @SerializedName("battery_level")
    private int batteryLevel;
    
    private int temperature;
    
    @SerializedName("signal_strength")
    private String signalStrength;
    
    @SerializedName("last_calibration")
    private String lastCalibration;
    
    @SerializedName("next_service_due")
    private String nextServiceDue;

    // Getters
    public String getName() { return name; }
    public String getSerialNumber() { return serialNumber; }
    public String getStatus() { return status; }
    public int getBatteryLevel() { return batteryLevel; }
    public int getTemperature() { return temperature; }
    public String getSignalStrength() { return signalStrength; }
    public String getLastCalibration() { return lastCalibration; }
    public String getNextServiceDue() { return nextServiceDue; }
}
