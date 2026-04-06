package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.utils.ImageUtils;
import com.example.bladder_frontend.utils.StorageUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Profile Card
        MaterialCardView profileCard = findViewById(R.id.profile_card);
        TextView tvProfileName = findViewById(R.id.tv_profile_name);
        SessionManager sessionManager = new SessionManager(this);
        
        if (tvProfileName != null) {
            String name = sessionManager.getDoctorName();
            if (name != null && !name.isEmpty()) {
                tvProfileName.setText(name);
            }
        }

        ImageView profileImage = findViewById(R.id.profile_image);
        if (profileImage != null) {
            String picUrl = sessionManager.getProfilePicture();
            if (!picUrl.isEmpty()) {
                ImageUtils.loadImageFromUrl(this, profileImage, picUrl);
            }
        }
        
        if (profileCard != null) {
            profileCard.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
        }

        // Status Card Sync
        View statusCard = findViewById(R.id.status_card);
        if (statusCard != null) {
            statusCard.setOnClickListener(v -> performDataSync());
        }

        // Features
        View btnAppointments = findViewById(R.id.btn_appointments);
        if (btnAppointments != null) {
            btnAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentActivity.class)));
        }

        View btnTraining = findViewById(R.id.btn_training);
        if (btnTraining != null) {
            btnTraining.setOnClickListener(v -> startActivity(new Intent(this, TrainingActivity.class)));
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

        // Privacy & Security
        View btnChangePassword = findViewById(R.id.btn_change_password);
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> startActivity(new Intent(this, SecurityActivity.class)));
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

        View btnAbout = findViewById(R.id.btn_about);
        if (btnAbout != null) {
            btnAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
        }

        // Log Out
        LinearLayout btnLogout = findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> startActivity(new Intent(this, LogoutActivity.class)));
        }

        // Manage Storage Button
        View btnManageStorage = findViewById(R.id.btn_manage_storage);
        if (btnManageStorage != null) {
            btnManageStorage.setOnClickListener(v -> startActivity(new Intent(this, CacheActivity.class)));
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

    @Override
    protected void onResume() {
        super.onResume();
        updateStorageUI();
        
        // Refresh profile info
        SessionManager sessionManager = new SessionManager(this);
        TextView tvProfileName = findViewById(R.id.tv_profile_name);
        if (tvProfileName != null) {
            tvProfileName.setText(sessionManager.getDoctorName());
        }
        
        ImageView profileImage = findViewById(R.id.profile_image);
        if (profileImage != null) {
            String picUrl = sessionManager.getProfilePicture();
            if (!picUrl.isEmpty()) {
                ImageUtils.loadImageFromUrl(this, profileImage, picUrl);
            }
        }
    }

    private void updateStorageUI() {
        TextView tvUsed = findViewById(R.id.used_txt);
        if (tvUsed != null) {
            long used = StorageUtils.getUsedInternalMemorySize();
            long total = StorageUtils.getTotalInternalMemorySize();
            tvUsed.setText(StorageUtils.formatSize(used));
            
            // Optional: Update total text if needed
            TextView tvTotal = findViewById(R.id.tv_total_storage); // If exists in XML
            if (tvTotal != null) {
                tvTotal.setText(StorageUtils.formatSize(total) + " Total");
            }
        }
    }

    private void performDataSync() {
        TextView tvLastSynced = findViewById(R.id.tv_last_synced);
        com.example.bladder_frontend.api.BladSenseApi api = com.example.bladder_frontend.api.RetrofitClient.getApi(this);
        api.syncData().enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    android.widget.Toast.makeText(SettingsActivity.this, "Data synced successfully", android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                android.widget.Toast.makeText(SettingsActivity.this, "Sync failed: " + t.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}
