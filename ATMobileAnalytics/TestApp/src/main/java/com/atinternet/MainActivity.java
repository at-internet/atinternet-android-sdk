package com.atinternet;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOKEN = "test";
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sendHit).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setOfflineMode(Tracker.OfflineMode.required, null, true);
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendHit:
                String s = "Click";
                tracker.Gestures().add(s).sendTouch();
                break;
            case R.id.optOut:
                ATInternet.optOut(this, !ATInternet.optOutEnabled(this));
                break;
        }
    }
}
