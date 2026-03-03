package com.example.bladder_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AnalyzeActivity extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        ImageView btnBackToolbar = findViewById(R.id.btn_back_toolbar);
        if (btnBackToolbar != null) {
            btnBackToolbar.setOnClickListener(v -> onBackPressed());
        }

        final String viewType = getIntent().getStringExtra("VIEW_TYPE");
        final String imageUriString = getIntent().getStringExtra("IMAGE_URI");

        drawingView = findViewById(R.id.iv_captured_image);
        LinearLayout placeholderLayout = findViewById(R.id.placeholder_layout);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            if (drawingView != null && placeholderLayout != null) {
                drawingView.setImageURI(imageUri);
                drawingView.setVisibility(View.VISIBLE);
                placeholderLayout.setVisibility(View.GONE);
            }
        }

        // Only Retake button remains
        LinearLayout btnRetake = findViewById(R.id.btn_retake);
        if (btnRetake != null) {
            btnRetake.setOnClickListener(v -> {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Discard Scan?")
                        .setMessage("Are you sure you want to discard this image?")
                        .setPositiveButton("Retake", (dialog, which) -> {
                            Intent intent = new Intent(AnalyzeActivity.this, CaptureActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });
        }

        MaterialButton btnAnalyzeImage = findViewById(R.id.btnAnalyzeImage);
        if (btnAnalyzeImage != null) {
            btnAnalyzeImage.setOnClickListener(v -> {
                Intent intent = new Intent(AnalyzeActivity.this, ProcessingActivity.class);
                intent.putExtra("VIEW_TYPE", viewType);
                startActivity(intent);
            });
        }
    }
}
