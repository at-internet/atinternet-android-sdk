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

import java.util.LinkedHashMap;

/**
 * Wrapper class for gesture tracking
 */
public class Gesture extends BusinessObject {

    /**
     * Enum with different type of gesture
     */
    public enum Action {
        /**
         * Simple gesture
         */
        Touch("A"),
        /**
         * Navigation gesture
         */
        Navigate("N"),
        /**
         * Download action
         */
        Download("T"),
        /**
         * Exit action
         */
        Exit("S"),
        /**
         * Internal search action
         */
        InternalSearch("IS");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private String name;
    private String chapter1;
    private String chapter2;
    private String chapter3;
    private Action action;
    private int level2;

    private LinkedHashMap<String, CustomObject> customObjectsMap;
    private InternalSearch internalSearch;
    private CustomObjects customObjects;

    LinkedHashMap<String, CustomObject> getCustomObjectsMap() {
        return customObjectsMap == null ? (customObjectsMap = new LinkedHashMap<>()) : customObjectsMap;
    }

    Gesture(Tracker tracker) {
        super(tracker);
        action = Action.Touch;
        level2 = -1;
        name = "";
    }

    /**
     * Attach an InternalSearch to gesture
     *
     * @param keywordLabel       String
     * @param resultScreenNumber int
     * @return InternalSearch
     * @deprecated Since 2.3.4, use {@link #InternalSearch(String, int, int)} instead.
     */
    @Deprecated
    public InternalSearch InternalSearch(String keywordLabel, int resultScreenNumber) {
        return internalSearch == null ? (internalSearch = new InternalSearch(tracker)
                .setKeyword(keywordLabel)
                .setResultScreenNumber(resultScreenNumber)) : internalSearch;
    }

    /**
     * Attach internal search information to gesture
     *
     * @param keywordLabel       keyword has been searched
     * @param resultScreenNumber screen result number
     * @param resultPosition     screen result position gesture
     * @return the InternalSearch instance
     */
    public InternalSearch InternalSearch(String keywordLabel, int resultScreenNumber, int resultPosition) {
        return internalSearch == null ? (internalSearch = new InternalSearch(tracker)
                .setKeyword(keywordLabel)
                .setResultScreenNumber(resultScreenNumber)
                .setResultPosition(resultPosition)) : internalSearch;
    }

    /**
     * Get CustomObjects
     *
     * @return CustomObjects instance
     */
    public CustomObjects CustomObjects() {
        return customObjects == null ? (customObjects = new CustomObjects(this)) : customObjects;
    }

    /**
     * Get the gesture name
     *
     * @return the gesture name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the first chapter
     *
     * @return the first chapter1
     */
    public String getChapter1() {
        return chapter1;
    }

    /**
     * Get the second chapter
     *
     * @return the second chapter
     */
    public String getChapter2() {
        return chapter2;
    }

    /**
     * Get the third chapter
     *
     * @return the third chapter
     */
    public String getChapter3() {
        return chapter3;
    }

    /**
     * Get the action type
     *
     * @return the action type
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get the level 2
     *
     * @return the level 2
     */
    public int getLevel2() {
        return level2;
    }

    /**
     * Set a new gesture name
     *
     * @param name /
     * @return Gesture instance
     */
    public Gesture setName(String name) {
        this.name = name;

        return this;
    }

    /**
     * Set a new first chapter
     *
     * @param chapter1 /
     * @return Gesture instance
     */
    public Gesture setChapter1(String chapter1) {
        this.chapter1 = chapter1;

        return this;
    }

    /**
     * Set a new second chapter
     *
     * @param chapter2 /
     * @return Gesture instance
     */
    public Gesture setChapter2(String chapter2) {
        this.chapter2 = chapter2;

        return this;
    }

    /**
     * Set a new third chapter
     *
     * @param chapter3 /
     * @return Gesture instance
     */
    public Gesture setChapter3(String chapter3) {
        this.chapter3 = chapter3;

        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return Gesture instance
     */
    public Gesture setAction(Action action) {
        this.action = action;

        return this;
    }

    /**
     * Set a new level2
     *
     * @param level2 /
     * @return Gesture instance
     */
    public Gesture setLevel2(int level2) {
        this.level2 = level2;

        return this;
    }

    /**
     * Send a gesture navigation event
     */
    public void sendNavigation() {
        action = Action.Navigate;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a gesture exit event
     */
    public void sendExit() {
        action = Action.Exit;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a gesture download event
     */
    public void sendDownload() {
        action = Action.Download;

        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a gesture action event
     */
    public void sendTouch() {
        action = Action.Touch;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a gesture search event
     */
    public void sendSearch() {
        action = Action.InternalSearch;
        tracker.getDispatcher().dispatch(this);
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

        if (internalSearch != null) {
            action = Action.InternalSearch;
            internalSearch.setEvent();
        }

        String value = chapter1;
        if (value == null) {
            value = chapter2;
        } else {
            value += chapter2 == null ? "" : "::" + chapter2;
        }
        if (value == null) {
            value = chapter3;
        } else {
            value += chapter3 == null ? "" : "::" + chapter3;
        }

        if (value == null) {
            value = name;
        } else {
            value += name == null ? "" : "::" + name;
        }

        if (customObjectsMap != null) {
            for (CustomObject co : customObjectsMap.values()) {
                co.setEvent();
            }
        }

        tracker.setParam(Hit.HitParam.Touch.stringValue(), action.stringValue())
                .Event().set("click", action.stringValue(), value);
    }
}
