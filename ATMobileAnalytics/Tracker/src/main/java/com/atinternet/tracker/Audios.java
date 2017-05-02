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
 * Wrapper class to manage Audio instances
 */
public class Audios {

    private final MediaPlayer player;
    final ArrayList<Audio> list = new ArrayList<>();

    Audios(MediaPlayer player) {
        this.player = player;
    }

    /**
     * Add a new audio
     *
     * @param name     audio name
     * @param duration audio duration
     * @return the Audio instance
     */
    public Audio add(String name, int duration) {
        int index = -1;
        int length = list.size();
        Audio audio;
        for (int i = 0; i < length; i++) {
            if (list.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            audio = new Audio(player)
                    .setName(name)
                    .setDuration(duration);

            list.add(audio);
        } else {
            Tool.executeCallback(player.getTracker().getListener(), Tool.CallbackType.warning, "Audio with the same name already exists");
            audio = list.get(index);
        }

        return audio;
    }

    /**
     * Add a new audio
     *
     * @param name     audio name
     * @param chapter1 audio first chapter
     * @param duration audio duration
     * @return the Audio instance
     */
    public Audio add(String name, String chapter1, int duration) {
        return add(name, duration).setChapter1(chapter1);
    }

    /**
     * Add a new audio
     *
     * @param name     audio name
     * @param chapter1 audio first chapter
     * @param chapter2 audio second chapter
     * @param duration audio duration
     * @return the Audio instance
     */
    public Audio add(String name, String chapter1, String chapter2, int duration) {
        return add(name, chapter1, duration).setChapter2(chapter2);
    }

    /**
     * Add a new audio
     *
     * @param name     audio name
     * @param chapter1 audio first chapter
     * @param chapter2 audio second chapter
     * @param chapter3 audio third chapter
     * @param duration audio duration
     * @return the Audio instance
     */
    public Audio add(String name, String chapter1, String chapter2, String chapter3, int duration) {
        return add(name, chapter1, chapter2, duration).setChapter3(chapter3);
    }

    /**
     * Remove an audio
     *
     * @param name audio identified by name
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
     * Remove all audios
     */
    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0).getName());
        }
    }
}
