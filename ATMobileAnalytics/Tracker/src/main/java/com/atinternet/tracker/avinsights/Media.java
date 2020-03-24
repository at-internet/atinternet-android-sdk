package com.atinternet.tracker.avinsights;

import android.util.SparseIntArray;

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Events;
import com.atinternet.tracker.RequiredPropertiesDataObject;
import com.atinternet.tracker.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Media extends RequiredPropertiesDataObject {

    private final int MIN_HEARTBEAT_DURATION = 5;
    private final int MIN_BUFFER_HEARTBEAT_DURATION = 1;

    private final Events events;

    private ScheduledExecutorService heartbeatExecutor;
    private final SparseIntArray heartbeatDurations;
    private final SparseIntArray bufferHeartbeatDurations;
    private final AVRunnable heartbeatRunnable;
    private final AVRunnable bufferHeartbeatRunnable;
    private final AVRunnable rebufferHeartbeatRunnable;

    private String sessionId = UUID.randomUUID().toString();
    private String previousEvent = "";
    private int previousCursorPositionMillis = 0;
    private int currentCursorPositionMillis = 0;
    private long eventDurationMillis = 0;
    private int sessionDurationMillis = 0;
    private long startSessionTimeMillis = 0;
    private long bufferTimeMillis = 0;
    private boolean isPlaying = false;
    private boolean isPlaybackActivated = false;

    private boolean autoHeartbeat;
    private boolean autoBufferHeartbeat;

    public Media(Events events) {
        super();
        this.events = events;
        heartbeatDurations = new SparseIntArray();
        bufferHeartbeatDurations = new SparseIntArray();

        heartbeatRunnable = new HeartbeatRunnable(this);
        bufferHeartbeatRunnable = new BufferHeartbeatRunnable(this);
        rebufferHeartbeatRunnable = new RebufferHeartbeatRunnable(this);
    }

    public Media(Events events, int heartbeat, int bufferHeartbeat) {
        this(events);
        setHeartbeat(heartbeat);
        setBufferHeartbeat(bufferHeartbeat);
    }

    public Media(Events events, SparseIntArray heartbeat, SparseIntArray bufferHeartbeat) {
        this(events);
        setHeartbeat(heartbeat);
        setBufferHeartbeat(bufferHeartbeat);
    }

    /***
     * Set heartbeat value
     * @param heartbeat int
     * @return current Media instance
     */
    Media setHeartbeat(int heartbeat) {
        if (heartbeat <= 0) {
            return this;
        }
        SparseIntArray sia = new SparseIntArray();
        sia.append(0, heartbeat);
        return setHeartbeat(sia);
    }

    /***
     * Set heartbeat value
     * @param heartbeat SparseIntArray
     * @return current Media instance
     */
    Media setHeartbeat(SparseIntArray heartbeat) {
        if (heartbeat == null) {
            return this;
        }
        int size = heartbeat.size();
        if (size == 0) {
            return this;
        }
        autoHeartbeat = true;
        heartbeatDurations.clear();
        for (int i = 0; i < size; i++) {
            int value = heartbeat.valueAt(i);
            if (value < MIN_HEARTBEAT_DURATION) {
                heartbeatDurations.put(heartbeat.keyAt(i), MIN_HEARTBEAT_DURATION);
            } else {
                heartbeatDurations.put(heartbeat.keyAt(i), value);
            }
        }
        if (heartbeatDurations.indexOfKey(0) < 0) {
            heartbeatDurations.put(0, MIN_HEARTBEAT_DURATION);
        }
        return this;
    }

    /***
     * Set buffer heartbeat value
     * @param bufferHeartbeat int
     * @return current Media instance
     */
    Media setBufferHeartbeat(int bufferHeartbeat) {
        if (bufferHeartbeat <= 0) {
            return this;
        }
        SparseIntArray sia = new SparseIntArray();
        sia.append(0, bufferHeartbeat);
        return setBufferHeartbeat(sia);
    }

    /***
     * Set buffer heartbeat value
     * @param bufferHeartbeat SparseIntArray
     * @return current Media instance
     */
    Media setBufferHeartbeat(SparseIntArray bufferHeartbeat) {
        if (bufferHeartbeat == null) {
            return this;
        }
        int size = bufferHeartbeat.size();
        if (size == 0) {
            return this;
        }
        autoBufferHeartbeat = true;
        bufferHeartbeatDurations.clear();
        for (int i = 0; i < size; i++) {
            int value = bufferHeartbeat.valueAt(i);
            if (value < MIN_BUFFER_HEARTBEAT_DURATION) {
                bufferHeartbeatDurations.put(bufferHeartbeat.keyAt(i), MIN_BUFFER_HEARTBEAT_DURATION);
            } else {
                bufferHeartbeatDurations.put(bufferHeartbeat.keyAt(i), value);
            }
        }
        if (bufferHeartbeatDurations.indexOfKey(0) < 0) {
            bufferHeartbeatDurations.put(0, MIN_BUFFER_HEARTBEAT_DURATION);
        }
        return this;
    }

    public synchronized void track(String event, Map<String, Object> options, Map<String, Object> extraProps) {
        if (options == null) {
            options = new HashMap<>();
        }

        switch (event) {
            case "av.heartbeat":
                heartbeat(extraProps);
                break;
            case "av.buffer.heartbeat":
                bufferHeartbeat(extraProps);
                break;
            case "av.rebuffer.heartbeat":
                rebufferHeartbeat(extraProps);
                break;
            case "av.play":
                play(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.buffer.start":
                bufferStart(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.start":
                playbackStart(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.resume":
                playbackResumed(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.pause":
                playbackPaused(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.stop":
                playbackStopped(Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.backward":
                seekBackward(Utility.parseInt(options.get("av_previous_position"), 0), Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.forward":
                seekForward(Utility.parseInt(options.get("av_previous_position"), 0), Utility.parseInt(options.get("av_position"), 0), extraProps);
                break;
            case "av.seek.start":
                seekStart(Utility.parseInt(options.get("av_previous_position"), 0), extraProps);
                break;
            case "av.error":
                error(Utility.parseString(options.get("av_player_error")), extraProps);
                break;
            default:
                sendEvents(createEvent(event, false, extraProps));
        }
    }

    /***
     * Generate heartbeat event.
     */
    public synchronized void heartbeat(Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis += eventDurationMillis;

        sendEvents(createEvent("av.heartbeat", true, extraProps));
    }

    synchronized void processAutoHeartbeat() {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis += eventDurationMillis;

        if (autoHeartbeat) {
            int diffMin = (int) ((System.currentTimeMillis() - startSessionTimeMillis) / 60000);
            heartbeatExecutor.schedule(heartbeatRunnable, heartbeatDurations.get(diffMin, MIN_HEARTBEAT_DURATION), TimeUnit.SECONDS);
        }

        sendEvents(createEvent("av.heartbeat", true, null));
    }

    /***
     * Generate heartbeat event during buffering.
     */
    public synchronized void bufferHeartbeat(Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        sendEvents(createEvent("av.buffer.heartbeat", true, extraProps));
    }

    synchronized void processAutoBufferHeartbeat() {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        if (autoBufferHeartbeat) {
            bufferTimeMillis = bufferTimeMillis == 0 ? System.currentTimeMillis() : bufferTimeMillis;
            int diffMin = (int) ((System.currentTimeMillis() - bufferTimeMillis) / 60000);
            heartbeatExecutor.schedule(bufferHeartbeatRunnable, bufferHeartbeatDurations.get(diffMin, MIN_BUFFER_HEARTBEAT_DURATION), TimeUnit.SECONDS);
        }
        sendEvents(createEvent("av.buffer.heartbeat", true, null));
    }

    /***
     * Generate heartbeat event during rebuffering.
     */
    public synchronized void rebufferHeartbeat(Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;

        sendEvents(createEvent("av.rebuffer.heartbeat", true, extraProps));
    }

    synchronized void processAutoRebufferHeartbeat() {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;

        if (autoBufferHeartbeat) {
            bufferTimeMillis = bufferTimeMillis == 0 ? System.currentTimeMillis() : bufferTimeMillis;
            int diffMin = (int) ((System.currentTimeMillis() - bufferTimeMillis) / 60000);
            heartbeatExecutor.schedule(rebufferHeartbeatRunnable, bufferHeartbeatDurations.get(diffMin, MIN_BUFFER_HEARTBEAT_DURATION), TimeUnit.SECONDS);
        }

        sendEvents(createEvent("av.rebuffer.heartbeat", true, null));
    }

    /***
     * Generate play event (play attempt).
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void play(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        eventDurationMillis = 0;

        previousCursorPositionMillis = cursorPosition;
        currentCursorPositionMillis = cursorPosition;

        bufferTimeMillis = 0;
        isPlaying = false;
        isPlaybackActivated = false;

        stopHeartbeatService();

        sendEvents(createEvent("av.play", true, extraProps));
    }

    /***
     * Player buffering start to initiate the launch of the media.
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void bufferStart(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();
        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis = cursorPosition;

        stopHeartbeatService();

        if (isPlaybackActivated) {
            if (autoBufferHeartbeat) {
                bufferTimeMillis = bufferTimeMillis == 0 ? System.currentTimeMillis() : bufferTimeMillis;
                int diffMin = (int) ((System.currentTimeMillis() - bufferTimeMillis) / 60000);
                heartbeatExecutor.schedule(rebufferHeartbeatRunnable, bufferHeartbeatDurations.get(diffMin, MIN_BUFFER_HEARTBEAT_DURATION), TimeUnit.SECONDS);
            }
            sendEvents(createEvent("av.rebuffer.start", true, extraProps));
        } else {
            if (autoBufferHeartbeat) {
                bufferTimeMillis = bufferTimeMillis == 0 ? System.currentTimeMillis() : bufferTimeMillis;
                int diffMin = (int) ((System.currentTimeMillis() - bufferTimeMillis) / 60000);
                heartbeatExecutor.schedule(bufferHeartbeatRunnable, bufferHeartbeatDurations.get(diffMin, MIN_BUFFER_HEARTBEAT_DURATION), TimeUnit.SECONDS);
            }
            sendEvents(createEvent("av.buffer.start", true, extraProps));
        }
    }

    /***
     * Media playback start (first frame of the media).
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void playbackStart(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = cursorPosition;
        currentCursorPositionMillis = cursorPosition;
        bufferTimeMillis = 0;
        isPlaying = true;
        isPlaybackActivated = true;

        stopHeartbeatService();
        if (autoHeartbeat) {
            int diffMin = (int) ((System.currentTimeMillis() - startSessionTimeMillis) / 60000);
            heartbeatExecutor.schedule(heartbeatRunnable, heartbeatDurations.get(diffMin, MIN_HEARTBEAT_DURATION), TimeUnit.SECONDS);
        }

        sendEvents(createEvent("av.start", true, extraProps));
    }

    /***
     * Media playback restarted manually after a pause.
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void playbackResumed(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis = cursorPosition;
        bufferTimeMillis = 0;
        isPlaying = true;
        isPlaybackActivated = true;

        stopHeartbeatService();
        if (autoHeartbeat) {
            int diffMin = (int) ((System.currentTimeMillis() - startSessionTimeMillis) / 60000);
            heartbeatExecutor.schedule(heartbeatRunnable, heartbeatDurations.get(diffMin, MIN_HEARTBEAT_DURATION), TimeUnit.SECONDS);
        }

        sendEvents(createEvent("av.resume", true, extraProps));
    }

    /***
     * Media playback paused.
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void playbackPaused(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis = cursorPosition;
        bufferTimeMillis = 0;
        isPlaying = false;
        isPlaybackActivated = true;

        stopHeartbeatService();

        sendEvents(createEvent("av.pause", true, extraProps));
    }

    /***
     * Media playback stopped.
     * @param cursorPosition Cursor position (milliseconds)
     */
    public synchronized void playbackStopped(int cursorPosition, Map<String, Object> extraProps) {
        startSessionTimeMillis = startSessionTimeMillis == 0 ? System.currentTimeMillis() : startSessionTimeMillis;

        updateDuration();

        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis = cursorPosition;

        bufferTimeMillis = 0;
        isPlaying = false;
        isPlaybackActivated = false;

        stopHeartbeatService();

        startSessionTimeMillis = 0;
        sessionDurationMillis = 0;
        bufferTimeMillis = 0;

        sendEvents(createEvent("av.stop", true, extraProps));

        resetState();
    }

    /***
     * Measuring seek event.
     * @param oldCursorPosition Starting position (milliseconds)
     * @param newCursorPosition Ending position (milliseconds)
     */
    public void seek(int oldCursorPosition, int newCursorPosition, Map<String, Object> extraProps) {
        if (oldCursorPosition > newCursorPosition) {
            seekBackward(oldCursorPosition, newCursorPosition, extraProps);
        } else {
            seekForward(oldCursorPosition, newCursorPosition, extraProps);
        }
    }

    /***
     * Measuring seek backward.
     * @param oldCursorPosition Starting position (milliseconds)
     * @param newCursorPosition Ending position (milliseconds)
     */
    public synchronized void seekBackward(int oldCursorPosition, int newCursorPosition, Map<String, Object> extraProps) {
        processSeek("backward", oldCursorPosition, newCursorPosition, extraProps);
    }

    /***
     * Measuring seek forward.
     * @param oldCursorPosition Starting position (milliseconds)
     * @param newCursorPosition Ending position (milliseconds)
     */
    public synchronized void seekForward(int oldCursorPosition, int newCursorPosition, Map<String, Object> extraProps) {
        processSeek("forward", oldCursorPosition, newCursorPosition, extraProps);
    }

    /***
     * Measuring seek start.
     * @param oldCursorPosition Old Cursor position (milliseconds)
     */
    public synchronized void seekStart(int oldCursorPosition, Map<String, Object> extraProps) {
        sendEvents(createSeekStart(oldCursorPosition, extraProps));
    }

    /***
     * Measuring media click (especially for ads).
     */
    public synchronized void adClick(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.ad.click", false, extraProps));
    }

    /***
     * Measuring media skip (especially for ads).
     */
    public synchronized void adSkip(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.ad.skip", false, extraProps));
    }

    /***
     * Error measurement preventing reading from continuing.
     */
    public synchronized void error(String message, Map<String, Object> extraProps) {
        if (extraProps == null) {
            extraProps = new HashMap<>();
        }
        extraProps.put("av_player_error", message);
        sendEvents(createEvent("av.error", false, extraProps));
    }

    /***
     * Measuring reco or Ad display.
     */
    public synchronized void display(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.display", false, extraProps));
    }

    /***
     * Measuring close action.
     */
    public synchronized void close(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.close", false, extraProps));
    }

    /***
     * Measurement of a volume change action.
     */
    public synchronized void volume(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.volume", false, extraProps));
    }

    /***
     * Measurement of activated subtitles.
     */
    public synchronized void subtitleOn(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.subtitle.on", false, extraProps));
    }

    /***
     * Measurement of deactivated subtitles.
     */
    public synchronized void subtitleOff(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.subtitle.off", false, extraProps));
    }

    /***
     * Measuring a full-screen display.
     */
    public synchronized void fullscreenOn(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.fullscreen.on", false, extraProps));
    }

    /***
     * Measuring a full screen deactivation.
     */
    public synchronized void fullscreenOff(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.fullscreen.off", false, extraProps));
    }

    /***
     * Measurement of a quality change action.
     */
    public synchronized void quality(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.quality", false, extraProps));
    }

    /***
     * Measurement of a speed change action.
     */
    public synchronized void speed(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.speed", false, extraProps));
    }

    /***
     * Measurement of a sharing action.
     */
    public synchronized void share(Map<String, Object> extraProps) {
        sendEvents(createEvent("av.share", false, extraProps));
    }

    private synchronized Event createSeekStart(int oldCursorPosition, Map<String, Object> extraProps) {
        previousCursorPositionMillis = currentCursorPositionMillis;
        currentCursorPositionMillis = oldCursorPosition;

        if (isPlaying) {
            updateDuration();
        } else {
            eventDurationMillis = 0;
        }
        return createEvent("av.seek.start", true, extraProps);
    }

    private synchronized void processSeek(String seekDirection, int oldCursorPosition, int newCursorPosition, Map<String, Object> extraProps) {
        Event seekStart = createSeekStart(oldCursorPosition, extraProps);

        eventDurationMillis = 0;
        previousCursorPositionMillis = oldCursorPosition;
        currentCursorPositionMillis = newCursorPosition;

        sendEvents(seekStart, createEvent("av." + seekDirection, true, extraProps));
    }

    private Event createEvent(String name, boolean withOptions, Map<String, Object> extraProps) {
        Map<String, Object> props = Utility.toFlatten(this.getProps());
        if (withOptions) {
            props.put("av_previous_position", this.previousCursorPositionMillis);
            props.put("av_position", this.currentCursorPositionMillis);
            props.put("av_duration", this.eventDurationMillis);
            props.put("av_previous_event", this.previousEvent);
            this.previousEvent = name;
        }
        props.put("av_session_id", sessionId);

        if (extraProps != null) {
            props.putAll(Utility.toFlatten(extraProps));
        }
        return new Event(name).setData(props);
    }

    private void sendEvents(Event... events) {
        if (events.length == 0) {
            return;
        }
        for (Event e : events) {
            this.events.add(e);
        }
        this.events.send();
    }

    private void updateDuration() {
        eventDurationMillis = System.currentTimeMillis() - startSessionTimeMillis - sessionDurationMillis;
        sessionDurationMillis += eventDurationMillis;
    }

    private void stopHeartbeatService() {
        if (heartbeatExecutor != null && !heartbeatExecutor.isShutdown()) {
            heartbeatExecutor.shutdownNow();
        }
        heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void resetState() {
        sessionId = UUID.randomUUID().toString();
        previousEvent = "";
        previousCursorPositionMillis = 0;
        currentCursorPositionMillis = 0;
        eventDurationMillis = 0;
    }
}
