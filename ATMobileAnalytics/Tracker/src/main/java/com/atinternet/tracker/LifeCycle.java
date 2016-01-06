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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;

class LifeCycle {

    /**
     * Key representing version code app
     */
    static final String VERSION_CODE_KEY = "VersionCode";

    /**
     * Key representing if it's first launch
     */
    static final String FIRST_LAUNCH_KEY = "FirstLaunch";

    /**
     * Key representing if it's first launch after update
     */
    static final String FIRST_LAUNCH_AFTER_UPDATE_KEY = "FirstLaunchAfterUpdate";

    /**
     * Key representing if it's first launch after update
     */
    static final String APPLICATION_UPDATED_KEY = "ApplicationUpdated";

    /**
     * Key representing first launch date
     */
    static final String FIRST_LAUNCH_DATE_KEY = "FirstLaunchDate";

    /**
     * Key representing first launch date after update
     */
    static final String UPDATE_LAUNCH_DATE = "FirstLaunchDateAfterUpdate";

    /**
     * Key representing last launch date
     */
    static final String LAST_LAUNCH_DATE_KEY = "LastLaunchDate";

    /**
     * Key representing the app launch count
     */
    static final String LAUNCH_COUNT_KEY = "LaunchCount";

    /**
     * Key representing the app launch count on day
     */
    static final String LAUNCH_COUNT_ON_DAY_KEY = "LaunchCountOnDay";

    /**
     * Key representing the app launch count on week
     */
    static final String LAUNCH_COUNT_ON_WEEK_KEY = "LaunchCountOnWeek";

    /**
     * Key representing the app launch count on month
     */
    static final String LAUNCH_COUNT_ON_MONTH_KEY = "LaunchCountOnMonth";

    /**
     * Key representing the app launch count since update
     */
    static final String LAUNCH_COUNT_SINCE_UPDATE_KEY = "LaunchCountSinceUpdate";

    /**
     * Key representing count of days since first launch
     */
    static final String DAYS_SINCE_FIRST_LAUNCH_KEY = "DaysSinceFirstLaunch";

    /**
     * Key representing count of days since first launch after update
     */
    static final String DAYS_SINCE_UPDATE_KEY = "DaysSinceFirstLaunchAfterUpdate";

    /**
     * Key representing count of days since last use
     */
    static final String DAYS_SINCE_LAST_USE_KEY = "DaysSinceLastUse";

    /**
     * Key representing id day on year
     */
    static final String ID_DAY_ON_YEAR_KEY = "IdDayOnYear";

    /**
     * Key representing id week on year
     */
    static final String ID_WEEK_ON_YEAR_KEY = "IdWeekOnYear";

    /**
     * Key representing id month
     */
    static final String ID_MONTH_KEY = "IdMonth";

    /**
     * Key representing id year
     */
    static final String ID_YEAR_KEY = "IdYear";

    /**
     * String pattern to get fld and uld
     */
    static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Check whether lifecycle has already been initialized
     */
    static boolean isInitialized = false;

    /**
     * Session identifier
     */
    private static String sessionId;

    /**
     * Version code
     */
    private static String versionCode;

