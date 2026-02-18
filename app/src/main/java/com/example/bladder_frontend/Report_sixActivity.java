package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Report_sixActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_six);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(Report_sixActivity.this, SearchActivity.class));
            });
        }

        MaterialButton btnBackToSelection = findViewById(R.id.btn_back_to_selection);
        if (btnBackToSelection != null) {
            btnBackToSelection.setOnClickListener(v -> finish());
        }

        MaterialButton btnExport = findViewById(R.id.btn_export_comparison);
        if (btnExport != null) {
            btnExport.setOnClickListener(v -> {
                // Logic for exporting comparison
            });
        }
    }
}
