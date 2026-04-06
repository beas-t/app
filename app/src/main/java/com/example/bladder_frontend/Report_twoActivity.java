package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import com.example.bladder_frontend.api.models.Patient;

public class Report_twoActivity extends AppCompatActivity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_two);

        // Get patient and scan data from intent
        patient = (Patient) getIntent().getSerializableExtra("patient");
        String volume = getIntent().getStringExtra("volume");
        String status = getIntent().getStringExtra("status");

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(Report_twoActivity.this, SearchActivity.class));
            });
        }

        // PDF Document Card Navigation
        MaterialCardView cardPdf = findViewById(R.id.card_pdf);
        if (cardPdf != null) {
            cardPdf.setOnClickListener(v -> {
                Intent intent = new Intent(Report_twoActivity.this, Report_eightActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("volume", volume);
                intent.putExtra("status", status);
                startActivity(intent);
            });
        }

        MaterialButton btnContinue = findViewById(R.id.btn_continue);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                Intent intent = new Intent(Report_twoActivity.this, Report_eightActivity.class);
                intent.putExtra("patient", patient);
                intent.putExtra("volume", volume);
                intent.putExtra("status", status);
                startActivity(intent);
            });
        }
    }
}
