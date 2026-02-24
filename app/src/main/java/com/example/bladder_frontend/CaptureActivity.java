package com.example.bladder_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class CaptureActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

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

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);
        ImageView btnCapture = findViewById(R.id.btnCapture);
        LinearLayout btnGallery = findViewById(R.id.btn_gallery);

        // Register gallery launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            boolean isSagittal = toggleGroup.getCheckedButtonId() == R.id.btnSagittal;
                            Intent intent = new Intent(CaptureActivity.this, AnalyzeActivity.class);
                            intent.putExtra("VIEW_TYPE", isSagittal ? "sagittal" : "transverse");
                            intent.putExtra("IMAGE_URI", selectedImageUri.toString());
                            startActivity(intent);
                        }
                    }
                }
        );

        if (btnGallery != null) {
            btnGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryLauncher.launch(intent);
                }
            });
        }

        if (btnCapture != null) {
            btnCapture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSagittal = toggleGroup.getCheckedButtonId() == R.id.btnSagittal;
                    Intent intent = new Intent(CaptureActivity.this, AnalyzeActivity.class);
                    intent.putExtra("VIEW_TYPE", isSagittal ? "sagittal" : "transverse");
                    startActivity(intent);
                }
            });
        }
    }
}
