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

import android.app.Application;
import android.text.format.DateFormat;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static com.atinternet.tracker.Tracker.OfflineMode;
import static com.atinternet.tracker.Tracker.getAppContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("unchecked")
public class TrackerTest extends AbstractTestClass {

    private final String key = "KEY";
    private SetConfigCallback callback;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        today = String.valueOf(DateFormat.format(pattern, System.currentTimeMillis()));
        callback = new SetConfigCallback() {
            @Override
            public void setConfigEnd() {

            }
        };
    }

    @Test
    public void Constructor() {
        new Tracker();
        assertTrue(Tracker.getAppContext() instanceof Application);

        new Tracker(ApplicationProvider.getApplicationContext());
        assertTrue(Tracker.getAppContext() instanceof Application);

        new Tracker(new HashMap<String, Object>());
        assertTrue(Tracker.getAppContext() instanceof Application);

        new Tracker(ApplicationProvider.getApplicationContext(), new HashMap<String, Object>());
        assertTrue(Tracker.getAppContext() instanceof Application);
    }


    @Test
    public void getContextTest() {
        assertEquals(ApplicationProvider.getApplicationContext(), getAppContext());
    }

    @Test
    public void getUserIdSyncTest() {
        assertNull(tracker.getUserIdSync());
        tracker.setParam("idclient", "test");
        Builder builder = new Builder(tracker);
        builder.build().first.get(0);
        assertEquals("test", tracker.getUserIdSync());

        tracker.setHashUserIdEnabled(true, null, true);
        builder.build().first.get(0);
        assertEquals("8652e6fddc89d1392129e8f5ade37e4288406503e5b73bad51619d6e4f3ce50c", tracker.getUserIdSync());

        TechnicalContext.optOut(ApplicationProvider.getApplicationContext(), true);
        builder.build().first.get(0);
        assertEquals("opt-out", tracker.getUserIdSync());

    }

    @Test
    public void setConfigSyncTest() {
        assertFalse(tracker.getConfiguration().containsKey("key"));
        tracker.setConfig("key", "value", null, true);
        assertTrue(tracker.getConfiguration().containsKey("key"));
        assertEquals(tracker.getConfiguration().get("key"), "value");
    }

    @Test
    public void setLogSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG), "logp");
        tracker.setLog("logtest", null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG), "logtest");
    }

    @Test
    public void setSecuredLogSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG_SSL), "logs");
        tracker.setSecuredLog("logstest", null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG_SSL), "logstest");
    }

    @Test
    public void setDomainSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.DOMAIN), "xiti.com");
        tracker.setDomain("domain", null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.DOMAIN), "domain");
    }

    @Test
    public void setSiteIdSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SITE), 552987);
        tracker.setSiteId(2, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SITE), 2);
    }

    @Test
    public void setOfflineModeSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.OFFLINE_MODE), "never");
        tracker.setOfflineMode(OfflineMode.always, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.OFFLINE_MODE), "always");
    }

    @Test
    public void setPluginsSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), "");
        tracker.setPlugins(new ArrayList<Tracker.PluginKey>() {{
            add(Tracker.PluginKey.nuggad);
        }}, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), Tracker.PluginKey.nuggad.toString());
        tracker.setPlugins(null, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), "");
    }

    @Test
    public void setSecureSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SECURE), false);
        tracker.setSecureModeEnabled(true, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SECURE), false);
    }

    @Test
    public void setIdentifierSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "androidId");
        tracker.setIdentifierType(Tracker.IdentifierType.advertisingId, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "advertisingId");
        tracker.setIdentifierType(Tracker.IdentifierType.UUID, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "UUID");
        tracker.setIdentifierType(Tracker.IdentifierType.googleAdId, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "googleAdId");
        tracker.setIdentifierType(Tracker.IdentifierType.huaweiOAId, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "huaweiOAId");
    }

    @Test
    public void setHashUserIdModeSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.HASH_USER_ID), false);
        tracker.setHashUserIdEnabled(true, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.HASH_USER_ID), true);
    }

    @Test
    public void setPixelPathSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PIXEL_PATH), "/hit.xiti");
        tracker.setPixelPath("/test.xiti", null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PIXEL_PATH), "/test.xiti");
    }

    @Test
    public void setPersistIdentifiedVisitorSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR), true);
        tracker.setPersistentIdentifiedVisitorEnabled(false, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR), false);
    }

    @Test
    public void setCrashDetectionSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION), true);
        tracker.setCrashDetectionEnabled(false, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION), false);
    }

    @Test
    public void setCampaignLastPersistenceSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE), false);
        tracker.setCampaignLastPersistenceEnabled(true, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE), true);
    }

    @Test
    public void setCampaigLifetimeSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LIFETIME), 30);
        tracker.setCampaignLifetime(2, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LIFETIME), 2);
    }

    @Test
    public void setSessionBackgroundDurationSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION), 60);
        tracker.setSessionBackgroundDuration(2, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION), 2);
    }

    @Test
    public void setIgnoreLimitedAdTrackingEnabledSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IGNORE_LIMITED_AD_TRACKING), false);
        tracker.setIgnoreLimitedAdTrackingEnabled(true, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IGNORE_LIMITED_AD_TRACKING), true);
    }

    @Test
    public void setSendHitWhenOptOutEnabledSyncTest() {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SEND_HIT_WHEN_OPT_OUT), true);
        tracker.setSendHitWhenOptOutEnabled(false, null, true);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SEND_HIT_WHEN_OPT_OUT), false);
    }

    @Test
    public void ScreensTest() {
        Screens screens = tracker.Screens();
        assertNotNull(tracker.Screens());
        assertEquals(tracker.Screens(), screens);
    }

    @Test
    public void EventTest() {
        Events events = tracker.Events();
        assertNotNull(tracker.Events());
        assertEquals(tracker.Events(), events);
    }

    @Test
    public void OfflineTest() {
        assertNotNull(tracker.Offline());
        Offline offline = tracker.Offline();
        assertEquals(tracker.Offline(), offline);
    }

    @Test
    public void GesturesTest() {
        Gestures gestures = tracker.Gestures();
        assertNotNull(tracker.Gestures());
        assertEquals(tracker.Gestures(), gestures);
    }

    @Test
    public void ContextTest() {
        Context context = tracker.Context();
        assertNotNull(tracker.Context());
        assertEquals(tracker.Context(), context);
    }

    @Test
    public void IdentifiedVisitorTest() {
        IdentifiedVisitor identifiedVisitor = tracker.IdentifiedVisitor();
        assertNotNull(tracker.IdentifiedVisitor());
        assertEquals(tracker.IdentifiedVisitor(), identifiedVisitor);
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = tracker.CustomObjects();
        assertNotNull(tracker.CustomObjects());
        assertEquals(tracker.CustomObjects(), customObjects);
    }

    @Test
    public void PublishersTest() {
        Publishers publishers = tracker.Publishers();
        assertNotNull(tracker.Publishers());
        assertEquals(tracker.Publishers(), publishers);
    }

    @Test
    public void SelfPromotionsTest() {
        SelfPromotions selfPromotions = tracker.SelfPromotions();
        assertNotNull(tracker.SelfPromotions());
        assertEquals(tracker.SelfPromotions(), selfPromotions);
    }

    @Test
    public void CartTest() {
        Cart cart = tracker.Cart();
        assertNotNull(tracker.Cart());
        assertEquals(tracker.Cart(), cart);
    }

    @Test
    public void NuggAdsTest() {
        NuggAds nuggads = tracker.NuggAds();
        assertNotNull(tracker.NuggAds());
        assertEquals(tracker.NuggAds(), nuggads);
    }

    @Test
    public void DynamicScreensTest() {
        DynamicScreens dynamicScreens = tracker.DynamicScreens();
        assertNotNull(tracker.DynamicScreens());
        assertEquals(tracker.DynamicScreens(), dynamicScreens);
    }

    @Test
    public void CustomVarsTest() {
        CustomVars customVars = tracker.CustomVars();
        assertNotNull(tracker.CustomVars());
        assertEquals(tracker.CustomVars(), customVars);
    }

    @Test
    public void OrdersTest() {
        Orders orders = tracker.Orders();
        assertNotNull(tracker.Orders());
        assertEquals(tracker.Orders(), orders);
    }

    @Test
    public void AislesTest() {
        Aisles aisles = tracker.Aisles();
        assertNotNull(tracker.Aisles());
        assertEquals(tracker.Aisles(), aisles);
    }

    @Test
    public void CampaignsTest() {
        Campaigns campaigns = tracker.Campaigns();
        assertNotNull(tracker.Campaigns());
        assertEquals(tracker.Campaigns(), campaigns);
    }

    @Test
    public void LocationsTest() {
        Locations locations = tracker.Locations();
        assertNotNull(tracker.Locations());
        assertEquals(tracker.Locations(), locations);
    }

    @Test
    public void InternalSearchesTest() {
        InternalSearches internalSearches = tracker.InternalSearches();
        assertNotNull(tracker.InternalSearches());
        assertEquals(tracker.InternalSearches(), internalSearches);
    }

    @Test
    public void CustomTreeStructureTest() {
        CustomTreeStructures customTreeStructures = tracker.CustomTreeStructures();
        assertNotNull(tracker.CustomTreeStructures());
        assertEquals(tracker.CustomTreeStructures(), customTreeStructures);
    }

    @Test
    public void ProductsTest() {
        Products products = tracker.Products();
        assertNotNull(tracker.Products());
        assertEquals(tracker.Products(), products);
    }

    @Test
    public void PlayersTest() {
        MediaPlayers mediaPlayers = tracker.Players();
        assertNotNull(tracker.Players());
        assertEquals(tracker.Players(), mediaPlayers);
    }

    @Test
    public void toStringEnumTest() {
        assertEquals("always", OfflineMode.always.toString());
        assertEquals("required", OfflineMode.required.toString());
        assertEquals("never", OfflineMode.never.toString());

        assertEquals("androidId", Tracker.IdentifierType.androidId.toString());
        assertEquals("advertisingId", Tracker.IdentifierType.advertisingId.toString());

        assertEquals("nuggad", Tracker.PluginKey.nuggad.toString());
    }

    @Test
    public void setParamIntTest() {
        tracker.setParam(key, 1);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("1", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamBooleanTest() {
        tracker.setParam(key, true);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("true", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamFloatTest() {
        tracker.setParam(key, 1.f);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals(String.valueOf(1.f), buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamDoubleTest() {
        tracker.setParam(key, 1.5);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("1.5", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamStringTest() {
        tracker.setParam(key, "StringValue");

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("StringValue", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamArrayTest() {
        ArrayList<Integer> array = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        tracker.setParam(key, array);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("1,2,3", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamTabTest() {
        Float[] tab = new Float[]{1.f, 2.f};
        tracker.setParam(key, tab);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("1.0,2.0", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamMapTest() {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>() {{
            put("boolean1", true);
            put("boolean2", false);
        }};
        tracker.setParam(key, map);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("{\"boolean2\":false,\"boolean1\":true}", buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamClosureTest() {
        Closure closure = new Closure() {
            @Override
            public String execute() {
                return String.valueOf(DateFormat.format(pattern, System.currentTimeMillis()));
            }
        };
        tracker.setParam(key, closure);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals(today, buffer.getVolatileParams().get(key).getValues().get(0).execute());
    }

    @Test
    public void setParamWithOptions() {
        ParamOption options = new ParamOption().setPersistent(true).setAppend(true);
        tracker.setParam(key, 1, options);

        assertEquals(0, buffer.getVolatileParams().size());
        assertEquals(1, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getPersistentParams().get(key).getValues().size());
        assertEquals(true, buffer.getPersistentParams().get(key).getOptions().isAppend());
        assertEquals("1", buffer.getPersistentParams().get(key).getValues().get(0).execute());

    }

    @Test
    public void setParamComplex() {
        ParamOption options = new ParamOption().setPersistent(true).setAppend(true);
        tracker.setParam(key, 1)
                .setParam(key, "test");
        tracker.setParam(key, 1, options)
                .setParam(key, 67, options);

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(1, buffer.getPersistentParams().size());

        assertEquals(3, buffer.getVolatileParams().get(key).getValues().size());
        assertEquals("test", buffer.getVolatileParams().get(key).getValues().get(0).execute());
        assertEquals("1", buffer.getVolatileParams().get(key).getValues().get(1).execute());
        assertEquals("67", buffer.getVolatileParams().get(key).getValues().get(2).execute());

        assertEquals(2, buffer.getPersistentParams().get(key).getValues().size());
        assertEquals(true, buffer.getPersistentParams().get(key).getOptions().isAppend());
        assertEquals("1", buffer.getPersistentParams().get(key).getValues().get(0).execute());
        assertEquals("67", buffer.getPersistentParams().get(key).getValues().get(1).execute());
    }

    @Test
    public void refreshConfigurationDependenciesTest() throws Exception {
        Configuration config = tracker.getConfiguration();
        config.put("identifier", "idTest");
        executePrivateMethod(tracker, "refreshConfigurationDependencies", new Object[0]);

    }

    @Test
    public void optOutTest() {
        Builder builder = new Builder(tracker);
        assertFalse(builder.build().first.get(0).contains("&idclient=" + "opt-out"));
        TechnicalContext.optOut(ApplicationProvider.getApplicationContext(), true);
        tracker.setParam("idclient", TechnicalContext.getUserId("androidId", false, 0, ""));
        builder = new Builder(tracker);
        String url = builder.build().first.get(0);
        String[] components = url.split("&idclient=");
        assertEquals("opt-out", components[1].split("&")[0]);
    }

    @Test
    public void emptyParameterTest() {
        tracker.setParam("test", "");
        Builder builder = new Builder(tracker);
        String url = builder.build().first.get(0);

        assertTrue(url.contains("&test="));
    }
}
