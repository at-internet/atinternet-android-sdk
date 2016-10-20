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

public class SelfPromotion extends OnAppAd {
    private static final String SELF_PROMOTION_FORMAT = "INT-%1$s-%2$s||%3$s";
    private static final String SCREEN = "screen";
    private static final String AD_TRACKING = "AT";

    private int adId;
    private String format;
    private String productId;

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private CustomObjects customObjects;

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        return customObjectsMap == null ? (customObjectsMap = new LinkedHashMap<>()) : customObjectsMap;
    }

    /**
     * Get ad id
     *
     * @return int
     */
    public int getAdId() {
        return adId;
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
     * Get product id
     *
     * @return String
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Set a new ad id
     *
     * @param adId int
     * @return SelfPromotion
     */
    public SelfPromotion setAdId(int adId) {
        this.adId = adId;
        return this;
    }

    /**
     * Set a new format
     *
     * @param format String
     * @return SelfPromotion
     */
    public SelfPromotion setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Set a new product id
     *
     * @param productId String
     * @return SelfPromotion
     */
    public SelfPromotion setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Set a new action
     *
     * @param action OnAppAd.Action
     * @return SelfPromotion
     */
    public SelfPromotion setAction(Action action) {
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

    SelfPromotion(Tracker tracker) {
        super(tracker);
        adId = -1;
    }

    @Override
    void setEvent() {
        String selfPromotion = String.format(SELF_PROMOTION_FORMAT,
                adId,
                format != null ? format : "",
                productId != null ? productId : "");

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

        tracker.setParam(action.stringValue(), selfPromotion, new ParamOption().setAppend(true).setEncode(true));
    }
}
