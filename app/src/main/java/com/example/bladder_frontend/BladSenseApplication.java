package com.example.bladder_frontend;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import com.example.bladder_frontend.api.SessionManager;

public class BladSenseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Force English locale
        LocaleListCompat appLocales = LocaleListCompat.forLanguageTags("en");
        AppCompatDelegate.setApplicationLocales(appLocales);

        // Apply saved theme
        SessionManager sessionManager = new SessionManager(this);
        String theme = sessionManager.getAppTheme();
        if ("dark".equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
