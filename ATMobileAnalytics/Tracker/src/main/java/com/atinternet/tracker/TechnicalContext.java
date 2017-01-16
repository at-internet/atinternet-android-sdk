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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.os.Build.BRAND;
import static android.os.Build.MODEL;
import static android.provider.Settings.Secure.ANDROID_ID;
import static android.provider.Settings.Secure.getString;

/**
 * Contextual information from user device
 */
class TechnicalContext {

    // Static variable to save screen name
    static String screenName = "";

    // Static variable to save level2
    static int level2 = 0;

    // Constant to retry get advertisingID
    private static final int RETRY_GET_ADVERTISING_COUNT = 3;

    // Constant android id key
    private static final String ANDROID_ID_KEY = "androidId";

    // Constant uuid key
    private static final String UUID_KEY = "UUID";

    /**
     * Get the tag version
     */
    static final Closure VTAG = new Closure() {
        @Override
        public String execute() {
            return "2.3.3";
        }
    };

    /**
     * Get the tag platform
     */
    static final Closure PTAG = new Closure() {
        @Override
        public String execute() {
            return "Android";
        }
    };

    /**
     * Enum with all networks connections type
     */
    enum ConnectionType {
        gprs, edge, twog, threeg, threegplus, fourg, wifi, offline, unknown
    }

