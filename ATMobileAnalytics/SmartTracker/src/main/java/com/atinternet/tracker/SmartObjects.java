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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

class Screenshot {

    private String b64Value;
    private View attachedRootView;

    Screenshot() {

        Activity currentActivity = SmartContext.currentActivity != null ? SmartContext.currentActivity.get() : null;
        if (currentActivity != null) {

            Bitmap bitmapSource;

            attachedRootView = currentActivity.getWindow().getDecorView().getRootView();
            boolean originalDrawingCache = attachedRootView.isDrawingCacheEnabled();
            attachedRootView.setDrawingCacheEnabled(true);

            if ((bitmapSource = attachedRootView.getDrawingCache()) != null) {
                Bitmap bitmapCopy = Bitmap.createBitmap(bitmapSource);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapCopy.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                b64Value = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
            }

            attachedRootView.setDrawingCacheEnabled(originalDrawingCache);
        }

    }

    String getB64Value() {
        return b64Value;
    }

    View getAttachedRootView() {
        return attachedRootView;
    }
}

class App {

    private String packageName;
    private String appIcon;
    private String device;
    private String platform;
    private String title;
    private int width;
    private int height;
    private float scale;
    private int screenOrientation;
    private String name;
    private String version;
    private String token;

    String getDevice() {
        return device;
    }

    String getSiteID() {
        String siteId = String.valueOf(AutoTracker.getInstance().getConfiguration().get(TrackerConfigurationKeys.SITE));
        if (TextUtils.isEmpty(siteId)) {
            throw new IllegalArgumentException("SiteId is not defined in your configuration");
        }
        return siteId;
    }

    String getVersion() {
        return version;
    }

    String getPackage() {
        return packageName;
    }

    String getAppId() {
        return platform + "." + getPackage();
    }

