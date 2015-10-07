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

import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ReferrerReceiverTest extends AbstractTestClass {

    private ReferrerReceiver referrerReceiver;
    private SharedPreferences preferences;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        referrerReceiver = new ReferrerReceiver();
        preferences = Robolectric.application.getSharedPreferences(TrackerKeys.PREFERENCES, android.content.Context.MODE_PRIVATE);
    }

    @Test
    public void onReceiveGoodTest() {
        Intent intent = new Intent();
        intent.setAction("com.android.vending.INSTALL_REFERRER");
        intent.putExtra("referrer", "test=value&xtor=campaign");
        referrerReceiver.onReceive(Robolectric.application, intent);

        assertEquals("campaign", preferences.getString(TrackerKeys.MARKETING_CAMPAIGN_SAVED, null));
    }

    @Test
    public void onReceiveBadOneTest() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("referrer", "test_value_set");
        referrerReceiver.onReceive(Robolectric.application, intent);

        assertNull(preferences.getString(TrackerKeys.MARKETING_CAMPAIGN_SAVED, null));
    }

    @Test
    public void onReceiveBadTwoTest() {
        Intent intent = new Intent();
        intent.setAction("com.android.vending.INSTALL_REFERRER");
        intent.putExtra("id", "test_value_set");
        referrerReceiver.onReceive(Robolectric.application, intent);

        assertNull(preferences.getString(TrackerKeys.MARKETING_CAMPAIGN_SAVED, null));
    }
}
