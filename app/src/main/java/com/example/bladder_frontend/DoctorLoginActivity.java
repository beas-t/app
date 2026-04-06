package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.widget.ProgressBar;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.api.models.AuthResponse;
import com.example.bladder_frontend.api.models.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorLoginActivity extends BaseActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        TextView signUpButton = findViewById(R.id.signUpButtonTextView);


        loginButton.setOnClickListener(v -> {
            String identity = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Clear previous errors
            emailEditText.setError(null);
            passwordEditText.setError(null);

            if (identity.isEmpty()) {
                emailEditText.setError("Please enter your registered Email Address or Medical ID.");
                emailEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Please enter your password to sign in.");
                passwordEditText.requestFocus();
                return;
            }

            SessionManager sessionManager1 = new SessionManager(DoctorLoginActivity.this);
            String localSavedPassword = sessionManager1.getLocalPassword(identity);
            
            // If user has changed their password, we must use the new one.
            if (localSavedPassword != null) {
                if (localSavedPassword.equals(password)) {
                    Toast.makeText(DoctorLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DoctorLoginActivity.this, HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    passwordEditText.setError("Invalid Credentials");
                    passwordEditText.requestFocus();
                    return;
                }
            }

            setLoading(true);

            BladSenseApi api = RetrofitClient.getApi(DoctorLoginActivity.this);
            api.login(new LoginRequest(identity, password)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (isFinishing() || isDestroyed()) return;
                    
                    setLoading(false);
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();

                        SessionManager sessionManager2 = new SessionManager(DoctorLoginActivity.this);
                        sessionManager2.saveAuthToken(authResponse.getAccessToken());
                        if (authResponse.getUser() != null) {
                            AuthResponse.UserData user = authResponse.getUser();
                            sessionManager2.saveUserDetail(user.getEmail(), "");
                            
                            if (user.getProfile() != null) {
                                AuthResponse.DoctorProfileData profile = user.getProfile();
                                sessionManager2.saveDoctorProfile(
                                    profile.getFullName(),
                                    profile.getSpecialty(),
                                    profile.getLicenseNumber(),
                                    profile.getPhone(),
                                    profile.getProfilePicture()
                                );
                            }
                            
                            // Always save credentials locally to support biometric login once enabled
                            sessionManager2.saveLocalPassword(user.getEmail(), password);
                            sessionManager2.saveLastEmail(user.getEmail());
                        }

                        String displayName = "Doctor";
                        if (authResponse.getUser() != null && authResponse.getUser().getProfile() != null) {
                            displayName = authResponse.getUser().getProfile().getFullName();
                        }
                        
                        Toast.makeText(DoctorLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        
                        Intent intent = new Intent(DoctorLoginActivity.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    if (isFinishing() || isDestroyed()) return;
                    setLoading(false);
                    showSnackbarError("Network Error: failed to connect to server", true);
                }
            });
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLoginActivity.this, ForgotActivity.class);
            startActivity(intent);
        });

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorLoginActivity.this, DoctorSignUpActivity.class);
            startActivity(intent);
        });

        toolbar.setOnLongClickListener(v -> {
            showServerConfigDialog();
            return true;
        });
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        findViewById(R.id.loginButton).setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.loginButton).setEnabled(!isLoading);
    }

    private String errorMsg = "Invalid Credentials";

    private void showSnackbarError(String message, boolean retry) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        if (retry) {
            snackbar.setAction("Retry", v -> findViewById(R.id.loginButton).performClick());
        }
        snackbar.show();
    }

    private void handleErrorResponse(Response<AuthResponse> response) {
        errorMsg = "Invalid Credentials";
        try {
            if (response.errorBody() != null) {
                String rawError = response.errorBody().string();
                if (rawError.startsWith("<!DOCTYPE html")) {
                    String currentUrl = RetrofitClient.getBaseUrl(this);
                    Toast.makeText(this,
                            "Network Error: Received HTML. Check URL: " + currentUrl,
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (rawError.startsWith("{")) {
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
        } catch (java.io.IOException e) {
            errorMsg = "Connection error";
        }

        // Show professional error on the relevant field or as a toast if generic
        if (errorMsg.toLowerCase().contains("email") || errorMsg.toLowerCase().contains("id") || errorMsg.toLowerCase().contains("user")) {
            emailInputLayout.getEditText().setError(errorMsg);
            emailInputLayout.getEditText().requestFocus();
        } else if (errorMsg.toLowerCase().contains("password") || errorMsg.toLowerCase().contains("credentials")) {
            passwordInputLayout.getEditText().setError(errorMsg);
            passwordInputLayout.getEditText().requestFocus();
        } else {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showServerConfigDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Server Configuration");

        final EditText input = new EditText(this);
        input.setHint("http://192.168.1.1:8000/");
        SessionManager sessionManager = new SessionManager(this);
        String currentUrl = RetrofitClient.getBaseUrl(this);
        input.setText(currentUrl);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUrl = input.getText().toString().trim();
            if (!newUrl.isEmpty()) {
                if (!newUrl.endsWith("/")) newUrl += "/";
                sessionManager.saveServerUrl(newUrl);
                RetrofitClient.resetRetrofit();
                Toast.makeText(this, "Server URL Updated", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
