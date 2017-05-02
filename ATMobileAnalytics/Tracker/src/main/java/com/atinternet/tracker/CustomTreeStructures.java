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
 * Wrapper class to manage CustomTreeStructure instances
 *
 * @deprecated Since 2.3.0, CustomTreeStructure is now only available as a screen object property.
 */
@Deprecated
public class CustomTreeStructures extends Helper {

    CustomTreeStructures(Tracker tracker) {
        super(tracker);
    }

    /**
     * Add a custom tree structure
     *
     * @param category1 first custom tree structure category
     * @return a CustomTreeStructure instance
     */
    public CustomTreeStructure add(int category1) {
        CustomTreeStructure customTreeStructure = new CustomTreeStructure(tracker).setCategory1(category1);

        tracker.getBusinessObjects().put(customTreeStructure.getId(), customTreeStructure);
        return customTreeStructure;
    }

    /**
     * Add a custom tree structure
     *
     * @param category1 first custom tree structure category
     * @param category2 second custom tree structure category
     * @return a CustomTreeStructure instance
     */
    public CustomTreeStructure add(int category1, int category2) {
        return add(category1).setCategory2(category2);
    }

    /**
     * Add a custom tree structure
     *
     * @param category1 first custom tree structure category
     * @param category2 second custom tree structure category
     * @param category3 third custom tree structure category
     * @return a CustomTreeStructure instance
     */
    public CustomTreeStructure add(int category1, int category2, int category3) {
        return add(category1, category2).setCategory3(category3);
    }
}
