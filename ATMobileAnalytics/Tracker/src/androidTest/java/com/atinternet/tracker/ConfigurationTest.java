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

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ConfigurationTest extends AbstractTestClass {

    private LinkedHashMap<String, Object> emulatePhoneConfig;
    private LinkedHashMap<String, Object> emulateTabletConfig;

    // On initialise une fausse configuration
    @Before
    public void setUp() throws IOException, JSONException {
        emulatePhoneConfig = createDefaultConfiguration(false);
        emulateTabletConfig = createDefaultConfiguration(true);
    }

    // On vérifie que la création d'une configuration par défaut pour téléphone possède bien les valeurs présentes dans le fichier JSON
    @Test
    public void phoneConfigurationTest() {
        assertEquals(19, emulatePhoneConfig.size());
        assertEquals("logp", emulatePhoneConfig.get("log"));
        assertEquals("logsPhone", emulatePhoneConfig.get("logSSL"));
        assertEquals(123, emulatePhoneConfig.get("site"));
        assertEquals("required", emulatePhoneConfig.get("storage"));
        assertEquals(4, emulatePhoneConfig.get("storageduration"));
        assertEquals("nuggad,tvtracking", emulatePhoneConfig.get("plugins").toString());
        assertEquals("xiti.com", emulatePhoneConfig.get("domain"));
        assertEquals("androidId", emulatePhoneConfig.get("identifier"));
        assertEquals(false, emulatePhoneConfig.get("secure"));
        assertEquals(false, emulatePhoneConfig.get("hashUserId"));
        assertEquals("/hit.xiti", emulatePhoneConfig.get("pixelPath"));
        assertEquals(false, emulatePhoneConfig.get("persistIdentifiedVisitor"));
        assertEquals("test.com", emulatePhoneConfig.get("tvtURL"));
        assertEquals(12, emulatePhoneConfig.get("tvtVisitDuration"));
        assertEquals(true, emulatePhoneConfig.get("enableCrashDetection"));
        assertEquals(true, emulatePhoneConfig.get("campaignLastPersistence"));
        assertEquals(10, emulatePhoneConfig.get("campaignLifetime"));
        assertEquals(5, emulatePhoneConfig.get("tvtSpotValidityTime"));
        assertEquals("int", emulatePhoneConfig.get("downloadSource"));
    }

    // On vérifie que la création d'une configuration par défaut pour tablette possède bien les valeurs présentes dans le fichier JSON
    @Test
    public void tabletConfigurationTest() {
        assertEquals(19, emulateTabletConfig.size());
        assertEquals("logTablet", emulateTabletConfig.get("log"));
        assertEquals("logsTablet", emulateTabletConfig.get("logSSL"));
        assertEquals(456, emulateTabletConfig.get("site"));
        assertEquals("required", emulatePhoneConfig.get("storage"));
        assertEquals(4, emulatePhoneConfig.get("storageduration"));
        assertEquals("xiti.com", emulateTabletConfig.get("domain"));
        assertEquals("nuggad,tvtracking", emulatePhoneConfig.get("plugins").toString());
        assertEquals("androidId", emulateTabletConfig.get("identifier"));
        assertEquals(false, emulateTabletConfig.get("secure"));
        assertEquals(false, emulatePhoneConfig.get("hashUserId"));
        assertEquals("/hit.xiti", emulateTabletConfig.get("pixelPath"));
        assertEquals(false, emulatePhoneConfig.get("persistIdentifiedVisitor"));
        assertEquals("test.com", emulatePhoneConfig.get("tvtURL"));
        assertEquals(12, emulatePhoneConfig.get("tvtVisitDuration"));
        assertEquals(true, emulatePhoneConfig.get("enableCrashDetection"));
        assertEquals(true, emulatePhoneConfig.get("campaignLastPersistence"));
        assertEquals(10, emulatePhoneConfig.get("campaignLifetime"));
        assertEquals(5, emulatePhoneConfig.get("tvtSpotValidityTime"));
        assertEquals("int", emulatePhoneConfig.get("downloadSource"));
    }

    // On vérifie que la configuration faite par le client écrase la configuration par défaut
    @Test
    public void configurationWithDictionaryTest() {
        Configuration config = new Configuration(emulatePhoneConfig);

        LinkedHashMap<String, Object> dictionary = new LinkedHashMap<String, Object>();
        dictionary.put("log", "logp");
        dictionary.put("logs", "");
        dictionary.put("site", "552987");
        dictionary.put("domain", "xiti.com");
        dictionary.put("pixelPath", "/hit.xiti");
        dictionary.put("secure", false);
        dictionary.put("hashUserId", true);
        dictionary.put("identifier", "androidId");

        config = new Configuration(dictionary);

        assertEquals("logp", config.get("log"));
        assertEquals("", config.get("logs"));
        assertEquals("552987", config.get("site"));
        assertFalse((Boolean) config.get("secure"));
        assertTrue((Boolean) config.get("hashUserId"));
        assertEquals("/hit.xiti", config.get("pixelPath"));
        assertEquals("xiti.com", config.get("domain"));
        assertEquals("androidId", config.get("identifier"));
        assertEquals(8, config.size());
    }
}