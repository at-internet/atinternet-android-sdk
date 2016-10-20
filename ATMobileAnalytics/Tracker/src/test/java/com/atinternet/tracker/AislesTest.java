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
public class AislesTest extends AbstractTestClass {

    private Aisles aisles;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aisles = new Aisles(tracker);
    }

    @Test
    public void addTest() {
        Random r = new Random();
        String[] levels = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;

        Aisle aisle = aisles.add(levels[i++], levels[i++], levels[i++], levels[i++], levels[i++], levels[i]);
        assertEquals(1, tracker.getBusinessObjects().size());
        i = 0;

        assertEquals(levels[i++], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel1());
        assertEquals(levels[i++], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel2());
        assertEquals(levels[i++], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel3());
        assertEquals(levels[i++], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel4());
        assertEquals(levels[i++], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel5());
        assertEquals(levels[i], ((Aisle) tracker.getBusinessObjects().get(aisle.getId())).getLevel6());
    }
}
