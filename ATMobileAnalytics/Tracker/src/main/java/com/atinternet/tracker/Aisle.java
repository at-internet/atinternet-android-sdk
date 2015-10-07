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

public class Aisle extends ScreenInfo {

    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
    private String level6;


    public String getLevel1() {
        return level1;
    }

    public String getLevel2() {
        return level2;
    }

    public String getLevel3() {
        return level3;
    }

    public String getLevel4() {
        return level4;
    }

    public String getLevel5() {
        return level5;
    }

    public String getLevel6() {
        return level6;
    }

    public Aisle setLevel1(String level1) {
        this.level1 = level1;
        return this;
    }

    public Aisle setLevel2(String level2) {
        this.level2 = level2;
        return this;
    }

    public Aisle setLevel3(String level3) {
        this.level3 = level3;
        return this;
    }

    public Aisle setLevel4(String level4) {
        this.level4 = level4;
        return this;
    }

    public Aisle setLevel5(String level5) {
        this.level5 = level5;
        return this;
    }

    public Aisle setLevel6(String level6) {
        this.level6 = level6;
        return this;
    }

    Aisle(Tracker tracker) {
        super(tracker);
    }

    @Override
    void setEvent() {
        String value = level1;

        if (value == null) {
            value = level2;
        } else {
            value += level2 == null ? "" : "::" + level2;
        }

        if (value == null) {
            value = level3;
        } else {
            value += level3 == null ? "" : "::" + level3;
        }
        if (value == null) {
            value = level4;
        } else {
            value += level4 == null ? "" : "::" + level4;
        }

        if (value == null) {
            value = level5;
        } else {
            value += level5 == null ? "" : "::" + level5;
        }

        if (value == null) {
            value = level6;
        } else {
            value += level6 == null ? "" : "::" + level6;
        }

        tracker.setParam("aisl", value, new ParamOption().setEncode(true));
    }
}
