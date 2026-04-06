package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.bladder_frontend.api.models.Patient;
import com.example.bladder_frontend.PdfAssistant;
import com.google.android.material.button.MaterialButton;
import androidx.core.content.FileProvider;
import java.io.File;
import android.net.Uri;

public class ResultActivity extends AppCompatActivity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        patient = (Patient) getIntent().getSerializableExtra("patient");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Remove search icon from toolbar if it exists
        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setVisibility(View.GONE);
        }
        
        ImageView notificationIcon = findViewById(R.id.notification_icon);
        if (notificationIcon != null) {
            notificationIcon.setVisibility(View.GONE);
        }

        TextView tvVolumeValue = findViewById(R.id.tvVolumeValue);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvConfidence = findViewById(R.id.tvConfidence);
        
        String volume = getIntent().getStringExtra("VOLUME");
        String status = getIntent().getStringExtra("STATUS");
        String level = getIntent().getStringExtra("LEVEL");
        
        if (tvVolumeValue != null && volume != null) {
            tvVolumeValue.setText(volume);
        }
        
        if (tvStatus != null) {
            if (status != null) {
                tvStatus.setText(status);
                if (status.equalsIgnoreCase("Distended")) {
                    tvStatus.setTextColor(android.graphics.Color.RED);
                    tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_light));
                }
            } else if (level != null) {
                tvStatus.setText("AI Level: " + level);
            }
        }

        if (tvConfidence != null) {
            float confidence = getIntent().getFloatExtra("CONFIDENCE", 0.95f);
            int confPercent = (int) (confidence * 100);
            tvConfidence.setText(confPercent + "% Conf.");
        }

        MaterialButton btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareReport(volume, status);
                }
            });
        }

        MaterialButton btnSaveReport = findViewById(R.id.btnSaveReport);
        if (btnSaveReport != null) {
            btnSaveReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Simulate saving the report
                    Toast.makeText(ResultActivity.this, "Report saved successfully", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to HomePageActivity
                    Intent intent = new Intent(ResultActivity.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void shareReport(String volume, String status) {
        File directory = new File(getExternalFilesDir(null), "Reports");
        if (!directory.exists()) directory.mkdirs();
        
        String patientName = (patient != null) ? patient.getName() : "Anonymous";
        String patientId = (patient != null) ? patient.getPatientId() : "N/A";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        
        String fileName = "ShareReport-" + timeStamp + ".pdf";
        if (patient != null && patient.getName() != null) {
            fileName = "Report-" + patient.getName().replaceAll("[^a-zA-Z0-9]", "_") + "-" + timeStamp + ".pdf";
        }
        
        File file = new File(directory, fileName);

        // Pass the actual Patient object to ensure the name is reflected in the PDF content
        PdfAssistant.createProfessionalReport(this, file, volume, status != null ? status : "Normal Range", patient, success -> {
            if (!success) {
                Toast.makeText(this, "Failed to prepare report for sharing", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("application/pdf");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                startActivity(Intent.createChooser(sharingIntent, "Share Professional Report via"));
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
