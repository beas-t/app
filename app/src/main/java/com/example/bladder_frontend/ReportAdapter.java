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
import com.example.bladder_frontend.api.models.ScanReport;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<ScanReport> reports;
    private Context context;

    public ReportAdapter(List<ScanReport> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ScanReport report = reports.get(position);
        holder.tvPatientName.setText(report.getPatientName());
        holder.tvReportInfo.setText(report.getReportId() + " • " + report.getScanDate());
        holder.tvVolume.setText(report.getVolume());
        holder.tvStatus.setText(report.getStatus());

        if ("Distended".equalsIgnoreCase(report.getStatus())) {
            holder.tvStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_grey));
            holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_orange_light));
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        } else {
            holder.tvStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_tag_green));
            holder.tvStatus.setBackgroundTintList(null);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.bottom_nav_item_color));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Report_oneActivity.class);
            intent.putExtra("patient", report.getPatient()); // Pass the nested patient object
            intent.putExtra("patient_name", report.getPatientName());
            intent.putExtra("report_id", report.getReportId());
            intent.putExtra("scan_date", report.getScanDate());
            intent.putExtra("volume", report.getVolume());
            intent.putExtra("status", report.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public void updateData(List<ScanReport> newReports) {
        this.reports = newReports;
        notifyDataSetChanged();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName, tvReportInfo, tvVolume, tvStatus;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvReportInfo = itemView.findViewById(R.id.tv_report_info);
            tvVolume = itemView.findViewById(R.id.tv_volume);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
