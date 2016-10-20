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

import java.util.Random;

import static org.junit.Assert.assertEquals;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class ScreensTest extends AbstractTestClass {

    private Screens screens;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        screens = new Screens(tracker);
    }

    @Test
    public void addTest() {
        Screen screen = screens.add();
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("", ((Screen) tracker.getBusinessObjects().get(screen.getId())).getName());
    }

    @Test
    public void addWithLevel2Test() {
        Screen screen = screens.add("name").setLevel2(3);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("name", ((Screen) tracker.getBusinessObjects().get(screen.getId())).getName());
        assertEquals(3, ((Screen) tracker.getBusinessObjects().get(screen.getId())).getLevel2());
    }

    @Test
    public void addWithChaptersTest() {
        Random r = new Random();
        String[] vals = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;
        Screen screen = screens.add(vals[i++], vals[i++], vals[i++], vals[i]);
        i = 0;

        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(vals[i++], ((Screen) tracker.getBusinessObjects().get(screen.getId())).getName());
        assertEquals(vals[i++], ((Screen) tracker.getBusinessObjects().get(screen.getId())).getChapter1());
        assertEquals(vals[i++], ((Screen) tracker.getBusinessObjects().get(screen.getId())).getChapter2());
        assertEquals(vals[i], ((Screen) tracker.getBusinessObjects().get(screen.getId())).getChapter3());
    }
}
