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

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SearchEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.lang.ref.WeakReference;
import java.util.List;

class SensorOrientationManager implements SensorEventListener {

    interface OrientationListener {
        void onOrientationChange(int orientation);
    }

    private SensorManager sensorManager;
    private Display display;
    static int orientation;
    private OrientationListener orientationListener;

    SensorOrientationManager(OrientationListener orientationListener) {
        this.orientationListener = orientationListener;
        sensorManager = (SensorManager) Tracker.getAppContext().getSystemService(android.content.Context.SENSOR_SERVICE);
        display = ((WindowManager) Tracker.getAppContext().getSystemService(android.content.Context.WINDOW_SERVICE)).getDefaultDisplay();
        orientation = Tracker.getAppContext().getResources().getConfiguration().orientation;
    }

    void registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int newOrientation = Tracker.getAppContext().getResources().getConfiguration().orientation;
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                newOrientation = 1;
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                newOrientation = 2;
                break;
        }
        if (orientation != newOrientation) {
            orientation = newOrientation;
            orientationListener.onOrientationChange(orientation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

@TargetApi(12)
class InteractionListener implements Window.Callback {

    private static final String TAG = "SmartTracker";

    private Window.Callback defaultCallback;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private SmartSender smartSender;

    Window.Callback getDefaultCallback() {
        return defaultCallback;
    }

    InteractionListener(Window.Callback defaultCallback, GestureDetector gestureDetector, ScaleGestureDetector scaleGestureDetector, SmartSender smartSender) {
        this.defaultCallback = defaultCallback;
        this.gestureDetector = gestureDetector;
        this.scaleGestureDetector = scaleGestureDetector;
        this.smartSender = smartSender;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // TODO Track it ??
            Log.d(TAG, "Back detected ");
        }
        return defaultCallback.dispatchKeyEvent(keyEvent);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        return defaultCallback.dispatchKeyShortcutEvent(keyEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 2) {
            scaleGestureDetector.onTouchEvent(motionEvent);
        } else {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return defaultCallback.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return defaultCallback.dispatchTrackballEvent(motionEvent);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        return defaultCallback.dispatchGenericMotionEvent(motionEvent);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return defaultCallback.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    @android.support.annotation.Nullable
    @Override
    public View onCreatePanelView(int i) {
        return defaultCallback.onCreatePanelView(i);
    }

    @Override
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return defaultCallback.onCreatePanelMenu(i, menu);
    }

    @Override
    public boolean onPreparePanel(int i, View view, Menu menu) {
        return defaultCallback.onPreparePanel(i, view, menu);
    }

    @Override
    public boolean onMenuOpened(int i, Menu menu) {
        return defaultCallback.onMenuOpened(i, menu);
    }

    @Override
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return defaultCallback.onMenuItemSelected(i, menuItem);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        defaultCallback.onWindowAttributesChanged(layoutParams);
    }

    @Override
    public void onContentChanged() {
        defaultCallback.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean b) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> views = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!views.isEmpty()) {
            ViewRootData vrd = views.get(views.size() - 1);
            ViewGroup vg = (ViewGroup) vrd.getView();
            View child = vg.getChildAt(vg.getChildCount() - 1);
            if (!(child instanceof ATLayer) && vrd.isDialogType() && vg.findViewById(R.id.atCustomDialogTitle) == null) {
                vg.addView(new ATLayer(currentActivity, smartSender));
            }
        }

        if (!b) {
            smartSender.sendMessage(SocketEmitterMessages.Screenshot("ScreenshotUpdated", false));
        }
        defaultCallback.onWindowFocusChanged(b);
    }

    @Override
    public void onAttachedToWindow() {
        defaultCallback.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        defaultCallback.onDetachedFromWindow();
    }

    @Override
    public void onPanelClosed(int i, Menu menu) {
        defaultCallback.onPanelClosed(i, menu);
    }

    @Override
    public boolean onSearchRequested() {
        return defaultCallback.onSearchRequested();
    }

    @TargetApi(23)
    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return defaultCallback.onSearchRequested(searchEvent);
    }

    @android.support.annotation.Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return defaultCallback.onWindowStartingActionMode(callback);
    }

    @TargetApi(23)
    @android.support.annotation.Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return defaultCallback.onWindowStartingActionMode(callback, i);
    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {
        defaultCallback.onActionModeStarted(actionMode);
    }

    @Override
    public void onActionModeFinished(ActionMode actionMode) {
        defaultCallback.onActionModeFinished(actionMode);
    }
}

