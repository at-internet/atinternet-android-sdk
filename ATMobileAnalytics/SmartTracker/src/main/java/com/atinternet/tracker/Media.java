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
 * Wrapper class to Medium instances
 */
public class Media {

    final ArrayList<Medium> list = new ArrayList<>();
    private final MediaPlayer player;

    Media(MediaPlayer player) {
        this.player = player;
    }

    /**
     * Create new Medium
     *
     * @param mediaLabel media label
     * @param mediaType  media type
     * @param duration   media duration
     * @return Medium instance
     */
    public Medium add(String mediaLabel, String mediaType, int duration) {
        int index = searchMediumIndexByMediaLabel(mediaLabel);
        Medium medium;
        if (index == -1) {
            medium = new Medium(player)
                    .setMediaLabel(mediaLabel)
                    .setMediaType(mediaType)
                    .setDuration(duration);

            list.add(medium);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.WARNING, "This medium already exists");
            medium = list.get(index);
        }

        return medium;
    }

    /**
     * Create new Medium
     *
     * @param mediaLabel  media label
     * @param mediaTheme1 first media theme
     * @param mediaType   media type
     * @param duration    media duration
     * @return Medium instance
     */
    public Medium add(String mediaLabel, String mediaTheme1, String mediaType, int duration) {
        return add(mediaLabel, mediaType, duration).setMediaTheme1(mediaTheme1);
    }

    /**
     * Create new Medium
     *
     * @param mediaLabel  media label
     * @param mediaTheme1 first media theme
     * @param mediaTheme2 second media theme
     * @param mediaType   media type
     * @param duration    media duration
     * @return Medium instance
     */
    public Medium add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaType, int duration) {
        return add(mediaLabel, mediaTheme1, mediaType, duration).setMediaTheme2(mediaTheme2);
    }

    /**
     * Create new Medium
     *
     * @param mediaLabel  media label
     * @param mediaTheme1 first media theme
     * @param mediaTheme2 second media theme
     * @param mediaTheme3 third media theme
     * @param mediaType   media type
     * @param duration    media duration
     * @return Medium instance
     */
    public Medium add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaTheme3, String mediaType, int duration) {
        return add(mediaLabel, mediaTheme1, mediaTheme2, mediaType, duration).setMediaTheme3(mediaTheme3);
    }

    /**
     * Remove a medium
     *
     * @param mediaLabel medium label
     */
    public void remove(String mediaLabel) {
        int index = searchMediumIndexByMediaLabel(mediaLabel);
        if (index > -1) {
            list.remove(index).sendStop();
        }
    }

    /**
     * Remove all media
     */
    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0).getMediaLabel());
        }
    }

    private int searchMediumIndexByMediaLabel(String mediaLabel) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (list.get(i).getMediaLabel().equals(mediaLabel)) {
                return i;
            }
        }
        return -1;
    }
}
