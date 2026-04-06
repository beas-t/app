package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bladder_frontend.api.BladSenseApi;
import com.example.bladder_frontend.api.RetrofitClient;
import com.example.bladder_frontend.api.models.Patient;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Patient_threeActivity extends AppCompatActivity {

    private Patient patient;
    private EditText etName, etPatientId, etAge, etHistory;
    private Spinner spinnerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_three);

        // Get patient from intent
        patient = (Patient) getIntent().getSerializableExtra("patient");

        etName = findViewById(R.id.et_name);
        etPatientId = findViewById(R.id.et_patient_id);
        etAge = findViewById(R.id.et_age);
        etHistory = findViewById(R.id.et_history);
        spinnerGender = findViewById(R.id.spinner_gender);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        View notificationContainer = findViewById(R.id.notification_container);
        if (notificationContainer != null) {
            notificationContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Patient_threeActivity.this, Notification_oneActivity.class));
                }
            });
        }

        ImageView searchIcon = findViewById(R.id.search_icon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Patient_threeActivity.this, SearchActivity.class));
                }
            });
        }

        // Setup Gender Spinner
        String[] genders = {"Male", "Female", "Other"};
        if (spinnerGender != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGender.setAdapter(adapter);
        }

        // Pre-fill data
        if (patient != null) {
            if (etName != null) etName.setText(patient.getName());
            if (etPatientId != null) etPatientId.setText(patient.getPatientId());
            if (etAge != null) etAge.setText(String.valueOf(patient.getAge()));
            // Assuming medical history might be stored in 'condition' or similar, but for now we'll just pre-fill if available
            if (etHistory != null && patient.getCondition() != null) etHistory.setText(patient.getCondition());

            if (spinnerGender != null) {
                for (int i = 0; i < genders.length; i++) {
                    if (genders[i].equalsIgnoreCase(patient.getGender())) {
                        spinnerGender.setSelection(i);
                        break;
                    }
                }
            }
        }

        MaterialButton btnSave = findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChanges();
                }
            });
        }
    }

    private void saveChanges() {
        if (patient == null) {
            Toast.makeText(this, "Error: Patient data missing", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String patientIdStr = etPatientId.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String history = etHistory.getText().toString().trim();

        if (name.isEmpty() || patientIdStr.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("name", name);
        updateData.put("patient_id", patientIdStr);
        try {
            updateData.put("age", Integer.parseInt(ageStr));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid age format", Toast.LENGTH_SHORT).show();
            return;
        }
        updateData.put("gender", gender);
        updateData.put("condition", history); // Mapping history to condition

        BladSenseApi api = RetrofitClient.getApi(this);
        api.updatePatient(patient.getId(), updateData).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Patient_threeActivity.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                    
                    // We need to return the updated patient to Patient_oneActivity so it refreshes
                    Intent intent = new Intent();
                    intent.putExtra("updated_patient", response.body());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(Patient_threeActivity.this, "Failed to update patient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(Patient_threeActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
