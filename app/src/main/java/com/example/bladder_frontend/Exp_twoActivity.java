package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Exp_twoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_two);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnDownload = findViewById(R.id.btn_download);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> {
                // Logic to download ZIP file
            });
        }

        MaterialButton btnDone = findViewById(R.id.btn_done);
        if (btnDone != null) {
            btnDone.setOnClickListener(v -> {
                Intent intent = new Intent(Exp_twoActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}
