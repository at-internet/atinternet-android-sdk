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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class CustomObjectsTest extends AbstractTestClass {

    @Test
    public void addAndRemoveInTrackerTest() throws JSONException {
        CustomObjects customObjects = new CustomObjects(tracker);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(1, tracker.getBusinessObjects().size());

        customObjects.remove(co.getId());
        assertEquals(0, tracker.getBusinessObjects().size());
    }

    @Test
    public void addAndRemoveInScreenTest() throws JSONException {
        Screen screen = new Screen(tracker);
        CustomObjects customObjects = new CustomObjects(screen);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(1, screen.getCustomObjectsMap().size());

        customObjects.remove(co.getId());
        assertEquals(0, screen.getCustomObjectsMap().size());
    }

    @Test
    public void addAndRemoveInGestureTest() throws JSONException {
        Gesture g = new Gesture(tracker);
        CustomObjects customObjects = new CustomObjects(g);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(1, g.getCustomObjectsMap().size());

        customObjects.remove(co.getId());
        assertEquals(0, g.getCustomObjectsMap().size());

    }

    @Test
    public void addAndRemoveInPublisherTest() throws JSONException {
        Publisher p = new Publisher(tracker);
        CustomObjects customObjects = new CustomObjects(p);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(1, p.getCustomObjectsMap().size());

        customObjects.remove(co.getId());
        assertEquals(0, p.getCustomObjectsMap().size());
    }

    @Test
    public void addAndRemoveInSelfPromotionTest() throws JSONException {
        SelfPromotion sp = new SelfPromotion(tracker);
        CustomObjects customObjects = new CustomObjects(sp);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(1, sp.getCustomObjectsMap().size());

        customObjects.remove(co.getId());
        assertEquals(0, sp.getCustomObjectsMap().size());
    }

    @Test
    public void addAndRemoveInProductTest() throws JSONException {
        Product p = new Product(tracker);
        CustomObjects customObjects = new CustomObjects(p);
        CustomObject co = customObjects.add(new JSONObject().put("key", "value").toString());
        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(1, p.getCustomObjectsMap().size());

        customObjects.remove(co.getId());
        assertEquals(0, p.getCustomObjectsMap().size());

    }
}
