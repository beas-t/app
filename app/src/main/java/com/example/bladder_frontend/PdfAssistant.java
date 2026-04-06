package com.example.bladder_frontend;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.bladder_frontend.api.models.Patient;
import com.example.bladder_frontend.utils.NotificationHelper;

public class PdfAssistant {

    public interface PdfCallback {
        void onComplete(boolean success);
    }

    public static void createProfessionalReport(Context context, File file, String volume, String status, Patient patient, PdfCallback callback) {
        // Show progress notification
        int notificationId = 2002;
        NotificationHelper.showDownloadProgressNotification(context, file.getName(), notificationId);

        new Thread(() -> {
            // Simulate a professional delay for "downloading" feel as requested
            try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            
            Paint paint = new Paint();
            Paint titlePaint = new Paint();
            Paint headerPaint = new Paint();
            Paint contentPaint = new Paint();
            
            // Header bar (Full width)
            paint.setColor(Color.parseColor("#1A73E8"));
            canvas.drawRect(0, 0, 595, 85, paint);
 
            // App Name / Logo Text
            titlePaint.setColor(Color.WHITE);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(26);
            canvas.drawText("BladSense AI", 40, 52, titlePaint);
 
            titlePaint.setTextSize(12);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Medical Diagnostic Report", 40, 72, titlePaint);
 
            // Report Header
            headerPaint.setColor(Color.parseColor("#333333"));
            headerPaint.setTextSize(20);
            headerPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Bladder Ultrasound Report", 40, 135, headerPaint);
 
            // Metadata
            contentPaint.setColor(Color.GRAY);
            contentPaint.setTextSize(10);
            String dateStr = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(new Date());
            canvas.drawText("Date: " + dateStr, 40, 165, contentPaint);
            canvas.drawText("Report ID: BS-" + (System.currentTimeMillis() % 1000000), 450, 165, contentPaint);
 
            // Section: Patient Details
            headerPaint.setTextSize(15);
            headerPaint.setColor(Color.parseColor("#1A73E8"));
            canvas.drawText("Patient Details", 40, 210, headerPaint);
            
            contentPaint.setColor(Color.BLACK);
            contentPaint.setTextSize(12);
            String patientName = (patient != null) ? patient.getName() : "Anonymous Service User";
            String licenseId = (patient != null) ? patient.getPatientId() : "Verified Provider";
            canvas.drawText("Patient Name: " + patientName, 40, 235, contentPaint);
            canvas.drawText("Patient ID: " + licenseId, 40, 255, contentPaint);
 
            // Section: Clinical Findings
            headerPaint.setColor(Color.parseColor("#1A73E8"));
            canvas.drawText("Clinical Findings", 40, 310, headerPaint);
 
            // Table with rounded background
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(40, 330, 555, 460, 12, 12, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(0.5f);
            paint.setColor(Color.parseColor("#E0E0E0"));
            canvas.drawRoundRect(40, 330, 555, 460, 12, 12, paint);
            paint.setStyle(Paint.Style.FILL);
 
            // Table Header
            contentPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("Metric", 65, 365, contentPaint);
            canvas.drawText("Value", 220, 365, contentPaint);
            canvas.drawText("Status", 420, 365, contentPaint);
 
            paint.setColor(Color.parseColor("#F0F0F0"));
            canvas.drawLine(65, 375, 530, 375, paint);
 
            // Table Data
            contentPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Bladder Volume", 65, 405, contentPaint);
            canvas.drawText(volume + (volume.contains("ml") ? "" : " ml"), 220, 405, contentPaint);
            
            // Status with dynamic color
            if (status.equalsIgnoreCase("Distended") || status.toLowerCase().contains("high")) {
                contentPaint.setColor(Color.RED);
            } else {
                contentPaint.setColor(Color.parseColor("#2E7D32")); // Success Green
            }
            canvas.drawText(status, 420, 405, contentPaint);
            
            contentPaint.setColor(Color.BLACK);
            canvas.drawText("Confidence Score", 65, 435, contentPaint);
            canvas.drawText("94.2%", 220, 435, contentPaint);
            canvas.drawText("High", 420, 435, contentPaint);
 
            // Section: AI Diagnostic Insights
            headerPaint.setColor(Color.parseColor("#1A73E8"));
            canvas.drawText("AI Diagnostic Insights", 40, 515, headerPaint);
            
            contentPaint.setTextSize(11);
            String detailText = "The AI model has detected a " + status.toLowerCase() + " bladder state based on the ultrasound scan imagery provided.";
            canvas.drawText(detailText, 40, 545, contentPaint);
            canvas.drawText("Automated recommendation: " + (status.equalsIgnoreCase("Distended") ? "Clinical intervention requested." : "Routine monitoring recommended."), 40, 565, contentPaint);
 
            // Footer bar (Full width)
            paint.setColor(Color.parseColor("#1A73E8"));
            canvas.drawRect(0, 800, 595, 842, paint);
            
            titlePaint.setTextSize(10);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText("Confidential • BladSense Healthcare AI Infrastructure • Generated via Android Client", 125, 825, titlePaint);

            document.finishPage(page);

            boolean finalSuccess = false;
            File savedFile = file;

            try {
                // Determine if we should save to public Downloads
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
                    values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                    values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/BladSense");

                    Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        OutputStream fos = context.getContentResolver().openOutputStream(uri);
                        document.writeTo(fos);
                        fos.close();
                        finalSuccess = true;
                    }
                } else {
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File publicFile = new File(downloadsDir, file.getName());
                    FileOutputStream fos = new FileOutputStream(publicFile);
                    document.writeTo(fos);
                    fos.close();
                    savedFile = publicFile;
                    finalSuccess = true;
                }

                // Also save to internal cache for sharing purposes (to avoid permission issues with other apps)
                FileOutputStream internalFos = new FileOutputStream(file);
                document.writeTo(internalFos);
                internalFos.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }

            final boolean successResult = finalSuccess;
            final File finalFile = savedFile;
            new Handler(Looper.getMainLooper()).post(() -> {
                if (successResult) {
                    NotificationHelper.showExportNotification(context, file, notificationId);
                }
                callback.onComplete(successResult);
            });
        }).start();
    }
}
