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

import java.util.ArrayList;

/**
 * Wrapper class to LiveVideo instances
 */
public class LiveVideos {

    final ArrayList<LiveVideo> list = new ArrayList<>();
    private final MediaPlayer player;

    LiveVideos(MediaPlayer player) {
        this.player = player;
    }

    /**
     * Create new video
     *
     * @param name live video name
     * @return LiveVideo instance
     */
    public LiveVideo add(String name) {
        int index = -1;
        int length = list.size();
        LiveVideo liveVideo;
        for (int i = 0; i < length; i++) {
            if (list.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            liveVideo = new LiveVideo(player)
                    .setName(name);

            list.add(liveVideo);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.warning, "LiveVideo with the same name already exists");
            liveVideo = list.get(index);
        }

        return liveVideo;
    }

    /**
     * Create new video
     *
     * @param name     live video name
     * @param chapter1 live video first chapter
     * @return LiveVideo instance
     */
    public LiveVideo add(String name, String chapter1) {
        return add(name).setChapter1(chapter1);
    }

    /**
     * Create new video
     *
     * @param name     live video name
     * @param chapter1 live video first chapter
     * @param chapter2 live video second chapter
     * @return LiveVideo instance
     */
    public LiveVideo add(String name, String chapter1, String chapter2) {
        return add(name, chapter1).setChapter2(chapter2);
    }

    /**
     * Create new video
     *
     * @param name     live video name
     * @param chapter1 live video first chapter
     * @param chapter2 live video second chapter
     * @param chapter3 live video third chapter
     * @return LiveVideo instance
     */
    public LiveVideo add(String name, String chapter1, String chapter2, String chapter3) {
        return add(name, chapter1, chapter2).setChapter3(chapter3);
    }

    /**
     * Remove a live video
     *
     * @param name live video name
     */
    public void remove(String name) {
        int length = list.size();
        int index = -1;

        for (int i = 0; i < length; i++) {
            if (list.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            if (list.get(index).executor != null && !list.get(index).executor.isShutdown()) {
                list.get(index).sendStop();
            }
            list.remove(index);
        }
    }

    /**
     * Remove all live videos
     */
    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0).getName());
        }
    }
}
