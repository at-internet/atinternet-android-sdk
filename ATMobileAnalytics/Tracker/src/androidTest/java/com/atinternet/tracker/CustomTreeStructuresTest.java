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

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class CustomTreeStructuresTest extends AbstractTestClass {

    private CustomTreeStructures customTreeStructures;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        customTreeStructures = new CustomTreeStructures(tracker);
    }

    @Test
    public void addOneTest() {
        CustomTreeStructure customTreeStructure = customTreeStructures.add(3);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(3, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory1());
        assertEquals(0, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory2());
        assertEquals(0, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory3());
    }

    @Test
    public void addTwoTest() {
        CustomTreeStructure customTreeStructure = customTreeStructures.add(3, 2);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(3, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory1());
        assertEquals(2, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory2());
        assertEquals(0, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory3());
    }

    @Test
    public void addThreeTest() {
        CustomTreeStructure customTreeStructure = customTreeStructures.add(3, 2, 1);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(3, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory1());
        assertEquals(2, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory2());
        assertEquals(1, ((CustomTreeStructure) tracker.getBusinessObjects().get(customTreeStructure.getId())).getCategory3());
    }
}
