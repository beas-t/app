package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Profile Card
        MaterialCardView profileCard = findViewById(R.id.profile_card);
        if (profileCard != null) {
            profileCard.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        }

        // Grid Actions
        MaterialCardView syncCard = findViewById(R.id.sync_card);
        if (syncCard != null) {
            syncCard.setOnClickListener(v -> Toast.makeText(this, "Syncing data...", Toast.LENGTH_SHORT).show());
        }

        MaterialCardView backupCard = findViewById(R.id.backup_card);
        if (backupCard != null) {
            backupCard.setOnClickListener(v -> startActivity(new Intent(this, BackupActivity.class)));
        }

        MaterialCardView clearCacheCard = findViewById(R.id.clear_cache_card);
        if (clearCacheCard != null) {
            clearCacheCard.setOnClickListener(v -> startActivity(new Intent(this, Cache_oneActivity.class)));
        }

        MaterialCardView exportAllCard = findViewById(R.id.export_all_card);
        if (exportAllCard != null) {
            exportAllCard.setOnClickListener(v -> startActivity(new Intent(this, ExpActivity.class)));
        }

        // Features
        View btnAnalytics = findViewById(R.id.btn_analytics);
        if (btnAnalytics != null) {
            btnAnalytics.setOnClickListener(v -> startActivity(new Intent(this, AnalyticsActivity.class)));
        }

        View btnTeamManagement = findViewById(R.id.btn_team_management);
        if (btnTeamManagement != null) {
            btnTeamManagement.setOnClickListener(v -> startActivity(new Intent(this, TeamActivity.class)));
        }

        View btnAppointments = findViewById(R.id.btn_appointments);
        if (btnAppointments != null) {
            btnAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentActivity.class)));
        }

        View btnTraining = findViewById(R.id.btn_training);
        if (btnTraining != null) {
            btnTraining.setOnClickListener(v -> startActivity(new Intent(this, TrainingActivity.class)));
        }

        View btnEquipment = findViewById(R.id.btn_equipment);
        if (btnEquipment != null) {
            btnEquipment.setOnClickListener(v -> startActivity(new Intent(this, EquipActivity.class)));
        }

        // App Settings
        View btnNotifications = findViewById(R.id.btn_notifications);
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        }

        View btnDisplay = findViewById(R.id.btn_display);
        if (btnDisplay != null) {
            btnDisplay.setOnClickListener(v -> startActivity(new Intent(this, DisplayActivity.class)));
        }

        View btnLanguage = findViewById(R.id.btn_language);
        if (btnLanguage != null) {
            btnLanguage.setOnClickListener(v -> startActivity(new Intent(this, LanguageActivity.class)));
        }

        // Privacy & Security
        View btnChangePassword = findViewById(R.id.btn_change_password);
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> startActivity(new Intent(this, SecurityActivity.class)));
        }

        View btnDataPrivacy = findViewById(R.id.btn_data_privacy);
        if (btnDataPrivacy != null) {
            btnDataPrivacy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyActivity.class)));
        }

        // Support
        View btnHelpCenter = findViewById(R.id.btn_help_center);
        if (btnHelpCenter != null) {
            btnHelpCenter.setOnClickListener(v -> startActivity(new Intent(this, HelpActivity.class)));
        }

        View btnFeedback = findViewById(R.id.btn_feedback);
        if (btnFeedback != null) {
            btnFeedback.setOnClickListener(v -> startActivity(new Intent(this, FeedActivity.class)));
        }

        View btnContactSupport = findViewById(R.id.btn_contact_support);
        if (btnContactSupport != null) {
            btnContactSupport.setOnClickListener(v -> startActivity(new Intent(this, ContactActivity.class)));
        }

        View btnAbout = findViewById(R.id.btn_about);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        }

        // Log Out
        LinearLayout btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> startActivity(new Intent(this, LogoutActivity.class)));
        }

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    startActivity(new Intent(this, HomePageActivity.class));
                    return true;
                } else if (id == R.id.navigation_scan) {
                    startActivity(new Intent(this, ScanActivity.class));
                    return true;
                } else if (id == R.id.navigation_patients) {
                    startActivity(new Intent(this, PatientActivity.class));
                    return true;
                } else if (id == R.id.navigation_reports) {
                    startActivity(new Intent(this, ReportActivity.class));
                    return true;
                } else if (id == R.id.navigation_settings) {
                    return true;
                }
                return false;
            });
        }
    }
}
