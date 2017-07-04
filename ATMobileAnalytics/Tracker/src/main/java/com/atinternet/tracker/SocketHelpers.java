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
import android.content.DialogInterface;

import org.json.JSONException;
import org.json.JSONObject;

abstract class SocketReceivable {
    protected JSONObject message;
    protected SmartSender smartSender;

    SocketReceivable(SmartSender smartSender, JSONObject message) {
        this.message = message;
        this.smartSender = smartSender;
    }

    protected abstract void process() throws JSONException;
}

class SocketEmitterMessages {

    static JSONObject DeviceAskedForLive() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("event", "DeviceAskedForLive")
                    .put("data", AutoTracker.getInstance().getApplication().getData());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject DeviceDisableLive(String eventName) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            dataObject.put("token", AutoTracker.getInstance().getToken());
            jsonObject.put("event", eventName)
                    .put("data", dataObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject DeviceAcceptedLive() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            dataObject.put("token", AutoTracker.getInstance().getToken());
            jsonObject.put("event", "DeviceAcceptedLive")
                    .put("data", dataObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject DeviceVersion() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("event", "DeviceVersion")
                    .put("data", AutoTracker.getInstance().getApplication().getData());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject App() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("event", "app")
                    .put("data", AutoTracker.getInstance().getApplication().getData());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject DeviceTokenAlreadyUsed() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("event", "DeviceTokenAlreadyUsed")
                    .put("data", AutoTracker.getInstance().getApplication().getData());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject ViewDidAppear(String... className) {
        try {
            Screen screen = new Screen();
            if (className.length == 1) {
                screen.setClassName(className[0]);
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            JSONObject screenObject = screen.getData();

            dataObject.put("screen", screenObject);
            jsonObject.put("event", "viewDidAppear")
                    .put("data", dataObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject Event(SmartEvent event) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("event", event.getType())
                    .put("data", event.getData());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject ScreenRotation(String... className) {
        try {
            Screen screen = new Screen();
            if (className.length == 1) {
                screen.setClassName(className[0]);
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            dataObject.put("screen", screen.getData())
                    .put("className", screen.getClassName())
                    .put("name", screen.getTitle())
                    .put("methodName", "rotate")
                    .put("triggeredBy", "")
                    .put("type", "screen")
                    .put("direction", SensorOrientationManager.orientation);


            jsonObject.put("event", "screenRotation")
                    .put("data", dataObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject Screenshot(String eventName, boolean withTree) {
        try {
            Screen screen = new Screen();
            JSONObject jsonObject = new JSONObject();
            JSONObject dataObject = new JSONObject();

            Screenshot sc = new Screenshot();

            dataObject.put("screen", screen.getData())
                    .put("screenshot", sc.getB64Value());

            if (withTree) {
                dataObject.put("tree", screen.getSuggestedEvents(sc.getAttachedRootView()));
            }

            jsonObject.put("event", eventName)
                    .put("data", dataObject);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class SocketFactory {

    static SocketReceivable create(SmartSender smartSender, JSONObject message) throws JSONException {
        String eventName = message.getString("event");
        switch (eventName) {
            case "ScreenshotRequest":
                return new ScreenshotRequestReceivable(smartSender, message);
            case "InterfaceAskedForLive":
                return new InterfaceAskedForLiveReceivable(smartSender, message);
            case "InterfaceAcceptedLive":
                return new InterfaceAcceptedLiveReceivable(smartSender, message);
            case "InterfaceRefusedLive":
                return new InterfaceRefusedLiveReceivable(smartSender, message);
            case "InterfaceAskedForScreenshot":
                return new InterfaceAskedForScreenshotReceivable(smartSender, message);
            case "InterfaceStoppedLive":
                return new InterfaceStoppedLiveReceivable(smartSender, message);
            case "InterfaceAbortedLiveRequest":
                return new InterfaceAbortedLiveRequestReceivable(smartSender, message);
            default:
                return null;
        }
    }
}

class InterfaceAskedForLiveReceivable extends SocketReceivable {

    InterfaceAskedForLiveReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {

        long now = System.currentTimeMillis();
        long elapsed = now - smartSender.startTime;
        if (elapsed < SmartSender.COOLDOWN ) {
            return;
        }

        if (smartSender.getAliveState() != SmartSender.AliveState.Asked) {
            smartSender.setAliveState(SmartSender.AliveState.Asked);

            JSONObject data = message.getJSONObject("data");
            boolean warning = false;

            if (data != null) {
                if (data.has("appID") && !AutoTracker.getInstance().getApplication().getAppId().equals(data.getString("appID"))) {
                    smartSender.sendMessage(SocketEmitterMessages.DeviceTokenAlreadyUsed(), true);
                    warning = true;
                } else if (data.has("version") && !AutoTracker.getInstance().getApplication().getVersion().equals(data.getString("version"))) {
                    if (smartSender.getLiveConnectionState() != SmartSender.LiveConnectionState.Pending) {
                        smartSender.sendMessage(SocketEmitterMessages.DeviceVersion(), true);
                        smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Pending);
                    }
                    smartSender.setAliveState(SmartSender.AliveState.None);
                    warning = true;
                }
            }
            if (!warning && smartSender.getLiveConnectionState() != SmartSender.LiveConnectionState.Connected) {
                smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Pending);

                final Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
                if (currentActivity != null) {
                    Tool.runOnMainThread(currentActivity, new Runnable() {
                        @Override
                        public void run() {
                            smartSender.showPopup(new Popup(currentActivity)
                                    .setTitle(R.string.pairing_request_title)
                                    .setMessage(R.string.pairing_request_message)
                                    .setNegativeButton(R.string.pairing_refuse_button, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Disconnected, "DeviceRefusedLive");
                                            smartSender.setAliveState(SmartSender.AliveState.None);
                                            dialogInterface.dismiss();
                                            smartSender.startTime = System.currentTimeMillis();
                                        }
                                    })
                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Connected, "DeviceAcceptedLive");
                                            smartSender.setAliveState(SmartSender.AliveState.None);
                                            dialogInterface.dismiss();
                                        }
                                    }));
                        }
                    });
                }
            }
        }
    }
}

class InterfaceAcceptedLiveReceivable extends SocketReceivable {

    InterfaceAcceptedLiveReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Connected);
    }
}

class InterfaceRefusedLiveReceivable extends SocketReceivable {

    InterfaceRefusedLiveReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        if (smartSender.getAliveState() != SmartSender.AliveState.Refused && smartSender.getLiveConnectionState() != SmartSender.LiveConnectionState.Disconnected) {
            smartSender.setAliveState(SmartSender.AliveState.Refused);
            smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Disconnected);

            final Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
            if (currentActivity != null) {
                Tool.runOnMainThread(currentActivity, new Runnable() {
                    @Override
                    public void run() {
                        smartSender.showPopup(new Popup(currentActivity)
                                .setTitle(R.string.pairing_refused_title)
                                .setMessage(R.string.pairing_refused_message)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        smartSender.setAliveState(SmartSender.AliveState.None);
                                        dialogInterface.dismiss();
                                    }
                                }));
                    }
                });
            }
        }
    }
}

