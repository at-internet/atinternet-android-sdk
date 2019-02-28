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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class LifeCycleTest extends AbstractTestClass {

    private SharedPreferences preferences;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        preferences = ApplicationProvider.getApplicationContext().getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        today = sdf.format(new Date());
    }

    @Test
    public void retrieveSDKV1LifecycleTest() {
        preferences.edit().putBoolean("FirstLaunch", true)
                .putString("FirstLaunchDate", null)
                .putString("LastLaunchDate", null)
                .putInt("LaunchCount", 0)
                .apply();

        SharedPreferences backwardPrefs = ApplicationProvider.getApplicationContext().getSharedPreferences("ATPrefs", Context.MODE_PRIVATE);
        backwardPrefs.edit()
                .putString("ATFirstLaunch", "fsd")
                .putInt("ATLaunchCount", 6)
                .putString("ATLastLaunch", "yesterday")
                .apply();

        assertTrue(preferences.getBoolean("FirstLaunch", true));
        assertTrue(preferences.getString("FirstLaunchDate", "").isEmpty());
        assertTrue(preferences.getString("LastLaunchDate", "").isEmpty());
        assertTrue(preferences.getInt("LaunchCount", 0) == 0);

        LifeCycle.firstSessionInit(preferences, backwardPrefs);

        assertFalse(preferences.getBoolean("FirstLaunch", true));
        assertEquals("fsd", preferences.getString("FirstLaunchDate", ""));
        assertEquals("yesterday", preferences.getString("LastLaunchDate", ""));
        assertEquals(6, preferences.getInt("LaunchCount", 0));
    }

    @Test
    public void firstLaunchInitTest() {
        LifeCycle.firstSessionInit(preferences, null);
        assertTrue(preferences.getBoolean(LifeCycle.FIRST_SESSION, false));
        assertFalse(preferences.getBoolean(LifeCycle.FIRST_SESSION_AFTER_UPDATE, false));
        assertEquals(today, preferences.getString(LifeCycle.FIRST_SESSION_DATE, null));

        assertEquals(1, preferences.getInt(LifeCycle.SESSION_COUNT, 0));
        assertEquals(1, preferences.getInt(LifeCycle.SESSION_COUNT_SINCE_UPDATE, 0));
        assertEquals(0, preferences.getInt(LifeCycle.DAYS_SINCE_FIRST_SESSION, 1));
        assertEquals(0, preferences.getInt(LifeCycle.DAYS_SINCE_LAST_SESSION, 1));
        assertEquals("0", preferences.getString(LifeCycle.VERSION_CODE_KEY, null));
    }

    @Test
    public void newLaunchInitTest() throws JSONException {

        LifeCycle.firstSessionInit(preferences, null);
        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");
        String sesssionId = life.getString("sessionId");

        LifeCycle.newSessionInit(preferences);
        obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        life = obj.getJSONObject("lifecycle");

        assertEquals(2, preferences.getInt(LifeCycle.SESSION_COUNT, 0));
        assertEquals(2, preferences.getInt(LifeCycle.SESSION_COUNT_SINCE_UPDATE, 0));
        assertNotSame(sesssionId, life.getString("sessionId"));
    }

    public void lifecycleContainedInHitTest() throws JSONException {
        LifeCycle.firstSessionInit(preferences, null);
        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");

        assertEquals(1, life.getInt("fs"));
        assertEquals(0, life.getInt("fsau"));
        assertEquals(1, life.getInt("sc"));
        assertEquals(Integer.parseInt(today), life.getInt("fsd"));
        assertEquals(0, life.getInt("dsls"));
        assertEquals(0, life.getInt("dsfs"));

        assertTrue(life.isNull("dsu"));
        assertTrue(life.isNull("scsu"));
        assertTrue(life.isNull("fsdau"));
    }

    @Test
    public void afterUpdateTest() throws JSONException {
        LifeCycle.firstSessionInit(preferences, null);
        preferences.edit().putString(LifeCycle.VERSION_CODE_KEY, "test").apply();
        LifeCycle.isInitialized = false;
        LifeCycle.updateFirstSession(preferences);
        LifeCycle.newSessionInit(preferences);

        JSONObject obj = new JSONObject(LifeCycle.getMetrics(preferences).execute());
        JSONObject life = obj.getJSONObject("lifecycle");

        assertEquals(0, life.getInt("fs"));
        assertEquals(1, life.getInt("fsau"));
        assertEquals(2, life.getInt("sc"));
        assertEquals(Integer.parseInt(today), life.getInt("fsd"));
        assertEquals(0, life.getInt("dsls"));
        assertEquals(0, life.getInt("dsfs"));
        assertEquals(0, life.getInt("dsu"));
        assertEquals(1, life.getInt("scsu"));
        assertEquals(Integer.parseInt(today), life.getInt("fsdau"));
    }
}
