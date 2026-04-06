package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.api.models.DashboardData;
import com.example.bladder_frontend.utils.ImageUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends BaseActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the app full screen and handle status/navigation bars properly
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_home_page);

        welcomeTextView = findViewById(R.id.tv_doctor_name);
        TextView tvSpecialist = findViewById(R.id.tv_specialist);

        SessionManager sessionManager = new SessionManager(this);
        String savedName = sessionManager.getDoctorName();
        String savedSpecialty = sessionManager.getDoctorSpecialty();
        String savedLicense = sessionManager.getDoctorLicense();

        if (!savedName.isEmpty()) {
            welcomeTextView.setText(savedName);
        }
        if (!savedSpecialty.isEmpty() && !savedLicense.isEmpty()) {
            if (tvSpecialist != null) {
                tvSpecialist.setText(savedSpecialty + " • " + savedLicense);
            }
        }

        ImageView ivDoctor = findViewById(R.id.iv_doctor);
        String savedPic = sessionManager.getProfilePicture();
        if (ivDoctor != null && !savedPic.isEmpty()) {
            ImageUtils.loadImageFromUrl(this, ivDoctor, savedPic);
        }


        // Adjust for system bars (Status Bar and Navigation Bar) using WindowInsets
        View rootLayout = findViewById(android.R.id.content);
        if (rootLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootLayout, (v, windowInsets) -> {
                int top = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                int bottom = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

                // Apply padding to the root layout or specific views to avoid overlap
                View toolbar = findViewById(R.id.toolbar);
                if (toolbar != null) {
                    toolbar.setPadding(0, top, 0, 0);
                }
                
                View bottomNav = findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setPadding(0, 0, 0, bottom);
                }

                return windowInsets;
            });
        }

        // Search navigation
        ImageView searchIcon = findViewById(R.id.search_top_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageActivity.this, SearchActivity.class));
                }
            });
        }

        // Notification navigation
        ImageView notificationIcon = findViewById(R.id.notification_icon);
        if (notificationIcon != null) {
            notificationIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomePageActivity.this, Notification_oneActivity.class));
                }
            });
        }

        CardView newScanCard = findViewById(R.id.new_scan_card);
        if (newScanCard != null) {
            newScanCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomePageActivity.this, ScanActivity.class);
                    startActivity(intent);
                }
            });
        }

        CardView viewReportsCard = findViewById(R.id.view_reports_card);
        if (viewReportsCard != null) {
            viewReportsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomePageActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
            });
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            bottomNavigationView
                    .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.navigation_scan) {
                                startActivity(new Intent(HomePageActivity.this, ScanActivity.class));
                                return true;
                            } else if (id == R.id.navigation_reports) {
                                startActivity(new Intent(HomePageActivity.this, ReportActivity.class));
                                return true;
                            } else if (id == R.id.navigation_patients) {
                                startActivity(new Intent(HomePageActivity.this, PatientActivity.class));
                                return true;
                            } else if (id == R.id.navigation_settings) {
                                startActivity(new Intent(HomePageActivity.this, SettingsActivity.class));
                                return true;
                            } else if (id == R.id.navigation_home) {
                                return true;
                            }
                            return false;
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh session data
        SessionManager sessionManager = new SessionManager(this);
        welcomeTextView.setText(sessionManager.getDoctorName());
        
        TextView tvSpecialist = findViewById(R.id.tv_specialist);
        if (tvSpecialist != null) {
            tvSpecialist.setText(sessionManager.getDoctorSpecialty() + " • " + sessionManager.getDoctorLicense());
        }

        ImageView ivDoctor = findViewById(R.id.iv_doctor);
        String savedPic = sessionManager.getProfilePicture();
        if (ivDoctor != null && !savedPic.isEmpty()) {
            ImageUtils.loadImageFromUrl(this, ivDoctor, savedPic);
        }
        
        fetchDashboardData();
    }

    private void fetchDashboardData() {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getHomeDashboard().enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                if (isFinishing() || isDestroyed()) return;
                if (response.isSuccessful() && response.body() != null) {
                    DashboardData data = response.body();
                    runOnUiThread(() -> updateUI(data));
                }
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                Toast.makeText(HomePageActivity.this, "Failed to load dashboard data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(DashboardData data) {
        if (welcomeTextView != null) {
            welcomeTextView.setText(data.getDoctorName());
        }

        ImageView ivDoctor = findViewById(R.id.iv_doctor);
        if (ivDoctor != null && data.getProfilePicture() != null) {
            ImageUtils.loadImageFromUrl(this, ivDoctor, data.getProfilePicture());
            
            // Keep session in sync
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.saveProfilePicture(data.getProfilePicture());
        }

        LinearLayout llActivity = findViewById(R.id.ll_recent_activity);
        if (llActivity != null) {
            llActivity.removeAllViews();
            List<DashboardData.RecentActivity> activities = data.getRecentActivities();
            if (activities != null && !activities.isEmpty()) {
                for (int i = 0; i < activities.size(); i++) {
                    addActivityItem(llActivity, activities.get(i), i == activities.size() - 1);
                }
            } else {
                TextView noActivity = new TextView(this);
                noActivity.setText("No recent activity");
                noActivity.setPadding(0, 20, 0, 20);
                noActivity.setGravity(android.view.Gravity.CENTER);
                llActivity.addView(noActivity);
            }
        }
        
        TextView tvSpecialty = findViewById(R.id.tv_specialist);
        if (tvSpecialty != null) {
            tvSpecialty.setText(data.getSpecialty() + " • " + data.getLicenseNumber());
        }

        // Team Counts
        updateStatCard(R.id.doctors_count, R.id.doctors_active, data.getDoctorCount(), data.getActiveDoctors(), "signed in today");
        updateStatCard(R.id.patients_count, R.id.patients_active, data.getPatientCount(), data.getActivePatients(), "attended by doctors");

        // Tasks count
        TextView tvTasks = findViewById(R.id.tv_tasks_today);
        if (tvTasks != null) {
            tvTasks.setText(data.getTaskCount() + " tasks\ntoday");
        }
    }

    private void addActivityItem(LinearLayout parent, DashboardData.RecentActivity activity, boolean isLast) {
        android.view.View view = android.view.LayoutInflater.from(this).inflate(R.layout.item_recent_activity, parent, false);
        
        ImageView icon = view.findViewById(R.id.iv_activity_icon);
        TextView title = view.findViewById(R.id.tv_title);
        TextView subtitle = view.findViewById(R.id.tv_subtitle);
        TextView time = view.findViewById(R.id.tv_time);

        if (title != null) title.setText(activity.getTitle());
        if (subtitle != null) subtitle.setText(activity.getSubtitle());
        if (time != null) time.setText(activity.getRelativeTime());

        if (icon != null) {
            if ("Feedback".equalsIgnoreCase(activity.getType())) {
                icon.setImageResource(R.drawable.ic_mail);
            } else {
                icon.setImageResource(R.drawable.ic_scan);
            }
        }

        parent.addView(view);

        if (!isLast) {
            android.view.View divider = new android.view.View(this);
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.setMargins(0, 8, 0, 8);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(android.graphics.Color.parseColor("#F3F4F6"));
            parent.addView(divider);
        }
    }

    private void updateStatCard(int countId, int activeId, int total, int active, String statusSuffix) {
        TextView tvCount = findViewById(countId);
        TextView tvActive = findViewById(activeId);
        if (tvCount != null) tvCount.setText(String.valueOf(total));
        if (tvActive != null) tvActive.setText(active + " " + statusSuffix);
    }
}
