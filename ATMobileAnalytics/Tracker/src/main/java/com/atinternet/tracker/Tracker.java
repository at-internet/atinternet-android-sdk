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
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.atinternet.tracker.Param.Type;
import static com.atinternet.tracker.Tool.CallbackType;

public class Tracker {

    /**
     * Enum for different offline mode
     */
    public enum OfflineMode {
        never, required, always
    }

    /**
     * Enum for different identifier
     */
    public enum IdentifierType {
        androidId, advertisingId, UUID
    }

    /**
     * Enum for different plugin
     */
    public enum PluginKey {
        tvtracking, nuggad
    }

    /**
     * Context
     */
    private static WeakReference<android.content.Context> appContext;

    /**
     * Crash Default Handler
     */
    private static Thread.UncaughtExceptionHandler defaultCrashHandler;

    /**
     * Listener for callbacks
     */
    private TrackerListener listener;

    /**
     * Listener for callbacks
     */
    private Dispatcher dispatcher;

    /**
     * Contains parameters
     */
    private Buffer buffer;

    /**
     * User Id member
     */
    private String internalUserId;

    /**
     * Contains Tracker configuration
     */
    private Configuration configuration;

    /**
     * Storage offline hits
     */
    private static Storage storage;

    /**
     * Business Object
     */
    private final LinkedHashMap<String, BusinessObject> businessObjects = new LinkedHashMap<>();

    /**
     * Screen tracking
     */
    private Screens screens;

    /**
     * Gestures tracking
     */
    private Gestures gestures;

    /**
     * Event tracking
     */
    private Event event;

    /**
     * Context tracking
     */
    private Context context;

    /**
     * TV tracking
     */
    private TVTracking tvTracking;

    /**
     * NuggAd
     */
    private NuggAds nuggAds;

    /**
     * Offline management
     */
    private Offline offline;

    /**
     * Custom Objects
     */
    private CustomObjects customObjects;

    /**
     * IdentifiedVisitor tracking
     */
    private IdentifiedVisitor identifiedVisitor;

    /**
     * Publisher tracking
     */
    private Publishers publishers;

    /**
     * SelfPromotion tracking
     */
    private SelfPromotions selfPromotions;

    /**
     * GPS tracking
     */
    private Locations locations;

    /**
     * Custom variables
     */
    private CustomVars customVars;

    /**
     * Orders
     */
    private Orders orders;

    /**
     * Cart
     */
    private Cart cart;

    /**
     * Aisles
     */
    private Aisles aisles;

    /**
     * Campaigns
     */
    private Campaigns campaigns;

    /**
     * InternalSearches
     */
    private InternalSearches internalSearches;

    /**
     * DynamicScreens
     */
    private DynamicScreens dynamicScreens;

    /**
     * Custom tree Structures
     */
    private CustomTreeStructures customTreeStructures;

    /**
     * Products
     */
    private Products products;

    /**
     * Players
     */
    private MediaPlayers mediaPlayers;

    /**
     * Boolean to detect if activity lifecycle tracking is enabled
     */
    private static boolean isTrackerActivityLifeCycleEnabled = false;

    /**
     * Get the current configuration
     *
     * @return LinkedHashMap
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Get the listener of the tracker instance
     *
     * @return TrackerListener
     */
    public TrackerListener getListener() {
        return listener;
    }

