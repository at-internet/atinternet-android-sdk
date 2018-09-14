package com.atinternet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.MediaPlayer;
import com.atinternet.tracker.MediaPlayers;
import com.atinternet.tracker.RichMedia;
import com.atinternet.tracker.Tracker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TOKEN = "test";
    Tracker tracker;
    RichMedia media;
    MediaPlayers players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.sendPlayWithoutRefresh).setOnClickListener(this);
        findViewById(R.id.sendPlay).setOnClickListener(this);
        findViewById(R.id.sendPause).setOnClickListener(this);
        findViewById(R.id.sendResume).setOnClickListener(this);
        findViewById(R.id.sendMove).setOnClickListener(this);
        findViewById(R.id.sendShare).setOnClickListener(this);
        findViewById(R.id.sendEmail).setOnClickListener(this);
        findViewById(R.id.sendFavor).setOnClickListener(this);
        findViewById(R.id.sendDownload).setOnClickListener(this);
        findViewById(R.id.sendInfo).setOnClickListener(this);
        findViewById(R.id.sendStop).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setOfflineMode(Tracker.OfflineMode.required, null, true);
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);
        players = tracker.Players();
        MediaPlayer player = players.add();
        media = player.Media().add("Media", "animation", 56).setLinkedContent("test");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendPlayWithoutRefresh:
                media.sendPlayWithoutRefresh();
                media.sendPlayWithoutRefresh(false);
                media.sendPlayWithoutRefresh(true);
                break;
            case R.id.sendPlay:
                media.sendPlay();
                break;
            case R.id.sendPause:
                media.sendPause();
                break;
            case R.id.sendResume:
                media.sendResume();
                break;
            case R.id.sendMove:
                media.sendMove();
                break;
            case R.id.sendShare:
                media.sendShare();
                break;
            case R.id.sendEmail:
                media.sendEmail();
                break;
            case R.id.sendFavor:
                media.sendFavor();
                break;
            case R.id.sendDownload:
                media.sendDownload();
                break;
            case R.id.sendInfo:
                media.sendInfo();
                media.sendInfo(false);
                media.sendInfo(true);
                break;
            case R.id.sendStop:
                players.removeAll();
                //media.sendStop();
                break;
        }
    }
}
