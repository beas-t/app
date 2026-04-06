package com.example.bladder_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bladder_frontend.api.models.Appointment;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        
        // Parse time (expecting "HH:mm:ss" or "HH:mm")
        String fullTime = appointment.getTime();
        if (fullTime != null && fullTime.contains(":")) {
            String[] parts = fullTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            String period = hour >= 12 ? "PM" : "AM";
            int displayHour = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
            holder.tvTime.setText(String.format("%02d:%s", displayHour, parts[1]));
            holder.tvPeriod.setText(period);
        } else {
            holder.tvTime.setText(fullTime);
            holder.tvPeriod.setText("");
        }

        holder.tvDuration.setText(appointment.getDurationMinutes() + "m");
        holder.tvPatientName.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "General Appointment");
        holder.tvType.setText(appointment.getAppointmentType());
        holder.tvLocation.setText(appointment.getLocation());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void updateData(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPeriod, tvDuration, tvPatientName, tvType, tvLocation;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);
            tvPeriod = itemView.findViewById(R.id.tv_appointment_period);
            tvDuration = itemView.findViewById(R.id.tv_appointment_duration);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvType = itemView.findViewById(R.id.tv_appointment_type);
            tvLocation = itemView.findViewById(R.id.tv_appointment_location);
        }
    }
}