class SmartSimpleGestureAnalyser extends GestureDetector.SimpleOnGestureListener {

    private static int DELTA = 50;
    static Runnable cancelable;
    private SmartSender smartSender;

    SmartSimpleGestureAnalyser(SmartSender smartSender, float scale) {
        this.smartSender = smartSender;
        DELTA *= scale;
    }

    @Override
    public boolean onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            String type;
            String direction;
            if (Math.abs(start.getRawX() - end.getRawX()) > DELTA && Math.abs(start.getRawY() - end.getRawY()) < DELTA) {
                type = "swipe";
                if (start.getRawX() < end.getRawX()) {
                    direction = "right";
                } else {
                    direction = "left";
                }
            } else if (Math.abs(start.getRawY() - end.getRawY()) > DELTA && Math.abs(start.getRawX() - end.getRawX()) < DELTA) {
                type = "scroll";
                if (start.getRawY() < end.getRawY()) {
                    direction = "up";
                } else {
                    direction = "down";
                }
            } else {
                type = "pan";
                direction = "left";
            }
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(start.getRawX()), Math.round(start.getRawY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, start.getRawX(), start.getRawY(), type, direction);
                cancelable = EventQueue.getInstance().insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                    }
                });
            }
        }
        return false;
    }

    public void onLongPress(MotionEvent event) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(event.getRawX()), Math.round(event.getRawY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, event.getRawX(), event.getRawY(), "tap", "long");
                cancelable = EventQueue.getInstance().insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                    }
                });
            }
        }
    }

    public boolean onDoubleTap(MotionEvent event) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(event.getRawX()), Math.round(event.getRawY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, event.getRawX(), event.getRawY(), "tap", "double");
                EventQueue.getInstance()
                        .cancel(cancelable)
                        .insert(new Runnable() {
                            @Override
                            public void run() {
                                smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                            }
                        });
            }
        }
        return super.onDoubleTap(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(event.getRawX()), Math.round(event.getRawY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, event.getRawX(), event.getRawY(), "tap", "single");
                cancelable = EventQueue.getInstance().cancel(cancelable).insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                    }
                });
            }
        }
        return super.onSingleTapUp(event);
    }
}

@TargetApi(11)
class SmartScaleGestureAnalyser extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private SmartSender smartSender;

    SmartScaleGestureAnalyser(SmartSender smartSender) {
        this.smartSender = smartSender;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            String direction = detector.getScaleFactor() > 1 ? "in" : "out";
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(detector.getCurrentSpanX()), Math.round(detector.getCurrentSpanY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, detector.getCurrentSpanX(), detector.getCurrentSpanY(), "pinch", direction);
                SmartSimpleGestureAnalyser.cancelable = EventQueue.getInstance()
                        .cancel(SmartSimpleGestureAnalyser.cancelable)
                        .insert(new Runnable() {
                            @Override
                            public void run() {
                                smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                            }
                        });
            }
        }
    }
}

class SmartTrackerActivityLifecycle extends TrackerActivityLifeCycle implements SensorOrientationManager.OrientationListener {

    private SensorOrientationManager sensorOrientationManager;
    private Handler handler;
    static Runnable cancelable;
    private GetConfigRequester getConfigRequester;

