package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AppInfo {
    private int id;
    @SerializedName("app_name")
    private String appName;
    private String version;
    @SerializedName("build_number")
    private String buildNumber;
    private String description;
    @SerializedName("footer_text")
    private String footerText;
    @SerializedName("copyright_text")
    private String copyrightText;
    private List<Feature> features;

    public String getAppName() { return appName; }
    public String getVersion() { return version; }
    public String getBuildNumber() { return buildNumber; }
    public String getDescription() { return description; }
    public String getFooterText() { return footerText; }
    public String getCopyrightText() { return copyrightText; }
    public List<Feature> getFeatures() { return features; }

    public static class Feature {
        private String title;
        private String description;
        @SerializedName("icon_type")
        private String iconType;

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getIconType() { return iconType; }
    }
}
