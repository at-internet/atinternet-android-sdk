package com.atinternet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Debugger;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tracker = ATInternet.getInstance().getTracker("IntegrationTracker", new HashMap<String, Object>() {{
            put(TrackerConfigurationKeys.LOG, "logp");
            put(TrackerConfigurationKeys.LOG_SSL, "logs");
            put(TrackerConfigurationKeys.SITE, 552987);
        }});

        findViewById(R.id.sendHit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.Screens().add("HomeTest").sendView();
            }
        });

        Debugger.create(this, tracker);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ATInternet.ALLOW_OVERLAY_INTENT_RESULT_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Debugger.create(this, tracker);
                }
            }
        }
    }
}
