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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * Plugin for partners
 */
abstract class Plugin {

    /**
     * Call Partner response
     */
    String response = new JSONObject().toString();

    /**
     * Tracker instance
     */
    Tracker tracker;

    /**
     * Execute call partner
     *
     * @param tracker Tracker
     */
    protected abstract void execute(Tracker tracker);

    /**
     * Get the response
     *
     * @return String
     */
    String getResponse() {
        return response;
    }
}

class PluginParam {

    static HashMap<String, String> get(Tracker tracker) {
        HashMap<String, String> dictionary = new HashMap<>();
        ArrayList<String> plugins = new ArrayList<>(Arrays.asList(((String) tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS)).split(",")));

        if (plugins.contains("tvtracking")) {
            dictionary.put(Hit.HitParam.TVT.stringValue(), TVTrackingPlugin.class.getName());
        }
        return dictionary;
    }
}

/**
 * TVTracking plugin
 */
class TVTrackingPlugin extends Plugin {

    /**
     * Constant timeout
     */
    private static final int TIMEOUT = 5000;

    /**
     * Strings constant
     */
    private static final String DIRECT = "direct";
    private static final String REMANENT = "remanent";
    private static final String INFO = "info";
    private static final String VERSION = "version";
    private static final String VERSION_TVT_CODE = "1.2.2m";
    private static final String MESSAGE = "message";
    private static final String ERROR = "errors";
    private static final String TVTRACKING = "tvtracking";
    private static final String UNDEFINED = "undefined";
    private static final String PRIORITY = "priority";
    private static final String CHANNEL = "channel";
    private static final String TIME = "time";
    private static final String LIFETIME = "lifetime";

    private static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    enum TVTrackingStatusCase {
        channelUndefined, noChannel, noData, timeError, ok
    }

    @Override
    protected void execute(Tracker tracker) {
        this.tracker = tracker;
        try {
            if (sessionIsExpired()) {
                setDirectCampaignToRemanent();

                URL url = new URL(tracker.TVTracking().getCampaignURL());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(TIMEOUT);
                connection.setDoInput(true);
                connection.connect();
                response = getTvTrackingResponse(stringifyTvTResponse(connection), connection);
                connection.disconnect();
            } else {
                response = getTvTrackingResponse(Tracker.getPreferences().getString(TrackerConfigurationKeys.DIRECT_CAMPAIGN_SAVED, null), null);
            }
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.partner, "TV Tracking : " + response);
            Tracker.getPreferences().edit().putLong(TrackerConfigurationKeys.LAST_TVT_EXECUTE_TIME, System.currentTimeMillis()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Formatting data from HTTPResponse content
     *
     * @param connection HttpURLConnection
     * @return String
     */
    private String stringifyTvTResponse(HttpURLConnection connection) throws IOException {
        String result = null;
        if (connection.getResponseCode() == 200) {
            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                result = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
            }
        }
        return result;
    }

    /**
     * Get formatted JSON TVTracking response
     *
     * @param directCampaign String
     * @param connection     HttpURLConnection
     * @return String
     * @throws JSONException
     */
    private String getTvTrackingResponse(String directCampaign, HttpURLConnection connection) throws JSONException, IOException {
        JSONObject resultJson = new JSONObject();

        JSONObject tvtrackingObject = new JSONObject();
        JSONObject infoObject;
        String infoSaved = Tracker.getPreferences().getString(TrackerConfigurationKeys.INFO_CAMPAIGN_SAVED, null);

        // if tvt send data
        if (directCampaign != null) {
            Object[] checkResult = checkCampaign(new JSONObject(directCampaign));
            switch ((TVTrackingStatusCase) checkResult[0]) {
                case noChannel:
                    // invalid data
                    infoObject = putInfos(infoSaved, TVTrackingStatusCase.noChannel, connection, null);
                    break;
                case channelUndefined:
                    // no diffusion
                    infoObject = putInfos(infoSaved, TVTrackingStatusCase.channelUndefined, connection, null);
                    break;
                case timeError:
                    // Bad time
                    infoObject = putInfos(infoSaved, TVTrackingStatusCase.timeError, connection, checkResult);
                    break;
                default:
                    // valid campaign
                    JSONObject directObject = (JSONObject) checkResult[1];
                    Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.DIRECT_CAMPAIGN_SAVED, directObject.toString()).apply();
                    tvtrackingObject.put(DIRECT, directObject);
                    infoObject = putInfos(infoSaved, TVTrackingStatusCase.ok, connection, checkResult);
                    break;
            }
        } else {
            infoObject = putInfos(infoSaved, TVTrackingStatusCase.noData, connection, null);
        }
        Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.INFO_CAMPAIGN_SAVED, infoObject.toString()).apply();

        String remanentCampaignSaved = Tracker.getPreferences().getString(TrackerConfigurationKeys.REMANENT_CAMPAIGN_SAVED, null);
        if (remanentCampaignSaved != null) {
            JSONObject remanentObject = new JSONObject(remanentCampaignSaved);
            tvtrackingObject.put(REMANENT, remanentObject);
        }

