/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

/**
 * Wrapper class to manage rich media tracking
 */
public class MediaPlayer {

    private final Tracker tracker;
    private int playerId;
    private Videos videos;
    private Audios audios;
    private LiveVideos liveVideos;
    private LiveAudios liveAudios;
    private Media media;
    private LiveMedia liveMedia;

    MediaPlayer(Tracker tracker) {
        this.tracker = tracker;
        playerId = 1;
    }

    Tracker getTracker() {
        return tracker;
    }

    /**
     * Get Media instance
     *
     * @return the Media instance
     */
    public Media Media() {
        if (media == null) {
            media = new Media(this);
        }
        return media;
    }

    /**
     * Get LiveMedia instance
     *
     * @return the LiveMedia instance
     */
    public LiveMedia LiveMedia() {
        if (liveMedia == null) {
            liveMedia = new LiveMedia(this);
        }
        return liveMedia;
    }

    /**
     * Get Videos instance
     *
     * @return the Videos instance
     */
    public Videos Videos() {
        if (videos == null) {
            videos = new Videos(this);
        }
        return videos;
    }

    /**
     * Get Audios instance
     *
     * @return the Audios instance
     */
    public Audios Audios() {
        if (audios == null) {
            audios = new Audios(this);
        }
        return audios;
    }

    /**
     * Get LiveVideos instance
     *
     * @return the LiveVideos instance
     */
    public LiveVideos LiveVideos() {
        if (liveVideos == null) {
            liveVideos = new LiveVideos(this);
        }
        return liveVideos;
    }

    /**
     * Get LiveAudios instance
     *
     * @return the LiveAudios instance
     */
    public LiveAudios LiveAudios() {
        if (liveAudios == null) {
            liveAudios = new LiveAudios(this);
        }
        return liveAudios;
    }

    /**
     * Get the player identifier
     *
     * @return the player identifier
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Set a new player identifier
     *
     * @param playerId player identifier
     * @return the MediaPlayer instance
     */
    public MediaPlayer setPlayerId(int playerId) {
        this.playerId = playerId;
        return this;
    }
}
