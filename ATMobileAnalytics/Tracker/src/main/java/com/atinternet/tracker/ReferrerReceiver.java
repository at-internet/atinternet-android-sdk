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

import android.content.BroadcastReceiver;
import android.content.Intent;

public class ReferrerReceiver extends BroadcastReceiver {

    /**
     * Constant String
     */
    private static final String REFERRER_INTENT_ACTION = "com.android.vending.INSTALL_REFERRER";
    private static final String REFERRER = "referrer";

    @Override
    public void onReceive(android.content.Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(REFERRER_INTENT_ACTION)) {
                String referrer = intent.getStringExtra(REFERRER);
                if (referrer != null) {
                    referrer = Tool.percentDecode(referrer);
                    String[] campaignParams = referrer.split("&");
                    for (String param : campaignParams) {
                        String[] paramComponents = param.split("=");
                        String key = paramComponents[0];
                        if (key.equals(Hit.HitParam.RemanentSource.stringValue())) {
                            context.getSharedPreferences(TrackerKeys.PREFERENCES, android.content.Context.MODE_PRIVATE)
                                    .edit().putString(TrackerKeys.MARKETING_CAMPAIGN_SAVED, paramComponents[1])
                                    .putLong(TrackerKeys.LAST_MARKETING_CAMPAIGN_TIME, System.currentTimeMillis())
                                    .apply();
                        }
                    }
                }
            }
        }
    }
}
