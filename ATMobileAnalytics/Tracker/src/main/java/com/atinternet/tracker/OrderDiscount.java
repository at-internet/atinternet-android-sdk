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

public class OrderDiscount {

    private final Order order;

    private double discountTaxIncluded;

    private double discountTaxFree;

    private String promotionalCode;

    public String getPromotionalCode() {
        return promotionalCode;
    }

    public double getDiscountTaxIncluded() {
        return discountTaxIncluded;
    }

    public double getDiscountTaxFree() {
        return discountTaxFree;
    }

    public OrderDiscount setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
        return this;
    }

    public OrderDiscount setDiscountTaxIncluded(double discountTaxIncluded) {
        this.discountTaxIncluded = discountTaxIncluded;
        return this;
    }

    public OrderDiscount setDiscountTaxFree(double discountTaxFree) {
        this.discountTaxFree = discountTaxFree;
        return this;
    }

    OrderDiscount(Order order) {
        this.order = order;
        discountTaxIncluded = -1;
        discountTaxFree = -1;
        promotionalCode = null;
    }

    /**
     * Helper to set all properties
     *
     * @param discountTaxFree     double
     * @param discountTaxIncluded double
     * @param promotionalCode     String
     * @return Order
     */
    public Order set(double discountTaxFree, double discountTaxIncluded, String promotionalCode) {
        setDiscountTaxFree(discountTaxFree)
                .setDiscountTaxIncluded(discountTaxIncluded)
                .setPromotionalCode(promotionalCode);
        return order;
    }
}
