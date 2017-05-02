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

/**
 * Abstract class to manage screen tracking
 */
public abstract class AbstractScreen extends BusinessObject {

    enum Action {
        View("view");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }


    protected String name;
    protected String chapter1;
    protected String chapter2;
    protected String chapter3;
    protected Action action;
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
     * Get the screen name
     *
     * @return the screen name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the action type
     *
     * @return the action type
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get the level 2
     *
     * @return the level 2
     */
    public int getLevel2() {
        return level2;
    }

    /**
     * Get the first chapter
     *
     * @return the first chapter
     */
    public String getChapter1() {
        return chapter1;
    }

    /**
     * Get the second chapter
     *
     * @return the second chapter
     */
    public String getChapter2() {
        return chapter2;
    }

    /**
     * Get the third chapter
     *
     * @return the third chapter
     */
    public String getChapter3() {
        return chapter3;
    }

    /**
     * Get "isBasketScreen" value
     *
     * @return true is screen is basket screen
     */
    public boolean isBasketScreen() {
        return isBasketScreen;
    }

    /**
     * Attach a cart to screen
     *
     * @param cart the cart instance
     */
    public void setCart(Cart cart) {
        this.cart = cart;
        isBasketScreen = this.cart != null;
    }

    /**
     * Attach location information to screen
     *
     * @param latitude  latitude x value
     * @param longitude longitude y value
     * @return the Location instance
     */
    public Location Location(double latitude, double longitude) {
        return location == null ? (location = new Location(tracker)
                .setLatitude(latitude)
                .setLongitude(longitude)) : location;
    }

    /**
     * Attach visited aisle information to screen
     *
     * @param level1 first aisle level
     * @return the Aisle instance
     */
    public Aisle Aisle(String level1) {
        return aisle == null ? (aisle = new Aisle(tracker).setLevel1(level1)) : aisle;
    }

    /**
     * Attach a custom tree structure to screen
     *
     * @param category1 first custom tree structure category
     * @return the CustomTreeStructure instance
     */
    public CustomTreeStructure CustomTreeStructure(int category1) {
        return customTreeStructure == null ? (customTreeStructure = new CustomTreeStructure(tracker).setCategory1(category1)) : customTreeStructure;
    }

    /**
     * Attach campaign information to screen
     *
     * @param campaignId campaign identifier
     * @return the Campaign identifier
     */
    public Campaign Campaign(String campaignId) {
        return campaign == null ? (campaign = new Campaign(tracker).setCampaignId(campaignId)) : campaign;
    }

    /**
     * Attach an order to screen
     *
     * @param orderId  order identifier
     * @param turnover order turnover
     * @return the Order instance
     * @deprecated Since 2.3.4, bug was revealed when this method is used. Use Orders tracker property instead.
     */
    @Deprecated
    public Order Order(String orderId, double turnover) {
        return order == null ? (order = new Order(tracker).setOrderId(orderId).setTurnover(turnover)) : order;
    }

    /**
     * Attach internal search information to screen
     *
     * @param keywordLabel       keyword has been searched
     * @param resultScreenNumber screen result number
     * @return the InternalSearch instance
     */
    public InternalSearch InternalSearch(String keywordLabel, int resultScreenNumber) {
        return internalSearch == null ? (internalSearch = new InternalSearch(tracker)
                .setKeyword(keywordLabel)
                .setResultScreenNumber(resultScreenNumber)) : internalSearch;
    }

    /**
     * Get a wrapper for CustomVar management
     *
     * @return CustomVars instance
     */
    public CustomVars CustomVars() {
        return customVars == null ? (customVars = new CustomVars(this)) : customVars;
    }

    /**
     * Get a wrapper for CustomObject management
     *
     * @return CustomObjects instance
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    /**
     * Get a wrapper for PublisherImpression management
     *
     * @return PublisherImpressions instance
     */
    public PublisherImpressions Publishers() {
        return publisherImpressions == null ? (publisherImpressions = new PublisherImpressions(this)) : publisherImpressions;
    }

    /**
     * Get a wrapper for SelfPromotionImpression management
     *
     * @return SelfPromotionImpressions instance
     */
    public SelfPromotionImpressions SelfPromotions() {
        return selfPromotionImpressions == null ? (selfPromotionImpressions = new SelfPromotionImpressions(this)) : selfPromotionImpressions;
    }

    /**
     * Send the screen view event
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
