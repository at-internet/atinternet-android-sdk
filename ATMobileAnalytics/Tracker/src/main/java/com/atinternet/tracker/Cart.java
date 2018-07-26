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

import java.util.ArrayList;

/**
 * Wrapper class to manage your cart
 */
public class Cart extends BusinessObject {

    private String cartId;
    private Products products;
    private ArrayList<Product> productsList;


    ArrayList<Product> getProductsList() {
        if (productsList == null) {
            productsList = new ArrayList<>();
        }
        return productsList;
    }

    Cart(Tracker tracker) {
        super(tracker);
        cartId = null;
        productsList = new ArrayList<>();
    }

    /**
     * Get cart products
     *
     * @return the Products instance
     */
    public Products Products() {
        if (products == null) {
            products = new Products(this);
        }
        return products;
    }

    /**
     * Get the cart identifier
     *
     * @return the cart identifier
     */
    public String getCartId() {
        return cartId;
    }


    /**
     * Set a new cart identifier
     *
     * @param cartId cart identifier
     * @return the Cart instance
     */
    public Cart setCartId(String cartId) {
        this.cartId = cartId;
        return this;
    }

    /**
     * Set a cart
     *
     * @param cartId cart identifier
     * @return the Cart instance
     */
    public Cart set(String cartId) {
        if (this.cartId != null && !this.cartId.equals(cartId) && products != null) {
            products.removeAll();
        }
        tracker.getBusinessObjects().put(id, this);
        return setCartId(cartId);
    }

    /**
     * Unset the cart
     *
     * @return the unset Cart instance
     */
    public Cart unset() {
        cartId = null;
        if (products != null) {
            products.removeAll();
        }
        tracker.getBusinessObjects().remove(id);
        return this;
    }

    @Override
    void setParams() {
        if (cartId != null) {
            tracker.setParam(Hit.HitParam.CartId.stringValue(), cartId);
        }

        if (products != null && productsList != null) {
            ParamOption encoding = new ParamOption().setEncode(true);
            int length = productsList.size();
            for (int i = 0; i < length; i++) {
                Product p = productsList.get(i);
                tracker.setParam("pdt" + (i + 1), p.buildProductName(), encoding);
                if (p.getQuantity() > -1) {
                    tracker.setParam("qte" + (i + 1), p.getQuantity());
                }
                if (p.getUnitPriceTaxFree() > -1) {
                    tracker.setParam("mtht" + (i + 1), p.getUnitPriceTaxFree());
                }
                if (p.getUnitPriceTaxIncluded() > -1) {
                    tracker.setParam("mt" + (i + 1), p.getUnitPriceTaxIncluded());
                }
                if (p.getDiscountTaxFree() > -1) {
                    tracker.setParam("dscht" + (i + 1), p.getDiscountTaxFree());
                }
                if (p.getDiscountTaxIncluded() > -1) {
                    tracker.setParam("dsc" + (i + 1), p.getDiscountTaxIncluded());
                }
                if (p.getPromotionalCode() != null) {
                    tracker.setParam("pcode" + (i + 1), p.getPromotionalCode(), encoding);
                }
            }
        }
    }
}
