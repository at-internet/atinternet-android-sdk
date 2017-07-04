package com.atinternet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.AutoTracker;
import com.atinternet.tracker.AutoTrackerListener;
import com.atinternet.tracker.Debugger;
import com.atinternet.tracker.Gesture;
import com.atinternet.tracker.Screen;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AutoTrackerListener {

    private static final String TOKEN = "44586204-102b-41b4-8662-aa1809719145";
    AutoTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracker = ATInternet.getInstance().getDefaultTracker(TOKEN);
        tracker.setSiteId(410501, null, true);
        tracker.setLog("logdev", null, true);
        tracker.enableAutoTracking(true);

        findViewById(R.id.sendHit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracker.Screens().add("HomeTest").sendView();
            }
        });

        if (overlayPermission()) {
            tracker.enableLiveTagging(true);
            Debugger.create(this, tracker);
        }
    }

    private boolean overlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ATInternet.ALLOW_OVERLAY_INTENT_RESULT_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ATInternet.ALLOW_OVERLAY_INTENT_RESULT_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(this)) {
                    tracker.enableLiveTagging(true);
                }
            }
        }
    }

    @Override
    public Screen screenDetected(Screen screen) {
        return screen.setLevel2(2)
                .setName("NewName");
    }

    @Override
    public Gesture gestureDetected(Gesture gesture) {
        gesture.CustomObjects().add(new HashMap<String, Object>() {{
            put("Enrich", "Test");
        }});
        return gesture;
    }
}
