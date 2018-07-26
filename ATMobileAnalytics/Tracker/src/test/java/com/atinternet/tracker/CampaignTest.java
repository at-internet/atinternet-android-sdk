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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class CampaignTest extends AbstractTestClass {

    private Campaign campaign;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        campaign = new Campaign(tracker);
    }

    @Test
    public void initTest() {
        assertNull(campaign.getCampaignId());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(campaign, new Campaign(tracker));
    }

    @Test
    public void setParamsWithoutRemanenceTest() {
        assertNull(Tracker.getPreferences().getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null));

        campaign.setCampaignId("campaign")
                .setParams();

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("xto").getValues().size());
        assertEquals("campaign", buffer.getVolatileParams().get("xto").getValues().get(0).execute());
        assertEquals("campaign", Tracker.getPreferences().getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null));

    }

    @Test
    public void setParamsWithRemanenceTest() {
        Configuration conf = tracker.getConfiguration();
        conf.put("campaignLastPersistence", false);
        tracker = new Tracker(RuntimeEnvironment.application, conf);

        assertNull(Tracker.getPreferences().getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null));
        Tracker.getPreferences().edit().putString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, "campaign").apply();
        Tracker.getPreferences().edit().putLong(TrackerConfigurationKeys.LAST_MARKETING_CAMPAIGN_TIME, System.currentTimeMillis()).apply();
        assertEquals("campaign", Tracker.getPreferences().getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null));

        campaign.setCampaignId("test")
                .setParams();
        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("xto").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("xto").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("xtor").getValues().size());
        assertEquals("campaign", buffer.getVolatileParams().get("xtor").getValues().get(0).execute());

        assertEquals("campaign", Tracker.getPreferences().getString(TrackerConfigurationKeys.MARKETING_CAMPAIGN_SAVED, null));

    }
}
