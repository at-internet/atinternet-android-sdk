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

public abstract class RichMedia extends BusinessObject {

    static final int MAX_DURATION = 86400;
    private final SparseIntArray DEFAULT_SPARSE_ARRAY;
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            tracker.setParam("a", Action.Refresh.stringValue());
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
        Move("move"),
        Share("share"),
        Email("email"),
        Favor("favor"),
        Dowload("download"),
        Info("info");

        private final String str;

        Action(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private final MediaPlayer mediaPlayer;
    private ScheduledExecutorService scheduler;
    private int currentRefreshDurationsSparseArrayIndex, delay, playTimestamp, elapsedTime;
    private ScheduledFuture refreshHandler;
    private SparseIntArray refreshDurationsSparseIntArray;

    String mediaLabel, mediaTheme1, mediaTheme2, mediaTheme3, mediaType, webDomain, linkedContent;
    int mediaLevel2, duration;
    BroadcastMode broadcastMode;
    boolean isEmbedded;


    MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    RichMedia(MediaPlayer player) {
        super(player.getTracker());
        mediaPlayer = player;
        mediaLabel = "";
        mediaType = "";
        broadcastMode = BroadcastMode.Clip;
        mediaLevel2 = -1;
        duration = 0;
        playTimestamp = -1;
        elapsedTime = 0;
        currentRefreshDurationsSparseArrayIndex = 0;
        DEFAULT_SPARSE_ARRAY = new SparseIntArray();
        DEFAULT_SPARSE_ARRAY.append(0, 5);
        DEFAULT_SPARSE_ARRAY.append(1, 15);
        DEFAULT_SPARSE_ARRAY.append(5, 30);
        DEFAULT_SPARSE_ARRAY.append(10, 60);

        isEmbedded = false;
        webDomain = null;
        linkedContent = null;
    }

    /***
     * Get broadcast mode
     * @return BroadcastMode
     */
    public BroadcastMode getBroadcastMode() {
        return broadcastMode;
    }

    /**
     * Get the duration
     *
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /***
     * Get media type
     * @return MediaType
     */
    public String getMediaType() {
        return mediaType;
    }


    /**
     * Get media level 2
     *
     * @return the media level 2
     */
    public int getMediaLevel2() {
        return mediaLevel2;
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
     * Get media label
     *
     * @return the media label
     */
    public String getMediaLabel() {
        return mediaLabel;
    }

    /**
     * Get first theme
     *
     * @return the first theme
     */
    public String getMediaTheme1() {
        return mediaTheme1;
    }

    /**
     * Get second theme
     *
     * @return the second theme
     */
    public String getMediaTheme2() {
        return mediaTheme2;
    }

    /**
     * Get third theme
     *
     * @return the third theme
     */
    public String getMediaTheme3() {
        return mediaTheme3;
    }

    /**
     * Get web domain
     *
     * @return the web domain
     */
    public String getWebDomain() {
        return webDomain;
    }

    /**
     * Get linked content
     *
     * @return the linked content
     */
    public String getLinkedContent() {
        return linkedContent;
    }


    /***
     * Set a new broadcast mode
     * @param mode /
     * @return the RichMedia instance
     */
    public RichMedia setBroadcastMode(BroadcastMode mode) {
        this.broadcastMode = mode;
        return this;
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the RichMedia instance
     */
    public RichMedia setMediaTheme1(String mediaTheme1) {
        this.mediaTheme1 = mediaTheme1;
        return this;
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the RichMedia instance
     */
    public RichMedia setMediaTheme2(String mediaTheme2) {
        this.mediaTheme2 = mediaTheme2;
        return this;
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the RichMedia instance
     */
    public RichMedia setMediaTheme3(String mediaTheme3) {
        this.mediaTheme3 = mediaTheme3;
        return this;
    }

    /**
     * Set a new media level2
     *
     * @param mediaLevel2 /
     * @return RichMedia instance
     */
    public RichMedia setMediaLevel2(int mediaLevel2) {
        this.mediaLevel2 = mediaLevel2;
        return this;
    }

    /**
     * Change boolean "isEmbedded" value
     *
     * @param isEmbedded /
     * @return RichMedia instance
     */
    public RichMedia setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;
        return this;
    }

    /**
     * Set a new webdomain
     *
     * @param webDomain /
     * @return RichMedia instance
     */
    public RichMedia setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }

    /**
     * Set a new linked content
     *
     * @param linkedContent /
     * @return RichMedia instance
     */
    public RichMedia setLinkedContent(String linkedContent) {
        this.linkedContent = linkedContent;
        return this;
    }

    @Override
    void setEvent() {
        ParamOption encode = new ParamOption().setEncode(true);

        tracker.setParam(Hit.HitParam.HitType.stringValue(), mediaType)
                .setParam(Hit.HitParam.Screen.stringValue(), buildMediaLabel(), encode)
                .setParam("m6", broadcastMode.stringValue())
                .setParam("plyr", mediaPlayer.getPlayerId())
                .setParam("m5", isEmbedded ? "ext" : "int");

        if (mediaLevel2 > 0) {
            tracker.setParam(Hit.HitParam.Level2.stringValue(), mediaLevel2);
        }
    }

    /**
     * Send a play action tracking. Refresh is enabled with default duration
     *
     * @param isBuffering /
     */
    public void sendPlay(boolean isBuffering) {
        tracker.setParam("buf", isBuffering ? 1 : 0);
        sendPlay();
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
     * Resume playing media tracking
     *
     * @param isBuffering /
     */
    public void sendResume(boolean isBuffering) {
        tracker.setParam("buf", isBuffering ? 1 : 0);
        sendResume();
    }

    /**
     * Resume playing media tracking
     */
    public void sendResume() {
        processSendPlayWithRefresh();
    }

    /***
     * Send only one play hit
     * @param isBuffering      /
     */
    public void sendPlayWithoutRefresh(boolean isBuffering) {
        tracker.setParam("buf", isBuffering ? 1 : 0);
        sendPlayWithoutRefresh();
    }

    /***
     * Send only one play hit
     */
    public void sendPlayWithoutRefresh() {
        tracker.setParam("a", Action.Play.stringValue());
        setPlayOrInfoParams();
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * /**
     * Send play with a custom refresh duration
     *
     * @param refreshDurations SparseIntArray with (startingMinute,refreshDuration) ex: (0,5) will send one hit every five seconds during the first minute
     * @param isBuffering      /
     */
    public void sendPlay(SparseIntArray refreshDurations, boolean isBuffering) {
        tracker.setParam("buf", isBuffering ? 1 : 0);
        sendPlay(refreshDurations);
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

        tracker.setParam("a", Action.Play.stringValue());
        setPlayOrInfoParams();
        tracker.getDispatcher().dispatch(this);
    }

    private void setPlayOrInfoParams() {
        ParamOption encode = new ParamOption().setEncode(true);

        if (isEmbedded && webDomain != null) {
            tracker.setParam("m9", webDomain, encode);
        }
        String sn = TechnicalContext.getScreenName();
        if (!TextUtils.isEmpty(sn)) {
            tracker.setParam(Hit.HitParam.RichMediaScreen.stringValue(), sn, encode);
        }

        int lvl2 = TechnicalContext.getLevel2();
        if (lvl2 > 0) {
            tracker.setParam(Hit.HitParam.RichMediaLevel2.stringValue(), lvl2);
        }
        if (linkedContent != null) {
            tracker.setParam("clnk", linkedContent, encode);
        }
    }

    /**
     * Send a info action tracking
     */
    public void sendInfo() {
        tracker.setParam("a", Action.Info.stringValue());
        setPlayOrInfoParams();
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a info action tracking
     *
     * @param isBuffering /
     */
    public void sendInfo(boolean isBuffering) {
        tracker.setParam("buf", isBuffering ? 1 : 0);
        sendInfo();
    }

    /**
     * Send a pause action tracking
     */
    public void sendPause() {
        tracker.setParam("a", Action.Pause.stringValue());
        elapsedTime += ((int) (System.currentTimeMillis() / 1000) - playTimestamp);
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a stop action tracking
     */
    public void sendStop() {
        tracker.setParam("a", Action.Stop.stringValue());
        currentRefreshDurationsSparseArrayIndex = 0;
        elapsedTime = 0;
        playTimestamp = -1;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a share action tracking
     */
    public void sendShare() {
        tracker.setParam("a", Action.Share.stringValue());
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a email action tracking
     */
    public void sendEmail() {
        tracker.setParam("a", Action.Email.stringValue());
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a favor action tracking
     */
    public void sendFavor() {
        tracker.setParam("a", Action.Favor.stringValue());
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a download action tracking
     */
    public void sendDownload() {
        tracker.setParam("a", Action.Dowload.stringValue());
        tracker.getDispatcher().dispatch(this);
    }

    /**
     * Send a move action tracking
     */
    public void sendMove() {
        tracker.setParam("a", Action.Move.stringValue());
        tracker.getDispatcher().dispatch(this);
    }

    private String buildMediaLabel() {
        String mediaLabel = mediaTheme1 == null ? "" : mediaTheme1 + "::";
        mediaLabel = mediaTheme2 == null ? mediaLabel : mediaLabel + mediaTheme2 + "::";
        mediaLabel = mediaTheme3 == null ? mediaLabel : mediaLabel + mediaTheme3 + "::";

        return mediaLabel + this.mediaLabel;
    }

    /**
     * Get level 2
     *
     * @return the level 2
     * @deprecated please use getMediaLevel2() instead
     */
    @Deprecated
    public int getLevel2() {
        return getMediaLevel2();
    }

    /**
     * Get boolean "isBuffering" value
     *
     * @return true if the media is buffering
     * @deprecated useless, property set when send called
     */
    @Deprecated
    public boolean isBuffering() {
        return false;
    }

    /**
     * Get media name
     *
     * @return the media name
     * @deprecated please use getMediaLabel() instead
     */
    @Deprecated
    public String getName() {
        return getMediaLabel();
    }

    /**
     * Get first chapter
     *
     * @return the first chapter
     * @deprecated please use getMediaTheme1() instead
     */
    @Deprecated
    public String getChapter1() {
        return getMediaTheme1();
    }

    /**
     * Get second chapter
     *
     * @return the second chapter
     * @deprecated please use getMediaTheme2() instead
     */
    @Deprecated
    public String getChapter2() {
        return getMediaTheme2();
    }

    /**
     * Get third chapter
     *
     * @return the third chapter
     * @deprecated please use getMediaTheme3() instead
     */
    @Deprecated
    public String getChapter3() {
        return getMediaTheme3();
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
}
