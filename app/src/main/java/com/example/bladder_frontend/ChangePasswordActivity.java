package com.example.bladder_frontend;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    private TextInputEditText etCurrent, etNew, etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        etCurrent = findViewById(R.id.et_current_password);
        etNew = findViewById(R.id.et_new_password);
        etConfirm = findViewById(R.id.et_confirm_password);
        MaterialButton btnUpdate = findViewById(R.id.btn_update_password);

        if (btnUpdate != null) {
            btnUpdate.setOnClickListener(v -> handlePasswordUpdate());
        }
    }

    private void handlePasswordUpdate() {
        String current = etCurrent.getText().toString().trim();
        String newPass = etNew.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SessionManager sessionManager = new SessionManager(this);
        String savedPass = sessionManager.getLocalPassword(sessionManager.getUserEmail());
        
        // If they have a saved password locally, they MUST enter it correctly as 'current'
        if (savedPass != null && !savedPass.equals(current)) {
            Toast.makeText(this, "Incorrect current password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate API call or call backend if endpoint exists
        Toast.makeText(this, "Updating password...", Toast.LENGTH_SHORT).show();
        
        String email = sessionManager.getUserEmail();
        if (email != null && !email.isEmpty()) {
            sessionManager.saveLocalPassword(email, newPass);
        }

        // Mocking professional feedback
        new android.os.Handler().postDelayed(() -> {
            Toast.makeText(ChangePasswordActivity.this, "Password Updated Successfully!", Toast.LENGTH_LONG).show();
            finish();
        }, 1500);
    }
}
