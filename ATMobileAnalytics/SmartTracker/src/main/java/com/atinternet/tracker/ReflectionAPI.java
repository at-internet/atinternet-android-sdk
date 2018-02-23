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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

final class ReflectionAPI {

    private ReflectionAPI() {
        throw new IllegalStateException("Private Class");
    }

    /**
     * Takes screenshot of provided activity and puts it into bitmap.
     *
     * @param activity Activity of which the screenshot will be taken.
     * @return Bitmap of what is displayed in activity.
     */
    static Pair<Bitmap, View> takeScreenshotBitmap(Activity activity) {
        try {
            return takeBitmapUnchecked(activity);
        } catch (Exception e) {
            return null;
        }
    }

    private static Pair<Bitmap, View> takeBitmapUnchecked(Activity activity) throws InterruptedException {
        final List<ViewRootData> viewRoots = getViewRootDatas(activity);
        if (viewRoots.isEmpty()) {
            return null;
        }

        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;

        for (ViewRootData viewRoot : viewRoots) {
            if (viewRoot.getView().getWidth() > maxWidth) {
                maxWidth = viewRoot.getView().getWidth();
            }
            if (viewRoot.getView().getHeight() > maxHeight) {
                maxHeight = viewRoot.getView().getHeight();
            }
        }

        final Bitmap bitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            drawRootsToBitmap(viewRoots, bitmap);
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        drawRootsToBitmap(viewRoots, bitmap);
                    } finally {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        }
        return new Pair<>(bitmap, viewRoots.get(viewRoots.size() - 1).getView());
    }

    private static void drawRootsToBitmap(List<ViewRootData> viewRoots, Bitmap bitmap) {
        for (ViewRootData rootData : viewRoots) {
            // now only dim supported
            if ((rootData.getLayoutParams().flags & LayoutParams.FLAG_DIM_BEHIND) == LayoutParams.FLAG_DIM_BEHIND) {
                Canvas dimCanvas = new Canvas(bitmap);

                int alpha = (int) (255 * rootData.getLayoutParams().dimAmount);
                dimCanvas.drawARGB(alpha, 0, 0, 0);
            }

            int[] coords = new int[2];
            rootData.getView().getLocationOnScreen(coords);

            int left = coords[0];
            int top = coords[1];

            Canvas canvas = new Canvas(bitmap);
            canvas.translate(left, top);
            rootData.getView().draw(canvas);
        }
    }

    @SuppressWarnings("unchecked") // no way to check
    static List<ViewRootData> getViewRootDatas(Activity activity) {
        if (activity == null) {
            return Collections.emptyList();
        }

        List<ViewRootData> rootViews = new ArrayList<>();

        Object globalWindowManager;
        if (Build.VERSION.SDK_INT <= 16) {
            globalWindowManager = getFieldValue("mWindowManager", activity.getWindowManager());
        } else {
            globalWindowManager = getFieldValue("mGlobal", activity.getWindowManager());
        }

        if (globalWindowManager == null) {
            return Collections.emptyList();
        }

        List rootObjects = null;
        List<LayoutParams> paramsObject = null;
        Object result = getFieldValue("mRoots", globalWindowManager);

        if (result instanceof List) {
            rootObjects = (List) result;
        }
        result = getFieldValue("mParams", globalWindowManager);
        if (result instanceof List) {
            paramsObject = (List<LayoutParams>) result;
        }

        if (rootObjects == null || paramsObject == null) {
            return Collections.emptyList();
        }

        int index = 0;
        for (Object root : rootObjects) {
            View view = (View) getFieldValue("mView", root);
            if (view != null && view.isShown() && !instanceOfAtView(view)) {
                rootViews.add(new ViewRootData(view, paramsObject.get(index)));
            }
            index++;
        }

        ensureDialogsAreAfterItsParentActivities(rootViews);

        return rootViews;
    }

    static ArrayList<View> getAllTouchables(ViewGroup baseView) {
        ArrayList<View> natives = baseView.getTouchables();
        ArrayList<View> reflection = getTouchablesByReflection(baseView);

        for (View ref : reflection) {
            if (!natives.contains(ref)) {
                natives.add(ref);
            }
        }

        return natives;
    }

    private static ArrayList<View> getTouchablesByReflection(ViewGroup baseView) {
        ArrayList<View> touchables = new ArrayList<>();

        for (int i = 0; i < baseView.getChildCount(); i++) {
            View child = baseView.getChildAt(i);
            Object mListenerInfo;
            if (child.isShown()
                    && (mListenerInfo = getFieldValue("mListenerInfo", child)) != null
                    && (getFieldValue("mOnClickListener", mListenerInfo) != null || getFieldValue("mOnTouchListener", mListenerInfo) != null)) {
                touchables.add(child);
            }
            if (child instanceof ViewGroup) {
                touchables.addAll(getTouchablesByReflection((ViewGroup) child));
            }
        }

        return touchables;
    }

    private static boolean instanceOfAtView(View v) {
        return v instanceof ATFrameLayout || v instanceof ATRelativeLayout || v instanceof ATImageView;
    }

    private static void ensureDialogsAreAfterItsParentActivities(List<ViewRootData> viewRoots) {
        if (viewRoots.size() <= 1) {
            return;
        }

        for (int dialogIndex = 0; dialogIndex < viewRoots.size() - 1; dialogIndex++) {
            ViewRootData viewRoot = viewRoots.get(dialogIndex);
            if (viewRoot.isDialogType()) {

                for (int parentIndex = dialogIndex + 1; parentIndex < viewRoots.size(); parentIndex++) {
                    ViewRootData possibleParent = viewRoots.get(parentIndex);
                    if (possibleParent.isActivityType()) {
                        viewRoots.remove(possibleParent);
                        viewRoots.add(dialogIndex, possibleParent);

                        break;
                    }
                }
            }
        }
    }

    private static Object getFieldValue(String fieldName, Object target) {
        try {
            return getFieldValueUnchecked(fieldName, target);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static Object getFieldValueUnchecked(String fieldName, Object target) throws IllegalAccessException {
        Field field = findField(fieldName, target.getClass());
        if (field != null) {
            field.setAccessible(true);
            return field.get(target);
        }
        return null;
    }

    private static Field findField(String name, Class clazz) {
        Class currentClass = clazz;
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return null;
    }
}

class ViewRootData {

    private final View view;
    private final LayoutParams layoutParams;

    View getView() {
        return view;
    }

    LayoutParams getLayoutParams() {
        return layoutParams;
    }

    ViewRootData(View view, LayoutParams layoutParams) {
        this.view = view;
        this.layoutParams = layoutParams;
    }

    boolean isDialogType() {
        return layoutParams.type == LayoutParams.TYPE_APPLICATION;
    }

    boolean isActivityType() {
        return layoutParams.type == LayoutParams.TYPE_BASE_APPLICATION;
    }
}
