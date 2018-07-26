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
 * Wrapper class to manage SalesTracker order feature
 */
public class Order extends BusinessObject {

    private String orderId;
    private double turnover;
    private int status;
    private OrderDiscount orderDiscount;
    private OrderAmount orderAmount;
    private OrderDelivery orderDelivery;
    private int paymentMethod;
    private boolean newCustomer;
    private OrderCustomVars customVariables;
    private boolean confirmationRequired;

    OrderCustomVars getCustomVariables() {
        return customVariables;
    }

    Order(Tracker tracker) {
        super(tracker);
        orderId = "";
        turnover = -1.0;
        status = -1;
        paymentMethod = -1;
        newCustomer = false;
        confirmationRequired = false;
    }

    /**
     * Get order discount object
     *
     * @return OrderDiscount instance
     */
    public OrderDiscount Discount() {
        if (orderDiscount == null) {
            orderDiscount = new OrderDiscount(this);
        }
        return orderDiscount;
    }

    /**
     * Get order amount object
     *
     * @return OrderAmount instance
     */
    public OrderAmount Amount() {
        if (orderAmount == null) {
            orderAmount = new OrderAmount(this);
        }
        return orderAmount;
    }

    /**
     * Get order delivery object
     *
     * @return OrderDelivery instance
     */
    public OrderDelivery Delivery() {
        if (orderDelivery == null) {
            orderDelivery = new OrderDelivery(this);
        }
        return orderDelivery;
    }

    /**
     * Get order custom variables
     *
     * @return OrderCustomVars instance
     */
    public OrderCustomVars CustomVars() {
        if (customVariables == null) {
            customVariables = new OrderCustomVars();
        }
        return customVariables;
    }

    /**
     * Get order identifier
     *
     * @return the order identifier
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Get order turnover
     *
     * @return the order turnover
     */
    public double getTurnover() {
        return turnover;
    }

    /**
     * Get order status
     *
     * @return the order status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Get boolean "newCustomer" value
     *
     * @return true if it's a new customer
     */
    public boolean isNewCustomer() {
        return newCustomer;
    }

    /**
     * Get order payment method
     *
     * @return the order payment method
     */
    public int getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Get boolean "confirmationRequired" value
     *
     * @return true if confirmation is required
     */
    public boolean isConfirmationRequired() {
        return confirmationRequired;
    }

    /**
     * Set a new order identifier
     *
     * @param orderId order identifier
     * @return Order instance
     */
    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * Set a new turnover
     *
     * @param turnover /
     * @return Order instance
     */
    public Order setTurnover(double turnover) {
        this.turnover = turnover;
        return this;
    }

    /**
     * Set a new status
     *
     * @param status /
     * @return Order instance
     */
    public Order setStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * Change boolean "newCustomer" value
     *
     * @param newCustomer /
     * @return Order instance
     */
    public Order setNewCustomer(boolean newCustomer) {
        this.newCustomer = newCustomer;
        return this;
    }

    /**
     * Set a new payment method
     *
     * @param paymentMethod /
     * @return Order instance
     */
    public Order setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Change boolean "confirmationRequired" value
     *
     * @param confirmationRequired /
     * @return Order instance
     */
    public Order setConfirmationRequired(boolean confirmationRequired) {
        this.confirmationRequired = confirmationRequired;
        return this;
    }

    @Override
    void setParams() {

        ParamOption encode = new ParamOption().setEncode(true);

        // Informations de commande
        tracker.setParam("cmd", orderId);

        if (turnover > -1.0) {
            tracker.setParam("roimt", turnover);
        }
        if (status > -1) {
            tracker.setParam("st", status);
        }

        // Nouveau client
        tracker.setParam("newcus", newCustomer ? "1" : "0");

        // Remises
        if (orderDiscount != null) {
            double discountTaxFree = orderDiscount.getDiscountTaxFree();
            double discountTaxIncluded = orderDiscount.getDiscountTaxIncluded();
            String promotionalCode = orderDiscount.getPromotionalCode();
            if (discountTaxFree > -1) {
                tracker.setParam("dscht", discountTaxFree);
            }
            if (discountTaxIncluded > -1) {
                tracker.setParam("dsc", discountTaxIncluded);
            }
            if (promotionalCode != null) {
                tracker.setParam("pcd", promotionalCode, encode);
            }
        }

        // Montants
        if (orderAmount != null) {
            double amountTaxFree = orderAmount.getAmountTaxFree();
            double amountTaxIncluded = orderAmount.getAmountTaxIncluded();
            double taxAmount = orderAmount.getTaxAmount();
            if (amountTaxFree > -1) {
                tracker.setParam("mtht", amountTaxFree);
            }
            if (amountTaxIncluded > -1) {
                tracker.setParam("mtttc", amountTaxIncluded);
            }
            if (taxAmount > -1) {
                tracker.setParam("tax", taxAmount);
            }
        }

        // Livraison
        if (orderDelivery != null) {
            double shippingFeesTaxFree = orderDelivery.getShippingFeesTaxFree();
            double shippingFeesTaxIncluded = orderDelivery.getShippingFeesTaxIncluded();
            String deliveryMethod = orderDelivery.getDeliveryMethod();
            if (shippingFeesTaxFree > -1) {
                tracker.setParam("fpht", shippingFeesTaxFree);
            }
            if (shippingFeesTaxIncluded > -1) {
                tracker.setParam("fp", shippingFeesTaxIncluded);
            }
            if (deliveryMethod != null) {
                tracker.setParam("dl", deliveryMethod, encode);
            }
        }

        // Custom variables
        if (customVariables != null) {
            for (OrderCustomVar var : customVariables) {
                tracker.setParam("o" + var.getVarId(), var.getValue());
            }
        }

        // Methode de paiement
        if (paymentMethod > -1) {
            tracker.setParam("mp", paymentMethod);
        }

        // Confirmation requise
        if (confirmationRequired) {
            tracker.setParam(Hit.HitParam.Tp.stringValue(), "pre1");
        }
    }
}
