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

/**
 * Wrapper class for screen tracking
 */
public class Screen extends AbstractScreen {

    private String builtScreenName;

    Screen(Tracker tracker) {
        super(tracker);
        builtScreenName = "";
    }

    /**
     * Set a new name
     *
     * @param name /
     * @return the Screen instance
     */
    public Screen setName(String name) {
        this.name = name;
        updateBuiltScreenName();
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
        updateBuiltScreenName();
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
        updateBuiltScreenName();
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
        updateBuiltScreenName();
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
        TechnicalContext.setLevel2(level2);
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

    void updateBuiltScreenName() {
        builtScreenName = chapter1;
        if (builtScreenName == null) {
            builtScreenName = chapter2;
        } else {
            builtScreenName += chapter2 == null ? "" : "::" + chapter2;
        }
        if (builtScreenName == null) {
            builtScreenName = chapter3;
        } else {
            builtScreenName += chapter3 == null ? "" : "::" + chapter3;
        }
        if (builtScreenName == null) {
            builtScreenName = name;
        } else {
            builtScreenName += name == null ? "" : "::" + name;
        }

        TechnicalContext.setScreenName(builtScreenName);
        CrashDetectionHandler.setCrashLastScreen(builtScreenName);
    }

    @Override
    void setParams() {
        super.setParams();
        tracker.setParam(Hit.HitParam.Screen.stringValue(), builtScreenName,
                new ParamOption().setRelativePosition(ParamOption.RelativePosition.after)
                        .setRelativeParameterKey(Hit.HitParam.UserId.stringValue())
                        .setEncode(true));
    }
}