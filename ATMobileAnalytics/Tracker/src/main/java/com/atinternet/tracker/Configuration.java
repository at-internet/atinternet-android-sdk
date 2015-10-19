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

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;

/**
 * Hit configuration
 */
class Configuration extends LinkedHashMap<String, Object> {

    /**
     * Properties to get configuration from json file in archive
     */
    private static final String ENCODING = "UTF-8";
    private static final String PHONE_CONFIGURATION = "phone";
    private static final String TABLET_CONFIGURATION = "tablet";
    private static final String JSON_FILE = "defaultConfiguration.json";

    /**
     * Configuration defined by UI
     */
    Configuration(Context context) {
        JSONObject jsonObject = getDefaultConfiguration(context);
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    put(key, jsonObject.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // FIXME TESTS ONLY

    /**
     * Override configuration
     *
     * @param configuration HashMap
     */
    Configuration(HashMap<String, Object> configuration) {
        clear();
        putAll(configuration);
    }

    /**
     * Get configuration defined in the UI
     *
     * @param context Context
     * @return JSONObject
     */
    private JSONObject getDefaultConfiguration(Context context) {
        JSONObject result = new JSONObject();
        String stringResult;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + JSON_FILE);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            stringResult = new String(buffer, ENCODING);
            JSONObject json = new JSONObject(stringResult);
            if (isTablet(context)) {
                result = json.getJSONObject(TABLET_CONFIGURATION);
            } else {
                result = json.getJSONObject(PHONE_CONFIGURATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                result.put("log", "logp")
                        .put("logSSL", "logs")
                        .put("domain", "xiti.com")
                        .put("pixelPath", "/hit.xiti")
                        .put("site", 552987)
                        .put("secure", false)
                        .put("identifier", "androidId")
                        .put("enableCrashDetection", true)
                        .put("plugins", "tvtracking")
                        .put("storage", "required")
                        .put("hashUserId", false)
                        .put("persistIdentifiedVisitor", true)
                        .put("tvtURL", "")
                        .put("tvtVisitDuration", 10)
                        .put("tvtSpotValidityTime", 5)
                        .put("campaignLastPersistence", false)
                        .put("campaignLifetime", 30);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

        return result;
    }

    /**
     * Device is tablet
     *
     * @param context Context
     * @return boolean
     */
    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) >= SCREENLAYOUT_SIZE_LARGE;
    }
}
