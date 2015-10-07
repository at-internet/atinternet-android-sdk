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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest extends AbstractTestClass {

    private Screen screen;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        screen = new Screen(tracker);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertEquals("", screen.getName());
        assertEquals(-1, screen.getLevel2());
        assertEquals(Screen.Action.View, screen.getAction());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(screen, new Screen(tracker));
    }

    @Test
    public void setTest() {
        assertEquals("name", screen.setName("name").getName());
        assertEquals(123, screen.setLevel2(123).getLevel2());
        assertTrue(screen.setIsBasketScreen(true).isBasketScreen());
    }

    @Test
    public void setEventTest() {
        screen.setName("name").setChapter3("chapter3").setEvent();
        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(1).getKey());
        assertEquals("view", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(2).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(2).getValue().execute());
    }

    @Test
    public void setEventOnlyChaptersTest() {
        screen.setChapter1("chapter1").setChapter3("chapter3").setEvent();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(1).getKey());
        assertEquals("view", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(2).getKey());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get(2).getValue().execute());
    }
}
