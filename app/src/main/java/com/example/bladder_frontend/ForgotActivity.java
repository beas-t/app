package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ForgotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        EditText etEmail = findViewById(R.id.et_email);
        MaterialButton btnSendCode = findViewById(R.id.btn_send_code);
        
        if (btnSendCode != null) {
            btnSendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                    
                    if (email.isEmpty()) {
                        Toast.makeText(ForgotActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Logic to send reset code
                    Toast.makeText(ForgotActivity.this, "Reset code sent to your email", Toast.LENGTH_SHORT).show();
                    
                    // Explicitly navigate to Forgot_oneActivity and pass email
                    Intent intent = new Intent(ForgotActivity.this, Forgot_oneActivity.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                }
            });
        }
    }
}
