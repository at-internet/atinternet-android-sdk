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

import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class GestureTest extends AbstractTestClass {

    private Gesture gesture;
    private Buffer buffer;
    private int i = 0;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gesture = new Gesture(tracker);
        buffer = tracker.getBuffer();
        i = 0;
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
    public void CustomObjectsTest() {
        CustomObjects customObjects = gesture.CustomObjects();
        Assert.assertNotNull(gesture.CustomObjects());
        Assert.assertEquals(gesture.CustomObjects(), customObjects);
    }

    @Test
    public void InternalSearchDeprecatedTest() {
        int id = new Random().nextInt(500);
        InternalSearch internalSearch = gesture.InternalSearch("test" + id, id);
        Assert.assertNotNull(internalSearch);
        Assert.assertEquals(gesture.InternalSearch("", 0), internalSearch);

        internalSearch = gesture.InternalSearch("", 0);

        Assert.assertEquals(internalSearch.getKeyword(), "test" + id);
        Assert.assertEquals(internalSearch.getResultScreenNumber(), id);
    }

    @Test
    public void InternalSearchTest() {
        int id = new Random().nextInt(500);
        InternalSearch internalSearch = gesture.InternalSearch("test" + id, id, id);
        Assert.assertNotNull(internalSearch);
        Assert.assertEquals(gesture.InternalSearch("", 0, 0), internalSearch);

        internalSearch = gesture.InternalSearch("", 0, 0);

        Assert.assertEquals(internalSearch.getKeyword(), "test" + id);
        Assert.assertEquals(internalSearch.getResultScreenNumber(), id);
        Assert.assertEquals(internalSearch.getResultPosition(), id);
    }

    @Test
    public void setTest() {
        assertEquals("toto", gesture.setName("toto").getName());
        assertEquals(Gesture.Action.Download, gesture.setAction(Gesture.Action.Download).getAction());
        assertEquals(123, gesture.setLevel2(123).getLevel2());
    }

    @Test
    public void setEventTest() {
        gesture.setName("gesture").setAction(Gesture.Action.Exit).setChapter2("chapter2").setEvent();
        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("click", buffer.getVolatileParams().get(i).getKey());
        assertEquals("S", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("click", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("S", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter2::gesture", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventOnlyChaptersTest() {
        gesture.setChapter1("chapter1").setChapter3("chapter3").setEvent();

        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("click", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("click", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventCompleteTest() {
        Random r = new Random();
        String[] vals = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;
        gesture.setName(vals[i++])
                .setChapter1(vals[i++])
                .setChapter2(vals[i++])
                .setChapter3(vals[i]).setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        i = 0;

        assertEquals("click", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("click", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals(vals[1] + "::" + vals[2] + "::" + vals[3] + "::" + vals[0], buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i).getValue().execute());
    }

    @Test
    public void setEventWithCustomObjects() {
        gesture.setChapter1("chapter1").setChapter3("chapter3").CustomObjects().add(new HashMap<String, Object>() {{
            put("key", "value");
        }});
        gesture.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{\"key\":\"value\"}", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("click", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("click", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("A", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithInternalSearch() {
        gesture.setChapter1("chapter1").setChapter3("chapter3");
        InternalSearch is = gesture.InternalSearch("searchLabel", 3);

        assertNotNull(is.getId());

        gesture.setEvent();

        assertEquals(7, buffer.getVolatileParams().size());

        assertEquals("mc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("searchLabel", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("np", buffer.getVolatileParams().get(i).getKey());
        assertEquals("3", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("click", buffer.getVolatileParams().get(i).getKey());
        assertEquals("IS", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("click", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("IS", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }
}
