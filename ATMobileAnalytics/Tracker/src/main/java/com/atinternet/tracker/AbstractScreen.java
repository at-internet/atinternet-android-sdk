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

public abstract class AbstractScreen extends BusinessObject {

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


    String name;
    String chapter1;
    String chapter2;
    String chapter3;
    Action action;
    boolean isBasketScreen;
    int level2;

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private LinkedHashMap<String, CustomVar> customVarsMap;
    private LinkedHashMap<String, SelfPromotionImpression> selfPromotionImpressionsMap;
    private LinkedHashMap<String, PublisherImpression> publisherImpressionsMap;

    private Location location;
    private Aisle aisle;
    private CustomTreeStructure customTreeStructure;
    private InternalSearch internalSearch;
    private Cart cart;
    private Campaign campaign;
    private Order order;
    private CustomVars customVars;
    private CustomObjects customObjects;
    private PublisherImpressions publisherImpressions;
    private SelfPromotionImpressions selfPromotionImpressions;

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        return customObjectsMap == null ? (customObjectsMap = new LinkedHashMap<>()) : customObjectsMap;
    }

    LinkedHashMap<String, CustomVar> getCustomVarsMap() {
        return customVarsMap == null ? (customVarsMap = new LinkedHashMap<>()) : customVarsMap;
    }

    LinkedHashMap<String, SelfPromotionImpression> getSelfPromotionImpressionsMap() {
        return selfPromotionImpressionsMap == null ? (selfPromotionImpressionsMap = new LinkedHashMap<>()) : selfPromotionImpressionsMap;
    }

    LinkedHashMap<String, PublisherImpression> getPublisherImpressionsMap() {
        return publisherImpressionsMap == null ? (publisherImpressionsMap = new LinkedHashMap<>()) : publisherImpressionsMap;
    }

    /**
     * Get the name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the action type
     *
     * @return AbstractScreen.Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get the level2
     *
     * @return int
     */
    public int getLevel2() {
        return level2;
    }

    /**
     * Get the first chapter
     *
     * @return String
     */
    public String getChapter1() {
        return chapter1;
    }

    /**
     * Get the second chapter
     *
     * @return String
     */
    public String getChapter2() {
        return chapter2;
    }

    /**
     * Get the third chapter
     *
     * @return String
     */
    public String getChapter3() {
        return chapter3;
    }

    /**
     * Get boolean isBasketScreen value
     *
     * @return boolean
     */
    public boolean isBasketScreen() {
        return isBasketScreen;
    }

    /**
     * Set a cart
     *
     * @param cart Cart
     */
    public void setCart(Cart cart) {
        this.cart = cart;
        isBasketScreen = this.cart != null;
    }

    /**
     * Add location informations
     *
     * @param latitude  double
     * @param longitude double
     * @return Location
     */
    public Location Location(double latitude, double longitude) {
        return location == null ? (location = new Location(tracker)
                .setLatitude(latitude)
                .setLongitude(longitude)) : location;
    }

    /**
     * Add aisle informations
     *
     * @param level1 String
     * @return Aisle
     */
    public Aisle Aisle(String level1) {
        return aisle == null ? (aisle = new Aisle(tracker).setLevel1(level1)) : aisle;
    }

    /**
     * Add a custom tree structure
     *
     * @param category1 int
     * @return CustomTreeStructure
     */
    public CustomTreeStructure CustomTreeStructure(int category1) {
        return customTreeStructure == null ? (customTreeStructure = new CustomTreeStructure(tracker).setCategory1(category1)) : customTreeStructure;
    }

    /**
     * Add campaign informations
     *
     * @param campaignId String
     * @return Campaign
     */
    public Campaign Campaign(String campaignId) {
        return campaign == null ? (campaign = new Campaign(tracker).setCampaignId(campaignId)) : campaign;
    }

    /**
     * Add an order
     *
     * @param orderId  String
     * @param turnover double
     * @return Order
     * @deprecated Since 2.3.4, bug was revealed when this method is used. Use Orders tracker property instead.
     */
    @Deprecated
    public Order Order(String orderId, double turnover) {
        return order == null ? (order = new Order(tracker).setOrderId(orderId).setTurnover(turnover)) : order;
    }

    /**
     * Add internal search informations
     *
     * @param keywordLabel     String
     * @param resultScreenNumber int
     * @return InternalSearch
     */
    public InternalSearch InternalSearch(String keywordLabel, int resultScreenNumber) {
        return internalSearch == null ? (internalSearch = new InternalSearch(tracker)
                .setKeyword(keywordLabel)
                .setResultScreenNumber(resultScreenNumber)) : internalSearch;
    }

    /**
     * Get CustomVars
     *
     * @return CustomVars
     */
    public CustomVars CustomVars() {
        return customVars == null ? (customVars = new CustomVars(this)) : customVars;
    }

    /**
     * Get CustomObjects
     *
     * @return CustomObjects
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    /**
     * Get Publishers
     *
     * @return PublisherImpressions
     */
    public PublisherImpressions Publishers() {
        return publisherImpressions == null ? (publisherImpressions = new PublisherImpressions(this)) : publisherImpressions;
    }

    /**
     * Get SelfPromotions
     *
     * @return SelfPromotionImpressions
     */
    public SelfPromotionImpressions SelfPromotions() {
        return selfPromotionImpressions == null ? (selfPromotionImpressions = new SelfPromotionImpressions(this)) : selfPromotionImpressions;
    }

    /**
     * Send a screen view event
     */
    public void sendView() {
        action = Action.View;
        tracker.getDispatcher().dispatch(this);
    }

    AbstractScreen(Tracker tracker) {
        super(tracker);
        action = Action.View;
        level2 = -1;
        name = "";
    }

    @Override
    void setEvent() {
        if (level2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), level2);
        }

        if (isBasketScreen) {
            tracker.setParam(Hit.HitParam.Tp.stringValue(), "cart");
        }

        if (location != null) {
            location.setEvent();
        }

        if (campaign != null) {
            campaign.setEvent();
        }

        if (internalSearch != null) {
            internalSearch.setEvent();
        }

        if (aisle != null) {
            aisle.setEvent();
        }

        if (cart != null) {
            cart.setEvent();
        }

        if (order != null) {
            order.setEvent();
        }

        if (customTreeStructure != null) {
            customTreeStructure.setEvent();
        }

        if (customObjectsMap != null) {
            for (CustomObject co : customObjectsMap.values()) {
                co.setEvent();
            }
        }

        if (customVarsMap != null) {
            for (CustomVar cv : customVarsMap.values()) {
                cv.setEvent();
            }
        }

        if (selfPromotionImpressionsMap != null) {
            for (SelfPromotionImpression spi : selfPromotionImpressionsMap.values()) {
                spi.setEvent();
            }
        }

        if (publisherImpressionsMap != null) {
            for (PublisherImpression pi : publisherImpressionsMap.values()) {
                pi.setEvent();
            }
        }
    }
}
