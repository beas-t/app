package com.example.bladder_frontend;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Locale;

public class Appointment_oneActivity extends AppCompatActivity {

    private TextView tvDate, tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_one);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        tvDate = findViewById(R.id.tv_selected_date);
        tvTime = findViewById(R.id.tv_selected_time);
        
        View dateContainer = findViewById(R.id.date_container);
        View timeContainer = findViewById(R.id.time_container);

        dateContainer.setOnClickListener(v -> showDatePicker());
        timeContainer.setOnClickListener(v -> showTimePicker());

        MaterialButton btnSchedule = findViewById(R.id.btn_schedule);
        btnSchedule.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year1);
                    tvDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Using TimePickerDialog with Holo theme to get the scrolling spinner picker as requested
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, 
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            (view, hourOfDay, minuteOfHour) -> {
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
                tvTime.setText(selectedTime);
            }, hour, minute, true);

        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }
}
