package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class Backup {
    private int id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("file_size")
    private String fileSize;
    @SerializedName("item_count")
    private int itemCount;
    @SerializedName("backup_file")
    private String backupFile;
    private String status;

    public int getId() { return id; }
    public String getCreatedAt() { return createdAt; }
    public String getFileSize() { return fileSize; }
    public int getItemCount() { return itemCount; }
    public String getBackupFile() { return backupFile; }
    public String getStatus() { return status; }
}
