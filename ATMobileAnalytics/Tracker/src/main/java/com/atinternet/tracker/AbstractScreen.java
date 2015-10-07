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

abstract class AbstractScreen extends BusinessObject {

    public AbstractScreen(Tracker tracker) {
        super(tracker);
        action = Action.View;
        level2 = -1;
        name = "";
    }

    public enum Action {
        View("view");

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
    protected String name;

    /**
     * Chapter 1
     */
    protected String chapter1;

    /**
     * Chapter 2
     */
    protected String chapter2;

    /**
     * Chapter 3
     */
    protected String chapter3;

    /**
     * Action type
     */
    protected Action action;

    /**
     * Screen contains cart info
     */
    protected boolean isBasketScreen;

    /**
     * Level2
     */
    protected int level2;

    public String getName() {
        return name;
    }

    public Action getAction() {
        return action;
    }

    public int getLevel2() {
        return level2;
    }

    public String getChapter1() {
        return chapter1;
    }

    public String getChapter2() {
        return chapter2;
    }

    public String getChapter3() {
        return chapter3;
    }

    public boolean isBasketScreen() {
        return isBasketScreen;
    }

    /**
     * Send a screen view event
     */
    public void sendView() {
        action = Action.View;
        tracker.getDispatcher().dispatch(this);
    }

    @Override
    void setEvent() {
        if (level2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), level2);
        }

        if (isBasketScreen) {
            tracker.setParam("tp", "cart");
        }
    }
}
