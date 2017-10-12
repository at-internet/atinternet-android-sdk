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

/**
 * Wrapper class to manage specific order discount feature
 */
public class OrderDiscount {

    private final Order order;
    private double discountTaxIncluded;
    private double discountTaxFree;
    private String promotionalCode;

    OrderDiscount(Order order) {
        this.order = order;
        discountTaxIncluded = -1;
        discountTaxFree = -1;
        promotionalCode = null;
    }

    /**
     * Get promotional code
     *
     * @return the promotional code
     */
    public String getPromotionalCode() {
        return promotionalCode;
    }

    /**
     * Get discount tax included value
     *
     * @return the discount tax included value
     */
    public double getDiscountTaxIncluded() {
        return discountTaxIncluded;
    }

    /**
     * Get discount tax free value
     *
     * @return the discount tax free value
     */
    public double getDiscountTaxFree() {
        return discountTaxFree;
    }

    /**
     * Set a new promotional code
     *
     * @param promotionalCode /
     * @return OrderDiscount instance
     */
    public OrderDiscount setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
        return this;
    }

    /**
     * Set a new discount tax included value
     *
     * @param discountTaxIncluded /
     * @return OrderDiscount instance
     */
    public OrderDiscount setDiscountTaxIncluded(double discountTaxIncluded) {
        this.discountTaxIncluded = discountTaxIncluded;
        return this;
    }

    /**
     * Set a new discount tax free value
     *
     * @param discountTaxFree /
     * @return OrderDiscount instance
     */
    public OrderDiscount setDiscountTaxFree(double discountTaxFree) {
        this.discountTaxFree = discountTaxFree;
        return this;
    }

    /**
     * Attach discount to order
     *
     * @param discountTaxFree     order discount tax free
     * @param discountTaxIncluded order discount tax included
     * @param promotionalCode     order promotional code
     * @return Order instance
     */
    public Order set(double discountTaxFree, double discountTaxIncluded, String promotionalCode) {
        setDiscountTaxFree(discountTaxFree)
                .setDiscountTaxIncluded(discountTaxIncluded)
                .setPromotionalCode(promotionalCode);
        return order;
    }
}
