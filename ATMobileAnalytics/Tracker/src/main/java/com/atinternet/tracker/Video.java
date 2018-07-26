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
 * Wrapper class for video media tracking
 */
public class Video extends RichMedia {

    Video(MediaPlayer player) {
        super(player);
        broadcastMode = BroadcastMode.Clip;
        mediaType = "video";
    }

    /**
     * Set a new duration
     *
     * @param duration /
     * @return Video instance
     */
    public Video setDuration(int duration) {
        this.duration = duration;

        return this;
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the Video instance
     */
    public Video setMediaLabel(String mediaLabel) {
        this.mediaLabel = mediaLabel;
        return this;
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the Video instance
     */
    public Video setMediaTheme1(String mediaTheme1) {
        this.mediaTheme1 = mediaTheme1;
        return this;
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the Video instance
     */
    public Video setMediaTheme2(String mediaTheme2) {
        this.mediaTheme2 = mediaTheme2;
        return this;
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the Video instance
     */
    public Video setMediaTheme3(String mediaTheme3) {
        this.mediaTheme3 = mediaTheme3;
        return this;
    }

    /**
     * Set a new media level 2
     *
     * @param mediaLevel2 /
     * @return Video instance
     */
    public Video setMediaLevel2(int mediaLevel2) {
        this.mediaLevel2 = mediaLevel2;
        return this;
    }

    /**
     * Set a new webdomain
     *
     * @param webDomain /
     * @return Video instance
     */
    public Video setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }

    /**
     * Change boolean "isEmbedded" value
     *
     * @param isEmbedded /
     * @return Video instance
     */
    public Video setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;

        return this;
    }

    /**
     * Set a new linked content
     *
     * @param linkedContent /
     * @return the Video instance
     */
    public Video setLinkedContent(String linkedContent) {
        this.linkedContent = linkedContent;
        return this;
    }

    @Override
    void setParams() {
        super.setParams();

        if (duration > MAX_DURATION) {
            duration = MAX_DURATION;
        }

        tracker.setParam(Hit.HitParam.MediaDuration.stringValue(), duration);
    }

    /**
     * Change boolean "isBuffering" value
     *
     * @param isBuffering /
     * @return Video instance
     * @deprecated useless property, buffering is set when send called
     */
    @Deprecated
    public Video setBuffering(boolean isBuffering) {
        return this;
    }

    /**
     * Set a new action
     *
     * @param action /
     * @return Video instance
     * @deprecated useless property, action is set when send called
     */
    @Deprecated
    public Video setAction(Action action) {
        return this;
    }

    /**
     * Set a new level 2
     *
     * @param mediaLevel2 /
     * @return Video instance
     * @deprecated please use setMediaLevel2(int) instead
     */
    @Deprecated
    public Video setLevel2(int mediaLevel2) {
        return setMediaLevel2(mediaLevel2);
    }

    /**
     * Set a new media label
     *
     * @param mediaLabel /
     * @return the Video instance
     * @deprecated please use setMediaLabel(String) instead
     */
    @Deprecated
    public Video setName(String mediaLabel) {
        return setMediaLabel(mediaLabel);
    }

    /**
     * Set a new first media theme
     *
     * @param mediaTheme1 /
     * @return the Video instance
     * @deprecated please use setMediaTheme1(String) instead
     */
    @Deprecated
    public Video setChapter1(String mediaTheme1) {
        return setMediaTheme1(mediaTheme1);
    }

    /**
     * Set a new second media theme
     *
     * @param mediaTheme2 /
     * @return the Video instance
     * @deprecated please use setMediaTheme2(String) instead
     */
    @Deprecated
    public Video setChapter2(String mediaTheme2) {
        return setMediaTheme2(mediaTheme2);
    }

    /**
     * Set a new third media theme
     *
     * @param mediaTheme3 /
     * @return the Video instance
     * @deprecated please use setMediaTheme3(String) instead
     */
    @Deprecated
    public Video setChapter3(String mediaTheme3) {
        return setMediaTheme3(mediaTheme3);
    }
}
