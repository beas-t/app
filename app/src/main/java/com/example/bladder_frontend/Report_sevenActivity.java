package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Report_sevenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_seven);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(Report_sevenActivity.this, SearchActivity.class));
            });
        }

        MaterialButton btnClearAll = findViewById(R.id.btn_clear_all);
        if (btnClearAll != null) {
            btnClearAll.setOnClickListener(v -> {
                // Navigate back to Reports page
                finish();
            });
        }

        MaterialButton btnApplyFilters = findViewById(R.id.btn_apply_filters);
        if (btnApplyFilters != null) {
            btnApplyFilters.setOnClickListener(v -> {
                // Navigate back to Reports page
                finish();
            });
        }
    }
}
