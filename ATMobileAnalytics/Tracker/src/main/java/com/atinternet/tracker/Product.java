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

import java.util.LinkedHashMap;

public class Product extends BusinessObject {

    /**
     * Action enum
     */
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

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private CustomObjects customObjects;

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        return customObjectsMap == null ? (customObjectsMap = new LinkedHashMap<>()) : customObjectsMap;
    }

    /**
     * Get action
     *
     * @return Product.Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get product id
     *
     * @return String
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Get first category
     *
     * @return String
     */
    public String getCategory1() {
        return category1;
    }

    /**
     * Get second category
     *
     * @return String
     */
    public String getCategory2() {
        return category2;
    }

    /**
     * Get third category
     *
     * @return String
     */
    public String getCategory3() {
        return category3;
    }

    /**
     * Get fourth category
     *
     * @return String
     */
    public String getCategory4() {
        return category4;
    }

    /**
     * Get fifth category
     *
     * @return String
     */
    public String getCategory5() {
        return category5;
    }

    /**
     * Get sixth category
     *
     * @return String
     */
    public String getCategory6() {
        return category6;
    }

    /**
     * Get quantity
     *
     * @return int
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get unit price tax included value
     *
     * @return double
     */
    public double getUnitPriceTaxIncluded() {
        return unitPriceTaxIncluded;
    }

    /**
     * Get unit price tax free value
     *
     * @return double
     */
    public double getUnitPriceTaxFree() {
        return unitPriceTaxFree;
    }

    /**
     * Get discount tax included value
     *
     * @return double
     */
    public double getDiscountTaxIncluded() {
        return discountTaxIncluded;
    }

    /**
     * Get discount tax free value
     *
     * @return double
     */
    public double getDiscountTaxFree() {
        return discountTaxFree;
    }

    /**
     * Get promotional code
     *
     * @return String
     */
    public String getPromotionalCode() {
        return promotionalCode;
    }

    /**
     * Set a new action
     *
     * @param action Product.Action
     * @return Product
     */
    public Product setAction(Action action) {
        this.action = action;
        return this;
    }

    /**
     * Set a new product id
     *
     * @param productId String
     * @return Product
     */
    public Product setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Set a new first category
     *
     * @param category1 String
     * @return Product
     */
    public Product setCategory1(String category1) {
        this.category1 = category1;
        return this;
    }

    /**
     * Set a new second category
     *
     * @param category2 String
     * @return Product
     */
    public Product setCategory2(String category2) {
        this.category2 = category2;
        return this;
    }

    /**
     * Set a new third category
     *
     * @param category3 String
     * @return Product
     */
    public Product setCategory3(String category3) {
        this.category3 = category3;
        return this;
    }

    /**
     * Set a new fourth category
     *
     * @param category4 String
     * @return Product
     */
    public Product setCategory4(String category4) {
        this.category4 = category4;
        return this;
    }

    /**
     * Set a new fifth category
     *
     * @param category5 String
     * @return Product
     */
    public Product setCategory5(String category5) {
        this.category5 = category5;
        return this;
    }

    /**
     * Set a new sixth category
     *
     * @param category6 String
     * @return Product
     */
    public Product setCategory6(String category6) {
        this.category6 = category6;
        return this;
    }

    /**
     * Set a new quantity
     *
     * @param quantity int
     * @return Product
     */
    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Set a new unit price tax included value
     *
     * @param unitPriceTaxIncluded double
     * @return Product
     */
    public Product setUnitPriceTaxIncluded(double unitPriceTaxIncluded) {
        this.unitPriceTaxIncluded = unitPriceTaxIncluded;
        return this;
    }

    /**
     * Set a new unit price tax free
     *
     * @param unitPriceTaxFree double
     * @return Product
     */
    public Product setUnitPriceTaxFree(double unitPriceTaxFree) {
        this.unitPriceTaxFree = unitPriceTaxFree;
        return this;
    }

    /**
     * Set a new discount tax included value
     *
     * @param discountTaxIncluded double
     * @return Product
     */
    public Product setDiscountTaxIncluded(double discountTaxIncluded) {
        this.discountTaxIncluded = discountTaxIncluded;
        return this;
    }

    /**
     * Set a new discount tax free value
     *
     * @param discountTaxFree double
     * @return Product
     */
    public Product setDiscountTaxFree(double discountTaxFree) {
        this.discountTaxFree = discountTaxFree;
        return this;
    }

    /**
     * Set a new promotional code
     *
     * @param promotionalCode String
     * @return Product
     */
    public Product setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
        return this;
    }

    /**
     * Get CustomObjects
     *
     * @return CustomObjects
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    Product(Tracker tracker) {
        super(tracker);
        action = Action.View;
        productId = "";
    }

    /**
     * Send products view hit
     */
    public void sendView() {
        action = Action.View;
        tracker.getDispatcher().dispatch(this);
    }

    @Override
    void setEvent() {
        tracker.setParam(Hit.HitParam.HitType.stringValue(), "pdt");

        if (customObjectsMap != null) {
            for (CustomObject co : customObjectsMap.values()) {
                co.setEvent();
            }
        }

        tracker.setParam(Hit.HitParam.ProductList.stringValue(), buildProductName(), new ParamOption().setAppend(true).setEncode(true).setSeparator("|"));
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
}
