package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Team_twoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_two);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Team_twoActivity.this, Notification_oneActivity.class));
                }
            });
        }

        // Get data from intent
        String profileType = getIntent().getStringExtra("PROFILE_TYPE");
        String name = getIntent().getStringExtra("NAME");
        String role = getIntent().getStringExtra("ROLE");
        String medicalId = getIntent().getStringExtra("MEDICAL_ID");
        String email = getIntent().getStringExtra("EMAIL");
        String avatarChar = getIntent().getStringExtra("AVATAR_CHAR");
        int avatarColor = getIntent().getIntExtra("AVATAR_COLOR", 0xFF3B82F6);

        // Update UI
        if (profileType != null) {
            ((TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title)).setText(profileType);
        }
        
        if (name != null) {
            ((TextView) findViewById(R.id.profile_name)).setText(name);
        }
        
        if (role != null) {
            ((TextView) findViewById(R.id.profile_role)).setText(role);
        }
        
        if (medicalId != null) {
            ((TextView) findViewById(R.id.profile_id)).setText(medicalId);
        }
        
        if (email != null) {
            ((TextView) findViewById(R.id.email_text)).setText(email);
        }

        if (avatarChar != null) {
            ((TextView) findViewById(R.id.profile_avatar)).setText(avatarChar);
        }
        
        findViewById(R.id.profile_avatar).getBackground().setTint(avatarColor);
    }
}
