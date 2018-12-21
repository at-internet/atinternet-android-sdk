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

import android.content.SharedPreferences;

/**
 * Wrapper class for marketing campaign tracking
 */
public class Campaign extends ScreenInfo {

    private String campaignId;

    Campaign(Tracker tracker) {
        super(tracker);
        campaignId = null;
    }

    /**
     * Get the campaign identifier
     *
     * @return the campaign identifier
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * Set a new campaign identifier
     *
     * @param campaignId campaign identifier
     * @return the Campaign instance
     */
    public Campaign setCampaignId(String campaignId) {
        this.campaignId = campaignId;
        return this;
    }

    @Override
    void setParams() {
        SharedPreferences preferences = Tracker.getPreferences();
        String remanentMarketingCampaign = preferences.getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null);
        long campaignDate = preferences.getLong(TrackerConfigurationKeys.LAST_MARKETING_CAMPAIGN_TIME, -1);

        ParamOption encoding = new ParamOption().setEncode(true);
        tracker.setParam(Hit.HitParam.Source.stringValue(), campaignId, encoding);
        preferences.edit().putBoolean(TrackerConfigurationKeys.CAMPAIGN_ADDED_KEY, true).apply();

        if (remanentMarketingCampaign != null) {
            if (Tool.getDaysBetweenTimes(System.currentTimeMillis(), campaignDate) > (Integer.parseInt(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LIFETIME))))) {
                preferences.edit().putString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null).apply();
                remanentMarketingCampaign = null;
            } else {
                tracker.setParam(Hit.HitParam.RemanentSource.stringValue(), remanentMarketingCampaign, encoding);
            }
        } else {
            preferences.edit().putString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, campaignId)
                    .putLong(TrackerConfigurationKeys.LAST_MARKETING_CAMPAIGN_TIME, System.currentTimeMillis())
                    .apply();
        }

        if (((Boolean) tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE)) || remanentMarketingCampaign == null) {
            preferences.edit().putString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, campaignId)
                    .putLong(TrackerConfigurationKeys.LAST_MARKETING_CAMPAIGN_TIME, System.currentTimeMillis())
                    .apply();
        }
    }
}
