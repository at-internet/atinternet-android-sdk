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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Wrapper class to manage SelfPromotion instances
 */
public class SelfPromotions extends Helper {

    SelfPromotions(Tracker tracker) {
        super(tracker);
    }

    /**
     * Add a self promotion advertising
     *
     * @param adId advertising identifier
     * @return the SelfPromotion instance
     */
    public SelfPromotion add(int adId) {
        SelfPromotion self = new SelfPromotion(tracker).setAdId(adId);
        tracker.getBusinessObjects().put(self.getId(), self);

        return self;
    }

    /**
     * Send all impressions
     */
    public void sendImpressions() {
        ArrayList<BusinessObject> impressions = new ArrayList<>();
        LinkedHashMap<String, BusinessObject> trackerObjects = tracker.getBusinessObjects();
        Set<String> keys = trackerObjects.keySet();

        for (String key : keys) {
            BusinessObject obj = trackerObjects.get(key);
            if (obj instanceof SelfPromotion) {
                SelfPromotion self = (SelfPromotion) obj;
                if (self.getAction() == OnAppAd.Action.View) {
                    impressions.add(self);
                }
            }
        }

        tracker.getDispatcher().dispatch((BusinessObject[]) impressions.toArray(new BusinessObject[impressions.size()]));
    }
}
