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

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;

class Dispatcher {

    private final Tracker tracker;

    Dispatcher(Tracker tracker) {
        this.tracker = tracker;
    }

    public void dispatch(BusinessObject... businessObjects) {
        ArrayList<BusinessObject> trackerObjects;
        for (BusinessObject businessObject : businessObjects) {
            businessObject.setEvent();

            if (businessObject instanceof AbstractScreen) {
                trackerObjects = new ArrayList<BusinessObject>() {{
                    addAll(tracker.getBusinessObjects().values());
                }};

                boolean hasOrder = false;

                for (BusinessObject object : trackerObjects) {
                    if (((object instanceof OnAppAd && ((OnAppAd) object).getAction() == OnAppAd.Action.View)
                            || object instanceof Order
                            || object instanceof InternalSearch
                            || object instanceof ScreenInfo) &&
                            object.getTimestamp() < businessObject.getTimestamp()) {

                        if (object instanceof Order) {
                            hasOrder = true;
                        }

                        object.setEvent();
                        tracker.getBusinessObjects().remove(object.getId());
                    }
                }

                if (tracker.Cart().getCartId() != null && (((Screen) businessObject).isBasketScreen() || hasOrder)) {
                    tracker.Cart().setEvent();
                }
            } else if (businessObject instanceof Gesture) {
                trackerObjects = new ArrayList<BusinessObject>() {{
                    addAll(tracker.getBusinessObjects().values());
                }};

                if (((Gesture) businessObject).getAction() == Gesture.Action.InternalSearch) {
                    for (BusinessObject object : trackerObjects) {
                        if ((object instanceof InternalSearch) && object.getTimestamp() < businessObject.getTimestamp()) {
                            object.setEvent();
                            tracker.getBusinessObjects().remove(object.getId());
                        }
                    }
                }
            }

            tracker.getBusinessObjects().remove(businessObject.getId());
            trackerObjects = new ArrayList<BusinessObject>() {{
                addAll(tracker.getBusinessObjects().values());
            }};

            for (BusinessObject object : trackerObjects) {
                if ((object instanceof CustomObject || object instanceof NuggAd) && object.getTimestamp() < businessObject.getTimestamp()) {
                    object.setEvent();
                    tracker.getBusinessObjects().remove(object.getId());
                }
            }
        }

        if (Hit.getHitType(tracker.getBuffer().getVolatileParams(), tracker.getBuffer().getPersistentParams()) == Hit.HitType.Screen) {
            TechnicalContext.screenName = Tool.appendParameterValues(Hit.HitParam.Screen.stringValue(), tracker.getBuffer().getVolatileParams(), tracker.getBuffer().getPersistentParams());
            CrashDetectionHandler.setCrashLastScreen(TechnicalContext.screenName);

            String level2 = Tool.appendParameterValues(Hit.HitParam.Level2.stringValue(), tracker.getBuffer().getVolatileParams(), tracker.getBuffer().getPersistentParams());
            TechnicalContext.level2 = (level2 != null && !level2.isEmpty()) ? Integer.parseInt(level2) : 0;

            SharedPreferences preferences = tracker.getPreferences();
            if (!preferences.getBoolean(TrackerKeys.CAMPAIGN_ADDED_KEY, false)) {
                String xtor = preferences.getString(TrackerKeys.MARKETING_CAMPAIGN_SAVED, null);
                if (xtor != null) {
                    ParamOption beforeStcPositionWithEncoding = new ParamOption()
                            .setRelativePosition(ParamOption.RelativePosition.before)
                            .setRelativeParameterKey(Hit.HitParam.JSON.stringValue())
                            .setEncode(true);

                    if (preferences.getBoolean(TrackerKeys.IS_FIRST_AFTER_INSTALL_HIT_KEY, true)) {
                        tracker.setParam(Hit.HitParam.Source.stringValue(), xtor, beforeStcPositionWithEncoding);
                        preferences.edit().putBoolean(TrackerKeys.IS_FIRST_AFTER_INSTALL_HIT_KEY, false).apply();
                    } else {
                        tracker.setParam(Hit.HitParam.RemanentSource.stringValue(), xtor, beforeStcPositionWithEncoding);
                    }
                    preferences.edit().putBoolean(TrackerKeys.CAMPAIGN_ADDED_KEY, true).apply();
                }
            }
        }

        setIdentifiedVisitorInfos();

        ParamOption appendWithEncoding = new ParamOption().setAppend(true).setEncode(true);
        tracker.setParam(Hit.HitParam.JSON.stringValue(), LifeCycle.getMetrics(tracker.getPreferences()), appendWithEncoding);
        if ((Boolean) tracker.getConfiguration().get(TrackerKeys.ENABLE_CRASH_DETECTION)) {
            tracker.setParam(Hit.HitParam.JSON.stringValue(), CrashDetectionHandler.getCrashInformation(), appendWithEncoding);
        }

        String referrer = tracker.getPreferences().getString(TrackerKeys.REFERRER, null);
        if(!TextUtils.isEmpty(referrer)){
            tracker.setParam("refstore", referrer);
            tracker.getPreferences().edit().putString(TrackerKeys.REFERRER, null).apply();
        }

        Builder builder = new Builder(tracker);
        tracker.getBuffer().getVolatileParams().clear();
        TrackerQueue.getInstance().put(builder);

        tracker.Context().setLevel2(tracker.Context().getLevel2());
    }

    /**
     * Add identified visitor infos
     */
    void setIdentifiedVisitorInfos() {
        if ((Boolean) tracker.getConfiguration().get(TrackerKeys.PERSIST_IDENTIFIED_VISITOR)) {
            ParamOption beforeStcPosition = new ParamOption()
                    .setRelativePosition(ParamOption.RelativePosition.before)
                    .setRelativeParameterKey(Hit.HitParam.JSON.stringValue());

            ParamOption beforeStcPositionWithEncoding = new ParamOption()
                    .setRelativePosition(ParamOption.RelativePosition.before)
                    .setRelativeParameterKey(Hit.HitParam.JSON.stringValue()).setEncode(true);

            String visitorNumericID = tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_NUMERIC, null);
            String visitorCategory = tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_CATEGORY, null);
            String visitorTextID = tracker.getPreferences().getString(IdentifiedVisitor.VISITOR_TEXT, null);
            if (visitorNumericID != null) {
                tracker.setParam(Hit.HitParam.VisitorIdentifierNumeric.stringValue(), visitorNumericID, beforeStcPosition);
            }
            if (visitorTextID != null) {
                tracker.setParam(Hit.HitParam.VisitorIdentifierText.stringValue(), visitorTextID, beforeStcPositionWithEncoding);
            }
            if (visitorCategory != null) {
                tracker.setParam(Hit.HitParam.VisitorCategory.stringValue(), visitorCategory, beforeStcPosition);
            }
        }
    }
}
