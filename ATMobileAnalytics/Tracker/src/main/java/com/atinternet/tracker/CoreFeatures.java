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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

class CrashDetectionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Key representing if the app crashed or not
     */
    private static final String CRASH_DETECTION = "CrashDetection";

    /**
     * Key representing if the crash informations has been got
     */
    private static final String CRASH_RECOVERY_INFO = "CrashRecoveryInfo";

    /**
     * Key representing the last track screen before crash
     */
    private static final String CRASH_LAST_SCREEN = "CrashLastScreen";

    /**
     * Key representing where the app crashed
     */
    private static final String CRASH_CLASS_CAUSE = "CrashClassCause";

    /**
     * Key representing why the app crashed
     */
    private static final String CRASH_EXCEPTION_NAME = "CrashExceptionName";

    /**
     * Default Handler to show dialog
     */
    private final Thread.UncaughtExceptionHandler defaultHandler;

    /***
     * Package name
     */
    private final String packageName;

    /**
     * Shared preferences
     */
    private SharedPreferences preferences;

    /**
     * Last Track Screen
     */
    private static String lastScreen;

    /**
     * Default constructor
     *
     * @param packageName    String
     * @param defaultHandler Thread.UncaughtExceptionHandler
     */
    CrashDetectionHandler(String packageName, SharedPreferences prefs, Thread.UncaughtExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.preferences = prefs;
        this.packageName = packageName;
    }

    /**
     * Set the last screen name
     *
     * @param name String
     */

    static void setCrashLastScreen(String name) {
        lastScreen = name;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        String className = (throwable.getCause() != null) ? getClassNameException(throwable.getCause()) : getClassNameException(throwable);
        String exceptionName = (throwable.getCause() != null) ? throwable.getCause().getClass().getName() : throwable.getClass().getName();

        preferences.edit().putBoolean(CRASH_DETECTION, true)
                .putBoolean(CRASH_RECOVERY_INFO, false)
                .putString(CRASH_LAST_SCREEN, lastScreen != null ? lastScreen : "")
                .putString(CRASH_CLASS_CAUSE, className)
                .putString(CRASH_EXCEPTION_NAME, exceptionName != null ? exceptionName : "")
                .commit();
        defaultHandler.uncaughtException(thread, throwable);
    }

    /**
     * Get a dictionary className : stackTrace
     *
     * @return Closure
     */
    static Closure getCrashInformation(final SharedPreferences preferences) {
        return new Closure() {
            @Override
            public String execute() {
                if (!preferences.getBoolean(CRASH_DETECTION, false)) {
                    return new JSONObject().toString();
                }
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                map.put("lastscreen", preferences.getString(CRASH_LAST_SCREEN, ""));
                map.put("classname", preferences.getString(CRASH_CLASS_CAUSE, ""));
                map.put("error", preferences.getString(CRASH_EXCEPTION_NAME, ""));
                preferences.edit().putBoolean(CRASH_DETECTION, false).apply();
                try {
                    return new JSONObject().put("crash", new JSONObject(map)).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return new JSONObject().toString();
            }
        };
    }

    /**
     * Get the class name where the exception thrown
     *
     * @param t Throwable
     * @return String
     */
    private String getClassNameException(Throwable t) {
        for (StackTraceElement element : t.getStackTrace()) {
            if (element.getClassName().contains(packageName)) {
                return element.getClassName();
            }
        }
        return "";
    }

    static Map<String, String> getCrashInfo(SharedPreferences prefs) {
        Map<String, String> map = new HashMap<>();
        if (prefs.getBoolean(CRASH_RECOVERY_INFO, true)) {
            return map;
        }
        map.put("lastscreen", prefs.getString(CRASH_LAST_SCREEN, ""));
        map.put("classname", prefs.getString(CRASH_CLASS_CAUSE, ""));
        map.put("error", prefs.getString(CRASH_EXCEPTION_NAME, ""));
        prefs.edit().putBoolean(CRASH_RECOVERY_INFO, true).apply();
        return map;
    }
}

final class LifeCycle {

    /**
     * Key representing if it's first launch (backward compat)
     */
    private static final String AT_FIRST_LAUNCH = "ATFirstLaunch";

    /**
     * Key representing version code app
     */
    static final String VERSION_CODE_KEY = "VersionCode";

    /**
     * Key representing if it's first session
     */
    static final String FIRST_SESSION = "FirstLaunch";

    /**
     * Key representing if it's first session after update
     */
    static final String FIRST_SESSION_AFTER_UPDATE = "FirstLaunchAfterUpdate";

    /**
     * Key representing first session date
     */
    static final String FIRST_SESSION_DATE = "FirstLaunchDate";

    /**
     * Key representing first session date after update
     */
    private static final String FIRST_SESSION_DATE_AFTER_UPDATE = "FirstLaunchDateAfterUpdate";

    /**
     * Key representing last session date
     */
    private static final String LAST_SESSION_DATE = "LastLaunchDate";

    /**
     * Key representing the app session count
     */
    static final String SESSION_COUNT = "LaunchCount";

