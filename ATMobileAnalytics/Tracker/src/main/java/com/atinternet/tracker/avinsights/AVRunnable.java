package com.atinternet.tracker.avinsights;

abstract class AVRunnable implements Runnable {
    protected Media media;

    AVRunnable(Media media) {
        this.media = media;
    }
}

class HeartbeatRunnable extends AVRunnable {

    HeartbeatRunnable(Media media) {
        super(media);
    }

    @Override
    public void run() {
        media.processHeartbeat(-1, true, null);
    }
}

class BufferHeartbeatRunnable extends AVRunnable {

    BufferHeartbeatRunnable(Media media) {
        super(media);
    }

    @Override
    public void run() {
        media.processBufferHeartbeat(true, null);
    }
}

class RebufferHeartbeatRunnable extends AVRunnable {

    RebufferHeartbeatRunnable(Media media) {
        super(media);
    }

    @Override
    public void run() {
        media.processRebufferHeartbeat(true, null);
    }
}
