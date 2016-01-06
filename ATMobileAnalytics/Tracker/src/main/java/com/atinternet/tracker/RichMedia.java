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

public abstract class RichMedia extends BusinessObject {

    protected static final int MAX_DURATION = 86400;

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

    /**
     * PRIVATE VARIABLES
     */

    /**
     * Player instance
     */
    protected MediaPlayer mediaPlayer;

    /**
     * Media type
     */
    protected String type;

    /**
     * Broadcast mode
     */
    protected BroadcastMode broadcastMode;


    /**
     * PUBLIC VARIABLES
     */

    /**
     * Action type
     */
    protected Action action;

    /**
     * Media is buffering
     */
    protected boolean isBuffering;

    /**
     * Media is embedded in app
     */
    protected boolean isEmbedded;

    /**
     * Media name
     */
    protected String name;

    /**
     * Media chapter1
     */
    protected String chapter1;

    /**
     * Media chapter2
     */
    protected String chapter2;

    /**
     * Media chapter3
     */
    protected String chapter3;

    /**
     * Level2
     */
    protected int level2;

    /**
     * Duration refresh
     */
    protected int refreshDuration;

    /**
     * WebDomain
     */
    protected String webDomain;

    /**
     * Scheduled executor to refresh
     */
    protected ScheduledThreadPoolExecutor executor;


    public int getLevel2() {
        return level2;
    }

    MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    int getRefreshDuration() {
        return refreshDuration;
    }

    Action getAction() {
        return action;
    }

    BroadcastMode getBroadcastMode() {
        return broadcastMode;
    }

    public boolean isBuffering() {
        return isBuffering;
    }

    public boolean isEmbedded() {
        return isEmbedded;
    }

    public String getName() {
        return name;
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

    String getType() {
        return type;
    }

    public String getWebDomain() {
        return webDomain;
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

    @Override
    void setEvent() {
        ParamOption encode = new ParamOption().setEncode(true);

        tracker.setParam("type", type)
                .setParam("p", buildMediaName(), encode)
                .setParam("a", action.stringValue())
                .setParam("m6", broadcastMode.stringValue())
                .setParam("plyr", mediaPlayer.getPlayerId())
                .setParam("m5", isEmbedded ? "ext" : "int");

        if (level2 > 0) {
            tracker.setParam("s2", level2);
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
                    tracker.setParam("prich", TechnicalContext.screenName, encode);
                }

                if (TechnicalContext.level2 > 0) {
                    tracker.setParam("s2rich", TechnicalContext.level2);
                }
            }
        }
    }

    /**
     * Send play
     */
    public void sendPlay() {
        sendPlay(5);
    }

    /**
     * Send play with a refresh duration
     *
     * @param refreshDuration int
     */
    public void sendPlay(int refreshDuration) {
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
                        tracker.getDispatcher().dispatch(RichMedia.this);
                    }
                }, refreshDuration, refreshDuration, TimeUnit.SECONDS);
            }
        }
        action = RichMedia.Action.Play;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send pause
     */
    public void sendPause() {
        action = RichMedia.Action.Pause;
        if (executor != null) {
            executor.shutdown();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send stop
     */
    public void sendStop() {
        action = RichMedia.Action.Stop;
        if (executor != null) {
            executor.shutdown();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send move
     */
    public void sendMove() {
        action = RichMedia.Action.Move;
        tracker.getDispatcher().dispatch(this);
    }

    String buildMediaName() {
        String mediaName = chapter1 == null ? "" : chapter1 + "::";
        mediaName = chapter2 == null ? mediaName : mediaName + chapter2 + "::";
        mediaName = chapter3 == null ? mediaName : mediaName + chapter3 + "::";

        return mediaName += name;
    }
}
