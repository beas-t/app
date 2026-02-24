package com.example.bladder_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;

public class AnalyzeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

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

        final String viewType = getIntent().getStringExtra("VIEW_TYPE");
        final String imageUriString = getIntent().getStringExtra("IMAGE_URI");

        ImageView ivCapturedImage = findViewById(R.id.iv_captured_image);
        LinearLayout placeholderLayout = findViewById(R.id.placeholder_layout);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            if (ivCapturedImage != null && placeholderLayout != null) {
                ivCapturedImage.setImageURI(imageUri);
                ivCapturedImage.setVisibility(View.VISIBLE);
                placeholderLayout.setVisibility(View.GONE);
            }
        }

        // Action Buttons
        LinearLayout btnDraw = findViewById(R.id.btn_draw);
        LinearLayout btnMeasure = findViewById(R.id.btn_measure);
        LinearLayout btnFilter = findViewById(R.id.btn_filter);
        LinearLayout btnRetake = findViewById(R.id.btn_retake);

        if (btnDraw != null) {
            btnDraw.setOnClickListener(v -> {
                Intent intent = new Intent(AnalyzeActivity.this, DrawActivity.class);
                intent.putExtra("IMAGE_URI", imageUriString);
                startActivity(intent);
            });
        }

        if (btnMeasure != null) {
            btnMeasure.setOnClickListener(v -> Toast.makeText(this, "Manual Measurement Enabled", Toast.LENGTH_SHORT).show());
        }

        if (btnFilter != null) {
            btnFilter.setOnClickListener(v -> Toast.makeText(this, "Image Filters Applied", Toast.LENGTH_SHORT).show());
        }

        if (btnRetake != null) {
            btnRetake.setOnClickListener(v -> {
                // Return to Capture screen
                Intent intent = new Intent(AnalyzeActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        MaterialButton btnAnalyzeImage = findViewById(R.id.btnAnalyzeImage);
        if (btnAnalyzeImage != null) {
            btnAnalyzeImage.setOnClickListener(v -> {
                // Navigate to ProcessingActivity and pass the view type
                Intent intent = new Intent(AnalyzeActivity.this, ProcessingActivity.class);
                intent.putExtra("VIEW_TYPE", viewType);
                startActivity(intent);
            });
        }
    }
}
