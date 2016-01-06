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

import android.text.TextUtils;

public class Gesture extends BusinessObject {

    public enum Action {
        Touch("A"),
        Navigate("N"),
        Download("T"),
        Exit("S"),
        InternalSearch("IS");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    /**
     * Screen name
     */
    private String name;

    /**
     * Action type
     */
    private Action action;

    /**
     * Level2
     */
    private int level2;

    public String getName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    public int getLevel2() {
        return level2;
    }

    Gesture setName(String name) {
        this.name = name;

        return this;
    }

    public Gesture setAction(Action action) {
        this.action = action;

        return this;
    }

    public Gesture setLevel2(int level2) {
        this.level2 = level2;

        return this;
    }

    /**
     * Constructor
     *
     * @param tracker Tracker
     */
    Gesture(Tracker tracker) {
        super(tracker);
        action = Action.Touch;
        level2 = -1;
        name = "";
    }

    @Override
    void setEvent() {
        if (!TextUtils.isEmpty(TechnicalContext.screenName)) {
            tracker.setParam(Hit.HitParam.TouchScreen.stringValue(), TechnicalContext.screenName, new ParamOption().setEncode(true));
        }

        if (TechnicalContext.level2 > 0) {
            tracker.setParam(Hit.HitParam.TouchLevel2.stringValue(), TechnicalContext.level2);
        }

        if (level2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), level2);
        }

        tracker.setParam(Hit.HitParam.Touch.stringValue(), action.stringValue())
                .Event().set("click", action.stringValue(), name);
    }

    /**
     * User navigate in app
     */
    public void sendNavigation() {
        action = Action.Navigate;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * User exits (home button or go to the another app)
     */
    public void sendExit() {
        action = Action.Exit;
        tracker.getDispatcher().dispatch(this);
    }

    public void sendDownload() {
        action = Action.Download;

        tracker.getDispatcher().dispatch(this);
    }

    public void sendTouch() {
        action = Action.Touch;
        tracker.getDispatcher().dispatch(this);
    }

    public void sendSearch() {
        action = Action.InternalSearch;
        tracker.getDispatcher().dispatch(this);
    }
}
