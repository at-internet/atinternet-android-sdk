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

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DynamicScreenTest extends AbstractTestClass {

    private DynamicScreen dynamicScreen;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dynamicScreen = new DynamicScreen(tracker);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertEquals(-1, dynamicScreen.getScreenId());
        assertNull(dynamicScreen.getChapter1());
        assertNull(dynamicScreen.getUpdate());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(dynamicScreen, new DynamicScreen(tracker));
    }

    @Test
    public void setTest() {
        Date date = new Date();
        assertEquals(123, dynamicScreen.setScreenId(123).getScreenId());
        assertEquals("chapter1", dynamicScreen.setChapter1("chapter1").getChapter1());
        assertEquals("chapter2", dynamicScreen.setChapter2("chapter2").getChapter2());
        assertEquals("chapter3", dynamicScreen.setChapter3("chapter3").getChapter3());
        assertEquals(date, dynamicScreen.setUpdate(date).getUpdate());
    }

    @Test
    public void setEventTest() {
        Date date = new Date();
        dynamicScreen.setScreenId(123).setName("name").setChapter1("chapter1").setUpdate(date).setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals("pid", buffer.getVolatileParams().get(0).getKey());
        assertEquals("123", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("pchap", buffer.getVolatileParams().get(1).getKey());
        assertEquals("chapter1", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("pidt", buffer.getVolatileParams().get(2).getKey());
        assertEquals(new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(date), buffer.getVolatileParams().get(2).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(3).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(3).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(4).getKey());
        assertEquals("view", buffer.getVolatileParams().get(4).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(5).getKey());
        assertEquals("name", buffer.getVolatileParams().get(5).getValue().execute());
    }

    @Test
    public void setEventWithChapter2Test() {
        Date date = new Date();
        dynamicScreen.setScreenId(123).setName("toto").setChapter1("toto").setChapter2("tata").setUpdate(date).setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals("pid", buffer.getVolatileParams().get(0).getKey());
        assertEquals("123", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("pchap", buffer.getVolatileParams().get(1).getKey());
        assertEquals("toto::tata", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("pidt", buffer.getVolatileParams().get(2).getKey());
        assertEquals(new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(date), buffer.getVolatileParams().get(2).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(3).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(3).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(4).getKey());
        assertEquals("view", buffer.getVolatileParams().get(4).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(5).getKey());
        assertEquals("toto", buffer.getVolatileParams().get(5).getValue().execute());
    }
}