    String getPlatform() {
        return platform;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    float getScale() {
        return scale;
    }

    String getName(android.content.Context c) {
        if (c.checkCallingOrSelfPermission("android.permission.BLUETOOTH") == PackageManager.PERMISSION_GRANTED) {
            BluetoothAdapter bluetoothAdapter;
            if ((bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
                return bluetoothAdapter.getName();
            }
        }
        return Build.MODEL;
    }

    App(String token, String version) {
        try {
            android.content.Context c = Tracker.getAppContext();
            this.token = token;
            this.version = version;

            PackageManager pm = c.getPackageManager();
            WindowManager wm = (WindowManager) c.getSystemService(android.content.Context.WINDOW_SERVICE);
            Display d = wm.getDefaultDisplay();
            packageName = packageName == null ? c.getPackageName() : packageName;
            device = device == null ? Build.BRAND + " " + Build.MODEL : device;
            platform = "android";
            Pair<Integer, Integer> pair = UI.getScreenSize(d);
            scale = UI.getScale(d);
            width = (int) (pair.first / scale);
            height = (int) (pair.second / scale);
            screenOrientation = UI.getScreenOrientation();
            name = getName(c);
            title = title == null ? getTitle(pm) : title;
            appIcon = appIcon == null ? getAppIcon(c, pm) : appIcon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getAppIcon(android.content.Context c, PackageManager pm) throws PackageManager.NameNotFoundException {
        Drawable icon = pm.getApplicationIcon(packageName);
        if (icon == null) {
            icon = getDefaultIconDrawable(c);
        }
        BitmapDrawable bitDw = ((BitmapDrawable) icon);
        Bitmap bitmap = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    private String getTitle(PackageManager pm) throws PackageManager.NameNotFoundException {
        return pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString();
    }

    @SuppressWarnings("deprecation")
    private Drawable getDefaultIconDrawable(android.content.Context c) {
        if (Build.VERSION.SDK_INT >= 21) {
            return c.getResources().getDrawable(com.atinternet.tracker.R.drawable.ic_launcher, null);
        } else {
            return c.getResources().getDrawable(com.atinternet.tracker.R.drawable.ic_launcher);
        }
    }

    JSONObject getData() {
        try {
            JSONObject appObject = new JSONObject();
            appObject.put("appIcon", appIcon)
                    .put("device", device)
                    .put("package", packageName)
                    .put("platform", platform)
                    .put("title", title)
                    .put("width", width)
                    .put("height", height)
                    .put("scale", scale)
                    .put("siteID", getSiteID())
                    .put("name", name)
                    .put("screenOrientation", screenOrientation)
                    .put("version", version)
                    .put("token", token);

            return appObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class SmartView {
    private int id;
    private String className;
    private String text;
    private String path;
    private String screenShot;
    private float x;
    private float y;
    private int width;
    private int height;
    private int position;
    private boolean visible;

    SmartView(View view, int[] coords) {
        id = view == null ? -1 : view.getId();
        className = view == null ? "" : view.getClass().getSimpleName();
        screenShot = "";
        x = coords[0];
        y = coords[1];
        width = view == null ? 0 : view.getWidth();
        height = view == null ? 0 : view.getHeight();
        visible = view != null && view.isShown();
        path = view == null ? "/" : getPath(view);
        text = view == null ? "" : (view instanceof ViewGroup ? getTextViewGroup((ViewGroup) view) : detectViewText(view));
        position = view == null ? -1 : getPositionInTreeView(view);
    }

    SmartView setPosition(int position) {
        this.position = position;

        return this;
    }

    SmartView setClassName(String className) {
        this.className = className;

        return this;
    }

    SmartView setX(float x) {
        this.x = x;

        return this;
    }

    SmartView setY(float y) {
        this.y = y;

        return this;
    }

    int getId() {
        return id;
    }

    private String getPath(View view) {
        StringBuilder path = new StringBuilder(view.getClass().getSimpleName());
        ViewParent parent = view.getParent();
        while (parent != null) {
            path.insert(0, "/")
                    .insert(0, parent.getClass().getSimpleName());
            parent = parent.getParent();
        }
        return path.toString();
    }

    private String getTextViewGroup(ViewGroup view) {
        String text = null;
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            if (child instanceof ViewGroup) {
                text = getTextViewGroup((ViewGroup) child);
            } else {
                text = detectViewText(child);
            }
            if (!TextUtils.isEmpty(text)) {
                break;
            }
        }
        return text;
    }

    private String detectViewText(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            CharSequence hint;
            if ((hint = editText.getHint()) == null) {
                return "";
            } else {
                return hint.toString();
            }
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        } else {
            return "";
        }
    }

    private int getPositionInTreeView(View view) {
        int position = -1;
        ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup)) {
            return 0;
        }
        ViewGroup parentVG = (ViewGroup) parent;
        int length = parentVG.getChildCount();
        for (int i = 0; i < length; i++) {
            View child = parentVG.getChildAt(i);
            if (child.getClass().isAssignableFrom(view.getClass())) {
                position++;
                if (child.equals(view)) {
                    return position;
                }
            }
        }
        return position;
    }

    JSONObject getData(float scale) {
        try {
            JSONObject viewObject = new JSONObject();
            viewObject.put("className", className)
                    .put("x", (int) (x / scale))
                    .put("y", (int) (y / scale))
                    .put("width", (int) (width / scale))
                    .put("height", (int) (height / scale))
                    .put("text", text)
                    .put("path", path)
                    .put("screenshot", screenShot)
                    .put("visible", visible)
                    .put("position", position);

            return viewObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class SmartEvent {
    private float x;
    private float y;
    private String type;
    private String methodName;
    private Screen screen;
    private String title;
    private String direction;
    private boolean isDefaultMethod;
    private SmartView smartView;

    SmartEvent(SmartView smartView, View rootView, float x, float y, String type, String direction) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.direction = direction;
        this.smartView = smartView;
        screen = new Screen();
        isDefaultMethod = this.smartView == null || this.smartView.getId() == -1;
        String defaultMethodName = getDefaultMethodName();
        methodName = isDefaultMethod ? defaultMethodName : defaultMethodName + " " + rootView.getResources().getResourceEntryName(this.smartView.getId());
        title = methodName;
    }

    String getType() {
        return type;
    }

    private String getDefaultMethodName() {
        switch (type) {
            case "tap":
                return "handle" +
                        Tool.upperCaseFirstLetter(direction) +
                        Tool.upperCaseFirstLetter(type) +
                        ":";
            case "pan":
                return "handle" +
                        Tool.upperCaseFirstLetter(type) +
                        ":";
            case "deviceRotate":
                return "handleDeviceRotation:";
            default:
                return "handle" +
                        Tool.upperCaseFirstLetter(type) +
                        Tool.upperCaseFirstLetter(direction) +
                        ":";
        }
    }

    JSONObject getData() {
        try {
            float scale = screen.getScale();
            JSONObject eventObject = new JSONObject();
            eventObject.put("x", (int) (x / scale))
                    .put("y", (int) (y / scale))
                    .put("type", type)
                    .put("methodName", methodName)
                    .put("title", title)
                    .put("direction", direction)
                    .put("isDefaultMethod", isDefaultMethod)
                    .put("view", smartView.getData(scale))
                    .put("screen", screen.getData());
            return eventObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
