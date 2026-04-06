package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        
        View btnDataPrivacy = findViewById(R.id.btn_data_privacy_detail);
        if (btnDataPrivacy != null) {
            btnDataPrivacy.setOnClickListener(v -> {
                Intent intent = new Intent(PrivacyActivity.this, Privacy_oneActivity.class);
                startActivity(intent);
            });
        }
    }
}
