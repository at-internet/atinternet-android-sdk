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

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static String parseString(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        return String.valueOf(o);
    }

    public static double parseDouble(Object o) {
        if (o == null) {
            return 0.0;
        }
        if (o instanceof Double) {
            return (double) o;
        }
        try {
            return Double.parseDouble(parseString(o));
        } catch (Exception ignored) {
            return 0.0;
        }
    }

    public static boolean parseBoolean(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Boolean) {
            return (boolean) o;
        }
        try {
            return Boolean.parseBoolean(parseString(o));
        } catch (Exception ignored) {
            return false;
        }
    }

    public static int parseInt(Object o, int defaultValue) {
        if (o == null) {
            return defaultValue;
        }
        if (o instanceof Integer) {
            return (int) o;
        }
        if (o instanceof Double) {
            return ((Double) o).intValue();
        }
        try {
            return Integer.parseInt(parseString(o));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static String getStringOrEmpty(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    public static String stringJoin(char delimiter, String... strs) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : strs) {
            if (first) {
                first = false;
                sb.append(s);
            } else {
                sb.append(delimiter).append(s);
            }
        }

        return sb.toString();
    }

    /// Workaround to bypass the identified NTP issue on Android
    /// (https://stackoverflow.com/questions/45509101/system-currenttimemillis-returns-incorrect-timestamp-on-huawei)
    public static long currentTimeMillis() {
        long timeMillis;
        int year;
        int retry = 3;
        do {
            retry--;
            timeMillis = System.currentTimeMillis();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeMillis);
            year = cal.get(Calendar.YEAR);
            if (year < 2000) {
                sleep();
            }
        } while (year < 2000 && retry > 0);

        return timeMillis;
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static Map<String, Object[]> toFlatten(Map<String, Object> src, boolean lowercase) {
        Map<String, Object[]> dst = new HashMap<>();
        doFlatten(src, "", dst, lowercase);
        return dst;
    }

    static Map<String, Object> toObject(Map<String, Object[]> flattened) {
        Map<String, Object> unflattened = new HashMap<>();
        for (String key : flattened.keySet()) {
            doUnflatten(unflattened, key, flattened.get(key));
        }
        return unflattened;
    }

    private static void doFlatten(Map<String, Object> src, String prefix, Map<String, Object[]> dst, boolean lowercase) {
        for (Map.Entry<String, Object> e : src.entrySet()) {
            Object value = e.getValue();
            String completeKey = TextUtils.isEmpty(prefix) ? e.getKey() : prefix + "_" + e.getKey();
            if (value instanceof Map) {
                doFlatten((Map<String, Object>) value, completeKey, dst, lowercase);
            } else if (value instanceof JSONObject) {
                doFlatten(Tool.toMap((JSONObject) value), completeKey, dst, lowercase);
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
                    if (i == last && !dst.containsKey(s) && containsKeyPrefix(dst.keySet(), s + "_")) {
                        sb.append("_$");
                    }
                    ///

                }
                dst.put(sb.toString(), new Object[]{value, lowercase ? finalPrefix.toLowerCase() : finalPrefix});
            }
        }
    }

    private static void doUnflatten(Map<String, Object> current, String key, Object[] originalValueWithPrefix) {

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

    private static String[] splitPrefixKey(String key) {
        if (key.length() < 2 || key.charAt(1) != ':') {
            return new String[]{"", key};
        }

        if (key.length() < 4 || key.charAt(3) != ':') {
            return new String[]{key.substring(0, 2), key.substring(2)};
        }

        return new String[]{key.substring(0, 4), key.substring(4)};
    }

    private static boolean containsKeyPrefix(Set<String> keys, String prefix) {
        for (String key : keys) {
            if (key.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}