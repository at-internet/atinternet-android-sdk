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
 * Wrapper class to manage global context tracking
 */
public class Context {

    /**
     * Enum with different background mode
     */
    public enum BackgroundMode {
        /**
         * Foreground tracking
         */
        Normal,
        /**
         * Tracking in background
         */
        Task
    }

    // Tracker instance
    private final Tracker tracker;

    // Tracker level2
    private int level2 = 0;

    // Tracker background mode
    private BackgroundMode backgroundMode = null;

    Context(Tracker tracker) {
        this.tracker = tracker;
    }

    /**
     * Get level 2
     *
     * @return the level 2
     */
    public int getLevel2() {
        return level2;
    }

    /**
     * Get the background mode
     *
     * @return the BackgroundMode
     */
    public BackgroundMode getBackgroundMode() {
        return backgroundMode;
    }

    /**
     * Set a new level 2
     *
     * @param level2 /
     */
    public void setLevel2(int level2) {
        this.level2 = level2;
        if (level2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), level2, new ParamOption().setPersistent(true));
        } else {
            tracker.unsetParam(Hit.HitParam.Level2.stringValue());
        }
    }

    /**
     * Set a new background mode
     *
     * @param backgroundMode /
     */
    public void setBackgroundMode(BackgroundMode backgroundMode) {
        this.backgroundMode = backgroundMode;

        if (backgroundMode != null) {
            switch (backgroundMode) {
                case Task:
                    tracker.setParam(Hit.HitParam.BackgroundMode.stringValue(), "task", new ParamOption().setPersistent(true));
                    break;
                default:
                    tracker.unsetParam(Hit.HitParam.BackgroundMode.stringValue());
                    break;
            }
        }
    }
}
