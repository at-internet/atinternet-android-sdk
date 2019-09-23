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

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceCart;
import com.atinternet.tracker.ecommerce.objectproperties.ECommercePayment;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceShipping;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionConfirmation extends Event {

    private Tracker tracker;
    private String screenLabel;
    private Screen screen;

    private ECommerceCart cart;
    private ECommerceTransaction transaction;
    private ECommerceShipping shipping;
    private ECommercePayment payment;
    private List<ECommerceProduct> products;


    TransactionConfirmation(Tracker tracker) {
        super("transaction.confirmation");
        this.tracker = tracker;
        cart = new ECommerceCart();
        transaction = new ECommerceTransaction();
        shipping = new ECommerceShipping();
        payment = new ECommercePayment();
        products = new ArrayList<>();
    }

    TransactionConfirmation setScreenLabel(String screenLabel) {
        this.screenLabel = screenLabel;
        return this;
    }

    TransactionConfirmation setScreen(Screen screen) {
        this.screen = screen;
        return this;
    }

    public ECommerceCart Cart() {
        return cart;
    }

    public ECommerceTransaction Transaction() {
        return transaction;
    }

    public ECommerceShipping Shipping() {
        return shipping;
    }

    public ECommercePayment Payment() {
        return payment;
    }

    public List<ECommerceProduct> Products() {
        return products;
    }

    @Override
    protected Map<String, Object> getData() {
        if (!cart.isEmpty()) {
            data.put("cart", cart.getAll());
        }
        if (!payment.isEmpty()) {
            data.put("payment", payment.getAll());
        }
        if (!shipping.isEmpty()) {
            data.put("shipping", shipping.getAll());
        }
        if (!transaction.isEmpty()) {
            data.put("transaction", transaction.getAll());
        }
        return super.getData();
    }

    @Override
    protected List<Event> getAdditionalEvents() {
        /// SALES INSIGHTS
        List<Event> generatedEvents = super.getAdditionalEvents();

        for (ECommerceProduct p : products) {
            ProductPurchased pp = new ProductPurchased();
            pp.Cart().set("id", String.valueOf(cart.get("s:id")));
            pp.Transaction().set("id", String.valueOf(transaction.get("s:id")));
            if (!p.isEmpty()) {
                pp.Product().setAll(p.getAll());
            }
            generatedEvents.add(pp);
        }

        /// SALES TRACKER
        if (Utility.parseBooleanFromString(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.AUTO_SALES_TRACKER)))) {
            double turnoverTaxIncluded = Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnovertaxincluded")));
            double turnoverTaxFree = Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnovertaxfree")));

            List<String> promoCodes = new ArrayList<>();
            Object pc = transaction.get("a:s:promocode");
            if (pc instanceof List) {
                promoCodes.addAll((List<String>) pc);
            }
            String[] codes = new String[promoCodes.size()];
            promoCodes.toArray(codes);
            tracker.Orders().add(String.valueOf(transaction.get("s:id")), Utility.parseDoubleFromString(String.valueOf(cart.get("f:turnovertaxincluded"))))
                    .setStatus(3).setPaymentMethod(0).setConfirmationRequired(false).setNewCustomer(Utility.parseBooleanFromString(String.valueOf(transaction.get("b:firstpurchase"))))
                    .Delivery().set(Utility.parseDoubleFromString(String.valueOf(shipping.get("f:costtaxfree"))), Utility.parseDoubleFromString(String.valueOf(shipping.get("f:costtaxincluded"))), String.valueOf(shipping.get("s:delivery")))
                    .Amount().set(turnoverTaxFree, turnoverTaxIncluded, turnoverTaxIncluded - turnoverTaxFree)
                    .Discount().setPromotionalCode(Utility.stringJoin('|', codes));

            com.atinternet.tracker.Cart stCart = tracker.Cart().set(String.valueOf(cart.get("s:id")));
            for (ECommerceProduct p : products) {
                String stProductId;
                Object name = p.get("s:$");
                if (name != null) {
                    stProductId = String.format("%s[%s]", String.valueOf(p.get("s:id")), String.valueOf(name));
                } else {
                    stProductId = String.valueOf(p.get("s:id"));
                }

                com.atinternet.tracker.Product stProduct = stCart.Products().add(stProductId)
                        .setQuantity(Utility.parseIntFromString(String.valueOf(p.get("n:quantity"))))
                        .setUnitPriceTaxIncluded(Utility.parseDoubleFromString(String.valueOf(p.get("f:pricetaxincluded"))))
                        .setUnitPriceTaxFree(Utility.parseDoubleFromString(String.valueOf(p.get("f:pricetaxfree"))));

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
            if (screen == null) {
                Screen s = tracker.Screens().add(screenLabel);
                s.setCart(stCart);
                s.setIsBasketScreen(false).sendView();
            } else {
                screen.setCart(stCart);
                screen.setIsBasketScreen(false).sendView();
                screen.setCart(null);
                stCart.unset();
            }
        }

        return generatedEvents;
    }
}
