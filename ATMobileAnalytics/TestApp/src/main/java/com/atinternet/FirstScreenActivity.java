package com.atinternet;

import android.app.Activity;
import android.os.Bundle;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.SetConfigCallback;
import com.atinternet.tracker.Tracker;

import java.util.HashMap;

public class FirstScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Tracker mySpecificTracker = ((ATInternet) getApplication()).getDefaultTracker();
        mySpecificTracker.Screens().add(FirstScreenActivity.this).sendView();

    }
}
