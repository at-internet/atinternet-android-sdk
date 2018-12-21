/*
This SDK is licensed under the MIT license (MIT)
Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.atinternet.tracker.ecommerce;

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayProduct extends Event {

    private java.util.List<ECommerceProduct> products;
    private Tracker tracker;

    DisplayProduct(Tracker tracker) {
        super("product.display");
        products = new ArrayList<>();
        this.tracker = tracker;
    }

    public java.util.List<ECommerceProduct> Products() {
        return products;
    }

    @Override
    protected Map<String, Object> getData() {
        return data;
    }

    @Override
    protected java.util.List<Event> getAdditionalEvents() {
        List<Event> generatedEvents = super.getAdditionalEvents();

        for (ECommerceProduct p : products) {
            /// SALES INSIGHTS
            DisplayProduct dp = new DisplayProduct(tracker);
            dp.data.put("product", p.getAll());
            generatedEvents.add(dp);
        }

        if (Utility.parseBooleanFromString(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.AUTO_SALES_TRACKER)))) {
            for (ECommerceProduct p : products) {
                /// SALES TRACKER
                String stProductId;
                Object name = p.get("s:name");
                if (name != null) {
                    stProductId = String.format("%s[%s]", String.valueOf(p.get("s:id")), String.valueOf(name));
                } else {
                    stProductId = String.valueOf(p.get("s:id"));
                }
                com.atinternet.tracker.Product stProduct = tracker.Products().add(stProductId);

                Object stCategory = p.get("s:category1");
                if (stCategory != null) {
                    stProduct.setCategory1(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category2");
                if (stCategory != null) {
                    stProduct.setCategory2(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category3");
                if (stCategory != null) {
                    stProduct.setCategory3(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category4");
                if (stCategory != null) {
                    stProduct.setCategory4(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category5");
                if (stCategory != null) {
                    stProduct.setCategory5(String.format("[%s]", String.valueOf(stCategory)));
                }
                stCategory = p.get("s:category6");
                if (stCategory != null) {
                    stProduct.setCategory6(String.format("[%s]", String.valueOf(stCategory)));
                }
            }
            tracker.Products().sendViews();
        }


        return generatedEvents;
    }
}
