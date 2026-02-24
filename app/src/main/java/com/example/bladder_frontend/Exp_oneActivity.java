package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class Exp_oneActivity extends AppCompatActivity {

    private LinearProgressIndicator progressBar;
    private TextView tvPercentage;
    private MaterialButton btnAction;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_one);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        progressBar = findViewById(R.id.progress_bar);
        tvPercentage = findViewById(R.id.tv_percentage);
        btnAction = findViewById(R.id.btn_action);

        startLoading();
    }

    private void startLoading() {
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    tvPercentage.setText(progressStatus + "% complete");
                });
                try {
                    Thread.sleep(30); // Speed for simulation
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.post(() -> {
                btnAction.setText("View Export");
                btnAction.setIcon(null);
                btnAction.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.bottom_nav_item_color)));
                btnAction.setOnClickListener(v -> {
                    Intent intent = new Intent(Exp_oneActivity.this, Exp_twoActivity.class);
                    startActivity(intent);
                    finish();
                });
            });
        }).start();
    }
}
