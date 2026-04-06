package com.example.bladder_frontend;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bladder_frontend.api.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Display_oneActivity extends BaseActivity {
    private SessionManager sessionManager;
    private SwitchMaterial switchVoice, switchReader, switchContrast;
    private MaterialButton btnSmall, btnMedium, btnLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_one);

        sessionManager = new SessionManager(this);

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Switches
        switchVoice = findViewById(R.id.switch_voice);
        switchReader = findViewById(R.id.switch_reader);
        switchContrast = findViewById(R.id.switch_contrast);

        switchVoice.setChecked(sessionManager.isVoiceGuidanceEnabled());
        switchReader.setChecked(sessionManager.isScreenReaderEnabled());
        switchContrast.setChecked(sessionManager.isHighContrastEnabled());

        switchVoice.setOnCheckedChangeListener((v, isChecked) -> {
            sessionManager.setVoiceGuidanceEnabled(isChecked);
            recreate();
        });
        switchReader.setOnCheckedChangeListener((v, isChecked) -> {
            sessionManager.setScreenReaderEnabled(isChecked);
            recreate();
        });
        switchContrast.setOnCheckedChangeListener((v, isChecked) -> {
            sessionManager.setHighContrastEnabled(isChecked);
            recreate();
        });

        // Button Sizes
        btnSmall = findViewById(R.id.btn_size_small);
        btnMedium = findViewById(R.id.btn_size_medium);
        btnLarge = findViewById(R.id.btn_size_large);

        String currentSize = sessionManager.getButtonSize();
        updateButtonSizeUI(currentSize);

        btnSmall.setOnClickListener(v -> updateButtonSize("small"));
        btnMedium.setOnClickListener(v -> updateButtonSize("medium"));
        btnLarge.setOnClickListener(v -> updateButtonSize("large"));
    }

    private void updateButtonSize(String size) {
        sessionManager.saveButtonSize(size);
        updateButtonSizeUI(size);
        recreate();
        Toast.makeText(this, "Button size set to " + size, Toast.LENGTH_SHORT).show();
    }

    private void updateButtonSizeUI(String size) {
        int activeColor = getResources().getColor(R.color.colorPrimary);
        int inactiveColor = getResources().getColor(R.color.gray_600);

        btnSmall.setTextColor(size.equals("small") ? activeColor : inactiveColor);
        btnSmall.setStrokeColorResource(size.equals("small") ? R.color.colorPrimary : R.color.gray_300);

        btnMedium.setTextColor(size.equals("medium") ? activeColor : inactiveColor);
        btnMedium.setStrokeColorResource(size.equals("medium") ? R.color.colorPrimary : R.color.gray_300);

        btnLarge.setTextColor(size.equals("large") ? activeColor : inactiveColor);
        btnLarge.setStrokeColorResource(size.equals("large") ? R.color.colorPrimary : R.color.gray_300);
    }
}
