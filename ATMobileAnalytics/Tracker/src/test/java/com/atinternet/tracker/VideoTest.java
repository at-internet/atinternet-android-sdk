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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class VideoTest extends AbstractTestClass {

    private Video video;
    private MediaPlayer player;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        player = new MediaPlayer(tracker);
        video = new Video(player);
    }

    @Test
    public void initTest() {
        assertEquals(-1, video.getLevel2());
        assertEquals(-1, video.getMediaLevel2());
        assertEquals(0, video.getDuration());
        assertEquals("", video.getName());
        assertEquals("", video.getMediaLabel());
        assertEquals("video", video.getMediaType());
        assertNull(video.getWebDomain());
        assertEquals(RichMedia.BroadcastMode.Clip, video.getBroadcastMode());
        assertFalse(video.isBuffering());
        assertFalse(video.isEmbedded());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(video, new Video(player));
    }

    @Test
    public void setEventPlayTest() {
        tracker.setParam("a", RichMedia.Action.Play.stringValue());
        video.setDuration(56)
                .setAction(RichMedia.Action.Play)
                .setLevel2(4)
                .setName("name")
                .setChapter1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("video", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("play", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());

        video.setDuration(56)
                .setAction(RichMedia.Action.Play)
                .setMediaLevel2(4)
                .setMediaLabel("name")
                .setMediaTheme1("chapter1")
                .setLinkedContent("content")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("video", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("play", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }

    @Test
    public void setEventRefreshTest() {
        tracker.setParam("a", RichMedia.Action.Refresh.stringValue());
        video.setDuration(56)
                .setAction(RichMedia.Action.Refresh)
                .setMediaLevel2(4)
                .setMediaLabel("name")
                .setMediaTheme1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("video", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("refresh", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }

    @Test
    public void setEventPauseTest() {
        tracker.setParam("a", RichMedia.Action.Pause.stringValue());
        video.setDuration(56)
                .setAction(RichMedia.Action.Pause)
                .setMediaLevel2(4)
                .setMediaLabel("name")
                .setMediaTheme1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("video", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("pause", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }

    @Test
    public void setEventStopTest() {
        tracker.setParam("a", RichMedia.Action.Stop.stringValue());
        video.setDuration(56)
                .setAction(RichMedia.Action.Stop)
                .setMediaLevel2(4)
                .setMediaLabel("name")
                .setMediaTheme1("chapter1")
                .setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("video", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("a").getValues().size());
        assertEquals("stop", buffer.getVolatileParams().get("a").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m6").getValues().size());
        assertEquals("clip", buffer.getVolatileParams().get("m6").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("plyr").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("plyr").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m5").getValues().size());
        assertEquals("int", buffer.getVolatileParams().get("m5").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("s2").getValues().size());
        assertEquals("4", buffer.getVolatileParams().get("s2").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("m1").getValues().size());
        assertEquals("56", buffer.getVolatileParams().get("m1").getValues().get(0).execute());
    }
}
