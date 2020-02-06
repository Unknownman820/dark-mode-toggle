package io.github.subhamtyagi.nightmode;

import android.app.UiModeManager;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LandingPage extends AppCompatActivity {

    RadioGroup radioGroup;
    UiModeManager uiModeManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radioGroup);
        uiModeManager = ContextCompat.getSystemService(this, UiModeManager.class);

        switch (uiModeManager.getNightMode()) {
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

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radioDay:
                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                    break;
                case R.id.radioNight:
                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                    break;
                case R.id.radioAuto:
                    uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_AUTO);
                    break;
            }
        });
    }
}
