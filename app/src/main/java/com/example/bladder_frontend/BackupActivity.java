package com.example.bladder_frontend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class BackupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

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
    }

    /**
     * Simulates the backup process.
     * In a real app, this would involve uploading data to a cloud service or
     * copying the local database to a secure storage location.
     */
    private void performBackup(MaterialButton button) {
        button.setEnabled(false);
        button.setText("Backing up...");
        
        Toast.makeText(this, "Starting backup process...", Toast.LENGTH_SHORT).show();

        // Simulate a delay for the backup process
        new Handler().postDelayed(() -> {
            button.setEnabled(true);
            button.setText("Backup Now");
            Toast.makeText(this, "Backup completed successfully!", Toast.LENGTH_LONG).show();
            // Here you would typically update the "Last Backup" timestamp in SharedPreferences
        }, 3000); // 3-second simulation
    }

    /**
     * Shows a confirmation dialog before restoring data, as restoration
     * usually overwrites current local data.
     */
    private void showRestoreConfirmation(MaterialButton button) {
        new AlertDialog.Builder(this)
                .setTitle("Restore Data")
                .setMessage("Are you sure you want to restore? This will overwrite your current local data with the latest backup.")
                .setPositiveButton("Restore", (dialog, which) -> performRestore(button))
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Simulates the restoration process.
     * In a real app, this would involve downloading the backup file and
     * replacing the local database/files.
     */
    private void performRestore(MaterialButton button) {
        button.setEnabled(false);
        button.setText("Restoring...");

        Toast.makeText(this, "Restoring data from latest backup...", Toast.LENGTH_SHORT).show();

        // Simulate a delay for the restore process
        new Handler().postDelayed(() -> {
            button.setEnabled(true);
            button.setText("Restore from Backup");
            Toast.makeText(this, "Restore completed. Data has been synchronized.", Toast.LENGTH_LONG).show();
        }, 4000); // 4-second simulation
    }
}
