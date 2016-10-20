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

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class VideosTest extends AbstractTestClass {

    private Videos videos;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MediaPlayer player = new MediaPlayer(tracker);
        videos = new Videos(player);
    }

    @Test
    public void initTest() {
        assertTrue(true);
    }

    @Test
    public void addOneTest() {
        videos.add("name", 80);
        videos.add("name", 89);
        assertEquals(1, videos.list.size());
        assertEquals("name", videos.list.get(0).getName());
        assertEquals(80, videos.list.get(0).getDuration());
    }

    @Test
    public void addTwoTest() {
        videos.add("name", "chapter1", 80);
        assertEquals(1, videos.list.size());
        assertEquals("name", videos.list.get(0).getName());
        assertEquals("chapter1", videos.list.get(0).getChapter1());
        assertEquals(80, videos.list.get(0).getDuration());
    }

    @Test
    public void addThreeTest() {
        videos.add("name", "chapter1", "chapter2", 80);
        assertEquals(1, videos.list.size());
        assertEquals("name", videos.list.get(0).getName());
        assertEquals("chapter1", videos.list.get(0).getChapter1());
        assertEquals("chapter2", videos.list.get(0).getChapter2());
        assertEquals(80, videos.list.get(0).getDuration());
    }

    @Test
    public void addFourTest() {
        Random r = new Random();
        int[] vals = {r.nextInt(500), r.nextInt(500), r.nextInt(500), r.nextInt(500), r.nextInt(500)};
        int i = 0;

        Video v = videos.add("name" + vals[i++], "chapter" + vals[i++], "chapter" + vals[i++], "chapter" + vals[i++], vals[i]);

        assertNotNull(v.getId());

        i = 0;
        assertEquals(1, videos.list.size());
        assertEquals("name" + vals[i++], videos.list.get(0).getName());
        assertEquals("chapter" + vals[i++], videos.list.get(0).getChapter1());
        assertEquals("chapter" + vals[i++], videos.list.get(0).getChapter2());
        assertEquals("chapter" + vals[i++], videos.list.get(0).getChapter3());
        assertEquals(vals[i], videos.list.get(0).getDuration());
    }

    @Test
    public void removeTest() {
        Video v1 = videos.add("name", 80);
        Video v2 = videos.add("chapter1", 10);
        videos.add("test", 40);
        assertEquals(3, videos.list.size());

        videos.remove("name");
        assertEquals(2, videos.list.size());
        assertFalse(videos.list.contains(v1));
        assertTrue(videos.list.contains(v2));
    }

    @Test
    public void removeAllTest() {
        videos.add("name", 80);
        videos.add("chapter", 10);
        videos.add("chapter2", 40);
        assertEquals(3, videos.list.size());

        videos.removeAll();
        assertEquals(0, videos.list.size());

    }
}
