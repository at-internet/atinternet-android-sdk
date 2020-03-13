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
        media.processAutoHeartbeat();
    }
}

class BufferHeartbeatRunnable extends AVRunnable {

    BufferHeartbeatRunnable(Media media) {
        super(media);
    }

    @Override
    public void run() {
        media.processAutoBufferHeartbeat();
    }
}

class RebufferHeartbeatRunnable extends AVRunnable {

    RebufferHeartbeatRunnable(Media media) {
        super(media);
    }

    @Override
    public void run() {
        media.processAutoRebufferHeartbeat();
    }
}
