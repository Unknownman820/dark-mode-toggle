package com.andrew67.darkmode;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.warn_carmode).setVisibility(View.GONE);
        }

        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            findViewById(R.id.info_oems).setVisibility(View.GONE);
        } else {
            findViewById(R.id.warn_disable).setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.toonew_title)
                    .setMessage(R.string.toonew_msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.toonew_action, (dialog, which) -> {
                        // Recipe from http://stackoverflow.com/a/32983128
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.setData(Uri.fromParts("package",
                                LandingPage.this.getPackageName(), null));
                        startActivity(i);
                        LandingPage.this.finish();
                    })
                    .show();
        }
    }

    private void updateRadioGroup() {
        switch (UiModeManagerUtil.getNightMode(uiModeManager)) {
            case UiModeManager.MODE_NIGHT_NO:
                radioGroup.check(R.id.radioDay);
                break;
            case UiModeManager.MODE_NIGHT_YES:
                radioGroup.check(R.id.radioNight);
                break;
            case UiModeManager.MODE_NIGHT_AUTO:
                radioGroup.check(R.id.radioAuto);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioDay:
                UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_NO);
                break;
            case R.id.radioNight:
                UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_YES);
                break;
            case R.id.radioAuto:
                UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_AUTO);
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
