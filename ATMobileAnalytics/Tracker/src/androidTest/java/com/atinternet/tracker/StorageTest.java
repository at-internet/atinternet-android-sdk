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

import com.atinternet.tracker.Tracker.OfflineMode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class StorageTest extends AbstractTestClass {

    private Storage storage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        storage = new Storage(Robolectric.application);
    }

    @Test
    public void multipleInstanceTest() {
        Storage storage1 = new Storage(Robolectric.application);
        assertNotSame(storage, storage1);
    }

    @Test
    public void getOfflineModeTest() {
        assertEquals(OfflineMode.required, storage.getOfflineMode());
    }

    @Test
    public void setOfflineModeTest() {
        storage.setOfflineMode(OfflineMode.always);
        assertEquals(OfflineMode.always, storage.getOfflineMode());
    }

    @Test
    public void getCountOfflineHitsTest() {
        assertEquals(0, storage.getCountOfflineHits());
    }

    @Test
    public void saveHitTest() {
        assertEquals(0, storage.getCountOfflineHits());
        storage.saveHit("HitTest", System.currentTimeMillis(), null);
        assertEquals(1, storage.getCountOfflineHits());
    }

    @Test
    public void deleteHitTest() {
        for (int i = 0; i < 5; i++) {
            storage.saveHit(String.valueOf(i), System.currentTimeMillis(), null);
        }
        assertEquals(5, storage.getCountOfflineHits());
        storage.deleteHit("3");
        assertEquals(4, storage.getCountOfflineHits());
    }

    @Test
    public void getOfflineHitsTest() {
        for (int i = 0; i < 10; i++) {
            storage.saveHit("HitTest", System.currentTimeMillis(), null);
        }
        assertEquals(10, storage.getCountOfflineHits());
        ArrayList<Hit> hits = storage.getOfflineHits();
        assertEquals(10, hits.size());
    }

    @Test
    public void buildHitToStoreTest() throws Exception {
        String olt = Tool.getTimeStamp().execute();
        String na = Tool.getTimeStamp().execute();
        String hit = "http://logp.xiti.com/hit.xiti?s=552987&cn=3g&ts=" + na;
        hit = storage.buildHitToStore(hit, olt);
        assertEquals("http://logp.xiti.com/hit.xiti?s=552987&cn=offline&ts=" + na + "&olt=" + olt, hit);
    }

    @Test
    public void removeAllOfflineHitsTest() {
        for (int i = 0; i < 5; i++) {
            storage.saveHit(String.valueOf(i), System.currentTimeMillis(), null);
        }
        assertEquals(5, storage.getCountOfflineHits());
        storage.removeAllOfflineHits();
        assertEquals(0, storage.getCountOfflineHits());
    }

    @Test
    public void getFirstOfflineHitTest() throws InterruptedException {
        long time = System.currentTimeMillis();
        storage.saveHit("hit", time, null);
        Thread.sleep(2000);
        long time2 = System.currentTimeMillis();
        storage.saveHit("hit2", time2, null);
        Thread.sleep(2000);
        long time3 = System.currentTimeMillis();
        storage.saveHit("hit3", time3, null);
        Assert.assertEquals(new Date(time), storage.getOldestOfflineHit().getDate());
        Assert.assertEquals("hit", storage.getOldestOfflineHit().getUrl());
    }

    @Test
    public void getLastOfflineHitTest() throws InterruptedException {
        long time = System.currentTimeMillis();
        storage.saveHit("hit", time, null);
        Thread.sleep(2000);
        long time2 = System.currentTimeMillis();
        storage.saveHit("hit2", time2, null);
        Thread.sleep(2000);
        long time3 = System.currentTimeMillis();
        storage.saveHit("hit3", time3, null);
        Assert.assertEquals(new Date(time3), storage.getLatestOfflineHit().getDate());
        Assert.assertEquals("hit3", storage.getLatestOfflineHit().getUrl());
    }

    @Test
    public void removeOldOfflineHitsTest() throws InterruptedException {
        storage.saveHit("hit", System.currentTimeMillis(), null);
        storage.saveHit("hit2", System.currentTimeMillis() / 10, null);
        storage.saveHit("hit3", System.currentTimeMillis() / 5, null);
        storage.saveHit("hit4", System.currentTimeMillis() / 20, null);
        assertEquals(4, storage.getCountOfflineHits());
        storage.removeOldOfflineHits(4);
        assertEquals(1, storage.getCountOfflineHits());
    }
}
