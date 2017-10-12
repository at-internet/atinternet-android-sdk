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

import android.text.TextUtils;

import java.util.LinkedHashMap;

/**
 * Wrapper class for advertising publisher tracking
 */
public class Publisher extends OnAppAd {
    private static final String PUBLISHER_FORMAT = "PUB-%1$s-%2$s-%3$s-%4$s-%5$s-%6$s-%7$s-%8$s";
    private static final String SCREEN = "screen";
    private static final String AD_TRACKING = "AT";

    private String campaignId;
    private String creation;
    private String variant;
    private String format;
    private String generalPlacement;
    private String detailedPlacement;
    private String advertiserId;
    private String url;

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private CustomObjects customObjects;

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        return customObjectsMap == null ? (customObjectsMap = new LinkedHashMap<>()) : customObjectsMap;
    }

    Publisher(Tracker tracker) {
        super(tracker);
        campaignId = "";
    }

    /**
     * Get campaign identifier
     *
     * @return the publisher campaign identifier
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * Get creation
     *
     * @return the publisher creation
     */
    public String getCreation() {
        return creation;
    }

    /**
     * Get variant
     *
     * @return the publisher variant
     */
    public String getVariant() {
        return variant;
    }

    /**
     * Get format
     *
     * @return the publisher format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Get general placement
     *
     * @return the publisher general placement
     */
    public String getGeneralPlacement() {
        return generalPlacement;
    }

    /**
     * Get detailed placement
     *
     * @return the publisher detailed placement
     */
    public String getDetailedPlacement() {
        return detailedPlacement;
    }

    /**
     * Get advertiser identifier
     *
     * @return the publisher edvertiser identifier
     */
    public String getAdvertiserId() {
        return advertiserId;
    }

    /**
     * Get url
     *
     * @return the publisher url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set a new campaign id
     *
     * @param campaignId campaign identifier
     * @return Publisher instance
     */
    public Publisher setCampaignId(String campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    /**
     * Set a new creation
     *
     * @param creation /
     * @return Publisher instance
     */
    public Publisher setCreation(String creation) {
        this.creation = creation;
        return this;
    }

    /**
     * Set variant
     *
     * @param variant /
     * @return Publisher instance
     */
    public Publisher setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    /**
     * Set format
     *
     * @param format /
     * @return Publisher instance
     */
    public Publisher setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Set a new general placement
     *
     * @param generalPlacement /
     * @return Publisher instance
     */
    public Publisher setGeneralPlacement(String generalPlacement) {
        this.generalPlacement = generalPlacement;
        return this;
    }

    /**
     * Set a new detailed placement
     *
     * @param detailedPlacement /
     * @return Publisher instance
     */
    public Publisher setDetailedPlacement(String detailedPlacement) {
        this.detailedPlacement = detailedPlacement;
        return this;
    }

    /**
     * Set a new advertiser id
     *
     * @param advertiserId /
     * @return Publisher instance
     */
    public Publisher setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
        return this;
    }

    /**
     * Set a new url
     *
     * @param url /
     * @return Publisher instance
     */
    public Publisher setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return Publisher instance
     */
    public Publisher setAction(Action action) {
        this.action = action;
        return this;
    }

    /**
     * Get CustomObjects
     *
     * @return CustomObjects instance
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    @Override
    void setEvent() {
        String publisher = String.format(PUBLISHER_FORMAT,
                campaignId,
                creation != null ? creation : "",
                variant != null ? variant : "",
                format != null ? format : "",
                generalPlacement != null ? generalPlacement : "",
                detailedPlacement != null ? detailedPlacement : "",
                advertiserId != null ? advertiserId : "",
                url != null ? url : "");

        Buffer buffer = tracker.getBuffer();
        String currentType = "";
        if (buffer.getVolatileParams().containsKey(Hit.HitParam.HitType.stringValue())) {
            currentType = buffer.getVolatileParams().get(Hit.HitParam.HitType.stringValue()).getValues().get(0).execute();
        } else if (buffer.getPersistentParams().containsKey(Hit.HitParam.HitType.stringValue())) {
            currentType = buffer.getPersistentParams().get(Hit.HitParam.HitType.stringValue()).getValues().get(0).execute();
        }

        if (!currentType.equals(SCREEN) && !currentType.equals(AD_TRACKING)) {
            tracker.setParam(Hit.HitParam.HitType.stringValue(), AD_TRACKING);
        }

        if (action == Action.Touch) {
            if (!TextUtils.isEmpty(TechnicalContext.screenName)) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchScreen.stringValue(), TechnicalContext.screenName, new ParamOption().setEncode(true));
            }

            if (TechnicalContext.level2 > 0) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchLevel2.stringValue(), TechnicalContext.level2);
            }
        }

        if (customObjectsMap != null) {
            for (CustomObject co : customObjectsMap.values()) {
                co.setEvent();
            }
        }

        tracker.setParam(action.stringValue(), publisher, new ParamOption().setAppend(true).setEncode(true));
    }
}
