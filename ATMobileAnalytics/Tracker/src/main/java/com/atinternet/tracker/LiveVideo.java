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

public class LiveVideo extends RichMedia {

    public LiveVideo setName(String name) {
        this.name = name;
        return this;
    }

    public LiveVideo setLevel2(int level2) {
        this.level2 = level2;

        return this;
    }

    public LiveVideo setBuffering(boolean isBuffering) {
        this.isBuffering = isBuffering;

        return this;
    }

    public LiveVideo setEmbedded(boolean isEmbedded) {
        this.isEmbedded = isEmbedded;

        return this;
    }

    LiveVideo setBroadcastMode(BroadcastMode broadcastMode) {
        this.broadcastMode = broadcastMode;

        return this;
    }

    public LiveVideo setChapter1(String chapter1) {
        this.chapter1 = chapter1;
        return this;
    }

    public LiveVideo setChapter2(String chapter2) {
        this.chapter2 = chapter2;
        return this;
    }

    public LiveVideo setChapter3(String chapter3) {
        this.chapter3 = chapter3;
        return this;
    }

    public LiveVideo setAction(Action action) {
        this.action = action;

        return this;
    }

    public LiveVideo setWebDomain(String webDomain) {
        this.webDomain = webDomain;
        return this;
    }

    LiveVideo(MediaPlayer player) {
        super(player);
        broadcastMode = BroadcastMode.Live;
        type = "video";
    }
}