    /**
     * Key representing the app session count since update
     */
    static final String SESSION_COUNT_SINCE_UPDATE = "LaunchCountSinceUpdate";

    /**
     * Key representing count of days since first session
     */
    static final String DAYS_SINCE_FIRST_SESSION = "DaysSinceFirstLaunch";

    /**
     * Key representing count of days since first session after update
     */
    private static final String DAYS_SINCE_UPDATE = "DaysSinceFirstLaunchAfterUpdate";

    /**
     * Key representing count of days since last session
     */
    static final String DAYS_SINCE_LAST_SESSION = "DaysSinceLastUse";

    /**
     * String pattern to get fld and uld
     */
    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Check whether lifecycle has already been initialized
     */
    static boolean isInitialized;

    /**
     * Session identifier
     */
    private static String sessionId;

    /**
     * Version code
     */
    private static String versionCode;

    private LifeCycle() {
        throw new IllegalStateException("Private class");
    }

    /**
     * Init lifecycle
     *
     * @param context Context
     */
    static void initLifeCycle(Context context) {

        SharedPreferences preferences = Tracker.getPreferences();
        try {
            versionCode = String.valueOf(context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode);

            // Not first session
            if (!preferences.getBoolean(FIRST_SESSION, true) || preferences.getBoolean("ATFirstInitLifecycleDone", false)) {
                newSessionInit(preferences);
            } else {
                SharedPreferences backwardPreferences = context.getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
                firstSessionInit(preferences, backwardPreferences);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        preferences.edit().putBoolean("ATFirstInitLifecycleDone", true).apply();
        isInitialized = true;
    }

    static void firstSessionInit(SharedPreferences preferences, SharedPreferences backwardPreferences) {
        // If SDKV1 lifecycle exists
        if (backwardPreferences != null && backwardPreferences.getString(AT_FIRST_LAUNCH, null) != null) {
            preferences.edit().putBoolean(FIRST_SESSION, false)
                    .putString(FIRST_SESSION_DATE, backwardPreferences.getString(AT_FIRST_LAUNCH, ""))
                    .putInt(SESSION_COUNT, backwardPreferences.getInt("ATLaunchCount", 0))
                    .putString(LAST_SESSION_DATE, backwardPreferences.getString("ATLastLaunch", "")).apply();

            backwardPreferences.edit().putString(AT_FIRST_LAUNCH, null).apply();
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            preferences.edit()
                    .putBoolean(FIRST_SESSION, true)
                    .putBoolean(FIRST_SESSION_AFTER_UPDATE, false)
                    .putInt(SESSION_COUNT, 1)
                    .putInt(SESSION_COUNT_SINCE_UPDATE, 1)
                    .putInt(DAYS_SINCE_FIRST_SESSION, 0)
                    .putInt(DAYS_SINCE_LAST_SESSION, 0)
                    .putString(FIRST_SESSION_DATE, simpleDateFormat.format(new Date()))
                    .putString(LAST_SESSION_DATE, simpleDateFormat.format(new Date()))
                    .apply();
        }

        preferences.edit().putString(LifeCycle.VERSION_CODE_KEY, versionCode)
                .apply();

        sessionId = UUID.randomUUID().toString();
    }

    static void updateFirstSession(SharedPreferences preferences) {
        preferences.edit()
                .putBoolean(FIRST_SESSION, false)
                .putBoolean(FIRST_SESSION_AFTER_UPDATE, false)
                .apply();
    }

    static void newSessionInit(SharedPreferences preferences) {
        /// Do not track
        if (ATInternet.optOutEnabled(Tracker.getAppContext())) {
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            updateFirstSession(preferences);
            // Calcul dsfs
            String firstLaunchDate = preferences.getString(FIRST_SESSION_DATE, "");
            if (!TextUtils.isEmpty(firstLaunchDate)) {
                long timeSinceFirstLaunch = simpleDateFormat.parse(firstLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_FIRST_SESSION, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunch)).apply();
            }

            // Calcul dsu
            String firstLaunchDateAfterUpdate = preferences.getString(FIRST_SESSION_DATE_AFTER_UPDATE, "");
            if (!TextUtils.isEmpty(firstLaunchDateAfterUpdate)) {
                long timeSinceFirstLaunchAfterUpdate = simpleDateFormat.parse(firstLaunchDateAfterUpdate).getTime();
                preferences.edit().putInt(DAYS_SINCE_UPDATE, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunchAfterUpdate)).apply();
            }

            // Calcul dsls
            String lastLaunchDate = preferences.getString(LAST_SESSION_DATE, "");
            if (!TextUtils.isEmpty(lastLaunchDate)) {
                long timeSinceLastUse = simpleDateFormat.parse(lastLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_LAST_SESSION, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceLastUse)).apply();
            }
            preferences.edit().putString(LAST_SESSION_DATE, simpleDateFormat.format(new Date())).apply();

            // sc
            preferences.edit().putInt(SESSION_COUNT, preferences.getInt(SESSION_COUNT, 0) + 1).apply();

            // Calcul scsu
            preferences.edit().putInt(SESSION_COUNT_SINCE_UPDATE, preferences.getInt(SESSION_COUNT_SINCE_UPDATE, 0) + 1).apply();

            // Application version changed
            String savedApvr = preferences.getString(VERSION_CODE_KEY, "");
            // Update detected
            if (!versionCode.equals(savedApvr)) {
                preferences.edit()
                        .putString(FIRST_SESSION_DATE_AFTER_UPDATE, simpleDateFormat.format(new Date()))
                        .putString(VERSION_CODE_KEY, versionCode)
                        .putInt(SESSION_COUNT_SINCE_UPDATE, 1)
                        .putInt(DAYS_SINCE_UPDATE, 0)
                        .putBoolean(FIRST_SESSION_AFTER_UPDATE, true)
                        .apply();
            }
        } catch (ParseException e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        sessionId = UUID.randomUUID().toString();
    }

