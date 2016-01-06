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

import android.app.Activity;
import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.atinternet.tracker.TechnicalContext.ConnectionType;
import static com.atinternet.tracker.TechnicalContext.getConnection;
import static com.atinternet.tracker.Tool.CallbackType;

class Sender implements Runnable {

    /**
     * Constant number of retry
     */
    private static final int RETRY_COUNT = 3;

    /**
     * Constant Timeout
     */
    private static final int TIMEOUT = 15000;

    /**
     * Constant name error
     */
    private static final String RECEIVE_FROM_SERVER_ERROR = "recvfrom";

    /**
     * Instance Tracker Listener for callbacks
     */
    private final TrackerListener trackerListener;

    /**
     * Instance Storage
     */
    private final Storage storage;

    /**
     * Hit
     */
    private final Hit hit;

    /**
     * Olt Param built by the builder
     */
    private final String oltParameter;

    /**
     * Offline hit processing boolean
     */
    private static boolean OfflineHitProcessing = false;

    /**
     * Boolean to know if this was created from tracker
     */
    private final boolean forceSendOfflineHits;

    /**
     * Init a sender
     *
     * @param trackerListener      TrackerListener
     * @param forceSendOfflineHits boolean
     * @param hit                  Hit
     */
    Sender(TrackerListener trackerListener, Hit hit, boolean forceSendOfflineHits, String... oltParameter) {
        this.trackerListener = trackerListener;
        this.storage = Tracker.getStorage();
        this.hit = hit;
        this.forceSendOfflineHits = forceSendOfflineHits;
        this.oltParameter = oltParameter.length > 0 ? oltParameter[0] : "";
    }

