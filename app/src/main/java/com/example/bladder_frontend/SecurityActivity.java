package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialCardView btnChangePassword = findViewById(R.id.card_change_password);
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(v -> {
                // Navigate to ChangePasswordActivity if it exists
                // startActivity(new Intent(SecurityActivity.this, ChangePasswordActivity.class));
            });
        }
    }
}
