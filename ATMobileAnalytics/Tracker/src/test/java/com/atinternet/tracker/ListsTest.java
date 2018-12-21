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

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class ListsTest extends AbstractTestClass {

    @Test
    public void getTypeHitKeyTest() {
        HashMap<String, Hit.HitType> typeHit = Lists.getProcessedTypes();
        assertTrue(typeHit.containsKey("audio"));
        assertTrue(typeHit.containsKey("video"));
        assertTrue(typeHit.containsKey("vpre"));
        assertTrue(typeHit.containsKey("vmid"));
        assertTrue(typeHit.containsKey("vpost"));
        assertTrue(typeHit.containsKey("animation"));
        assertTrue(typeHit.containsKey("anim"));
        assertTrue(typeHit.containsKey("podcast"));
        assertTrue(typeHit.containsKey("rss"));
        assertTrue(typeHit.containsKey("email"));
        assertTrue(typeHit.containsKey("pub"));
        assertTrue(typeHit.containsKey("ad"));
        assertTrue(typeHit.containsKey("click"));
        assertTrue(typeHit.containsKey("clic"));
        assertTrue(typeHit.containsKey("AT"));
        assertTrue(typeHit.containsKey("pdt"));
        assertTrue(typeHit.containsKey("mvt"));
        assertTrue(typeHit.containsKey("wbo"));
        assertTrue(typeHit.containsKey("screen"));
        assertEquals(19, typeHit.size());
    }

    @Test
    public void getTypeHitValueTest() {
        HashMap<String, Hit.HitType> typeHit = Lists.getProcessedTypes();
        assertEquals(Hit.HitType.Audio, typeHit.get("audio"));
        assertEquals(Hit.HitType.Video, typeHit.get("video"));
        assertEquals(Hit.HitType.Video, typeHit.get("vpre"));
        assertEquals(Hit.HitType.Video, typeHit.get("vmid"));
        assertEquals(Hit.HitType.Video, typeHit.get("vpost"));
        assertEquals(Hit.HitType.Animation, typeHit.get("animation"));
        assertEquals(Hit.HitType.Animation, typeHit.get("anim"));
        assertEquals(Hit.HitType.PodCast, typeHit.get("podcast"));
        assertEquals(Hit.HitType.RSS, typeHit.get("rss"));
        assertEquals(Hit.HitType.Email, typeHit.get("email"));
        assertEquals(Hit.HitType.Publicite, typeHit.get("pub"));
        assertEquals(Hit.HitType.Publicite, typeHit.get("ad"));
        assertEquals(Hit.HitType.Touch, typeHit.get("click"));
        assertEquals(Hit.HitType.Touch, typeHit.get("clic"));
        assertEquals(Hit.HitType.AdTracking, typeHit.get("AT"));
        assertEquals(Hit.HitType.MvTesting, typeHit.get("mvt"));
        assertEquals(Hit.HitType.Weborama, typeHit.get("wbo"));
        assertEquals(Hit.HitType.Screen, typeHit.get("screen"));
        assertEquals(19, typeHit.size());

        assertNull(typeHit.get("test"));
    }

    @Test
    public void getSliceReadyParameterTest() {
        HashSet<String> sliceReadyParameter = Lists.getSliceReadyParams();
        assertTrue(sliceReadyParameter.contains("ati"));
        assertTrue(sliceReadyParameter.contains("atc"));
        assertTrue(sliceReadyParameter.contains("pdtl"));
        assertTrue(sliceReadyParameter.contains("stc"));
        assertTrue(sliceReadyParameter.contains("events"));
        assertEquals(5, sliceReadyParameter.size());
    }
}
