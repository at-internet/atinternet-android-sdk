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
 * Wrapper class for live audio tracking
 */
public class LiveAudio extends RichMedia {

    LiveAudio(MediaPlayer player) {
        super(player);
        broadcastMode = BroadcastMode.Live;
        mediaType = "audio";
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the LiveAudio instance
     */
    public LiveAudio setMediaLabel(String mediaLabel) {
        this.mediaLabel = mediaLabel;
        return this;
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the LiveAudio instance
     */
    public LiveAudio setMediaTheme1(String mediaTheme1) {
        this.mediaTheme1 = mediaTheme1;
        return this;
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the LiveAudio instance
     */
    public LiveAudio setMediaTheme2(String mediaTheme2) {
        this.mediaTheme2 = mediaTheme2;
        return this;
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the LiveAudio instance
     */
    public LiveAudio setMediaTheme3(String mediaTheme3) {
        this.mediaTheme3 = mediaTheme3;
        return this;
    }

    /**
     * Set a new media level2
     *
     * @param mediaLevel2 /
     * @return LiveAudio instance
     */
    public LiveAudio setMediaLevel2(int mediaLevel2) {
        this.mediaLevel2 = mediaLevel2;
        return this;
    }

    /**
     * Change boolean "isEmbedded" value
     *
     * @param isEmbedded /
     * @return LiveAudio instance
     */
    public LiveAudio setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;

        return this;
    }

    /**
     * Set a new webdomain
     *
     * @param webDomain /
     * @return LiveAudio instance
     */
    public LiveAudio setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }

    /**
     * Set a new linked content
     *
     * @param linkedContent /
     * @return the LiveAudio instance
     */
    public LiveAudio setLinkedContent(String linkedContent) {
        this.linkedContent = linkedContent;
        return this;
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the LiveAudio instance
     * @deprecated please use setMediaLabel(String) instead
     */
    @Deprecated
    public LiveAudio setName(String mediaLabel) {
        return setMediaLabel(mediaLabel);
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the LiveAudio instance
     * @deprecated please use setTheme1(String) instead
     */
    @Deprecated
    public LiveAudio setChapter1(String mediaTheme1) {
        return setMediaTheme1(mediaTheme1);
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the LiveAudio instance
     * @deprecated please use setTheme2(String) instead
     */
    @Deprecated
    public LiveAudio setChapter2(String mediaTheme2) {
        return setMediaTheme2(mediaTheme2);
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the LiveAudio instance
     * @deprecated please use setTheme3(String) instead
     */
    @Deprecated
    public LiveAudio setChapter3(String mediaTheme3) {
        return setMediaTheme3(mediaTheme3);
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return LiveAudio instance
     * @deprecated useless property, action is set when send called
     */
    @Deprecated
    public LiveAudio setAction(Action action) {
        return this;
    }

    /**
     * Change boolean "isBuffering" value
     *
     * @param isBuffering /
     * @return LiveAudio instance
     * @deprecated useless property, buffering is set when send called
     */
    @Deprecated
    public LiveAudio setBuffering(boolean isBuffering) {
        return this;
    }

    /**
     * Set a new level2
     *
     * @param mediaLevel2 /
     * @return LiveAudio instance
     * @deprecated please use setMediaLevel2(int) instead
     */
    @Deprecated
    public LiveAudio setLevel2(int mediaLevel2) {
        return setMediaLevel2(mediaLevel2);
    }
}
