package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.bladder_frontend.databinding.ActivitySettingsBinding;
import com.google.android.material.navigation.NavigationBarView;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        // --- Bottom Navigation Setup ---
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_settings);
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(SettingsActivity.this, HomePageActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_scan) {
                    startActivity(new Intent(SettingsActivity.this, ScanActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_patients) {
                    startActivity(new Intent(SettingsActivity.this, PatientActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_reports) {
                    startActivity(new Intent(SettingsActivity.this, ReportActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (id == R.id.navigation_settings) {
                    return true;
                }
                return false;
            }
        });

        // --- Toolbar & Search/Notifications ---
        binding.searchIcon.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, SearchActivity.class));
        });

        binding.notificationContainer.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, Notification_oneActivity.class));
        });

        // --- Profile Card Click ---
        binding.profileCard.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
        });

        // --- Top Grid Actions ---
        binding.analyticsCard.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AnalyticsActivity.class));
        });

        binding.backupCard.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, BackupActivity.class));
        });

        // --- Features List (Includes) ---
        binding.btnAnalytics.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AnalyticsActivity.class));
        });

        binding.btnTeamManagement.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, TeamActivity.class));
        });

        binding.btnAppointments.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AppointmentActivity.class));
        });

        binding.btnTraining.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, TrainingActivity.class));
        });

        binding.btnEquipment.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, EquipActivity.class));
        });

        // --- App Settings (Includes) ---
        binding.btnNotifications.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, Notification_oneActivity.class));
        });

        binding.btnDisplay.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, DisplayActivity.class));
        });

        binding.btnLanguage.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, LanguageActivity.class));
        });

        // --- Privacy & Security ---
        binding.btnChangePassword.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, SecurityActivity.class));
        });

        binding.btnDataPrivacy.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, PrivacyActivity.class));
        });

        // --- Support (Includes) ---
        binding.btnHelpCenter.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, HelpActivity.class));
        });

        binding.btnFeedback.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, FeedActivity.class));
        });

        binding.btnContactSupport.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ContactActivity.class));
        });

        binding.btnAbout.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
        });

        // --- Logout ---
        binding.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, RoleSelectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
