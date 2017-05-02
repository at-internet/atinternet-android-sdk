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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class BufferTest extends AbstractTestClass {

    private Param param;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        param = new Param("key", new Closure() {
            @Override
            public String execute() {
                return "value";
            }
        });
    }

    @Test
    public void multiInstanceTest() {
        Buffer buffer2 = new Buffer(tracker);
        assertNotSame(buffer, buffer2);
    }

    @Test
    public void addPersistentParameterTest() {
        buffer.getPersistentParams().put(param.getKey(), param);
        assertEquals(1, buffer.getPersistentParams().size());
    }

    @Test
    public void addVolatileParameterTest() {
        buffer.getVolatileParams().put(param.getKey(), param);
        assertEquals(1, buffer.getVolatileParams().size());
    }

    @Test
    public void setIdentifierKey() {
        buffer.setIdentifierKey("androidId");
        assertEquals(TechnicalContext.getUserId("androidId").execute(), buffer.getPersistentParams().get("idclient").getValues().get(0).execute());
    }

    @Test
    public void addContextVariablesTest() throws Exception {
        executePrivateMethod(buffer, "addContextVariables", new Object[]{tracker});
        assertEquals(15, buffer.getPersistentParams().size());
        assertEquals(TechnicalContext.VTAG.execute(), buffer.getPersistentParams().get("vtag").getValues().get(0).execute());
        assertEquals("Android", buffer.getPersistentParams().get("ptag").getValues().get(0).execute());
    }

    @Test
    public void initConstantClosuresTest() throws Exception {
        executePrivateMethod(buffer, "initConstantClosures", new Object[0]);
        assertNotNull(getAccessibleField(buffer, "osClosure"));
        assertNotNull(getAccessibleField(buffer, "deviceClosure"));
        assertNotNull(getAccessibleField(buffer, "apvrClosure"));
    }
}
