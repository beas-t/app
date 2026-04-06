package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Patient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientActivity extends AppCompatActivity {

    private RecyclerView rvPatients;
    private PatientAdapter adapter;
    private List<Patient> patientList = new ArrayList<>();
    private TextView tvPatientCount;
    private EditText etSearch;
    private boolean showingArchived = false;
    private com.google.android.material.button.MaterialButton btnToggleArchived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        tvPatientCount = findViewById(R.id.tv_patient_count);
        etSearch = findViewById(R.id.et_search);
        rvPatients = findViewById(R.id.rv_patients);
        rvPatients.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(patientList, this);
        rvPatients.setAdapter(adapter);

        btnToggleArchived = findViewById(R.id.btn_toggle_archived);
        if (btnToggleArchived != null) {
            btnToggleArchived.setOnClickListener(v -> {
                showingArchived = !showingArchived;
                btnToggleArchived.setText(showingArchived ? "Show Active" : "Show Archived");
                tvPatientCount.setText(showingArchived ? "Archived Patients" : "All Patients");
                fetchPatients();
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
                    // Assuming this was meant to go to Patient_threeActivity based on the broken code
                    Intent intent = new Intent(PatientActivity.this, Patient_threeActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_patients);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.navigation_home) {
                        startActivity(new Intent(PatientActivity.this, HomePageActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_scan) {
                        startActivity(new Intent(PatientActivity.this, ScanActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_patients) {
                        return true;
                    } else if (id == R.id.navigation_reports) {
                        startActivity(new Intent(PatientActivity.this, ReportActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    } else if (id == R.id.navigation_settings) {
                        startActivity(new Intent(PatientActivity.this, SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }

        fetchPatients();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPatients();
    }

    private void fetchPatients() {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getPatients(null, showingArchived).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    patientList = response.body();
                    adapter.updateData(patientList);
                    if (tvPatientCount != null) {
                        tvPatientCount.setText("All Patients (" + patientList.size() + ")");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                // Silently fail
            }
        });
    }
}
