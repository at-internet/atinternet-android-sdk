package com.atinternet;

import android.app.Activity;
import android.os.Bundle;

import com.atinternet.tracker.Tracker;

public class SecondScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Tracker mySpecificTracker = new Tracker(this);

        mySpecificTracker.Screens().add("SecondScreen").sendView();
    }
}
