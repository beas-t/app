package com.example.bladder_frontend;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        TextView btnSave = findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> finish());
        }

        String imageUriString = getIntent().getStringExtra("IMAGE_URI");
        ImageView ivEditImage = findViewById(R.id.iv_edit_image);
        if (imageUriString != null && ivEditImage != null) {
            ivEditImage.setImageURI(Uri.parse(imageUriString));
        }
    }
}
