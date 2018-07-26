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
package com.atinternet.tracker;

import com.atinternet.tracker.ecommerce.AddProducts;
import com.atinternet.tracker.ecommerce.ClickProducts;
import com.atinternet.tracker.ecommerce.DeliveryCheckouts;
import com.atinternet.tracker.ecommerce.DisplayCarts;
import com.atinternet.tracker.ecommerce.DisplayLists;
import com.atinternet.tracker.ecommerce.DisplayPageProducts;
import com.atinternet.tracker.ecommerce.DisplayProducts;
import com.atinternet.tracker.ecommerce.PaymentCheckouts;
import com.atinternet.tracker.ecommerce.RemoveProducts;
import com.atinternet.tracker.ecommerce.TransactionConfirmations;
import com.atinternet.tracker.ecommerce.UpdateCarts;

public class ECommerce {

    private Tracker tracker;
    private Events events;

    private DisplayProducts displayProducts;
    private DisplayLists displayLists;
    private ClickProducts clickProducts;
    private DisplayPageProducts displayPageProducts;
    private AddProducts addProducts;
    private RemoveProducts removeProducts;
    private DisplayCarts displayCarts;
    private UpdateCarts updateCarts;
    private DeliveryCheckouts deliveryCheckouts;
    private PaymentCheckouts paymentCheckouts;
    private TransactionConfirmations transactionConfirmations;

    ECommerce(Tracker tracker) {
        this.tracker = tracker;
        this.events = tracker.Events();
    }

    /**
     * Enable automatic sales tracker
     *
     * @param enabled           /
     * @param setConfigCallback Callback called when the operation has been done
     * @param sync              (optional) perform the operation synchronously (default: false)
     */
    public void setAutoSalesTrackerEnabled(boolean enabled, SetConfigCallback setConfigCallback, boolean... sync) {
        tracker.setConfig(TrackerConfigurationKeys.AUTO_SALES_TRACKER, enabled, setConfigCallback, sync);
    }

    /***
     * Create DisplayProducts helper
     * @return DisplayProducts
     */
    public DisplayProducts DisplayProducts() {
        if (displayProducts == null) {
            displayProducts = new DisplayProducts(events);
        }
        return displayProducts;
    }

    /***
     * Create DisplayLists helper
     * @return DisplayLists
     */
    public DisplayLists DisplayLists() {
        if (displayLists == null) {
            displayLists = new DisplayLists(events);
        }
        return displayLists;
    }

    /***
     * Create ClickProducts helper
     * @return ClickProducts
     */
    public ClickProducts ClickProducts() {
        if (clickProducts == null) {
            clickProducts = new ClickProducts(events);
        }
        return clickProducts;
    }

    /***
     * Create DisplayPageProducts helper
     * @return DisplayPageProducts
     */
    public DisplayPageProducts DisplayPageProducts() {
        if (displayPageProducts == null) {
            displayPageProducts = new DisplayPageProducts(events, tracker);
        }
        return displayPageProducts;
    }

    /***
     * Create AddProducts helper
     * @return AddProducts
     */
    public AddProducts AddProducts() {
        if (addProducts == null) {
            addProducts = new AddProducts(events);
        }
        return addProducts;
    }

    /***
     * Create RemoveProducts helper
     * @return RemoveProducts
     */
    public RemoveProducts RemoveProducts() {
        if (removeProducts == null) {
            removeProducts = new RemoveProducts(events);
        }
        return removeProducts;
    }

    /***
     * Create DisplayCarts helper
     * @return DisplayCarts
     */
    public DisplayCarts DisplayCarts() {
        if (displayCarts == null) {
            displayCarts = new DisplayCarts(events, tracker);
        }
        return displayCarts;
    }

    /***
     * Create UpdateCarts helper
     * @return UpdateCarts
     */
    public UpdateCarts UpdateCarts() {
        if (updateCarts == null) {
            updateCarts = new UpdateCarts(events);
        }
        return updateCarts;
    }

    /***
     * Create DeliveryCheckouts helper
     * @return DeliveryCheckouts
     */
    public DeliveryCheckouts DeliveryCheckouts() {
        if (deliveryCheckouts == null) {
            deliveryCheckouts = new DeliveryCheckouts(events);
        }
        return deliveryCheckouts;
    }

    /***
     * Create PaymentCheckouts helper
     * @return PaymentCheckouts
     */
    public PaymentCheckouts PaymentCheckouts() {
        if (paymentCheckouts == null) {
            paymentCheckouts = new PaymentCheckouts(events);
        }
        return paymentCheckouts;
    }

    /***
     * Create TransactionConfirmations helper
     * @return TransactionConfirmations
     */
    public TransactionConfirmations TransactionConfirmations() {
        if (transactionConfirmations == null) {
            transactionConfirmations = new TransactionConfirmations(events, tracker);
        }
        return transactionConfirmations;
    }
}
