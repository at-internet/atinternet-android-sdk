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

/**
 * Tracker Configuration Keys constants
 */
public class TrackerConfigurationKeys {

    private TrackerConfigurationKeys() {
        throw new IllegalStateException("Utility class");
    }

    // Key representing the namespace used for preferences
    static final String PREFERENCES = "ATPreferencesKey";

    //Global key representing if is install hit sent
    static final String IS_FIRST_AFTER_INSTALL_HIT_KEY = "ATIsFirstAfterInstallHit";

    //Global key representing if is first hit sent
    static final String CAMPAIGN_ADDED_KEY = "ATCampaignAdded";

    //Global key representing if user wants tracked or not
    static final String OPT_OUT_ENABLED = "ATDoNotTrackEnabled";

    //Global key representing campaign marketing saved
    static final String MARKETING_CAMPAIGN_SAVED = "ATMarketingCampaignSaved";

    //Global key representing campaign marketing date saved
    static final String LAST_MARKETING_CAMPAIGN_TIME = "ATLastMarketingCampaignTime";

    //Global key representing date of last hit sent
    static final String REFERRER = "ATReferrer";

    //Global key representing idclient from uuid
    static final String IDCLIENT_UUID = "ATIdclientUUID";

    /**
     * Constant for storage offline mode configuration key
     */
    public static final String OFFLINE_MODE = "storage";

    /**
     * Constant for identifier configuration
     */
    public static final String IDENTIFIER = "identifier";

    /**
     * Constant for hash use rid configuration
     */
    public static final String HASH_USER_ID = "hashUserId";

    /**
     * Constant for enableCrashDetection configuration
     */
    public static final String ENABLE_CRASH_DETECTION = "enableCrashDetection";

    /**
     * Constant for secure configuration
     */
    public static final String SECURE = "secure";

    /**
     * Constant for log configuration
     */
    public static final String LOG = "log";

    /**
     * Constant for log secure configuration
     */
    public static final String LOG_SSL = "logSSL";

    /**
     * Constant for site configuration
     */
    public static final String SITE = "site";

    /**
     * Constant for pixel path configuration
     */
    public static final String PIXEL_PATH = "pixelPath";

    /**
     * Constant for domain configuration
     */
    public static final String DOMAIN = "domain";

    /**
     * Constant for persist identified visitor configuration
     */
    public static final String PERSIST_IDENTIFIED_VISITOR = "persistIdentifiedVisitor";

    /**
     * Constant for plugins configuration
     */
    public static final String PLUGINS = "plugins";

    /**
     * Constant for campaign remanence configuration
     */
    public static final String CAMPAIGN_LAST_PERSISTENCE = "campaignLastPersistence";

    /**
     * Constant for lifetime marketing campaign
     */
    public static final String CAMPAIGN_LIFETIME = "campaignLifetime";

    /**
     * Constant for session duration
     */
    public static final String SESSION_BACKGROUND_DURATION = "sessionBackgroundDuration";

    /**
     * Constant for enable auto SalesTracker (Sales insight)
     */
    public static final String AUTO_SALES_TRACKER = "autoSalesTracker";

    /**
     * Constant for collect domain configuration (events only)
     */
    public static final String COLLECT_DOMAIN = "collectDomain";

    /**
     * Constant for download SDK source
     */
    static final String DOWNLOAD_SOURCE = "downloadSource";

    /**
     * Constant for ignore limited AdTracking
     */
    public static final String IGNORE_LIMITED_AD_TRACKING = "ignoreLimitedAdTracking";
}
