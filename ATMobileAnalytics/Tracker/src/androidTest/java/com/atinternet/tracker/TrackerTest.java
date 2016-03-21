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

import android.text.format.DateFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.atinternet.tracker.Tracker.OfflineMode;
import static com.atinternet.tracker.Tracker.doNotTrackEnabled;
import static com.atinternet.tracker.Tracker.getAppContext;
import static com.atinternet.tracker.Tracker.getStorage;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class TrackerTest extends AbstractTestClass {


    private Buffer buffer;
    private Storage storage;
    private final String key = "KEY";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        today = String.valueOf(DateFormat.format(pattern, System.currentTimeMillis()));
        storage = getStorage();
        buffer = tracker.getBuffer();
        buffer.getPersistentParams().clear();
        buffer.getVolatileParams().clear();
    }

    @Test
    public void initTrackerTest() {
        HashMap<String, Object> conf = new HashMap<String, Object>() {{
            put("storage", "always");
            put("storageduration", 2);
            put("enableCrashDetection", true);
        }};
        Tracker t = new Tracker(Robolectric.application, conf);
        assertNotNull(tracker);
        assertNotSame(tracker, t);
    }

    @Test
    public void getConfigurationTest() {
        assertEquals(18, tracker.getConfiguration().size());
        assertTrue(tracker.getConfiguration().containsKey("log"));
        assertTrue(tracker.getConfiguration().containsKey("logSSL"));
        assertTrue(tracker.getConfiguration().containsKey("site"));
        assertTrue(tracker.getConfiguration().containsKey("domain"));
        assertTrue(tracker.getConfiguration().containsKey("pixelPath"));
        assertTrue(tracker.getConfiguration().containsKey("storage"));
        assertTrue(tracker.getConfiguration().containsKey("plugins"));
        assertTrue(tracker.getConfiguration().containsKey("identifier"));
        assertTrue(tracker.getConfiguration().containsKey("secure"));
        assertTrue(tracker.getConfiguration().containsKey("hashUserId"));
        assertTrue(tracker.getConfiguration().containsKey("persistIdentifiedVisitor"));
        assertTrue(tracker.getConfiguration().containsKey("tvtURL"));
        assertTrue(tracker.getConfiguration().containsKey("tvtVisitDuration"));
        assertTrue(tracker.getConfiguration().containsKey("enableCrashDetection"));
        assertTrue(tracker.getConfiguration().containsKey("campaignLastPersistence"));
        assertTrue(tracker.getConfiguration().containsKey("campaignLifetime"));
        assertTrue(tracker.getConfiguration().containsKey("tvtSpotValidityTime"));
        assertTrue(tracker.getConfiguration().containsKey("sessionBackgroundDuration"));
    }

    @Test
    public void getContextTest() {
        assertEquals(Robolectric.application, getAppContext());
    }

    @Test
    public void getListenerTest() {
        assertNull(tracker.getListener());
    }

    @Test
    public void setListenerTest() {
        TrackerListener listener = new TrackerListener() {
            @Override
            public void trackerNeedsFirstLaunchApproval(String message) {

            }

            @Override
            public void buildDidEnd(HitStatus status, String message) {

            }

            @Override
            public void sendDidEnd(HitStatus status, String message) {

            }

            @Override
            public void didCallPartner(String response) {

            }

            @Override
            public void warningDidOccur(String message) {

            }

            @Override
            public void saveDidEnd(String message) {

            }

            @Override
            public void errorDidOccur(String message) {

            }
        };
        tracker.setListener(listener);
        assertNotNull(tracker.getListener());
        assertEquals(listener, tracker.getListener());
    }

    @Test
    public void setConfigTest() throws Exception {
        assertFalse(tracker.getConfiguration().containsKey("key"));
        tracker.setConfig("key", "value", null);
        Thread.sleep(200);
        assertTrue(tracker.getConfiguration().containsKey("key"));
        assertEquals(tracker.getConfiguration().get("key"), "value");
    }

    @Test
    public void setLogTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG), "logp");
        tracker.setLog("logtest", null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG), "logtest");
    }

    @Test
    public void setSecuredLogTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG_SSL), "logs");
        tracker.setSecuredLog("logstest", null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.LOG_SSL), "logstest");
    }

    @Test
    public void setDomainTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.DOMAIN), "xiti.com");
        tracker.setDomain("domain", null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.DOMAIN), "domain");
    }

    @Test
    public void setSiteIdTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SITE), 552987);
        tracker.setSiteId(123, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SITE), 123);
    }

    @Test
    public void setOfflineModeTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.OFFLINE_MODE), "required");
        tracker.setOfflineMode(OfflineMode.always, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.OFFLINE_MODE), "always");
    }

    @Test
    public void setPlugins() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), "");
        tracker.setPlugins(new ArrayList<Tracker.PluginKey>() {{
            add(Tracker.PluginKey.tvtracking);
        }}, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), Tracker.PluginKey.tvtracking.toString());
        tracker.setPlugins(new ArrayList<Tracker.PluginKey>() {{
            add(Tracker.PluginKey.tvtracking);
            add(Tracker.PluginKey.nuggad);
        }}, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), Tracker.PluginKey.tvtracking.toString() + "," + Tracker.PluginKey.nuggad.toString());
        tracker.setPlugins(null, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PLUGINS), "");
    }

    @Test
    public void setSecureTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SECURE), false);
        tracker.setSecureModeEnabled(true, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SECURE), true);
    }

    @Test
    public void setIdentifierTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "androidId");
        tracker.setIdentifierType(Tracker.IdentifierType.advertisingId, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.IDENTIFIER), "advertisingId");
    }

    @Test
    public void setHashUserIdModeTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.HASH_USER_ID), false);
        tracker.setHashUserIdEnabled(true, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.HASH_USER_ID), true);
    }

    @Test
    public void setPixelPathTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PIXEL_PATH), "/hit.xiti");
        tracker.setPixelPath("/test.xiti", null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PIXEL_PATH), "/test.xiti");
    }

    @Test
    public void setPersistIdentifiedVisitorTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR), true);
        tracker.setPersistentIdentifiedVisitorEnabled(false, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR), false);
    }

    @Test
    public void setTVTUrlTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_URL), "");
        tracker.setTvTrackingUrl("test.com", null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_URL), "test.com");
    }

    @Test
    public void setTVTVisitDurationTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_VISIT_DURATION), 10);
        tracker.setTvTrackingVisitDuration(5, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_VISIT_DURATION), 5);
    }

    @Test
    public void setTVTSpotValidityTimeTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_SPOT_VALIDITY_TIME), 5);
        tracker.setTvTrackingSpotValidityTime(3, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.TVTRACKING_SPOT_VALIDITY_TIME), 3);
    }

    @Test
    public void setCrashDetectionTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION), true);
        tracker.setCrashDetectionEnabled(false, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.ENABLE_CRASH_DETECTION), false);
    }

    @Test
    public void setCampaignLastPersistenceTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE), false);
        tracker.setCampaignLastPersistenceEnabled(true, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LAST_PERSISTENCE), true);
    }

    @Test
    public void setCampaigLifetimeTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LIFETIME), 30);
        tracker.setCampaignLifetime(10, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.CAMPAIGN_LIFETIME), 10);
    }

    @Test
    public void setSessionBackgroundDurationTest() throws Exception {
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION), 60);
        tracker.setSessionBackgroundDuration(15, null);
        Thread.sleep(200);
        assertEquals(tracker.getConfiguration().get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION), 15);
    }

    @Test
    public void getScreensPluginTest() {
        Screens screens = tracker.Screens();
        assertNotNull(tracker.Screens());
        assertEquals(tracker.Screens(), screens);
    }

    @Test
    public void getEventPluginTest() {
        Event event = tracker.Event();
        assertNotNull(tracker.Event());
        assertEquals(tracker.Event(), event);
    }

    @Test
    public void getOfflinePluginTest() {
        assertNotNull(tracker.Offline());
        Offline offline = tracker.Offline();
        assertEquals(tracker.Offline(), offline);
    }

    @Test
    public void getGesturesPluginTest() {
        Gestures gestures = tracker.Gestures();
        assertNotNull(tracker.Gestures());
        assertEquals(tracker.Gestures(), gestures);
    }

    @Test
    public void getContextPluginTest() {
        Context context = tracker.Context();
        assertNotNull(tracker.Context());
        assertEquals(tracker.Context(), context);
    }

    @Test
    public void getIdentifiedVisitorPluginTest() {
        IdentifiedVisitor identifiedVisitor = tracker.IdentifiedVisitor();
        assertNotNull(tracker.IdentifiedVisitor());
        assertEquals(tracker.IdentifiedVisitor(), identifiedVisitor);
    }

    @Test
    public void getCustomObjectsPluginTest() {
        CustomObjects customObjects = tracker.CustomObjects();
        assertNotNull(tracker.CustomObjects());
        assertEquals(tracker.CustomObjects(), customObjects);
    }

    @Test
    public void getPublishersPluginTest() {
        Publishers publishers = tracker.Publishers();
        assertNotNull(tracker.Publishers());
        assertEquals(tracker.Publishers(), publishers);
    }

    @Test
    public void getSelfPromotionsPluginTest() {
        SelfPromotions selfPromotions = tracker.SelfPromotions();
        assertNotNull(tracker.SelfPromotions());
        assertEquals(tracker.SelfPromotions(), selfPromotions);
    }

    @Test
    public void getCustomVarsPluginTest() {
        CustomVars customVars = tracker.CustomVars();
        assertNotNull(tracker.CustomVars());
        assertEquals(tracker.CustomVars(), customVars);
    }

    @Test
    public void getOrdersPluginTest() {
        Orders orders = tracker.Orders();
        assertNotNull(tracker.Orders());
        assertEquals(tracker.Orders(), orders);
    }

    @Test
    public void getCartPluginTest() {
        Cart cart = tracker.Cart();
        assertNotNull(tracker.Cart());
        assertEquals(tracker.Cart(), cart);
    }

    @Test
    public void getAislesPluginTest() {
        Aisles aisles = tracker.Aisles();
        assertNotNull(tracker.Aisles());
        assertEquals(tracker.Aisles(), aisles);
    }

    @Test
    public void getCampaignsPluginTest() {
        Campaigns campaigns = tracker.Campaigns();
        assertNotNull(tracker.Campaigns());
        assertEquals(tracker.Campaigns(), campaigns);
    }

    @Test
    public void getGPSPluginTest() {
        Locations locations = tracker.Locations();
        assertNotNull(tracker.Locations());
        assertEquals(tracker.Locations(), locations);
    }

    @Test
    public void getTVTrackingPluginTest() {
        TVTracking tvt = tracker.TVTracking();
        assertNotNull(tracker.TVTracking());
        assertEquals(tracker.TVTracking(), tvt);
    }

    @Test
    public void getNuggAdsPluginTest() {
        NuggAds nuggads = tracker.NuggAds();
        assertNotNull(tracker.NuggAds());
        assertEquals(tracker.NuggAds(), nuggads);
    }

    @Test
    public void getInternalSearchesPluginTest() {
        InternalSearches internalSearches = tracker.InternalSearches();
        assertNotNull(tracker.InternalSearches());
        assertEquals(tracker.InternalSearches(), internalSearches);
    }

    @Test
    public void getDynamicLabelsPluginTest() {
        DynamicScreens dynamicScreens = tracker.DynamicScreens();
        assertNotNull(tracker.DynamicScreens());
        assertEquals(tracker.DynamicScreens(), dynamicScreens);
    }

    @Test
    public void getCustomTreeStructurePluginTest() {
        CustomTreeStructures customTreeStructures = tracker.CustomTreeStructures();
        assertNotNull(tracker.CustomTreeStructures());
        assertEquals(tracker.CustomTreeStructures(), customTreeStructures);
    }

    @Test
    public void getProductsPluginTest() {
        Products products = tracker.Products();
        assertNotNull(tracker.Products());
        assertEquals(tracker.Products(), products);
    }

    @Test
    public void getPlayersPluginTest() {
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

        assertEquals("tvtracking", Tracker.PluginKey.tvtracking.toString());
        assertEquals("nuggad", Tracker.PluginKey.nuggad.toString());
    }

    @Test
    public void setParamIntTest() {
        tracker.setParam(key, 1);
        assertEquals("1", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamBooleanTest() {
        tracker.setParam(key, true);
        assertEquals("true", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamFloatTest() {
        tracker.setParam(key, 1.f);
        assertEquals("1.0", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamDoubleTest() {
        tracker.setParam(key, 1.);
        assertEquals("1.0", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamStringTest() {
        tracker.setParam(key, "StringValue");
        assertEquals("StringValue", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamArrayTest() {
        ArrayList<Integer> array = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        tracker.setParam(key, array);
        assertEquals("1,2,3", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamTabTest() {
        Float[] tab = new Float[]{1.f, 2.f};
        tracker.setParam(key, tab);
        assertEquals("1.0,2.0", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamMapTest() {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>() {{
            put("boolean1", true);
            put("boolean2", false);
        }};
        tracker.setParam(key, map);
        assertEquals("{\"boolean2\":false,\"boolean1\":true}", buffer.getVolatileParams().get(0).getValue().execute());
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
        assertEquals(today, buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void setParamIntWithOptions() {
        ParamOption options = new ParamOption().setPersistent(true);

        tracker.setParam(key, 1, options);
        assertEquals(0, buffer.getVolatileParams().size());
        assertEquals("1", buffer.getPersistentParams().get(0).getValue().execute());

    }

    @Test
    public void setParamBooleanWithOptions() {
        ParamOption options = new ParamOption().setPersistent(true);

        tracker.setParam(key, true, options);
        assertEquals(0, buffer.getVolatileParams().size());
        assertTrue(buffer.getPersistentParams().get(0).getOptions().isPersistent());
        assertEquals("true", buffer.getPersistentParams().get(0).getValue().execute());

    }

    @Test
    public void setParamFloatWithOptions() {
        ParamOption options = new ParamOption().setRelativePosition(ParamOption.RelativePosition.first);

        tracker.setParam(key, 1.f, options);
        assertEquals(0, buffer.getPersistentParams().size());
        assertEquals(ParamOption.RelativePosition.first, buffer.getVolatileParams().get(0).getOptions().getRelativePosition());

    }

    @Test
    public void setParamDoubleWithOptions() {
        ParamOption options = new ParamOption().setRelativePosition(ParamOption.RelativePosition.last);

        tracker.setParam(key, 1.0, options);
        assertEquals(0, buffer.getPersistentParams().size());
        assertEquals(ParamOption.RelativePosition.last, buffer.getVolatileParams().get(0).getOptions().getRelativePosition());
    }

    @Test
    public void setParamStringWithOptions() {
        ParamOption options = new ParamOption()
                .setEncode(true)
                .setPersistent(true);

        tracker.setParam(key, " encoding ", options);
        assertEquals(0, buffer.getVolatileParams().size());
        assertTrue(buffer.getPersistentParams().get(0).getOptions().isEncode() && buffer.getPersistentParams().get(0).getOptions().isPersistent());
    }

    @Test
    public void setParamArrayWithOptionsTest() {
        ParamOption options = new ParamOption().setSeparator("_");
        ArrayList<Integer> array = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};

        tracker.setParam(key, array, options);
        assertEquals(0, buffer.getPersistentParams().size());
        assertEquals("_", buffer.getVolatileParams().get(0).getOptions().getSeparator());
    }

    @Test
    public void setParamTabWithOptionsTest() {
        Float[] tab = new Float[]{1.f, 2.f};

        ParamOption options = new ParamOption()
                .setSeparator("///");
        tracker.setParam(key, tab, options);
        assertEquals(0, buffer.getPersistentParams().size());
        assertEquals("///", buffer.getVolatileParams().get(0).getOptions().getSeparator());
    }

    @Test
    public void setParamMapWithOptionsTest() {
        LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>() {{
            put("boolean1", true);
            put("boolean2", false);
        }};

        ParamOption options = new ParamOption()
                .setRelativePosition(ParamOption.RelativePosition.before)
                .setRelativeParameterKey("REFKEY");
        tracker.setParam(key, map, options);

        ParamOption paramOptions = buffer.getVolatileParams().get(0).getOptions();
        assertTrue(paramOptions.getRelativePosition() == ParamOption.RelativePosition.before);
        assertEquals("REFKEY", paramOptions.getRelativeParameterKey());
    }

    @Test
    public void setParamClosureWithOptionsTest() {
        ParamOption options = new ParamOption().setEncode(true);
        final int count = 2;
        Closure closure = new Closure() {
            @Override
            public String execute() {
                return String.valueOf(count * 2);
            }
        };

        tracker.setParam(key, closure, options);
        assertTrue(buffer.getVolatileParams().get(0).getOptions().isEncode());
        assertEquals("4", buffer.getVolatileParams().get(0).getValue().execute());
    }

    @Test
    public void refreshConfigurationDependenciesTest() throws Exception {
        Configuration config = tracker.getConfiguration();
        config.put("identifier", "idTest");
        config.put("storage", "always");
        executePrivateMethod(tracker, "refreshConfigurationDependencies", new Object[0]);
        assertEquals(OfflineMode.always, storage.getOfflineMode());

    }

    @Test
    public void doNotTrackTest() {
        Builder builder = new Builder(tracker);
        assertFalse(((ArrayList<String>) builder.build()[0]).get(0).contains("&idclient=" + "opt-out"));
        TechnicalContext.doNotTrack(Robolectric.application, true);
        tracker.setParam("idclient", TechnicalContext.getUserId("androidId"));
        builder = new Builder(tracker);
        String url = ((ArrayList<String>) builder.build()[0]).get(0);
        String[] components = url.split("&idclient=");
        assertEquals("opt-out", components[1].split("&")[0]);
    }

    @Test
    public void emptyParameterTest() {
        tracker.setParam("test", "");
        Builder builder = new Builder(tracker);
        String url = ((ArrayList<String>) builder.build()[0]).get(0);

        assertTrue(url.contains("&test="));
    }

    @Test
    public void doNotTrackEnabledTest() {
        assertFalse(doNotTrackEnabled());
    }
}
