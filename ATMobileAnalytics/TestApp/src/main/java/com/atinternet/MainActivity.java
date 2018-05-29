package com.atinternet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.ParamOption;
import com.atinternet.tracker.Tracker;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOKEN = "test";
    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sendHit).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);

        CheckBox optOut = findViewById(R.id.optOut);
        optOut.setChecked(Tracker.optOutEnabled());
        optOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendHit:
                String s = "Click";
                tracker.Gestures().add(s).sendTouch();
                break;
            case R.id.optOut:
                Tracker.optOut(((CheckBox) v).isChecked());
                break;
        }
    }
}
