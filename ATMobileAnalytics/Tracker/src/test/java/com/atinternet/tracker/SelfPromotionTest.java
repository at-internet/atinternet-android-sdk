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
public class SelfPromotionTest extends AbstractTestClass {

    private SelfPromotion selfPromotion;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        selfPromotion = new SelfPromotion(tracker);
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = selfPromotion.CustomObjects();
        Assert.assertNotNull(selfPromotion.CustomObjects());
        Assert.assertEquals(selfPromotion.CustomObjects(), customObjects);
    }

    @Test
    public void initTest() {
        assertEquals(-1, selfPromotion.getAdId());
        assertNull(selfPromotion.getFormat());
        assertNull(selfPromotion.getProductId());
        assertEquals(SelfPromotion.Action.View, selfPromotion.getAction());
    }

    @Test
    public void setParamsImpressionTest() {
        selfPromotion.setAdId(98).setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("INT-98-||", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
    }

    @Test
    public void setParamsWithCustomObjectsTest() {
        selfPromotion.setAdId(4).CustomObjects().add("{\"test\":\"value\"}");
        selfPromotion.setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{\"test\":\"value\"}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("INT-4-||", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
    }

    @Test
    public void setParamsTouchTest() {
        selfPromotion.setAdId(98).setProductId("pdt").setAction(OnAppAd.Action.Touch).setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("atc").getValues().size());
        assertEquals("INT-98-||pdt", buffer.getVolatileParams().get("atc").getValues().get(0).execute());
    }

    @Test
    public void multipleValuesTest() {
        new SelfPromotion(tracker).setAdId(98).setProductId("pdt").setAction(OnAppAd.Action.Touch).setParams();
        new SelfPromotion(tracker).setAdId(99).setProductId("pdt").setAction(OnAppAd.Action.Touch).setParams();
        new SelfPromotion(tracker).setAdId(100).setProductId("pdt").setAction(OnAppAd.Action.Touch).setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("AT", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(3, buffer.getVolatileParams().get("atc").getValues().size());
        assertEquals("INT-98-||pdt", buffer.getVolatileParams().get("atc").getValues().get(0).execute());
        assertEquals("INT-99-||pdt", buffer.getVolatileParams().get("atc").getValues().get(1).execute());
        assertEquals("INT-100-||pdt", buffer.getVolatileParams().get("atc").getValues().get(2).execute());
    }
}
