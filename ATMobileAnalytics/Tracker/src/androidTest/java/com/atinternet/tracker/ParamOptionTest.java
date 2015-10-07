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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.atinternet.tracker.ParamOption.RelativePosition.last;
import static com.atinternet.tracker.ParamOption.RelativePosition.none;
import static org.junit.Assert.*;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ParamOptionTest extends AbstractTestClass {

    private final ParamOption options = new ParamOption();

    @Test
    public void defaultParameterOption() {
        assertEquals(none, options.getRelativePosition());
        assertEquals("", options.getRelativeParameterKey());
        assertEquals(",", options.getSeparator());
        assertFalse(options.isEncode());
        assertFalse(options.isPersistent());
        assertFalse(options.isAppend());
    }

    @Test
    public void setEncodeTest() {
        options.setEncode(true);
        assertTrue(options.isEncode());
    }

    @Test
    public void setPermanentTest() {
        assertFalse(options.isPersistent());
        options.setPersistent(true);
        assertTrue(options.isPersistent());
    }

    @Test
    public void setRelativeParameterKeyTest() {
        options.setRelativeParameterKey("p");
        assertEquals("p", options.getRelativeParameterKey());
    }

    @Test
    public void setRelativePositionTest() {
        options.setRelativePosition(last);
        assertEquals(last, options.getRelativePosition());
    }

    @Test
    public void setSeparatorTest() {
        options.setSeparator("::");
        assertEquals("::", options.getSeparator());
    }

    @Test
    public void setAppendTest() {
        options.setAppend(true);
        assertTrue(options.isAppend());
    }
}
