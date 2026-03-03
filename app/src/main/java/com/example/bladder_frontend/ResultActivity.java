package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Remove search icon from toolbar if it exists
        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setVisibility(View.GONE);
        }
        
        ImageView notificationIcon = findViewById(R.id.notification_icon);
        if (notificationIcon != null) {
            notificationIcon.setVisibility(View.GONE);
        }

        TextView tvVolumeValue = findViewById(R.id.tvVolumeValue);
        String volume = tvVolumeValue != null ? tvVolumeValue.getText().toString() : "450";

        MaterialButton btnShare = findViewById(R.id.btnShare);
        if (btnShare != null) {
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareReport(volume);
                }
            });
        }

        MaterialButton btnSaveReport = findViewById(R.id.btnSaveReport);
        if (btnSaveReport != null) {
            btnSaveReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Simulate saving the report
                    Toast.makeText(ResultActivity.this, "Report saved successfully", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to HomePageActivity
                    Intent intent = new Intent(ResultActivity.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void shareReport(String volume) {
        String shareBody = "BladSense AI Analysis Result\n" +
                "Calculated Bladder Volume: " + volume + " ml\n" +
                "Status: Normal Range\n" +
                "Generated via BladSense AI App";

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bladder Analysis Report");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        
        startActivity(Intent.createChooser(sharingIntent, "Share Report via"));
    }
}
