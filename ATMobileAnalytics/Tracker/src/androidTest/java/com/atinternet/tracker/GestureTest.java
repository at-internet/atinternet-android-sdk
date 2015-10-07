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

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class GestureTest extends AbstractTestClass {

    private Gesture gesture;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gesture = new Gesture(tracker);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertEquals("", gesture.getName());
        assertEquals(-1, gesture.getLevel2());
        assertEquals(Gesture.Action.Touch, gesture.getAction());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(gesture, new Screen(tracker));
    }

    @Test
    public void setTest() {
        assertEquals("toto", gesture.setName("toto").getName());
        assertEquals(Gesture.Action.Download, gesture.setAction(Gesture.Action.Download).getAction());
        assertEquals(123, gesture.setLevel2(123).getLevel2());
    }

    @Test
    public void setEventTest() {
        gesture.setName("gesture").setEvent();
        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("click", buffer.getVolatileParams().get(0).getKey());
        assertEquals("A", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(1).getKey());
        assertEquals("click", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(2).getKey());
        assertEquals("A", buffer.getVolatileParams().get(2).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(3).getKey());
        assertEquals("gesture", buffer.getVolatileParams().get(3).getValue().execute());
    }
}
