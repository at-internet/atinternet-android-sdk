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
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

@TargetApi(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class TrackerActivityLifeCyle implements Application.ActivityLifecycleCallbacks {

    private static final int DELTA = 2;

    private long timeInBackground;
    private Configuration configuration;
    private String savedActivityName;
    private int savedActivityTaskId;

    TrackerActivityLifeCyle(Configuration configuration) {
        timeInBackground = -1;
        this.configuration = configuration;
        savedActivityName = null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedActivityName == null || !activity.getClass().getCanonicalName().equals(savedActivityName)
                || activity.getTaskId() == savedActivityTaskId) {
            timeInBackground = -1;
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (timeInBackground > -1) {
            int sessionBackgroundDuration = Integer.parseInt(String.valueOf(configuration.get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION)));
            if (Tool.getSecondsBetweenTimes(System.currentTimeMillis(), timeInBackground) >= (sessionBackgroundDuration < DELTA ? DELTA : sessionBackgroundDuration)) {
                LifeCycle.newSessionInit(Tracker.getPreferences());
                timeInBackground = -1;
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Required by interface
    }

    @Override
    public void onActivityPaused(Activity activity) {
        savedActivityName = activity.getClass().getCanonicalName();
        savedActivityTaskId = activity.getTaskId();
        timeInBackground = System.currentTimeMillis();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // Required by interface
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Required by interface
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Required by interface
    }
}
