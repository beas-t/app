package com.example.bladder_frontend.api;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "BladSensePrefs";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_SPECIALTY = "user_specialty";
    private static final String KEY_USER_LICENSE = "user_license";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_PROFILE_PIC = "user_profile_pic";
    private static final String KEY_SERVER_URL = "server_url";
    private static final String KEY_LAST_WATCHED_MODULE = "last_watched_module";
    private static final String KEY_APP_LANGUAGE = "app_language";
    private static final String KEY_NOTIF_CRITICAL = "notif_critical";
    private static final String KEY_NOTIF_PATIENT = "notif_patient";
    private static final String KEY_NOTIF_REPORT = "notif_report";
    private static final String KEY_NOTIF_SYSTEM = "notif_system";
    private static final String KEY_NOTIF_MARKETING = "notif_marketing";
    private static final String KEY_NOTIF_QUIET_HOURS = "notif_quiet_hours";
    private static final String KEY_NOTIF_SOUND = "notif_sound";
    private static final String KEY_APP_THEME = "app_theme";
    private static final String KEY_BRIGHTNESS = "brightness";
    private static final String KEY_AUTO_BRIGHTNESS = "auto_brightness";
    private static final String KEY_VOLUME_UNIT = "volume_unit";
    private static final String KEY_VOICE_GUIDANCE = "voice_guidance";
    private static final String KEY_SCREEN_READER = "screen_reader";
    private static final String KEY_HIGH_CONTRAST = "high_contrast";
    private static final String KEY_BUTTON_SIZE = "button_size";
    private static final String KEY_LAST_LOGIN_TIME = "last_login_time";
    private static final String KEY_LAST_LOGIN_DEVICE = "last_login_device";
    private static final String KEY_USER_PASSWORD_PREFIX = "user_password_";
    private static final String KEY_LAST_EMAIL = "last_email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public String fetchAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public void saveUserDetail(String email, String name) {
        if (email != null) {
            editor.putString(KEY_USER_EMAIL, email.toLowerCase().trim());
        }
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public void saveDoctorProfile(String fullName, String specialty, String license, String phone, String profilePic) {
        editor.putString(KEY_USER_NAME, fullName);
        editor.putString(KEY_USER_SPECIALTY, specialty);
        editor.putString(KEY_USER_LICENSE, license);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_PROFILE_PIC, profilePic);
        editor.apply();
    }

    public void saveProfilePicture(String url) {
        editor.putString(KEY_USER_PROFILE_PIC, url);
        editor.apply();
    }

    public String getProfilePicture() {
        return sharedPreferences.getString(KEY_USER_PROFILE_PIC, "");
    }

    public void saveDoctorName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public void saveUserEmail(String email) {
        if (email != null) {
            editor.putString(KEY_USER_EMAIL, email.toLowerCase().trim());
            editor.apply();
        }
    }

    public void saveDoctorSpecialty(String specialty) {
        editor.putString(KEY_USER_SPECIALTY, specialty);
        editor.apply();
    }

    public String getDoctorName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public String getDoctorSpecialty() {
        return sharedPreferences.getString(KEY_USER_SPECIALTY, "");
    }

    public String getDoctorLicense() {
        return sharedPreferences.getString(KEY_USER_LICENSE, "");
    }

    public String getDoctorPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public void saveServerUrl(String url) {
        editor.putString(KEY_SERVER_URL, url);
        editor.apply();
    }

    public String getServerUrl() {
        return sharedPreferences.getString(KEY_SERVER_URL, null);
    }

    public boolean isLoggedIn() {
        return fetchAuthToken() != null;
    }

    public void saveLastWatchedModule(int moduleId) {
        editor.putInt(KEY_LAST_WATCHED_MODULE, moduleId);
        editor.apply();
    }

    public int getLastWatchedModule() {
        return sharedPreferences.getInt(KEY_LAST_WATCHED_MODULE, -1);
    }

    // Notification Settings
    public void setNotificationEnabled(String type, boolean enabled) {
        switch (type) {
            case "critical": editor.putBoolean(KEY_NOTIF_CRITICAL, enabled); break;
            case "patient": editor.putBoolean(KEY_NOTIF_PATIENT, enabled); break;
            case "report": editor.putBoolean(KEY_NOTIF_REPORT, enabled); break;
            case "system": editor.putBoolean(KEY_NOTIF_SYSTEM, enabled); break;
            case "marketing": editor.putBoolean(KEY_NOTIF_MARKETING, enabled); break;
            case "quiet": editor.putBoolean(KEY_NOTIF_QUIET_HOURS, enabled); break;
        }
        editor.apply();
    }

    public boolean isNotificationEnabled(String type) {
        switch (type) {
            case "critical": return sharedPreferences.getBoolean(KEY_NOTIF_CRITICAL, true);
            case "patient": return sharedPreferences.getBoolean(KEY_NOTIF_PATIENT, true);
            case "report": return sharedPreferences.getBoolean(KEY_NOTIF_REPORT, true);
            case "system": return sharedPreferences.getBoolean(KEY_NOTIF_SYSTEM, true);
            case "marketing": return sharedPreferences.getBoolean(KEY_NOTIF_MARKETING, false);
            case "quiet": return sharedPreferences.getBoolean(KEY_NOTIF_QUIET_HOURS, false);
            default: return true;
        }
    }

    public void saveNotificationSound(String soundName) {
        editor.putString(KEY_NOTIF_SOUND, soundName);
        editor.apply();
    }

    public String getNotificationSound() {
        return sharedPreferences.getString(KEY_NOTIF_SOUND, "Urgent");
    }

    // Display Settings
    public void saveAppTheme(String theme) {
        editor.putString(KEY_APP_THEME, theme);
        editor.apply();
    }

    public String getAppTheme() {
        return sharedPreferences.getString(KEY_APP_THEME, "light");
    }

    public void saveBrightness(int brightness) {
        editor.putInt(KEY_BRIGHTNESS, brightness);
        editor.apply();
    }

    public int getBrightness() {
        return sharedPreferences.getInt(KEY_BRIGHTNESS, 128);
    }

    public void setAutoBrightness(boolean enabled) {
        editor.putBoolean(KEY_AUTO_BRIGHTNESS, enabled);
        editor.apply();
    }

    public boolean isAutoBrightnessEnabled() {
        return sharedPreferences.getBoolean(KEY_AUTO_BRIGHTNESS, false);
    }

    public void saveVolumeUnit(String unit) {
        editor.putString(KEY_VOLUME_UNIT, unit);
        editor.apply();
    }

    public String getVolumeUnit() {
        return sharedPreferences.getString(KEY_VOLUME_UNIT, "ml");
    }

    // Accessibility Settings
    public void setVoiceGuidanceEnabled(boolean enabled) {
        editor.putBoolean(KEY_VOICE_GUIDANCE, enabled);
        editor.apply();
    }

    public boolean isVoiceGuidanceEnabled() {
        return sharedPreferences.getBoolean(KEY_VOICE_GUIDANCE, false);
    }

    public void setScreenReaderEnabled(boolean enabled) {
        editor.putBoolean(KEY_SCREEN_READER, enabled);
        editor.apply();
    }

    public boolean isScreenReaderEnabled() {
        return sharedPreferences.getBoolean(KEY_SCREEN_READER, false);
    }

    public void setHighContrastEnabled(boolean enabled) {
        editor.putBoolean(KEY_HIGH_CONTRAST, enabled);
        editor.apply();
    }

    public boolean isHighContrastEnabled() {
        return sharedPreferences.getBoolean(KEY_HIGH_CONTRAST, false);
    }

    public void saveButtonSize(String size) {
        editor.putString(KEY_BUTTON_SIZE, size);
        editor.apply();
    }

    public String getButtonSize() {
        return sharedPreferences.getString(KEY_BUTTON_SIZE, "medium");
    }


    public void saveSessionInfo(String deviceName, String time) {
        editor.putString(KEY_LAST_LOGIN_DEVICE, deviceName);
        editor.putString(KEY_LAST_LOGIN_TIME, time);
        editor.apply();
    }

    public String getLastLoginDevice() {
        return sharedPreferences.getString(KEY_LAST_LOGIN_DEVICE, "Unknown Device");
    }

    public String getLastLoginTime() {
        return sharedPreferences.getString(KEY_LAST_LOGIN_TIME, "Never");
    }

    public void saveLocalPassword(String email, String password) {
        if (email != null) {
            editor.putString(KEY_USER_PASSWORD_PREFIX + email.toLowerCase().trim(), password);
            editor.apply();
        }
    }

    public String getLocalPassword(String email) {
        if (email == null) return null;
        return sharedPreferences.getString(KEY_USER_PASSWORD_PREFIX + email.toLowerCase().trim(), null);
    }

    public void saveLastEmail(String email) {
        if (email != null) {
            editor.putString(KEY_LAST_EMAIL, email.toLowerCase().trim());
            editor.apply();
        }
    }

    public String getLastEmail() {
        return sharedPreferences.getString(KEY_LAST_EMAIL, "");
    }
}
