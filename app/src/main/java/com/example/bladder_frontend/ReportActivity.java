package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.ScanReport;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private RecyclerView rvReports;
    private ReportAdapter adapter;
    private List<ScanReport> reportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // RecyclerView Setup
        rvReports = findViewById(R.id.rv_reports);
        rvReports.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportAdapter(reportList, this);
        rvReports.setAdapter(adapter);

        // Fetch Data from API
        fetchReports();

        // Search navigation
        ImageView searchIcon = findViewById(R.id.ic_search_small);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(ReportActivity.this, SearchActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        fetchReports();
    }

    private void fetchReports() {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getReports(null, null).enqueue(new Callback<List<ScanReport>>() {
            @Override
            public void onResponse(Call<List<ScanReport>> call, Response<List<ScanReport>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reportList = response.body();
                    adapter.updateData(reportList);
                }
            }

            @Override
            public void onFailure(Call<List<ScanReport>> call, Throwable t) {
                // Silently fail for now or show Toast
            }
        });
    }
}
