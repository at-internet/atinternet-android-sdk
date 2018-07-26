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
 * Wrapper class for custom tree structure tracking
 */
public class CustomTreeStructure extends ScreenInfo {

    private static final String CUSTOM_TREE_FORMAT = "%1$s-%2$s-%3$s";

    private int category1;
    private int category2;
    private int category3;

    CustomTreeStructure(Tracker tracker) {
        super(tracker);
        category1 = 0;
        category2 = 0;
        category3 = 0;
    }

    /**
     * Get the first category
     *
     * @return the first category
     */
    public int getCategory1() {
        return category1;
    }

    /**
     * Get the second category
     *
     * @return the second category
     */
    public int getCategory2() {
        return category2;
    }

    /**
     * Get the third category
     *
     * @return the third category
     */
    public int getCategory3() {
        return category3;
    }

    /**
     * Set a new first category
     *
     * @param category1 /
     * @return the CustomTreeStructure instance
     */
    public CustomTreeStructure setCategory1(int category1) {
        this.category1 = category1;
        return this;
    }

    /**
     * Set a new second category
     *
     * @param category2 /
     * @return the CustomTreeStructure instance
     */
    public CustomTreeStructure setCategory2(int category2) {
        this.category2 = category2;
        return this;
    }

    /**
     * Set a new third category
     *
     * @param category3 /
     * @return the CustomTreeStructure instance
     */
    public CustomTreeStructure setCategory3(int category3) {
        this.category3 = category3;
        return this;
    }

    @Override
    void setParams() {
        tracker.setParam(Hit.HitParam.CustomTreeStructure.stringValue(), String.format(CUSTOM_TREE_FORMAT, category1, category2, category3));
    }
}
