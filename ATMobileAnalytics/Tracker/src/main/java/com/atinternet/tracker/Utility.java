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

import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Object> toFlatten(Map<String, Object> src) {
        Map<String, Object> dst = new HashMap<>();
        doFlatten(src, "", dst);
        return dst;
    }

    public static Map<String, Object> toObject(Map<String, Object> flattened) {
        Map<String, Object> unflattened = new HashMap<>();
        for (String key : flattened.keySet()) {
            doUnflatten(unflattened, key, flattened.get(key));
        }
        return unflattened;
    }

    private static void doFlatten(Map<String, Object> src, String prefix, Map<String, Object> dst) {
        for (Map.Entry<String, Object> e : src.entrySet()) {
            Object value = e.getValue();
            String key = TextUtils.isEmpty(prefix) ? e.getKey() : prefix + "_" + e.getKey();
            if (value instanceof Map) {
                doFlatten((Map<String, Object>) value, key, dst);
            } else {
                dst.put(key, value);
            }
        }
    }

    private static void doUnflatten(Map<String, Object> current, String key, Object originalValue) {

        String[] parts = key.split("_");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == (parts.length - 1)) {
                if (current.containsKey(part)) {
                    Map<String, Object> m = new HashMap<>((Map<String, Object>) current.get(part));
                    m.put("$", originalValue);
                    originalValue = m;
                }
                current.put(part, originalValue);
                return;
            }

            Map<String, Object> nestedMap;
            Object v = current.get(part);
            if (v != null && !(v instanceof Map)) {
                Map<String, Object> m = new HashMap<>();
                m.put("$", v);
                current.put(part, m);
            }
            nestedMap = (Map<String, Object>) current.get(part);
            if (nestedMap == null) {
                nestedMap = new HashMap<>();
                current.put(part, nestedMap);
            }

            current = nestedMap;
        }
    }
}