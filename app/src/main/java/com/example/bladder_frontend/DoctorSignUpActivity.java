package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.api.models.AuthResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.AdapterView;
import android.text.Editable;
import android.text.TextWatcher;
import com.example.bladder_frontend.api.models.AuthResponse;
import com.example.bladder_frontend.api.BladSenseApi;

public class DoctorSignUpActivity extends AppCompatActivity {

    private TextInputLayout fullNameInputLayout, emailInputLayout, licenseInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    private CheckBox termsCheckBox;
    private ProgressBar progressBar;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView signInButton = findViewById(R.id.signInButtonTextView);
        
        fullNameInputLayout = findViewById(R.id.fullNameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        licenseInputLayout = findViewById(R.id.licenseInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);

        EditText fullNameEditText = findViewById(R.id.fullNameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText licenseEditText = findViewById(R.id.licenseEditText);
        Spinner specialtySpinner = findViewById(R.id.specialtySpinner);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        
        termsCheckBox = findViewById(R.id.termsCheckBox);
        createAccountButton = findViewById(R.id.createAccountButton);
        progressBar = findViewById(R.id.progressBar);


        String[] specialties = { "Select specialty", "Urology", "Gynecology", "General Surgery", "Cardiology", "Neurology", "Pediatrics" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialtySpinner.setAdapter(adapter);

        // Initial state
        createAccountButton.setEnabled(true);

        android.text.TextWatcher validationWatcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                validateAllFields(false);
            }
        };

        fullNameEditText.addTextChangedListener(validationWatcher);
        emailEditText.addTextChangedListener(validationWatcher);
        licenseEditText.addTextChangedListener(validationWatcher);
        passwordEditText.addTextChangedListener(validationWatcher);
        confirmPasswordEditText.addTextChangedListener(validationWatcher);
        termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> validateAllFields(false));
        
        specialtySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                validateAllFields(false);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorSignUpActivity.this, RoleSelectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorSignUpActivity.this, DoctorLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateAllFields(true)) return;

                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String license = licenseEditText.getText().toString().trim();
                String specialty = specialtySpinner.getSelectedItem().toString();
                String password = passwordEditText.getText().toString().trim();

                setLoading(true);

                java.util.Map<String, String> signUpData = new java.util.HashMap<>();
                signUpData.put("fullName", fullName);
                signUpData.put("email", email);
                signUpData.put("licenseNumber", license);
                signUpData.put("specialty", specialty);
                signUpData.put("password", password);

                com.example.bladder_frontend.api.BladSenseApi api = com.example.bladder_frontend.api.RetrofitClient
                        .getApi(DoctorSignUpActivity.this);
                api.signUp(signUpData)
                        .enqueue(new retrofit2.Callback<com.example.bladder_frontend.api.models.AuthResponse>() {
                            @Override
                            public void onResponse(
                                    retrofit2.Call<com.example.bladder_frontend.api.models.AuthResponse> call,
                                    retrofit2.Response<com.example.bladder_frontend.api.models.AuthResponse> response) {
                                setLoading(false);
                                if (response.isSuccessful() && response.body() != null) {
                                    com.example.bladder_frontend.api.models.AuthResponse authResponse = response.body();

                                    // Save Token
                                    com.example.bladder_frontend.api.SessionManager sessionManager = new com.example.bladder_frontend.api.SessionManager(
                                            DoctorSignUpActivity.this);
                                    sessionManager.saveAuthToken(authResponse.getAccessToken());
                                    if (authResponse.getUser() != null) {
                                        com.example.bladder_frontend.api.models.AuthResponse.UserData user = authResponse.getUser();
                                        sessionManager.saveUserDetail(user.getEmail(), "");
                                        
                                        if (user.getProfile() != null) {
                                            com.example.bladder_frontend.api.models.AuthResponse.DoctorProfileData profile = user.getProfile();
                                            sessionManager.saveDoctorProfile(
                                                profile.getFullName(),
                                                profile.getSpecialty(),
                                                profile.getLicenseNumber(),
                                                profile.getPhone(),
                                                profile.getProfilePicture()
                                            );
                                        }
                                    }

                                    Toast.makeText(DoctorSignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DoctorSignUpActivity.this, HomePageActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    handleApiResponseError(response);
                                }
                            }

                            @Override
                            public void onFailure(
                                    retrofit2.Call<com.example.bladder_frontend.api.models.AuthResponse> call,
                                    Throwable t) {
                                setLoading(false);
                                showSnackbarError("Network Error: failed to connect to server", true);
                            }
                        });
            }
        });
        toolbar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showServerConfigDialog();
                return true;
            }
        });
    }

    private boolean validateAllFields(boolean showErrors) {
        EditText fullNameEditText = fullNameInputLayout.getEditText();
        EditText emailEditText = emailInputLayout.getEditText();
        EditText licenseEditText = licenseInputLayout.getEditText();
        EditText passwordEditText = passwordInputLayout.getEditText();
        EditText confirmPasswordEditText = confirmPasswordInputLayout.getEditText();
        Spinner specialtySpinner = findViewById(R.id.specialtySpinner);
        
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String license = licenseEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String specialty = specialtySpinner.getSelectedItem().toString();
        boolean termsAccepted = termsCheckBox.isChecked();

        boolean isValid = true;

        // Clear previous errors
        if (showErrors || fullName.length() > 0) fullNameEditText.setError(null);
        if (showErrors || email.length() > 0) emailEditText.setError(null);
        if (showErrors || license.length() > 0) licenseEditText.setError(null);
        if (showErrors || password.length() > 0) passwordEditText.setError(null);
        if (showErrors || confirmPassword.length() > 0) confirmPasswordEditText.setError(null);

        // Full Name Validation
        if (fullName.isEmpty() || fullName.length() < 3 || !fullName.matches("^[a-zA-Z\\s]+$")) {
            if (showErrors && isValid) {
                fullNameEditText.setError("Please enter a valid full name (letters only, min 3 chars).");
                fullNameEditText.requestFocus();
            }
            isValid = false;
        }

        // Email Validation
        if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            if (showErrors && isValid) {
                emailEditText.setError("Please enter a valid Gmail address (example@gmail.com).");
                emailEditText.requestFocus();
            }
            isValid = false;
        }

        // Medical ID Validation
        if (!license.matches("^[a-zA-Z0-9]{6,15}$")) {
            if (showErrors && isValid) {
                licenseEditText.setError("Please enter a valid Medical License ID (6-15 alphanumeric).");
                licenseEditText.requestFocus();
            }
            isValid = false;
        }

        // Specialty Validation
        if (specialty.equals("Select specialty")) {
            if (showErrors && isValid) {
                Toast.makeText(this, "Please select your professional specialty.", Toast.LENGTH_SHORT).show();
            }
            isValid = false;
        }

        // Password Validation
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_\\.])[A-Za-z\\d@$!%*?&\\-_\\.]{8,}$")) {
            if (showErrors && isValid) {
                passwordEditText.setError("Please use a stronger password (8+ chars, upper, lower, number, special).");
                passwordEditText.requestFocus();
            }
            isValid = false;
        }

        // Confirm Password Validation
        if (!password.equals(confirmPassword)) {
            if (showErrors && isValid) {
                confirmPasswordEditText.setError("Passwords do not match. Please verify.");
                confirmPasswordEditText.requestFocus();
            }
            isValid = false;
        }

        // Terms & Conditions Validation
        if (!termsAccepted) {
            if (showErrors && isValid) {
                Toast.makeText(this, "Please accept the Terms & Conditions to proceed.", Toast.LENGTH_SHORT).show();
            }
            isValid = false;
        }

        return isValid;
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        createAccountButton.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        createAccountButton.setEnabled(!isLoading);
    }

    private void handleApiResponseError(retrofit2.Response<com.example.bladder_frontend.api.models.AuthResponse> response) {
        try {
            String errorMsg = "Sign up failed";
            if (response.errorBody() != null) {
                String rawError = response.errorBody().string();
                if (rawError.startsWith("{")) {
                    try {
                        com.google.gson.JsonObject jsonObject = new com.google.gson.Gson().fromJson(rawError, com.google.gson.JsonObject.class);
                        if (jsonObject.has("message")) {
                            errorMsg = jsonObject.get("message").getAsString();
                        }
                    } catch (Exception e) {
                        errorMsg = rawError;
                    }
                } else {
                    errorMsg = rawError;
                }
            }
            // Handle backend errors by showing them on the specific field if possible
            if (errorMsg.toLowerCase().contains("email")) {
                emailInputLayout.getEditText().setError(errorMsg);
                emailInputLayout.getEditText().requestFocus();
            } else if (errorMsg.toLowerCase().contains("full name")) {
                fullNameInputLayout.getEditText().setError(errorMsg);
                fullNameInputLayout.getEditText().requestFocus();
            } else if (errorMsg.toLowerCase().contains("license") || errorMsg.toLowerCase().contains("id")) {
                licenseInputLayout.getEditText().setError(errorMsg);
                licenseInputLayout.getEditText().requestFocus();
            } else if (errorMsg.toLowerCase().contains("password")) {
                passwordInputLayout.getEditText().setError(errorMsg);
                passwordInputLayout.getEditText().requestFocus();
            } else {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        } catch (java.io.IOException e) {
            Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSnackbarError(String message, boolean retry) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        if (retry) {
            snackbar.setAction("Retry", v -> createAccountButton.performClick());
        }
        snackbar.show();
    }

    private void showServerConfigDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Server Configuration");

        final EditText input = new EditText(this);
        input.setHint("http://192.168.1.1:8000/");
        com.example.bladder_frontend.api.SessionManager sessionManager = new com.example.bladder_frontend.api.SessionManager(this);
        String currentUrl = com.example.bladder_frontend.api.RetrofitClient.getBaseUrl(this);
        input.setText(currentUrl);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUrl = input.getText().toString().trim();
            if (!newUrl.endsWith("/")) newUrl += "/";
            sessionManager.saveServerUrl(newUrl);
            com.example.bladder_frontend.api.RetrofitClient.resetRetrofit();
            Toast.makeText(this, "Server URL Updated", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