    /**
     * Date format
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Init lifecycle
     *
     * @param context Context
     */
    static void initLifeCycle(Context context) {
        try {
            versionCode = String.valueOf(context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode);
            SharedPreferences preferences = Tracker.getPreferences();

            // Not first launch
            if (!preferences.getBoolean(FIRST_LAUNCH_KEY, true)) {
                newLaunchInit(preferences);
            } else {
                SharedPreferences backwardPreferences = context.getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
                firstLaunchInit(preferences, backwardPreferences);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        isInitialized = true;
    }

    static void firstLaunchInit(SharedPreferences preferences, SharedPreferences backwardPreferences) {
        // If SDKV1 lifecycle exists
        if (backwardPreferences != null && backwardPreferences.getString("ATFirstLaunch", null) != null) {
            preferences.edit().putBoolean(FIRST_LAUNCH_KEY, false)
                    .putString(FIRST_LAUNCH_DATE_KEY, backwardPreferences.getString("ATFirstLaunch", ""))
                    .putInt(LAUNCH_COUNT_KEY, backwardPreferences.getInt("ATLaunchCount", 0))
                    .putString(LAST_LAUNCH_DATE_KEY, backwardPreferences.getString("ATLastLaunch", "")).apply();

            backwardPreferences.edit().putString("ATFirstLaunch", null).apply();
        } else {
            Calendar calendar = Calendar.getInstance();
            preferences.edit()
                    .putBoolean(FIRST_LAUNCH_KEY, true)
                    .putBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, false)
                    .putInt(ID_YEAR_KEY, calendar.get(Calendar.YEAR))
                    .putInt(ID_DAY_ON_YEAR_KEY, calendar.get(Calendar.DAY_OF_YEAR))
                    .putInt(ID_MONTH_KEY, calendar.get(Calendar.MONTH))
                    .putInt(ID_WEEK_ON_YEAR_KEY, calendar.get(Calendar.WEEK_OF_YEAR))
                    .putInt(LAUNCH_COUNT_ON_DAY_KEY, 1)
                    .putInt(LAUNCH_COUNT_ON_WEEK_KEY, 1)
                    .putInt(LAUNCH_COUNT_ON_MONTH_KEY, 1)
                    .putInt(LAUNCH_COUNT_KEY, 1)
                    .putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 1)
                    .putInt(DAYS_SINCE_FIRST_LAUNCH_KEY, 0)
                    .putInt(DAYS_SINCE_LAST_USE_KEY, 0)
                    .putString(FIRST_LAUNCH_DATE_KEY, simpleDateFormat.format(new Date()))
                    .apply();
        }

        preferences.edit().putString(LifeCycle.VERSION_CODE_KEY, versionCode)
                .apply();

        sessionId = UUID.randomUUID().toString();
    }

