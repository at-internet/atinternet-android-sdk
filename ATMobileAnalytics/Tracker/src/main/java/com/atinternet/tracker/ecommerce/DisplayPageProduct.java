/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker.ecommerce;

import android.text.TextUtils;

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.List;
import java.util.Map;

public class DisplayPageProduct extends Event {

    private ECommerceProduct product;
    private Tracker tracker;
    private String screenLabel;
    private Screen screen;

    DisplayPageProduct(Tracker tracker) {
        super("product.page_display");
        product = new ECommerceProduct();
        this.tracker = tracker;
    }

    DisplayPageProduct setScreenLabel(String screenLabel) {
        this.screenLabel = screenLabel;
        return this;
    }

    DisplayPageProduct setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public ECommerceProduct Product() {
        return product;
    }

    @Override
    protected Map<String, Object> getData() {
        if (!product.isEmpty()) {
            data.put("product", product.getAll());
        }
        return super.getData();
    }

    @Override
    protected List<Event> getAdditionalEvents() {
        if (Utility.parseBooleanFromString(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.AUTO_SALES_TRACKER)))) {
            /// SALES TRACKER
            String stProductId;
            Object name = product.get("s:$");
            if (name != null) {
                stProductId = String.format("%s[%s]", String.valueOf(product.get("s:id")), String.valueOf(name));
            } else {
                stProductId = String.valueOf(product.get("s:id"));
            }
            com.atinternet.tracker.Product stProduct = tracker.Products().add(stProductId);

            Object stCategory = product.get("s:category1");
            if (stCategory != null) {
                stProduct.setCategory1(String.format("[%s]", String.valueOf(stCategory)));
            }
            stCategory = product.get("s:category2");
            if (stCategory != null) {
                stProduct.setCategory2(String.format("[%s]", String.valueOf(stCategory)));
            }
            stCategory = product.get("s:category3");
            if (stCategory != null) {
                stProduct.setCategory3(String.format("[%s]", String.valueOf(stCategory)));
            }
            stCategory = product.get("s:category4");
            if (stCategory != null) {
                stProduct.setCategory4(String.format("[%s]", String.valueOf(stCategory)));
            }
            stCategory = product.get("s:category5");
            if (stCategory != null) {
                stProduct.setCategory5(String.format("[%s]", String.valueOf(stCategory)));
            }
            stCategory = product.get("s:category6");
            if (stCategory != null) {
                stProduct.setCategory6(String.format("[%s]", String.valueOf(stCategory)));
            }
            if (screen == null) {
                if (!TextUtils.isEmpty(screenLabel)) {
                    tracker.setParam("p", screenLabel);
                }
            } else {
                if (!TextUtils.isEmpty(screen.getCompleteLabel())) {
                    tracker.setParam("p", screen.getCompleteLabel());
                }
                if (screen.getLevel2() > 0) {
                    tracker.setParam("s2", screen.getLevel2());
                }
            }
            stProduct.sendView();
        }

        return super.getAdditionalEvents();
    }
}
