package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Search navigation
        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(ReportActivity.this, SearchActivity.class));
            });
        }

        // Navigation to detailed reports with dynamic data passing
        setupReportNavigation(R.id.btn_report_james, "James Wilson", "R-1023", "Mar 10, 2024", "450 ml", "Normal");
        setupReportNavigation(R.id.btn_report_sarah, "Sarah Connor", "R-1022", "Mar 09, 2024", "620 ml", "Distended");
        setupReportNavigation(R.id.btn_report_robert, "Robert Smith", "R-1021", "Mar 08, 2024", "210 ml", "Normal");
        setupReportNavigation(R.id.btn_report_emily, "Emily Davis", "R-1020", "Mar 07, 2024", "150 ml", "Normal");

        // Navigation to Volume Trends (Report_fourActivity)
        MaterialButton btnTrends = findViewById(R.id.btn_trends);
        if (btnTrends != null) {
            btnTrends.setOnClickListener(v -> {
                startActivity(new Intent(ReportActivity.this, Report_fourActivity.class));
            });
        }

        // Navigation to Compare Reports (Report_fiveActivity)
        MaterialButton btnCompare = findViewById(R.id.btn_compare);
        if (btnCompare != null) {
            btnCompare.setOnClickListener(v -> {
                startActivity(new Intent(ReportActivity.this, Report_fiveActivity.class));
            });
        }

        // Navigation to Filter Reports (Report_sevenActivity)
        MaterialCardView btnFilter = findViewById(R.id.btn_filter);
        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> {
                startActivity(new Intent(ReportActivity.this, Report_sevenActivity.class));
            });
        }

        // Bottom Navigation Setup
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_reports);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.navigation_home) {
                        startActivity(new Intent(ReportActivity.this, HomePageActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_scan) {
                        startActivity(new Intent(ReportActivity.this, ScanActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_patients) {
                        startActivity(new Intent(ReportActivity.this, PatientActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_reports) {
                        return true;
                    } else if (id == R.id.navigation_settings) {
                        startActivity(new Intent(ReportActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void setupReportNavigation(int cardId, String name, String id, String date, String volume, String status) {
        MaterialCardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                Intent intent = new Intent(ReportActivity.this, Report_oneActivity.class);
                intent.putExtra("patient_name", name);
                intent.putExtra("report_id", id);
                intent.putExtra("scan_date", date);
                intent.putExtra("volume", volume);
                intent.putExtra("status", status);
                startActivity(intent);
            });
        }
    }
}
