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
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class SelfPromotionTest extends AbstractTestClass {

    private SelfPromotion selfPromotion;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selfPromotion = new SelfPromotion(tracker);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertEquals(-1, selfPromotion.getAdId());
        assertNull(selfPromotion.getFormat());
        assertNull(selfPromotion.getProductId());
        assertEquals(SelfPromotion.Action.View, selfPromotion.getAction());
    }

    @Test
    public void setTest() {
        assertEquals(23, selfPromotion.setAdId(23).getAdId());
        assertEquals("test", selfPromotion.setFormat("test").getFormat());
        assertEquals("truc", selfPromotion.setProductId("truc").getProductId());
        assertEquals(SelfPromotion.Action.Touch, selfPromotion.setAction(SelfPromotion.Action.Touch).getAction());
    }

    @Test
    public void setEventImpressionTest() {
        selfPromotion.setAdId(98).setEvent();

        assertEquals(2, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("AT", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(1).getKey());
        assertEquals("INT-98-||", buffer.getVolatileParams().get(1).getValue().execute());
    }

    @Test
    public void setEventTouchTest() {
        selfPromotion.setAdId(98).setProductId("pdt").setAction(OnAppAd.Action.Touch).setEvent();

        assertEquals(2, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("AT", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("atc", buffer.getVolatileParams().get(1).getKey());
        assertEquals("INT-98-||pdt", buffer.getVolatileParams().get(1).getValue().execute());
    }
}
