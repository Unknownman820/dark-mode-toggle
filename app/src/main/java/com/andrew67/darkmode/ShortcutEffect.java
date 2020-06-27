package com.andrew67.darkmode;

import android.app.UiModeManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Invisible activity which performs a shortcut action (set/toggle dark mode) then finishes
 */
public class ShortcutEffect extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = "toggle";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startActivity(new Intent(this, LandingPage.class));
        } else {
            final Uri data = getIntent().getData();
            if (data != null && data.getLastPathSegment() != null) action = data.getLastPathSegment();

            final UiModeManager uiModeManager =
                    ContextCompat.getSystemService(this, UiModeManager.class);

            if (uiModeManager != null) {
                switch (action) {
                    case "light":
                        UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_NO);
                        break;
                    case "dark":
                        UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_YES);
                        break;
                    case "toggle":
                    default:
                        if (UiModeManagerUtil.getNightMode(uiModeManager) == UiModeManager.MODE_NIGHT_YES) {
                            UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_NO);
                        } else {
                            UiModeManagerUtil.setNightMode(uiModeManager, UiModeManager.MODE_NIGHT_YES);
                        }
                        break;
                }
            } else {
                Toast.makeText(this, R.string.toggle_error, Toast.LENGTH_LONG).show();
            }
        }

        finish();
    }

}
