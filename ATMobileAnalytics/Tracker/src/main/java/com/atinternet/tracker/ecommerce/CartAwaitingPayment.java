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
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceCart;
import com.atinternet.tracker.ecommerce.objectproperties.ECommercePayment;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceShipping;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceTransaction;

import java.util.Map;

public class CartAwaitingPayment extends Event {

    private ECommerceCart cart;
    private ECommerceTransaction transaction;
    private ECommerceShipping shipping;
    private ECommercePayment payment;

    public CartAwaitingPayment() {
        super("cart.awaiting_payment");
        cart = new ECommerceCart();
        transaction = new ECommerceTransaction();
        shipping = new ECommerceShipping();
        payment = new ECommercePayment();
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
}
