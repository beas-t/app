package com.example.bladder_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import android.widget.SeekBar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;
import android.view.WindowManager;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bladder_frontend.api.SessionManager;

public class DisplayActivity extends BaseActivity {
    private SessionManager sessionManager;
    private MaterialButton btnLight, btnDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Theme Buttons
        btnLight = findViewById(R.id.btn_theme_light);
        btnDark = findViewById(R.id.btn_theme_dark);

        btnLight.setOnClickListener(v -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO, "light"));
        btnDark.setOnClickListener(v -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES, "dark"));

        // Brightness
        SeekBar seekBrightness = findViewById(R.id.seek_brightness);
        SwitchMaterial switchAuto = findViewById(R.id.switch_auto_brightness);

        // Get initial values from session manager
        String currentTheme = sessionManager.getAppTheme();
        updateThemeButtonsUI(currentTheme);

        int curBrightness = sessionManager.getBrightness();
        seekBrightness.setProgress(curBrightness);
        setBrightness(curBrightness);

        // Initial Auto-Brightness state from system
        try {
            int mode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            switchAuto.setChecked(mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Exception e) {
            switchAuto.setChecked(sessionManager.isAutoBrightnessEnabled());
        }

        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setBrightness(progress);
                    sessionManager.saveBrightness(progress);
                    updateSystemBrightness(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        switchAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkWriteSettingsPermission()) {
                toggleAutoBrightness(isChecked);
                sessionManager.setAutoBrightness(isChecked);
            } else {
                buttonView.setChecked(!isChecked);
                requestWriteSettingsPermission();
            }
        });

    }

    private boolean checkWriteSettingsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return Settings.System.canWrite(this);
        }
        return true;
    }

    private void requestWriteSettingsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Toast.makeText(this, "Allow BladSense to modify system settings to change brightness", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(android.net.Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void toggleAutoBrightness(boolean enabled) {
        try {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                            : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Toast.makeText(this, "Auto-brightness " + (enabled ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error toggling auto-brightness", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSystemBrightness(int brightness) {
        if (checkWriteSettingsPermission()) {
            try {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
            } catch (Exception ignored) {}
        }
    }

    private void updateTheme(int mode, String themeName) {
        AppCompatDelegate.setDefaultNightMode(mode);
        sessionManager.saveAppTheme(themeName);
        updateThemeButtonsUI(themeName);
        // Recreate the activity to apply the theme change immediately
        recreate();
        Toast.makeText(this, "Theme set to " + themeName, Toast.LENGTH_SHORT).show();
    }

    private void updateThemeButtonsUI(String theme) {
        // Highlighting active button
        btnLight.setStrokeWidth(theme.equals("light") ? 4 : 2);
        btnDark.setStrokeWidth(theme.equals("dark") ? 4 : 2);
        
        btnLight.setTextColor(theme.equals("light") ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.gray_600));
        btnDark.setTextColor(theme.equals("dark") ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.gray_600));
    }

    private void setBrightness(int brightness) {
        WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
        layoutpars.screenBrightness = brightness / 255f;
        getWindow().setAttributes(layoutpars);
    }
}
