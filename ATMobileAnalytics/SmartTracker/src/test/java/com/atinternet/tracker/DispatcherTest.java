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

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class DispatcherTest extends AbstractTestClass {

    private Dispatcher dispatcher;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dispatcher = new Dispatcher(tracker);
        buffer = tracker.getBuffer();
        buffer.getPersistentParams().clear();
        buffer.getVolatileParams().clear();
    }

    @Test
    public void setNoPersistentIdentifiedVisitorTest() {
        Assert.assertEquals(0, buffer.getPersistentParams().size());
        dispatcher.setIdentifiedVisitorInfos();
        Assert.assertEquals(0, buffer.getPersistentParams().size());
    }

    @Test
    public void setNoPersistentIdentifiedTwoVisitorTest() {
        tracker.getConfiguration().put("persistentIdentifiedVisitor", true);
        Assert.assertEquals(0, buffer.getPersistentParams().size());
        dispatcher.setIdentifiedVisitorInfos();
        Assert.assertEquals(0, buffer.getPersistentParams().size());
    }

    @Test
    public void setPersistentIdentifiedVisitorTest() {
        tracker.getConfiguration().put("persistIdentifiedVisitor", true);
        Assert.assertEquals(0, buffer.getVolatileParams().size());
        Tracker.getPreferences().edit().putString(IdentifiedVisitor.VISITOR_NUMERIC, "1").apply();
        Tracker.getPreferences().edit().putString(IdentifiedVisitor.VISITOR_CATEGORY, "3").apply();
        Tracker.getPreferences().edit().putString(IdentifiedVisitor.VISITOR_TEXT, "test").apply();
        dispatcher.setIdentifiedVisitorInfos();
        Assert.assertEquals(3, buffer.getVolatileParams().size());
        Assert.assertEquals("an", buffer.getVolatileParams().get("an").getKey());
        Assert.assertEquals("1", buffer.getVolatileParams().get("an").getValues().get(0).execute());

        Assert.assertEquals("at", buffer.getVolatileParams().get("at").getKey());
        Assert.assertEquals("test", buffer.getVolatileParams().get("at").getValues().get(0).execute());

        Assert.assertEquals("ac", buffer.getVolatileParams().get("ac").getKey());
        Assert.assertEquals("3", buffer.getVolatileParams().get("ac").getValues().get(0).execute());
    }
}
