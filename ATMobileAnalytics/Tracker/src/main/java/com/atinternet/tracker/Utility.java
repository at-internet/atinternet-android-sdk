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

public class Utility {

    public static double parseDoubleFromString(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception ignored) {
            return 0.0;
        }
    }

    public static boolean parseBooleanFromString(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception ignored) {
            return false;
        }
    }

    public static int parseIntFromString(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ignored) {
            return 0;
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
}
