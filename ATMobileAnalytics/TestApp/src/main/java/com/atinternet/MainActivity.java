package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.avinsights.Media;
import com.atinternet.tracker.ecommerce.CartAwaitingPayment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;
    private HashMap config = new HashMap<String, Object>() {{
        put("logSSL", "logs");
        put("log", "logp");
        put("domain", "xiti.com");
        put("pixelPath", "/hit.xiti");
        //put("identifier", "advertisingId");
        put("site", 552987);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setConfig(config, null, true);
        tracker.setDefaultListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                CartAwaitingPayment cp = tracker.ECommerce().CartAwaitingPayments().add();
                cp.Cart().set("TEST", "totot");

                tracker.Events().add("evENnt", new HashMap<String, Object>() {{
                    put("My_Prop", true);
                }});

                tracker.setProp("PROp_1", "test", false);
                tracker.setProps(new HashMap<String, String>() {{
                    put("dic_PROP", "45");
                    put("dFc_PROP", "33");
                }}, false);
                tracker.dispatch();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}