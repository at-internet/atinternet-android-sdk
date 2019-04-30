package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.ecommerce.CartAwaitingPayment;
import com.atinternet.tracker.ecommerce.ProductAwaitingPayment;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.firstAction).setOnClickListener(this);
        findViewById(R.id.goToSecondScreen).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);
        tracker.ECommerce().setCollectDomain("collect-euw1", null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                // add action
                CartAwaitingPayment cap = tracker.ECommerce().CartAwaitingPayments().add();
                cap.Cart()
                        .set("id", "test")
                        .set("currency", "test")
                        .set("turnovertaxincluded", 54.9);
                cap.Transaction()
                        .set("promocode", new ArrayList<>(Arrays.asList("xyz", "abc")))
                        .set("firstpurchase", true);
                ProductAwaitingPayment pap = tracker.ECommerce().ProductAwaitingPayments().add();
                pap.Cart().set("id", "test");
                pap.Product()
                        .set("id", "toto")
                        .set("$", "name");
                tracker.Events().send();
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
