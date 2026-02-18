package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Report_oneActivity extends AppCompatActivity {

    private String patientName, reportId, scanDate, volume, status;

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

        // Fallback to defaults if data is missing
        if (patientName == null) patientName = "James Wilson";
        if (reportId == null) reportId = "R-1023";
        if (scanDate == null) scanDate = "Mar 10, 2024";
        if (volume == null) volume = "450 ml";
        if (status == null) status = "Normal";

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
                    // Pass data along if needed
                    startActivity(exportIntent);
                }
            });
        }

        LinearLayout btnPrint = findViewById(R.id.btn_print);
        if (btnPrint != null) {
            btnPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent printIntent = new Intent(Report_oneActivity.this, Report_threeActivity.class);
                    printIntent.putExtra("patient_name", patientName);
                    printIntent.putExtra("report_id", reportId);
                    printIntent.putExtra("scan_date", scanDate);
                    printIntent.putExtra("volume", volume);
                    startActivity(printIntent);
                }
            });
        }
    }
}
