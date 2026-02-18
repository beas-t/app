package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Report_fiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_five);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(Report_fiveActivity.this, SearchActivity.class));
            });
        }

        MaterialButton btnDetailedComparison = findViewById(R.id.btn_detailed_comparison);
        if (btnDetailedComparison != null) {
            btnDetailedComparison.setOnClickListener(v -> {
                Intent intent = new Intent(Report_fiveActivity.this, Report_sixActivity.class);
                startActivity(intent);
            });
        }
    }
}
