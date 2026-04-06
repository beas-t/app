package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class BackupSettings {
    @SerializedName("auto_backup")
    private boolean autoBackup;
    @SerializedName("include_images")
    private boolean includeImages;
    @SerializedName("wifi_only")
    private boolean wifiOnly;
    @SerializedName("last_backup_at")
    private String lastBackupAt;

    public boolean isAutoBackup() { return autoBackup; }
    public void setAutoBackup(boolean autoBackup) { this.autoBackup = autoBackup; }
    public boolean isIncludeImages() { return includeImages; }
    public void setIncludeImages(boolean includeImages) { this.includeImages = includeImages; }
    public boolean isWifiOnly() { return wifiOnly; }
    public void setWifiOnly(boolean wifiOnly) { this.wifiOnly = wifiOnly; }
    public String getLastBackupAt() { return lastBackupAt; }
}
