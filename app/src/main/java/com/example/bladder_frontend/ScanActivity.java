package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Using custom back button in layout

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScanActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        // Search navigation
        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ScanActivity.this, SearchActivity.class));
                }
            });
        }

        CardView aiAutoMeasureCard = findViewById(R.id.ai_auto_measure_card);
        CardView manualModeCard = findViewById(R.id.manual_mode_card);

        View.OnClickListener captureNavigationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CaptureActivity
                Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        };

        if (aiAutoMeasureCard != null) aiAutoMeasureCard.setOnClickListener(captureNavigationListener);
        if (manualModeCard != null) manualModeCard.setOnClickListener(captureNavigationListener);
    }
}
