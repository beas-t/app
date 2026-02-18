package com.example.bladder_frontend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class TeamActivity extends AppCompatActivity {

    private TextView filterAll, filterDoctors, filterNurses, filterTechnicians;
    private MaterialCardView cardSarah, cardEmily, cardMike;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // Initialize Filter Buttons
        filterAll = findViewById(R.id.filter_all);
        filterDoctors = findViewById(R.id.filter_doctors);
        filterNurses = findViewById(R.id.filter_nurses);
        filterTechnicians = findViewById(R.id.filter_technicians);

        // Initialize Member Cards
        cardSarah = findViewById(R.id.card_sarah_johnson);
        cardEmily = findViewById(R.id.card_emily_chen);
        cardMike = findViewById(R.id.card_mike_rodriguez);

        // Initialize List Title
        tvTitle = findViewById(R.id.tv_team_members_title);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ImageView searchIcon = findViewById(R.id.search_icon_toolbar);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                startActivity(new Intent(TeamActivity.this, SearchActivity.class));
            });
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(v -> {
                startActivity(new Intent(TeamActivity.this, Notification_oneActivity.class));
            });
        }

        MaterialButton btnInvite = findViewById(R.id.btn_invite);
        btnInvite.setOnClickListener(v -> {
            Intent intent = new Intent(TeamActivity.this, Team_oneActivity.class);
            startActivity(intent);
        });

        // Set Filter Click Listeners
        filterAll.setOnClickListener(v -> applyFilter("all"));
        filterDoctors.setOnClickListener(v -> applyFilter("doctors"));
        filterNurses.setOnClickListener(v -> applyFilter("nurses"));
        filterTechnicians.setOnClickListener(v -> applyFilter("technicians"));

        // Set Individual Card Click Listeners for Navigation
        cardSarah.setOnClickListener(v -> {
            Intent intent = new Intent(TeamActivity.this, Team_twoActivity.class);
            startActivity(intent);
        });

        cardEmily.setOnClickListener(v -> {
            Intent intent = new Intent(TeamActivity.this, Team_threeActivity.class);
            startActivity(intent);
        });

        cardMike.setOnClickListener(v -> {
            Intent intent = new Intent(TeamActivity.this, Team_fourActivity.class);
            startActivity(intent);
        });
    }

    private void applyFilter(String category) {
        // Reset all filters to default gray style
        resetFilters();

        // Hide all member cards by default
        cardSarah.setVisibility(View.GONE);
        cardEmily.setVisibility(View.GONE);
        cardMike.setVisibility(View.GONE);

        switch (category) {
            case "all":
                highlightFilter(filterAll);
                cardSarah.setVisibility(View.VISIBLE);
                cardEmily.setVisibility(View.VISIBLE);
                cardMike.setVisibility(View.VISIBLE);
                tvTitle.setText("Team Members (4)");
                break;
            case "doctors":
                highlightFilter(filterDoctors);
                cardSarah.setVisibility(View.VISIBLE);
                tvTitle.setText("Doctors Profile");
                break;
            case "nurses":
                highlightFilter(filterNurses);
                cardEmily.setVisibility(View.VISIBLE);
                tvTitle.setText("Nurses Profile");
                break;
            case "technicians":
                highlightFilter(filterTechnicians);
                cardMike.setVisibility(View.VISIBLE);
                tvTitle.setText("Technicians Profile");
                break;
        }
    }

    private void resetFilters() {
        TextView[] filters = {filterAll, filterDoctors, filterNurses, filterTechnicians};
        for (TextView f : filters) {
            f.setBackgroundResource(R.drawable.bg_tag_grey);
            f.setTextColor(Color.parseColor("#4B5563"));
            f.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private void highlightFilter(TextView filter) {
        filter.setBackgroundResource(R.drawable.bg_tag_green);
        filter.getBackground().setTint(Color.parseColor("#0066CC"));
        filter.setTextColor(Color.WHITE);
        filter.setTypeface(null, android.graphics.Typeface.BOLD);
    }
}
