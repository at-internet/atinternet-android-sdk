package com.atinternet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.setOlderContext).setOnClickListener(this);
        findViewById(R.id.sendHit).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker()
                .setDefaultListener();
        tracker.setConfig(new HashMap<String, Object>() {{
            put("logSSL", "logs");
            put("log", "logp");
            put("domain", "xiti.com");
            put("pixelPath", "/hit.xiti");
            put("identifier", "uuid");
            put("site", 552987);
            put("UUIDDuration", 1);
        }}, null, true);
        tracker.setMaxHitSize(1500);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = getSharedPreferences("ATPreferencesKey", Context.MODE_PRIVATE);
        Map<String, ?> values;
        switch (v.getId()) {
            case R.id.setOlderContext:
                preferences.edit()
                        .clear()
                        .putString("ATIdclientUUID", "test")
                        .apply();
                break;
            case R.id.getPrefs:
                values = preferences.getAll();
                Log.d("ATINTERNET", "Debug stop");
                break;
            case R.id.sendHit:
                tracker.AVInsights().Media(3, 10).playbackStart(0, null);
                //tracker.Screens().add("Page").sendView();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}