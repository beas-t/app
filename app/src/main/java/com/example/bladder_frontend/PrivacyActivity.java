package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class PrivacyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnViewDataUsage = findViewById(R.id.btn_view_data_usage);
        if (btnViewDataUsage != null) {
            btnViewDataUsage.setOnClickListener(v -> {
                startActivity(new Intent(PrivacyActivity.this, Privacy_oneActivity.class));
            });
        }
    }
}
