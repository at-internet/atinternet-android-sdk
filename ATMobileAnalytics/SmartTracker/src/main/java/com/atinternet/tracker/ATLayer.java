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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

@SuppressLint("Instantiatable")
class ATLayer extends ATFrameLayout {

    private SmartSender smartSender;

    ATLayer(android.content.Context context, SmartSender smartSender) {
        super(context);

        this.setBackgroundColor(UI.getColor(context, android.R.color.transparent));
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.smartSender = smartSender;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        List<ViewRootData> viewRootDatas = ReflectionAPI.getViewRootDatas(currentActivity);
        if (!viewRootDatas.isEmpty()) {
            ViewRootData vrd = viewRootDatas.get(viewRootDatas.size() - 1);
            View rootView = vrd.getView();
            SmartView smartView;
            if ((smartView = UI.getTouchedView(rootView, Math.round(event.getRawX()), Math.round(event.getRawY()))) != null) {
                final SmartEvent smartEvent = new SmartEvent(smartView, rootView, event.getRawX(), event.getRawY(), "tap", "single");
                EventQueue.getInstance().insert(new Runnable() {
                    @Override
                    public void run() {
                        smartSender.sendMessage(SocketEmitterMessages.Event(smartEvent));
                    }
                });
            }
        }
        return super.dispatchTouchEvent(event);
    }
}