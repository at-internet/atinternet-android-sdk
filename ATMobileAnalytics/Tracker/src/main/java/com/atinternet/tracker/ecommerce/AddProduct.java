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
import com.atinternet.tracker.Utility;
import com.atinternet.tracker.ecommerce.objectproperties.Cart;
import com.atinternet.tracker.ecommerce.objectproperties.Product;

import java.util.List;
import java.util.Map;

public class AddProduct extends EcommerceEvent {

    private Product product;
    private Cart cart;

    AddProduct(Screen screen) {
        super("product.add_to_cart", screen);
        cart = new Cart();
        product = new Product();
    }

    public Product Product() {
        return product;
    }


    public Cart Cart() {
        return cart;
    }

    @Override
    protected Map<String, Object> getData() {
        data.put("product", product);
        data.put("cart", cart);
        return super.getData();
    }

    @Override
    protected List<Event> getAdditionalEvents() {
        List<Event> generatedEvents = super.getAdditionalEvents();

        if (Utility.parseBooleanFromString(String.valueOf(product.get("b:cart.creation")))) {
            CartCreation cc = new CartCreation(screen);
            Cart ccc = cc.Cart();
            int quantity = Utility.parseIntFromString(String.valueOf(product.get("n:quantity")));
            ccc.put("id", String.valueOf(cart.get("s:id")));
            ccc.put("currency", String.valueOf(product.get("s:currency")));
            ccc.put("turnoverTaxIncluded", Utility.parseDoubleFromString(String.valueOf(product.get("f:priceTaxIncluded"))) * quantity);
            ccc.put("turnoverTaxFree", Utility.parseDoubleFromString(String.valueOf(product.get("f:priceTaxFree"))) * quantity);
            ccc.put("quantity", quantity);
            ccc.put("nbDistinctProduct", 1);
            generatedEvents.add(cc);
        }

        return generatedEvents;
    }
}
