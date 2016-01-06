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

import org.json.JSONException;
import org.json.JSONObject;

public class TVTracking {

    /**
     * Tracker instance
     */
    private final Tracker tracker;

    /**
     * URL
     */
    private String campaignURL;

    /**
     * Visit Duration
     */
    private int visitDuration;

    /**
     * Get the Campaign URL
     *
     * @return String
     */
    public String getCampaignURL() {
        return campaignURL;
    }

    /**
     * Get visit duration
     *
     * @return int
     */
    public int getVisitDuration() {
        return visitDuration;
    }

    /**
     * Constructor
     *
     * @param tracker Tracker
     */
    TVTracking(Tracker tracker) {
        this.tracker = tracker;
        visitDuration = 10;

        String url = (String) tracker.getConfiguration().get(TrackerKeys.TVT_URL);
        if (!TextUtils.isEmpty(url)) {
            campaignURL = url;
        } else {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.warning, "TVTracking URL not set");
        }

        Integer visit = Integer.parseInt(String.valueOf(tracker.getConfiguration().get(TrackerKeys.TVT_VISIT_DURATION)));
        if (visit != 0) {
            visitDuration = visit;
        }

        try {
            String remanentCampaign = tracker.getPreferences().getString(TrackerKeys.REMANENT_CAMPAIGN_SAVED, null);
            if (remanentCampaign != null) {
                JSONObject remanentObject = new JSONObject(remanentCampaign);
                int lifetime;
                if (remanentObject.get("lifetime") instanceof String) {
                    lifetime = Integer.parseInt((String) remanentObject.get("lifetime"));
                } else {
                    lifetime = (Integer) remanentObject.get("lifetime");
                }
                long savedLifetime = tracker.getPreferences().getLong(TrackerKeys.REMANENT_CAMPAIGN_TIME_SAVED, 0);

                if (Tool.getDaysBetweenTimes(System.currentTimeMillis(), savedLifetime) >= lifetime) {
                    tracker.getPreferences().edit().putString(TrackerKeys.REMANENT_CAMPAIGN_SAVED, null).apply();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set TVTracking data
     *
     * @return Tracker
     */
    public Tracker set() {
        if (PluginParam.get(tracker).containsKey(Hit.HitParam.TVT.stringValue())) {
            ParamOption options = new ParamOption().setPersistent(true).setEncode(true);
            tracker.setParam(Hit.HitParam.TVT.stringValue(), true, options);
        } else {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.warning, "TVTracking not enabled");
        }
        return tracker;
    }

    /**
     * Set TVTracking data
     *
     * @param campaignURL String
     * @return Tracker
     */
    public Tracker set(String campaignURL) {
        this.campaignURL = campaignURL;
        return set();
    }

    /**
     * Set TVTracking data
     *
     * @param campaignURL   String
     * @param visitDuration int
     * @return Tracker
     */
    public Tracker set(String campaignURL, int visitDuration) {
        this.visitDuration = visitDuration;
        return set(campaignURL);
    }

    /**
     * Remove tvt parameter
     */
    public void unset() {
        tracker.unsetParam(Hit.HitParam.TVT.stringValue());
    }
}
