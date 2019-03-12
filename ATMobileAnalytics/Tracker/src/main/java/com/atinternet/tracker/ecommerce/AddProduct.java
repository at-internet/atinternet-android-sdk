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
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceCart;
import com.atinternet.tracker.ecommerce.objectproperties.ECommerceProduct;

import java.util.List;
import java.util.Map;

public class AddProduct extends Event {

    private ECommerceProduct product;
    private ECommerceCart cart;

    AddProduct() {
        super("product.add_to_cart");
        cart = new ECommerceCart();
        product = new ECommerceProduct();
    }

    public ECommerceProduct Product() {
        return product;
    }


    public ECommerceCart Cart() {
        return cart;
    }

    @Override
    protected Map<String, Object> getData() {
        data.put("product", product.getAll());
        data.put("cart", cart.getAll());
        return super.getData();
    }

    @Override
    protected List<Event> getAdditionalEvents() {
        List<Event> generatedEvents = super.getAdditionalEvents();

        if (Utility.parseBooleanFromString(String.valueOf(product.get("b:cartcreation")))) {
            CartCreation cc = new CartCreation();
            ECommerceCart ccc = cc.Cart();
            int quantity = Utility.parseIntFromString(String.valueOf(product.get("n:quantity")));
            ccc.set("id", String.valueOf(cart.get("s:id")));
            ccc.set("currency", String.valueOf(product.get("s:currency")));
            ccc.set("turnoverTaxIncluded", Utility.parseDoubleFromString(String.valueOf(product.get("f:priceTaxIncluded"))) * quantity);
            ccc.set("turnoverTaxFree", Utility.parseDoubleFromString(String.valueOf(product.get("f:priceTaxFree"))) * quantity);
            ccc.set("quantity", quantity);
            ccc.set("nbDistinctProduct", 1);
            generatedEvents.add(cc);
        }

        return generatedEvents;
    }
}
