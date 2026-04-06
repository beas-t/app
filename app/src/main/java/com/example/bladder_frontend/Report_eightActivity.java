package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import com.example.bladder_frontend.api.models.Patient;

public class Report_eightActivity extends AppCompatActivity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_eight);

        // Get patient and scan data from intent
        patient = (Patient) getIntent().getSerializableExtra("patient");
        String volume = getIntent().getStringExtra("volume");
        String status = getIntent().getStringExtra("status");

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(v -> {
                startActivity(new Intent(Report_eightActivity.this, Notification_oneActivity.class));
            });
        }
        
        TextView tvPreviewInfo = findViewById(R.id.tv_preview_info);
        if (tvPreviewInfo != null && patient != null) {
            String dateStr = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
            tvPreviewInfo.setText(patient.getName() + " • " + dateStr);
        }

        MaterialButton btnGenerate = findViewById(R.id.btn_generate);
        if (btnGenerate != null) {
            btnGenerate.setOnClickListener(v -> {
                Intent intent = new Intent(Report_eightActivity.this, Report_nineActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("volume", volume);
                intent.putExtra("status", status);
                startActivity(intent);
            });
        }
    }
}
