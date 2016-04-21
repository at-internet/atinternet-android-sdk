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

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class HitParamTest extends AbstractTestClass {

    @Test
    public void stringValueTest() {
        assertEquals(24, Hit.HitParam.values().length);

        assertEquals("action", Hit.HitParam.Action.stringValue());
        assertEquals("bg", Hit.HitParam.BackgroundMode.stringValue());
        assertEquals("type", Hit.HitParam.HitType.stringValue());
        assertEquals("gy", Hit.HitParam.GPSLatitude.stringValue());
        assertEquals("gx", Hit.HitParam.GPSLongitude.stringValue());
        assertEquals("stc", Hit.HitParam.JSON.stringValue());
        assertEquals("s2", Hit.HitParam.Level2.stringValue());
        assertEquals("ati", Hit.HitParam.OnAppAdsImpression.stringValue());
        assertEquals("atc", Hit.HitParam.OnAppAdsTouch.stringValue());
        assertEquals("p", Hit.HitParam.Screen.stringValue());
        assertEquals("click", Hit.HitParam.Touch.stringValue());
        assertEquals("s2click", Hit.HitParam.TouchLevel2.stringValue());
        assertEquals("s2atc", Hit.HitParam.OnAppAdTouchLevel2.stringValue());
        assertEquals("pclick", Hit.HitParam.TouchScreen.stringValue());
        assertEquals("patc", Hit.HitParam.OnAppAdTouchScreen.stringValue());
        assertEquals("idclient", Hit.HitParam.UserId.stringValue());
        assertEquals("ac", Hit.HitParam.VisitorCategory.stringValue());
        assertEquals("an", Hit.HitParam.VisitorIdentifierNumeric.stringValue());
        assertEquals("at", Hit.HitParam.VisitorIdentifierText.stringValue());
        assertEquals("refstore", Hit.HitParam.Refstore.stringValue());
        assertEquals("ref", Hit.HitParam.Referrer.stringValue());
        assertEquals("tvt", Hit.HitParam.TVT.stringValue());
        assertEquals("xto", Hit.HitParam.Source.stringValue());
        assertEquals("xtor", Hit.HitParam.RemanentSource.stringValue());
    }
}
