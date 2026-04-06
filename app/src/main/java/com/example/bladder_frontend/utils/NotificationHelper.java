package com.example.bladder_frontend.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import com.example.bladder_frontend.R;
import com.example.bladder_frontend.api.SessionManager;
import java.io.File;
import java.util.Calendar;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 1;
    private static final int TEST_NOTIFICATION_ID = 2;
    private static final String CHANNEL_NAME = "Export Reports";

    private static String getChannelId(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String soundName = sessionManager.getNotificationSound();
        // Use a dynamic channel ID based on the sound name to ensure sound updates are applied
        return "export_notifications_" + soundName.toLowerCase().replace(" ", "_");
    }

    private static Uri getSoundUri(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String soundName = sessionManager.getNotificationSound();

        // Map strings to system URIs (matching NotificationActivity logic)
        if (soundName.equalsIgnoreCase("Urgent")) {
            return android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM);
        } else if (soundName.equalsIgnoreCase("Medical Alert")) {
            return android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_RINGTONE);
        } else if (soundName.contains("Gentle")) {
            return android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION);
        } else {
            return android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION);
        }
    }

    public static void showTestNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getChannelId(context);
        createChannel(context, notificationManager, channelId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(getSoundUri(context))
                .setAutoCancel(true);

        notificationManager.notify(TEST_NOTIFICATION_ID, builder.build());
    }

    private static void createChannel(Context context, NotificationManager notificationManager, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Check if channel already exists
            if (notificationManager.getNotificationChannel(channelId) != null) {
                return;
            }

            Uri soundUri = getSoundUri(context);
            NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for exported medical reports");
            
            if (soundUri != null) {
                android.media.AudioAttributes audioAttributes = new android.media.AudioAttributes.Builder()
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setSound(soundUri, audioAttributes);
            }
            
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static boolean shouldShowNotification(Context context, String type) {
        SessionManager sessionManager = new SessionManager(context);
        return sessionManager.isNotificationEnabled(type);
    }

    public static void showDownloadProgressNotification(Context context, String fileName, int notificationId) {
        if (!shouldShowNotification(context, "report")) return;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getChannelId(context);
        createChannel(context, notificationManager, channelId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Downloading Report")
                .setContentText("Generating professional PDF: " + fileName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setProgress(0, 0, true)
                .setOngoing(true)
                .setAutoCancel(false);

        notificationManager.notify(notificationId, builder.build());
    }

    public static void showExportNotification(Context context, File file) {
        showExportNotification(context, file, NOTIFICATION_ID);
    }

    public static void showExportNotification(Context context, File file, int notificationId) {
        if (!shouldShowNotification(context, "report")) return;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getChannelId(context);
        createChannel(context, notificationManager, channelId);

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        openIntent.setDataAndType(contentUri, "application/pdf");
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Report Downloaded Successfully")
                .setContentText("Medical report " + file.getName() + " is ready in Downloads.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(getSoundUri(context))
                .setAutoCancel(true)
                .setOngoing(false)
                .setProgress(0, 0, false)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, builder.build());
    }
}
