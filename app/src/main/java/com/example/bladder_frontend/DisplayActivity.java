package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DisplayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        RelativeLayout btnLanguage = findViewById(R.id.btn_language);
        if (btnLanguage != null) {
            btnLanguage.setOnClickListener(v -> {
                startActivity(new Intent(DisplayActivity.this, LanguageActivity.class));
            });
        }

        MaterialButton btnAccessibility = findViewById(R.id.btn_accessibility);
        if (btnAccessibility != null) {
            btnAccessibility.setOnClickListener(v -> {
                startActivity(new Intent(DisplayActivity.this, Display_oneActivity.class));
            });
        }
    }
}
