package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Appointment;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();
    private TextView tvUpcomingCount;
    private String selectedDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private List<TextView> dateTextViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        tvUpcomingCount = findViewById(R.id.tv_upcoming_count);
        rvAppointments = findViewById(R.id.rv_appointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(appointmentList, this);
        rvAppointments.setAdapter(adapter);

        // Calendar Initialization
        selectedDate = sdf.format(new Date());
        setupCalendar();
        fetchAppointments(selectedDate);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        MaterialButton btnNewSchedule = findViewById(R.id.btn_new_schedule);
        if (btnNewSchedule != null) {
            btnNewSchedule.setOnClickListener(v -> {
                Intent intent = new Intent(this, NewAppointmentActivity.class);
                intent.putExtra("selected_date", selectedDate);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAppointments(selectedDate);
    }

    private void setupCalendar() {
        Calendar cal = Calendar.getInstance();
        // Move to start of week (Sunday)
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        
        int[] dateIds = {R.id.tv_date_1, R.id.tv_date_2, R.id.tv_date_3, R.id.tv_date_4, R.id.tv_date_5, R.id.tv_date_6, R.id.tv_date_7};
        
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        
        TextView tvMonthYear = findViewById(R.id.tv_month_year);
        if (tvMonthYear != null) {
            tvMonthYear.setText(monthYearFormat.format(new Date()));
        }

        for (int i = 0; i < 7; i++) {
            TextView tvDate = findViewById(dateIds[i]);
            Date d = cal.getTime();
            String dateString = sdf.format(d);
            
            if (tvDate != null) {
                tvDate.setText(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                
                // Highlight if selected
                if (dateString.equals(selectedDate)) {
                    tvDate.setBackgroundResource(R.drawable.circle_image);
                    tvDate.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#0066CC")));
                    tvDate.setTextColor(android.graphics.Color.WHITE);
                } else {
                    tvDate.setBackground(null);
                    tvDate.setTextColor(android.graphics.Color.parseColor("#6B7280"));
                }
                
                tvDate.setOnClickListener(v -> {
                    selectedDate = dateString;
                    setupCalendar(); // Refresh UI
                    fetchAppointments(selectedDate);
                });
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void fetchAppointments(String date) {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getAppointments(date).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    appointmentList = response.body();
                    adapter.updateData(appointmentList);
                    if (tvUpcomingCount != null) {
                        tvUpcomingCount.setText("Upcoming (" + appointmentList.size() + ")");
                    }
                } else {
                    if (tvUpcomingCount != null) {
                        tvUpcomingCount.setText("Upcoming (0)");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Toast.makeText(AppointmentActivity.this, "Failed to load appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