class InterfaceAskedForScreenshotReceivable extends SocketReceivable {

    InterfaceAskedForScreenshotReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        if (currentActivity != null) {
            Tool.runOnMainThread(currentActivity, new Runnable() {
                @Override
                public void run() {
                    smartSender.sendMessage(SocketEmitterMessages.Screenshot("ScreenshotUpdated", true));
                }
            });
        }
    }
}

class InterfaceStoppedLiveReceivable extends SocketReceivable {

    InterfaceStoppedLiveReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        if (smartSender.getAliveState() != SmartSender.AliveState.Stopped) {
            smartSender.setAliveState(SmartSender.AliveState.Stopped);
            smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Disconnected);

            final Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
            if (currentActivity != null) {
                Tool.runOnMainThread(currentActivity, new Runnable() {
                    @Override
                    public void run() {
                        smartSender.showPopup(new Popup(currentActivity)
                                .setTitle(R.string.pairing_stopped_title)
                                .setMessage(R.string.pairing_stopped_message)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        smartSender.setAliveState(SmartSender.AliveState.None);
                                        dialogInterface.dismiss();
                                    }
                                }));
                    }
                });
            }
        }
    }
}

class InterfaceAbortedLiveRequestReceivable extends SocketReceivable {

    InterfaceAbortedLiveRequestReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        if (smartSender.getAliveState() != SmartSender.AliveState.Aborted) {
            smartSender.setAliveState(SmartSender.AliveState.Aborted);
            smartSender.setLiveConnectionState(SmartSender.LiveConnectionState.Disconnected);

            final Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
            if (currentActivity != null) {
                Tool.runOnMainThread(currentActivity, new Runnable() {
                    @Override
                    public void run() {
                        smartSender.showPopup(new Popup(currentActivity)
                                .setTitle(R.string.pairing_canceled_title)
                                .setMessage(R.string.pairing_canceled_message)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        smartSender.setAliveState(SmartSender.AliveState.None);
                                        dialogInterface.dismiss();
                                    }
                                }));
                    }
                });
            }
        }
    }
}

class ScreenshotRequestReceivable extends SocketReceivable {

    ScreenshotRequestReceivable(SmartSender smartSender, JSONObject message) {
        super(smartSender, message);
    }

    @Override
    protected void process() throws JSONException {
        JSONObject data = message.getJSONObject("data");
        boolean valid = false;
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        if (data != null) {
            if (data.has("screen")) {
                JSONObject screenObj = data.getJSONObject("screen");
                if (screenObj.has("className")) {
                    String className = screenObj.getString("className");
                    if (currentActivity != null && currentActivity.getClass().getSimpleName().equals(className)) {
                        valid = true;
                    } else {
                        android.support.v4.app.Fragment frag = SmartContext.currentFragment != null ? SmartContext.currentFragment.get() : null;
                        if (frag != null && frag.getClass().getSimpleName().equals(className)) {
                            valid = true;
                        }
                    }
                }

            }
        }

        if (valid && currentActivity != null) {
            Tool.runOnMainThread(currentActivity, new Runnable() {
                @Override
                public void run() {
                    smartSender.sendMessage(SocketEmitterMessages.Screenshot("screenshot", true));
                }
            });
        }
    }
}


