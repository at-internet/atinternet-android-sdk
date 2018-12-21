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

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class GestureTest extends AbstractTestClass {

    private Gesture gesture;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        gesture = new Gesture(tracker);
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
        assertNotNull(gesture.CustomObjects());
        assertEquals(gesture.CustomObjects(), customObjects);
    }

    @Test
    public void InternalSearchDeprecatedTest() {
        InternalSearch internalSearch = gesture.InternalSearch("test", 1);
        assertNotNull(internalSearch);
        assertEquals(gesture.InternalSearch("", 0), internalSearch);
    }

    @Test
    public void InternalSearchTest() {
        InternalSearch internalSearch = gesture.InternalSearch("test", 1, 2);
        assertNotNull(internalSearch);
        assertEquals(gesture.InternalSearch("", 0, 0), internalSearch);
    }

    @Test
    public void setParamsTest() {
        gesture.setName("gesture").setAction(Gesture.Action.Exit).setChapter2("chapter2").setParams();
        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("S", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter2::gesture", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setParamsOnlyChaptersTest() {
        gesture.setChapter1("chapter1").setChapter3("chapter3").setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("A", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setParamsCompleteTest() {
        gesture.setName("name")
                .setChapter1("chap1")
                .setChapter2("chap2")
                .setChapter3("chap3").setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("A", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chap1::chap2::chap3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setParamsWithCustomObjects() {
        gesture.setChapter1("chapter1").setChapter3("chapter3").CustomObjects().add(new HashMap<String, Object>() {{
            put("key", "value");
        }});
        gesture.setParams();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());


        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("A", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get("p").getValues().get(0).execute());


        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{\"key\":\"value\"}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

    }

    @Test
    public void setParamsWithInternalDeprecatedSearch() {
        gesture.setChapter1("chapter1").setChapter3("chapter3");
        gesture.InternalSearch("searchLabel", 3);

        gesture.setParams();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("mc").getValues().size());
        assertEquals("searchLabel", buffer.getVolatileParams().get("mc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("np").getValues().size());
        assertEquals("3", buffer.getVolatileParams().get("np").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("IS", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }

    @Test
    public void setParamsWithInternalSearch() {
        gesture.setChapter1("chapter1").setChapter3("chapter3");
        gesture.InternalSearch("searchLabel", 3, 4);
        gesture.setParams();

        assertEquals(6, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());


        assertEquals(1, buffer.getVolatileParams().get("mc").getValues().size());
        assertEquals("searchLabel", buffer.getVolatileParams().get("mc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("np").getValues().size());
        assertEquals("3", buffer.getVolatileParams().get("np").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mcrg").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("mcrg").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("click").getValues().size());
        assertEquals("IS", buffer.getVolatileParams().get("click").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("click", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get("p").getValues().get(0).execute());
    }
}
