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
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class InternalSearchTest extends AbstractTestClass {

    private InternalSearch internalSearch;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        internalSearch = new InternalSearch(tracker);
    }

    @Test
    public void initTest() {
        assertNull(internalSearch.getKeyword());
        assertEquals(1, internalSearch.getResultScreenNumber());
        assertEquals(-1, internalSearch.getResultPosition());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(internalSearch, new InternalSearch(tracker));
    }

    @Test
    public void setParamsTest() {
        internalSearch.setKeyword("search").setResultScreenNumber(3).setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("mc").getValues().size());
        assertEquals("search", buffer.getVolatileParams().get("mc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("np").getValues().size());
        assertEquals("3", buffer.getVolatileParams().get("np").getValues().get(0).execute());
    }

    @Test
    public void setParamsTouchTest() {
        internalSearch.setKeyword("se)a..rù c^h").setResultScreenNumber(3).setResultPosition(5).setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("mc").getValues().size());
        assertEquals("search", buffer.getVolatileParams().get("mc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("np").getValues().size());
        assertEquals("3", buffer.getVolatileParams().get("np").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mcrg").getValues().size());
        assertEquals("5", buffer.getVolatileParams().get("mcrg").getValues().get(0).execute());
    }
}