    static ConnectionType getConnection() {
        android.content.Context context = Tracker.getAppContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return ConnectionType.wifi;
            } else {
                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return ConnectionType.gprs;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return ConnectionType.edge;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return ConnectionType.twog;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        return ConnectionType.threeg;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return ConnectionType.threegplus;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return ConnectionType.fourg;
                    default:
                        return ConnectionType.unknown;
                }
            }
        } else {
            return ConnectionType.offline;
        }
    }

    /**
     * Get Connection type
     *
     * @return Closure
     */
    static Closure getConnectionType() {
        return new Closure() {
            @Override
            public String execute() {
                switch (getConnection()) {
                    case gprs:
                        return "gprs";
                    case edge:
                        return "edge";
                    case twog:
                        return "2g";
                    case threeg:
                        return "3g";
                    case threegplus:
                        return "3g+";
                    case fourg:
                        return "4g";
                    case wifi:
                        return "wifi";
                    case unknown:
                        return "unknown";
                    default:
                        return "offline";
                }
            }
        };
    }

    /**
     * Get the operator name
     *
     * @return Closure
     */
    static Closure getCarrier() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
                return telephonyManager != null ? telephonyManager.getNetworkOperatorName() : "";
            }
        };
    }

    /**
     * Get the device language
     *
     * @return Closure
     */
    static Closure getLanguage() {
        return new Closure() {
            @Override
            public String execute() {
                return Locale.getDefault() != null ? Locale.getDefault().toString().toLowerCase() : "";
            }
        };
    }

    /**
     * Get device brand, model , api level and screen resolution
     *
     * @return Closure
     */
    static Closure getDevice() {
        return new Closure() {
            @Override
            public String execute() {
                return String.format("[%1$s]-[%2$s]", BRAND, Tool.removeCharacters(MODEL, " ", "-", ".").toLowerCase());
            }
        };
    }

    /**
     * Get the OS version
     *
     * @return Closure
     */
    static Closure getOS() {
        return new Closure() {
            @Override
            public String execute() {
                return String.format("[android]-[%s]", Build.VERSION.RELEASE);
            }
        };
    }

    /**
     * Get the local hour
     *
     * @return Closure
     */
    static Closure getLocalHour() {
        return new Closure() {
            @Override
            public String execute() {
                return new SimpleDateFormat("HH'x'mm'x'ss", Locale.getDefault()).format(new Date());
            }
        };
    }

    /**
     * Get the application identifier
     *
     * @return Closure
     */
    static Closure getApplicationIdentifier() {
        return new Closure() {
            @Override
            public String execute() {
                return Tracker.getAppContext().getPackageName();
            }
        };
    }

    /**
     * Get the application version
     *
     * @return Closure
     */
    static Closure getApplicationVersion() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                String versionName = "";
                try {
                    if (context.getPackageManager() != null &&
                            context.getPackageName() != null &&
                            context.getPackageManager().getPackageInfo(context.getPackageName(), 0) != null) {

                        versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return String.format("[%s]", versionName);
            }
        };
    }

    /**
     * Get the device resolution
     *
     * @return Closure
     */
    static Closure getResolution() {
        return new Closure() {
            @Override
            public String execute() {
                android.content.Context context = Tracker.getAppContext();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((WindowManager) context.getApplicationContext().getSystemService(android.content.Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
            }
        };
    }

    /**
     * Get diagonal
     *
     * @return Closure
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Closure getDiagonal() {
        return new Closure() {
            @Override
            public String execute() {
                DisplayMetrics metrics = new DisplayMetrics();
                Display d = ((WindowManager) Tracker.getAppContext().getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();
                d.getMetrics(metrics);

                // since SDK_INT = 1;
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;

                try {
                    // includes window decorations (statusbar bar/menu bar)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                        heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                    }
                    // includes window decorations (statusbar bar/menu bar)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        Point realSize = new Point();
                        d.getRealSize(realSize);

                        widthPixels = realSize.x;
                        heightPixels = realSize.y;

                    }
                } catch (Exception ignored) {
                }

                double x = Math.pow(widthPixels / metrics.xdpi, 2);
                double y = Math.pow(heightPixels / metrics.ydpi, 2);


                return String.format(Locale.getDefault(), "%.1f", Math.sqrt(x + y));
            }
        };
    }

    /**
     * Get the download SDK source
     *
     * @return Closure
     */
    static Closure getDownloadSource(final Tracker tracker) {
        return new Closure() {
            @Override
            public String execute() {
                Object dls = tracker.getConfiguration().get(TrackerConfigurationKeys.DOWNLOAD_SOURCE);
                if (dls != null) {
                    return String.valueOf(dls);
                } else {
                    return "ext";
                }
            }
        };
    }

    /**
     * Get the user ID
     *
     * @param identifier String
     * @return Closure
     */
    static Closure getUserId(final String identifier) {
        return new Closure() {
            @Override
            public String execute() {
                final android.content.Context context = Tracker.getAppContext();
                SharedPreferences preferences = context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, Context.MODE_PRIVATE);

                if (preferences.getBoolean(TrackerConfigurationKeys.DO_NOT_TRACK_ENABLED, false)) {
                    return "opt-out";
                } else if (identifier.equals(ANDROID_ID_KEY)) {
                    return getString(context.getContentResolver(), ANDROID_ID);
                } else if (identifier.equals(UUID_KEY)) {
                    String uuid = preferences.getString(TrackerConfigurationKeys.IDCLIENT_UUID, null);
                    if (uuid == null) {
                        uuid = UUID.randomUUID().toString();
                        preferences.edit().putString(TrackerConfigurationKeys.IDCLIENT_UUID, uuid).apply();
                    }
                    return uuid;
                } else {
                    try {
                        com.google.android.gms.ads.identifier.AdvertisingIdClient.Info adInfo = null;
                        int count = 0;
                        while (adInfo == null && count < RETRY_GET_ADVERTISING_COUNT) {
                            adInfo = com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo(context);
                            count++;
                        }
                        if (adInfo != null && !adInfo.isLimitAdTrackingEnabled()) {
                            return adInfo.getId();
                        } else {
                            return "opt-out";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            }
        };
    }

    /**
     * Enable or disable tracking
     *
     * @param context Context
     * @param enabled boolean
     */
    static void doNotTrack(android.content.Context context, boolean enabled) {
        context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE).edit().putBoolean(TrackerConfigurationKeys.DO_NOT_TRACK_ENABLED, enabled).apply();
    }

    /**
     * Tracking enabled or disabled
     *
     * @param context Context
     * @return boolean
     */
    static boolean doNotTrackEnabled(android.content.Context context) {
        return context.getSharedPreferences(TrackerConfigurationKeys.PREFERENCES, android.content.Context.MODE_PRIVATE).getBoolean(TrackerConfigurationKeys.DO_NOT_TRACK_ENABLED, false);
    }
}
