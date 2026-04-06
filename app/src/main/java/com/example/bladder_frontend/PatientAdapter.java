package com.example.bladder_frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bladder_frontend.api.models.Patient;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patients;
    private Context context;

    public PatientAdapter(List<Patient> patients, Context context) {
        this.patients = patients;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.tvPatientName.setText(patient.getName());
        holder.tvPatientDetails.setText(patient.getPatientId() + " • " + patient.getAge() + "y • " + patient.getGender());
        holder.tvLastScan.setText("Last: " + (patient.getLastScanDate() != null ? patient.getLastScanDate() : "N/A"));
        holder.tvScanCount.setText(patient.getScanCount() + " Scans");
        holder.tvPatientStatus.setText(patient.getStatus() != null ? patient.getStatus() : "Normal");

        if ("Critical".equalsIgnoreCase(patient.getStatus())) {
            holder.tvPatientStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_white_bg));
            holder.tvPatientStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_red_light));
            holder.tvPatientStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else if ("Retention".equalsIgnoreCase(patient.getStatus())) {
            holder.tvPatientStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_grey));
            holder.tvPatientStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_orange_light));
            holder.tvPatientStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        } else {
            holder.tvPatientStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_green));
            holder.tvPatientStatus.setBackgroundTintList(null);
            holder.tvPatientStatus.setTextColor(ContextCompat.getColor(context, R.color.bottom_nav_item_color));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Patient_oneActivity.class);
            intent.putExtra("patient", patient);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void updateData(List<Patient> newPatients) {
        this.patients = newPatients;
        notifyDataSetChanged();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvPatientDetails, tvPatientStatus, tvLastScan, tvScanCount;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvPatientDetails = itemView.findViewById(R.id.tv_patient_details);
            tvPatientStatus = itemView.findViewById(R.id.tv_patient_status);
            tvLastScan = itemView.findViewById(R.id.tv_last_scan);
            tvScanCount = itemView.findViewById(R.id.tv_scan_count);
        }
    }
}
