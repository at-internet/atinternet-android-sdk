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
import static org.junit.Assert.assertSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class MediaPlayerTest extends AbstractTestClass {

    private MediaPlayer mediaPlayer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mediaPlayer = new MediaPlayer(tracker);
    }

    @Test
    public void initTest() {
        assertEquals(1, mediaPlayer.getPlayerId());
    }

    @Test
    public void multiInstanceTest() {
        assertNotSame(mediaPlayer, new MediaPlayer(tracker));
    }

    @Test
    public void getVideosTest() {
        Videos videos = mediaPlayer.Videos();
        assertNotNull(mediaPlayer.Videos());
        assertSame(videos, mediaPlayer.Videos());
    }

    @Test
    public void getAudiosTest() {
        Audios audios = mediaPlayer.Audios();
        assertNotNull(mediaPlayer.Audios());
        assertSame(audios, mediaPlayer.Audios());
    }

    @Test
    public void getLiveVideosTest() {
        LiveVideos liveVideos = mediaPlayer.LiveVideos();
        assertNotNull(mediaPlayer.LiveVideos());
        assertSame(liveVideos, mediaPlayer.LiveVideos());
    }

    @Test
    public void getLiveAudiosTest() {
        LiveAudios liveAudios = mediaPlayer.LiveAudios();
        assertNotNull(mediaPlayer.LiveAudios());
        assertSame(liveAudios, mediaPlayer.LiveAudios());
    }
}
