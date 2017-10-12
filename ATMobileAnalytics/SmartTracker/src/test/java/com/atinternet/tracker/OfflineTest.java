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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class OfflineTest extends AbstractTestClass {

    private Offline offline;
    private Storage storage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        storage = Tracker.getStorage();
        offline = new Offline(tracker);
    }

    @Test
    public void getCountOfflineHitsTest() {
        for (int i = 0; i < 10; i++) {
            storage.saveHit("HitTest", System.currentTimeMillis(), null);
        }
        assertEquals(10, offline.count());
    }

    @Test
    public void removeAllOfflineHitsTest() {
        for (int i = 0; i < 5; i++) {
            storage.saveHit(String.valueOf(i), System.currentTimeMillis(), null);
        }
        Assert.assertEquals(5, storage.getCountOfflineHits());
        offline.delete();
        Assert.assertEquals(0, storage.getCountOfflineHits());
    }

    @Test
    public void getOfflineHits() {
        for (int i = 0; i < 10; i++) {
            storage.saveHit("HitTest", System.currentTimeMillis(), null);
        }

        Assert.assertEquals(10, offline.count());
        ArrayList<Hit> hits = offline.get();
        Assert.assertNotNull(hits);
        Assert.assertEquals(10, hits.size());
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
        Assert.assertEquals(new Date(time), offline.oldest().getDate());
        Assert.assertEquals("hit", offline.oldest().getUrl());
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
        Assert.assertEquals(new Date(time3), offline.latest().getDate());
        Assert.assertEquals("hit3", offline.latest().getUrl());
    }

    @Test
    public void removeOfflineHitsTest() {
        storage.saveHit("hit", System.currentTimeMillis(), null);
        storage.saveHit("hit2", System.currentTimeMillis() / 10, null);
        storage.saveHit("hit3", System.currentTimeMillis() / 5, null);
        storage.saveHit("hit4", System.currentTimeMillis() / 20, null);
        Assert.assertEquals(4, storage.getCountOfflineHits());
        offline.delete();
        Assert.assertEquals(0, offline.count());
    }

    @Test
    public void removeOldOfflineHitsWithDaysCountTest() {
        storage.saveHit("hit", System.currentTimeMillis(), null);
        storage.saveHit("hit2", System.currentTimeMillis() / 10, null);
        storage.saveHit("hit3", System.currentTimeMillis() / 5, null);
        storage.saveHit("hit4", System.currentTimeMillis() / 20, null);
        Assert.assertEquals(4, storage.getCountOfflineHits());
        offline.delete(10);
        Assert.assertEquals(1, offline.count());
    }

    @Test
    public void removeOldOfflineHitsWithDateTest() throws ParseException {
        storage.saveHit("hit", System.currentTimeMillis(), null);
        storage.saveHit("hit2", System.currentTimeMillis() / 10, null);
        storage.saveHit("hit3", System.currentTimeMillis() / 5, null);
        storage.saveHit("hit4", System.currentTimeMillis() / 20, null);
        Assert.assertEquals(4, storage.getCountOfflineHits());
        Date date = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).parse("01012015");
        offline.delete(date);
        Assert.assertEquals(1, offline.count());
    }
}
