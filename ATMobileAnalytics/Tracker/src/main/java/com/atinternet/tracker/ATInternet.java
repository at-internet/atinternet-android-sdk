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

import android.app.Application;

import java.util.HashMap;

public class ATInternet extends Application {

    public static final int ALLOW_OVERLAY_INTENT_RESULT_CODE = 230; // Version number when this code appairs

    private final HashMap<String, Tracker> trackers = new HashMap<>();

    /**
     * Get the default tracker
     *
     * @return Tracker
     */
    public Tracker getDefaultTracker() {
        return getTracker("defaultTracker");
    }

    /**
     * Get a tracker with name
     *
     * @param trackerName String
     * @return Tracker
     */
    public Tracker getTracker(String trackerName) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker(this);
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }

    /**
     * Get a tracker with name and a set the configuration
     *
     * @param trackerName   String
     * @param configuration HashMap
     * @return Tracker
     */
    public Tracker getTracker(String trackerName, HashMap<String, Object> configuration) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker(this, configuration);
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }
}
