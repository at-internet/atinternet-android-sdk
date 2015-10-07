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

import android.content.*;
import android.content.Context;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

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
    private static final String FIRST_LAUNCH_AFTER_UPDATE_KEY = "FirstLaunchAfterUpdate";

    /**
     * Key representing if it's first launch after update
     */
    private static final String APPLICATION_UPDATED_KEY = "ApplicationUpdated";

    /**
     * Key representing first launch date
     */
    private static final String FIRST_LAUNCH_DATE_KEY = "FirstLaunchDate";

    /**
     * Key representing first launch date after update
     */
    private static final String FIRST_LAUNCH_DATE_AFTER_UPDATE_KEY = "FirstLaunchDateAfterUpdate";

    /**
     * Key representing last launch date
     */
    private static final String LAST_LAUNCH_DATE_KEY = "LastLaunchDate";

    /**
     * Key representing the app launch count
     */
    private static final String LAUNCH_COUNT_KEY = "LaunchCount";

    /**
     * Key representing the app launch count on day
     */
    private static final String LAUNCH_COUNT_ON_DAY_KEY = "LaunchCountOnDay";

    /**
     * Key representing the app launch count on week
     */
    private static final String LAUNCH_COUNT_ON_WEEK_KEY = "LaunchCountOnWeek";

    /**
     * Key representing the app launch count on month
     */
    private static final String LAUNCH_COUNT_ON_MONTH_KEY = "LaunchCountOnMonth";

    /**
     * Key representing the app launch count since update
     */
    private static final String LAUNCH_COUNT_SINCE_UPDATE_KEY = "LaunchCountSinceUpdate";

    /**
     * Key representing count of days since first launch
     */
    private static final String DAYS_SINCE_FIRST_LAUNCH_KEY = "DaysSinceFirstLaunch";

    /**
     * Key representing count of days since first launch after update
     */
    private static final String DAYS_SINCE_FIRST_LAUNCH_AFTER_UPDATE_KEY = "DaysSinceFirstLaunchAfterUpdate";

    /**
     * Key representing count of days since last use
     */
    private static final String DAYS_SINCE_LAST_USE_KEY = "DaysSinceLastUse";

    /**
     * Key representing id day on year
     */
    private static final String ID_DAY_ON_YEAR_KEY = "IdDayOnYear";

    /**
     * Key representing id week on year
     */
    private static final String ID_WEEK_ON_YEAR_KEY = "IdWeekOnYear";

    /**
     * Key representing id month
     */
    private static final String ID_MONTH_KEY = "IdMonth";

    /**
     * Key representing id year
     */
    private static final String ID_YEAR_KEY = "IdYear";

    /**
     * String pattern to get fld and uld
     */
    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * Check whether lifecycle has already been initialized
     */
    static boolean isInitialized = false;

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
            SharedPreferences preferences = context.getSharedPreferences(TrackerKeys.PREFERENCES, Context.MODE_PRIVATE);
            String versionCode = String.valueOf(context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode);

            String savedVersionCode = preferences.getString(LifeCycle.VERSION_CODE_KEY, null);

            // S'il s'agit du premier lancement de l'application
            SharedPreferences backwardPreferences = context.getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
            if (preferences.getBoolean(LifeCycle.FIRST_LAUNCH_KEY, true)) {
                LifeCycle.updateFirstLaunchInformation(preferences, versionCode, backwardPreferences);
            }
            // Sinon, s'il s'agit du premier lancement depuis un update
            else if (savedVersionCode != null && !savedVersionCode.equals(versionCode)) {
                LifeCycle.updateFirstLaunchAfterUpdateInformation(preferences, versionCode);
            }
            LifeCycle.updateLaunchInformation(preferences);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        isInitialized = true;
    }

    /**
     * Update lifecycle information when app is launched for the first time
     *
     * @param preferences         SharedPreferences
     * @param versionCode         String
     * @param backwardPreferences SharedPreferences
     */
    static void updateFirstLaunchInformation(SharedPreferences preferences, String versionCode, SharedPreferences backwardPreferences) {
        // If SDKV1 lifecycle exists
        if (backwardPreferences != null && backwardPreferences.getString("ATFirstLaunch", null) != null) {
            preferences.edit().putBoolean(FIRST_LAUNCH_KEY, false)
                    .putString(FIRST_LAUNCH_DATE_KEY, backwardPreferences.getString("ATFirstLaunch", ""))
                    .putInt(LAUNCH_COUNT_KEY, backwardPreferences.getInt("ATLaunchCount", 0))
                    .putString(LAST_LAUNCH_DATE_KEY, backwardPreferences.getString("ATLastLaunch", "")).apply();

            backwardPreferences.edit().putString("ATFirstLaunch", null).apply();
        } else {
            preferences.edit().putBoolean(FIRST_LAUNCH_KEY, true)
                    .putInt(LAUNCH_COUNT_KEY, 0)
                    .putInt(DAYS_SINCE_FIRST_LAUNCH_KEY, 0)
                    .putInt(DAYS_SINCE_LAST_USE_KEY, 0).apply();
        }

        preferences.edit().putString(LifeCycle.VERSION_CODE_KEY, versionCode)
                .apply();
    }

    /**
     * Update lifecycle information when app is launched for the first time after update
     *
     * @param preferences SharedPreferences
     * @param versionCode String
     */
    static void updateFirstLaunchAfterUpdateInformation(SharedPreferences preferences, String versionCode) {
        preferences.edit().putBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, true)
                .putBoolean(APPLICATION_UPDATED_KEY, true)
                .putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0)
                .putInt(DAYS_SINCE_FIRST_LAUNCH_AFTER_UPDATE_KEY, 0)
                .putString(LifeCycle.VERSION_CODE_KEY, versionCode)
                .apply();
    }

    /**
     * Update launch information
     *
     * @param preferences SharedPreferences
     */
    static void updateLaunchInformation(SharedPreferences preferences) {

        // Calcul lc
        int launchCount = preferences.getInt(LAUNCH_COUNT_KEY, 0);
        preferences.edit().putInt(LAUNCH_COUNT_KEY, launchCount + 1).apply();

        // Calcul lcsu
        if (preferences.getBoolean(APPLICATION_UPDATED_KEY, false)) {
            int launchCountSinceUpdate = preferences.getInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0);
            preferences.edit().putInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, launchCountSinceUpdate + 1).apply();
        }

        try {
            // Calcul dsfl
            String firstLaunchDate = preferences.getString(FIRST_LAUNCH_DATE_KEY, "");
            if (!firstLaunchDate.isEmpty()) {
                long timeSinceFirstLaunch = simpleDateFormat.parse(firstLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_FIRST_LAUNCH_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunch)).apply();
            }

            // Calcul dsu
            String firstLaunchDateAfterUpdate = preferences.getString(FIRST_LAUNCH_DATE_AFTER_UPDATE_KEY, "");
            if (!firstLaunchDateAfterUpdate.isEmpty()) {
                long timeSinceFirstLaunchAfterUpdate = simpleDateFormat.parse(firstLaunchDateAfterUpdate).getTime();
                preferences.edit().putInt(DAYS_SINCE_FIRST_LAUNCH_AFTER_UPDATE_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceFirstLaunchAfterUpdate)).apply();
            }

            // Calcul dslu
            String lastLaunchDate = preferences.getString(LAST_LAUNCH_DATE_KEY, "");
            if (!lastLaunchDate.isEmpty()) {
                long timeSinceLastUse = simpleDateFormat.parse(lastLaunchDate).getTime();
                preferences.edit().putInt(DAYS_SINCE_LAST_USE_KEY, Tool.getDaysBetweenTimes(System.currentTimeMillis(), timeSinceLastUse)).apply();
            }
            preferences.edit().putString(LAST_LAUNCH_DATE_KEY, simpleDateFormat.format(new Date())).apply();


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
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                if (isInitialized) {
                    LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();

                    final int launchCount = preferences.getInt(LAUNCH_COUNT_KEY, 0);
                    final int launchCountSinceUpdate = preferences.getInt(LAUNCH_COUNT_SINCE_UPDATE_KEY, 0);
                    final int daysSinceFirstLaunch = preferences.getInt(DAYS_SINCE_FIRST_LAUNCH_KEY, 0);
                    final int daysSinceUpdate = preferences.getInt(DAYS_SINCE_FIRST_LAUNCH_AFTER_UPDATE_KEY, -1);
                    final int daysSinceLastUse = preferences.getInt(DAYS_SINCE_LAST_USE_KEY, 0);
                    final int launchCountOnDay = preferences.getInt(LAUNCH_COUNT_ON_DAY_KEY, 0);
                    final int launchCountOnWeek = preferences.getInt(LAUNCH_COUNT_ON_WEEK_KEY, 0);
                    final int launchCountOnMonth = preferences.getInt(LAUNCH_COUNT_ON_MONTH_KEY, 0);
                    int firstLaunchDate = -1;
                    int updateLaunchDate = -1;
                    boolean isFirstLaunch = false;
                    boolean isFirstLaunchAfterUpdate = false;

                    // fl
                    if (preferences.getBoolean(FIRST_LAUNCH_KEY, false)) {
                        preferences.edit().putBoolean(FIRST_LAUNCH_KEY, false)
                                .putString(FIRST_LAUNCH_DATE_KEY, simpleDateFormat.format(new Date()))
                                .apply();
                        isFirstLaunch = true;
                    }

                    // flau
                    if (preferences.getBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, false)) {
                        preferences.edit().putBoolean(FIRST_LAUNCH_AFTER_UPDATE_KEY, false)
                                .putString(FIRST_LAUNCH_DATE_AFTER_UPDATE_KEY, simpleDateFormat.format(new Date())).apply();
                        isFirstLaunchAfterUpdate = true;
                    }

                    String firstLaunchDateString = preferences.getString(FIRST_LAUNCH_DATE_KEY, "");
                    String updateLaunchDateString = preferences.getString(FIRST_LAUNCH_DATE_AFTER_UPDATE_KEY, "");

                    if (!firstLaunchDateString.isEmpty()) {
                        firstLaunchDate = Integer.parseInt(firstLaunchDateString);
                    }

                    if (!updateLaunchDateString.isEmpty()) {
                        updateLaunchDate = Integer.parseInt(updateLaunchDateString);
                    }

                    try {
                        preferences.edit().putBoolean(TrackerKeys.IS_FIRST_SCREEN_HIT_KEY, false).apply();

                        map.put("fl", isFirstLaunch ? 1 : 0);
                        if (firstLaunchDate != -1) {
                            map.put("fld", firstLaunchDate);
                        }
                        map.put("dsfl", daysSinceFirstLaunch);
                        map.put("flau", isFirstLaunchAfterUpdate ? 1 : 0);
                        if (updateLaunchDate != -1) {
                            map.put("uld", updateLaunchDate);
                        }
                        if (daysSinceUpdate != -1) {
                            map.put("dsu", daysSinceUpdate);
                        }
                        map.put("dslu", daysSinceLastUse);
                        map.put("lc", launchCount);
                        map.put("lcsu", launchCountSinceUpdate);
                        map.put("ldc", launchCountOnDay);
                        map.put("lwc", launchCountOnWeek);
                        map.put("lmc", launchCountOnMonth);

                        if (firstLaunchDate != -1) {
                            map.put("fld", firstLaunchDate);
                        }
                        map.put("dsfl", daysSinceFirstLaunch);
                        if (updateLaunchDate != -1) {
                            map.put("uld", updateLaunchDate);
                        }
                        if (daysSinceUpdate != -1) {
                            map.put("dsu", daysSinceUpdate);
                        }
                        map.put("dslu", daysSinceLastUse);
                        map.put("lc", launchCount);

                        return new JSONObject().put("lifecycle", new JSONObject(map)).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return "";
            }
        };
    }
}
