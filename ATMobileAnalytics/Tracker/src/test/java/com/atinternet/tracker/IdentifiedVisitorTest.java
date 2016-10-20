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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class IdentifiedVisitorTest extends AbstractTestClass {

    private IdentifiedVisitor identifiedVisitor;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Configuration configuration = tracker.getConfiguration();
        configuration.put(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR, false);
        tracker = new Tracker(RuntimeEnvironment.application, configuration);
        identifiedVisitor = new IdentifiedVisitor(tracker);
        buffer = tracker.getBuffer();
        buffer.getPersistentParams().clear();
    }

    @Test
    public void setOneTest() {
        int id = new Random().nextInt(500);
        tracker = identifiedVisitor.set(id);
        assertEquals(1, buffer.getPersistentParams().size());
        assertEquals("an", buffer.getPersistentParams().get(0).getKey());
        assertEquals(String.valueOf(id), buffer.getPersistentParams().get(0).getValue().execute());
    }

    @Test
    public void setTwoTest() {
        int id = new Random().nextInt(500);
        int cat = new Random().nextInt(500);
        tracker = identifiedVisitor.set(id, cat);
        assertEquals(2, buffer.getPersistentParams().size());
        assertEquals("an", buffer.getPersistentParams().get(0).getKey());
        assertEquals(String.valueOf(id), buffer.getPersistentParams().get(0).getValue().execute());

        assertEquals("ac", buffer.getPersistentParams().get(1).getKey());
        assertEquals(String.valueOf(cat), buffer.getPersistentParams().get(1).getValue().execute());
    }

    @Test
    public void setThreeTest() {
        int id = new Random().nextInt(500);
        tracker = identifiedVisitor.set("visitor" + id);
        assertEquals(1, buffer.getPersistentParams().size());
        assertEquals("at", buffer.getPersistentParams().get(0).getKey());
        assertEquals("visitor" + id, buffer.getPersistentParams().get(0).getValue().execute());

    }

    @Test
    public void setFourTest() {
        int id = new Random().nextInt(500);
        int cat = new Random().nextInt(500);
        tracker = identifiedVisitor.set("visitor" + id, cat);
        assertEquals(2, buffer.getPersistentParams().size());
        assertEquals("at", buffer.getPersistentParams().get(0).getKey());
        assertEquals("visitor" + id, buffer.getPersistentParams().get(0).getValue().execute());

        assertEquals("ac", buffer.getPersistentParams().get(1).getKey());
        assertEquals(String.valueOf(cat), buffer.getPersistentParams().get(1).getValue().execute());
    }

    @Test
    public void unsetTest() {
        tracker = identifiedVisitor.set("visitor", 2);
        assertEquals(2, buffer.getPersistentParams().size());
        assertEquals("at", buffer.getPersistentParams().get(0).getKey());
        assertEquals("visitor", buffer.getPersistentParams().get(0).getValue().execute());

        assertEquals("ac", buffer.getPersistentParams().get(1).getKey());
        assertEquals("2", buffer.getPersistentParams().get(1).getValue().execute());

        identifiedVisitor.unset();
        assertEquals(0, buffer.getPersistentParams().size());
    }
}
