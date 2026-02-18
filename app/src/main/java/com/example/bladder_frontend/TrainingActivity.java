package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class TrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrainingActivity.this, Notification_oneActivity.class));
                }
            });
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrainingActivity.this, SearchActivity.class));
                }
            });
        }

        MaterialCardView btnUltrasoundBasics = findViewById(R.id.btn_ultrasound_basics);
        if (btnUltrasoundBasics != null) {
            btnUltrasoundBasics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrainingActivity.this, Training_oneActivity.class);
                    startActivity(intent);
                }
            });
        }

        MaterialCardView btnBladderVolume = findViewById(R.id.btn_bladder_volume);
        if (btnBladderVolume != null) {
            btnBladderVolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrainingActivity.this, Training_oneActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
