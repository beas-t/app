package com.example.bladder_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bladder_frontend.api.models.Backup;
import java.util.List;

public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.BackupViewHolder> {

    private List<Backup> backups;
    private Context context;

    public BackupAdapter(List<Backup> backups, Context context) {
        this.backups = backups;
        this.context = context;
    }

    @NonNull
    @Override
    public BackupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_backup, parent, false);
        return new BackupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BackupViewHolder holder, int position) {
        Backup backup = backups.get(position);
        holder.tvBackupDate.setText(backup.getCreatedAt() != null ? backup.getCreatedAt() : "Unknown Date");
        holder.tvBackupSize.setText(backup.getFileSize());

        if ("Failed".equalsIgnoreCase(backup.getStatus())) {
            holder.ivStatusIcon.setImageResource(R.drawable.ic_warning);
            holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_red_dark));
            holder.ivBackupIcon.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_red_light));
        } else {
            holder.ivStatusIcon.setImageResource(R.drawable.ic_check_circle);
            holder.ivStatusIcon.setColorFilter(ContextCompat.getColor(context, ContextCompat.getColor(context, R.color.bottom_nav_item_color)));
            holder.ivBackupIcon.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_green_light));
        }
    }

    @Override
    public int getItemCount() {
        return backups.size();
    }

    public void updateData(List<Backup> newBackups) {
        this.backups = newBackups;
        notifyDataSetChanged();
    }

    public static class BackupViewHolder extends RecyclerView.ViewHolder {
        TextView tvBackupDate, tvBackupSize;
        ImageView ivBackupIcon, ivStatusIcon;

        public BackupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBackupDate = itemView.findViewById(R.id.tv_backup_date);
            tvBackupSize = itemView.findViewById(R.id.tv_backup_size);
            ivBackupIcon = itemView.findViewById(R.id.iv_backup_icon);
            ivStatusIcon = itemView.findViewById(R.id.iv_status_icon);
        }
    }
}
