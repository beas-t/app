package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Training_oneActivity extends AppCompatActivity {
    private VideoView videoView;
    private ProgressBar videoLoader;
    private ImageView playIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_one);

        // Get Data from Intent
        int moduleId = getIntent().getIntExtra("module_id", -1);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String videoUrl = getIntent().getStringExtra("video_url");

        // Initialize Views
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        TextView tvModuleTitle = findViewById(R.id.tv_module_title); 
        TextView tvModuleDesc = findViewById(R.id.tv_module_description);
        videoView = findViewById(R.id.video_view);
        videoLoader = findViewById(R.id.video_loader);
        playIcon = findViewById(R.id.play_icon);
        MaterialButton btnComplete = findViewById(R.id.btn_complete);

        // Standard Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Update Labels (Handling potential nulls)
        if (tvToolbarTitle != null && title != null) {
            tvToolbarTitle.setText(title);
        }
        if (tvModuleTitle != null && title != null) {
            tvModuleTitle.setText(title);
        }
        if (tvModuleDesc != null && description != null) {
            tvModuleDesc.setText(description);
        }

        // Setup Video
        if (videoView != null && videoUrl != null) {
            setupVideoPlayer(videoUrl);
        }

        if (btnComplete != null) {
            btnComplete.setOnClickListener(v -> {
                if (moduleId != -1) {
                    BladSenseApi api = RetrofitClient.getApi(this);
                    api.completeModule(moduleId).enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(Training_oneActivity.this, "Module Completed!", Toast.LENGTH_SHORT).show();
                                SessionManager sessionManager = new SessionManager(Training_oneActivity.this);
                                sessionManager.saveLastWatchedModule(moduleId);
                                finish();
                            } else {
                                Toast.makeText(Training_oneActivity.this, "Failed to update progress", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            Toast.makeText(Training_oneActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    finish();
                }
            });
        }
    }

    private void setupVideoPlayer(String url) {
        videoLoader.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(mp -> {
            videoLoader.setVisibility(View.GONE);
            playIcon.setVisibility(View.GONE);
            videoView.start();
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            videoLoader.setVisibility(View.GONE);
            Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
            return true;
        });

        // Play on card click if not started
        View videoCard = findViewById(R.id.video_card);
        if (videoCard != null) {
            videoCard.setOnClickListener(v -> {
                if (!videoView.isPlaying()) {
                    videoView.start();
                    playIcon.setVisibility(View.GONE);
                }
            });
        }
    }
}
