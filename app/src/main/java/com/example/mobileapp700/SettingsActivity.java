package com.example.mobileapp700;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchExpiry;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This displays the settings screen
        setContentView(R.layout.activity_settings);

        // This gets the expiry alert switch
        switchExpiry = findViewById(R.id.switchExpiry);

        // This gets the saved settings
        preferences = getSharedPreferences(
                "Settings",
                MODE_PRIVATE
        );

        // This loads the saved expiry alert setting
        boolean expiryAlerts =
                preferences.getBoolean(
                        "expiryAlerts",
                        false
                );

        switchExpiry.setChecked(expiryAlerts);

        // This saves the setting when the switch changes
        switchExpiry.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView,
                            boolean isChecked
                    ) {

                        SharedPreferences.Editor editor =
                                preferences.edit();

                        editor.putBoolean(
                                "expiryAlerts",
                                isChecked
                        );

                        editor.apply();
                    }
                });
    }
}