package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class PrivacyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnViewDataUsage = findViewById(R.id.btn_view_data_usage);
        if (btnViewDataUsage != null) {
            btnViewDataUsage.setOnClickListener(v -> {
                startActivity(new Intent(PrivacyActivity.this, Privacy_oneActivity.class));
            });
        }

        // Export All Data
        MaterialButton btnExportAll = findViewById(R.id.btn_export_all);
        if (btnExportAll != null) {
            btnExportAll.setOnClickListener(v -> showExportDialog());
        }

        // View Privacy Policy
        MaterialButton btnViewPrivacyPolicy = findViewById(R.id.btn_view_privacy_policy);
        if (btnViewPrivacyPolicy != null) {
            btnViewPrivacyPolicy.setOnClickListener(v -> showPrivacyPolicyDialog());
        }

        // Delete Account
        MaterialButton btnDeleteAccount = findViewById(R.id.btn_delete_account);
        if (btnDeleteAccount != null) {
            btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        }
    }

    private void showExportDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Export Data")
                .setMessage("Your data is being prepared for export. You will receive a notification when the download link is ready.")
                .setPositiveButton("OK", (dialog, which) -> {
                    Toast.makeText(this, "Export started...", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showPrivacyPolicyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage("BladSense AI is committed to protecting your privacy. We use industry-standard encryption to secure your data and comply with HIPAA regulations.\n\nFor more details, please visit our official website.")
                .setPositiveButton("Close", null)
                .show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account?")
                .setMessage("Are you sure you want to delete your account? This action is permanent and all your data will be irrecoverable.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    Toast.makeText(this, "Account deletion requested", Toast.LENGTH_LONG).show();
                    // In a real app, you would call the API here and then logout
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
