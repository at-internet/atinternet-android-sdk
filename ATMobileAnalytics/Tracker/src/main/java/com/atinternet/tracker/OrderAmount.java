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
 * Wrapper class to manage specific order amount feature
 */
public class OrderAmount {

    private final Order order;
    private double amountTaxFree;
    private double amountTaxIncluded;
    private double taxAmount;

    OrderAmount(Order order) {
        this.order = order;
        amountTaxFree = -1;
        amountTaxIncluded = -1;
        taxAmount = -1;
    }

    /**
     * Get amount tax free
     *
     * @return the amount tax free
     */
    public double getAmountTaxFree() {
        return amountTaxFree;
    }

    /**
     * Get amount tax included
     *
     * @return the amount tax included
     */
    public double getAmountTaxIncluded() {
        return amountTaxIncluded;
    }

    /**
     * Get tax amount
     *
     * @return the tax amount
     */
    public double getTaxAmount() {
        return taxAmount;
    }

    /**
     * Set a new amount tax free
     *
     * @param amountTaxFree /
     * @return OrderAmount instance
     */
    public OrderAmount setAmountTaxFree(double amountTaxFree) {
        this.amountTaxFree = amountTaxFree;
        return this;
    }

    /**
     * Set a new amount tax included
     *
     * @param amountTaxIncluded /
     * @return OrderAmount instance
     */
    public OrderAmount setAmountTaxIncluded(double amountTaxIncluded) {
        this.amountTaxIncluded = amountTaxIncluded;
        return this;
    }

    /**
     * Set a new tax amount
     *
     * @param taxAmount /
     * @return OrderAmount instance
     */
    public OrderAmount setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    /**
     * Attach amount object to order
     *
     * @param amountTaxFree     order amount tax free
     * @param amountTaxIncluded order amount tax included
     * @param taxAmount         order amount tax
     * @return Order instance
     */
    public Order set(double amountTaxFree, double amountTaxIncluded, double taxAmount) {
        setAmountTaxFree(amountTaxFree)
                .setAmountTaxIncluded(amountTaxIncluded)
                .setTaxAmount(taxAmount);
        return order;
    }
}
