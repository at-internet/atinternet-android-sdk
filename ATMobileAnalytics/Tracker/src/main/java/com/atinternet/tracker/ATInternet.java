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

import android.text.TextUtils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Use this class to manage tracker instances
 */
public final class ATInternet {

    /**
     * Enum for different encryption mode
     */
    public enum EncryptionMode {
        /**
         * No encryption stored data
         */
        none,
        /**
         * encryption stored data enabled if device is compatible
         */
        ifCompatible,
        /**
         * /!\ encryption stored data enable AND if not data not stored
         */
        force
    }

    /**
     * Overlay permission Activity result code
     */
    // Version number when this code appairs
    public static final int ALLOW_OVERLAY_INTENT_RESULT_CODE = 230;

    static final String TAG = "ATINTERNET";

    private static final ATInternet instance = new ATInternet();

    private final ConcurrentHashMap<String, Tracker> trackers = new ConcurrentHashMap<>();

    private String userAgent;
    private String applicationVersion;

    private ATInternet() {
    }

    /**
     * Set ATInternet database path
     */
    public static void setDatabasePath(String path) {
        Storage.setDatabasePath(path);
    }

    /***
     * Get ATInternet database path
     * @return Database path
     */
    public static String getDatabasePath() {
        return Storage.getDatabasePath();
    }


    /***
     * Set ATInternet encryption mode
     * @param encryptionMode EncryptionMode
     */
    public static void setEncryptionMode(EncryptionMode encryptionMode) {
        Crypt.setEncryptionMode(encryptionMode);
    }

    /**
     * Get ATInternet encryption mode
     *
     * @return Encryption mode
     */
    public static EncryptionMode getEncryptionMode() {
        return Crypt.getEncryptionMode();
    }

    /**
     * Disable user identification
     *
     * @param enabled /
     */
    public static void optOut(android.content.Context context, final boolean enabled) {
        TechnicalContext.optOut(context, enabled);
    }

    /**
     * Get "optOut" value
     *
     * @return true if "optOut" is enabled
     */
    public static boolean optOutEnabled(android.content.Context context) {
        return TechnicalContext.optOutEnabled(context);
    }

    /**
     * Get ATInternet singleton
     *
     * @return the ATInternet singleton object
     */
    public static ATInternet getInstance() {
        return instance;
    }

    /**
     * Get default tracker
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
            registerTracker(trackerName, new Tracker(false));
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
            registerTracker(trackerName, new Tracker(context, false));
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
            registerTracker(trackerName, new Tracker(configuration, false));
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
            registerTracker(trackerName, new Tracker(context, configuration, false));
        }
        return trackers.get(trackerName);
    }

    void registerTracker(String trackerName, Tracker t) {
        if (!TextUtils.isEmpty(userAgent)) {
            t.setUserAgent(userAgent);
        }
        if (!TextUtils.isEmpty(applicationVersion)) {
            t.setApplicationVersion(applicationVersion);
        }
        trackers.putIfAbsent(trackerName, t);
    }

    /***
     * Get custom application version
     * @return String
     */
    public String getApplicationVersion() {
        if (!TextUtils.isEmpty(applicationVersion)) {
            return applicationVersion;
        }
        return TechnicalContext.getApplicationVersion();
    }

    /***
     * Method to override application version for all trackers
     * @param apvr new application version
     */
    public void setApplicationVersion(String apvr) {
        applicationVersion = apvr;
        for (Tracker t : trackers.values()) {
            t.setApplicationVersion(applicationVersion);
        }
    }

    /***
     * Get user agent
     * @return String
     */
    public String getUserAgent() {
        if (!TextUtils.isEmpty(userAgent)) {
            return userAgent;
        }
        return TechnicalContext.getDefaultUserAgent();
    }

    /***
     * Method to override user agent for all trackers
     * @param ua new user agent
     */
    public void setUserAgent(String ua) {
        userAgent = ua;
        for (Tracker t : trackers.values()) {
            t.setUserAgent(ua);
        }
    }
}
