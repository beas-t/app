package com.example.bladder_frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Patient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private View scrollSuggestions;
    private PatientAdapter adapter;
    private List<Patient> searchResults = new ArrayList<>();
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearchResults = findViewById(R.id.rv_search_results);
        scrollSuggestions = findViewById(R.id.scroll_suggestions);
        etSearch = findViewById(R.id.et_search);

        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(searchResults, this);
        rvSearchResults.setAdapter(adapter);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (etSearch != null) {
            etSearch.requestFocus();
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String query = s.toString().trim();
                    if (query.length() > 0) {
                        performSearch(query);
                    } else {
                        showSuggestions();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void performSearch(String query) {
        scrollSuggestions.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.VISIBLE);

        BladSenseApi api = RetrofitClient.getApi(this);
        api.getPatients(query, false).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults = response.body();
                    adapter.updateData(searchResults);
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                // Silently handle failure
            }
        });
    }

    private void showSuggestions() {
        scrollSuggestions.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        searchResults.clear();
        adapter.updateData(searchResults);
    }
}
