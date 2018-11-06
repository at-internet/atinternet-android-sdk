package com.atinternet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setOfflineMode(Tracker.OfflineMode.required, null, true);
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);
        tracker.Context().setLevel2(4);
        tracker.Orders().add("orderID", 67);
        tracker.dispatch();

        tracker.Screens().add(this).setLevel2(0).sendView();


        tracker.Screens().add(this).sendView();
        tracker.Context().setLevel2(-1);
        tracker.dispatch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List l = new ArrayList();

        l.get(0);
    }
}
