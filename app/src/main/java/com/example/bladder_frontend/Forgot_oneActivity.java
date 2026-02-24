package com.example.bladder_frontend;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Forgot_oneActivity extends AppCompatActivity {

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_one);

        // Retrieve email from intent
        userEmail = getIntent().getStringExtra("EMAIL");
        if (userEmail == null) {
            userEmail = "docotor@GMAIL.COM"; // Fallback
        }

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        TextView tvInstruction = findViewById(R.id.tv_instruction);
        if (tvInstruction != null) {
            tvInstruction.setText("We sent a 6-digit verification code to " + userEmail);
        }

        EditText etVerificationCode = findViewById(R.id.et_verification_code);
        MaterialButton btnVerifyCode = findViewById(R.id.btn_verify_code);

        if (etVerificationCode != null && btnVerifyCode != null) {
            etVerificationCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 6) {
                        btnVerifyCode.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0066CC")));
                    } else {
                        btnVerifyCode.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#93C5FD")));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            btnVerifyCode.setOnClickListener(v -> {
                if (etVerificationCode.getText().toString().length() == 6) {
                    Intent intent = new Intent(Forgot_oneActivity.this, Forgot_twoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please enter a 6-digit code", Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView btnResendCode = findViewById(R.id.btn_resend_code);
        if (btnResendCode != null) {
            btnResendCode.setOnClickListener(v -> {
                Toast.makeText(this, "A new code has been sent to " + userEmail, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
