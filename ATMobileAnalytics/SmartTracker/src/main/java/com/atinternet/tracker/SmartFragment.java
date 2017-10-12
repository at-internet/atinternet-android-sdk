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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * /!\ SmartTracker only <br>
 * Abstract class to help auto tracking fragment navigation
 */
public abstract class SmartFragment extends android.support.v4.app.Fragment {

    /**
     * Abstract method to get the current fragment class : please use return this
     * @return the current fragment class
     */
    public abstract android.support.v4.app.Fragment getFragment();

    @Override
    public void onResume() {
        super.onResume();
        SmartContext.currentFragment = new WeakReference<>(getFragment());
        EventQueue.getInstance().cancel(SmartTrackerActivityLifecycle.cancelable).insert(new Runnable() {
            @Override
            public void run() {
                AutoTracker.getInstance().getSmartSender().sendMessage(SocketEmitterMessages.ViewDidAppear((SmartContext.currentFragment != null && SmartContext.currentFragment.get() != null) ? SmartContext.currentFragment.get().getClass().getSimpleName() : ""), false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {

        TechnicalContext.screenName = null;
        TechnicalContext.level2 = 0;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
