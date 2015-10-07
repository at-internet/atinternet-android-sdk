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

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

class CrashDetectionHandler implements Thread.UncaughtExceptionHandler {

    /**
     * Key representing if the app crashed or not
     */
    private static final String CRASH_DETECTION = "CrashDetection";

    /**
     * Key representing the last track screen before crash
     */
    private static final String CRASH_LAST_SCREEN = "CrashLastScreen";

    /**
     * Key representing where the app crashed
     */
    private static final String CRASH_CLASS_CAUSE = "CrashClassCause";

    /**
     * Key representing why the app crashed
     */
    private static final String CRASH_EXCEPTION_NAME = "CrashExceptionName";

    /**
     * Default Handler to show dialog
     */
    private final Thread.UncaughtExceptionHandler defaultHandler;

    /**
     * Context
     */
    private final Context context;

    /**
     * Shared preferences
     */
    private static SharedPreferences preferences;

    /**
     * Last Track Screen
     */
    private static String lastScreen;

    /**
     * Set the last screen name
     *
     * @param name String
     */
    static void setCrashLastScreen(String name) {
        lastScreen = name;
    }


    /**
     * Default constructor
     *
     * @param context        Context
     * @param defaultHandler Thread.UncaughtExceptionHandler
     */
    public CrashDetectionHandler(Context context, Thread.UncaughtExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        this.context = context;
        preferences = context.getApplicationContext().getSharedPreferences(TrackerKeys.PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        String className = (throwable.getCause() != null) ? getClassNameException(throwable.getCause()) : getClassNameException(throwable);
        String exceptionName = (throwable.getCause() != null) ? throwable.getCause().getClass().getName() : throwable.getClass().getName();

        preferences.edit().putBoolean(CRASH_DETECTION, true)
                .putString(CRASH_LAST_SCREEN, lastScreen != null ? lastScreen : "")
                .putString(CRASH_CLASS_CAUSE, className)
                .putString(CRASH_EXCEPTION_NAME, exceptionName != null ? exceptionName : "")
                .apply();
        defaultHandler.uncaughtException(thread, throwable);
    }

    /**
     * Get a dictionary className : stackTrace
     *
     * @return Closure
     */
    public static Closure getCrashInformation() {
        return new Closure() {
            @Override
            public String execute() {
                boolean hasCrashed = preferences.getBoolean(CRASH_DETECTION, false);
                if (hasCrashed) {
                    final String lastScreen = preferences.getString(CRASH_LAST_SCREEN, "");
                    final String className = preferences.getString(CRASH_CLASS_CAUSE, "");
                    final String exceptionName = preferences.getString(CRASH_EXCEPTION_NAME, "");
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>() {{
                        put("lastscreen", lastScreen);
                        put("classname", className);
                        put("error", exceptionName);
                    }};
                    preferences.edit().putBoolean(CRASH_DETECTION, false).apply();
                    try {
                        return new JSONObject().put("crash", new JSONObject(map)).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return new JSONObject().toString();
            }
        };
    }

    /**
     * Get the class name where the exception thrown
     *
     * @param t Throwable
     * @return String
     */
    private String getClassNameException(Throwable t) {
        for (StackTraceElement element : t.getStackTrace()) {
            if (element.getClassName().contains(context.getApplicationContext().getPackageName())) {
                return element.getClassName();
            }
        }
        return "";
    }
}
