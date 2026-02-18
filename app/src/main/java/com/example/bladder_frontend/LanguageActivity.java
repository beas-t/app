package com.example.bladder_frontend;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class LanguageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Set up click listeners for language cards
        setupLanguageCard(R.id.card_english);
        setupLanguageCard(R.id.card_spanish);
        setupLanguageCard(R.id.card_french);
        setupLanguageCard(R.id.card_german);
        setupLanguageCard(R.id.card_italian);
        setupLanguageCard(R.id.card_portuguese);
        setupLanguageCard(R.id.card_chinese);
        setupLanguageCard(R.id.card_japanese);
        setupLanguageCard(R.id.card_korean);
        setupLanguageCard(R.id.card_arabic);
    }

    private void setupLanguageCard(int cardId) {
        MaterialCardView card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> {
                // Handle language selection
                finish();
            });
        }
    }
}
