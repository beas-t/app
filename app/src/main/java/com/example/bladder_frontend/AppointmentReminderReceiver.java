package com.example.bladder_frontend;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AppointmentReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "appointment_reminders";

    @Override
    public void onReceive(Context context, Intent intent) {
        String patientName = intent.getStringExtra("patient_name");
        String appointmentType = intent.getStringExtra("appointment_type");
        String time = intent.getStringExtra("time");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Appointment Reminders", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent openIntent = new Intent(context, AppointmentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications) // Fixed: changed ic_notification to ic_notifications
                .setContentTitle("Upcoming Appointment")
                .setContentText("Appointment with " + patientName + " for " + appointmentType + " at " + time)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
