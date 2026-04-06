package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.AnalyticsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Patient_twoActivity extends AppCompatActivity {

    private TextView tvTotalPatients, tvDischargeRate;
    private TextView tvMalePct, tvFemalePct, tvOtherPct;
    private ProgressBar pbMale, pbFemale, pbOther;
    private View bar0_18, bar19_30, bar31_50, bar51_70, bar70Plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_two);

        initViews();
        setupListeners();
        fetchAnalytics();
    }

    private void initViews() {
        tvTotalPatients = findViewById(R.id.tv_total_patients);
        tvDischargeRate = findViewById(R.id.tv_discharge_rate);
        
        tvMalePct = findViewById(R.id.tv_male_pct);
        tvFemalePct = findViewById(R.id.tv_female_pct);
        tvOtherPct = findViewById(R.id.tv_other_pct);
        
        pbMale = findViewById(R.id.pb_male);
        pbFemale = findViewById(R.id.pb_female);
        pbOther = findViewById(R.id.pb_other);
        
        bar0_18 = findViewById(R.id.bar_0_18);
        bar19_30 = findViewById(R.id.bar_19_30);
        bar31_50 = findViewById(R.id.bar_31_50);
        bar51_70 = findViewById(R.id.bar_51_70);
        bar70Plus = findViewById(R.id.bar_70_plus);
    }

    private void setupListeners() {
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> startActivity(new Intent(Patient_twoActivity.this, SearchActivity.class)));
        }
    }

    private void fetchAnalytics() {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getAnalytics().enqueue(new Callback<AnalyticsData>() {
            @Override
            public void onResponse(Call<AnalyticsData> call, Response<AnalyticsData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    Toast.makeText(Patient_twoActivity.this, "Failed to load statistics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AnalyticsData> call, Throwable t) {
                Toast.makeText(Patient_twoActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(AnalyticsData data) {
        // Summary
        tvTotalPatients.setText(String.valueOf(data.getTotalPatients()));
        tvDischargeRate.setText(data.getDischargeRate() + "%");

        // Demographics
        int total = data.getTotalPatients();
        if (total > 0) {
            int malePct = data.getMaleCount() * 100 / total;
            int femalePct = data.getFemaleCount() * 100 / total;
            int otherPct = 100 - (malePct + femalePct);

            tvMalePct.setText(malePct + "%");
            pbMale.setProgress(malePct);
            
            tvFemalePct.setText(femalePct + "%");
            pbFemale.setProgress(femalePct);
            
            tvOtherPct.setText(otherPct + "%");
            pbOther.setProgress(otherPct);
        }

        // Age Distribution Bars
        updateAgeBars(data);
    }

    private void updateAgeBars(AnalyticsData data) {
        int max = Math.max(data.getAge0_18(), 
                  Math.max(data.getAge19_30(), 
                  Math.max(data.getAge31_50(), 
                  Math.max(data.getAge51_70(), data.getAge70Plus()))));
        
        if (max == 0) max = 1;
        
        // 140dp is slightly less than the 160dp container height
        float maxHeightPx = getResources().getDisplayMetrics().density * 130;
        
        setBarHeight(bar0_18, data.getAge0_18(), max, maxHeightPx);
        setBarHeight(bar19_30, data.getAge19_30(), max, maxHeightPx);
        setBarHeight(bar31_50, data.getAge31_50(), max, maxHeightPx);
        setBarHeight(bar51_70, data.getAge51_70(), max, maxHeightPx);
        setBarHeight(bar70Plus, data.getAge70Plus(), max, maxHeightPx);
    }

    private void setBarHeight(View bar, int count, int max, float maxHeightPx) {
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = (int) (maxHeightPx * count / max);
        if (params.height < 5) params.height = 5; // Minimum height to be visible
        bar.setLayoutParams(params);
    }
}
