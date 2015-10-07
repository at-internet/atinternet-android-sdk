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

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LifeCycleTest extends AbstractTestClass {

    private SharedPreferences preferences;
    private String fourDaysAgo;
    private String twoDaysAgo;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        LifeCycle.isInitialized = true;
        preferences = Robolectric.application.getSharedPreferences(TrackerKeys.PREFERENCES, Context.MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        today = sdf.format(new Date());
        fourDaysAgo = sdf.format(new Date(System.currentTimeMillis() - (172800000 * 2)));
        twoDaysAgo = sdf.format(new Date(System.currentTimeMillis() - (172800000)));
        preferences.edit().putString("FirstLaunchDate", fourDaysAgo).apply();
        preferences.edit().putString("FirstLaunchDateAfterUpdate", twoDaysAgo).apply();
        preferences.edit().putString("LastLaunchDate", twoDaysAgo).apply();
        preferences.edit().putInt("IdDayOnYear", Calendar.getInstance().get(Calendar.DAY_OF_YEAR)).apply();
        preferences.edit().putInt("IdWeekOnYear", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)).apply();
        preferences.edit().putInt("IdMonth", Calendar.getInstance().get(Calendar.MONTH)).apply();
        preferences.edit().putInt("IdYear", Calendar.getInstance().get(Calendar.YEAR)).apply();
    }

    @Test
    public void retrieveSDKV1LifecycleTest() {
        preferences.edit().putBoolean("FirstLaunch", true)
                .putString("FirstLaunchDate", null)
                .putString("LastLaunchDate", null)
                .putInt("LaunchCount", 0)
                .apply();

        SharedPreferences backwardPrefs = Robolectric.application.getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
        backwardPrefs.edit()
                .putString("ATFirstLaunch", "fld")
                .putInt("ATLaunchCount", 6)
                .putString("ATLastLaunch", "yesterday")
                .apply();

        assertTrue(preferences.getBoolean("FirstLaunch", true));
        assertTrue(preferences.getString("FirstLaunchDate", "").isEmpty());
        assertTrue(preferences.getString("LastLaunchDate", "").isEmpty());
        assertTrue(preferences.getInt("LaunchCount", 0) == 0);

        LifeCycle.updateFirstLaunchInformation(preferences, "1", backwardPrefs);

        assertFalse(preferences.getBoolean("FirstLaunch", true));
        assertEquals("fld", preferences.getString("FirstLaunchDate", ""));
        assertEquals("yesterday", preferences.getString("LastLaunchDate", ""));
        assertEquals(6, preferences.getInt("LaunchCount", 0));
    }

    @Test
    public void updateFirstLaunchInformationTest() throws NoSuchFieldException, IllegalAccessException {
        LifeCycle.updateFirstLaunchInformation(preferences, "1", null);
        assertEquals("1", preferences.getString("VersionCode", ""));
        assertTrue(preferences.getBoolean("FirstLaunch", false));
        assertEquals(0, preferences.getInt("LaunchCount", -1));
        assertEquals(0, preferences.getInt("DaysSinceFirstLaunch", -1));
        assertEquals(0, preferences.getInt("DaysSinceLastUse", -1));
    }

    @Test
    public void updateFirstLaunchAfterUpdateInformationTest() throws NoSuchFieldException, IllegalAccessException {
        LifeCycle.updateFirstLaunchAfterUpdateInformation(preferences, "2");
        assertEquals("2", preferences.getString("VersionCode", ""));
        assertTrue(preferences.getBoolean("ApplicationUpdated", false));
        assertTrue(preferences.getBoolean("FirstLaunchAfterUpdate", false));
        assertEquals(0, preferences.getInt("LaunchCountSinceUpdate", -1));
        assertEquals(0, preferences.getInt("DaysSinceFirstLaunchAfterUpdate", -1));
    }

    @Test
    public void updateLaunchInformationTest() {
        LifeCycle.updateLaunchInformation(preferences);
        assertEquals(1, preferences.getInt("LaunchCount", -1));
        assertEquals(4, preferences.getInt("DaysSinceFirstLaunch", -1));
        assertEquals(2, preferences.getInt("DaysSinceFirstLaunchAfterUpdate", -1));
        assertEquals(2, preferences.getInt("DaysSinceLastUse", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnDay", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnWeek", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnMonth", -1));
    }

    @Test
    public void updateLaunchCountTest() {
        LifeCycle.updateLaunchInformation(preferences);
        LifeCycle.updateLaunchInformation(preferences);
        assertEquals(2, preferences.getInt("LaunchCount", -1));
        assertEquals(2, preferences.getInt("LaunchCountOnDay", -1));
        assertEquals(2, preferences.getInt("LaunchCountOnWeek", -1));
        assertEquals(2, preferences.getInt("LaunchCountOnMonth", -1));
    }

    @Test
    public void updateLaunchCountDayTest() {
        for (int i = 0; i < 6; i++) {
            LifeCycle.updateLaunchInformation(preferences);
        }
        assertEquals(6, preferences.getInt("LaunchCountOnDay", -1));
        preferences.edit().putInt("IdDayOnYear", Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1).apply();
        LifeCycle.updateLaunchInformation(preferences);
        assertEquals(1, preferences.getInt("LaunchCountOnDay", -1));
        assertEquals(7, preferences.getInt("LaunchCountOnWeek", -1));
        assertEquals(7, preferences.getInt("LaunchCountOnMonth", -1));
    }

    @Test
    public void updateLaunchCountWeekTest() {
        for (int i = 0; i < 6; i++) {
            LifeCycle.updateLaunchInformation(preferences);
        }
        assertEquals(6, preferences.getInt("LaunchCountOnWeek", -1));
        preferences.edit().putInt("IdDayOnYear", Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 7).apply();
        preferences.edit().putInt("IdWeekOnYear", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 1).apply();
        LifeCycle.updateLaunchInformation(preferences);
        assertEquals(1, preferences.getInt("LaunchCountOnDay", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnWeek", -1));
        assertEquals(7, preferences.getInt("LaunchCountOnMonth", -1));
    }

    @Test
    public void updateLaunchCountMonthTest() {
        for (int i = 0; i < 6; i++) {
            LifeCycle.updateLaunchInformation(preferences);
        }
        assertEquals(6, preferences.getInt("LaunchCountOnMonth", -1));
        preferences.edit().putInt("IdDayOnYear", Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 30).apply();
        preferences.edit().putInt("IdWeekOnYear", Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 4).apply();
        preferences.edit().putInt("IdMonth", Calendar.getInstance().get(Calendar.MONTH) + 1).apply();
        LifeCycle.updateLaunchInformation(preferences);
        assertEquals(1, preferences.getInt("LaunchCountOnMonth", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnWeek", -1));
        assertEquals(1, preferences.getInt("LaunchCountOnMonth", -1));
    }

    @Test
    public void getMetricsFirstLaunchFirstHitPageTest() throws Exception {
        preferences.edit().putBoolean("IsFirstHit", true).apply();
        preferences.edit().putBoolean("FirstLaunch", true).apply();
        preferences.edit().putString("FirstLaunchDate", "").apply();
        preferences.edit().putString("FirstLaunchDateAfterUpdate", "").apply();
        preferences.edit().putString("LastLaunchDate", "").apply();
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(1, lifecycle.getInt("fl"));
        assertEquals(Integer.parseInt(today), lifecycle.getInt("fld"));
        assertEquals(0, lifecycle.getInt("dsfl"));
        assertEquals(0, lifecycle.getInt("flau"));
        assertEquals(0, lifecycle.getInt("dslu"));
        assertEquals(1, lifecycle.getInt("lc"));
        assertEquals(0, lifecycle.getInt("lcsu"));
        assertEquals(1, lifecycle.getInt("ldc"));
        assertEquals(1, lifecycle.getInt("lwc"));
        assertEquals(1, lifecycle.getInt("lmc"));
    }

    @Test
    public void getMetricsNoFirstLaunchFirstHitTest() throws Exception {
        preferences.edit().putBoolean("IsFirstHit", true).apply();
        preferences.edit().putString("FirstLaunchDateAfterUpdate", "").apply();
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(0, lifecycle.getInt("fl"));
        assertEquals(Integer.parseInt(fourDaysAgo), lifecycle.getInt("fld"));
        assertEquals(4, lifecycle.getInt("dsfl"));
        assertEquals(0, lifecycle.getInt("flau"));
        assertEquals(2, lifecycle.getInt("dslu"));
        assertEquals(1, lifecycle.getInt("lc"));
        assertEquals(0, lifecycle.getInt("lcsu"));
        assertEquals(1, lifecycle.getInt("ldc"));
        assertEquals(1, lifecycle.getInt("lwc"));
        assertEquals(1, lifecycle.getInt("lmc"));
    }

    @Test
    public void getMetricsNoFirstHitPageTest() throws Exception {
        preferences.edit().putBoolean("FirstLaunch", true).apply();
        preferences.edit().putString("FirstLaunchDate", "").apply();
        preferences.edit().putString("FirstLaunchDateAfterUpdate", "").apply();
        preferences.edit().putString("LastLaunchDate", "").apply();
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(Integer.parseInt(today), lifecycle.getInt("fld"));
        assertEquals(0, lifecycle.getInt("dsfl"));
        assertEquals(0, lifecycle.getInt("dslu"));
    }

    @Test
    public void getMetricsFirstLaunchAfterUpdateFirstHitPageTest() throws Exception {
        preferences.edit().putBoolean("IsFirstHit", true).apply();
        preferences.edit().putBoolean("FirstLaunchAfterUpdate", true).apply();
        preferences.edit().putString("FirstLaunchDateAfterUpdate", "").apply();
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(0, lifecycle.getInt("fl"));
        assertEquals(Integer.parseInt(fourDaysAgo), lifecycle.getInt("fld"));
        assertEquals(4, lifecycle.getInt("dsfl"));
        assertEquals(1, lifecycle.getInt("flau"));
        assertEquals(Integer.parseInt(today), lifecycle.getInt("uld"));
        assertEquals(2, lifecycle.getInt("dslu"));
        assertEquals(1, lifecycle.getInt("lc"));
        assertEquals(0, lifecycle.getInt("lcsu"));
        assertEquals(1, lifecycle.getInt("ldc"));
        assertEquals(1, lifecycle.getInt("lwc"));
        assertEquals(1, lifecycle.getInt("lmc"));
    }

    @Test
    public void getMetricsNoFirstLaunchAfterUpdateFirstHitTest() throws Exception {
        preferences.edit().putBoolean("IsFirstHit", true).apply();
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(0, lifecycle.getInt("fl"));
        assertEquals(Integer.parseInt(fourDaysAgo), lifecycle.getInt("fld"));
        assertEquals(4, lifecycle.getInt("dsfl"));
        assertEquals(0, lifecycle.getInt("flau"));
        assertEquals(Integer.parseInt(twoDaysAgo), lifecycle.getInt("uld"));
        assertEquals(2, lifecycle.getInt("dsu"));
        assertEquals(2, lifecycle.getInt("dslu"));
        assertEquals(1, lifecycle.getInt("lc"));
        assertEquals(0, lifecycle.getInt("lcsu"));
        assertEquals(1, lifecycle.getInt("ldc"));
        assertEquals(1, lifecycle.getInt("lwc"));
        assertEquals(1, lifecycle.getInt("lmc"));
    }

    @Test
    public void getMetricsNoFirstHitPageAfterUpdateTest() throws Exception {
        LifeCycle.updateLaunchInformation(preferences);
        JSONObject lifecycle = new JSONObject(LifeCycle.getMetrics(tracker.getPreferences()).execute()).getJSONObject("lifecycle");
        assertEquals(Integer.parseInt(fourDaysAgo), lifecycle.getInt("fld"));
        assertEquals(4, lifecycle.getInt("dsfl"));
        assertEquals(Integer.parseInt(twoDaysAgo), lifecycle.getInt("uld"));
        assertEquals(2, lifecycle.getInt("dsu"));
        assertEquals(2, lifecycle.getInt("dslu"));
    }
}
