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

import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Wrapper class for screen tracking
 */
public class Screen extends AbstractScreen {

    private String title;
    private String className;
    private int width;
    private int height;
    private float scale;
    private App app;

    String getClassName() {
        return className;
    }

    Screen setClassName(String className) {
        this.className = className;

        return this;
    }

    String getTitle() {
        return title;
    }

    Screen() {
        super();
        className = (SmartContext.currentFragment != null && SmartContext.currentFragment.get() != null) ? SmartContext.currentFragment.get().getClass().getSimpleName() :
                (SmartContext.currentActivity != null && SmartContext.currentActivity.get() != null) ? SmartContext.currentActivity.get().getClass().getSimpleName() : null;
        title = className;
        app = AutoTracker.getInstance().getApplication();
        if (app != null) {
            width = app.getWidth();
            height = app.getHeight();
            scale = app.getScale();
        }
    }

    float getScale() {
        return scale;
    }

    JSONArray getSuggestedEvents(View rootView) throws JSONException {
        ArrayList<View> touchables = ReflectionAPI.getAllTouchables((ViewGroup) rootView);
        JSONArray jsonArray = new JSONArray();
        for (View v : touchables) {
            int[] coords = new int[2];
            v.getLocationOnScreen(coords);
            SmartEvent event = new SmartEvent(new SmartView(v, coords), rootView, -1, -1, "tap", "single");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", event.getType())
                    .put("data", event.getData());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    JSONObject getData() {
        try {
            JSONObject appObject = new JSONObject();
            JSONObject screenObj = new JSONObject();
            appObject.put("version", app.getVersion())
                    .put("token", AutoTracker.getInstance().getToken())
                    .put("device", app.getDevice())
                    .put("package", app.getPackage())
                    .put("siteID", app.getSiteID())
                    .put("platform", app.getPlatform());

            screenObj.put("title", title)
                    .put("className", className)
                    .put("scale", scale)
                    .put("width", width)
                    .put("height", height)
                    .put("orientation", SensorOrientationManager.orientation)
                    .put("app", appObject);
            return screenObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set a new name
     *
     * @param name /
     * @return the Screen instance
     */
    public Screen setName(String name) {
        this.name = name;

        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return the Screen instance
     */
    public Screen setAction(Action action) {
        this.action = action;

        return this;
    }

    /**
     * Set a new first chapter
     *
     * @param chapter1 /
     * @return the Screen instance
     */
    public Screen setChapter1(String chapter1) {
        this.chapter1 = chapter1;
        return this;
    }

    /**
     * Set a new second chapter
     *
     * @param chapter2 /
     * @return the Screen instance
     */
    public Screen setChapter2(String chapter2) {
        this.chapter2 = chapter2;
        return this;
    }

    /**
     * Set a new third chapter
     *
     * @param chapter3 /
     * @return the Screen instance
     */
    public Screen setChapter3(String chapter3) {
        this.chapter3 = chapter3;
        return this;
    }

    /**
     * Set a new level 2
     *
     * @param level2 /
     * @return the Screen instance
     */
    public Screen setLevel2(int level2) {
        this.level2 = level2;

        return this;
    }

    /**
     * Change boolean "isBasketScreen" value
     *
     * @param isBasketScreen /
     * @return the Screen instance
     */
    public Screen setIsBasketScreen(boolean isBasketScreen) {
        this.isBasketScreen = isBasketScreen;
        return this;
    }

    Screen(Tracker tracker) {
        super(tracker);
    }

    @Override
    void setEvent() {
        super.setEvent();

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

        tracker.Event().set("screen", action.stringValue(), value);
    }
}