    /**
     * Get the object which contains lifecycle metrics
     *
     * @return Closure
     */
    static Closure getMetrics(final SharedPreferences preferences) {
        return new Closure() {
            @Override
            public String execute() {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();

                    // fs
                    map.put("fs", preferences.getBoolean(FIRST_SESSION, false) ? 1 : 0);

                    // fsau
                    map.put("fsau", preferences.getBoolean(FIRST_SESSION_AFTER_UPDATE, false) ? 1 : 0);

                    if (!TextUtils.isEmpty(preferences.getString(FIRST_SESSION_DATE_AFTER_UPDATE, ""))) {
                        map.put("scsu", preferences.getInt(SESSION_COUNT_SINCE_UPDATE, 0));
                        map.put("fsdau", Integer.parseInt(preferences.getString(FIRST_SESSION_DATE_AFTER_UPDATE, simpleDateFormat.format(new Date()))));
                        map.put("dsu", preferences.getInt(DAYS_SINCE_UPDATE, 0));
                    }

                    map.put("sc", preferences.getInt(SESSION_COUNT, 0));
                    map.put("fsd", Integer.parseInt(preferences.getString(FIRST_SESSION_DATE, simpleDateFormat.format(new Date()))));
                    map.put("dsls", preferences.getInt(DAYS_SINCE_LAST_SESSION, 0));
                    map.put("dsfs", preferences.getInt(DAYS_SINCE_FIRST_SESSION, 0));
                    map.put("sessionId", sessionId);

                    return new JSONObject().put("lifecycle", new JSONObject(map)).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
    }

    static Map<String, Object> getMetricsMap(final SharedPreferences preferences) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Map<String, Object> map = new LinkedHashMap<>();

        // fs
        map.put("fs", preferences.getBoolean(FIRST_SESSION, false) ? 1 : 0);

        // fsau
        map.put("fsau", preferences.getBoolean(FIRST_SESSION_AFTER_UPDATE, false) ? 1 : 0);

        if (!TextUtils.isEmpty(preferences.getString(FIRST_SESSION_DATE_AFTER_UPDATE, ""))) {
            map.put("scsu", preferences.getInt(SESSION_COUNT_SINCE_UPDATE, 0));
            map.put("fsdau", Integer.parseInt(preferences.getString(FIRST_SESSION_DATE_AFTER_UPDATE, simpleDateFormat.format(new Date()))));
            map.put("dsu", preferences.getInt(DAYS_SINCE_UPDATE, 0));
        }

        map.put("sc", preferences.getInt(SESSION_COUNT, 0));
        map.put("fsd", Integer.parseInt(preferences.getString(FIRST_SESSION_DATE, simpleDateFormat.format(new Date()))));
        map.put("dsls", preferences.getInt(DAYS_SINCE_LAST_SESSION, 0));
        map.put("dsfs", preferences.getInt(DAYS_SINCE_FIRST_SESSION, 0));
        map.put("sessionId", sessionId);

        return map;
    }
}

@TargetApi(14)
class TrackerActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private static final int DELAY = 300;
    private static final int DELTA = 2;

    private long timeInBackground;
    protected final Configuration configuration;
    private String savedActivityName;
    private int savedActivityTaskId;
    private Handler handler;
    private Runnable debuggerCancelable;

    TrackerActivityLifeCycle(Configuration configuration) {
        this.configuration = configuration;
        timeInBackground = -1;
        savedActivityName = null;
        handler = new Handler();
        debuggerCancelable = new Runnable() {
            @Override
            public void run() {
                Debugger.setViewerVisibility(false);
                Debugger.setDebuggerViewerLayout(false);
            }
        };
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        TechnicalContext.resetScreenContext();
        if (savedActivityName == null || activity == null || !activity.getClass().getCanonicalName().equals(savedActivityName)
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
        if (Debugger.isActive()) {
            handler.removeCallbacks(debuggerCancelable);
            Debugger.setViewerVisibility(true);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        savedActivityName = activity.getClass().getCanonicalName();
        savedActivityTaskId = activity.getTaskId();
        timeInBackground = System.currentTimeMillis();
        if (Debugger.isActive()) {
            handler.postDelayed(debuggerCancelable, DELAY);
        }
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

