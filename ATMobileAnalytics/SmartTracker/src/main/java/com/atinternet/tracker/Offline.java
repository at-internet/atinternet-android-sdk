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
import java.util.Date;

/**
 * Wrapper class to manage offline hits stored
 */
public class Offline {

    private final Storage storage;
    private final TrackerListener listener;

    Offline(Tracker tracker) {
        storage = Tracker.getStorage();
        listener = tracker.getListener();
    }

    /**
     * Get stored hits
     *
     * @return stored hits list
     */
    public ArrayList<Hit> get() {
        return storage.getOfflineHits();
    }

    /**
     * Get the count of stored hits
     *
     * @return the count of stored hits
     */
    public int count() {
        return storage.getCountOfflineHits();
    }

    /**
     * Remove all stored hits
     */
    public void delete() {
        storage.removeAllOfflineHits();
    }

    /**
     * Get the first stored hit
     *
     * @return the oldest stored hit instance
     */
    public Hit oldest() {
        return storage.getOldestOfflineHit();
    }

    /**
     * Get the last stored hit
     *
     * @return the latest stored hit instance
     */
    public Hit latest() {
        return storage.getLatestOfflineHit();
    }

    /**
     * Remove stored hits older than days count parameter
     *
     * @param daysCount /
     */
    public void delete(int daysCount) {
        storage.removeOldOfflineHits(daysCount);
    }

    /**
     * Remove stored hits older than a date
     *
     * @param date /
     */
    public void delete(Date date) {
        int daysCount = Tool.getDaysBetweenTimes(System.currentTimeMillis(), date.getTime());
        storage.removeOldOfflineHits(daysCount);
    }

    /**
     * Send all hits stored
     */
    public void dispatch() {
        Sender.sendOfflineHits(listener, storage, true, true);
    }
}
