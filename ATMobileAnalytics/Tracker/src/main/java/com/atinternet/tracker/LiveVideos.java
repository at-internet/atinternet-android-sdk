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
     * @param mediaLabel live video media label
     * @return LiveVideo instance
     */
    public LiveVideo add(String mediaLabel) {
        int index = searchLiveVideoIndexByMediaLabel(mediaLabel);
        LiveVideo liveVideo;
        if (index == -1) {
            liveVideo = new LiveVideo(player)
                    .setMediaLabel(mediaLabel);

            list.add(liveVideo);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.WARNING, "This LiveVideo already exists");
            liveVideo = list.get(index);
        }

        return liveVideo;
    }

    /**
     * Create new video
     *
     * @param mediaLabel  live video media label
     * @param mediaTheme1 live video first media theme
     * @return LiveVideo instance
     */
    public LiveVideo add(String mediaLabel, String mediaTheme1) {
        return add(mediaLabel).setMediaTheme1(mediaTheme1);
    }

    /**
     * Create new video
     *
     * @param mediaLabel  live video media label
     * @param mediaTheme1 live video first media theme
     * @param mediaTheme2 live video second media theme
     * @return LiveVideo instance
     */
    public LiveVideo add(String mediaLabel, String mediaTheme1, String mediaTheme2) {
        return add(mediaLabel, mediaTheme1).setMediaTheme2(mediaTheme2);
    }

    /**
     * Create new video
     *
     * @param mediaLabel  live video media label
     * @param mediaTheme1 live video first media theme
     * @param mediaTheme2 live video second media theme
     * @param mediaTheme3 live video third media theme
     * @return LiveVideo instance
     */
    public LiveVideo add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaTheme3) {
        return add(mediaLabel, mediaTheme1, mediaTheme2).setMediaTheme3(mediaTheme3);
    }

    /**
     * Remove a live video
     *
     * @param mediaLabel live video media label
     */
    public void remove(String mediaLabel) {
        int index = searchLiveVideoIndexByMediaLabel(mediaLabel);
        if (index > -1) {
            if (list.get(index).scheduler != null && !list.get(index).scheduler.isShutdown()) {
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
            remove(list.get(0).getMediaLabel());
        }
    }

    private int searchLiveVideoIndexByMediaLabel(String mediaLabel) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (list.get(i).getMediaLabel().equals(mediaLabel)) {
                return i;
            }
        }
        return -1;
    }
}
