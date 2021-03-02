package com.atinternet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Privacy;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;
    private final HashMap<String, Object> config = new HashMap<String, Object>() {{
        put("logSSL", "logs");
        put("domain", "xiti.com");
        put("identifier", "uuid");
        put("site", 999999);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.setVisitorOptOut).setOnClickListener(this);
        findViewById(R.id.setVisitorOptIn).setOnClickListener(this);
        findViewById(R.id.setVisitorExempt).setOnClickListener(this);
        findViewById(R.id.setVisitorNoConsent).setOnClickListener(this);
        findViewById(R.id.setVisitorNone).setOnClickListener(this);
        findViewById(R.id.setVisitorCustom1).setOnClickListener(this);
        findViewById(R.id.setVisitorCustom2).setOnClickListener(this);
        findViewById(R.id.sendHit).setOnClickListener(this);
        findViewById(R.id.sendHitPage).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        Privacy.extendIncludeStorageForVisitorMode("OptOut", Privacy.StorageFeature.Lifecycle, Privacy.StorageFeature.Crash);
        Privacy.extendIncludeStorageForVisitorMode("custom1", Privacy.StorageFeature.Lifecycle, Privacy.StorageFeature.Crash);

        Privacy.extendIncludeBufferForVisitorMode("custom1", "p", "vtag");
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
            case R.id.setVisitorCustom1:
                Privacy.setVisitorMode("custom1", true, null);
                break;
            case R.id.setVisitorCustom2:
                Privacy.setVisitorMode("custom2", false, "pas-consent");
                break;
            case R.id.sendHit:
                tracker = ATInternet.getInstance().getTracker("test", config).setDefaultListener();
                tracker.CustomObjects().add(new HashMap<String, Object>() {{
                    put("test", "12");
                    put("test6", "2");
                }});
                tracker.AVInsights().Media().playbackStart(0, null);
                break;
            case R.id.sendHitPage:
                tracker = ATInternet.getInstance().getTracker("test", config).setDefaultListener();
                tracker.Screens().add("homepage");
                tracker.IdentifiedVisitor().set("test", 45);
                tracker.Campaigns().add("camp");
                tracker.setProps(new HashMap<String, String>() {{
                    put("n:contentId", "1234");
                    put("ressort", "politics");
                }}, false);
                tracker.Screens().add("test_privacy").sendView();
                break;
            case R.id.goToSecondScreen:
                List l = null;
                l.get(0);
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
        SharedPreferences prefs = getSharedPreferences("ATPreferencesKey", MODE_PRIVATE);
        Log.d("Message:", prefs.getAll().toString());
    }
}