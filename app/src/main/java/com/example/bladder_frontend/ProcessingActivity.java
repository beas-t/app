package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProcessingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        TextView tvProcessingSubtext = findViewById(R.id.tvProcessingSubtext);
        String viewType = getIntent().getStringExtra("VIEW_TYPE");
        
        if (viewType != null && viewType.equals("sagittal")) {
            tvProcessingSubtext.setText("Calculating volume from sagittal view...");
        } else {
            tvProcessingSubtext.setText("Calculating volume from transverse view...");
        }

        // Simulate processing and navigate to ResultActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ProcessingActivity.this, ResultActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3 seconds delay
    }
}
