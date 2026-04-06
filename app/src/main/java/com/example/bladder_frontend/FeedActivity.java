package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class FeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Setup Dropdown
        AutoCompleteTextView dropdown = findViewById(R.id.auto_feedback_type);
        if (dropdown != null) {
            String[] items = getResources().getStringArray(R.array.feedback_types);
            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, R.layout.list_item, items);
            dropdown.setAdapter(adapter);
        }

        com.google.android.material.textfield.TextInputEditText etMessage = findViewById(R.id.et_message);
        com.google.android.material.textfield.TextInputEditText etEmail = findViewById(R.id.et_email);
        MaterialButton btnSubmit = findViewById(R.id.btn_submit);

        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                String type = dropdown != null ? dropdown.getText().toString() : "General";
                String message = etMessage != null ? etMessage.getText().toString().trim() : "";
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";

                if (message.isEmpty()) {
                    Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }

                submitFeedback(type, message, email.isEmpty() ? null : email);
            });
        }
    }

    private void submitFeedback(String type, String message, String email) {
        com.example.bladder_frontend.api.models.Feedback feedback = 
            new com.example.bladder_frontend.api.models.Feedback(type, message, email);
        
        com.example.bladder_frontend.api.BladSenseApi api = 
            com.example.bladder_frontend.api.RetrofitClient.getApi(this);
        
        api.submitFeedback(feedback).enqueue(new retrofit2.Callback<com.example.bladder_frontend.api.models.Feedback>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.bladder_frontend.api.models.Feedback> call, 
                                 retrofit2.Response<com.example.bladder_frontend.api.models.Feedback> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedActivity.this, "Feedback Submitted Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMsg = "Failed to submit feedback";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(FeedActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    android.util.Log.e("FeedActivity", "Error body: " + errorMsg);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.example.bladder_frontend.api.models.Feedback> call, Throwable t) {
                Toast.makeText(FeedActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                android.util.Log.e("FeedActivity", "Failure: ", t);
            }
        });
    }
}
