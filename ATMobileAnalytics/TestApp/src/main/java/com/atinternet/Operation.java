package com.atinternet;

public class Operation {

    public Operation(int hitId) {
        this.hitId = hitId;
    }

    public int getHitId() {
        return hitId;
    }

    public void start() {
        sendAction.run();
    }

    public Operation setSendAction(Runnable sendAction) {
        this.sendAction = sendAction;
        return this;
    }

    private int hitId;
    private Runnable sendAction;
}
