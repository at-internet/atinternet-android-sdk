package com.atinternet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Debugger;
import com.atinternet.tracker.Tracker;

public class MainActivity extends AppCompatActivity {

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setSiteId(410501, null, true);
        tracker.setLog("logdev", null, true);

        findViewById(R.id.sendHit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.Screens().add("HomeTest").sendView();
            }
        });

        Debugger.create(this, tracker);

    }
}
