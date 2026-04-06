package com.example.bladder_frontend;
 
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Build;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Report_nineActivity extends AppCompatActivity {

    private static final String DIR_NAME = "Reports";
    private com.example.bladder_frontend.api.models.Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_nine);

        // Get patient from intent
        patient = (com.example.bladder_frontend.api.models.Patient) getIntent().getSerializableExtra("patient");

        TextView tvFileNameDisplay = findViewById(R.id.tv_file_name_display);
        if (tvFileNameDisplay != null) {
            tvFileNameDisplay.setText(getDynamicFileName());
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(v -> {
                startActivity(new Intent(Report_nineActivity.this, Notification_oneActivity.class));
            });
        }

        MaterialButton btnDownload = findViewById(R.id.btn_download);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> handleDownload());
        }

        MaterialButton btnShare = findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> handleShare());
        }

        TextView btnDone = findViewById(R.id.btn_done);
        if (btnDone != null) {
            btnDone.setOnClickListener(v -> {
                Intent intent = new Intent(Report_nineActivity.this, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
 
        // Request Notification Permission for Android 13+
        checkNotificationPermission();
    }
 
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void handleDownload() {
        File file = getPdfFile();
        
        // Professional Progress Feedback
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating Professional Report...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Retrieve dynamic scan results
        String volume = getIntent().getStringExtra("volume");
        String status = getIntent().getStringExtra("status");
        if (volume == null) volume = "0.0";
        if (status == null) status = "Unknown";

        // Always recreate to ensure it's the professional version if parameters changed
        PdfAssistant.createProfessionalReport(this, file, volume, status, patient, success -> {
            progressDialog.dismiss();
            if (success) {
                Toast.makeText(this, "Report Downloaded Successfully", Toast.LENGTH_LONG).show();
                // Optionally auto-open the report or provide a more prominent success state
            } else {
                Toast.makeText(this, "Failed to generate professional report", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleShare() {
        File file = getPdfFile();
        String volume = getIntent().getStringExtra("volume");
        String status = getIntent().getStringExtra("status");
        if (volume == null) volume = "100.5";
        if (status == null) status = "Normal Range";
        
        PdfAssistant.createProfessionalReport(this, file, volume, status, patient, success -> {
            if (!success) {
                Toast.makeText(this, "Could not prepare professional report for sharing", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                startActivity(Intent.createChooser(shareIntent, "Share Professional Report via"));
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Error sharing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDynamicFileName() {
        if (patient != null) {
            String name = patient.getName().replaceAll("[^a-zA-Z0-9]", "_");
            String id = patient.getPatientId().replaceAll("[^a-zA-Z0-9]", "_");
            return "Report-" + name + "-" + id + ".pdf";
        }
        return "Report-Unknown.pdf";
    }

    private File getPdfFile() {
        File directory = new File(getExternalFilesDir(null), DIR_NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, getDynamicFileName());
    }
}
