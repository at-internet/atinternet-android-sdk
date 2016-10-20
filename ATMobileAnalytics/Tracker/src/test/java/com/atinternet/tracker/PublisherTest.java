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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class PublisherTest extends AbstractTestClass {

    private Publisher publisher;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        publisher = new Publisher(tracker);
        buffer = tracker.getBuffer();
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
    public void setTest() {
        Random r = new Random();
        String[] vals = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;

        assertEquals(vals[i], publisher.setCampaignId(vals[i++]).getCampaignId());
        assertEquals(vals[i], publisher.setAdvertiserId(vals[i++]).getAdvertiserId());
        assertEquals(vals[i], publisher.setCreation(vals[i++]).getCreation());
        assertEquals(vals[i], publisher.setFormat(vals[i++]).getFormat());
        assertEquals(vals[i], publisher.setVariant(vals[i++]).getVariant());
        assertEquals(vals[i], publisher.setGeneralPlacement(vals[i++]).getGeneralPlacement());
        assertEquals(vals[i], publisher.setDetailedPlacement(vals[i++]).getDetailedPlacement());
        assertEquals(vals[i], publisher.setUrl(vals[i]).getUrl());
        assertEquals(OnAppAd.Action.Touch, publisher.setAction(OnAppAd.Action.Touch).getAction());
    }

    @Test
    public void setEventImpressionTest() {
        publisher.setCampaignId("[pub]").setEvent();

        assertEquals(2, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("AT", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(1).getKey());
        assertEquals("PUB-[pub]-------", buffer.getVolatileParams().get(1).getValue().execute());
    }

    @Test
    public void setEventWithCustomObjectsTest() {
        publisher.setCampaignId("[pub]").CustomObjects().add("{\"test\":\"value\"}");
        publisher.setEvent();

        assertEquals(3, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("AT", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(1).getKey());
        assertEquals("{\"test\":\"value\"}", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(2).getKey());
        assertEquals("PUB-[pub]-------", buffer.getVolatileParams().get(2).getValue().execute());
    }

    @Test
    public void setEventTouchTest() {
        publisher.setCampaignId("[pub]").setGeneralPlacement("[pub2]").setAction(OnAppAd.Action.Touch).setEvent();

        assertEquals(2, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("AT", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("atc", buffer.getVolatileParams().get(1).getKey());
        assertEquals("PUB-[pub]----[pub2]---", buffer.getVolatileParams().get(1).getValue().execute());
    }
}
