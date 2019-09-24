package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setConfig(new HashMap<String, Object>() {{
            put("logSSL", "logs");
            put("log", "logp");
            put("domain", "xiti.com");
            put("pixelPath", "/hit.xiti");
            put("secure", false);
            put("site", 552987);
        }}, null, true);
        tracker.ECommerce().setAutoSalesTrackerEnabled(true, null, true);
        //tracker.ECommerce().setCollectDomain("logp", null, true);
        tracker.setOfflineMode(Tracker.OfflineMode.required, null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                tracker.ECommerce().TransactionConfirmations().add("test");
                tracker.ECommerce().DisplayPageProducts().add(tracker.Screens().add("truc").setLevel2(4));
                tracker.dispatch();
                // add action
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
