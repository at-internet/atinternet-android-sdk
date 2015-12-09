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

import java.util.ArrayList;

public class Publisher extends OnAppAd {

    private static final String PUBLISHER_FORMAT = "PUB-%1$s-%2$s-%3$s-%4$s-%5$s-%6$s-%7$s-%8$s";
    private static final String SCREEN = "screen";
    private static final String AD_TRACKING = "AT";

    /**
     * CampaignId
     */
    private String campaignId;

    /**
     * Creation
     */
    private String creation;

    /**
     * Variant
     */
    private String variant;

    /**
     * Format
     */
    private String format;

    /**
     * General Placement
     */
    private String generalPlacement;

    /**
     * Detailed Placement
     */
    private String detailedPlacement;

    /**
     * AdvertiserId
     */
    private String advertiserId;

    /**
     * Url
     */
    private String url;

    public String getCampaignId() {
        return campaignId;
    }

    public String getCreation() {
        return creation;
    }

    public String getVariant() {
        return variant;
    }

    public String getFormat() {
        return format;
    }

    public String getGeneralPlacement() {
        return generalPlacement;
    }

    public String getDetailedPlacement() {
        return detailedPlacement;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public String getUrl() {
        return url;
    }

    public Publisher setCampaignId(String campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    public Publisher setCreation(String creation) {
        this.creation = creation;
        return this;
    }

    public Publisher setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    public Publisher setFormat(String format) {
        this.format = format;
        return this;
    }

    public Publisher setGeneralPlacement(String generalPlacement) {
        this.generalPlacement = generalPlacement;
        return this;
    }

    public Publisher setDetailedPlacement(String detailedPlacement) {
        this.detailedPlacement = detailedPlacement;
        return this;
    }

    public Publisher setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
        return this;
    }

    public Publisher setUrl(String url) {
        this.url = url;
        return this;
    }

    public Publisher setAction(Action action) {
        this.action = action;
        return this;
    }

    /**
     * Constructor
     *
     * @param tracker Tracker
     */
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
            if (TechnicalContext.screenName != null && !TechnicalContext.screenName.isEmpty()) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchScreen.stringValue(), TechnicalContext.screenName, new ParamOption().setEncode(true));
            }

            if (TechnicalContext.level2 > 0) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchLevel2.stringValue(), TechnicalContext.level2);
            }
        }

        if (action == Action.View) {
            tracker.setParam(action.stringValue(), publisher, new ParamOption().setAppend(true));
        } else {
            tracker.setParam(action.stringValue(), publisher, new ParamOption().setAppend(true).setEncode(true));
        }
    }
}
