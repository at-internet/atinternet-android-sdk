package com.atinternet;

import android.app.Activity;
import android.os.Bundle;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

public class FirstScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Tracker mySpecificTracker = ((ATInternet) getApplication()).getDefaultTracker();

        mySpecificTracker.Screens().add(this).sendView();

    }
}
