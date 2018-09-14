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
 * Wrapper class to manage Video instances
 */
public class Videos {

    final ArrayList<Video> list = new ArrayList<>();
    private final MediaPlayer player;

    Videos(MediaPlayer player) {
        this.player = player;
    }

    /**
     * Add a new video
     *
     * @param mediaLabel video media label
     * @param duration   video duration
     * @return Video instance
     */
    public Video add(String mediaLabel, int duration) {
        int index = searchVideoIndexByMediaLabel(mediaLabel);
        Video video;
        if (index == -1) {
            video = new Video(player)
                    .setMediaLabel(mediaLabel)
                    .setDuration(duration);

            list.add(video);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.WARNING, "This Video already exists");
            video = list.get(index);
        }

        return video;
    }

    /**
     * Add a new video
     *
     * @param mediaLabel  video media label
     * @param mediaTheme1 video first media theme
     * @param duration    video duration
     * @return Video instance
     */
    public Video add(String mediaLabel, String mediaTheme1, int duration) {
        return add(mediaLabel, duration).setMediaTheme1(mediaTheme1);
    }

    /**
     * Add a new video
     *
     * @param mediaLabel  video media label
     * @param mediaTheme1 video first media theme
     * @param mediaTheme2 video second media theme
     * @param duration    video duration
     * @return Video instance
     */
    public Video add(String mediaLabel, String mediaTheme1, String mediaTheme2, int duration) {
        return add(mediaLabel, mediaTheme1, duration).setMediaTheme2(mediaTheme2);
    }

    /**
     * Add a new video
     *
     * @param mediaLabel  video media label
     * @param mediaTheme1 video first media theme
     * @param mediaTheme2 video second media theme
     * @param mediaTheme3 video third media theme
     * @param duration    video duration
     * @return Video instance
     */
    public Video add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaTheme3, int duration) {
        return add(mediaLabel, mediaTheme1, mediaTheme2, duration).setMediaTheme3(mediaTheme3);
    }

    /**
     * Remove a video
     *
     * @param mediaLabel video identified by media label
     */
    public void remove(String mediaLabel) {
        int index = searchVideoIndexByMediaLabel(mediaLabel);
        if (index > -1) {
            list.remove(index).sendStop();
        }
    }

    /**
     * Remove all videos
     */
    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0).getMediaLabel());
        }
    }

    private int searchVideoIndexByMediaLabel(String mediaLabel) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (list.get(i).getMediaLabel().equals(mediaLabel)) {
                return i;
            }
        }
        return -1;
    }
}