    /**
     * Send the hit
     */
    private void send(final Hit hit) {

        if (storage.getOfflineMode() == Storage.OfflineMode.always && !forceSendOfflineHits) {
            saveHitDatabase(hit);
        }
        // Si pas de connexion
        else if (getConnection() == ConnectionType.offline || (!hit.isOffline() && storage.getCountOfflineHits() > 0)) {
            // Si le hit ne provient pas du offline
            if (storage.getOfflineMode() != Storage.OfflineMode.never && !hit.isOffline()) {
                saveHitDatabase(hit);
            }
        } else {
            HttpURLConnection connection = null;
            try {
                // Execution de la requête
                URL url = new URL(hit.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(TIMEOUT);
                connection.setConnectTimeout(TIMEOUT);
                connection.connect();

                int statusCode = connection.getResponseCode();
                final String message = connection.getResponseMessage();

                // Le hit n'a pas pu être envoyé
                if (statusCode != 200) {
                    if (storage.getOfflineMode() != Storage.OfflineMode.never) {
                        if (!hit.isOffline()) {
                            saveHitDatabase(hit);
                        } else {
                            updateRetryCount(hit);
                        }
                    }
                    Tool.executeCallback(trackerListener, CallbackType.send, message, TrackerListener.HitStatus.Failed);
                    updateDebugger(message, "error48", false);
                }
                // Le hit a été envoyé
                else {
                    // Si le hit provient du stockage, on le supprime de la base
                    if (hit.isOffline()) {
                        storage.deleteHit(hit.getUrl());
                    }
                    Tool.executeCallback(trackerListener, CallbackType.send, String.valueOf(statusCode), TrackerListener.HitStatus.Success);
                    updateDebugger(hit.getUrl(), "sent48", true);
                }
            } catch (final Exception e) {
                updateDebugger(e.getMessage(), "error48", false);
                // Si une erreur est survenue au moment de la récupération du pixel de marquage mais que le hit est bien envoyé
                if (checkExceptionServerReceiveData(e)) {
                    // Si il s'agissait d'un hit offline, on le supprime
                    if (hit.isOffline()) {
                        storage.deleteHit(hit.getUrl());
                    }
                } else {
                    if (storage.getOfflineMode() != Storage.OfflineMode.never) {
                        if (!hit.isOffline()) {
                            saveHitDatabase(hit);
                        } else {
                            updateRetryCount(hit);
                        }
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    @Override
    public void run() {
        send(false);
    }

    /**
     * Send hit
     *
     * @param includeOfflineHits boolean
     */
    void send(boolean includeOfflineHits) {
        if (includeOfflineHits) {
            Sender.sendOfflineHits(trackerListener, storage, false, forceSendOfflineHits);
        }
        send(hit);
    }

    /**
     * Send offline hits
     *
     * @param listener             TrackerListener
     * @param storage              Storage
     * @param async                boolean
     * @param forceSendOfflineHits boolean
     */
    public static void sendOfflineHits(TrackerListener listener, Storage storage, boolean forceSendOfflineHits, boolean async) {
        if ((storage.getOfflineMode() != Storage.OfflineMode.always || forceSendOfflineHits) && TechnicalContext.getConnection() != ConnectionType.offline && !OfflineHitProcessing) {

            if (TrackerQueue.getEnabledFillQueueFromDatabase() && storage.getCountOfflineHits() > 0) {
                ArrayList<Hit> offlineHits = storage.getOfflineHits();
                if (async) {
                    TrackerQueue.setEnabledFillQueueFromDatabase(false);
                    for (Hit hit : offlineHits) {
                        Sender sender = new Sender(listener, hit, forceSendOfflineHits);
                        TrackerQueue.getInstance().put(sender);
                    }
                    TrackerQueue.getInstance().put(new Runnable() {
                        @Override
                        public void run() {
                            TrackerQueue.setEnabledFillQueueFromDatabase(true);
                        }
                    });
                } else {
                    OfflineHitProcessing = true;
                    for (Hit hit : offlineHits) {
                        Sender sender = new Sender(listener, hit, forceSendOfflineHits);
                        sender.send(false);
                    }
                    OfflineHitProcessing = false;
                }
            }
        }
    }

    /**
     * Update retry
     */
    private void updateRetryCount(Hit hit) {
        int retryCount = hit.getRetry();
        if (retryCount < RETRY_COUNT) {
            storage.updateRetry(hit.getUrl(), retryCount + 1);
        } else {
            storage.deleteHit(hit.getUrl());
        }
    }

    /**
     * Store the hit into Database
     */
    void saveHitDatabase(final Hit hit) {
        final String url = storage.saveHit(hit.getUrl(), System.currentTimeMillis(), oltParameter);
        if (!TextUtils.isEmpty(url)) {
            Tool.executeCallback(trackerListener, CallbackType.save, hit.getUrl());
            updateDebugger(url, "save48", true);
        } else {
            Tool.executeCallback(trackerListener, CallbackType.warning, "Hit could not be saved : " + hit.getUrl());
            updateDebugger("Hit could not be saved : " + hit.getUrl(), "warning48", false);
        }
    }

    /**
     * Check if request sent but the device not receive pixel data
     *
     * @param e Exception
     * @return boolean
     */
    private boolean checkExceptionServerReceiveData(Exception e) {
        int index = 0;
        do {
            if (e.getStackTrace()[index].getMethodName().equals(RECEIVE_FROM_SERVER_ERROR)) {
                return true;
            }
            index++;
        }
        while (index < e.getStackTrace().length);
        return false;
    }

    /**
     * Update Debugger from a new event
     *
     * @param message String
     * @param type    String
     * @param isHit   boolean
     */
    private void updateDebugger(final String message, final String type, final boolean isHit) {
        if (Debugger.getContext() != null) {
            ((Activity) Debugger.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Debugger.getDebuggerEvents().add(0, new Debugger.DebuggerEvent(message, type, isHit));
                    if (Debugger.currentViewVisibleId == R.id.eventViewer) {
                        Debugger.getDebuggerEventListAdapter().notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
