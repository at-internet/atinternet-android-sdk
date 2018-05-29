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
import android.util.SparseIntArray;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class to manage rich media tracking
 */
public abstract class RichMedia extends BusinessObject {

    static final int MAX_DURATION = 86400;
    private final SparseIntArray DEFAULT_SPARSE_ARRAY;
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            action = RichMedia.Action.Refresh;
            tracker.getDispatcher().dispatch(RichMedia.this);
        }
    };

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

    BroadcastMode broadcastMode;
    Action action;
    boolean isBuffering, isEmbedded;
    String name, chapter1, chapter2, type, chapter3, webDomain;
    int level2, currentRefreshDurationsSparseArrayIndex, delay, playTimestamp, elapsedTime;

    ScheduledExecutorService scheduler;
    ScheduledFuture refreshHandler;
    SparseIntArray refreshDurationsSparseIntArray;

    MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    Action getAction() {
        return action;
    }

    BroadcastMode getBroadcastMode() {
        return broadcastMode;
    }

    String getType() {
        return type;
    }

    RichMedia(MediaPlayer player) {
        super(player.getTracker());
        mediaPlayer = player;
        name = "";
        level2 = -1;
        playTimestamp = -1;
        elapsedTime = 0;
        currentRefreshDurationsSparseArrayIndex = 0;
        DEFAULT_SPARSE_ARRAY = new SparseIntArray();
        DEFAULT_SPARSE_ARRAY.append(0, 5);
        DEFAULT_SPARSE_ARRAY.append(1, 15);
        DEFAULT_SPARSE_ARRAY.append(5, 30);
        DEFAULT_SPARSE_ARRAY.append(10, 60);

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
                String sn = TechnicalContext.getScreenName();
                if (!TextUtils.isEmpty(sn)) {
                    tracker.setParam(Hit.HitParam.RichMediaScreen.stringValue(), sn, encode);
                }

                int lvl2 = TechnicalContext.getLevel2();
                if (lvl2 > 0) {
                    tracker.setParam(Hit.HitParam.RichMediaLevel2.stringValue(), lvl2);
                }
            }
        }
    }

    /**
     * Send a play action tracking. Refresh is enabled with default duration
     */
    public void sendPlay() {
        currentRefreshDurationsSparseArrayIndex = 0;
        elapsedTime = 0;
        playTimestamp = -1;
        refreshDurationsSparseIntArray = DEFAULT_SPARSE_ARRAY;
        processSendPlayWithRefresh();
    }

    /**
     * Send play with a custom refresh duration
     *
     * @param refreshDuration refresh duration in second, must be higher than 5
     * @deprecated please use sendPlay(SparseIntArray) instead
     */
    @Deprecated
    public void sendPlay(int refreshDuration) {
        if (refreshDuration != 0) {
            SparseIntArray arr = new SparseIntArray();
            arr.append(0, refreshDuration);
            sendPlay(arr);
        } else {
            sendPlayWithoutRefresh();
        }
    }

    /***
     * Send only one play hit
     */
    public void sendPlayWithoutRefresh() {
        action = RichMedia.Action.Play;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * /**
     * Send play with a custom refresh duration
     *
     * @param refreshDurations SparseIntArray with (startingMinute,refreshDuration) ex: (0,5) will send one hit every five seconds during the first minute
     */
    public void sendPlay(SparseIntArray refreshDurations) {
        /// Verifications des valeurs de refresh
        if (refreshDurations == null) {
            refreshDurations = DEFAULT_SPARSE_ARRAY;
        }
        int length = refreshDurations.size();
        if (length == 0) {
            refreshDurations = DEFAULT_SPARSE_ARRAY;
        }
        for (int i = 0; i < length; i++) {
            if (refreshDurations.valueAt(i) < 5) {
                refreshDurations.put(refreshDurations.keyAt(i), 5);
            }
        }
        if (refreshDurations.indexOfKey(0) < 0) {
            refreshDurations.put(0, 5);
        }
        refreshDurationsSparseIntArray = Tool.sortSparseIntArrayByKey(refreshDurations);
        elapsedTime = 0;
        playTimestamp = -1;
        currentRefreshDurationsSparseArrayIndex = 0;
        processSendPlayWithRefresh();
    }

    private void processSendPlayWithRefresh() {
        /// Check sur l'état du scheduler
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }

        playTimestamp = (int) (System.currentTimeMillis() / 1000);

        /// Démarrage du refresh avec le premier intervalle
        int rd = refreshDurationsSparseIntArray.valueAt(currentRefreshDurationsSparseArrayIndex);
        refreshHandler = scheduler.scheduleWithFixedDelay(refreshRunnable, rd, rd, TimeUnit.SECONDS);

        /// Process pour la mise en place du changement d'intervalle dynamique
        /// Si il y a une autre définition d'intervalle
        if (currentRefreshDurationsSparseArrayIndex < refreshDurationsSparseIntArray.size() - 1) {

            /// Calcul du délai avant de procéder au changement de durée de refresh
            int startedMinute = refreshDurationsSparseIntArray.keyAt(currentRefreshDurationsSparseArrayIndex);
            int nextStartedMinute = refreshDurationsSparseIntArray.keyAt(currentRefreshDurationsSparseArrayIndex + 1);
            if (elapsedTime == 0) {
                delay = (nextStartedMinute - startedMinute) * 60;
            } else {
                delay = nextStartedMinute * 60 - elapsedTime;
            }

            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    currentRefreshDurationsSparseArrayIndex++;
                    refreshHandler.cancel(false);
                    int rd = refreshDurationsSparseIntArray.valueAt(currentRefreshDurationsSparseArrayIndex);
                    refreshHandler = scheduler.scheduleWithFixedDelay(refreshRunnable, 0, rd, TimeUnit.SECONDS);

                    if (currentRefreshDurationsSparseArrayIndex < refreshDurationsSparseIntArray.size() - 1) {
                        int startedMinute = refreshDurationsSparseIntArray.keyAt(currentRefreshDurationsSparseArrayIndex);
                        int nextStartedMinute = refreshDurationsSparseIntArray.keyAt(currentRefreshDurationsSparseArrayIndex + 1);
                        delay = (nextStartedMinute - startedMinute) * 60;
                        scheduler.schedule(this, delay, TimeUnit.SECONDS);
                    }
                }
            }, delay, TimeUnit.SECONDS);
        }
        action = RichMedia.Action.Play;
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a pause action tracking
     */
    public void sendPause() {
        action = RichMedia.Action.Pause;
        elapsedTime += ((int) (System.currentTimeMillis() / 1000) - playTimestamp);
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Resume playing media tracking
     */
    public void sendResume() {
        processSendPlayWithRefresh();
    }

    /**
     * Send a stop action tracking
     */
    public void sendStop() {
        action = RichMedia.Action.Stop;
        currentRefreshDurationsSparseArrayIndex = 0;
        elapsedTime = 0;
        playTimestamp = -1;
        if (scheduler != null) {
            scheduler.shutdownNow();
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
