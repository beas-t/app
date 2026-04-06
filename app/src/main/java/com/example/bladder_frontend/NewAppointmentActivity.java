package com.example.bladder_frontend;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Appointment;
import com.example.bladder_frontend.api.models.Patient;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends AppCompatActivity {

    private Spinner spinnerPatients;
    private EditText etType, etLocation, etDuration;
    private TextView tvDate, tvTime;
    private List<Patient> patientList = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        spinnerPatients = findViewById(R.id.spinner_patients);
        etType = findViewById(R.id.et_type);
        etLocation = findViewById(R.id.et_location);
        etDuration = findViewById(R.id.et_duration);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        MaterialButton btnSave = findViewById(R.id.btn_save);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // Default date from intent
        String initialDate = getIntent().getStringExtra("selected_date");
        if (initialDate != null) {
            tvDate.setText(initialDate);
            try {
                calendar.setTime(dateFormat.parse(initialDate));
            } catch (Exception ignored) {}
        } else {
            tvDate.setText(dateFormat.format(calendar.getTime()));
        }
        tvTime.setText(timeFormat.format(calendar.getTime()));

        tvDate.setOnClickListener(v -> showDatePicker());
        tvTime.setOnClickListener(v -> showTimePicker());
        btnSave.setOnClickListener(v -> saveAppointment());

        fetchPatients();
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tvDate.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            tvTime.setText(timeFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void fetchPatients() {
        BladSenseApi api = RetrofitClient.getApi(this);
        api.getPatients(null, false).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    patientList = response.body();
                    List<String> names = new ArrayList<>();
                    for (Patient p : patientList) names.add(p.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_spinner_item, names);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPatients.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Failed to load patients", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAppointment() {
        if (patientList.isEmpty()) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient selectedPatient = patientList.get(spinnerPatients.getSelectedItemPosition());
        Map<String, Object> data = new HashMap<>();
        data.put("patient", selectedPatient.getId());
        data.put("patient_name", selectedPatient.getName());
        data.put("appointment_type", etType.getText().toString());
        data.put("date", tvDate.getText().toString());
        data.put("time", tvTime.getText().toString());
        data.put("location", etLocation.getText().toString());
        
        int duration = 30;
        try {
            duration = Integer.parseInt(etDuration.getText().toString());
        } catch (Exception ignored) {}
        data.put("duration_minutes", duration);

        BladSenseApi api = RetrofitClient.getApi(this);
        api.createAppointment(data).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    scheduleNotification(response.body());
                    Toast.makeText(NewAppointmentActivity.this, "Appointment Scheduled Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMsg = "Failed to save appointment";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(NewAppointmentActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scheduleNotification(Appointment appointment) {
        Intent intent = new Intent(this, AppointmentReminderReceiver.class);
        intent.putExtra("patient_name", appointment.getPatientName());
        intent.putExtra("appointment_type", appointment.getAppointmentType());
        intent.putExtra("time", appointment.getTime());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, appointment.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Notify 30 minutes before
        long triggerTime = calendar.getTimeInMillis() - (30 * 60 * 1000);
        if (triggerTime > System.currentTimeMillis()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }
}
