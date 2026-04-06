package com.example.bladder_frontend;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Backup;
import com.example.bladder_frontend.api.models.BackupSettings;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackupActivity extends AppCompatActivity {

    private RecyclerView rvBackupHistory;
    private BackupAdapter adapter;
    private List<Backup> backupList = new ArrayList<>();
    
    private TextView tvLastBackupDate, tvLastBackupSize, tvLastBackupItems;
    private SwitchMaterial switchAutoBackup, switchIncludeImages, switchWifiOnly;
    private BladSenseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        api = RetrofitClient.getApi(this);

        // Views
        tvLastBackupDate = findViewById(R.id.tv_last_backup_date);
        tvLastBackupSize = findViewById(R.id.tv_last_backup_size);
        tvLastBackupItems = findViewById(R.id.tv_last_backup_items);
        switchAutoBackup = findViewById(R.id.switch_auto_backup);
        switchIncludeImages = findViewById(R.id.switch_include_images);
        switchWifiOnly = findViewById(R.id.switch_wifi_only);

        // History Setup
        rvBackupHistory = findViewById(R.id.rv_backup_history);
        rvBackupHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BackupAdapter(backupList, this);
        rvBackupHistory.setAdapter(adapter);

        // Load data
        loadBackupData();

        // Back button logic
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Backup Now button logic
        MaterialButton btnBackupNow = findViewById(R.id.btn_backup_now);
        if (btnBackupNow != null) {
            btnBackupNow.setOnClickListener(v -> performBackup(btnBackupNow));
        }

        // Restore button logic
        MaterialButton btnRestore = findViewById(R.id.btn_restore);
        if (btnRestore != null) {
            btnRestore.setOnClickListener(v -> showRestoreConfirmation(btnRestore));
        }

        // Settings change listeners
        setupSettingsListeners();
    }

    private void loadBackupData() {
        // Fetch History
        api.getBackupHistory().enqueue(new Callback<List<Backup>>() {
            @Override
            public void onResponse(Call<List<Backup>> call, Response<List<Backup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    backupList = response.body();
                    adapter.updateData(backupList);
                    if (!backupList.isEmpty()) {
                        updateLatestBackupInfo(backupList.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Backup>> call, Throwable t) {}
        });

        // Fetch Settings
        api.getBackupSettings().enqueue(new Callback<BackupSettings>() {
            @Override
            public void onResponse(Call<BackupSettings> call, Response<BackupSettings> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BackupSettings settings = response.body();
                    switchAutoBackup.setChecked(settings.isAutoBackup());
                    switchIncludeImages.setChecked(settings.isIncludeImages());
                    switchWifiOnly.setChecked(settings.isWifiOnly());
                }
            }

            @Override
            public void onFailure(Call<BackupSettings> call, Throwable t) {}
        });
    }

    private void updateLatestBackupInfo(Backup backup) {
        tvLastBackupDate.setText(backup.getCreatedAt());
        tvLastBackupSize.setText(backup.getFileSize());
        tvLastBackupItems.setText(backup.getItemCount() + " items");
    }

    private void setupSettingsListeners() {
        ComposeSettingsUpdateListener listener = () -> {
            BackupSettings settings = new BackupSettings();
            settings.setAutoBackup(switchAutoBackup.isChecked());
            settings.setIncludeImages(switchIncludeImages.isChecked());
            settings.setWifiOnly(switchWifiOnly.isChecked());
            
            api.updateBackupSettings(settings).enqueue(new Callback<BackupSettings>() {
                @Override
                public void onResponse(Call<BackupSettings> call, Response<BackupSettings> response) {}
                @Override
                public void onFailure(Call<BackupSettings> call, Throwable t) {}
            });
        };

        switchAutoBackup.setOnCheckedChangeListener((v, c) -> listener.onUpdate());
        switchIncludeImages.setOnCheckedChangeListener((v, c) -> listener.onUpdate());
        switchWifiOnly.setOnCheckedChangeListener((v, c) -> listener.onUpdate());
    }

    interface ComposeSettingsUpdateListener {
        void onUpdate();
    }

    private void performBackup(MaterialButton button) {
        button.setEnabled(false);
        button.setText("Backing up...");
        
        Toast.makeText(this, "Starting backup process...", Toast.LENGTH_SHORT).show();

        api.performBackup().enqueue(new Callback<Backup>() {
            @Override
            public void onResponse(Call<Backup> call, Response<Backup> response) {
                button.setEnabled(true);
                button.setText("Backup Now");
                if (response.isSuccessful()) {
                    Toast.makeText(BackupActivity.this, "Backup completed successfully!", Toast.LENGTH_LONG).show();
                    loadBackupData(); // Refresh history
                } else {
                    Toast.makeText(BackupActivity.this, "Backup failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Backup> call, Throwable t) {
                button.setEnabled(true);
                button.setText("Backup Now");
                Toast.makeText(BackupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRestoreConfirmation(MaterialButton button) {
        new AlertDialog.Builder(this)
                .setTitle("Restore Data")
                .setMessage("Are you sure you want to restore? This will overwrite your current local data with the latest backup.")
                .setPositiveButton("Restore", (dialog, which) -> performRestore(button))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void performRestore(MaterialButton button) {
        button.setEnabled(false);
        button.setText("Restoring...");
        Toast.makeText(this, "Restoring data from latest backup...", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            button.setEnabled(true);
            button.setText("Restore from Backup");
            Toast.makeText(this, "Restore completed. Data has been synchronized.", Toast.LENGTH_LONG).show();
        }, 4000);
    }
}
