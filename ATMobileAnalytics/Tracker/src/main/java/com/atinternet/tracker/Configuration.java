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

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Class to manage tracker configuration
 */
public class Configuration extends LinkedHashMap<String, Object> {

    private static final String ENCODING = "UTF-8";
    private static final String PHONE_CONFIGURATION = "phone";
    private static final String TABLET_CONFIGURATION = "tablet";
    private static final String JSON_FILE = "defaultConfiguration.json";

    Configuration(Context context) {
        JSONObject jsonObject = getDefaultConfiguration(Tool.isTablet(context));
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    put(key, jsonObject.get(key));
                } catch (JSONException e) {
                    Log.e(ATInternet.TAG, e.toString());
                }
            }
        }
    }

    Configuration(HashMap<String, Object> configuration) {
        JSONObject jsonObject = getDefaultConfiguration(false);
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    put(key, jsonObject.get(key));
                } catch (JSONException e) {
                    Log.e(ATInternet.TAG, e.toString());
                }
            }
        }
        for (Entry<String, Object> entry : configuration.entrySet()) {
            String key = entry.getKey();
            put(key, configuration.get(key));
        }
    }

    private JSONObject getDefaultConfiguration(boolean isTablet) {
        JSONObject result = new JSONObject();
        String stringResult;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + JSON_FILE);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            if (inputStream.read(buffer) != -1) {
                stringResult = new String(buffer, ENCODING);
                JSONObject json = new JSONObject(stringResult);
                if (isTablet) {
                    result = json.getJSONObject(TABLET_CONFIGURATION);
                } else {
                    result = json.getJSONObject(PHONE_CONFIGURATION);
                }
            }
            inputStream.close();
        } catch (Exception e) {
            try {
                result.put(TrackerConfigurationKeys.LOG, "")
                        .put(TrackerConfigurationKeys.LOG_SSL, "")
                        .put(TrackerConfigurationKeys.DOMAIN, "xiti.com")
                        .put(TrackerConfigurationKeys.PIXEL_PATH, "/hit.xiti")
                        .put(TrackerConfigurationKeys.SITE, "")
                        .put(TrackerConfigurationKeys.IDENTIFIER, "androidId")
                        .put(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION, true)
                        .put(TrackerConfigurationKeys.PLUGINS, "")
                        .put(TrackerConfigurationKeys.STORAGE, "never")
                        .put(TrackerConfigurationKeys.HASH_USER_ID, false)
                        .put(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR, true)
                        .put(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE, false)
                        .put(TrackerConfigurationKeys.CAMPAIGN_LIFETIME, 30)
                        .put(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION, 60)
                        .put(TrackerConfigurationKeys.AUTO_SALES_TRACKER, false)
                        .put(TrackerConfigurationKeys.IGNORE_LIMITED_AD_TRACKING, false)
                        .put(TrackerConfigurationKeys.SEND_HIT_WHEN_OPT_OUT, true)
                        .put(TrackerConfigurationKeys.MAX_HIT_SIZE, 8_000)
                        .put(TrackerConfigurationKeys.UUID_DURATION, 397)
                        .put(TrackerConfigurationKeys.UUID_EXPIRATION_MODE, "fixed")
                        .put(TrackerConfigurationKeys.PROXY_TYPE, "none")
                        .put(TrackerConfigurationKeys.PROXY_ADDRESS, "");
            } catch (JSONException e1) {
                Log.e(ATInternet.TAG, e1.toString());
            }
        }

        return result;
    }

    /**
     * Stringify configuration
     *
     * @return the pretty printed configuration
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tracker configuration : \n");
        Iterator<Entry<String, Object>> iter = entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            sb.append("\t").append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append('\n');
            }
        }
        return sb.toString();

    }
}
