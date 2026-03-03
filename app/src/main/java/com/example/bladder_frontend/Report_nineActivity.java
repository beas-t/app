package com.example.bladder_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Report_nineActivity extends AppCompatActivity {

    private static final String FILE_NAME = "Report-R-1023.pdf";
    private static final String DIR_NAME = "Reports";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_nine);

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(v -> {
                startActivity(new Intent(Report_nineActivity.this, Notification_oneActivity.class));
            });
        }

        MaterialButton btnDownload = findViewById(R.id.btn_download);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> handleDownload());
        }

        MaterialButton btnShare = findViewById(R.id.btn_share);
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> handleShare());
        }

        TextView btnDone = findViewById(R.id.btn_done);
        if (btnDone != null) {
            btnDone.setOnClickListener(v -> {
                Intent intent = new Intent(Report_nineActivity.this, Report_oneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }

    private void handleDownload() {
        File file = getPdfFile();
        if (file.exists()) {
            Toast.makeText(this, "Download Successful (File already exists)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (createSamplePdf(file)) {
            Toast.makeText(this, "Download Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to download report", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleShare() {
        File file = getPdfFile();
        if (!file.exists()) {
            if (!createSamplePdf(file)) {
                Toast.makeText(this, "Could not prepare report for sharing", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            
            startActivity(Intent.createChooser(shareIntent, "Share Report via"));
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error sharing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File getPdfFile() {
        File directory = new File(getExternalFilesDir(null), DIR_NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, FILE_NAME);
    }

    private boolean createSamplePdf(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // In a real production app, you would use a PDF library like iText or PdfDocument
            // For this frontend implementation, we'll create a dummy valid-ish PDF header
            String content = "%PDF-1.4\n1 0 obj\n<< /Title (BladSense Report) >>\nendobj\ntrailer\n<< /Root 1 0 R >>\n%%EOF";
            fos.write(content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
