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
import java.util.Date;

public class Hit {

    public enum HitParam {
        Screen("p"),
        Level2("s2"),
        JSON("stc"),
        UserId("idclient"),
        Action("action"),
        Refstore("refstore"),
        HitType("type"),
        Touch("click"),
        TouchScreen("pclick"),
        OnAppAdTouchScreen("patc"),
        TouchLevel2("s2click"),
        OnAppAdTouchLevel2("s2atc"),
        VisitorIdentifierNumeric("an"),
        VisitorIdentifierText("at"),
        VisitorCategory("ac"),
        BackgroundMode("bg"),
        OnAppAdsTouch("atc"),
        OnAppAdsImpression("ati"),
        GPSLatitude("gy"),
        GPSLongitude("gx"),
        TVT("tvt"),
        Source("xto"),
        RemanentSource("xtor");

        private final String str;

        HitParam(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    public enum HitType {
        Audio, Video, Animation, PodCast, RSS, Email, Publicite, Touch, AdTracking, ProduitImpression, Weborama, MvTesting, Screen
    }

    /**
     * Url
     */
    private final String url;

    /**
     * Date of creation
     */
    private Date date;

    /**
     * Number of retry that were made to dispatch the url
     */
    private int retry;

    /**
     * Boolean is offline hit
     */
    private boolean isOffline;

    /**
     * Default constructor
     *
     * @param url String
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
     * @param url       String
     * @param date      Date
     * @param retry     int
     * @param isOffline boolean
     */
    public Hit(String url, Date date, int retry, boolean isOffline) {
        this(url);
        this.date = date;
        this.retry = retry;
        this.isOffline = isOffline;
    }

    /**
     * Get the url
     *
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Get the date of the hit
     *
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the retry number
     *
     * @return int
     */
    public int getRetry() {
        return retry;
    }

    /**
     * Get the offline property
     *
     * @return boolean
     */
    public boolean isOffline() {
        return isOffline;
    }

    /**
     * Get the hit type
     *
     * @return HitType
     */
    static HitType getHitType(final ArrayList<Param> volatileParams, final ArrayList<Param> persistentParams) {
        ArrayList<Param> buffer = new ArrayList<Param>() {{
            addAll(volatileParams);
            addAll(persistentParams);
        }};

        HitType type = HitType.Screen;

        for (Param p : buffer) {
            if (p.getKey().equals("clic") || p.getKey().equals("click") || (p.getKey().equals("type") && Lists.getProcessedTypes().containsKey(p.getValue().execute()))) {
                if (p.getKey().equals("type")) {
                    type = Lists.getProcessedTypes().get(p.getValue().execute());
                    break;
                }

                if (p.getKey().equals("clic") || p.getKey().equals("click")) {
                    type = HitType.Touch;
                }
            }
        }
        return type;
    }

    /**
     * Get the type for offline type
     *
     * @return HitType
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
