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
 * Wrapper class to manage specific order delivery feature
 */
public class OrderDelivery {

    private final Order order;
    private double shippingFeesTaxIncluded;
    private double shippingFeesTaxFree;
    private String deliveryMethod;

    OrderDelivery(Order order) {
        this.order = order;
        shippingFeesTaxIncluded = -1;
        shippingFeesTaxFree = -1;
        deliveryMethod = null;
    }

    /**
     * Get shipping fees tax included value
     *
     * @return the shipping fees tax included value
     */
    public double getShippingFeesTaxIncluded() {
        return shippingFeesTaxIncluded;
    }

    /**
     * Get shipping fess tax free value
     *
     * @return the shipping fess tax free value
     */
    public double getShippingFeesTaxFree() {
        return shippingFeesTaxFree;
    }

    /**
     * Get delivery method
     *
     * @return the delivery method
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Set a new shipping fees tax included value
     *
     * @param shippingFeesTaxIncluded /
     * @return OrderDelivery instance
     */
    public OrderDelivery setShippingFeesTaxIncluded(double shippingFeesTaxIncluded) {
        this.shippingFeesTaxIncluded = shippingFeesTaxIncluded;
        return this;
    }

    /**
     * Set a new shipping fees tax free value
     *
     * @param shippingFeesTaxFree /
     * @return OrderDelivery instance
     */
    public OrderDelivery setShippingFeesTaxFree(double shippingFeesTaxFree) {
        this.shippingFeesTaxFree = shippingFeesTaxFree;
        return this;
    }

    /**
     * Set a new delivery method
     *
     * @param deliveryMethod /
     * @return OrderDelivery instance
     */
    public OrderDelivery setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }

    /**
     * Attach delivery to order
     *
     * @param shippingFeesTaxFree     order delivery shipping fees tax free
     * @param shippingFeesTaxIncluded order delivery shipping fees tax included
     * @param deliveryMethod          order delivery method
     * @return Order instance
     */
    public Order set(double shippingFeesTaxFree, double shippingFeesTaxIncluded, String deliveryMethod) {
        setShippingFeesTaxFree(shippingFeesTaxFree)
                .setShippingFeesTaxIncluded(shippingFeesTaxIncluded)
                .setDeliveryMethod(deliveryMethod);
        return order;
    }
}
