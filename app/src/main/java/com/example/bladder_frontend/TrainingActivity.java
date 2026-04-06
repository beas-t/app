package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import android.widget.TextView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.TrainingModule;
import com.example.bladder_frontend.api.models.TrainingSummary;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.bladder_frontend.api.SessionManager;
import android.util.Log;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity {
    private BladSenseApi api;
    private TextView tvTechnicianLevel, tvCompletionPercent;
    private LinearProgressIndicator progressCompletion;
    private TextView tvModuleTitle1, tvModuleInfo1, tvModuleTitle2, tvModuleInfo2;
    private LinearProgressIndicator progress1, progress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrainingActivity.this, Notification_oneActivity.class));
                }
            });
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TrainingActivity.this, SearchActivity.class));
                }
            });
        }

        MaterialCardView btnUltrasoundBasics = findViewById(R.id.btn_ultrasound_basics);
        if (btnUltrasoundBasics != null) {
            btnUltrasoundBasics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrainingActivity.this, Training_oneActivity.class);
                    intent.putExtra("module_id", 1);
                    intent.putExtra("title", "Ultrasound Basics");
                    intent.putExtra("description", "Probe handling & physics");
                    intent.putExtra("video_url", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"); // Demo URL
                    startActivity(intent);
                }
            });
        }

        MaterialCardView btnBladderVolume = findViewById(R.id.btn_bladder_volume);
        if (btnBladderVolume != null) {
            btnBladderVolume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TrainingActivity.this, Training_oneActivity.class);
                    intent.putExtra("module_id", 2);
                    intent.putExtra("title", "Bladder Volume Measurement");
                    intent.putExtra("description", "Standard protocol for measurements");
                    intent.putExtra("video_url", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"); // Demo URL
                    startActivity(intent);
                }
            });
        }

        // Initialize Views
        tvTechnicianLevel = findViewById(R.id.tvTechnicianLevel);
        tvCompletionPercent = findViewById(R.id.tvCompletionPercent);
        progressCompletion = findViewById(R.id.progress_completion);

        tvModuleTitle1 = findViewById(R.id.tvModuleTitle1);
        tvModuleInfo1 = findViewById(R.id.tvModuleInfo1);
        progress1 = findViewById(R.id.progress1);

        tvModuleTitle2 = findViewById(R.id.tvModuleTitle2);
        tvModuleInfo2 = findViewById(R.id.tvModuleInfo2);
        progress2 = findViewById(R.id.progress2);

        // Initialize API
        api = RetrofitClient.getApi(this);

        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void loadDashboardData() {
        // Fetch Summary
        api.getTrainingSummary().enqueue(new Callback<TrainingSummary>() {
            @Override
            public void onResponse(Call<TrainingSummary> call, Response<TrainingSummary> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrainingSummary summary = response.body();
                    tvTechnicianLevel.setText(summary.getTechnicianLevel());
                    tvCompletionPercent.setText(summary.getCompletionPercent() + "%");
                    progressCompletion.setProgress(summary.getCompletionPercent());
                }
            }

            @Override
            public void onFailure(Call<TrainingSummary> call, Throwable t) {
                Log.e("TrainingActivity", "Summary Error: " + t.getMessage());
                // Demo Fallback
                tvTechnicianLevel.setText("Level 1: Novice Technician");
                tvCompletionPercent.setText("0%");
                progressCompletion.setProgress(0);
            }
        });

        // Fetch Modules
        api.getTrainingModules().enqueue(new Callback<List<TrainingModule>>() {
            @Override
            public void onResponse(Call<List<TrainingModule>> call, Response<List<TrainingModule>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<TrainingModule> modules = response.body();
                    updateModulesUI(modules);
                } else {
                    // Demo Fallback
                    showDemoModules();
                }
            }

            @Override
            public void onFailure(Call<List<TrainingModule>> call, Throwable t) {
                Log.e("TrainingActivity", "Modules Error: " + t.getMessage());
                showDemoModules();
            }
        });
    }

    private void showDemoModules() {
        tvModuleTitle1.setText("Ultrasound Basics");
        tvModuleInfo1.setText("Probe handling & physics • 15 min");
        progress1.setProgress(100); // Demo: first one done

        tvModuleTitle2.setText("Bladder Volume Measurement");
        tvModuleInfo2.setText("Standard protocol • 20 min");
        progress2.setProgress(40); // Demo: second one in progress

        tvCompletionPercent.setText("70%");
        progressCompletion.setProgress(70);
        tvTechnicianLevel.setText("Level 2: Advanced Technician");
    }

    private void updateModulesUI(List<TrainingModule> modules) {
        int completedCount = 0;
        int totalModules = modules.size();

        if (totalModules > 0) {
            TrainingModule m1 = modules.get(0);
            tvModuleTitle1.setText(m1.getTitle());
            tvModuleInfo1.setText(m1.getSubtitle() + " • " + m1.getDurationMinutes() + " min");
            progress1.setProgress(m1.isCompleted() ? 100 : 0);
            if (m1.isCompleted()) completedCount++;
        }

        if (totalModules > 1) {
            TrainingModule m2 = modules.get(1);
            tvModuleTitle2.setText(m2.getTitle());
            tvModuleInfo2.setText(m2.getSubtitle() + " • " + m2.getDurationMinutes() + " min");
            progress2.setProgress(m2.isCompleted() ? 100 : 0);
            if (m2.isCompleted()) completedCount++;
        }

        // Calculate and update overall progress
        if (totalModules > 0) {
            int percent = (completedCount * 100) / totalModules;
            tvCompletionPercent.setText(percent + "%");
            progressCompletion.setProgress(percent);
            
            // Update Level based on progress
            if (percent >= 100) {
                tvTechnicianLevel.setText("Level 3: Expert Technician");
            } else if (percent >= 50) {
                tvTechnicianLevel.setText("Level 2: Advanced Technician");
            } else {
                tvTechnicianLevel.setText("Level 1: Novice Technician");
            }
        }
    }
}
