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

/**
 * Wrapper class for identified visitor tracking
 */
public class IdentifiedVisitor {

    static final String VISITOR_NUMERIC = "ATVisitorNumeric";
    static final String VISITOR_CATEGORY = "ATVisitorCategory";
    static final String VISITOR_TEXT = "ATVisitorText";

    private final Tracker tracker;
    private final boolean persistIdentifiedVisitor;
    private final ParamOption option = new ParamOption();

    IdentifiedVisitor(Tracker tracker) {
        this.tracker = tracker;
        persistIdentifiedVisitor = Boolean.parseBoolean(String.valueOf(tracker.getConfiguration().get(TrackerConfigurationKeys.PERSIST_IDENTIFIED_VISITOR)));
        option.setPersistent(true).setEncode(true);
    }

    /**
     * Set Identified visitor for the current session
     *
     * @param visitorId numeric identifier
     * @return Tracker instance
     */
    public Tracker set(long visitorId) {
        unset();
        save(Hit.HitParam.VisitorIdentifierNumeric.stringValue(), VISITOR_NUMERIC, String.valueOf(visitorId));

        return tracker;
    }

    /**
     * Set Identified visitor for the current session
     *
     * @param visitorId       numeric identifier
     * @param visitorCategory visitor category
     * @return Tracker instance
     */
    public Tracker set(long visitorId, int visitorCategory) {
        set(visitorId);
        save(Hit.HitParam.VisitorCategory.stringValue(), VISITOR_CATEGORY, String.valueOf(visitorCategory));

        return tracker;
    }

    /**
     * Set Identified visitor for the current session
     *
     * @param visitorId textual identifier
     * @return Tracker instance
     */
    public Tracker set(String visitorId) {
        unset();
        save(Hit.HitParam.VisitorIdentifierText.stringValue(), VISITOR_TEXT, visitorId);

        return tracker;
    }

    /**
     * Set Identified visitor for the current session
     *
     * @param visitorId       textual identifier
     * @param visitorCategory visitor category
     * @return Tracker instance
     */
    public Tracker set(String visitorId, int visitorCategory) {
        set(visitorId);
        save(Hit.HitParam.VisitorCategory.stringValue(), VISITOR_CATEGORY, String.valueOf(visitorCategory));

        return tracker;
    }

    /**
     * Unset identified visitor data. Identified visitor parameters won't appear in next hits
     */
    public void unset() {
        tracker.unsetParam(Hit.HitParam.VisitorIdentifierNumeric.stringValue());
        tracker.unsetParam(Hit.HitParam.VisitorIdentifierText.stringValue());
        tracker.unsetParam(Hit.HitParam.VisitorCategory.stringValue());
        Tracker.getPreferences().edit().putString(VISITOR_NUMERIC, null)
                .putString(VISITOR_CATEGORY, null)
                .putString(VISITOR_TEXT, null).apply();
    }

    private void save(String key, String preferencesKey, String value) {
        if (persistIdentifiedVisitor) {
            Tracker.getPreferences().edit().putString(preferencesKey, value).apply();
        } else {
            tracker.setParam(key, value, option);
        }
    }
}
