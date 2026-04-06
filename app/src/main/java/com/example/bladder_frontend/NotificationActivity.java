package com.example.bladder_frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.utils.NotificationHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotificationActivity extends AppCompatActivity {
    
    private SessionManager sessionManager;
    private TextView tvSelectedSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tvSelectedSound = findViewById(R.id.tv_selected_sound);
        if (tvSelectedSound != null) {
            tvSelectedSound.setText(sessionManager.getNotificationSound());
        }

        View btnSoundSelector = findViewById(R.id.btn_sound_selector);
        if (btnSoundSelector != null) {
            btnSoundSelector.setOnClickListener(v -> showSoundSelectorDialog());
        }

        View btnTestNotif = findViewById(R.id.btn_test_notif);
        if (btnTestNotif != null) {
            btnTestNotif.setOnClickListener(v -> sendTestNotification());
        }

        setupSwitches();
    }

    private void setupSwitches() {
        setupSwitch(R.id.switch_critical, "critical");
        setupSwitch(R.id.switch_patient, "patient");
        setupSwitch(R.id.switch_report, "report");
        setupSwitch(R.id.switch_system, "system");
        setupSwitch(R.id.switch_marketing, "marketing");
        setupSwitch(R.id.switch_quiet, "quiet");
    }

    private void setupSwitch(int id, String type) {
        SwitchMaterial sw = findViewById(id);
        if (sw != null) {
            sw.setChecked(sessionManager.isNotificationEnabled(type));
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sessionManager.setNotificationEnabled(type, isChecked);
            });
        }
    }

    private android.media.Ringtone currentRingtone;

    private void stopCurrentSound() {
        if (currentRingtone != null && currentRingtone.isPlaying()) {
            currentRingtone.stop();
        }
    }

    private void showSoundSelectorDialog() {
        String[] sounds = {
            getString(R.string.sound_urgent),
            getString(R.string.sound_default),
            getString(R.string.sound_medical_alert),
            getString(R.string.sound_gentle)
        };

        String currentSound = sessionManager.getNotificationSound();
        final int[] selectedIndex = {0};
        for (int i = 0; i < sounds.length; i++) {
            if (sounds[i].equals(currentSound)) {
                selectedIndex[0] = i;
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.label_notif_sound);
        
        builder.setSingleChoiceItems(sounds, selectedIndex[0], (dialog, which) -> {
            selectedIndex[0] = which;
            playPreviewSound(which);
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            stopCurrentSound();
            String selectedSound = sounds[selectedIndex[0]];
            sessionManager.saveNotificationSound(selectedSound);
            tvSelectedSound.setText(selectedSound);
            Toast.makeText(this, "Sound updated to " + selectedSound, Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> stopCurrentSound());
        
        builder.setOnDismissListener(dialog -> stopCurrentSound());
        
        builder.show();
    }

    private void playPreviewSound(int which) {
        stopCurrentSound();
        
        android.net.Uri soundUri;
        switch (which) {
            case 0: // Urgent
                soundUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM);
                break;
            case 2: // Medical Alert
                soundUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_RINGTONE);
                break;
            case 3: // Gentle Chime
                soundUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION);
                break;
            case 1: // Default
            default:
                soundUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION);
                break;
        }

        if (soundUri != null) {
            currentRingtone = android.media.RingtoneManager.getRingtone(this, soundUri);
            if (currentRingtone != null) {
                currentRingtone.play();
                
                // Stop after 2 seconds to keep it a "sample"
                new android.os.Handler().postDelayed(() -> {
                    if (currentRingtone != null && currentRingtone.isPlaying()) {
                        currentRingtone.stop();
                    }
                }, 2000);
            }
        }
    }

    private void sendTestNotification() {
        NotificationHelper.showTestNotification(this, "System Test", "This is a professional test notification from BladSense.");
        Toast.makeText(this, "Test notification sent!", Toast.LENGTH_SHORT).show();
    }
}