    private Runnable toolbarCancelable;
    private SmartSender smartSender;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    SmartTrackerActivityLifecycle(com.atinternet.tracker.Configuration conf, final SmartSender smartSender, float scale) {
        super(conf);
        sensorOrientationManager = new SensorOrientationManager(this);
        this.smartSender = smartSender;
        handler = new Handler();
        gestureDetector = new GestureDetector(AutoTracker.getAppContext(), new SmartSimpleGestureAnalyser(smartSender, scale));
        scaleGestureDetector = new ScaleGestureDetector(AutoTracker.getAppContext(), new SmartScaleGestureAnalyser(smartSender));
        toolbarCancelable = new Runnable() {
            @Override
            public void run() {
                if (smartSender.getToolbar() != null) {
                    smartSender.getToolbar().setVisible(false);
                }
            }
        };
    }

    private void enableListener(Activity activity) {
        handler.removeCallbacks(toolbarCancelable);
        if (smartSender.getToolbar() != null) {
            smartSender.getToolbar().setVisible(true);
        }
        if (!SmartContext.listenersSet) {
            sensorOrientationManager.registerListener();

            Window win = activity.getWindow();
            InteractionListener interactionListener = new InteractionListener(win.getCallback(), gestureDetector, scaleGestureDetector, smartSender);

            win.setCallback(interactionListener);
            SmartContext.listenersSet = true;
        }
    }

    @SuppressWarnings("depreciation")
    private void disableListener(Activity activity) {
        sensorOrientationManager.unregisterListener();
        SmartContext.listenersSet = false;
        Window win = activity.getWindow();
        Window.Callback callback = win.getCallback();
        if (callback instanceof InteractionListener) {
            InteractionListener interactionListener = (InteractionListener) callback;
            win.setCallback(interactionListener.getDefaultCallback());
        } else {
            win.setCallback(callback);
        }
        handler.postDelayed(toolbarCancelable, DELAY);
    }

    void setGetConfigRequester(GetConfigRequester getConfigRequester) {
        this.getConfigRequester = getConfigRequester;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        SmartContext.currentActivity = new WeakReference<>(activity);
        SmartContext.currentFragment = null;
        if (timeInBackground > -1) {
            if (Tool.getSecondsBetweenTimes(System.currentTimeMillis(), timeInBackground) >= DELTA) {
                TrackerQueue.getInstance().put(getConfigRequester);
            }

            int sessionBackgroundDuration = Integer.parseInt(String.valueOf(configuration.get(TrackerConfigurationKeys.SESSION_BACKGROUND_DURATION)));
            if (Tool.getSecondsBetweenTimes(System.currentTimeMillis(), timeInBackground) >= (sessionBackgroundDuration < DELTA ? DELTA : sessionBackgroundDuration)) {
                LifeCycle.newSessionInit(Tracker.getPreferences());
                timeInBackground = -1;
            }

        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        enableListener(activity);
        if (cancelable == null) {
            cancelable = EventQueue.getInstance().insert(new Runnable() {
                @Override
                public void run() {
                    cancelable = null;
                    smartSender.sendMessage(SocketEmitterMessages.ViewDidAppear(), false);
                }
            });
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);
        disableListener(activity);
    }

    @Override
    public void onOrientationChange(final int orientation) {
        if (smartSender.getLiveConnectionState() == SmartSender.LiveConnectionState.Pending) {
            smartSender.reset();
        } else {
            final SmartEvent smartEvent = new SmartEvent(new SmartView(null, new int[2]).setClassName("UIRotation"), null, -1, -1, "deviceRotate", "-1");

            if (AutoTracker.getInstance().isEnabledAutoTracking()) {
                cancelable = EventQueue.getInstance().cancel(cancelable)
                        .insert(new Runnable() {
                            @Override
                            public void run() {
                                cancelable = null;
                                smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                            }
                        });
            } else {
                EventQueue.getInstance().insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                    }
                });
            }

            if (AutoTracker.getInstance().isEnabledLiveTagging()) {
                EventQueue.getInstance().insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.ScreenRotation());
                    }
                });
            }
        }
    }
}
