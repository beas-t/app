package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Make the app full screen and handle status/navigation bars properly
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        
        setContentView(R.layout.activity_home_page);

        // Adjust for system bars (Status Bar and Navigation Bar) using WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, windowInsets) -> {
            int top = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int bottom = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            
            // Apply padding to the root layout or specific views to avoid overlap
            findViewById(R.id.toolbar).setPadding(0, top, 0, 0);
            findViewById(R.id.bottom_navigation).setPadding(0, 0, 0, bottom);
            
            return windowInsets;
        });

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
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
}
