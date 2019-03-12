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

import java.util.ArrayList;

/**
 * Wrapper class to LiveMedium instances
 */
public class LiveMedia {

    final ArrayList<LiveMedium> list = new ArrayList<>();
    private final MediaPlayer player;

    LiveMedia(MediaPlayer player) {
        this.player = player;
    }

    /**
     * Create new LiveMedium
     *
     * @param mediaLabel live media label
     * @param mediaType  live media type
     * @return LiveMedium instance
     */
    public LiveMedium add(String mediaLabel, String mediaType) {
        int index = searchLiveMediumIndexByMediaLabel(mediaLabel);
        LiveMedium liveMedium;
        if (index == -1) {
            liveMedium = new LiveMedium(player)
                    .setMediaLabel(mediaLabel)
                    .setMediaType(mediaType);

            list.add(liveMedium);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.WARNING, "This live medium already exists");
            liveMedium = list.get(index);
        }

        return liveMedium;
    }

    /**
     * Create new LiveMedium
     *
     * @param mediaLabel  live media label
     * @param mediaTheme1 live media first media theme
     * @param mediaType   live media type
     * @return LiveMedium instance
     */
    public LiveMedium add(String mediaLabel, String mediaTheme1, String mediaType) {
        return add(mediaLabel, mediaType).setMediaTheme1(mediaTheme1);
    }

    /**
     * Create new LiveMedium
     *
     * @param mediaLabel  live media media label
     * @param mediaTheme1 live media first media theme
     * @param mediaTheme2 live media second media theme
     * @param mediaType   live media type
     * @return LiveMedium instance
     */
    public LiveMedium add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaType) {
        return add(mediaLabel, mediaType, mediaTheme1).setMediaTheme2(mediaTheme2);
    }

    /**
     * Create new LiveMedium
     *
     * @param mediaLabel  live media label
     * @param mediaTheme1 live first media theme
     * @param mediaTheme2 live second media theme
     * @param mediaTheme3 live third media theme
     * @param mediaType   live media type
     * @return LiveMedium instance
     */
    public LiveMedium add(String mediaLabel, String mediaTheme1, String mediaTheme2, String mediaTheme3, String mediaType) {
        return add(mediaLabel, mediaType, mediaTheme1, mediaTheme2).setMediaTheme3(mediaTheme3);
    }

    /**
     * Remove a live medium
     *
     * @param mediaLabel live medium label
     */
    public void remove(String mediaLabel) {
        int index = searchLiveMediumIndexByMediaLabel(mediaLabel);
        if (index > -1) {
            list.remove(index).sendStop();
        }
    }

    /**
     * Remove all live media
     */
    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0).getMediaLabel());
        }
    }

    private int searchLiveMediumIndexByMediaLabel(String mediaLabel) {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            if (list.get(i).getMediaLabel().equals(mediaLabel)) {
                return i;
            }
        }
        return -1;
    }
}
