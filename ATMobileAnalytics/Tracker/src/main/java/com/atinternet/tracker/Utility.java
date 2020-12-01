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

import java.util.Calendar;
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
        do {
            timeMillis = System.currentTimeMillis();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeMillis);
            year = cal.get(Calendar.YEAR);
            if (year < 2000) {
                sleep();
            }
        } while (year < 2000);

        return timeMillis;
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}