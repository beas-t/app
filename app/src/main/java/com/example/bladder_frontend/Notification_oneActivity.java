package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Notification_oneActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private java.util.List<com.example.bladder_frontend.api.models.Notification> notificationList = new java.util.ArrayList<>();
    private com.example.bladder_frontend.api.BladSenseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_one);

        api = com.example.bladder_frontend.api.RetrofitClient.getApi(this);

        rvNotifications = findViewById(R.id.rv_notifications);
        rvNotifications.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this);
        rvNotifications.setAdapter(adapter);

        fetchNotifications();

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Notification_oneActivity.this, SearchActivity.class));
                }
            });
        }

        android.widget.TextView btnMarkRead = findViewById(R.id.btn_mark_read);
        if (btnMarkRead != null) {
            btnMarkRead.setOnClickListener(v -> markAllAsRead());
        }
    }

    private void fetchNotifications() {
        api.getNotifications().enqueue(new retrofit2.Callback<java.util.List<com.example.bladder_frontend.api.models.Notification>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.List<com.example.bladder_frontend.api.models.Notification>> call, retrofit2.Response<java.util.List<com.example.bladder_frontend.api.models.Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notificationList = response.body();
                    adapter.updateData(notificationList);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.List<com.example.bladder_frontend.api.models.Notification>> call, Throwable t) {
                android.widget.Toast.makeText(Notification_oneActivity.this, "Failed to load notifications", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markAllAsRead() {
        // In a real app, send a batch API request
        android.widget.Toast.makeText(this, "Marking all as read...", android.widget.Toast.LENGTH_SHORT).show();
        for (com.example.bladder_frontend.api.models.Notification n : notificationList) {
            if (!n.isRead()) {
                api.markNotificationRead(n.getId()).enqueue(new retrofit2.Callback<java.util.Map<String, String>>() {
                    @Override
                    public void onResponse(retrofit2.Call<java.util.Map<String, String>> call, retrofit2.Response<java.util.Map<String, String>> response) {}
                    @Override
                    public void onFailure(retrofit2.Call<java.util.Map<String, String>> call, Throwable t) {}
                });
            }
        }
        fetchNotifications(); // Refresh
    }
}
