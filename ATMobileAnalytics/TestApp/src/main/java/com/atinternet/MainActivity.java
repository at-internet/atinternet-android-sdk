package com.atinternet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.ecommerce.CartAwaitingPayment;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.ArrayList;
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
        tracker.ECommerce().setCollectDomain("logp", null, true);
        tracker.setOfflineMode(Tracker.OfflineMode.required, null, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstAction:
                CartAwaitingPayment cap = tracker.ECommerce().CartAwaitingPayments().add(tracker.Screens().add("prout").setLevel2(6));
                cap.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "34");
                    put("currency", "EUR");
                    put("turnovertaxfree", 463.2);
                    put("turnovertaxincluded", 557.4);
                    put("creation_utc", 1514973161);
                    put("quantity", 1);
                    put("nbdistinctproduct", 1);
                }});
                cap.Products().add(new ECommerceProduct(new HashMap<String, Object>() {{
                    put("id", "7");
                    put("variant", "1");
                    put("$", "laptop_A56");
                    put("brand", "ACER");
                    put("discount", 1);
                    put("pricetaxincluded", 549);
                    put("pricetaxfree", 456.2);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("quantity", 1);
                    put("category1", "Computers_and_Networking");
                    put("category2", "Computers");
                    put("category3", "Laptops");
                }}));
                cap.Shipping().setAll(new HashMap<String, Object>() {{
                    put("costtaxfree", 7);
                    put("costtaxincluded", 8.4);
                    put("delivery", "My carrier");
                }});
                cap.Payment().set("mode", "Credit card");
                cap.Transaction().setAll(new HashMap<String, Object>(){{
                    put("promocode", new ArrayList<String>() {{
                        add("DQQYRZSJ");
                        add("UN1ENE27");
                    }});
                    put("firstpurchase", 0);
                }});
                tracker.dispatch();
                // add action
                break;
            case R.id.goToSecondScreen:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
