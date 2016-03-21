package com.atinternet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.atinternet.tracker.SetConfigCallback;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstScreenActivity extends Activity implements View.OnClickListener {

    ScrollView mainView;
    Button sendHit;
    Button changeSite;
    Button changeOfflineMode;
    Button changeId;
    Button changeHash;
    Button changeCrashDetection;
    Button changeSessionBGDuration;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tracker = new Tracker(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        tracker.setListener(tracker.createDefaultTrackerListener());
        mainView = (ScrollView) findViewById(R.id.scrollView);
        tracker.setConfig(new HashMap<String, Object>() {{
            put(TrackerConfigurationKeys.LOG, "logp");
            put(TrackerConfigurationKeys.LOG_SSL, "logs");
            put(TrackerConfigurationKeys.SITE, "552987");
        }}, false, new SetConfigCallback() {
            @Override
            public void setConfigEnd() {
                mainView.setVisibility(View.VISIBLE);
            }
        });
        sendHit = (Button) findViewById(R.id.sendHit);
        changeSite = (Button) findViewById(R.id.changeSite);
        changeOfflineMode = (Button) findViewById(R.id.changeOfflineMode);
        changeId = (Button) findViewById(R.id.changeId);
        changeHash = (Button) findViewById(R.id.changeHash);
        changeCrashDetection = (Button) findViewById(R.id.changeCrashDetection);
        changeSessionBGDuration = (Button) findViewById(R.id.changeSessionBGDuration);

        sendHit.setOnClickListener(FirstScreenActivity.this);
        changeSite.setOnClickListener(FirstScreenActivity.this);
        changeOfflineMode.setOnClickListener(FirstScreenActivity.this);
        changeId.setOnClickListener(FirstScreenActivity.this);
        changeHash.setOnClickListener(FirstScreenActivity.this);
        changeCrashDetection.setOnClickListener(FirstScreenActivity.this);
        changeSessionBGDuration.setOnClickListener(FirstScreenActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendHit:
                tracker.Screens().add("TestWithClick").sendView();
                break;
            case R.id.changeSite:
                tracker.setSiteId(486136, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.Screens().add("New site id").sendView();
                        tracker.setSiteId(552987, new SetConfigCallback() {
                            @Override
                            public void setConfigEnd() {
                                tracker.Screens().add("New site id").sendView();
                            }
                        });
                    }
                });
                break;
            case R.id.changeOfflineMode:
                tracker.setOfflineMode(Tracker.OfflineMode.always, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.Screens().add("Mode Always").sendView();
                        tracker.setOfflineMode(Tracker.OfflineMode.required, new SetConfigCallback() {
                            @Override
                            public void setConfigEnd() {
                                tracker.Screens().add("Mode Required").sendView();
                            }
                        });
                    }
                });
                break;
            case R.id.changeId:
                tracker.setIdentifierType(Tracker.IdentifierType.advertisingId, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.Screens().add("AdvertisingId").sendView();
                        tracker.setIdentifierType(Tracker.IdentifierType.androidId, new SetConfigCallback() {
                            @Override
                            public void setConfigEnd() {
                                tracker.Screens().add("AndroidId").sendView();

                            }
                        });
                    }
                });
                break;
            case R.id.changeHash:
                tracker.setHashUserIdEnabled(true, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.Screens().add("hash user id enable").sendView();
                    }
                });
                break;
            case R.id.changeCrashDetection:
                tracker.setCrashDetectionEnabled(false, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        // WARNING : THiS CODE CRASH
                        FirstScreenActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList e = null;
                                e.get(0);
                            }
                        });
                    }
                });
                break;
            case R.id.changeSessionBGDuration:
                tracker.setSessionBackgroundDuration(5, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.Screens().add("new session duration").sendView();
                    }
                });
                break;
            case R.id.goToSecondScreenId:
                startActivity(new Intent(FirstScreenActivity.this, SecondScreenActivity.class));
                break;
            default:
                break;
        }
    }
}
