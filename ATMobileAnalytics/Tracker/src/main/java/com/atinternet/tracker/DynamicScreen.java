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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DynamicScreen extends AbstractScreen {

    private static final String UPDATE_FORMAT = "yyyyMMddHHmm";

    private String screenId;

    private Date update;

    /**
     * Get the id
     *
     * @return String
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Get the update date
     *
     * @return Date
     */
    public Date getUpdate() {
        return update;
    }


    /**
     * Set a new id
     *
     * @param screenId int
     * @return DynamicScreen
     * @deprecated Since 2.2.1, use {@link #setScreenId(String)} instead.
     */
    @Deprecated
    public DynamicScreen setScreenId(int screenId) {
        this.screenId = String.valueOf(screenId);
        return this;
    }

    /**
     * Set a new id
     *
     * @param screenId String
     * @return DynamicScreen
     */
    public DynamicScreen setScreenId(String screenId) {
        this.screenId = screenId;
        return this;
    }

    /**
     * Set a new first chapter
     *
     * @param chapter1 String
     * @return DynamicScreen
     */
    public DynamicScreen setChapter1(String chapter1) {
        this.chapter1 = chapter1;
        return this;
    }

    /**
     * Set a new second chapter
     *
     * @param chapter2 String
     * @return DynamicScreen
     */
    public DynamicScreen setChapter2(String chapter2) {
        this.chapter2 = chapter2;
        return this;
    }

    /**
     * Set a new third chapter
     *
     * @param chapter3 String
     * @return DynamicScreen
     */
    public DynamicScreen setChapter3(String chapter3) {
        this.chapter3 = chapter3;
        return this;
    }

    /**
     * Set a new update date
     *
     * @param update Date
     * @return DynamicScreen
     */
    public DynamicScreen setUpdate(Date update) {
        this.update = update;
        return this;
    }

    /**
     * Set a new name
     *
     * @param name String
     * @return DynamicScreen
     */
    public DynamicScreen setName(String name) {
        this.name = name;

        return this;
    }

    /**
     * Set a new action
     *
     * @param action Action
     * @return DynamiceScreen
     */
    public DynamicScreen setAction(Action action) {
        this.action = action;

        return this;
    }

    /**
     * Set a new level 2
     *
     * @param level2 int
     * @return DynamicScreen
     */
    public DynamicScreen setLevel2(int level2) {
        this.level2 = level2;

        return this;
    }

    /**
     * Change boolean isBasketScreen value
     *
     * @param isBasketScreen boolean
     * @return DynamicScreen
     */
    public DynamicScreen setIsBasketScreen(boolean isBasketScreen) {
        this.isBasketScreen = isBasketScreen;
        return this;
    }

    DynamicScreen(Tracker tracker) {
        super(tracker);
        screenId = null;
        chapter1 = null;
        chapter2 = null;
        chapter3 = null;
        update = null;
    }

    @Override
    void setEvent() {
        super.setEvent();

        if (screenId.length() > 255) {
            screenId = "";
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.warning, "screenId too long, replaced by empty value");
        }

        tracker.setParam(Hit.HitParam.DynamicScreenId.stringValue(), screenId);
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

        tracker.setParam(Hit.HitParam.DynamicScreenValue.stringValue(), value, new ParamOption().setEncode(true))
                .setParam(Hit.HitParam.DynamicScreenDate.stringValue(), new SimpleDateFormat(UPDATE_FORMAT, Locale.getDefault()).format(update))
                .Event().set("screen", action.stringValue(), name);
    }
}
