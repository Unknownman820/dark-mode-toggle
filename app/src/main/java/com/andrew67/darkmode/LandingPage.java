package com.andrew67.darkmode;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LandingPage extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "DarkModePage";

    private RadioGroup radioGroup;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) Log.d(TAG,
                "onCreate savedInstanceState null = " + (savedInstanceState == null));

        setContentView(R.layout.activity_main);
        uiModeManager = ContextCompat.getSystemService(this, UiModeManager.class);

        radioGroup = findViewById(R.id.radioGroup);
        if (savedInstanceState == null) updateRadioGroup();
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void updateRadioGroup() {
        switch (uiModeManager.getNightMode()) {
            case UiModeManager.MODE_NIGHT_NO:
                radioGroup.check(R.id.radioDay);
                if (BuildConfig.DEBUG) Log.d(TAG, "updateRadioGroup nightMode = off");
                break;
            case UiModeManager.MODE_NIGHT_YES:
                radioGroup.check(R.id.radioNight);
                if (BuildConfig.DEBUG) Log.d(TAG, "updateRadioGroup nightMode = on");
                break;
            case UiModeManager.MODE_NIGHT_AUTO:
                radioGroup.check(R.id.radioAuto);
                if (BuildConfig.DEBUG) Log.d(TAG, "updateRadioGroup nightMode = auto");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioDay:
                if (BuildConfig.DEBUG) Log.d(TAG, "onCheckedChanged set nightMode = off");
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                break;
            case R.id.radioNight:
                if (BuildConfig.DEBUG) Log.d(TAG, "onCheckedChanged set nightMode = on");
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                break;
            case R.id.radioAuto:
                if (BuildConfig.DEBUG) Log.d(TAG, "onCheckedChanged set nightMode = auto");
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_AUTO);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (BuildConfig.DEBUG)
            Log.d(TAG, "onConfigurationChanged nightMode = " +
                    ((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK)
                            == Configuration.UI_MODE_NIGHT_YES));
        updateRadioGroup();
        recreate();
    }
}
