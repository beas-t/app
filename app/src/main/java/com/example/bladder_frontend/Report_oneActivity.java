package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bladder_frontend.api.models.Patient;

public class Report_oneActivity extends AppCompatActivity {

    private String patientName, reportId, scanDate, volume, status;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_one);

        // Retrieve data from Intent
        Intent intent = getIntent();
        patientName = intent.getStringExtra("patient_name");
        reportId = intent.getStringExtra("report_id");
        scanDate = intent.getStringExtra("scan_date");
        volume = intent.getStringExtra("volume");
        status = intent.getStringExtra("status");
        patient = (Patient) intent.getSerializableExtra("patient");

        // Fallback to defaults if data is missing
        // Fallback to dynamic data if available, then generic
        if (patientName == null && patient != null) patientName = patient.getName();
        if (patientName == null) patientName = "Patient Name";
        if (reportId == null) reportId = "R-0000";
        if (scanDate == null) scanDate = "Date Unknown";
        if (volume == null) volume = "0 ml";
        if (status == null) status = "Unknown";

        // Update UI components
        TextView tvToolbarId = findViewById(R.id.tv_toolbar_id);
        TextView tvPatientName = findViewById(R.id.tv_patient_name);
        TextView tvStatusBadge = findViewById(R.id.tv_status_badge);
        TextView tvReportDate = findViewById(R.id.tv_report_date);
        TextView tvVolumeValue = findViewById(R.id.tv_volume_value);

        if (tvToolbarId != null) tvToolbarId.setText(reportId);
        if (tvPatientName != null) tvPatientName.setText(patientName);
        if (tvReportDate != null) tvReportDate.setText(scanDate);
        if (tvVolumeValue != null) tvVolumeValue.setText(volume.replace(" ml", ""));
        
        if (tvStatusBadge != null) {
            tvStatusBadge.setText(status);
            if ("Distended".equalsIgnoreCase(status)) {
                tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tag_grey));
                tvStatusBadge.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_orange_light));
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
            } else {
                tvStatusBadge.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tag_green));
                tvStatusBadge.setTextColor(ContextCompat.getColor(this, R.color.bottom_nav_item_color)); // Assuming green is defined or use custom
            }
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        LinearLayout btnExport = findViewById(R.id.btn_export);
        if (btnExport != null) {
            btnExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent exportIntent = new Intent(Report_oneActivity.this, Report_twoActivity.class);
                    exportIntent.putExtra("patient", patient);
                    exportIntent.putExtra("volume", volume);
                    exportIntent.putExtra("status", status);
                    startActivity(exportIntent);
                }
            });
        }
    }
}
