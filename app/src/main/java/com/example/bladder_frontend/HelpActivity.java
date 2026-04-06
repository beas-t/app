package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private HelpArticleAdapter adapter;
    private List<HelpArticle> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setupToolbar();
        setupSearch();
        setupRecyclerView();
    }

    private void setupToolbar() {
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void setupSearch() {
        EditText editSearch = findViewById(R.id.edit_search);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_help_topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();
        loadMockArticles();

        adapter = new HelpArticleAdapter(articleList, article -> {
            Intent intent = new Intent(this, HelpDetailActivity.class);
            intent.putExtra("article", article);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadMockArticles() {
        articleList.add(new HelpArticle("Getting Started with BladSense",
                "BladSense is your ultimate tool for non-invasive bladder volume monitoring. " +
                "To get started, ensures your device is fully charged and connected to the BladSense probe. " +
                "Follow the on-screen instructions to calibrate the device for the first use."));

        articleList.add(new HelpArticle("How to Perform a Scan",
                "1. Apply a small amount of ultrasound gel to the probe.\n" +
                "2. Place the probe on the patient's lower abdomen, just above the pubic bone.\n" +
                "3. Press the 'Scan' button in the app.\n" +
                "4. Keep the probe steady until the progress bar completes."));

        articleList.add(new HelpArticle("Understanding Volume Measurements",
                "The bladder volume is measured in milliliters (mL). " +
                "Results are displayed immediately after the scan is processed. " +
                "Normal adult bladder capacity typically ranges from 300mL to 500mL, " +
                "but this can vary based on the patient's age and clinical condition."));

        articleList.add(new HelpArticle("Interpreting AI Results",
                "Our advanced AI engine analyzes the ultrasound reflections to estimate volume. " +
                "A 'High Accuracy' badge means the scan was clear and the result is reliable. " +
                "If the result is 'Indeterminate', try repositioning the probe and scanning again."));

        articleList.add(new HelpArticle("Patient Data Management",
                "All patient data is stored securely and encrypted. " +
                "You can view recent scans in the 'Patients' tab. " +
                "To export a report, select the scan and click 'Generate PDF'. " +
                "Ensure you follow your facility's HIPAA and privacy guidelines when handling data."));
    }
}
