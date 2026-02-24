package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Forgot_twoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_two);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        EditText etNewPassword = findViewById(R.id.et_new_password);
        EditText etConfirmPassword = findViewById(R.id.et_confirm_password);
        TextView tvErrorMessage = findViewById(R.id.tv_error_message);
        MaterialButton btnResetPassword = findViewById(R.id.btn_reset_password);

        if (btnResetPassword != null) {
            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPassword = etNewPassword.getText().toString().trim();
                    String confirmPassword = etConfirmPassword.getText().toString().trim();

                    // Hide error message initially
                    if (tvErrorMessage != null) {
                        tvErrorMessage.setVisibility(View.GONE);
                    }

                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        Toast.makeText(Forgot_twoActivity.this, "Please enter both passwords", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        if (tvErrorMessage != null) {
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        }
                        return;
                    }

                    if (newPassword.length() < 8) {
                        Toast.makeText(Forgot_twoActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Navigate to Forgot_threeActivity (Success Screen)
                    Intent intent = new Intent(Forgot_twoActivity.this, Forgot_threeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
