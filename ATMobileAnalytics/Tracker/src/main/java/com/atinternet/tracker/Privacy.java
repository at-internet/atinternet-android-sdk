package com.atinternet.tracker;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class Privacy {

    public enum VisitorMode {
        OptOut,
        OptIn,
        NoConsent,
        Exempt,
        None
    }

    private static final String wildcard = "*";

    private static final Map<VisitorMode, Set<String>> includeBufferByMode = createIncludeBufferByModeMap();

    private static final List<String> specificKeys = new ArrayList<>(Arrays.asList("stc", "events", "context"));

    private static final List<String> JSONParameters = new ArrayList<>(Arrays.asList("events", "context"));

    private static Map<VisitorMode, Set<String>> createIncludeBufferByModeMap() {
        Map<VisitorMode, Set<String>> m = new HashMap<>();
        m.put(VisitorMode.None, new HashSet<>(Collections.singletonList("*")));
        m.put(VisitorMode.OptIn, new HashSet<>(Collections.singletonList("*")));
        m.put(VisitorMode.OptOut, new HashSet<>(Arrays.asList("idclient", "ts", "olt", "cn", "click", "type")));
        m.put(VisitorMode.NoConsent, new HashSet<>(Arrays.asList("idclient", "ts", "olt", "cn", "click", "type")));
        m.put(VisitorMode.Exempt, new HashSet<>(Arrays.asList("idclient", "p", "olt", "vtag", "ptag", "ts", "click", "type", "cn", "apvr", "mfmd", "model", "manufacturer", "os", "stc_crash_*")));
        return m;
    }

    private Privacy() {

    }

    /***
     * Set user OptOut
     */
    public static void setVisitorOptOut() {
        setVisitorMode(VisitorMode.OptOut);
    }

    /***
     * Set user OptIn
     */
    public static void setVisitorOptIn() {
        setVisitorMode(VisitorMode.OptIn);
    }

    /***
     * Set User Privacy mode
     * @param visitorMode selected mode from user context
     */
    public static void setVisitorMode(VisitorMode visitorMode) {
        setVisitorMode(visitorMode, 397);
    }

    /***
     * Set User Privacy mode
     * @param visitorMode selected mode from user context
     * @param duration storage validity for privacy information (in days)
     */
    public static void setVisitorMode(VisitorMode visitorMode, int duration) {
        SharedPreferences.Editor editor = Tracker.getPreferences().edit();
        if (visitorMode != VisitorMode.OptIn && visitorMode != VisitorMode.None) {
            editor.putString(TrackerConfigurationKeys.VISITOR_NUMERIC, null)
                    .putString(TrackerConfigurationKeys.VISITOR_CATEGORY, null)
                    .putString(TrackerConfigurationKeys.VISITOR_TEXT, null);
        }

        editor.putString(TrackerConfigurationKeys.PRIVACY_MODE, visitorMode.name())
                .putLong(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP, Utility.currentTimeMillis() + (duration * 86_400_000L)) /// days to millis
                .apply();
    }

    /***
     * Get current User Privacy mode
     * @return user privacy mode
     */
    public static VisitorMode getVisitorMode() {
        SharedPreferences prefs = Tracker.getPreferences();
        long privacyModeExpirationTs = prefs.getLong(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP, -1);
        if (Utility.currentTimeMillis() >= privacyModeExpirationTs) {
            prefs.edit()
                    .putString(TrackerConfigurationKeys.PRIVACY_MODE, VisitorMode.None.name())
                    .putLong(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP, -1)
                    .apply();
        }
        return VisitorMode.valueOf(prefs.getString(TrackerConfigurationKeys.PRIVACY_MODE, VisitorMode.None.name()));
    }

    public static void extendIncludeBuffer(String... keys) {
        List<String> lowercaseKeys = new ArrayList<>();
        for (String k : keys) {
            lowercaseKeys.add(k.toLowerCase());
        }
        includeBufferByMode.get(getVisitorMode()).addAll(lowercaseKeys);
    }

    static LinkedHashMap<String, Pair<String, String>> apply(LinkedHashMap<String, Pair<String, String>> parameters) {
        VisitorMode currentPrivacyMode = getVisitorMode();
        Set<String> includeBufferKeys = includeBufferByMode.get(currentPrivacyMode);

        LinkedHashMap<String, Pair<String, String>> result = new LinkedHashMap<>();
        Map<String, List<String>> specificIncludedKeys = new HashMap<>();

        switch (currentPrivacyMode) {
            case OptIn:
                result.put("vc", new Pair<>("&vc=1", ","));
                result.put("vm", new Pair<>("&vm=optin", ","));
                break;
            case OptOut:
                result.put("vc", new Pair<>("&vc=0", ","));
                result.put("vm", new Pair<>("&vm=optout", ","));
                result.put("idclient", new Pair<>("&idclient=opt-out", ","));
                break;
            case NoConsent:
                result.put("vc", new Pair<>("&vc=0", ","));
                result.put("vm", new Pair<>("&vm=no-consent", ","));
                result.put("idclient", new Pair<>("&idclient=Consent-NO", ","));
                break;
            case Exempt:
                result.put("vc", new Pair<>("&vc=0", ","));
                result.put("vm", new Pair<>("&vm=exempt", ","));
                break;
            default: /// None
                break;
        }

        for (String key : includeBufferKeys) {
            key = key.toLowerCase();

            /// WILDCARD
            if (key.equals(wildcard)) {
                result.putAll(parameters);
                break;
            }

            /// SPECIFIC
            for (String specificKey : specificKeys) {
                if (!key.startsWith(specificKey)) {
                    continue;
                }

                if (!specificIncludedKeys.containsKey(specificKey)) {
                    specificIncludedKeys.put(specificKey, new ArrayList<String>());
                }

                specificIncludedKeys.get(specificKey).add(key);
                break;
            }

            Pair<String, String> value = parameters.get(key);
            if (value != null) {
                result.put(key, value);
            }
        }

        /// STC
        List<String> includedStcKeys = specificIncludedKeys.get("stc");
        if (includedStcKeys != null) {
            Pair<String, String> stc = parameters.get("stc");
            if (stc != null) {
                result.put("stc", applyToStc(stc, includedStcKeys));
            }
        }

        /// JSONParameter
        for (String jsonParameter : JSONParameters) {
            List<String> includeJSONParameterKeys = specificIncludedKeys.get(jsonParameter);
            if (includeJSONParameterKeys == null) {
                continue;
            }
            Pair<String, String> jsonParam = parameters.get(jsonParameter);
            if (jsonParam != null) {
                result.put(jsonParameter, applyToJSONParameter(jsonParameter, jsonParam, includeJSONParameterKeys));
            }
        }

        return result;
    }

    private static Pair<String, String> applyToStc(Pair<String, String> stc, List<String> includedStcKeys) {
        int equalsCharIndex = stc.first.indexOf('=');
        if (equalsCharIndex == -1) {
            return stc;
        }
        String value = Tool.percentDecode(stc.first.substring(equalsCharIndex + 1));
        try {
            Map<String, Object[]> stcFlattened = Utility.toFlatten(Tool.toMap(new JSONObject(value)), true);
            Map<String, Object[]> stcResult = new HashMap<>();
            for (String includeKey : includedStcKeys) {
                for (String stcKey : stcFlattened.keySet()) {
                    int wildcardIndex = includeKey.indexOf(wildcard);
                    String completeKey = "stc_" + stcKey;
                    if (wildcardIndex == -1) {
                        if (completeKey.equals(includeKey)) {
                            stcResult.put(stcKey, stcFlattened.get(stcKey));
                        }
                    } else if (completeKey.startsWith(includeKey.substring(0, wildcardIndex))) {
                        stcResult.put(stcKey, stcFlattened.get(stcKey));
                    }
                }
            }
            return new Pair<>("&stc=" + Tool.percentEncode(new JSONObject(Utility.toObject(stcResult)).toString()), stc.second);
        } catch (JSONException e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return stc;
    }

    private static Pair<String, String> applyToJSONParameter(String paramKey, Pair<String, String> param, List<String> includedKeys) {
        int equalsCharIndex = param.first.indexOf('=');
        if (equalsCharIndex == -1) {
            return param;
        }

        String value = Tool.percentDecode(param.first.substring(equalsCharIndex + 1));
        try {
            JSONArray array = new JSONArray(value);
            JSONArray arrayResult = new JSONArray();
            int arrayLength = array.length();
            for (int i = 0; i < arrayLength; i++) {
                Map<String, Object[]> objectFlattened = Utility.toFlatten(Tool.toMap(array.getJSONObject(i)), true);
                Map<String, Object[]> objectResult = new HashMap<>();
                for (String includeKey : includedKeys) {
                    for (String k : objectFlattened.keySet()) {
                        int wildcardIndex = includeKey.indexOf(wildcard);
                        String completeKey = paramKey + "_" + k;
                        if (wildcardIndex == -1) {
                            if (completeKey.equals(includeKey)) {
                                objectResult.put(k, objectFlattened.get(k));
                            }
                        } else if (completeKey.startsWith(includeKey.substring(0, wildcardIndex))) {
                            objectResult.put(k, objectFlattened.get(k));
                        }
                    }
                }
                arrayResult.put(new JSONObject(Utility.toObject(objectResult)));
            }
            return new Pair<>("&" + paramKey + "=" + Tool.percentEncode(arrayResult.toString()), param.second);
        } catch (JSONException e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return param;
    }
}
