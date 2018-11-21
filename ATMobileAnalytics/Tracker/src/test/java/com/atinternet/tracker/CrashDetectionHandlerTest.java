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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class CrashDetectionHandlerTest extends AbstractTestClass {

    private CrashDetectionHandler crashDetectionHandler;
    private SharedPreferences preferences;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        crashDetectionHandler = new CrashDetectionHandler(RuntimeEnvironment.application, Thread.getDefaultUncaughtExceptionHandler());
        preferences = RuntimeEnvironment.application.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE);
    }

    @Test
    public void multiInstanceTest() {
        CrashDetectionHandler crashDetectionHandler = new CrashDetectionHandler(RuntimeEnvironment.application, Thread.getDefaultUncaughtExceptionHandler());
        assertNotSame(this.crashDetectionHandler, crashDetectionHandler);
    }

    @Test
    public void getCrashInformationTest() throws JSONException {
        preferences.edit().putBoolean("CrashDetection", true).apply();
        preferences.edit().putString("CrashLastScreen", "LastScreen").apply();
        preferences.edit().putString("CrashClassCause", "Class").apply();
        preferences.edit().putString("CrashExceptionName", Throwable.class.getName()).apply();

        JSONObject json = new JSONObject(CrashDetectionHandler.getCrashInformation(RuntimeEnvironment.application.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, Context.MODE_PRIVATE)).execute());

        assertNotNull(json.getJSONObject("crash"));
        assertEquals(json.getJSONObject("crash").get("lastscreen"), "LastScreen");
        assertEquals(json.getJSONObject("crash").get("classname"), "Class");
        assertEquals(json.getJSONObject("crash").get("error"), "java.lang.Throwable");
    }

    @Test
    public void setCrashLastScreenTest() throws NoSuchFieldException, IllegalAccessException {
        CrashDetectionHandler.setCrashLastScreen("Screen");
        assertEquals("Screen", getAccessibleField(crashDetectionHandler, "lastScreen"));
    }

    @Test
    public void getClassNameTest() throws Exception {
        assertNotNull(executePrivateMethod(crashDetectionHandler, "getClassNameException", new Object[]{new Throwable()}));
    }
}
