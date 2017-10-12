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
        if (overlayPermission()) {
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
                    Debugger.create(this, tracker);
                }
            }
        }
    }
}
