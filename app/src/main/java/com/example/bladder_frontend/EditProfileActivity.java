package com.example.bladder_frontend;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.utils.ImageUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private Uri selectedImageUri;
    private Uri cameraImageUri;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private SessionManager sessionManager;
    private EditText etFullName, etEmail, etMedicalId;
    private TextView tvSpecialtyValue, tvDisplayName, tvDisplaySpecialty;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImage = findViewById(R.id.profile_image);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnSave = findViewById(R.id.btn_save);
        
        // Find the camera button card
        View btnChooseImage = findViewById(R.id.btn_change_photo);
        if (btnChooseImage != null) {
            btnChooseImage.setOnClickListener(v -> showImagePickerDialog());
        }

        tvDisplayName = findViewById(R.id.tv_display_name);
        tvDisplaySpecialty = findViewById(R.id.tv_display_specialty);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etMedicalId = findViewById(R.id.et_medical_id);
        tvSpecialtyValue = findViewById(R.id.tv_specialty_value);

        sessionManager = new SessionManager(this);

        String name = sessionManager.getDoctorName();
        String specialty = sessionManager.getDoctorSpecialty();
        String license = sessionManager.getDoctorLicense();
        String email = sessionManager.getUserEmail();

        if (!name.isEmpty()) {
            tvDisplayName.setText(name);
            etFullName.setText(name);
        }
        if (!specialty.isEmpty()) {
            tvDisplaySpecialty.setText(specialty + " Specialist");
            tvSpecialtyValue.setText(specialty);
        }
        if (!license.isEmpty()) {
            etMedicalId.setText(license);
        }
        if (!email.isEmpty()) {
            etEmail.setText(email);
        }

        String profilePicUrl = sessionManager.getProfilePicture();
        if (!profilePicUrl.isEmpty()) {
            ImageUtils.loadImageFromUrl(this, profileImage, profilePicUrl);
        }

        // Initialize Launchers
        setupActivityResultLaunchers();

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void setupActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        selectedImageUri = cameraImageUri;
                        updateImagePreview(selectedImageUri);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        updateImagePreview(selectedImageUri);
                    }
                }
        );

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera();
                    } else {
                        Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void updateImagePreview(Uri uri) {
        profileImage.setImageURI(uri);
        profileImage.setImageTintList(null); // Clear the blue tint!
        profileImage.setBackground(null);    // Clear the background color
        profileImage.setPadding(0, 0, 0, 0); // Remove placeholder padding
    }

    private void showImagePickerDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_image_picker_options, null);
        
        view.findViewById(R.id.option_camera).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openCamera();
        });

        view.findViewById(R.id.option_gallery).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            galleryLauncher.launch("image/*");
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            return;
        }

        File photoFile = new File(getExternalCacheDir(), "profile_temp.jpg");
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        cameraLauncher.launch(intent);
    }

    private void saveProfileChanges() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String specialty = tvSpecialtyValue.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody specialtyPart = RequestBody.create(MediaType.parse("text/plain"), specialty);
        
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            File file = getFileFromUri(selectedImageUri);
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImageUri)), file);
                imagePart = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);
            }
        }

        RetrofitClient.getApi(this).updateFullProfile(namePart, emailPart, specialtyPart, imagePart).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                btnSave.setEnabled(true);
                btnSave.setText("Save Changes");

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> body = response.body();
                    // Update Local Session
                    sessionManager.saveDoctorName(name);
                    sessionManager.saveUserEmail(email);
                    sessionManager.saveDoctorSpecialty(specialty);
                    
                    if (body.get("profile_picture") != null) {
                        sessionManager.saveProfilePicture(body.get("profile_picture").toString());
                    }
                    
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("Save Changes");
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getExternalCacheDir(), "upload_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
