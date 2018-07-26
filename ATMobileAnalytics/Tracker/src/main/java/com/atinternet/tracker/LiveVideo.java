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
 * Wrapper class for live video tracking
 */
public class LiveVideo extends RichMedia {

    LiveVideo(MediaPlayer player) {
        super(player);
        broadcastMode = BroadcastMode.Live;
        mediaType = "video";
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the LiveVideo instance
     * @deprecated please use setMediaLabel(String) instead
     */
    @Deprecated
    public LiveVideo setName(String mediaLabel) {
        return setMediaLabel(mediaLabel);
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the LiveVideo instance
     * @deprecated please use setMediaTheme1(String) instead
     */
    @Deprecated
    public LiveVideo setChapter1(String mediaTheme1) {
        return setMediaTheme1(mediaTheme1);
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the LiveVideo instance
     * @deprecated please use setMediaTheme2(String) instead
     */
    @Deprecated
    public LiveVideo setChapter2(String mediaTheme2) {
        return setMediaTheme2(mediaTheme2);
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the LiveVideo instance
     * @deprecated please use setMediaTheme3(String) instead
     */
    @Deprecated
    public LiveVideo setChapter3(String mediaTheme3) {
        return setMediaTheme3(mediaTheme3);
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the LiveVideo instance
     */
    public LiveVideo setMediaLabel(String mediaLabel) {
        this.mediaLabel = mediaLabel;
        return this;
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the LiveVideo instance
     */
    public LiveVideo setMediaTheme1(String mediaTheme1) {
        this.mediaTheme1 = mediaTheme1;
        return this;
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the LiveVideo instance
     */
    public LiveVideo setMediaTheme2(String mediaTheme2) {
        this.mediaTheme2 = mediaTheme2;
        return this;
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the LiveVideo instance
     */
    public LiveVideo setMediaTheme3(String mediaTheme3) {
        this.mediaTheme3 = mediaTheme3;
        return this;
    }

    /**
     * Set a new media level2
     *
     * @param mediaLevel2 /
     * @return LiveVideo instance
     */
    @Deprecated
    public LiveVideo setLevel2(int mediaLevel2) {
        return setMediaLevel2(mediaLevel2);
    }

    /**
     * Set a new media level2
     *
     * @param mediaLevel2 /
     * @return LiveVideo instance
     */
    public LiveVideo setMediaLevel2(int mediaLevel2) {
        this.mediaLevel2 = mediaLevel2;
        return this;
    }

    /**
     * Change boolean "isBuffering" value
     *
     * @param isBuffering /
     * @return LiveVideo instance
     */
    public LiveVideo setBuffering(boolean isBuffering) {
        this.isBuffering = isBuffering;

        return this;
    }

    /**
     * Change boolean "isEmbedded" value
     *
     * @param isEmbedded /
     * @return LiveVideo instance
     */
    public LiveVideo setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;

        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return LiveVideo instance
     */
    public LiveVideo setAction(Action action) {
        this.action = action;

        return this;
    }

    /**
     * Set a new webdomain
     *
     * @param webDomain /
     * @return LiveVideo instance
     */
    public LiveVideo setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }
}