    /**
     * Asynchronous method to get user ID for webview
     *
     * @param callback UserIdCallback
     */
    public void getUserId(final UserIdCallback callback) {
        TrackerQueue.getInstance().put(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    String userID = TechnicalContext.getUserId((String) configuration.get(TrackerConfigurationKeys.IDENTIFIER)).execute();
                    if ((Boolean) configuration.get(TrackerConfigurationKeys.HASH_USER_ID) && !doNotTrackEnabled()) {
                        callback.receiveUserId(Tool.SHA_256(userID));
                    } else {
                        callback.receiveUserId(userID);
                    }
                } else {
                    Tool.executeCallback(listener, CallbackType.warning, "Enabled to get user id");
                }
            }
        });
    }

    /**
     * Get user id (require one hit sent)
     *
     * @return String
     */
    public String getUserIdSync() {
        if (internalUserId == null) {
            Tool.executeCallback(listener, CallbackType.warning, "User id must be set");
        }
        return internalUserId;
    }

    /**
     * Get Screen instance
     *
     * @return Screens
     */
    public Screens Screens() {
        return screens == null ? (screens = new Screens(this)) : screens;
    }

    /**
     * Get Gestures instance
     *
     * @return Gestures
     */
    public Gestures Gestures() {
        return gestures == null ? (gestures = new Gestures(this)) : gestures;
    }

    /**
     * Get Event instance
     *
     * @return Event
     */
    Event Event() {
        return event == null ? (event = new Event(this)) : event;
    }

    /**
     * Get Offline instance
     *
     * @return Offline
     */
    public Offline Offline() {
        return offline == null ? (offline = new Offline(this)) : offline;
    }

    /**
     * Get Context instance
     *
     * @return Context
     */
    public Context Context() {
        return context == null ? (context = new Context(this)) : context;
    }

    /**
     * Get TVTracking instance
     *
     * @return TVTracking
     */
    public TVTracking TVTracking() {
        return tvTracking == null ? (tvTracking = new TVTracking(this)) : tvTracking;
    }

    /**
     * Get NuggAd instance
     *
     * @return NuggAd
     */
    public NuggAds NuggAds() {
        return nuggAds == null ? (nuggAds = new NuggAds(this)) : nuggAds;
    }

    /**
     * Get Custom objects instance
     *
     * @return CustomObjects
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    /**
     * Get IdentifiedVisitor instance
     *
     * @return IdentifiedVisitor
     */
    public IdentifiedVisitor IdentifiedVisitor() {
        return identifiedVisitor == null ? (identifiedVisitor = new IdentifiedVisitor(this)) : identifiedVisitor;
    }

    /**
     * Get Publishers instance
     *
     * @return Publishers
     */
    public Publishers Publishers() {
        return publishers == null ? (publishers = new Publishers(this)) : publishers;
    }

    /**
     * Get SelfPromotions instance
     *
     * @return SelfPromotions
     */
    public SelfPromotions SelfPromotions() {
        return selfPromotions == null ? (selfPromotions = new SelfPromotions(this)) : selfPromotions;
    }

    /**
     * Get DynamicLabels instance
     *
     * @return DynamicScreens
     */
    public DynamicScreens DynamicScreens() {
        return dynamicScreens == null ? (dynamicScreens = new DynamicScreens(this)) : dynamicScreens;
    }

    /**
     * Get Products instance
     *
     * @return Products
     */
    public Products Products() {
        return products == null ? (products = new Products(this)) : products;
    }

    /**
     * Get Cart instance
     *
     * @return Cart
     */
    public Cart Cart() {
        return cart == null ? (cart = new Cart(this)) : cart;
    }

    /**
     * Get Players instance
     *
     * @return Players
     */
    public MediaPlayers Players() {
        return mediaPlayers == null ? (mediaPlayers = new MediaPlayers(this)) : mediaPlayers;
    }

    /**
     * Get GPS instance
     *
     * @return GPS
     * @deprecated Since 2.3.0, Location is now only available as a screen object property.
     */
    @Deprecated
    public Locations Locations() {
        return locations == null ? (locations = new Locations(this)) : locations;
    }

    /**
     * Get CustomVars instance
     *
     * @return CustomVars
     * @deprecated Since 2.3.0, CustomVars is now only available as a screen object property.
     */
    @Deprecated
    public CustomVars CustomVars() {
        return customVars == null ? (customVars = new CustomVars(this)) : customVars;
    }

    /**
     * Get Aisles instance
     *
     * @return Aisles
     * @deprecated Since 2.3.0, Aisles is now only available as a screen object property.
     */
    @Deprecated
    public Aisles Aisles() {
        return aisles == null ? (aisles = new Aisles(this)) : aisles;
    }

    /**
     * Get Campaigns instance
     *
     * @return Campaigns
     * @deprecated Since 2.3.0, Campaign is now only available as a screen object property.
     */
    @Deprecated
    public Campaigns Campaigns() {
        return campaigns == null ? (campaigns = new Campaigns(this)) : campaigns;
    }

    /**
     * Get CustomTreeStructures instance
     *
     * @return CustomTreeStructures
     * @deprecated Since 2.3.0, CustomTreeStructure is now only available as a screen object property.
     */
    @Deprecated
    public CustomTreeStructures CustomTreeStructures() {
        return customTreeStructures == null ? (customTreeStructures = new CustomTreeStructures(this)) : customTreeStructures;
    }

    /**
     * Get InternalSearches instance
     *
     * @return InternalSearches
     * @deprecated Since 2.3.0, InternalSearch is now only available as a screen or gesture object property.
     */
    @Deprecated
    public InternalSearches InternalSearches() {
        return internalSearches == null ? (internalSearches = new InternalSearches(this)) : internalSearches;
    }

    /**
     * Get Orders instance
     *
     * @return Orders
     * @deprecated Since 2.3.0, Order is now only available as a screen object property.
     */
    @Deprecated
    public Orders Orders() {
        return orders == null ? (orders = new Orders(this)) : orders;
    }

    /**
     * Asynchronous method to set a new log
     *
     * @param log               String
     * @param setConfigCallback SetConfigCallback
     */
    public void setLog(String log, SetConfigCallback setConfigCallback) {
        if (TextUtils.isEmpty(log)) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for log, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.LOG, log, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new secured log
     *
     * @param securedLog        String
     * @param setConfigCallback SetConfigCallback
     */
    public void setSecuredLog(String securedLog, SetConfigCallback setConfigCallback) {
        if (TextUtils.isEmpty(securedLog)) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for secured log, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.LOG_SSL, securedLog, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new domain
     *
     * @param domain            String
     * @param setConfigCallback SetConfigCallback
     */
    public void setDomain(String domain, SetConfigCallback setConfigCallback) {
        if (TextUtils.isEmpty(domain)) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for domain, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.DOMAIN, domain, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new siteId
     *
     * @param siteId            int
     * @param setConfigCallback SetConfigCallback
     */
    public void setSiteId(int siteId, SetConfigCallback setConfigCallback) {
        if (siteId <= 0) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for site id, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.SITE, siteId, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new offline mode
     *
     * @param offlineMode       OfflineMode
     * @param setConfigCallback SetConfigCallback
     */
    public void setOfflineMode(OfflineMode offlineMode, SetConfigCallback setConfigCallback) {
        if (offlineMode == null) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for offline mode, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.OFFLINE_MODE, offlineMode.toString(), setConfigCallback);
        }
    }

    /**
     * Asynchronous method to enable secure mode
     *
     * @param enabled           boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setSecureModeEnabled(boolean enabled, SetConfigCallback setConfigCallback) {
        setConfig(TrackerConfigurationKeys.SECURE, enabled, setConfigCallback);
    }

    /**
     * Asynchronous method to set a new identifier type
     *
     * @param identifierType    IdentifierType
     * @param setConfigCallback SetConfigCallback
     */
    public void setIdentifierType(IdentifierType identifierType, SetConfigCallback setConfigCallback) {
        if (identifierType == null) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for identifier type, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.IDENTIFIER, identifierType.toString(), setConfigCallback);
        }
    }

    /**
     * Asynchronous method to enable hash user id
     *
     * @param enabled           boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setHashUserIdEnabled(boolean enabled, SetConfigCallback setConfigCallback) {
        setConfig(TrackerConfigurationKeys.HASH_USER_ID, enabled, setConfigCallback);
    }

    /**
     * Asynchronous method to set new plugins
     *
     * @param plugins           List
     * @param setConfigCallback SetConfigCallback
     */
    public void setPlugins(List<PluginKey> plugins, SetConfigCallback setConfigCallback) {
        if (plugins == null) {
            setConfig(TrackerConfigurationKeys.PLUGINS, "", setConfigCallback);
        } else {
            String value = "";
            boolean isFirst = true;
            for (PluginKey plugin : plugins) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    value += ",";
                }
                value += plugin.toString();
            }
            setConfig(TrackerConfigurationKeys.PLUGINS, value, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set new plugins
     *
     * @param pixelPath         String
     * @param setConfigCallback SetConfigCallback
     */
    public void setPixelPath(String pixelPath, SetConfigCallback setConfigCallback) {
        if (TextUtils.isEmpty(pixelPath)) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for pixel path, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.PIXEL_PATH, pixelPath, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to enable persistent identified visitor
     *
     * @param enabled           boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setPersistentIdentifiedVisitorEnabled(boolean enabled, SetConfigCallback setConfigCallback) {
        setConfig(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR, enabled, setConfigCallback);
    }

    /**
     * Asynchronous method to set a new TVTracking url
     *
     * @param url               String
     * @param setConfigCallback SetConfigCallback
     */
    public void setTvTrackingUrl(String url, SetConfigCallback setConfigCallback) {
        if (TextUtils.isEmpty(url)) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for tv tracking url, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.TVTRACKING_URL, url, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new TVTracking visit duration
     *
     * @param visitDuration     int
     * @param setConfigCallback SetConfigCallback
     */
    public void setTvTrackingVisitDuration(int visitDuration, SetConfigCallback setConfigCallback) {
        if (visitDuration <= 0) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for tv tracking visit duration, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.TVTRACKING_VISIT_DURATION, visitDuration, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new TVTracking spot validity time
     *
     * @param time              int
     * @param setConfigCallback SetConfigCallback
     */
    public void setTvTrackingSpotValidityTime(int time, SetConfigCallback setConfigCallback) {
        if (time <= 0) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for tv tracking spot validity time, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.TVTRACKING_SPOT_VALIDITY_TIME, time, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to enable crash detection
     *
     * @param enabled           boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setCrashDetectionEnabled(boolean enabled, SetConfigCallback setConfigCallback) {
        setConfig(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION, enabled, setConfigCallback);
    }

    /**
     * Asynchronous method to enable last persistence campaign
     *
     * @param enabled           boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setCampaignLastPersistenceEnabled(boolean enabled, SetConfigCallback setConfigCallback) {
        setConfig(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE, enabled, setConfigCallback);
    }

    /**
     * Asynchronous method to set a new campaign lifetime
     *
     * @param lifetime          int
     * @param setConfigCallback SetConfigCallback
     */
    public void setCampaignLifetime(int lifetime, SetConfigCallback setConfigCallback) {
        if (lifetime <= 0) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for campaign lifetime, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.CAMPAIGN_LIFETIME, lifetime, setConfigCallback);
        }
    }

    /**
     * Asynchronous method to set a new session background duration
     *
     * @param duration          int
     * @param setConfigCallback SetConfigCallback
     */
    public void setSessionBackgroundDuration(int duration, SetConfigCallback setConfigCallback) {
        if (duration <= 0) {
            Tool.executeCallback(listener, CallbackType.warning, "Bad value for session background duration, default value retained");
        } else {
            setConfig(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION, duration, setConfigCallback);
        }
    }

    /**
     * Create a default listener
     *
     * @return TrackerListener
     */
    public TrackerListener createDefaultTrackerListener() {
        return new TrackerListener() {
            @Override
            public void trackerNeedsFirstLaunchApproval(String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: First Launch \n\tMessage: " + message);
            }

            @Override
            public void buildDidEnd(HitStatus status, String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Building Hit \n\tStatus: " + status.toString() + "\n\tMessage: " + message);
            }

            @Override
            public void sendDidEnd(HitStatus status, String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Sending Hit \n\tStatus: " + status.toString() + "\n\tMessage: " + message);
            }

            @Override
            public void didCallPartner(String response) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Calling Partner \n\tResponse: " + response);
            }

            @Override
            public void warningDidOccur(String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Warning \n\tMessage: " + message);
            }

            @Override
            public void saveDidEnd(String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Saving Hit \n\tMessage: " + message);
            }

            @Override
            public void errorDidOccur(String message) {
                Log.d("ATINTERNET", "Debugging message: \n\tEvent: Error \n\tMessage: " + message);
            }
        };
    }

    /**
     * Asynchronous method to set a new configuration
     *
     * @param conf              HashMap
     * @param override          boolean
     * @param setConfigCallback SetConfigCallback
     */
    public void setConfig(final HashMap<String, Object> conf, final boolean override, final SetConfigCallback setConfigCallback) {
        TrackerQueue.getInstance().put(new Runnable() {
            @Override
            public void run() {
                if (override) {
                    configuration.clear();
                }
                Set<String> keys = conf.keySet();
                for (String key : keys) {
                    if (!Lists.getReadOnlyConfigs().contains(key)) {
                        configuration.put(key, conf.get(key));
                    } else {
                        Tool.executeCallback(listener, CallbackType.warning, "Cannot to overwrite " + key + " configuration");
                    }
                }
                refreshConfigurationDependencies();
                if (setConfigCallback != null) {
                    setConfigCallback.setConfigEnd();
                }
            }
        });
    }

    /**
     * Asynchronous method to change a configuration value for an existing key
     *
     * @param key               String
     * @param value             Object
     * @param setConfigCallback SetConfigCallback
     */
    public void setConfig(final String key, final Object value, final SetConfigCallback setConfigCallback) {
        TrackerQueue.getInstance().put(new Runnable() {
            @Override
            public void run() {
                if (!Lists.getReadOnlyConfigs().contains(key)) {
                    configuration.put(key, value);
                    refreshConfigurationDependencies();
                    if (setConfigCallback != null) {
                        setConfigCallback.setConfigEnd();
                    }
                } else {
                    Tool.executeCallback(listener, CallbackType.warning, "Cannot to overwrite " + key + " configuration");
                }
            }
        });
    }

    /**
     * Set a new listener
     *
     * @param trackerListener TrackerListener
     * @return Tracker
     */
    public Tracker setListener(TrackerListener trackerListener) {
        this.listener = trackerListener;
        if (getPreferences().getBoolean(LifeCycle.FIRST_SESSION, false)) {
            listener.trackerNeedsFirstLaunchApproval("Tracker First Launch");
        }
        return this;
    }

    /**
     * Set a default tracker listener
     *
     * @return Tracker
     */
    public Tracker setDefaultListener() {
        this.listener = createDefaultTrackerListener();
        if (getPreferences().getBoolean(LifeCycle.FIRST_SESSION, false)) {
            listener.trackerNeedsFirstLaunchApproval("Tracker First Launch");
        }
        return this;
    }

    /**
     * Init with default configuration
     *
     * @param context Context
     */
    public Tracker(android.content.Context context) {
        appContext = new WeakReference<>(context);
        configuration = new Configuration(appContext.get());
        initTracker();
        if (!LifeCycle.isInitialized) {
            LifeCycle.initLifeCycle(appContext.get());
        }
    }

    /**
     * Init with new configuration
     *
     * @param context       Context
     * @param configuration HashMap
     */
    public Tracker(android.content.Context context, final HashMap<String, Object> configuration) {
        appContext = new WeakReference<>(context);
        this.configuration = new Configuration(configuration);
        initTracker();
        if (!LifeCycle.isInitialized) {
            LifeCycle.initLifeCycle(appContext.get());
        }
    }

    /**
     * Enable or disable tracking
     *
     * @param enabled boolean
     */
    public static void doNotTrack(final boolean enabled) {
        TrackerQueue.getInstance().put(new Runnable() {
            @Override
            public void run() {
                TechnicalContext.doNotTrack(appContext.get(), enabled);
            }
        });
    }

    /**
     * Tracking enabled or disabled
     *
     * @return boolean
     */
    public static boolean doNotTrackEnabled() {
        return TechnicalContext.doNotTrackEnabled(appContext.get());
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value int
     * @return Tracker
     */
    public Tracker setParam(String key, int value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Integer);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   int
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, int value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Integer, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value float
     * @return Tracker
     */
    public Tracker setParam(String key, float value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Float);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   float
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, float value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Float, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value double
     * @return Tracker
     */
    public Tracker setParam(String key, double value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Double);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   double
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, double value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Double, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value boolean
     * @return Tracker
     */
    public Tracker setParam(String key, boolean value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Boolean);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   boolean
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, boolean value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Boolean, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value String
     * @return Tracker
     */
    public Tracker setParam(String key, String value) {
        if (value != null && Tool.parseJSON(value) == null) {
            return handleNotClosureStringParameterSetting(key, value, Type.String);
        } else {
            return handleNotClosureStringParameterSetting(key, value, Type.JSON);
        }
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   String
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, final String value, ParamOption options) {
        if (value != null && Tool.parseJSON(value) == null) {
            return handleNotClosureStringParameterSetting(key, value, Type.String, options);
        } else {
            return handleNotClosureStringParameterSetting(key, value, Type.JSON, options);
        }
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value List
     * @return Tracker
     */
    public Tracker setParam(String key, List value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Array);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   List
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, List value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Array, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value Object[]
     * @return Tracker
     */
    public Tracker setParam(String key, Object[] value) {
        return handleNotClosureStringParameterSetting(key, value, Type.Array);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   Object[]
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, Object[] value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.Array, options);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key   String
     * @param value Map
     * @return Tracker
     */
    public Tracker setParam(String key, Map value) {
        return handleNotClosureStringParameterSetting(key, value, Type.JSON);
    }

    /**
     * Add a parameter in the hit querystring
     *
     * @param key     String
     * @param value   Map
     * @param options ParamOption
     * @return Tracker
     */
    public Tracker setParam(String key, Map value, ParamOption options) {
        return handleNotClosureStringParameterSetting(key, value, Type.JSON, options);
    }

    /**
     * Remove a parameter from the hit querystring
     *
     * @param key String
     */
    public void unsetParam(String key) {
        ArrayList<int[]> positions = Tool.findParameterPosition(key, buffer.getPersistentParams(), buffer.getVolatileParams());

        // Check if parameter is already set in the buffer
        if (positions.size() > 0) {
            for (int[] indexTab : positions) {
                if (indexTab[0] == 0) {
                    buffer.getPersistentParams().remove(indexTab[1]);
                } else {
                    buffer.getVolatileParams().remove(indexTab[1]);
                }
            }
        }
    }

    /**
     * Send the built hit
     */
    public void dispatch() {
        if (businessObjects.size() > 0) {
            ArrayList<BusinessObject> onAppAds = new ArrayList<>();
            ArrayList<BusinessObject> customObjects = new ArrayList<>();
            ArrayList<BusinessObject> objects = new ArrayList<BusinessObject>() {{
                addAll(businessObjects.values());
            }};
            ArrayList<BusinessObject> screenObjects = new ArrayList<>();
            ArrayList<BusinessObject> salesTrackerObjects = new ArrayList<>();
            ArrayList<BusinessObject> internalSearchObjects = new ArrayList<>();
            ArrayList<BusinessObject> productsObjects = new ArrayList<>();

            for (BusinessObject businessObject : objects) {

                if (!(businessObject instanceof Product)) {
                    dispatchObjects(productsObjects, customObjects);
                }

                // Dispatch onAppAds before sending other object
                if (!(businessObject instanceof OnAppAd || businessObject instanceof ScreenInfo || businessObject instanceof AbstractScreen || businessObject instanceof InternalSearch || businessObject instanceof Cart || businessObject instanceof Order)
                        || (businessObject instanceof OnAppAd && ((OnAppAd) businessObject).getAction() == OnAppAd.Action.Touch)) {
                    dispatchObjects(onAppAds, customObjects);
                }

                if (businessObject instanceof OnAppAd) {
                    OnAppAd ad = (OnAppAd) businessObject;
                    if (ad.getAction() == OnAppAd.Action.View) {
                        onAppAds.add(ad);
                    } else {
                        customObjects.add(businessObject);
                        dispatcher.dispatch((BusinessObject[]) customObjects.toArray(new BusinessObject[customObjects.size()]));
                        customObjects.clear();
                    }
                } else if (businessObject instanceof CustomObject || businessObject instanceof NuggAd) {
                    customObjects.add(businessObject);
                } else if (businessObject instanceof ScreenInfo) {
                    screenObjects.add(businessObject);
                } else if (businessObject instanceof InternalSearch) {
                    internalSearchObjects.add(businessObject);
                } else if (businessObject instanceof Product) {
                    productsObjects.add(businessObject);
                } else if (businessObject instanceof Order || businessObject instanceof Cart) {
                    salesTrackerObjects.add(businessObject);
                } else if (businessObject instanceof AbstractScreen) {
                    onAppAds.addAll(customObjects);
                    onAppAds.addAll(screenObjects);
                    onAppAds.addAll(internalSearchObjects);

                    //Sales tracker
                    ArrayList<BusinessObject> orders = new ArrayList<>();
                    Cart cart = null;

                    for (BusinessObject obj : salesTrackerObjects) {
                        if (obj instanceof Cart) {
                            cart = (Cart) obj;
                        } else {
                            orders.add(obj);
                        }
                    }

                    if (cart != null && (((AbstractScreen) businessObject).isBasketScreen() || !orders.isEmpty())) {
                        onAppAds.add(cart);
                    }

                    onAppAds.addAll(orders);
                    onAppAds.add(businessObject);
                    dispatcher.dispatch((BusinessObject[]) onAppAds.toArray(new BusinessObject[onAppAds.size()]));

                    screenObjects.clear();
                    salesTrackerObjects.clear();
                    internalSearchObjects.clear();
                    onAppAds.clear();
                    customObjects.clear();
                } else {
                    if (businessObject instanceof Gesture && ((Gesture) businessObject).getAction() == Gesture.Action.InternalSearch) {
                        onAppAds.addAll(internalSearchObjects);
                        internalSearchObjects.clear();
                    }
                    onAppAds.addAll(customObjects);
                    onAppAds.add(businessObject);
                    dispatcher.dispatch((BusinessObject[]) onAppAds.toArray(new BusinessObject[onAppAds.size()]));

                    onAppAds.clear();
                    customObjects.clear();
                }
            }

            dispatchObjects(onAppAds, customObjects);

            dispatchObjects(productsObjects, customObjects);

            if (!customObjects.isEmpty() || !screenObjects.isEmpty() || !internalSearchObjects.isEmpty()) {
                customObjects.addAll(screenObjects);
                customObjects.addAll(internalSearchObjects);
                dispatcher.dispatch((BusinessObject[]) customObjects.toArray(new BusinessObject[customObjects.size()]));

                customObjects.clear();
                screenObjects.clear();
                internalSearchObjects.clear();
            }
        } else {
            dispatcher.dispatch();
        }
    }

    Buffer getBuffer() {
        return buffer;
    }

    static Storage getStorage() {
        return storage;
    }

    LinkedHashMap<String, BusinessObject> getBusinessObjects() {
        return businessObjects;
    }

    Dispatcher getDispatcher() {
        return dispatcher;
    }

    static android.content.Context getAppContext() {
        return appContext.get();
    }

    static SharedPreferences getPreferences() {
        return appContext.get().getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE);
    }

    String getInternalUserId() {
        return internalUserId;
    }

    void setInternalUserId(String internalUserId) {
        this.internalUserId = internalUserId;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setTrackerActivityLifecycle() {
        isTrackerActivityLifeCycleEnabled = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ((Application) appContext.get()).registerActivityLifecycleCallbacks(new TrackerActivityLifeCyle(configuration));
        }
    }

    private void initTracker() {
        try {
            listener = null;
            defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
            storage = new Storage(appContext.get());
            storage.setOfflineMode(Tool.convertStringToOfflineMode((String) configuration.get(TrackerConfigurationKeys.OFFLINE_MODE)));
            buffer = new Buffer(this);
            dispatcher = new Dispatcher(this);
            if ((Boolean) configuration.get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION) && !(Thread.getDefaultUncaughtExceptionHandler() instanceof CrashDetectionHandler)) {
                Thread.setDefaultUncaughtExceptionHandler(new CrashDetectionHandler(appContext.get(), defaultCrashHandler));
            }
            getPreferences().edit().putBoolean(TrackerConfigurationKeys.CAMPAIGN_ADDED_KEY, false).apply();

            if (!isTrackerActivityLifeCycleEnabled) {
                setTrackerActivityLifecycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Tracker setParam(String key, Closure value, Type type) {
        // Check whether the parameter is not in read only mode
        if (!Lists.getReadOnlyParams().contains(key)) {
            Param p = new Param(key, value, type);
            ArrayList<int[]> positions = Tool.findParameterPosition(key, buffer.getPersistentParams(), buffer.getVolatileParams());

            // Check if parameter is already set
            if (!positions.isEmpty()) {
                // If found, change parameter value in appropriate buffer array
                boolean isFirst = true;
                for (int[] indexTab : positions) {
                    int idArray = indexTab[0];
                    int position = indexTab[1];
                    if (isFirst) {
                        if (idArray == 0) {
                            buffer.getPersistentParams().set(position, p);
                        } else {
                            buffer.getVolatileParams().set(position, p);
                        }
                        isFirst = false;
                    } else if (idArray == 0) {
                        buffer.getPersistentParams().remove(position);
                    } else {
                        buffer.getVolatileParams().remove(position);
                    }
                }
            } else {
                // If not found, append parameter to volatile buffer
                buffer.getVolatileParams().add(p);
            }
        } else {
            Tool.executeCallback(listener, CallbackType.warning, String.format("Param %s is read only. Value will not be updated", key));
        }

        return this;
    }

    private Tracker setParam(String key, Closure value, Type type, ParamOption options) {
        // Check whether the parameter is not in read only mode
        if (!Lists.getReadOnlyParams().contains(key)) {
            Param p = new Param(key, value, type, options);
            ArrayList<int[]> positions = Tool.findParameterPosition(key, buffer.getPersistentParams(), buffer.getVolatileParams());

            if (options.isAppend()) {
                // Check if parameter is already set
                for (int[] indexTab : positions) {
                    int idArray = indexTab[0];
                    int index = indexTab[1];
                    // If new parameter is set to be persistent we move old parameters into the right buffer array
                    if (options.isPersistent() && idArray == 1) {
                        // If old parameter was in volatile buffer, we place it into the persistent buffer
                        Param existingParam = buffer.getVolatileParams().remove(index);
                        buffer.getPersistentParams().add(existingParam);
                    } else if (idArray == 0) {
                        Param existingParam = buffer.getPersistentParams().remove(index);
                        buffer.getVolatileParams().add(existingParam);
                    }
                }
                if (options.isPersistent()) {
                    buffer.getPersistentParams().add(p);
                } else {
                    buffer.getVolatileParams().add(p);
                }
            } else {
                // Check if parameter is already set
                if (!positions.isEmpty()) {
                    boolean isFirst = true;
                    // If found, replace first parameter with new value and delete others in appropriate buffer array
                    for (int[] indexTab : positions) {
                        int idArray = indexTab[0];
                        int index = indexTab[1];
                        if (isFirst) {
                            if (idArray == 0) {
                                if (options.isPersistent()) {
                                    buffer.getPersistentParams().set(index, p);
                                } else {
                                    buffer.getPersistentParams().remove(index);
                                    buffer.getVolatileParams().add(p);
                                }
                            } else {
                                if (options.isPersistent()) {
                                    buffer.getVolatileParams().remove(index);
                                    buffer.getPersistentParams().add(p);
                                } else {
                                    buffer.getVolatileParams().set(index, p);
                                }
                            }
                            isFirst = false;
                        } else if (idArray == 0) {
                            buffer.getPersistentParams().remove(index);
                        } else {
                            buffer.getVolatileParams().remove(index);
                        }
                    }
                } else if (options.isPersistent()) {
                    buffer.getPersistentParams().add(p);
                } else {
                    buffer.getVolatileParams().add(p);
                }
            }
        } else {
            Tool.executeCallback(listener, CallbackType.warning, String.format("Param %s is read only. Value will not be updated", key));
        }

        return this;
    }

    Tracker setParam(String key, Closure value) {
        return setParam(key, value, Type.Closure);
    }

    Tracker setParam(String key, Closure value, ParamOption options) {
        return setParam(key, value, Type.Closure, options);
    }

    private void refreshConfigurationDependencies() {
        String identifierKey = String.valueOf(configuration.get(TrackerConfigurationKeys.IDENTIFIER));
        String offlineMode = String.valueOf(configuration.get(TrackerConfigurationKeys.OFFLINE_MODE));
        boolean enableCrashDetectionHandler = Boolean.parseBoolean(String.valueOf(configuration.get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION)));

        if (!TextUtils.isEmpty(identifierKey)) {
            buffer.setIdentifierKey(identifierKey);
        }
        if (!TextUtils.isEmpty(offlineMode)) {
            storage.setOfflineMode(Tool.convertStringToOfflineMode(offlineMode));
        }

        if (enableCrashDetectionHandler) {
            if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CrashDetectionHandler)) {
                Thread.setDefaultUncaughtExceptionHandler(new CrashDetectionHandler(appContext.get(), defaultCrashHandler));
            }
        } else {
            if (Thread.getDefaultUncaughtExceptionHandler() instanceof CrashDetectionHandler) {
                Thread.setDefaultUncaughtExceptionHandler(defaultCrashHandler);
            }
        }
    }

    private Tracker handleNotClosureStringParameterSetting(String key, final Object value, Type type, final ParamOption... options) {
        Closure stringValue = new Closure() {
            @Override
            public String execute() {
                return Tool.convertToString(value, options.length > 0 ? options[0].getSeparator() : ",");
            }
        };
        if (options.length > 0) {
            return setParam(key, stringValue, type, options[0]);
        } else {
            return setParam(key, stringValue, type);
        }
    }

    private void dispatchObjects(ArrayList<BusinessObject> objects, ArrayList<BusinessObject> customObjects) {
        if (!objects.isEmpty()) {
            objects.addAll(customObjects);
            dispatcher.dispatch((BusinessObject[]) objects.toArray(new BusinessObject[objects.size()]));
            customObjects.clear();
            objects.clear();
        }
    }
}
