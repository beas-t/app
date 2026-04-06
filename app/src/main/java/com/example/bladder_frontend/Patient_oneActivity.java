package com.example.bladder_frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Patient;
import com.example.bladder_frontend.api.models.ScanReport;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.LayoutInflater;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Patient_oneActivity extends AppCompatActivity {

    private Patient patient;
    private TextView nameTextView, idTextView, ageTextView, genderTextView, avatarTextView, scansCountTextView, historyTextView;
    private LinearLayout timelineContainer;
    private ActivityResultLauncher<Intent> editActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_one);

        // Get patient data from Intent
        patient = (Patient) getIntent().getSerializableExtra("patient");

        // UI References
        nameTextView = findViewById(R.id.tv_patient_name);
        idTextView = findViewById(R.id.tv_patient_id);
        ageTextView = findViewById(R.id.tv_patient_age);
        genderTextView = findViewById(R.id.tv_patient_gender);
        avatarTextView = findViewById(R.id.tv_avatar_text);
        scansCountTextView = findViewById(R.id.tv_patient_scans);
        historyTextView = findViewById(R.id.tv_patient_history);
        timelineContainer = findViewById(R.id.timeline_container);
        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView searchIcon = findViewById(R.id.search_top_icon);
        View btnEditProfile = findViewById(R.id.btn_edit_profile);

        // Initialize ActivityResultLauncher
        editActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Patient updatedPatient = (Patient) result.getData().getSerializableExtra("updated_patient");
                        if (updatedPatient != null) {
                            this.patient = updatedPatient;
                            populateUI();
                            fetchClinicalHistory(); // Refresh history too
                        }
                    }
                }
        );

        // Populate UI
        populateUI();
        fetchClinicalHistory();

        // Archive / Unarchive Button
        com.google.android.material.button.MaterialButton btnArchive = findViewById(R.id.btn_archive);
        if (btnArchive != null && patient != null) {
            if (patient.isArchived()) {
                btnArchive.setText("Restore Patient");
                btnArchive.setOnClickListener(v -> handleUnarchive());
            } else {
                btnArchive.setOnClickListener(v -> handleArchive());
            }
        }

        // Back Navigation
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Search Navigation
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                // Navigate to CaptureActivity
                Intent intent = new Intent(Patient_oneActivity.this, SearchActivity.class);
                intent.putExtra("patient", patient); // Pass the patient object
                startActivity(intent);
            }
});
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Patient_oneActivity.this, Notification_oneActivity.class));
                }
            });
        }

        // Edit Profile Navigation
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Patient_oneActivity.this, Patient_threeActivity.class);
                    intent.putExtra("patient", patient);
                    editActivityResultLauncher.launch(intent);
                }
            });
        }

        // Quick Action Navigations
        findViewById(R.id.btn_new_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_oneActivity.this, ScanActivity.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_oneActivity.this, ReportActivity.class));
            }
        });


        findViewById(R.id.btn_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Patient_oneActivity.this, Report_twoActivity.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });
    }

    private void populateUI() {
        if (patient != null) {
            nameTextView.setText(patient.getName());
            avatarTextView.setText(String.valueOf(patient.getName().charAt(0)));
            idTextView.setText(patient.getPatientId());
            ageTextView.setText(String.valueOf(patient.getAge()));
            genderTextView.setText(patient.getGender());
            if (historyTextView != null) {
                historyTextView.setText(patient.getCondition() != null && !patient.getCondition().isEmpty() 
                    ? patient.getCondition() : "No history recorded.");
            }
        }
    }

    private void fetchClinicalHistory() {
        if (patient == null) return;

        BladSenseApi api = RetrofitClient.getApi(this);
        api.getReports(patient.getId(), null).enqueue(new Callback<List<ScanReport>>() {
            @Override
            public void onResponse(Call<List<ScanReport>> call, Response<List<ScanReport>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateTimeline(response.body());
                    if (scansCountTextView != null) {
                        scansCountTextView.setText(String.valueOf(response.body().size()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScanReport>> call, Throwable t) {
                // Fail silently or show error
            }
        });
    }

    private void updateTimeline(List<ScanReport> reports) {
        if (timelineContainer == null) return;
        timelineContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < reports.size(); i++) {
            ScanReport report = reports.get(i);
            View timelineItem = inflater.inflate(R.layout.item_timeline, timelineContainer, false);

            TextView tvDate = timelineItem.findViewById(R.id.tv_date);
            TextView tvTitle = timelineItem.findViewById(R.id.tv_title);
            TextView tvDesc = timelineItem.findViewById(R.id.tv_description);
            View dot = timelineItem.findViewById(R.id.dot);
            View line = timelineItem.findViewById(R.id.line);

            // Set date
            try {
                // Backend usually returns YYYY-MM-DD
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date date = inputFormat.parse(report.getScanDate());
                tvDate.setText(outputFormat.format(date));
            } catch (Exception e) {
                tvDate.setText(report.getScanDate());
            }

            tvTitle.setText("Bladder Scan - " + report.getStatus());
            tvDesc.setText("Measured " + report.getVolume() + ". " + (report.getNotes() != null ? report.getNotes() : ""));

            // Visual Styling
            if (report.getStatus().equalsIgnoreCase("High") || report.getStatus().toLowerCase().contains("distended")) {
                dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFEF4444)); // Red
            } else {
                dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF3B82F6)); // Blue
            }

            // Hide line for last item
            if (i == reports.size() - 1) {
                line.setVisibility(View.GONE);
            }

            timelineContainer.addView(timelineItem);
        }

        // If no reports, show a placeholder
        if (reports.isEmpty()) {
            addAssessmentItem("Initial Assessment", "No scan reports found yet.", "Feb 24, 2024", 0xFF9CA3AF);
        }
    }

    private void addAssessmentItem(String title, String desc, String date, int dotColor) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View timelineItem = inflater.inflate(R.layout.item_timeline, timelineContainer, false);
        ((TextView) timelineItem.findViewById(R.id.tv_title)).setText(title);
        ((TextView) timelineItem.findViewById(R.id.tv_description)).setText(desc);
        ((TextView) timelineItem.findViewById(R.id.tv_date)).setText(date);
        timelineItem.findViewById(R.id.dot).setBackgroundTintList(android.content.res.ColorStateList.valueOf(dotColor));
        timelineItem.findViewById(R.id.line).setVisibility(View.GONE);
        timelineContainer.addView(timelineItem);
    }

    private void handleArchive() {
        if (patient == null) return;

        BladSenseApi api = RetrofitClient.getApi(this);
        api.archivePatient(patient.getId()).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Patient_oneActivity.this, "Patient archived successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(Patient_oneActivity.this, "Failed to archive patient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(Patient_oneActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleUnarchive() {
        if (patient == null) return;

        BladSenseApi api = RetrofitClient.getApi(this);
        api.unarchivePatient(patient.getId()).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Patient_oneActivity.this, "Patient restored successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(Patient_oneActivity.this, "Failed to restore patient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(Patient_oneActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
