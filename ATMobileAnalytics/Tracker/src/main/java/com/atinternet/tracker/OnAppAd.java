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

public abstract class OnAppAd extends BusinessObject {

    public enum Action {
        View("ati"),
        Touch("atc");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    /**
     * Action
     */
    protected Action action;

    public Action getAction() {
        return action;
    }

    OnAppAd(Tracker tracker) {
        super(tracker);
        action = Action.View;
    }

    /**
     * Send this impression
     */
    public void sendImpression() {
        action = Action.View;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send this touch
     */
    public void sendTouch() {
        action = Action.Touch;
        tracker.getDispatcher().dispatch(this);
    }
}
