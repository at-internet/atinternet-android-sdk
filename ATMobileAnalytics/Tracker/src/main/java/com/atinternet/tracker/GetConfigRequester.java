package com.atinternet.tracker;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetConfigRequester implements Runnable {

    private static final String EXPIRATION_TS_CONFIG_KEY = "ATExpirationTSConfig";
    private static final String AUTOTRACK_CONFIGURATION_KEY = "ATAutoTrackConfiguration";
    private static final int CONFIGURATION_TIME_TO_LIVE = 3600;
    private static final int CONFIGURATION_TIME_TO_LIVE_RANDOM = 1200;

    private String endpoint;

    GetConfigRequester(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void run() {
        try {
            String conf = "{}";
            if (!TextUtils.isEmpty(endpoint)) {
                int timeToLive = -1;
                if (AutoTracker.getInstance().isEnabledAutoTracking()) {
                    timeToLive = CONFIGURATION_TIME_TO_LIVE + (int) (Math.random() * CONFIGURATION_TIME_TO_LIVE_RANDOM);
                }
                if (AutoTracker.getInstance().isEnabledLiveTagging()) {
                    timeToLive = 0;
                }
                if (timeToLive != -1) {
                    long lastTs = AutoTracker.getPreferences().getLong(EXPIRATION_TS_CONFIG_KEY, -1);
                    long now = System.currentTimeMillis() / 1000;
                    if (timeToLive == 0 || lastTs <= now) {
                        StringBuilder result = new StringBuilder();
                        HttpURLConnection urlConnection;
                        URL url = new URL(endpoint);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            conf = result.toString();

                            long newTs = now + timeToLive;
                            AutoTracker.getPreferences().edit().putLong(EXPIRATION_TS_CONFIG_KEY, newTs)
                                    .putString(AUTOTRACK_CONFIGURATION_KEY, conf)
                                    .apply();
                        } else {
                            conf = AutoTracker.getPreferences().getString(AUTOTRACK_CONFIGURATION_KEY, "{}");
                        }

                    } else {
                        conf = AutoTracker.getPreferences().getString(AUTOTRACK_CONFIGURATION_KEY, "{}");
                    }
                }
            }
            AutoTracker.getInstance().setAutoTrackingConfiguration(new JSONObject(conf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
