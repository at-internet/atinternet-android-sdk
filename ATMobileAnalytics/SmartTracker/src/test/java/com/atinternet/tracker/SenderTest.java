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


@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class SenderTest extends AbstractTestClass {

    private Sender sender;

    private Storage storage;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        storage = Storage.getInstance(Tracker.getAppContext());
        sender = new Sender(tracker, new Hit("www.atinternet.com"), false);
    }

    public void updateRetryCountTest() throws Exception {
        storage.saveHit("www.atinternet.com", System.currentTimeMillis(), null);
        Hit hit = storage.getLatestOfflineHit();
        assertEquals(1, storage.getCountOfflineHits());
        executePrivateMethod(sender, "updateRetryCount", new Object[]{hit});
        assertEquals(1, hit.getRetry());
        executePrivateMethod(sender, "updateRetryCount", new Object[]{hit});
        assertEquals(2, hit.getRetry());
        executePrivateMethod(sender, "updateRetryCount", new Object[]{hit});
        assertEquals(3, hit.getRetry());
        executePrivateMethod(sender, "updateRetryCount", new Object[]{hit});
        assertEquals(0, storage.getCountOfflineHits());
    }

    @Test
    public void saveHitDatabaseTest() {
        sender.saveHitDatabase(new Hit("www.hit.com"));
        assertEquals(1, storage.getCountOfflineHits());
    }
}
