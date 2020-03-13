package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.avinsights.Media;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;
    private HashMap config = new HashMap<String, Object>() {{
        put("logSSL", "logs");
        put("log", "logp");
        put("domain", "xiti.com");
        put("pixelPath", "/hit.xiti");
        //put("identifier", "advertisingId");
        put("site", 552987);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setConfig(config, null, true);
        tracker.setDefaultListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                Media m = tracker.AVInsights().Media();
                m.set("content", "test");
                m.set("content_id", 23);
                m.set("player", "play");
                m.set("player_id", 45);
                m.set("test", "yt");
                m.playbackStart(0, null);
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
