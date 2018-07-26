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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class PublisherTest extends AbstractTestClass {

    private Publisher publisher;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        publisher = new Publisher(tracker);
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = publisher.CustomObjects();
        Assert.assertNotNull(publisher.CustomObjects());
        Assert.assertEquals(publisher.CustomObjects(), customObjects);
    }

    @Test
    public void initTest() {
        assertEquals("", publisher.getCampaignId());
        assertNull(publisher.getCreation());
        assertNull(publisher.getFormat());
        assertNull(publisher.getVariant());
        assertNull(publisher.getGeneralPlacement());
        assertNull(publisher.getDetailedPlacement());
        assertNull(publisher.getAdvertiserId());
        assertNull(publisher.getUrl());
        assertEquals(Publisher.Action.View, publisher.getAction());
    }

    @Test
    public void setParamsImpressionTest() {
        publisher.setCampaignId("[pub]").setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("PUB-[pub]-------", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
    }

    @Test
    public void setParamsWithCustomObjectsTest() {
        publisher.setCampaignId("[pub]").CustomObjects().add("{\"test\":\"value\"}");
        publisher.setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{\"test\":\"value\"}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("PUB-[pub]-------", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
    }

    @Test
    public void setParamsTouchTest() {
        publisher.setCampaignId("[pub]").setGeneralPlacement("[pub2]").setAction(OnAppAd.Action.Touch).setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("atc").getValues().size());
        assertEquals("PUB-[pub]----[pub2]---", buffer.getVolatileParams().get("atc").getValues().get(0).execute());
    }

    @Test
    public void multiplesValuesTest() {
        new Publisher(tracker).setCampaignId("[pub]").setParams();
        new Publisher(tracker).setCampaignId("[pub1]").setParams();
        new Publisher(tracker).setCampaignId("[pub2]").setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(3, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("PUB-[pub]-------", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
        assertEquals("PUB-[pub1]-------", buffer.getVolatileParams().get("ati").getValues().get(1).execute());
        assertEquals("PUB-[pub2]-------", buffer.getVolatileParams().get("ati").getValues().get(2).execute());
    }
}
