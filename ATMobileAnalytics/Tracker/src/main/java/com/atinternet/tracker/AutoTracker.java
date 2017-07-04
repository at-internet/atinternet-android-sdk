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

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONObject;

/**
 * Wrapper class to use auto tracking feature
 */
@TargetApi(14)
public class AutoTracker extends Tracker {

    private static AutoTracker instance = null;

    private GetConfigRequester getConfigRequester;
    private JSONObject autoTrackingConfiguration;
    private SmartTrackerActivityLifecycle smartTrackerActivityLifecycle;
    private String token;
    private App app;
    private boolean enabledLiveTagging;
    private boolean enabledAutoTracking;
    private String appVersion;

    private SmartSender smartSender;
    private Endpoints endpoints;

    static AutoTracker getInstance(String... args) {
        return instance == null ? instance = new AutoTracker(args) : instance;
    }

    AutoTracker(String... args) {
        super();
        Context context = appContext.get();

        token = retrieveValidToken(args);
        appVersion = getAppVersion(context);
        app = new App(token, appVersion);

        endpoints = new Endpoints(String.valueOf(configuration.get("atEnv")), token, appVersion);
        smartSender = new SmartSender(token, endpoints.getResourceEndpoint(Endpoints.Resource.Socket));
        WindowManager wm = (WindowManager) context.getSystemService(android.content.Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        float scale = UI.getScale(d);
        smartTrackerActivityLifecycle = new SmartTrackerActivityLifecycle(configuration, smartSender, scale);

        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(smartTrackerActivityLifecycle);
        }
    }

    String getToken() {
        return token;
    }

    JSONObject getConf() {
        return autoTrackingConfiguration;
    }

    App getApplication() {
        return app;
    }

    SmartSender getSmartSender() {
        return smartSender;
    }

    boolean isEnabledLiveTagging() {
        return enabledLiveTagging;
    }

    boolean isEnabledAutoTracking() {
        return enabledAutoTracking;
    }

    void setAutoTrackingConfiguration(JSONObject conf) {
        autoTrackingConfiguration = conf;
    }

    private String retrieveValidToken(String... args) {
        String tkn = args.length > 0 ? args[0] : null;
        if (tkn == null) {
            return String.valueOf(configuration.get(TrackerConfigurationKeys.TOKEN));
        }
        return tkn;
    }

    private String getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Enable/Disable LiveTagging
     *
     * @param enabled /
     */
    public void enableLiveTagging(boolean enabled) {
        enabledLiveTagging = enabled;
        if (enabledLiveTagging) {
            smartSender.init();
        } else {
            smartSender.disconnect();
        }
    }

    /**
     * Enable/Disable auto tracking
     *
     * @param enabled /
     */
    public void enableAutoTracking(boolean enabled) {
        enabledAutoTracking = enabled;
        if (enabledAutoTracking && getConfigRequester == null) {
            getConfigRequester = new GetConfigRequester(endpoints.getResourceEndpoint(Endpoints.Resource.GetConfig));
            smartTrackerActivityLifecycle.setGetConfigRequester(getConfigRequester);
            TrackerQueue.getInstance().put(getConfigRequester);
        }
    }
}