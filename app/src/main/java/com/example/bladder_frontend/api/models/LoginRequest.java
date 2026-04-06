package com.example.bladder_frontend.api.models;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
