package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnSendMessage = findViewById(R.id.btn_send_message);
        if (btnSendMessage != null) {
            btnSendMessage.setOnClickListener(v -> {
                // Show success message
                Toast.makeText(ContactActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                
                // Navigate back to SettingsActivity
                Intent intent = new Intent(ContactActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}
