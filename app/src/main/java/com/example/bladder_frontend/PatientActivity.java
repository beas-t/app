package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        // Navigation to SearchActivity
        ImageView searchIcon = findViewById(R.id.search_top_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PatientActivity.this, SearchActivity.class));
                }
            });
        }

        // Explicitly setup Navigation to Patient_twoActivity (Statistics)
        View btnViewStats = findViewById(R.id.btn_view_stats);
        if (btnViewStats != null) {
            btnViewStats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PatientActivity.this, Patient_twoActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Setup Navigation to Report_sevenActivity (Filter)
        View btnFilter = findViewById(R.id.btn_filter);
        if (btnFilter != null) {
            btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PatientActivity.this, Report_sevenActivity.class);
                    startActivity(intent);
                }
            });
        }

        findViewById(R.id.card_james_wilson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this, Patient_oneActivity.class);
                intent.putExtra("patient_name", "James Wilson");
                intent.putExtra("patient_id", "PT-8823");
                intent.putExtra("patient_age", "45");
                intent.putExtra("patient_gender", "Male");
                intent.putExtra("patient_scans", "4");
                startActivity(intent);
            }
        });

        findViewById(R.id.card_sarah_connor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this, Patient_oneActivity.class);
                intent.putExtra("patient_name", "Sarah Connor");
                intent.putExtra("patient_id", "PT-8824");
                intent.putExtra("patient_age", "32");
                intent.putExtra("patient_gender", "Female");
                intent.putExtra("patient_scans", "2");
                startActivity(intent);
            }
        });

        findViewById(R.id.card_robert_smith).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this, Patient_oneActivity.class);
                intent.putExtra("patient_name", "Robert Smith");
                intent.putExtra("patient_id", "PT-8825");
                intent.putExtra("patient_age", "68");
                intent.putExtra("patient_gender", "Male");
                intent.putExtra("patient_scans", "12");
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_patients);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.navigation_scan) {
                        startActivity(new Intent(PatientActivity.this, ScanActivity.class));
                        finish();
                        return true;
                    } else if (id == R.id.navigation_reports) {
                        startActivity(new Intent(PatientActivity.this, ReportActivity.class));
                        finish();
                        return true;
                    } else if (id == R.id.navigation_patients) {
                        return true;
                    } else if (id == R.id.navigation_settings) {
                        startActivity(new Intent(PatientActivity.this, SettingsActivity.class));
                        finish();
                        return true;
                    } else if (id == R.id.navigation_home) {
                        startActivity(new Intent(PatientActivity.this, HomePageActivity.class));
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
