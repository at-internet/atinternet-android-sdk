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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ConfigurationTest extends AbstractTestClass {

    private Configuration defaultConfiguration;

    @Before
    public void setUp() {
        defaultConfiguration = new Configuration(RuntimeEnvironment.application);
    }

    @Test
    public void getDefaultConfigurationTest() {
        assertEquals(18, defaultConfiguration.size());
        assertEquals("", defaultConfiguration.get("log"));
        assertEquals("", defaultConfiguration.get("logSSL"));
        assertEquals("", defaultConfiguration.get("site"));
        assertEquals("required", defaultConfiguration.get("storage"));
        assertEquals("", defaultConfiguration.get("plugins"));
        assertEquals("xiti.com", defaultConfiguration.get("domain"));
        assertEquals("androidId", defaultConfiguration.get("identifier"));
        assertEquals(false, defaultConfiguration.get("secure"));
        assertEquals(false, defaultConfiguration.get("hashUserId"));
        assertEquals("/hit.xiti", defaultConfiguration.get("pixelPath"));
        assertEquals(true, defaultConfiguration.get("persistIdentifiedVisitor"));
        assertEquals("", defaultConfiguration.get("tvtURL"));
        assertEquals(10, defaultConfiguration.get("tvtVisitDuration"));
        assertEquals(true, defaultConfiguration.get("enableCrashDetection"));
        assertEquals(false, defaultConfiguration.get("campaignLastPersistence"));
        assertEquals(30, defaultConfiguration.get("campaignLifetime"));
        assertEquals(5, defaultConfiguration.get("tvtSpotValidityTime"));
        assertEquals(60, defaultConfiguration.get("sessionBackgroundDuration"));
    }

    @Test
    public void configurationWithDictionaryTest() {

        LinkedHashMap<String, Object> dictionary = new LinkedHashMap<String, Object>() {{
            put("log", "logtest");
            put("logSSL", "");
            put("site", "123456");
            put("domain", "test.com");
            put("pixelPath", "/test.xiti");
            put("secure", false);
            put("hashUserId", true);
            put("identifier", "androidId");
        }};

        defaultConfiguration = new Configuration(dictionary);

        assertEquals(18, defaultConfiguration.size());
        assertEquals("logtest", defaultConfiguration.get("log"));
        assertEquals("", defaultConfiguration.get("logSSL"));
        assertEquals("123456", defaultConfiguration.get("site"));
        assertEquals("required", defaultConfiguration.get("storage"));
        assertEquals("", defaultConfiguration.get("plugins"));
        assertEquals("test.com", defaultConfiguration.get("domain"));
        assertEquals("androidId", defaultConfiguration.get("identifier"));
        assertEquals(false, defaultConfiguration.get("secure"));
        assertEquals(true, defaultConfiguration.get("hashUserId"));
        assertEquals("/test.xiti", defaultConfiguration.get("pixelPath"));
        assertEquals(true, defaultConfiguration.get("persistIdentifiedVisitor"));
        assertEquals("", defaultConfiguration.get("tvtURL"));
        assertEquals(10, defaultConfiguration.get("tvtVisitDuration"));
        assertEquals(true, defaultConfiguration.get("enableCrashDetection"));
        assertEquals(false, defaultConfiguration.get("campaignLastPersistence"));
        assertEquals(30, defaultConfiguration.get("campaignLifetime"));
        assertEquals(5, defaultConfiguration.get("tvtSpotValidityTime"));
        assertEquals(60, defaultConfiguration.get("sessionBackgroundDuration"));
    }
}