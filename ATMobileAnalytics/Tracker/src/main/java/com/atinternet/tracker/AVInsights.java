/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

import android.util.SparseIntArray;

public class AVInsights {

    private Events events;

    AVInsights(Tracker tracker) {
        this.events = tracker.Events();
    }

    /***
     * Create new Media
     * @return Media instance
     */
    public com.atinternet.tracker.avinsights.Media Media() {
        return new com.atinternet.tracker.avinsights.Media(events);
    }

    /***
     * Create new Media
     * @param heartbeat heartbeat period
     * @param bufferHeartbeat buffer heartbeat period
     * @return Media instance
     */
    public com.atinternet.tracker.avinsights.Media Media(int heartbeat, int bufferHeartbeat) {
        return new com.atinternet.tracker.avinsights.Media(events, heartbeat, bufferHeartbeat, null);
    }

    /***
     * Create new Media
     * @param heartbeat heartbeat period
     * @param bufferHeartbeat buffer heartbeat period
     * @param sessionId custom sessionId
     * @return Media instance
     */
    public com.atinternet.tracker.avinsights.Media Media(int heartbeat, int bufferHeartbeat, String sessionId) {
        return new com.atinternet.tracker.avinsights.Media(events, heartbeat, bufferHeartbeat, sessionId);
    }

    /***
     * Create new Media
     * @param heartbeat heartbeat periods
     * @param bufferHeartbeat buffer heartbeat periods
     * @return Media instance
     */
    public com.atinternet.tracker.avinsights.Media Media(SparseIntArray heartbeat, SparseIntArray bufferHeartbeat) {
        return new com.atinternet.tracker.avinsights.Media(events, heartbeat, bufferHeartbeat, null);
    }

    /***
     * Create new Media
     * @param heartbeat heartbeat periods
     * @param bufferHeartbeat buffer heartbeat periods
     * @param sessionId custom sessionId
     * @return Media instance
     */
    public com.atinternet.tracker.avinsights.Media Media(SparseIntArray heartbeat, SparseIntArray bufferHeartbeat, String sessionId) {
        return new com.atinternet.tracker.avinsights.Media(events, heartbeat, bufferHeartbeat, sessionId);
    }
}
