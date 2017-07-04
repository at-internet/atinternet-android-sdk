package com.atinternet.tracker;

import android.app.Activity;
import android.view.View;

import java.util.List;

/**
 * Public class to help auto tracking pull to refresh event
 */
public class SmartPullToRefreshListener implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {

    private android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener defaultOnRefreshListener;
    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;

    public SmartPullToRefreshListener(android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener defaultOnRefreshListener) {
        this.defaultOnRefreshListener = defaultOnRefreshListener;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onRefresh() {
        if (defaultOnRefreshListener != null) {
            defaultOnRefreshListener.onRefresh();
        }
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            final View rootView = viewRootDatas.get(viewRootDatas.size() - 1).getView();
            int[] coords = new int[2];
            swipeRefreshLayout.getLocationOnScreen(coords);
            SmartView smartView = new SmartView(swipeRefreshLayout, coords);
            final SmartEvent smartEvent = new SmartEvent(smartView, rootView, -1, -1, "refresh", "down");
            EventQueue.getInstance().cancel(SmartSimpleGestureAnalyser.cancelable).insert(new Runnable() {
                @Override
                public void run() {
                    AutoTracker.getInstance().getSmartSender().sendMessage(SocketEmitterMessages.Event(smartEvent));
                }
            });
        }
    }
}
