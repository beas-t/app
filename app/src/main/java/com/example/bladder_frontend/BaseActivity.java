package com.example.bladder_frontend;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bladder_frontend.api.SessionManager;
import com.example.bladder_frontend.R;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    protected SessionManager sessionManager;
    private static final String TAG = "BaseActivity";
    private TextToSpeech tts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
