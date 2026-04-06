package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.bladder_frontend.utils.StorageUtils;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.progressindicator.LinearProgressIndicator;

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
                    if (StorageUtils.clearCache(CacheActivity.this)) {
                        Toast.makeText(CacheActivity.this, "Cache cleared successfully", Toast.LENGTH_SHORT).show();
                        updateCacheUI();
                    } else {
                        Toast.makeText(CacheActivity.this, "Failed to clear cache", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        
        updateCacheUI();
    }

    private void updateCacheUI() {
        TextView tvCacheSize = findViewById(R.id.tv_cache_size);
        LinearProgressIndicator progressCache = findViewById(R.id.progress_cache);
        
        long cacheSize = StorageUtils.getCacheSize(this);
        if (tvCacheSize != null) {
            tvCacheSize.setText("Current size: " + StorageUtils.formatSize(cacheSize));
        }
        
        if (progressCache != null) {
            // Set arbitrary max or base on some logic, here just showing it works
            int progress = (int) (Math.min(cacheSize / (1024 * 1024), 100)); // 1MB = 1% for demo
            progressCache.setProgress(progress);
        }
    }
}
