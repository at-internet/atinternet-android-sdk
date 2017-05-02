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

import java.util.HashMap;

/**
 * Use this class to manage tracker instances
 */
public class ATInternet {

    /**
     * Overlay permission Activity result code
     */
    public static final int ALLOW_OVERLAY_INTENT_RESULT_CODE = 230; // Version number when this code appairs

    private static ATInternet instance = new ATInternet();

    private ATInternet() {
    }

    /**
     * Get ATInternet singleton
     *
     * @return the ATInternet singleton object
     */
    public static ATInternet getInstance() {
        return instance;
    }

    private final HashMap<String, Tracker> trackers = new HashMap<>();

    /**
     * Get a tracker with "defaultTracker" name
     *
     * @return Tracker instance
     */
    public Tracker getDefaultTracker() {
        return getTracker("defaultTracker");
    }

    /**
     * Get a tracker with default configuration
     *
     * @param trackerName tracker identifier
     * @return a new Tracker or existing instance
     */
    public Tracker getTracker(String trackerName) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker();
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }

    /**
     * Get a tracker with default configuration
     *
     * @param context     current activity context
     * @param trackerName tracker identifier
     * @return a new Tracker or existing instance
     */
    public Tracker getTracker(android.content.Context context, String trackerName) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker(context);
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }

    /**
     * Get a tracker with custom configuration
     *
     * @param trackerName   tracker identifier
     * @param configuration custom configuration (see TrackerConfigurationKeys)
     * @return a new Tracker or existing instance
     */
    public Tracker getTracker(String trackerName, HashMap<String, Object> configuration) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker(configuration);
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }

    /**
     * Get a tracker with custom configuration
     *
     * @param context       current activity context
     * @param trackerName   tracker identifier
     * @param configuration custom configuration (see TrackerConfigurationKeys)
     * @return a new Tracker or existing instance
     */
    public Tracker getTracker(android.content.Context context, String trackerName, HashMap<String, Object> configuration) {
        if (!trackers.containsKey(trackerName)) {
            Tracker tracker = new Tracker(context, configuration);
            trackers.put(trackerName, tracker);
        }
        return trackers.get(trackerName);
    }
}
