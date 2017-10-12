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

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class to manage rich media tracking
 */
public abstract class RichMedia extends BusinessObject {

    static final int MAX_DURATION = 86400;

    /**
     * Enum with different broadcast mode
     */
    public enum BroadcastMode {
        Clip("clip"),
        Live("live");

        private final String str;

        BroadcastMode(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    /**
     * Enum with different action types
     */
    public enum Action {
        Play("play"),
        Pause("pause"),
        Stop("stop"),
        Refresh("refresh"),
        Move("move");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private final MediaPlayer mediaPlayer;
    private final int refreshDuration;
    String type;
    BroadcastMode broadcastMode;
    Action action;
    boolean isBuffering;
    boolean isEmbedded;
    String name;
    String chapter1;
    String chapter2;
    String chapter3;
    int level2;
    String webDomain;

    ScheduledThreadPoolExecutor executor;

    MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    Action getAction() {
        return action;
    }

    BroadcastMode getBroadcastMode() {
        return broadcastMode;
    }

    int getRefreshDuration() {
        return refreshDuration;
    }

    String getType() {
        return type;
    }

    RichMedia(MediaPlayer player) {
        super(player.getTracker());
        mediaPlayer = player;
        name = "";
        level2 = -1;
        refreshDuration = -1;
        isEmbedded = false;
        isBuffering = false;
        webDomain = null;
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
     * Get boolean "isBuffering" value
     *
     * @return true if the media is buffering
     */
    public boolean isBuffering() {
        return isBuffering;
    }

    /**
     * Get boolean "isEmbedded" value
     *
     * @return true if the media is embedded in app
     */
    public boolean isEmbedded() {
        return isEmbedded;
    }

    /**
     * Get media name
     *
     * @return the media name
     */
    public String getName() {
        return name;
    }

    /**
     * Get first chapter
     *
     * @return the first chapter
     */
    public String getChapter1() {
        return chapter1;
    }

    /**
     * Get second chapter
     *
     * @return the second chapter
     */
    public String getChapter2() {
        return chapter2;
    }

    /**
     * Get third chapter
     *
     * @return the third chapter
     */
    public String getChapter3() {
        return chapter3;
    }

    /**
     * Get web domain
     *
     * @return the web domain
     */
    public String getWebDomain() {
        return webDomain;
    }

    @Override
    void setEvent() {
        ParamOption encode = new ParamOption().setEncode(true);

        tracker.setParam(Hit.HitParam.HitType.stringValue(), type)
                .setParam(Hit.HitParam.Screen.stringValue(), buildMediaName(), encode)
                .setParam("a", action.stringValue())
                .setParam("m6", broadcastMode.stringValue())
                .setParam("plyr", mediaPlayer.getPlayerId())
                .setParam("m5", isEmbedded ? "ext" : "int");

        if (level2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), level2);
        }

        if (action == RichMedia.Action.Play) {
            if (isBuffering) {
                tracker.setParam("buf", 1);
            }


            if (isEmbedded && webDomain != null) {
                tracker.setParam("m9", webDomain);
            }

            if (!isEmbedded) {
                if (!TextUtils.isEmpty(TechnicalContext.screenName)) {
                    tracker.setParam(Hit.HitParam.RichMediaScreen.stringValue(), TechnicalContext.screenName, encode);
                }

                if (TechnicalContext.level2 > 0) {
                    tracker.setParam(Hit.HitParam.RichMediaLevel2.stringValue(), TechnicalContext.level2);
                }
            }
        }
    }

    /**
     * Send a play action tracking. Refresh is enabled with default duration
     */
    public void sendPlay() {
        sendPlay(5);
    }

    /**
     * Send play with a custom refresh duration
     *
     * @param refreshDuration refresh duration in second, must be higher than 5
     */
    public void sendPlay(int refreshDuration) {
        final RichMedia self = this;
        if (refreshDuration != 0) {
            if (refreshDuration < 5) {
                refreshDuration = 5;
            }
            if (executor == null || executor.isShutdown()) {
                executor = new ScheduledThreadPoolExecutor(1);
                executor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        action = RichMedia.Action.Refresh;
                        tracker.getDispatcher().dispatch(self);
                    }
                }, refreshDuration, refreshDuration, TimeUnit.SECONDS);
            }
        }
        action = RichMedia.Action.Play;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a pause action tracking
     */
    public void sendPause() {
        action = RichMedia.Action.Pause;
        if (executor != null) {
            executor.shutdown();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a stop action tracking
     */
    public void sendStop() {
        action = RichMedia.Action.Stop;
        if (executor != null) {
            executor.shutdown();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a move action tracking
     */
    public void sendMove() {
        action = RichMedia.Action.Move;
        tracker.getDispatcher().dispatch(this);
    }

    private String buildMediaName() {
        String mediaName = chapter1 == null ? "" : chapter1 + "::";
        mediaName = chapter2 == null ? mediaName : mediaName + chapter2 + "::";
        mediaName = chapter3 == null ? mediaName : mediaName + chapter3 + "::";

        return mediaName + name;
    }
}
