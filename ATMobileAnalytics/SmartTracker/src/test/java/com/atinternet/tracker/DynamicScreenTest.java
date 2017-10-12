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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class DynamicScreenTest extends AbstractTestClass {

    private DynamicScreen dynamicScreen;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dynamicScreen = new DynamicScreen(tracker);
    }

    @Test
    public void initTest() {
        assertNull(dynamicScreen.getScreenId());
        assertNull(dynamicScreen.getChapter1());
        assertNull(dynamicScreen.getUpdate());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(dynamicScreen, new DynamicScreen(tracker));
    }

    @Test
    public void setEventTest() {
        Date date = new Date();
        dynamicScreen.setScreenId("test").setName("name").setChapter1("chapter1").setUpdate(date).setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("pid").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("pid").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pchap").getValues().size());
        assertEquals("chapter1", buffer.getVolatileParams().get("pchap").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pidt").getValues().size());
        assertEquals(new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(date), buffer.getVolatileParams().get("pidt").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("name", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setEventWithTooLongScreenIdTest() {
        Date date = new Date();
        String id = "";
        for (int i = 0; i < 256; i++) {
            id += i;
        }
        dynamicScreen.setScreenId(id).setName("name").setChapter1("chapter1").setUpdate(date).setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("pid").getValues().size());
        assertEquals("", buffer.getVolatileParams().get("pid").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pchap").getValues().size());
        assertEquals("chapter1", buffer.getVolatileParams().get("pchap").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pidt").getValues().size());
        assertEquals(new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(date), buffer.getVolatileParams().get("pidt").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("name", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setEventWithChapter2Test() {
        Date date = new Date();
        dynamicScreen.setScreenId("test").setName("name").setChapter1("chap1").setChapter2("chap2").setUpdate(date).setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("pid").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("pid").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pchap").getValues().size());
        assertEquals("chap1::chap2", buffer.getVolatileParams().get("pchap").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pidt").getValues().size());
        assertEquals(new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(date), buffer.getVolatileParams().get("pidt").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

    }
}
