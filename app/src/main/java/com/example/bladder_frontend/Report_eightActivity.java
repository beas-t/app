package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Report_eightActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_eight);

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

        MaterialButton btnGenerate = findViewById(R.id.btn_generate);
        if (btnGenerate != null) {
            btnGenerate.setOnClickListener(v -> {
                Intent intent = new Intent(Report_eightActivity.this, Report_nineActivity.class);
                startActivity(intent);
            });
        }
    }
}
