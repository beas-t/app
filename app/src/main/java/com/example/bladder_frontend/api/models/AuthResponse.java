package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("tokens")
    private TokenData tokens;
    @SerializedName("user")
    private UserData user;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public TokenData getTokens() {
        return tokens;
    }

    public String getAccessToken() {
        return tokens != null ? tokens.getAccess() : null;
    }

    public String getRefreshToken() {
        return tokens != null ? tokens.getRefresh() : null;
    }

    public UserData getUser() {
        return user;
    }

    public static class TokenData {
        @SerializedName("access")
        private String access;
        @SerializedName("refresh")
        private String refresh;

        public String getAccess() {
            return access;
        }

        public String getRefresh() {
            return refresh;
        }
    }

    public static class UserData {
        @SerializedName("email")
        private String email;
        @SerializedName("username")
        private String username;
        @SerializedName("profile")
        private DoctorProfileData profile;

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public DoctorProfileData getProfile() {
            return profile;
        }
    }

    public static class DoctorProfileData {
        @SerializedName("fullName")
        private String fullName;
        @SerializedName("specialty")
        private String specialty;
        @SerializedName("licenseNumber")
        private String licenseNumber;
        @SerializedName("phone")
        private String phone;
        @SerializedName("profile_picture")
        private String profilePicture;

        public String getFullName() {
            return fullName;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getLicenseNumber() {
            return licenseNumber;
        }

        public String getPhone() {
            return phone;
        }

        public String getProfilePicture() {
            return profilePicture;
        }
    }
}
