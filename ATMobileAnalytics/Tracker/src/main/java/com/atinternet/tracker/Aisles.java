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
 * Wrapper class to manage Aisle instances
 *
 * @deprecated Since 2.3.0, Aisle is now only available as a screen object property.
 */
@Deprecated
public class Aisles extends Helper {

    Aisles(Tracker tracker) {
        super(tracker);
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @return a Aisle instance
     */
    public Aisle add(String level1) {
        Aisle aisle = new Aisle(tracker)
                .setLevel1(level1);
        tracker.getBusinessObjects().put(aisle.getId(), aisle);

        return aisle;
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @param level2 aisle second level
     * @return a Aisle instance
     */
    public Aisle add(String level1, String level2) {
        return add(level1).setLevel2(level2);
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @param level2 aisle second level
     * @param level3 aisle third level
     * @return a Aisle instance
     */
    public Aisle add(String level1, String level2, String level3) {
        return add(level1, level2).setLevel3(level3);
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @param level2 aisle second level
     * @param level3 aisle third level
     * @param level4 aisle fourth level
     * @return a Aisle instance
     */
    public Aisle add(String level1, String level2, String level3, String level4) {
        return add(level1, level2, level3).setLevel4(level4);
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @param level2 aisle second level
     * @param level3 aisle third level
     * @param level4 aisle fourth level
     * @param level5 aisle fifth level
     * @return a Aisle instance
     */
    public Aisle add(String level1, String level2, String level3, String level4, String level5) {
        return add(level1, level2, level3, level4).setLevel5(level5);
    }

    /**
     * Add an aisle
     *
     * @param level1 aisle first level
     * @param level2 aisle second level
     * @param level3 aisle third level
     * @param level4 aisle fourth level
     * @param level5 aisle fifth level
     * @param level6 aisle sixth level
     * @return a Aisle instance
     */
    public Aisle add(String level1, String level2, String level3, String level4, String level5, String level6) {
        return add(level1, level2, level3, level4, level5).setLevel6(level6);
    }
}
