package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;
    private HashMap config = new HashMap<String, Object>() {{
        put("logSSL", "logs");
        put("log", "logp");
        put("domain", "xiti.com");
        put("pixelPath", "/hit.xiti");
        put("secure", false);
        put("site", 552987);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.secondAction).setOnClickListener(this);
        findViewById(R.id.thirdAction).setOnClickListener(this);
        findViewById(R.id.fourthAction).setOnClickListener(this);
        findViewById(R.id.fifthAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setConfig(config, null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                /// global apvr
                ATInternet.getInstance().setApplicationVersion("globalapp");
                tracker.dispatch();
                tracker.Screens().add(this).sendView();
                // add action
                break;
            case R.id.secondAction:
                /// global apvr & global ua
                ATInternet.getInstance().setUserAgent("globalua");
                tracker.dispatch();
                break;
            case R.id.thirdAction:
                /// global apvr & global ua
                tracker = ATInternet.getInstance().getTracker("tracker", config);
                tracker.dispatch();
                break;
            case R.id.fourthAction:
                /// Normal
                ATInternet.getInstance().setUserAgent(null);
                ATInternet.getInstance().setApplicationVersion(null);
                tracker = new Tracker(config);
                tracker.dispatch();
                break;
            case R.id.fifthAction:
                /// Normal
                ATInternet.getInstance().setApplicationVersion("globalapp");
                ATInternet.getInstance().setUserAgent("globalua");
                tracker = new Tracker(config);
                ATInternet.getInstance().setUserAgent(null);
                ATInternet.getInstance().setApplicationVersion(null);
                tracker.dispatch();
                tracker.Screens().add(this).sendView();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
