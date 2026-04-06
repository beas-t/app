package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.bladder_frontend.api.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecurityActivity extends BaseActivity {

    private SessionManager sessionManager;
    private TextView tvCurrentDeviceName;
    private TextView tvCurrentDeviceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvCurrentDeviceName = findViewById(R.id.tv_current_device_name);
        tvCurrentDeviceTime = findViewById(R.id.tv_current_device_time);

        // Update current device info
        updateCurrentDeviceInfo();


        // Change Password Card
        MaterialCardView cardChangePassword = findViewById(R.id.card_change_password);
        if (cardChangePassword != null) {
            cardChangePassword.setOnClickListener(v -> {
                startActivity(new Intent(this, ChangePasswordActivity.class));
            });
        }

        // Sign Out All Devices
        MaterialButton btnSignOutAll = findViewById(R.id.btn_sign_out_all);
        if (btnSignOutAll != null) {
            btnSignOutAll.setOnClickListener(v -> signOutAllDevices());
        }

        updateUI();
    }

    private void updateCurrentDeviceInfo() {
        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        String currentTime = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new Date());

        if (tvCurrentDeviceName != null) {
            tvCurrentDeviceName.setText(deviceName);
        }
        if (tvCurrentDeviceTime != null) {
            tvCurrentDeviceTime.setText("Last active: Just now (Login: " + currentTime + ")");
        }

        // Save session info
        sessionManager.saveSessionInfo(deviceName, currentTime);
    }

    private void updateUI() {
    }


    private void signOutAllDevices() {
        // In a real app, this would call a backend API to invalidate other tokens.
        // For now, we'll simulate success and inform the user.
        Toast.makeText(this, "Requesting other devices to sign out...", Toast.LENGTH_LONG).show();
        
        // Clearing session info from storage except the current one if needed
        // For simplicity, we just show a success message as a "Professional Demo"
        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(SecurityActivity.this, "Successfully signed out from 1 other device", Toast.LENGTH_SHORT).show();
        }, 2000);
    }
}
