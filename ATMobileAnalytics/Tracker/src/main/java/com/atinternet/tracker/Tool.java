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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static com.atinternet.tracker.Tracker.*;
import static com.atinternet.tracker.TrackerListener.HitStatus;

/**
 * Class to stock tool functions
 */
class Tool {

    enum CallbackType {
        firstLaunch, build, send, partner, warning, save, error
    }

    /**
     * Encoding a string
     *
     * @param s String
     * @return String
     */
    static String percentEncode(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s.replace("+", "%20")
                .replace("*", "%2A")
                .replace("-", "%2D")
                .replace(".", "%2E")
                .replace("_", "%5F");
    }

    /**
     * Decoding a string
     *
     * @param s String
     * @return String
     */
    static String percentDecode(String s) {
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Remove characters into a string
     *
     * @param s               String
     * @param charTobeRemoved String...
     * @return String
     */
    static String removeCharacters(String s, String... charTobeRemoved) {
        String result = s;
        for (String c : charTobeRemoved) {
            result = result.replace(c, "");
        }
        return result;
    }

    /**
     * Convert object to string
     *
     * @param value     Object
     * @param separator String
     * @return String
     */
    static String convertToString(Object value, String separator) {
        separator = (!TextUtils.isEmpty(separator)) ? separator : ",";
        String result = "";
        boolean isFirst = true;

        if (value != null) {
            if (value instanceof List) {
                List<Object> listResult = (List<Object>) value;
                for (Object object : listResult) {
                    value = object;
                    if (isFirst) {
                        result = convertToString(value, separator);
                        isFirst = false;
                    } else {
                        result += separator + convertToString(value, separator);
                    }
                }
            } else if (value instanceof Object[]) {
                Object[] objects = (Object[]) value;
                value = Arrays.asList(objects);
                return convertToString(value, separator);
            } else if (value instanceof Map) {
                result = new JSONObject((Map) value).toString();
            } else {
                result = String.valueOf(value);
            }
        }

        return result;
    }

    /**
     * Find parameter position and the array index
     *
     * @param searchKey  String
     * @param parameters ArrayList<Param>
     * @return ArrayList
     */
    @SafeVarargs
    static ArrayList<int[]> findParameterPosition(String searchKey, ArrayList<Param>... parameters) {
        ArrayList<int[]> indexes = new ArrayList<int[]>();
        int indexValue = 0;
        int idArray = 0;

        for (ArrayList<Param> array : parameters) {
            for (Param p : array) {
                if (p.getKey().equals(searchKey)) {
                    indexes.add(new int[]{idArray, indexValue});
                }
                indexValue++;
            }
            idArray++;
            indexValue = 0;
        }

        return indexes;
    }

    /**
     * Get the timestamp
     *
     * @return Closure
     */
    static Closure getTimeStamp() {
        return new Closure() {
            @Override
            public String execute() {

                double result = System.currentTimeMillis() / 1000.0;
                long d = (long) result;
                String afterZero = Double.toString(result - d);

                return afterZero.length() > 1 ? Long.toString(d) + afterZero.substring(1) : "";
            }
        };
    }

    static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & SCREENLAYOUT_SIZE_MASK) >= SCREENLAYOUT_SIZE_LARGE;
    }


    static OfflineMode convertStringToOfflineMode(String offlineMode) {
        if (offlineMode.equals("always")) {
            return OfflineMode.always;
        } else if (offlineMode.equals("never")) {
            return OfflineMode.never;
        } else {
            return OfflineMode.required;
        }
    }

    /**
     * Function to execute callback
     *
     * @param trackerListener TrackerListener
     * @param callbackType    CallbackType
     * @param message         String
     * @param hitStatuses     HitStatus
     */
    static void executeCallback(TrackerListener trackerListener, CallbackType callbackType, String message, HitStatus... hitStatuses) {
        if (trackerListener != null) {
            switch (callbackType) {
                case firstLaunch:
                    trackerListener.trackerNeedsFirstLaunchApproval(message);
                    break;
                case build:
                    trackerListener.buildDidEnd(hitStatuses[0], message);
                    break;
                case send:
                    trackerListener.sendDidEnd(hitStatuses[0], message);
                    break;
                case partner:
                    trackerListener.didCallPartner(message);
                    break;
                case warning:
                    trackerListener.warningDidOccur(message);
                    break;
                case save:
                    trackerListener.saveDidEnd(message);
                    break;
                default://error
                    trackerListener.errorDidOccur(message);
                    break;
            }
        }
    }

    /**
     * Get days count between two millis time
     *
     * @param latestTimeMillis long
     * @param oldestTimeMillis long
     * @return int
     */
    static int getDaysBetweenTimes(long latestTimeMillis, long oldestTimeMillis) {
        return (int) TimeUnit.DAYS.convert((latestTimeMillis - oldestTimeMillis), TimeUnit.MILLISECONDS);
    }

    /**
     * Get minutes count between two millis time
     *
     * @param latestTimeMillis long
     * @param oldestTimeMillis long
     * @return int
     */
    static int getMinutesBetweenTimes(long latestTimeMillis, long oldestTimeMillis) {
        return (int) TimeUnit.MINUTES.convert((latestTimeMillis - oldestTimeMillis), TimeUnit.MILLISECONDS);
    }

    /**
     * Get seconds count between two millis time
     *
     * @param latestTimeMillis long
     * @param oldestTimeMillis long
     * @return int
     */
    static int getSecondsBetweenTimes(long latestTimeMillis, long oldestTimeMillis) {
        return (int) TimeUnit.SECONDS.convert((latestTimeMillis - oldestTimeMillis), TimeUnit.MILLISECONDS);
    }

    /**
     * Add 0 to have length (mh parameter)
     *
     * @param s      String
     * @param length int
     * @return String
     */
    static String formatNumberLength(String s, int length) {
        String result = "";
        for (int i = s.length(); i < length; i++) {
            result += "0";
        }
        return result + s;
    }

    /**
     * Convert String to JSON
     *
     * @param s String
     * @return Object
     */
    static Object parseJSON(String s) {
        try {
            return new JSONObject(s);
        } catch (JSONException e) {
            try {
                return new JSONArray(s);
            } catch (JSONException e1) {
                return null;
            }
        }
    }

    /**
     * Convert a JSONObject to Map
     *
     * @param jsonObject JSONObject
     * @return Map
     */
    static Map toMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = null;
            try {
                value = jsonObject.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * Return a appended String
     *
     * @param key              String
     * @param volatileParams   ArrayList<Param>
     * @param persistentParams ArrayList<Param>
     * @return String
     */
    static String appendParameterValues(String key, ArrayList<Param> volatileParams, ArrayList<Param> persistentParams) {
        ArrayList<int[]> indexPositions = Tool.findParameterPosition(key, volatileParams, persistentParams);
        boolean isFirst = true;
        String result = "";
        for (int[] index : indexPositions) {
            Param param = index[0] == 0 ? volatileParams.get(index[1]) : persistentParams.get(index[1]);
            if (isFirst) {
                result = param.getValue().execute();
                isFirst = false;
            } else if (param.getOptions() != null) {
                result += param.getOptions().getSeparator() + param.getValue().execute();
            } else {
                result += "," + param.getValue().execute();
            }
        }
        return result;
    }

    /**
     * SHA-256 hash
     *
     * @param s String
     * @return String
     */
    static String SHA_256(String s) {
        String baseString = "AT" + s;
        String result = "";
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-256");
            md.update(baseString.getBytes());

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) {
                    result += "0";
                }
                result += hex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Resize an image
     *
     * @param imageID int
     * @param context Context
     * @param width   int
     * @param height  int
     * @return Drawable
     */
    static Drawable getResizedImage(int imageID, Context context, int width, int height) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), imageID);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

    /**
     * Get color from resources
     *
     * @param context Context
     * @param colorId int
     * @return int
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("deprecation")
    static int getColor(Context context, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorId, null);
        } else {
            return context.getResources().getColor(colorId);
        }
    }
}
