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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

class Popup {
    private Context c;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    Popup setTitle(int titleStrId) {
        DisplayMetrics m = new DisplayMetrics();
        ((WindowManager) c.getSystemService(android.content.Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(m);

        TextView title = (TextView) View.inflate(c, R.layout.custom_popup_title, null);
        title.setText(c.getString(titleStrId).toUpperCase());
        title.setTypeface(UI.getFonts(c).get("Montserrat-Bold"));
        title.setTextColor(UI.getColor(c, R.color.at_popup_text));
        title.setPadding(0, (int) (15 * m.density), 0, 0);

        builder.setCustomTitle(title);
        return this;
    }

    Popup setMessage(int messageStrId) {
        builder.setMessage(messageStrId);
        return this;
    }

    Popup setPositiveButton(int positiveButtonStrId, DialogInterface.OnClickListener positiveButtonListener) {
        builder.setPositiveButton(c.getString(positiveButtonStrId).toUpperCase(), positiveButtonListener);
        return this;
    }

    Popup setNegativeButton(int negativeButtonStrId, DialogInterface.OnClickListener negativeButtonListener) {
        builder.setNegativeButton(c.getString(negativeButtonStrId).toUpperCase(), negativeButtonListener);
        return this;
    }

    Popup setNeutralButton(int neutralButtonStrId, DialogInterface.OnClickListener neutralButtonListener) {
        builder.setNeutralButton(c.getString(neutralButtonStrId).toUpperCase(), neutralButtonListener);
        return this;
    }

    Popup(Context c) {
        this.c = c;
        builder = new AlertDialog.Builder(c);
    }

    void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    void show() {
        dialog = builder.create();
        Window w;
        if ((w = dialog.getWindow()) != null) {
            w.getAttributes().windowAnimations = R.style.PopUp;
        }
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ((TextView) dialog.findViewById(android.R.id.message)).setTypeface(UI.getFonts(c).get("OpenSans-Regular"));
                dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(UI.getColor(c, R.color.at_popup_button_text));
                dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(UI.getColor(c, R.color.at_popup_button_text));
                dialog.getButton(Dialog.BUTTON_NEUTRAL).setTextColor(UI.getColor(c, R.color.at_popup_button_text));
            }
        });
        dialog.show();
    }
}

final class SmartContext {

    private SmartContext() {
        throw new IllegalStateException("Private class");
    }

    static boolean listenersSet = false;
    static WeakReference<Activity> currentActivity;
    static WeakReference<android.support.v4.app.Fragment> currentFragment;
}

class SmartSender implements View.OnClickListener {

    enum LiveConnectionState {
        DISCONNECTED, PENDING, CONNECTED
    }

    enum AliveState {
        ABORTED, STOPPED, REFUSED, ASKED, NONE
    }

    long startTime = System.currentTimeMillis();
    static final int COOLDOWN = 3000;

    private static final int DELAY = 3000;
    private static final HashSet<String> NOT_TRACKABLE_EVENT_LIST = new HashSet<>();

    private JSONObject lastViewDidAppear;
    private LiveConnectionState liveConnectionState;
    private static io.socket.client.Socket socketIOClient;
    private Toolbar toolbar;
    private Popup popup;
    private Handler handler = new Handler();
    private Runnable runnable;
    private AliveState aliveState;

    private String token;
    private String socketEndpoint;

