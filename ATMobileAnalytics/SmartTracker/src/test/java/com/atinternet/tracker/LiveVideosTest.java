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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class LiveVideosTest extends AbstractTestClass {

    private LiveVideos liveVideos;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MediaPlayer player = new MediaPlayer(tracker);
        liveVideos = new LiveVideos(player);
    }

    @Test
    public void initTest() {
        assertTrue(true);
    }

    @Test
    public void addOneTest() {
        liveVideos.add("name");
        assertEquals(1, liveVideos.list.size());
    }

    @Test
    public void addTwoTest() {
        liveVideos.add("name", "chapter1");
        assertEquals(1, liveVideos.list.size());
    }

    @Test
    public void addThreeTest() {
        liveVideos.add("name", "chapter1", "chapter2");
        assertEquals(1, liveVideos.list.size());
    }

    @Test
    public void addFourTest() {
        liveVideos.add("name", "chapter1", "chapter2", "chapter3");
        assertEquals(1, liveVideos.list.size());
    }

    @Test
    public void removeTest() {
        LiveVideo v1 = liveVideos.add("name");
        LiveVideo v2 = liveVideos.add("other");
        liveVideos.add("lv3");
        assertEquals(3, liveVideos.list.size());

        liveVideos.remove("name");
        assertEquals(2, liveVideos.list.size());
        assertFalse(liveVideos.list.contains(v1));
        assertTrue(liveVideos.list.contains(v2));
    }

    @Test
    public void removeAllTest() {
        liveVideos.add("name");
        liveVideos.add("toto");
        liveVideos.add("titi");
        assertEquals(3, liveVideos.list.size());

        liveVideos.removeAll();
        assertEquals(0, liveVideos.list.size());

    }
}
