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

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    /**
     * Get campaign id
     *
     * @return String
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * Get creation
     *
     * @return String
     */
    public String getCreation() {
        return creation;
    }

    /**
     * Get variant
     *
     * @return String
     */
    public String getVariant() {
        return variant;
    }

    /**
     * Get format
     *
     * @return String
     */
    public String getFormat() {
        return format;
    }

    /**
     * Get general placement
     *
     * @return String
     */
    public String getGeneralPlacement() {
        return generalPlacement;
    }

    /**
     * Get detailed placement
     *
     * @return String
     */
    public String getDetailedPlacement() {
        return detailedPlacement;
    }

    /**
     * Get advertiser id
     *
     * @return String
     */
    public String getAdvertiserId() {
        return advertiserId;
    }

    /**
     * Get url
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set a new campaign id
     *
     * @param campaignId String
     * @return Publisher
     */
    public Publisher setCampaignId(String campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    /**
     * Set a new creation
     *
     * @param creation String
     * @return Publisher
     */
    public Publisher setCreation(String creation) {
        this.creation = creation;
        return this;
    }

    /**
     * Set variant
     *
     * @param variant String
     * @return Pubisher
     */
    public Publisher setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    /**
     * Set format
     *
     * @param format String
     * @return Publisher
     */
    public Publisher setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Set a new general placement
     *
     * @param generalPlacement String
     * @return Publisher
     */
    public Publisher setGeneralPlacement(String generalPlacement) {
        this.generalPlacement = generalPlacement;
        return this;
    }

    /**
     * Set a new detailed placement
     *
     * @param detailedPlacement String
     * @return Publisher
     */
    public Publisher setDetailedPlacement(String detailedPlacement) {
        this.detailedPlacement = detailedPlacement;
        return this;
    }

    /**
     * Set a new advertiser id
     *
     * @param advertiserId String
     * @return Publisher
     */
    public Publisher setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
        return this;
    }

    /**
     * Set a new url
     *
     * @param url String
     * @return Publisher
     */
    public Publisher setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Set a new action
     *
     * @param action OnAppAd.Action
     * @return Publisher
     */
    public Publisher setAction(Action action) {
        this.action = action;
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

    Publisher(Tracker tracker) {
        super(tracker);
        campaignId = "";
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
        ArrayList<int[]> indexes = Tool.findParameterPosition(Hit.HitParam.HitType.stringValue(), buffer.getVolatileParams(), buffer.getPersistentParams());
        String currentType = "";

        for (int[] index : indexes) {
            int arrayID = index[0];
            int itemPosition = index[1];
            if (arrayID == 0) {
                currentType = buffer.getVolatileParams().get(itemPosition).getValue().execute();
            } else {
                currentType = buffer.getPersistentParams().get(itemPosition).getValue().execute();
            }
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
