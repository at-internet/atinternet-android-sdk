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

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class HitParamTest extends AbstractTestClass {

    @Test
    public void stringValueTest() {
        int length = 38;
        int i = 0;
        assertEquals(length, Hit.HitParam.values().length);

        assertEquals("action", Hit.HitParam.Action.stringValue());
        i++;
        assertEquals("bg", Hit.HitParam.BackgroundMode.stringValue());
        i++;
        assertEquals("type", Hit.HitParam.HitType.stringValue());
        i++;
        assertEquals("gy", Hit.HitParam.GPSLatitude.stringValue());
        i++;
        assertEquals("gx", Hit.HitParam.GPSLongitude.stringValue());
        i++;
        assertEquals("stc", Hit.HitParam.JSON.stringValue());
        i++;
        assertEquals("s2", Hit.HitParam.Level2.stringValue());
        i++;
        assertEquals("ati", Hit.HitParam.OnAppAdsImpression.stringValue());
        i++;
        assertEquals("atc", Hit.HitParam.OnAppAdsTouch.stringValue());
        i++;
        assertEquals("p", Hit.HitParam.Screen.stringValue());
        i++;
        assertEquals("click", Hit.HitParam.Touch.stringValue());
        i++;
        assertEquals("s2click", Hit.HitParam.TouchLevel2.stringValue());
        i++;
        assertEquals("s2atc", Hit.HitParam.OnAppAdTouchLevel2.stringValue());
        i++;
        assertEquals("pclick", Hit.HitParam.TouchScreen.stringValue());
        i++;
        assertEquals("patc", Hit.HitParam.OnAppAdTouchScreen.stringValue());
        i++;
        assertEquals("idclient", Hit.HitParam.UserId.stringValue());
        i++;
        assertEquals("ac", Hit.HitParam.VisitorCategory.stringValue());
        i++;
        assertEquals("an", Hit.HitParam.VisitorIdentifierNumeric.stringValue());
        i++;
        assertEquals("at", Hit.HitParam.VisitorIdentifierText.stringValue());
        i++;
        assertEquals("refstore", Hit.HitParam.Refstore.stringValue());
        i++;
        assertEquals("ref", Hit.HitParam.Referrer.stringValue());
        i++;
        assertEquals("tvt", Hit.HitParam.TVT.stringValue());
        i++;
        assertEquals("xto", Hit.HitParam.Source.stringValue());
        i++;
        assertEquals("xtor", Hit.HitParam.RemanentSource.stringValue());
        i++;
        assertEquals("aisl", Hit.HitParam.Aisle.stringValue());
        i++;
        assertEquals("m1", Hit.HitParam.MediaDuration.stringValue());
        i++;
        assertEquals("ptype", Hit.HitParam.CustomTreeStructure.stringValue());
        i++;
        assertEquals("tp", Hit.HitParam.Tp.stringValue());
        i++;
        assertEquals("pdtl", Hit.HitParam.ProductList.stringValue());
        i++;
        assertEquals("pid", Hit.HitParam.DynamicScreenId.stringValue());
        i++;
        assertEquals("pchap", Hit.HitParam.DynamicScreenValue.stringValue());
        i++;
        assertEquals("pidt", Hit.HitParam.DynamicScreenDate.stringValue());
        i++;
        assertEquals("mc", Hit.HitParam.InternalSearchKeyword.stringValue());
        i++;
        assertEquals("np", Hit.HitParam.InternalSearchResultScreenNumber.stringValue());
        i++;
        assertEquals("mcrg", Hit.HitParam.InternalSearchResultPosition.stringValue());
        i++;
        assertEquals("idcart", Hit.HitParam.CartId.stringValue());
        i++;
        assertEquals("s2rich", Hit.HitParam.RichMediaLevel2.stringValue());
        i++;
        assertEquals("prich", Hit.HitParam.RichMediaScreen.stringValue());
        i++;

        assertEquals(length, i);
    }
}
