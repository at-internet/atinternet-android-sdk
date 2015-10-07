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
import static org.junit.Assert.assertNull;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class MediaPlayersTest extends AbstractTestClass {

    private MediaPlayers mediaPlayers;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mediaPlayers = new MediaPlayers(tracker);
    }

    @Test
    public void initTest() {
        assertNotNull(mediaPlayers.players);
    }

    @Test
    public void addOneTest() {
        assertEquals(1, mediaPlayers.add().getPlayerId());
        assertEquals(2, mediaPlayers.add().getPlayerId());
        assertEquals(3, mediaPlayers.add().getPlayerId());

        assertEquals(3, mediaPlayers.players.size());
        assertNotNull(mediaPlayers.players.get(1));
        assertNotNull(mediaPlayers.players.get(2));
        assertNotNull(mediaPlayers.players.get(3));

    }

    @Test
    public void addTwoTest() {
        assertEquals(189, mediaPlayers.add(189).getPlayerId());
        assertEquals(189, mediaPlayers.add(189).getPlayerId());

        assertEquals(1, mediaPlayers.players.size());
        assertNotNull(mediaPlayers.players.get(189));
        assertNull(mediaPlayers.players.get(1));
    }

    @Test
    public void removeTest() {
        assertEquals(1, mediaPlayers.add().getPlayerId());
        assertEquals(2, mediaPlayers.add().getPlayerId());
        assertEquals(3, mediaPlayers.add().getPlayerId());
        assertEquals(3, mediaPlayers.players.size());

        mediaPlayers.remove(3);
        assertEquals(2, mediaPlayers.players.size());
        assertNull(mediaPlayers.players.get(3));
    }

    @Test
    public void removeAllTest() {
        assertEquals(1, mediaPlayers.add().getPlayerId());
        assertEquals(2, mediaPlayers.add().getPlayerId());
        assertEquals(3, mediaPlayers.add().getPlayerId());
        assertEquals(3, mediaPlayers.players.size());

        mediaPlayers.removeAll();
        assertEquals(0, mediaPlayers.players.size());
    }
}
