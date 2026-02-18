package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Patient_oneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_one);

        // Get patient data from Intent
        final String patientName = getIntent().getStringExtra("patient_name");
        final String patientId = getIntent().getStringExtra("patient_id");
        final String patientAge = getIntent().getStringExtra("patient_age");
        final String patientGender = getIntent().getStringExtra("patient_gender");

        // UI References
        TextView nameTextView = findViewById(R.id.tv_patient_name);
        TextView idTextView = findViewById(R.id.tv_patient_id);
        TextView ageTextView = findViewById(R.id.tv_patient_age);
        TextView genderTextView = findViewById(R.id.tv_patient_gender);
        TextView avatarTextView = findViewById(R.id.tv_avatar_text);
        ImageView btnBack = findViewById(R.id.btn_back);
        ImageView searchIcon = findViewById(R.id.search_top_icon);
        View btnEditProfile = findViewById(R.id.btn_edit_profile);

        // Populate UI
        if (patientName != null) {
            nameTextView.setText(patientName);
            avatarTextView.setText(String.valueOf(patientName.charAt(0)));
        }
        if (patientId != null) idTextView.setText(patientId);
        if (patientAge != null) ageTextView.setText(patientAge);
        if (patientGender != null) genderTextView.setText(patientGender);

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
                    startActivity(new Intent(Patient_oneActivity.this, SearchActivity.class));
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
                    startActivity(new Intent(Patient_oneActivity.this, Patient_threeActivity.class));
                }
            });
        }

        // Quick Action Navigations
        findViewById(R.id.btn_new_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_oneActivity.this, ScanActivity.class));
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
                startActivity(new Intent(Patient_oneActivity.this, Report_twoActivity.class));
            }
        });
    }
}
