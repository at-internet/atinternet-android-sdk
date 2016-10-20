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

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class DynamicScreensTest extends AbstractTestClass {

    private DynamicScreens dynamicScreens;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dynamicScreens = new DynamicScreens(tracker);
    }

    @Test
    public void addTest() {
        Date date = new Date();
        DynamicScreen dynamicScreen = dynamicScreens.add("8", "test", date);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("8", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getScreenId());
        assertEquals("test", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getName());
        assertEquals(date, ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getUpdate());
    }

    @Test
    public void addTwoTest() {
        Date date = new Date();
        DynamicScreen dynamicScreen = dynamicScreens.add("8", "name", date, "chapter1");
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("8", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getScreenId());
        assertEquals("name", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getName());
        assertEquals("chapter1", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter1());
        assertEquals(date, ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getUpdate());
    }

    @Test
    public void addThreeTest() {
        Date date = new Date();
        DynamicScreen dynamicScreen = dynamicScreens.add("8", "name", date, "chapter1", "chapter2");
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("8", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getScreenId());
        assertEquals("name", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getName());
        assertEquals("chapter1", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter1());
        assertEquals("chapter2", ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter2());
        assertEquals(date, ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getUpdate());
    }

    @Test
    public void addFourTest() {
        Random r = new Random();
        String[] vals = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;
        Date date = new Date();
        DynamicScreen dynamicScreen = dynamicScreens.add(vals[i++], vals[i++], date, vals[i++], vals[i++], vals[i]);
        i = 0;

        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(vals[i++], ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getScreenId());
        assertEquals(vals[i++], ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getName());
        assertEquals(vals[i++], ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter1());
        assertEquals(vals[i++], ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter2());
        assertEquals(vals[i], ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getChapter3());
        assertEquals(date, ((DynamicScreen) tracker.getBusinessObjects().get(dynamicScreen.getId())).getUpdate());
    }
}
