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

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Class to provide hit information
 */
public class Hit {

    /**
     * Enum with different hit parameter keys
     */
    public enum HitParam {
        /**
         * Aisle parameter key
         */
        Aisle("aisl"),
        /**
         * Screen parameter key
         */
        Screen("p"),
        /**
         * Level 2 parameter key
         */
        Level2("s2"),
        /**
         * Custom tree structure parameter key
         */
        CustomTreeStructure("ptype"),
        /**
         * Custom object parameter key
         */
        JSON("stc"),
        /**
         * User Id parameter key
         */
        UserId("idclient"),
        /**
         * Action parameter key
         */
        Action("action"),
        /**
         * Store referrer parameter key
         */
        Refstore("refstore"),
        /**
         * Referrer parameter key
         */
        Referrer("ref"),
        /**
         * Hit type parameter key
         */
        HitType("type"),
        /**
         * Touch gesture parameter key
         */
        Touch("click"),
        /**
         * Touched screen parameter key
         */
        TouchScreen("pclick"),
        /**
         * Advertising screen parameter key
         */
        OnAppAdTouchScreen("patc"),
        /**
         * Touched screen level 2 parameter key
         */
        TouchLevel2("s2click"),
        /**
         * Touched advertising level 2 parameter key
         */
        OnAppAdTouchLevel2("s2atc"),
        /**
         * Numeric visitor identifier parameter key
         */
        VisitorIdentifierNumeric("an"),
        /**
         * Textual visitor identifier parameter key
         */
        VisitorIdentifierText("at"),
        /**
         * Identified visitor category parameter key
         */
        VisitorCategory("ac"),
        /**
         * Background mode parameter key
         */
        BackgroundMode("bg"),
        /**
         * Touched advertising parameter key
         */
        OnAppAdsTouch("atc"),
        /**
         * Advertising impression parameter key
         */
        OnAppAdsImpression("ati"),
        /**
         * Location latitude parameter key
         */
        GPSLatitude("gy"),
        /**
         * Location longitude parameter key
         */
        GPSLongitude("gx"),
        /**
         * Enable TVTracking parameter key
         */
        TVT("tvt"),
        /**
         * New marketing campaign parameter key
         */
        Source("xto"),
        /**
         * RichMedia duration parameter key
         */
        MediaDuration("m1"),
        /**
         * Remanent marketing campaign parameter key
         */
        RemanentSource("xtor"),
        /**
         * Tp parameter key
         */
        Tp("tp"),
        /**
         * Product list parameter key
         */
        ProductList("pdtl"),
        /**
         * Dynamic screen identifier parameter key
         */
        DynamicScreenId("pid"),
        /**
         * Dynamic screen value parameter key
         */
        DynamicScreenValue("pchap"),
        /**
         * Dynamic screen date parameter key
         */
        DynamicScreenDate("pidt"),
        /**
         * Internal search keyword parameter key
         */
        InternalSearchKeyword("mc"),
        /**
         * Internal search result screen number parameter key
         */
        InternalSearchResultScreenNumber("np"),
        /**
         * Internal search result position parameter key
         */
        InternalSearchResultPosition("mcrg"),
        /**
         * Cart identifier parameter key
         */
        CartId("idcart"),
        /**
         * RichMedia level 2 parameter key
         */
        RichMediaLevel2("s2rich"),
        /**
         * RichMedia screen parameter key
         */
        RichMediaScreen("prich");

        private final String str;

        HitParam(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    /**
     * Enum with different hit types
     */
    public enum HitType {
        Audio, Video, Animation, PodCast, RSS, Email, Publicite, Touch, AdTracking, ProduitImpression, Weborama, MvTesting, Screen
    }

    private final String url;
    private Date date;
    private int retry;
    private boolean isOffline;

    /**
     * Default constructor
     *
     * @param url the complete hit
     */
    public Hit(String url) {
        this.url = url;
        date = null;
        retry = -1;
        isOffline = false;
    }

    /**
     * Constructor with parameters
     *
     * @param url       the complete hit
     * @param date      hit building date
     * @param retry     sending retry count
     * @param isOffline true if hit come from storage
     */
    public Hit(String url, Date date, int retry, boolean isOffline) {
        this(url);
        this.date = date;
        this.retry = retry;
        this.isOffline = isOffline;
    }

    /**
     * Get the hit
     *
     * @return the hit
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the hit date
     *
     * @return the hit date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the sending retry count
     *
     * @return the retry count
     */
    public int getRetry() {
        return retry;
    }

    /**
     * Get the "isOffline" property value
     *
     * @return true if hit come from storage
     */
    public boolean isOffline() {
        return isOffline;
    }

    static HitType getHitType(final LinkedHashMap<String, Param> volatileParams, final LinkedHashMap<String, Param> persistentParams) {
        LinkedHashMap<String, Param> buffer = new LinkedHashMap<String, Param>() {{
            putAll(volatileParams);
            putAll(persistentParams);
        }};

        HitType type = HitType.Screen;

        if (buffer.containsKey("clic") || buffer.containsKey("click")) {
            type = HitType.Touch;
        }

        Param typeParam;
        if ((typeParam = buffer.get("type")) != null) {
            HitType typeVal;
            if ((typeVal = Lists.getProcessedTypes().get(typeParam.getValues().get(0).execute())) != null) {
                type = typeVal;
            }
        }

        return type;
    }

    /**
     * Get the hit type
     *
     * @return the hit type
     */
    public HitType getHitType() {
        HitType type = HitType.Screen;

        if (!TextUtils.isEmpty(url)) {
            String[] hitComponents = url.split("&");

            for (int i = 1; i < hitComponents.length; i++) {
                String[] parameterComponents = hitComponents[i].split("=");

                if (parameterComponents[0].equals(HitParam.HitType.stringValue())) {
                    type = Lists.getProcessedTypes().get(parameterComponents[1]);
                    break;
                }

                if (parameterComponents[0].equals("clic") || parameterComponents[0].equals(HitParam.Touch.stringValue())) {
                    type = HitType.Touch;
                }
            }
        }
        return type;
    }
}
