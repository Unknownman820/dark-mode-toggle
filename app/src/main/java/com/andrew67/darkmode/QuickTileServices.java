package com.andrew67.darkmode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.UiModeManager;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

@TargetApi(Build.VERSION_CODES.N)
public class QuickTileServices extends TileService {
    private static final String TAG = "DarkModeQuickTile";
    private Toast errorToast = null;

    @Override
    public void onClick() {
        super.onClick();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            final Intent landingPageIntent = new Intent(this, LandingPage.class);
            landingPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityAndCollapse(landingPageIntent);
        } else {
            try {
                toggleSetting();
                syncTile();
            } catch (Exception e) {
                displayErrorToast();
            }
        }
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        try {
            syncTile();
        } catch (Exception e) {
            displayErrorToast();
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        if (BuildConfig.DEBUG) Log.d(TAG, "onStartListening");
        try {
            syncTile();
        } catch (Exception e) {
            displayErrorToast();
        }
    }

    private boolean isNightModeOn(@NonNull UiModeManager uiModeManager) {
        final boolean isNightMode = uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
        if (BuildConfig.DEBUG) Log.d(TAG, "isNightModeOn " + isNightMode);
        return isNightMode;
    }

    private void toggleSetting() {
        final UiModeManager uiModeManager = getSystemService(UiModeManager.class);
        if (uiModeManager != null) {
            if (isNightModeOn(uiModeManager)) {
                //switch off the night mode
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                if (BuildConfig.DEBUG) Log.d(TAG, "toggleSetting set nightMode = off");
            } else {
                // enable night mode
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                if (BuildConfig.DEBUG) Log.d(TAG, "toggleSetting set nightMode = on");
            }
        } else {
            throw new NullPointerException("UiModeManager is null");
        }
    }

    private void syncTile() {
        final UiModeManager uiModeManager = getSystemService(UiModeManager.class);
        if (uiModeManager != null) {
            final Tile tile = getQsTile();
            tile.setState(isNightModeOn(uiModeManager) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.updateTile();
            if (BuildConfig.DEBUG) Log.d(TAG, "syncTile");
        } else {
            throw new NullPointerException("UiModeManager is null");
        }
    }

    @SuppressLint("ShowToast")
    private void displayErrorToast() {
        if (errorToast == null) {
            errorToast = Toast.makeText(this, R.string.tile_error, Toast.LENGTH_LONG);
        }
        errorToast.show();
    }

}
