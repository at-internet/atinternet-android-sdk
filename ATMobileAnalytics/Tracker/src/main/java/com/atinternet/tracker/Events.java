/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Events extends BusinessObject {

    private final List<Event> eventLists;

    Events(Tracker tracker) {
        super(tracker);
        eventLists = new ArrayList<>();
    }

    /***
     * Add an event
     * @param name String
     * @param dataObject Map
     * @return Event
     */
    public Event add(String name, Map<String, Object> dataObject) {
        return add(new Event(name).setData(dataObject));
    }

    /***
     * Add an event
     * @param e Event
     * @return Event
     */
    public Event add(Event e) {
        eventLists.add(e);
        tracker.getBusinessObjects().put(getId(), this);
        return e;
    }

    /**
     * Send all events stored
     */
    public void send() {
        tracker.getDispatcher().dispatch(this);
    }

    @Override
    void setParams() {
        try {
            JSONArray eventsArray = new JSONArray();

            for (Event e : eventLists) {

                Map<String, Object[]> data = toFlatten(e.getData(), true);

                if (data.size() != 0) {
                    eventsArray.put(new JSONObject()
                            .put("name", e.getName().toLowerCase())
                            .put("data", new JSONObject(toObject(data))));
                }

                List<Event> additionalEvents = e.getAdditionalEvents();

                for (Event ev : additionalEvents) {
                    data = toFlatten(ev.getData(), true);
                    eventsArray.put(new JSONObject()
                            .put("name", ev.getName().toLowerCase())
                            .put("data", new JSONObject(toObject(data))));
                }
            }

            eventLists.clear();

            tracker.setParam("col", "2").setParam("events", eventsArray.toString(), new ParamOption().setEncode(true));

            /// Context
            Map<String, Object> pageContext = getPageContext();
            if (pageContext.size() != 0) {
                JSONArray contextArray = new JSONArray();
                contextArray.put(new JSONObject().put("data", new JSONObject(pageContext)));
                tracker.setParam("context", contextArray.toString(), new ParamOption().setEncode(true));
            }

        } catch (JSONException e1) {
            Tool.executeCallback(tracker.getListener(), Tool.CallbackType.BUILD, "error on create events list : " + e1, TrackerListener.HitStatus.Failed);
            tracker.setParam("events", String.valueOf(eventLists), new ParamOption().setEncode(true));
            eventLists.clear();
        }
    }

    private Map<String, Object> getPageContext() {
        Map<String, Object> pageContext = new HashMap<>();

        /// Page
        String s = TechnicalContext.getScreenName();
        if (s != null) {
            Map<String, Object> pageObj = new HashMap<>();
            String[] splt = s.split("::");
            pageObj.put("$", splt[splt.length - 1]);
            for (int i = 0; i < splt.length - 1; i++) {
                pageObj.put("chapter" + (i + 1), splt[i]);
            }
            pageContext.put("page", pageObj);
        }

        /// Level2
        String level2 = TechnicalContext.getLevel2();
        if (level2 != null) {
            Map<String, Object> siteObj = new HashMap<>();
            int level2Int = Utility.parseInt(level2, -1);
            if (level2Int >= 0 && TechnicalContext.isIsLevel2Int()) {
                siteObj.put("level2_id", level2Int);
            } else {
                siteObj.put("level2", level2);
            }
            pageContext.put("site", siteObj);
        }

        return pageContext;
    }

    private Map<String, Object[]> toFlatten(Map<String, Object> src, boolean lowercase) {
        Map<String, Object[]> dst = new HashMap<>();
        doFlatten(src, "", dst, lowercase);
        return dst;
    }

    private Map<String, Object> toObject(Map<String, Object[]> flattened) {
        Map<String, Object> unflattened = new HashMap<>();
        for (String key : flattened.keySet()) {
            doUnflatten(unflattened, key, flattened.get(key));
        }
        return unflattened;
    }

    private void doFlatten(Map<String, Object> src, String prefix, Map<String, Object[]> dst, boolean lowercase) {
        for (Map.Entry<String, Object> e : src.entrySet()) {
            Object value = e.getValue();
            String completeKey = TextUtils.isEmpty(prefix) ? e.getKey() : prefix + "_" + e.getKey();
            if (value instanceof Map) {
                doFlatten((Map<String, Object>) value, completeKey, dst, lowercase);
            } else {
                String[] parts = completeKey.split("_");
                String finalPrefix = "";
                StringBuilder sb = new StringBuilder();
                int last = parts.length - 1;

                for (int i = 0; i < parts.length; i++) {
                    String part = parts[i];

                    String[] splt = splitPrefixKey(part);
                    String keyPrefix = splt[0];
                    String key = splt[1];

                    if (!TextUtils.isEmpty(keyPrefix)) {
                        finalPrefix = keyPrefix;
                    }
                    if (i != 0) {
                        sb.append('_');
                    }
                    sb.append(lowercase ? key.toLowerCase() : key);

                    /// test -> test_$ on existing key if the current key is not complete
                    String s = sb.toString();
                    if (i != last && dst.containsKey(s)) {
                        dst.put(s + "_$", dst.remove(s));
                        continue;
                    }
                    ///

                    /// test -> test_$ on current key if the current key is complete
                    if (i == last && !dst.containsKey(s) && containsKeyPrefix(dst.keySet(), s)) {
                        sb.append("_$");
                    }
                    ///

                }
                dst.put(sb.toString(), new Object[]{value, lowercase ? finalPrefix.toLowerCase() : finalPrefix});
            }
        }
    }

    private void doUnflatten(Map<String, Object> current, String key, Object[] originalValueWithPrefix) {

        String[] parts = key.split("_");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == (parts.length - 1)) {
                current.put(originalValueWithPrefix[1] + part, originalValueWithPrefix[0]);
                return;
            }

            Map<String, Object> nestedMap = (Map<String, Object>) current.get(part);
            if (nestedMap == null) {
                nestedMap = new HashMap<>();
                current.put(part, nestedMap);
            }

            current = nestedMap;
        }
    }

    private String[] splitPrefixKey(String key) {
        if (key.length() < 2 || key.charAt(1) != ':') {
            return new String[]{"", key};
        }

        if (key.length() < 4 || key.charAt(3) != ':') {
            return new String[]{key.substring(0, 2), key.substring(2)};
        }

        return new String[]{key.substring(0, 4), key.substring(4)};
    }

    private boolean containsKeyPrefix(Set<String> keys, String prefix) {
        for (String key : keys) {
            if (key.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
