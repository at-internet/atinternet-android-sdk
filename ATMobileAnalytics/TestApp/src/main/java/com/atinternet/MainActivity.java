package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Privacy;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.setVisitorOptOut).setOnClickListener(this);
        findViewById(R.id.setVisitorOptIn).setOnClickListener(this);
        findViewById(R.id.setVisitorExempt).setOnClickListener(this);
        findViewById(R.id.setVisitorNoConsent).setOnClickListener(this);
        findViewById(R.id.setVisitorNone).setOnClickListener(this);
        findViewById(R.id.sendHit).setOnClickListener(this);
        findViewById(R.id.sendHitPage).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker()
                .setDefaultListener();
        tracker.setConfig(new HashMap<String, Object>() {{
            put("logSSL", "logs");
            put("log", "logp");
            put("domain", "xiti.com");
            put("pixelPath", "/hit.xiti");
            put("identifier", "uuid");
            put("site", 999999);
            put("UUIDDuration", 1);
        }}, null, true);
        tracker.setMaxHitSize(1500);
        tracker.setSendHitWhenOptOutEnabled(false, null, true);
        Privacy.extendIncludeBuffer("events_name", "events_data_av_duration", "events_data_av_p*", "stc_test6");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setVisitorOptOut:
                Privacy.setVisitorOptOut();
                break;
            case R.id.setVisitorOptIn:
                Privacy.setVisitorOptIn();
                break;
            case R.id.setVisitorExempt:
                Privacy.setVisitorMode(Privacy.VisitorMode.Exempt);
                break;
            case R.id.setVisitorNoConsent:
                Privacy.setVisitorMode(Privacy.VisitorMode.NoConsent);
                break;
            case R.id.setVisitorNone:
                Privacy.setVisitorMode(Privacy.VisitorMode.None);
                break;
            case R.id.sendHit:
                tracker.CustomObjects().add(new HashMap<String, Object>() {{
                    put("test", "12");
                    put("test6", "2");
                }});
                tracker.AVInsights().Media().playbackStart(0, null);
                break;
            case R.id.sendHitPage:
                tracker.CustomObjects().add(new HashMap<String, Object>() {{
                    put("test", "12");
                    put("test6", "2");
                }});
                tracker.Screens().add("test_privacy").sendView();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}