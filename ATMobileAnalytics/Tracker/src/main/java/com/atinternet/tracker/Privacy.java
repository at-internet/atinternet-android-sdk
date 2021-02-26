package com.atinternet.tracker;

import android.content.SharedPreferences;
import android.text.TextUtils;
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
        OptOut("optout"),
        OptIn("optin"),
        NoConsent("no-consent"),
        Exempt("exempt"),
        None(null);

        private final String str;

        VisitorMode(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private static final String wildcard = "*";

    private static final int defaultDuration = 397;

    private static final Map<String, Set<String>> includeBufferByMode = initIncludeBufferByModeMap();

    private static final Map<String, Boolean> visitorConsentByMode = initVisitorConsentByModeMap();

    private static final Map<String, String> userIdByMode = initUserIdByModeMap();

    private static final List<String> specificKeys = new ArrayList<>(Arrays.asList("stc", "events", "context"));

    private static final List<String> JSONParameters = new ArrayList<>(Arrays.asList("events", "context"));

    private static Map<String, Set<String>> initIncludeBufferByModeMap() {
        Map<String, Set<String>> m = new HashMap<>();
        m.put(VisitorMode.None.name(), new HashSet<>(Collections.singletonList("*")));
        m.put(VisitorMode.OptIn.name(), new HashSet<>(Collections.singletonList("*")));
        m.put(VisitorMode.OptOut.name(), new HashSet<>(Arrays.asList("idclient", "ts", "olt", "cn", "click", "type")));
        m.put(VisitorMode.NoConsent.name(), new HashSet<>(Arrays.asList("idclient", "ts", "olt", "cn", "click", "type")));
        m.put(VisitorMode.Exempt.name(), new HashSet<>(Arrays.asList("idclient", "p", "olt", "vtag", "ptag", "ts", "click", "type", "cn", "dg", "apvr", "mfmd", "model", "manufacturer", "os", "stc_crash_*")));
        return m;
    }

    private static Map<String, Boolean> initVisitorConsentByModeMap() {
        Map<String, Boolean> m = new HashMap<>();
        m.put(VisitorMode.None.name(), true);
        m.put(VisitorMode.OptIn.name(), true);
        m.put(VisitorMode.OptOut.name(), false);
        m.put(VisitorMode.NoConsent.name(), false);
        m.put(VisitorMode.Exempt.name(), false);
        return m;
    }

    private static Map<String, String> initUserIdByModeMap() {
        Map<String, String> m = new HashMap<>();
        m.put(VisitorMode.None.name(), null);
        m.put(VisitorMode.OptIn.name(), null);
        m.put(VisitorMode.OptOut.name(), "opt-out");
        m.put(VisitorMode.NoConsent.name(), "Consent-NO");
        m.put(VisitorMode.Exempt.name(), null);
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
        setVisitorMode(visitorMode, defaultDuration);
    }

    /***
     * Set User Privacy mode
     * @param visitorMode selected mode from user context
     * @param duration storage validity for privacy information (in days)
     */
    public static void setVisitorMode(VisitorMode visitorMode, int duration) {
        Boolean visitorConsent = visitorConsentByMode.get(visitorMode.name());
        setVisitorMode(visitorMode.name(), visitorConsent, userIdByMode.get(visitorMode.name()), duration);
    }

    /***
     * Set User Privacy custom mode
     * @param visitorMode selected mode from user context
     * @param visitorConsent visitor consent to tracking
     * @param customUserIdValue optional custom user id
     */
    public static void setVisitorMode(String visitorMode, boolean visitorConsent, String customUserIdValue) {
        setVisitorMode(visitorMode, visitorConsent, customUserIdValue, defaultDuration);
    }

    /***
     * Set User Privacy custom mode
     * @param visitorMode selected mode from user context
     * @param visitorConsent visitor consent to tracking
     * @param customUserIdValue optional custom user id
     * @param duration storage validity for privacy information (in days)
     */
    public static void setVisitorMode(String visitorMode, boolean visitorConsent, String customUserIdValue, int duration) {
        if (TextUtils.isEmpty(visitorMode)) {
            return;
        }

        SharedPreferences.Editor editor = Tracker.getPreferences().edit();
        if (!(visitorMode.equals(VisitorMode.None.name()) || visitorMode.equals(VisitorMode.OptIn.name()))) {
            editor.remove(TrackerConfigurationKeys.VISITOR_NUMERIC)
                    .remove(TrackerConfigurationKeys.VISITOR_CATEGORY)
                    .remove(TrackerConfigurationKeys.VISITOR_TEXT);
        }

        editor.putString(TrackerConfigurationKeys.PRIVACY_MODE, visitorMode)
                .putLong(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP, Utility.currentTimeMillis() + (duration * 86_400_000L)) /// days to millis
                .putBoolean(TrackerConfigurationKeys.PRIVACY_VISITOR_CONSENT, visitorConsent)
                .putString(TrackerConfigurationKeys.PRIVACY_USER_ID, customUserIdValue)
                .apply();
    }

    /***
     * Get current User Privacy mode
     * @return user privacy mode
     * @deprecated Since 2.21.0, use getVisitorModeString() instead
     */
    @Deprecated
    public static VisitorMode getVisitorMode() {
        try {
            return VisitorMode.valueOf(getVisitorModeString());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /***
     * Get current User Privacy mode
     * @return user privacy mode
     */
    public static String getVisitorModeString() {
        SharedPreferences prefs = Tracker.getPreferences();
        long privacyModeExpirationTs = prefs.getLong(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP, -1);
        if (Utility.currentTimeMillis() >= privacyModeExpirationTs) {
            prefs.edit()
                    .remove(TrackerConfigurationKeys.PRIVACY_MODE)
                    .remove(TrackerConfigurationKeys.PRIVACY_MODE_EXPIRATION_TIMESTAMP)
                    .remove(TrackerConfigurationKeys.PRIVACY_USER_ID)
                    .remove(TrackerConfigurationKeys.PRIVACY_VISITOR_CONSENT)
                    .apply();
        }
        return prefs.getString(TrackerConfigurationKeys.PRIVACY_MODE, VisitorMode.None.name());
    }


    public static void extendIncludeBuffer(String... keys) {
        extendIncludeBufferForVisitorMode(getVisitorModeString(), keys);
    }

    public static void extendIncludeBuffer(VisitorMode visitorMode, String... keys) {
        extendIncludeBufferForVisitorMode(visitorMode.name(), keys);
    }

    /***
     * Extend include buffer for visitor mode set in parameter
     * @param visitorMode selected mode from user context
     * @param keys keys to include in the hit
     */
    public static void extendIncludeBufferForVisitorMode(String visitorMode, String... keys) {
        if (TextUtils.isEmpty(visitorMode)) {
            return;
        }

        List<String> lowercaseKeys = new ArrayList<>();
        for (String k : keys) {
            lowercaseKeys.add(k.toLowerCase());
        }

        if (includeBufferByMode.get(visitorMode) == null) {
            includeBufferByMode.put(visitorMode, new HashSet<>(includeBufferByMode.get(VisitorMode.OptOut.name())));
        }
        includeBufferByMode.get(visitorMode).addAll(lowercaseKeys);
    }

    static LinkedHashMap<String, Pair<String, String>> apply(LinkedHashMap<String, Pair<String, String>> parameters) {
        String currentPrivacyMode = getVisitorModeString();
        Set<String> includeBufferKeys = includeBufferByMode.get(currentPrivacyMode);
        if (includeBufferKeys == null) {
            includeBufferKeys = includeBufferByMode.get(VisitorMode.OptOut.name());
        }

        LinkedHashMap<String, Pair<String, String>> result = new LinkedHashMap<>();
        Map<String, List<String>> specificIncludedKeys = new HashMap<>();

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

        if (!currentPrivacyMode.equals(VisitorMode.None.name())) {
            VisitorMode mode = null;
            if (currentPrivacyMode.equals(VisitorMode.OptIn.name())) {
                mode = VisitorMode.OptIn;
            } else if (currentPrivacyMode.equals(VisitorMode.OptOut.name())) {
                mode = VisitorMode.OptOut;
            } else if (currentPrivacyMode.equals(VisitorMode.NoConsent.name())) {
                mode = VisitorMode.NoConsent;
            } else if (currentPrivacyMode.equals(VisitorMode.Exempt.name())) {
                mode = VisitorMode.Exempt;
            }

            String visitorModeValue;
            String visitorConsentValue;
            String userIdValue;
            if (mode != null) {
                visitorModeValue = mode.stringValue();
                visitorConsentValue = visitorConsentByMode.get(mode.name()) ? "1" : "0";
                userIdValue = userIdByMode.get(mode.name());
            } else {
                /// CUSTOM
                SharedPreferences prefs = Tracker.getPreferences();
                visitorModeValue = prefs.getString(TrackerConfigurationKeys.PRIVACY_MODE, null);
                visitorConsentValue = prefs.getBoolean(TrackerConfigurationKeys.PRIVACY_VISITOR_CONSENT, true) ? "1" : "0";
                userIdValue = prefs.getString(TrackerConfigurationKeys.PRIVACY_USER_ID, null);
            }

            if (visitorModeValue != null) {
                result.put("vm", new Pair<>(String.format("&vm=%s", visitorModeValue), ","));
                result.put("vc", new Pair<>(String.format("&vc=%s", visitorConsentValue), ","));
                if (userIdValue != null) {
                    result.put("idclient", new Pair<>(String.format("&idclient=%s", userIdValue), ","));
                }
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
