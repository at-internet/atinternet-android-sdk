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

import java.util.TreeMap;

/**
 * Wrapper class to manage MediaPlayer instances
 */
public class MediaPlayers extends Helper {

    final TreeMap<Integer, MediaPlayer> players;

    MediaPlayers(Tracker tracker) {
        super(tracker);
        players = new TreeMap<>();
    }

    /**
     * Add a player
     *
     * @return a new MediaPlayer instance
     */
    public MediaPlayer add() {
        int id = 0;
        do {
            id++;
        } while (players.get(id) != null);
        return add(id);
    }

    /**
     * Add a player
     *
     * @param playerId player identifier
     * @return a new MediaPlayer instance
     */
    public MediaPlayer add(int playerId) {
        if (players.get(playerId) == null) {
            MediaPlayer player = new MediaPlayer(tracker).setPlayerId(playerId);

            players.put(player.getPlayerId(), player);

            return player;
        } else {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.warning, "Player with the same id already exists");
            return players.get(playerId);
        }
    }

    /**
     * Remove a MediaPlayer with all media attached
     *
     * @param playerId player identifier
     */
    public void remove(int playerId) {
        MediaPlayer player = players.remove(playerId);
        if (player != null) {
            for (Video video : player.Videos().list) {
                if (video.executor != null && !video.executor.isShutdown()) {
                    video.sendStop();
                }
            }
            player.Videos().removeAll();
            for (LiveVideo liveVideo : player.LiveVideos().list) {
                if (liveVideo.executor != null && !liveVideo.executor.isShutdown()) {
                    liveVideo.sendStop();
                }
            }
            player.LiveVideos().removeAll();
            for (Audio audio : player.Audios().list) {
                if (audio.executor != null && !audio.executor.isShutdown()) {
                    audio.sendStop();
                }
            }
            player.Audios().removeAll();
            for (LiveAudio liveAudio : player.LiveAudios().list) {
                if (liveAudio.executor != null && !liveAudio.executor.isShutdown()) {
                    liveAudio.sendStop();
                }
            }
            player.LiveAudios().removeAll();
        }
    }

    /**
     * Remove all players
     */
    public void removeAll() {
        while (!players.isEmpty()) {
            remove(players.firstEntry().getValue().getPlayerId());
        }
    }
}
