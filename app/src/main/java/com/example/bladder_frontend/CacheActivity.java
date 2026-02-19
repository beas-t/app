package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class CacheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        ImageView btnBack = findViewById(R.id.btn_back);
        MaterialButton btnClearCache = findViewById(R.id.btn_clear_cache);

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (btnClearCache != null) {
            btnClearCache.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Logic to clear cache would go here
                    Intent intent = new Intent(CacheActivity.this, Cache_oneActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