        tvtrackingObject.put(INFO, infoObject);
        return resultJson.put(TVTRACKING, tvtrackingObject).toString();
    }

    /**
     * Set direct campaign to remanent campaignt
     *
     * @throws JSONException
     */
    private void setDirectCampaignToRemanent() throws JSONException {
        String directCampaign = Tracker.getPreferences().getString(TrackerConfigurationKeys.DIRECT_CAMPAIGN_SAVED, null);
        if (directCampaign != null) {
            JSONObject directObject = new JSONObject(directCampaign);
            String remanentCampaign = Tracker.getPreferences().getString(TrackerConfigurationKeys.REMANENT_CAMPAIGN_SAVED, null);
            if (remanentCampaign != null) {
                JSONObject remanentObject = new JSONObject(remanentCampaign);
                int directPriority;
                int remanentPriority;
                if (directObject.get(PRIORITY) instanceof String) {
                    directPriority = Integer.parseInt((String) directObject.get(PRIORITY));
                } else {
                    directPriority = (Integer) directObject.get(PRIORITY);
                }
                if (remanentObject.get(PRIORITY) instanceof String) {
                    remanentPriority = Integer.parseInt((String) remanentObject.get(PRIORITY));
                } else {
                    remanentPriority = (Integer) remanentObject.get(PRIORITY);
                }
                if (directPriority == 1 || (directPriority == 0 && remanentPriority == 1)) {
                    Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.REMANENT_CAMPAIGN_SAVED, directCampaign).apply();
                }
            } else {
                Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.REMANENT_CAMPAIGN_SAVED, directCampaign).apply();
            }
            Tracker.getPreferences().edit().putLong(TrackerConfigurationKeys.REMANENT_CAMPAIGN_TIME_SAVED, System.currentTimeMillis())
                    .putString(TrackerConfigurationKeys.DIRECT_CAMPAIGN_SAVED, null)
                    .apply();
        }
        Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.INFO_CAMPAIGN_SAVED, null).apply();
    }

    /**
     * Check if campaign is valid
     *
     * @param response JSONObject
     * @return JSONObject
     * @throws JSONException
     */
    private Object[] checkCampaign(JSONObject response) throws JSONException {
        // invalid data
        if (!response.toString().contains(CHANNEL)) {
            return new Object[]{TVTrackingStatusCase.noChannel, null};
        }
        // no diffusion
        else if (response.get(CHANNEL).equals(UNDEFINED)) {
            return new Object[]{TVTrackingStatusCase.channelUndefined, null};
        } else {
            if (response.get(PRIORITY).equals(UNDEFINED)) {
                response.put(PRIORITY, "1");
            }
            if (response.get(LIFETIME).equals(UNDEFINED)) {
                response.put(LIFETIME, "30");
            }

            String partnerTime = (String) response.get(TIME);
            SimpleDateFormat sdf;
            Date date = null;

            // UTC
            try {
                sdf = new SimpleDateFormat(UTC_DATE_FORMAT, Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                date = sdf.parse(partnerTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date == null || Tool.getMinutesBetweenTimes(System.currentTimeMillis(), date.getTime()) > Integer.parseInt(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_SPOT_VALIDITY_TIME)))) {
                return new Object[]{TVTrackingStatusCase.timeError, partnerTime};
            }
        }
        return new Object[]{TVTrackingStatusCase.ok, response};
    }

    /**
     * Return true if session is expired
     *
     * @return boolean
     */
    private boolean sessionIsExpired() {
        long lastHitSentTime = Tracker.getPreferences().getLong(TrackerConfigurationKeys.LAST_TVT_EXECUTE_TIME, 0);
        return Tool.getSecondsBetweenTimes(System.currentTimeMillis(), lastHitSentTime) >= tracker.TVTracking().getVisitDuration() * 60;
    }

    /**
     * Insert info object in tvt response
     *
     * @param infoSaved  String
     * @param status     TVTrackingStatusCase
     * @param connection HttpURLConnection
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject putInfos(String infoSaved, TVTrackingStatusCase status, HttpURLConnection connection, Object[] checkResult) throws JSONException, IOException {
        // if session is not expired
        if (infoSaved != null) {
            return new JSONObject(infoSaved);
        } else {
            JSONObject infoObject = new JSONObject();
            switch (status) {
                case noChannel:
                    // No channel
                    return infoObject.put(VERSION, VERSION_TVT_CODE)
                            .put(MESSAGE, String.valueOf(connection.getResponseCode()))
                            .put(ERROR, "noChannel");
                case channelUndefined:
                    // Invalid campaign
                    return infoObject.put(VERSION, VERSION_TVT_CODE)
                            .put(MESSAGE, String.valueOf(connection.getResponseCode()) + "-channelUndefined")
                            .put(ERROR, "");
                case noData:
                    // No campaign
                    return infoObject.put(VERSION, VERSION_TVT_CODE)
                            .put(MESSAGE, String.valueOf(connection.getResponseCode()))
                            .put(ERROR, "noData");
                case timeError:
                    // Bad time
                    return infoObject.put(VERSION, VERSION_TVT_CODE)
                            .put(MESSAGE, String.valueOf(connection.getResponseCode()) + "-" + String.valueOf(checkResult[1]))
                            .put(ERROR, "timeError");
                default:
                    // Valid campaign
                    return infoObject.put(VERSION, VERSION_TVT_CODE)
                            .put(MESSAGE, String.valueOf(connection.getResponseCode()))
                            .put(ERROR, "");
            }
        }
    }
}

