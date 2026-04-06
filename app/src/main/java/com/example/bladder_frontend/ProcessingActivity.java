package com.example.bladder_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Patient;
import com.example.bladder_frontend.api.models.ScanReport;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingActivity extends AppCompatActivity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        
        patient = (Patient) getIntent().getSerializableExtra("patient");

        TextView tvProcessingSubtext = findViewById(R.id.tvProcessingSubtext);
        String viewType = getIntent().getStringExtra("VIEW_TYPE");
        String imageUriString = getIntent().getStringExtra("IMAGE_URI");
        
        if (viewType != null && viewType.equals("sagittal")) {
            tvProcessingSubtext.setText("Analyzing sagittal view with AI...");
        } else {
            tvProcessingSubtext.setText("Analyzing transverse view with AI...");
        }

        if (imageUriString != null) {
            uploadAndAnalyze(Uri.parse(imageUriString));
        } else {
            // Simulated fallback if no image
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(ProcessingActivity.this, ResultActivity.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
                finish();
            }, 2000);
        }
    }

    private void uploadAndAnalyze(Uri imageUri) {
        try {
            File file = getFileFromUri(imageUri);
            if (file == null) {
                Toast.makeText(this, "Failed to process image file", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            
            // Use the actual patient ID if available
            String pId = (patient != null) ? String.valueOf(patient.getId()) : "";
            RequestBody patientId = RequestBody.create(MediaType.parse("text/plain"), pId);
            RequestBody notes = RequestBody.create(MediaType.parse("text/plain"), "AI Analysis Scan");

            BladSenseApi api = RetrofitClient.getApi(this);
            api.saveReport(patientId, notes, body).enqueue(new Callback<ScanReport>() {
                @Override
                public void onResponse(Call<ScanReport> call, Response<ScanReport> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ScanReport report = response.body();
                        // Strip " ml" from volume if present for the large display
                        String volValue = report.getVolume().replace(" ml", "").replace("ml", "").trim();
                        
                        Intent intent = new Intent(ProcessingActivity.this, ResultActivity.class);
                        intent.putExtra("VOLUME", volValue);
                        intent.putExtra("STATUS", report.getStatus());
                        intent.putExtra("LEVEL", report.getNotes().contains("AI Level:") ? 
                            report.getNotes().split("\\.")[0].replace("AI Level:", "").trim() : "NormalRange");
                        
                        // Pass the nested patient data (which now includes updated name from server)
                        intent.putExtra("patient", report.getPatient());
                            
                        // Pass confidence extracted from updated ScanReport model
                        if (report.getAiDetails() != null) {
                            intent.putExtra("CONFIDENCE", report.getAiDetails().getConfidence());
                        } else {
                            // Fallback
                            intent.putExtra("CONFIDENCE", 0.95f);
                        }
                        
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = "AI Analysis failed";
                        try {
                            if (response.errorBody() != null) {
                                String errorJson = response.errorBody().string();
                                try {
                                    org.json.JSONObject obj = new org.json.JSONObject(errorJson);
                                    if (obj.has("message")) {
                                        errorMsg = obj.getString("message");
                                    } else {
                                        errorMsg += ": " + errorJson;
                                    }
                                } catch (org.json.JSONException e) {
                                    errorMsg += ": " + errorJson;
                                }
                            } else {
                                errorMsg += ": " + response.message();
                            }
                        } catch (IOException e) {
                            errorMsg += ": " + response.message();
                        }
                        
                        // Show professional error dialog/toast
                        final String finalError = errorMsg;
                        runOnUiThread(() -> {
                            Toast.makeText(ProcessingActivity.this, finalError, Toast.LENGTH_LONG).show();
                            // Redirect back to capture to allow retake
                            finish();
                        });
                    }
                }

                @Override
                public void onFailure(Call<ScanReport> call, Throwable t) {
                    Toast.makeText(ProcessingActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            });

        } catch (Exception e) {
            Log.e("ProcessingActivity", "Error uploading", e);
            finish();
        }
    }

    private File getFileFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("scan_", ".jpg", getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return tempFile;
    }
}
