package com.atinternet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.MvTesting;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.ecommerce.AddProduct;
import com.atinternet.tracker.ecommerce.DeliveryCheckout;
import com.atinternet.tracker.ecommerce.DisplayCart;
import com.atinternet.tracker.ecommerce.DisplayPageProduct;
import com.atinternet.tracker.ecommerce.DisplayProduct;
import com.atinternet.tracker.ecommerce.PaymentCheckout;
import com.atinternet.tracker.ecommerce.RemoveProduct;
import com.atinternet.tracker.ecommerce.TransactionConfirmation;
import com.atinternet.tracker.ecommerce.UpdateCart;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.custom).setOnClickListener(this);
        findViewById(R.id.crash).setOnClickListener(this);
        findViewById(R.id.mvTesting).setOnClickListener(this);
        findViewById(R.id.displayProduct).setOnClickListener(this);
        findViewById(R.id.displayPageProduct).setOnClickListener(this);
        findViewById(R.id.addProduct).setOnClickListener(this);
        findViewById(R.id.addProductAndCartCreation).setOnClickListener(this);
        findViewById(R.id.removeProduct).setOnClickListener(this);
        findViewById(R.id.displayCart).setOnClickListener(this);
        findViewById(R.id.updateCart).setOnClickListener(this);
        findViewById(R.id.deliveryCheckout).setOnClickListener(this);
        findViewById(R.id.paymentCheckout).setOnClickListener(this);
        findViewById(R.id.transactionConfirmation).setOnClickListener(this);

        tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.setDefaultListener();
        tracker.setSiteId(552987, null, true);
        tracker.setLog("logp", null, true);
        tracker.setSecuredLog("logs", null, true);

        Log.d("ATTestApp", tracker.getCrashInformation().toString());
        Log.d("ATTestApp", tracker.getCrashInformation().toString());
        tracker.ECommerce().setAutoSalesTrackerEnabled(true, null, true);
        tracker.ECommerce().setCollectDomain("collect-euw1", null, true);
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> productData1 = new HashMap<>();
        productData1.put("id", "1");
        productData1.put("key", "value");

        Map<String, Object> productData2 = new HashMap<>();
        productData2.put("id", "2");

        Map<String, Object> productData3 = new HashMap<>();
        productData3.put("id", "3");
        productData3.put("brand", "adadas");
        productData3.put("key", "value");

        switch (v.getId()) {
            case R.id.crash:
                List l = new ArrayList();
                l.get(0);
                break;
            case R.id.mvTesting:
                MvTesting mvt = tracker.MvTestings().add("1[My_first_Test]", 1, "2[Version2_page_subscription]");
                mvt.Variables().add("1[Header]", "2[Grey]");
                mvt.Variables().add("2[Header]", "4[Black]");
                mvt.send();
                break;
            case R.id.custom:
                tracker.Events().add("pageLoad", new HashMap<String, Object>() {{
                    put("s:name", "pageName");
                    put("s:chapter1", "chap1");
                    put("s:chapter2", "chap2");
                    put("s:chapter3", "chap3");
                    put("n:level2", 123);
                    put("j:customObject", new HashMap<String, Object>() {{
                        put("param1", "val1");
                        put("param2", "val2");
                    }});
                }});
                tracker.dispatch();
                break;
            case R.id.displayPageProduct:
                DisplayPageProduct dpp = tracker.ECommerce().DisplayPageProducts().add();
                dpp.Product().setAll(new HashMap<String, Object>() {{
                    put("id", "7");
                    put("variant", "1");
                    put("name", "Robe en mousseline imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 1);
                    put("priceTaxIncluded", 19.68);
                    put("priceTaxFree", 16.4);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes d'été");
                    put("category5", "Robes d'été");
                    put("category6", "Robes d'été");
                }});
                tracker.dispatch();
                break;
            case R.id.displayProduct:
                DisplayProduct dp = tracker.ECommerce().DisplayProducts().add();
                dp.Products().add(new ECommerceProduct(new HashMap<String, Object>() {{
                    put("id", "3");
                    put("variant", "1");
                    put("name", "Robe imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 0);
                    put("priceTaxIncluded", 31.2);
                    put("priceTaxFree", 26);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes décontractées");
                    put("category5", "Robes décontractées");
                    put("category6", "Robes décontractées");
                }}));
                dp.Products().add(new ECommerceProduct(new HashMap<String, Object>() {{
                    put("id", "2");
                    put("variant", "2");
                    put("name", "Robe imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 0);
                    put("priceTaxIncluded", 31.2);
                    put("priceTaxFree", 26);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes décontractées");
                    put("category5", "Robes décontractées");
                    put("category6", "Robes décontractées");
                }}));
                tracker.dispatch();
                break;
            case R.id.addProductAndCartCreation:
                AddProduct apWcc = tracker.ECommerce().AddProducts().add();
                apWcc.Cart().set("id", "34");
                apWcc.Product().setAll(new HashMap<String, Object>() {{
                    put("id", "3");
                    put("variant", "1");
                    put("name", "Robe imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 0);
                    put("priceTaxIncluded", 31.2);
                    put("priceTaxFree", 26);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("quantity", 1);
                    put("cartcreation", true);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes décontractées");
                    put("category5", "Robes décontractées");
                    put("category6", "Robes décontractées");
                }});
                tracker.dispatch();
            case R.id.addProduct:
                AddProduct ap = tracker.ECommerce().AddProducts().add();
                ap.Cart().set("id", "34");
                ap.Product().setAll(new HashMap<String, Object>() {{
                    put("id", "3");
                    put("variant", "1");
                    put("name", "Robe imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 0);
                    put("priceTaxIncluded", 31.2);
                    put("priceTaxFree", 26);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("quantity", 1);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes décontractées");
                    put("category5", "Robes décontractées");
                    put("category6", "Robes décontractées");
                }});
                tracker.dispatch();
                break;
            case R.id.removeProduct:
                RemoveProduct rp = tracker.ECommerce().RemoveProducts().add();
                rp.Cart().set("id", "34");
                rp.Product().setAll(new HashMap<String, Object>() {{
                    put("id", "4");
                    put("variant", "1");
                    put("name", "Robe imprimée");
                    put("brand", "Fashion Manufacturer");
                    put("discount", 0);
                    put("priceTaxIncluded", 61.19);
                    put("priceTaxFree", 50.99);
                    put("currency", "EUR");
                    put("stock", 1);
                    put("quantity", 1);
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Robes");
                    put("category4", "Robes de soirée");
                    put("category5", "Robes de soirée");
                    put("category6", "Robes de soirée");
                }});
                tracker.dispatch();
                break;
            case R.id.displayCart:
                DisplayCart dc = tracker.ECommerce().DisplayCarts().add();
                dc.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "53");
                    put("turnoverTaxFree", 456.2);
                    put("turnoverTaxIncluded", 549);
                    put("nbDistinctProduct", 1);
                    put("quantity", 1);
                    put("currency", "EUR");
                }});
                dc.Products().add(new ECommerceProduct(productData1));
                dc.Products().add(new ECommerceProduct(productData2));
                dc.Products().add(new ECommerceProduct(productData3));

                tracker.dispatch();
                break;
            case R.id.updateCart:
                UpdateCart uc = tracker.ECommerce().UpdateCarts().add();
                uc.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "34");
                    put("turnoverTaxFree", 52);
                    put("turnoverTaxIncluded", 62.4);
                    put("nbDistinctProduct", 1);
                    put("quantity", 2);
                    put("currency", "EUR");
                }});
                tracker.dispatch();
                break;
            case R.id.deliveryCheckout:
                DeliveryCheckout dch = tracker.ECommerce().DeliveryCheckouts().add();
                dch.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "34");
                    put("turnoverTaxFree", 34);
                    put("turnoverTaxIncluded", 40.8);
                    put("nbDistinctProduct", 1);
                    put("quantity", 1);
                    put("currency", "EUR");
                }});
                dch.Shipping().setAll(new HashMap<String, Object>() {{
                    put("costTaxFree", 7);
                    put("costTaxIncluded", 8.4);
                    put("delivery", "My carrier");
                }});
                tracker.dispatch();
                break;
            case R.id.paymentCheckout:
                PaymentCheckout pc = tracker.ECommerce().PaymentCheckouts().add();
                pc.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "34");
                    put("turnoverTaxFree", 34);
                    put("turnoverTaxIncluded", 40.8);
                    put("nbDistinctProduct", 1);
                    put("quantity", 1);
                    put("currency", "EUR");
                }});
                pc.Shipping().setAll(new HashMap<String, Object>() {{
                    put("costTaxFree", 7);
                    put("costTaxIncluded", 8.4);
                    put("delivery", "My carrier");
                }});
                tracker.dispatch();
                break;
            case R.id.transactionConfirmation:
                TransactionConfirmation tc = tracker.ECommerce().TransactionConfirmations().add(null);
                tc.PromotionalCodes().add("DQQYRZSJ");
                tc.PromotionalCodes().add("UN1ENE27");
                tc.Cart().setAll(new HashMap<String, Object>() {{
                    put("id", "34");
                    put("creation", 1514973161);
                    put("turnoverTaxFree", 34);
                    put("turnoverTaxIncluded", 40.8);
                    put("nbDistinctProduct", 1);
                    put("quantity", 1);
                    put("currency", "EUR");
                }});
                tc.Customer().set("new", 0);
                tc.Payment().set("mode", "Chèque");
                tc.Shipping().setAll(new HashMap<String, Object>() {{
                    put("costTaxFree", 7);
                    put("costTaxIncluded", 8.4);
                    put("delivery", "My carrier");
                }});
                tc.Transaction().set("id", "27");
                tc.Products().add(new ECommerceProduct(new HashMap<String, Object>() {{
                    put("id", "2");
                    put("name", "Chemisier");
                    put("variant", "1");
                    put("discount", "0");
                    put("stock", "1");
                    put("priceTaxFree", "27");
                    put("priceTaxIncluded", "32.4");
                    put("quantity", 1);
                    put("brand", "Fashion Manufacturer");
                    put("category1", "Accueil");
                    put("category2", "Femmes");
                    put("category3", "Tops");
                    put("category4", "Chemisiers");
                    put("category5", "Chemisiers");
                    put("category6", "Chemisiers");
                    put("currency", "EUR");
                }}));
                tracker.dispatch();
                break;
        }
    }
}
