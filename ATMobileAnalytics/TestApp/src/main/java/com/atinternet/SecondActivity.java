package com.atinternet;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        findViewById(R.id.firstAction).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                // add action
                break;
        }
    }
}
