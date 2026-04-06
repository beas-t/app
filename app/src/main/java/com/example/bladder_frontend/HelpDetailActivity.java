package com.example.bladder_frontend;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        HelpArticle article = (HelpArticle) getIntent().getSerializableExtra("article");

        ImageView btnBack = findViewById(R.id.btn_back);
        TextView textToolbarTitle = findViewById(R.id.text_toolbar_title);
        TextView textTitle = findViewById(R.id.text_article_title);
        TextView textContent = findViewById(R.id.text_article_content);

        btnBack.setOnClickListener(v -> finish());

        if (article != null) {
            textToolbarTitle.setText("Help Article");
            textTitle.setText(article.getTitle());
            textContent.setText(article.getContent());
        }
    }
}
