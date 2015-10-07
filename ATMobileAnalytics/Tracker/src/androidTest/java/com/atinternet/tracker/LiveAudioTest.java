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

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class LiveAudioTest extends AbstractTestClass {

    private LiveAudio liveAudio;
    private Buffer buffer;
    MediaPlayer player;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        player = new MediaPlayer(tracker);
        liveAudio = new LiveAudio(player);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertEquals(-1, liveAudio.getLevel2());
        assertEquals("", liveAudio.getName());
        assertEquals("audio", liveAudio.getType());
        assertNull(liveAudio.getAction());
        assertNull(liveAudio.getWebDomain());
        assertEquals(RichMedia.BroadcastMode.Live, liveAudio.getBroadcastMode());
        assertFalse(liveAudio.isBuffering());
        assertFalse(liveAudio.isEmbedded());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(liveAudio, new LiveAudio(player));
    }

    @Test
    public void setTest() {
        assertEquals("name", liveAudio.setName("name").getName());
        assertEquals("test", liveAudio.setWebDomain("test").getWebDomain());
        assertEquals(RichMedia.Action.Stop, liveAudio.setAction(RichMedia.Action.Stop).getAction());
        assertEquals(RichMedia.BroadcastMode.Live, liveAudio.setBroadcastMode(RichMedia.BroadcastMode.Live).getBroadcastMode());
        assertEquals(true, liveAudio.setBuffering(true).isBuffering());
        assertEquals(true, liveAudio.setEmbedded(true).isEmbedded());
        assertEquals(3, liveAudio.setLevel2(3).getLevel2());
    }

    @Test
    public void setEventPlayTest() {
        liveAudio.setAction(RichMedia.Action.Play)
                .setName("name")
                .setLevel2(9)
                .setChapter1("chapter1")
                .setEvent();
        assertEquals(7, buffer.getVolatileParams().size());
        int i = 0;

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("audio", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("a", buffer.getVolatileParams().get(i).getKey());
        assertEquals("play", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m6", buffer.getVolatileParams().get(i).getKey());
        assertEquals("live", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("plyr", buffer.getVolatileParams().get(i).getKey());
        assertEquals("1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m5", buffer.getVolatileParams().get(i).getKey());
        assertEquals("int", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("s2", buffer.getVolatileParams().get(i).getKey());
        assertEquals("9", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventRefreshTest() {
        liveAudio.setAction(RichMedia.Action.Refresh)
                .setName("name")
                .setLevel2(9)
                .setChapter1("chapter1")
                .setEvent();
        assertEquals(7, buffer.getVolatileParams().size());
        int i = 0;

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("audio", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("a", buffer.getVolatileParams().get(i).getKey());
        assertEquals("refresh", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m6", buffer.getVolatileParams().get(i).getKey());
        assertEquals("live", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("plyr", buffer.getVolatileParams().get(i).getKey());
        assertEquals("1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m5", buffer.getVolatileParams().get(i).getKey());
        assertEquals("int", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("s2", buffer.getVolatileParams().get(i).getKey());
        assertEquals("9", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventPauseTest() {
        liveAudio.setAction(RichMedia.Action.Pause)
                .setName("name")
                .setLevel2(9)
                .setChapter1("chapter1")
                .setEvent();
        assertEquals(7, buffer.getVolatileParams().size());
        int i = 0;

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("audio", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("a", buffer.getVolatileParams().get(i).getKey());
        assertEquals("pause", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m6", buffer.getVolatileParams().get(i).getKey());
        assertEquals("live", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("plyr", buffer.getVolatileParams().get(i).getKey());
        assertEquals("1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m5", buffer.getVolatileParams().get(i).getKey());
        assertEquals("int", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("s2", buffer.getVolatileParams().get(i).getKey());
        assertEquals("9", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventStopTest() {
        liveAudio.setAction(RichMedia.Action.Stop)
                .setName("name")
                .setLevel2(9)
                .setChapter1("chapter1")
                .setEvent();
        assertEquals(7, buffer.getVolatileParams().size());
        int i = 0;

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("audio", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("a", buffer.getVolatileParams().get(i).getKey());
        assertEquals("stop", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m6", buffer.getVolatileParams().get(i).getKey());
        assertEquals("live", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("plyr", buffer.getVolatileParams().get(i).getKey());
        assertEquals("1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("m5", buffer.getVolatileParams().get(i).getKey());
        assertEquals("int", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("s2", buffer.getVolatileParams().get(i).getKey());
        assertEquals("9", buffer.getVolatileParams().get(i++).getValue().execute());
    }
}
