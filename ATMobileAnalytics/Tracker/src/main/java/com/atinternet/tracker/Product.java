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

public class Product extends BusinessObject {

    public enum Action {
        View("view");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private Action action;

    private String productId = null;

    private String category1 = null;

    private String category2 = null;

    private String category3 = null;

    private String category4 = null;

    private String category5 = null;

    private String category6 = null;

    private int quantity = -1;

    private double unitPriceTaxIncluded = -1;

    private double unitPriceTaxFree = -1;

    private double discountTaxIncluded = -1;

    private double discountTaxFree = -1;

    private String promotionalCode = null;

    public Action getAction() {
        return action;
    }

    public String getProductId() {
        return productId;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public String getCategory4() {
        return category4;
    }

    public String getCategory5() {
        return category5;
    }

    public String getCategory6() {
        return category6;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPriceTaxIncluded() {
        return unitPriceTaxIncluded;
    }

    public double getUnitPriceTaxFree() {
        return unitPriceTaxFree;
    }

    public double getDiscountTaxIncluded() {
        return discountTaxIncluded;
    }

    public double getDiscountTaxFree() {
        return discountTaxFree;
    }

    public String getPromotionalCode() {
        return promotionalCode;
    }

    public Product setAction(Action action) {
        this.action = action;
        return this;
    }

    public Product setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public Product setCategory1(String category1) {
        this.category1 = category1;
        return this;
    }

    public Product setCategory2(String category2) {
        this.category2 = category2;
        return this;
    }

    public Product setCategory3(String category3) {
        this.category3 = category3;
        return this;
    }

    public Product setCategory4(String category4) {
        this.category4 = category4;
        return this;
    }

    public Product setCategory5(String category5) {
        this.category5 = category5;
        return this;
    }

    public Product setCategory6(String category6) {
        this.category6 = category6;
        return this;
    }

    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Product setUnitPriceTaxIncluded(double unitPriceTaxIncluded) {
        this.unitPriceTaxIncluded = unitPriceTaxIncluded;
        return this;
    }

    public Product setUnitPriceTaxFree(double unitPriceTaxFree) {
        this.unitPriceTaxFree = unitPriceTaxFree;
        return this;
    }

    public Product setDiscountTaxIncluded(double discountTaxIncluded) {
        this.discountTaxIncluded = discountTaxIncluded;
        return this;
    }

    public Product setDiscountTaxFree(double discountTaxFree) {
        this.discountTaxFree = discountTaxFree;
        return this;
    }

    public Product setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
        return this;
    }

    /**
     * Super constructor
     *
     * @param tracker Tracker
     */
    Product(Tracker tracker) {
        super(tracker);
        action = Action.View;
        productId = "";
    }

    @Override
    void setEvent() {
        tracker.setParam("type", "pdt");

        tracker.setParam("pdtl", buildProductName(), new ParamOption().setAppend(true).setEncode(true).setSeparator("|"));
    }

    String buildProductName() {
        String productName = category1 == null ? "" : category1 + "::";
        productName = category2 == null ? productName : productName + category2 + "::";
        productName = category3 == null ? productName : productName + category3 + "::";
        productName = category4 == null ? productName : productName + category4 + "::";
        productName = category5 == null ? productName : productName + category5 + "::";
        productName = category6 == null ? productName : productName + category6 + "::";

        return productName + productId;
    }

    /**
     * Send products view hit
     */
    public void sendView() {
        action = Action.View;
        tracker.getDispatcher().dispatch(this);
    }
}