    static void updateFirstLaunch(SharedPreferences preferences) {
        preferences.edit()
                .putBoolean(FIRST_LAUNCH_KEY, false)
                .putBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, false)
                .apply();
    }

    static void newLaunchInit(SharedPreferences preferences) {
        try {
            // Calcul dsfl
            String firstLaunchDate = preferences.getString(FIRST_LAUNCH_DATE_KEY, "");
            if (!TextUtils.isEmpty(firstLaunchDate)) {
                long timeSinceFirstLaunch = simpleDateFormat.parse(firstLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_FIRST_LAUNCH_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunch)).apply();
            }

            // Calcul dsu
            String firstLaunchDateAfterUpdate = preferences.getString(UPDATE_LAUNCH_DATE, "");
            if (!TextUtils.isEmpty(firstLaunchDateAfterUpdate)) {
                long timeSinceFirstLaunchAfterUpdate = simpleDateFormat.parse(firstLaunchDateAfterUpdate).getTime();
                preferences.edit().putInt(DAYS_SINCE_UPDATE_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunchAfterUpdate)).apply();
            }

            // Calcul dslu
            String lastLaunchDate = preferences.getString(LAST_LAUNCH_DATE_KEY, "");
            if (!TextUtils.isEmpty(lastLaunchDate)) {
                long timeSinceLastUse = simpleDateFormat.parse(lastLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_LAST_USE_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceLastUse)).apply();
            }
            preferences.edit().putString(LAST_LAUNCH_DATE_KEY, simpleDateFormat.format(new Date())).apply();

            if (!isInitialized) {

                // lc
                preferences.edit().putInt(LAUNCH_COUNT_KEY, preferences.getInt(LAUNCH_COUNT_KEY, 0) + 1).apply();


                // Calcul lcsu
                if (preferences.getBoolean(APPLICATION_UPDATED_KEY, false)) {
                    int launchCountSinceUpdate = preferences.getInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0);
                    preferences.edit().putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, launchCountSinceUpdate + 1).apply();
                }

                Calendar calendar = Calendar.getInstance();
                int idYear = preferences.getInt(ID_YEAR_KEY, -1);
                int idCurrentYear = calendar.get(Calendar.YEAR);

                // ldc
                int idCurrentDayOnYear = calendar.get(Calendar.DAY_OF_YEAR);
                int dayOnYear = preferences.getInt(ID_DAY_ON_YEAR_KEY, -1);
                int launchCountOnDay;
                if (dayOnYear == -1 || dayOnYear != idCurrentDayOnYear || idYear == -1 || idYear != idCurrentYear) {
                    preferences.edit().putInt(ID_DAY_ON_YEAR_KEY, idCurrentDayOnYear).apply();
                    launchCountOnDay = 0;
                } else {
                    launchCountOnDay = preferences.getInt(LAUNCH_COUNT_ON_DAY_KEY, 0);
                }
                preferences.edit().putInt(LAUNCH_COUNT_ON_DAY_KEY, launchCountOnDay + 1).apply();

                // lwc
                int idCurrentWeekOnYear = calendar.get(Calendar.WEEK_OF_YEAR);
                int weekOnYear = preferences.getInt(ID_WEEK_ON_YEAR_KEY, -1);
                int launchCountOnWeek;
                if (weekOnYear == -1 || weekOnYear != idCurrentWeekOnYear || idYear == -1 || idYear != idCurrentYear) {
                    preferences.edit().putInt(ID_WEEK_ON_YEAR_KEY, idCurrentWeekOnYear).apply();
                    launchCountOnWeek = 0;
                } else {
                    launchCountOnWeek = preferences.getInt(LAUNCH_COUNT_ON_WEEK_KEY, 0);
                }
                preferences.edit().putInt(LAUNCH_COUNT_ON_WEEK_KEY, launchCountOnWeek + 1).apply();

                // lmc
                int idCurrentMonth = calendar.get(Calendar.MONTH);
                int month = preferences.getInt(ID_MONTH_KEY, -1);
                int launchCountOnMonth;
                if (month == -1 || month != idCurrentMonth || idYear == -1 || idYear != idCurrentYear) {
                    preferences.edit().putInt(ID_MONTH_KEY, idCurrentMonth).apply();
                    launchCountOnMonth = 0;
                } else {
                    launchCountOnMonth = preferences.getInt(LAUNCH_COUNT_ON_MONTH_KEY, 0);
                }
                preferences.edit().putInt(LAUNCH_COUNT_ON_MONTH_KEY, launchCountOnMonth + 1).apply();

                preferences.edit().putInt(ID_YEAR_KEY, idCurrentYear).apply();
            }

            // Application version changed
            String savedApvr = preferences.getString(VERSION_CODE_KEY, "");
            // Update detected
            if (!versionCode.equals(savedApvr)) {
                preferences.edit()
                        .putString(UPDATE_LAUNCH_DATE, simpleDateFormat.format(new Date()))
                        .putString(VERSION_CODE_KEY, versionCode)
                        .putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 1)
                        .putInt(DAYS_SINCE_UPDATE_KEY, 0)
                        .putBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, true)
                        .apply();
            } else if (!isInitialized) {
                preferences.edit().putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, preferences.getInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0) + 1).apply();
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
                    LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

                    // fl
                    map.put("fl", preferences.getBoolean(FIRST_LAUNCH_KEY, false) ? 1 : 0);

                    // flau
                    map.put("flau", preferences.getBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, false) ? 1 : 0);

                    map.put("ldc", preferences.getInt(LAUNCH_COUNT_ON_DAY_KEY, 0));
                    map.put("lwc", preferences.getInt(LAUNCH_COUNT_ON_WEEK_KEY, 0));
                    map.put("lmc", preferences.getInt(LAUNCH_COUNT_ON_MONTH_KEY, 0));

                    if (!TextUtils.isEmpty(preferences.getString(UPDATE_LAUNCH_DATE, ""))) {
                        map.put("lcsu", preferences.getInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0));
                        map.put("uld", Integer.parseInt(preferences.getString(UPDATE_LAUNCH_DATE, "")));
                        map.put("dsu", preferences.getInt(DAYS_SINCE_UPDATE_KEY, 0));
                    }

                    map.put("lc", preferences.getInt(LAUNCH_COUNT_KEY, 0));
                    map.put("fld", Integer.parseInt(preferences.getString(FIRST_LAUNCH_DATE_KEY, "")));
                    map.put("dslu", preferences.getInt(DAYS_SINCE_LAST_USE_KEY, 0));
                    map.put("dsfl", preferences.getInt(DAYS_SINCE_FIRST_LAUNCH_KEY, 0));
                    map.put("sessionId", sessionId);

                    return new JSONObject().put("lifecycle", new JSONObject(map)).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
    }
}
