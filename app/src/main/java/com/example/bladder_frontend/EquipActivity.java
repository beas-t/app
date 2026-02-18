package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class EquipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(EquipActivity.this, SearchActivity.class));
            });
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(v -> {
                startActivity(new Intent(EquipActivity.this, Notification_oneActivity.class));
            });
        }

        MaterialButton btnRecalibrate = findViewById(R.id.btn_recalibrate);
        if (btnRecalibrate != null) {
            btnRecalibrate.setOnClickListener(v -> {
                // Calibration logic
            });
        }

        MaterialButton btnDisconnect = findViewById(R.id.btn_disconnect);
        if (btnDisconnect != null) {
            btnDisconnect.setOnClickListener(v -> {
                // Disconnect logic
                finish();
            });
        }
    }
}
