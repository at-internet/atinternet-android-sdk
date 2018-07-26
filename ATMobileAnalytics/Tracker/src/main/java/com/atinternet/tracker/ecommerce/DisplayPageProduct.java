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
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayPageProduct extends EcommerceEvent {

    private java.util.List<Product> products;
    private Tracker tracker;

    DisplayPageProduct(Tracker tracker, Screen screen) {
        super("product.page_display", screen);
        products = new ArrayList<>();
        this.tracker = tracker;
    }

    public java.util.List<Product> Products() {
        return products;
    }

    @Override
    protected Map<String, Object> getData() {
        return data;
    }

    @Override
    protected java.util.List<Event> getAdditionalEvents() {
        List<Event> generatedEvents = super.getAdditionalEvents();

        Map<String, Object> screenObj = parseScreenNameForEvent(screen);
        Map<String, Object> level2Obj = parseLevel2ForEvent(screen);

        for (Product p : products) {
            /// SALES INSIGHTS
            DisplayPageProduct dp = new DisplayPageProduct(tracker, screen);
            if (screenObj.size() != 0) {
                dp.data.put("page", screenObj);
            }
            if (level2Obj.size() != 0) {
                dp.data.put("level2", level2Obj);
            }
            dp.data.put("product", p);
            generatedEvents.add(dp);
        }

        if (Utility.parseBooleanFromString(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.AUTO_SALES_TRACKER)))) {
            for (Product p : products) {
                /// SALES TRACKER
                String stProductId;
                if (p.containsKey("s:name")) {
                    stProductId = String.format("%s[%s]", String.valueOf(p.get("s:id")), String.valueOf(p.get("s:name")));
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