    SmartSender(String token, String socketEndpoint) {
        this.token = token;
        this.socketEndpoint = socketEndpoint;
        NOT_TRACKABLE_EVENT_LIST.add("DeviceAcceptedLive");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceAskedForLive");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceRefusedLive");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceTokenAlreadyUsed");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceVersion");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceAbortedLiveRequest");
        NOT_TRACKABLE_EVENT_LIST.add("DeviceStoppedLive");
        NOT_TRACKABLE_EVENT_LIST.add("ScreenshotUpdated");
        NOT_TRACKABLE_EVENT_LIST.add("app");
        NOT_TRACKABLE_EVENT_LIST.add("screenshot");
        NOT_TRACKABLE_EVENT_LIST.add("screenRotation");
    }

    Toolbar getToolbar() {
        return toolbar;
    }

    void showPopup(Popup popup) {
        if (this.popup != null) {
            this.popup.dismiss();
        }
        this.popup = popup;
        this.popup.show();
    }

    void reset() {
        this.popup.dismiss();
        aliveState = AliveState.NONE;
        setLiveConnectionState(LiveConnectionState.DISCONNECTED);
    }

    AliveState getAliveState() {
        return aliveState;
    }

    void setAliveState(AliveState aliveState) {
        this.aliveState = aliveState;
    }

    LiveConnectionState getLiveConnectionState() {
        return liveConnectionState;
    }

    void setLiveConnectionState(final LiveConnectionState liveConnectionState, final String... args) {
        this.liveConnectionState = liveConnectionState;

        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        if (currentActivity != null) {
            Tool.runOnMainThread(currentActivity, new Runnable() {
                @Override
                public void run() {
                    if (liveConnectionState == LiveConnectionState.CONNECTED) {
                        toolbar.setConnectedState();
                        handler.removeCallbacks(runnable);
                        if (args.length == 1 && args[0].equals("DeviceAcceptedLive")) {
                            sendMessage(SocketEmitterMessages.DeviceAcceptedLive());
                        }
                        sendMessage(SocketEmitterMessages.App());
                        if (lastViewDidAppear != null) {
                            sendMessage(lastViewDidAppear);
                        }
                    } else if (liveConnectionState == LiveConnectionState.PENDING) {
                        toolbar.setPendingState();
                        if (args.length == 1 && args[0].equals("Device")) {
                            runnable = new Runnable() {
                                @Override
                                public void run() {
                                    sendMessage(SocketEmitterMessages.DeviceAskedForLive(), true);
                                    handler.postDelayed(runnable, DELAY);
                                }
                            };
                            handler.postDelayed(runnable, 0);
                        }
                    } else if (liveConnectionState == LiveConnectionState.DISCONNECTED) {
                        toolbar.setDisconnectedState();
                        handler.removeCallbacks(runnable);
                        if (args.length == 1) {
                            sendMessage(SocketEmitterMessages.DeviceDisableLive(args[0]), true);
                        }
                    }
                }
            });
        }
    }

    void init() {
        if (toolbar == null) {
            this.toolbar = new Toolbar();
            toolbar.setClickListeners(this);
        }
        if (socketIOClient == null) {
            initWebSocket();
        }
        socketIOClient.connect();
    }

    private void initWebSocket() {
        liveConnectionState = LiveConnectionState.DISCONNECTED;
        try {
            io.socket.client.IO.Options opts = new io.socket.client.IO.Options();
            opts.query = "token=" + token;
            opts.transports = new String[]{"websocket", "polling"};
            opts.path = "/smartsdk/socket.io/";

            final SmartSender self = this;
            io.socket.emitter.Emitter.Listener listener = new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        if (args != null && args.length > 0) {
                            Object obj;
                            if ((obj = args[0]) != null) {
                                JSONObject message = new JSONObject(String.valueOf(obj));
                                SocketReceivable sr;
                                if ((sr = SocketFactory.create(self, message)) != null) {
                                    sr.process();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            socketIOClient = io.socket.client.IO.socket(socketEndpoint, opts);
            socketIOClient
                    .on("ScreenshotRequest", listener)
                    .on("InterfaceAskedForLive", listener)
                    .on("InterfaceAcceptedLive", listener)
                    .on("InterfaceRefusedLive", listener)
                    .on("InterfaceAskedForScreenshot", listener)
                    .on("InterfaceStoppedLive", listener)
                    .on("InterfaceAbortedLiveRequest", listener);

        } catch (Exception e) {
            Log.e(Tracker.TAG, e.toString());
        }
    }

    void disconnect() {
        if (socketIOClient != null) {
            socketIOClient.disconnect();
        }
    }

    private ArrayList<String> getEventsCombination(String eventName, JSONObject jsonObject) {

        ArrayList<String> arrayList = new ArrayList<>();

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            final String eventKeyBase = eventName + "." + data.getString("direction") + "." + data.getString("methodName");
            final String position = "." + data.getJSONObject("view").getString("position");
            final String view = "." + data.getJSONObject("view").getString("className");
            final String screen = "." + data.getJSONObject("screen").getString("className");

            arrayList = new ArrayList<>();
            arrayList.add(eventKeyBase + position + view + screen);
            arrayList.add(eventKeyBase + position + view);
            arrayList.add(eventKeyBase + position);
            arrayList.add(eventKeyBase + position + screen);
            arrayList.add(eventKeyBase);
            arrayList.add(eventKeyBase + view);
            arrayList.add(eventKeyBase + screen);
            arrayList.add(eventKeyBase + view + screen);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return arrayList;

    }

    private Pair<AutoTrackerListener, AutoTrackerListener> getValidClasses(String className) {

        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        android.support.v4.app.Fragment currentFragment = SmartContext.currentFragment != null ? SmartContext.currentFragment.get() : null;

        AutoTrackerListener finalFrag = null;
        AutoTrackerListener finalAct = null;

        if (currentFragment != null) {

            String currentFragmentClassName = currentFragment.getClass().getSimpleName();
            if (currentFragment instanceof AutoTrackerListener && currentFragmentClassName.equals(className)) {
                finalFrag = (AutoTrackerListener) currentFragment;
            }
        }

        if (currentActivity != null) {

            String currentActivityClassName = currentActivity.getClass().getSimpleName();
            if (currentActivity instanceof AutoTrackerListener && currentActivityClassName.equals(className)) {
                finalAct = (AutoTrackerListener) currentActivity;
            }
        }

        return new Pair<>(finalFrag, finalAct);
    }

    private void sendScreen(String className, String name) {

        Screen screen = AutoTracker.getInstance().setParam("auto", 1).Screens().add(name);

        Pair<AutoTrackerListener, AutoTrackerListener> validClasses = getValidClasses(className);

        if (validClasses.first != null) {
            screen = validClasses.first.screenDetected(screen);
        } else if (validClasses.second != null) {
            screen = validClasses.second.screenDetected(screen);
        }

        if (screen != null) {
            AutoTracker.getInstance().getDispatcher().dispatch(screen);
        }
    }

    private void sendGesture(String className, String name, String action) {

        Gesture gesture = AutoTracker.getInstance().setParam("auto", 1).Gestures().add(name);

        switch (action) {
            case "download":
                gesture.setAction(Gesture.Action.Download);
                break;
            case "exit":
                gesture.setAction(Gesture.Action.Exit);
                break;
            case "navigation":
                gesture.setAction(Gesture.Action.Navigate);
                break;
            default:
                gesture.setAction(Gesture.Action.Touch);
                break;
        }

        Pair<AutoTrackerListener, AutoTrackerListener> validClasses = getValidClasses(className);

        if (validClasses.first != null) {
            gesture = validClasses.first.gestureDetected(gesture);
        } else if (validClasses.second != null) {
            gesture = validClasses.second.gestureDetected(gesture);
        }

        if (gesture != null) {
            AutoTracker.getInstance().getDispatcher().dispatch(gesture);
        }
    }

    private void mapConfAndSend(JSONObject jsonObject) {
        try {
            String eventName = jsonObject.getString("event");

            JSONObject conf = AutoTracker.getInstance().getConf();
            if (conf.length() == 0) {
                String className = jsonObject.getJSONObject("data").getJSONObject("screen").getString("className");
                if (eventName.equals("viewDidAppear")) {
                    sendScreen(className, className);
                } else if (eventName.equals("tap")) {
                    sendGesture(className, jsonObject.getJSONObject("data").getString("methodName"), "auto");
                }
            } else {
                JSONObject configuration;
                if ((configuration = conf.getJSONObject("configuration")) != null) {

                    if (eventName.equals("viewDidAppear")) {

                        //Screens
                        JSONObject screens;
                        if ((screens = configuration.getJSONObject("screens")) != null) {
                            JSONObject screen;

                            String className = jsonObject.getJSONObject("data").getJSONObject("screen").getString("className");
                            if ((screen = screens.getJSONObject(className)) != null) {
                                boolean ignoreScreen = screen.getBoolean("ignoreElement");

                                if (!ignoreScreen) {
                                    sendScreen(className, screen.getString("title"));
                                }
                            }
                        }
                    } else {
                        //Events
                        boolean ignoreEvent = false;

                        JSONObject rules;
                        if ((rules = configuration.getJSONObject("rules")) != null) {
                            String rule = "ignore" + Tool.upperCaseFirstLetter(eventName);
                            ignoreEvent = !rules.has(rule) || rules.getBoolean(rule);
                        }

                        if (!ignoreEvent) {
                            JSONObject events;
                            if ((events = configuration.getJSONObject("events")) != null) {

                                ArrayList<String> eventsCombination = getEventsCombination(eventName, jsonObject);
                                for (String value : eventsCombination) {
                                    if (events.has(value)) {
                                        JSONObject event = events.getJSONObject(value);
                                        boolean ignoreElement = event.getBoolean("ignoreElement");
                                        if (!ignoreElement) {
                                            String action = event.has("action") ? event.getString("action") : "auto";
                                            sendGesture(jsonObject.getJSONObject("data").getJSONObject("screen").getString("className"), event.getString("title"), action);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean shouldTrackEvent(String eventName) {
        return !NOT_TRACKABLE_EVENT_LIST.contains(eventName);
    }

    void sendMessage(final JSONObject jsonObject, boolean... force) {
        try {
            String event = jsonObject.getString("event");
            if (event.equals("viewDidAppear")) {
                lastViewDidAppear = jsonObject;
            }
            if (AutoTracker.getInstance().isEnabledAutoTracking() && shouldTrackEvent(event)) {
                TrackerQueue.getInstance().put(new Runnable() {
                    @Override
                    public void run() {
                        mapConfAndSend(jsonObject);
                    }
                });
            }
            if (AutoTracker.getInstance().isEnabledLiveTagging()
                    && socketIOClient != null
                    && socketIOClient.connected()
                    && (liveConnectionState == LiveConnectionState.CONNECTED || (force.length == 1 && force[0]))) {
                socketIOClient.emit("message", jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int currentId = view.getId();
        if (currentId == R.id.recordButtonView) {
            switch (liveConnectionState) {
                case DISCONNECTED:
                    setLiveConnectionState(LiveConnectionState.PENDING, "Device");
                    break;
                case PENDING:
                    setLiveConnectionState(LiveConnectionState.DISCONNECTED, "DeviceAbortedLiveRequest");
                    break;
                case CONNECTED:
                    setLiveConnectionState(LiveConnectionState.DISCONNECTED, "DeviceStoppedLive");
                    break;
            }
        } else if (currentId == R.id.cameraImageView) {
            Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
            if (currentActivity != null) {

                toolbar.setClickListeners(null);
                takeScreenshot(currentActivity);

                android.media.MediaPlayer player = android.media.MediaPlayer.create(currentActivity, R.raw.camera_sound);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        sendMessage(SocketEmitterMessages.Screenshot("ScreenshotUpdated", true));
                    }
                });
                player.start();
            }
        }
    }

    private void takeScreenshot(final Activity currentAct) {
        final SmartSender self = this;
        final FrameLayout flashLayout = (FrameLayout) View.inflate(currentAct, R.layout.flash_layout, null);

        Animation flash = AnimationUtils.loadAnimation(currentAct, R.anim.flash);
        flash.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do Nothing because event must be ignored
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewGroup) currentAct.getWindow().getDecorView().getRootView()).removeView(flashLayout);
                toolbar.setClickListeners(self);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do Nothing because event must be ignored
            }
        });

        ((ViewGroup) currentAct.getWindow().getDecorView().getRootView()).addView(flashLayout, new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        flashLayout.startAnimation(flash);
    }
}

class UI {

    private UI() {
        throw new IllegalStateException("Private class");
    }

    private static HashMap<String, Typeface> fonts;

    static HashMap<String, Typeface> getFonts(final Context c) {
        if (fonts == null) {
            fonts = new HashMap<>();
            fonts.put("OpenSans-Regular", Typeface.createFromAsset(c.getAssets(), "fonts/OpenSans-Regular.ttf"));
            fonts.put("Montserrat-Bold", Typeface.createFromAsset(c.getAssets(), "fonts/Montserrat-Bold.ttf"));
            fonts.put("Montserrat-Regular", Typeface.createFromAsset(c.getAssets(), "fonts/Montserrat-Regular.ttf"));
        }
        return fonts;
    }

    static Pair<Integer, Integer> getScreenSize(Display d) {
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                if (Build.VERSION.SDK_INT < 17) {
                    int width = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                    int height = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                    return new Pair<>(width, height);
                } else {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    return new Pair<>(realSize.x, realSize.y);
                }
            } catch (Exception e) {
                Log.e(Tracker.TAG, e.toString());
            }
        }
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }

    static Float getScale(Display d) {
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        return metrics.density;
    }

    static int getScreenOrientation() {
        return Tracker.getAppContext().getResources().getConfiguration().orientation;
    }

    @SuppressWarnings("depreciation")
    static int getColor(Context c, int colorResId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return c.getColor(colorResId);
        } else {
            return c.getResources().getColor(colorResId);
        }
    }

    static SmartView getTouchedView(View rootView, int eventX, int eventY) {
        if (rootView instanceof ViewGroup) {
            ArrayList<View> touchables = ReflectionAPI.getAllTouchables((ViewGroup) rootView);

            for (int i = touchables.size() - 1; i >= 0; i--) {
                View v = touchables.get(i);
                int[] coords = new int[2];
                v.getLocationOnScreen(coords);
                if (isInHitBox(eventX, eventY, coords, v)) {
                    return new SmartView(v, coords);
                }
            }
        }
        return null;
    }

    private static boolean isInHitBox(int eventX, int eventY, int[] coords, View view) {
        return eventX > coords[0]
                && eventX < coords[0] + view.getWidth()
                && eventY > coords[1]
                && eventY < coords[1] + view.getHeight();
    }
}

class Toolbar extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

    private static final int TIMER = 1000;
    private static final String TIMER_FORMAT = "%02d : %02d : %02d";

    private static int TOOLBARWIDTH;
    private static int TOOLBARHEIGHT;
    private static WeakReference<ATRelativeLayout> toolbarLayout;

    private final View recordButtonView;
    private final TextView connectionTextView;
    private final TextView timerTextView;

    private final Handler handler;
    private final Runnable timerRunnable;
    private final WindowManager wm;
    private final WindowManager.LayoutParams toolbarLayoutParams;
    private final GestureDetector gestureDetector;

    private int seconds;
    private int min;
    private int hours;

    private void startTimer() {
        handler.postDelayed(timerRunnable, TIMER);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
        seconds = 0;
        min = 0;
        hours = 0;
        timerTextView.setText(String.format(Locale.getDefault(), TIMER_FORMAT, hours, min, seconds));
    }

    private void updateTimer() {
        seconds++;
        if (seconds == 60) {
            min++;
            seconds = 0;
        }
        if (min == 60) {
            hours++;
            min = 0;
            seconds = 0;
        }
        timerTextView.setText(String.format(Locale.getDefault(), TIMER_FORMAT, hours, min, seconds));
    }


    boolean isVisible() {
        return toolbarLayout.get().getVisibility() == View.VISIBLE;
    }

    void setVisible(boolean visible) {
        toolbarLayout.get().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    Toolbar() {
        Context c = AutoTracker.appContext.get();
        seconds = 0;
        min = 0;
        hours = 0;
        handler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                updateTimer();
                handler.postDelayed(timerRunnable, TIMER);
            }
        };

        DisplayMetrics metrics = new DisplayMetrics();
        wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        TOOLBARWIDTH = (int) (200 * metrics.density);
        TOOLBARHEIGHT = (int) (50 * metrics.density);

        toolbarLayout = new WeakReference<>((ATRelativeLayout) View.inflate(c, R.layout.toolbar_layout, null));
        recordButtonView = toolbarLayout.get().findViewById(R.id.recordButtonView);
        connectionTextView = toolbarLayout.get().findViewById(R.id.connectionTextView);
        timerTextView = toolbarLayout.get().findViewById(R.id.timerTextView);

        Typeface tp = UI.getFonts(c).get("OpenSans-Regular");
        connectionTextView.setTypeface(tp);
        timerTextView.setTypeface(tp);

        gestureDetector = new GestureDetector(c, this);
        toolbarLayout.get().setOnTouchListener(this);

        int windowType;
        if (Build.VERSION.SDK_INT >= 26) {
            windowType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            windowType = WindowManager.LayoutParams.TYPE_PHONE;
        }

        toolbarLayoutParams = new WindowManager.LayoutParams(
                TOOLBARWIDTH,
                TOOLBARHEIGHT,
                windowType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        toolbarLayoutParams.gravity = Gravity.TOP | Gravity.START;
        toolbarLayoutParams.x = metrics.widthPixels / 2 - (TOOLBARWIDTH / 2);
        toolbarLayoutParams.y = metrics.heightPixels - (TOOLBARHEIGHT * 2);

        wm.addView(toolbarLayout.get(), toolbarLayoutParams);
    }

    void setPendingState() {
        recordButtonView.setBackgroundResource(R.drawable.pending_record_shape);
        connectionTextView.setText(R.string.connecting);
    }

    void setConnectedState() {
        recordButtonView.setBackgroundResource(R.drawable.stop_record_shape);
        connectionTextView.setText(R.string.connected);
        startTimer();
    }

    void setDisconnectedState() {
        recordButtonView.setBackgroundResource(R.drawable.record_shape);
        connectionTextView.setText(R.string.disconnected);
        stopTimer();
    }

    void setClickListeners(View.OnClickListener cl) {
        recordButtonView.setOnClickListener(cl);
        toolbarLayout.get().findViewById(R.id.cameraImageView).setOnClickListener(cl);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                toolbarLayoutParams.x = (int) event.getRawX() - (TOOLBARWIDTH / 2);
                toolbarLayoutParams.y = (int) event.getRawY() - TOOLBARHEIGHT;
                wm.updateViewLayout(toolbarLayout.get(), toolbarLayoutParams);
            }
            return true;
        }
    }
}


