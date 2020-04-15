/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

import android.text.TextUtils;

import java.util.LinkedHashMap;

/**
 * Wrapper class for advertising self promotion tracking
 */
public class SelfPromotion extends OnAppAd {
    private static final String SELF_PROMOTION_FORMAT = "INT-%1$s-%2$s||%3$s";
    private static final String AD_TRACKING = "AT";

    private int adId;
    private String format;
    private String productId;

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private CustomObjects customObjects;

    SelfPromotion(Tracker tracker) {
        super(tracker);
        adId = -1;
    }

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        if (customObjectsMap == null) {
            customObjectsMap = new LinkedHashMap<>();
        }
        return customObjectsMap;
    }

    /**
     * Get advertising identifier
     *
     * @return the advertising identifier
     */
    public int getAdId() {
        return adId;
    }

    /**
     * Get advertising format
     *
     * @return the advertising format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Get advertising product identifier
     *
     * @return the advertising product identifier
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Set a new advertising identifier
     *
     * @param adId advertising identifier
     * @return the SelfPromotion instance
     */
    public SelfPromotion setAdId(int adId) {
        this.adId = adId;
        return this;
    }

    /**
     * Set a new advertising format
     *
     * @param format advertising format
     * @return the SelfPromotion instance
     */
    public SelfPromotion setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * Set a new advertising product identifier
     *
     * @param productId advertising product identifier
     * @return the SelfPromotion instance
     */
    public SelfPromotion setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return the SelfPromotion instance
     */
    public SelfPromotion setAction(Action action) {
        this.action = action;
        return this;
    }

    /**
     * Get a wrapper for CustomObject management
     *
     * @return the CustomObjects instance
     */
    public CustomObjects CustomObjects() {
        if (customObjects == null) {
            customObjects = new CustomObjects(this);
        }
        return customObjects;
    }

    @Override
    void setParams() {
        String selfPromotion = String.format(SELF_PROMOTION_FORMAT,
                adId,
                format != null ? format : "",
                productId != null ? productId : "");

        if (!fromScreen) {
            tracker.setParam(Hit.HitParam.HitType.stringValue(), AD_TRACKING);
        }

        if (action == Action.Touch) {
            String sn = TechnicalContext.getScreenName();
            if (sn != null) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchScreen.stringValue(), sn, new ParamOption().setEncode(true));
            }

            int lvl2 = TechnicalContext.getLevel2();
            if (lvl2 >= 0) {
                tracker.setParam(Hit.HitParam.OnAppAdTouchLevel2.stringValue(), lvl2);
            }
        }

        if (customObjectsMap != null) {
            for (CustomObject co : customObjectsMap.values()) {
                co.setParams();
            }
        }

        tracker.setParam(action.stringValue(), selfPromotion, new ParamOption().setAppend(true).setEncode(true));
    }
}
