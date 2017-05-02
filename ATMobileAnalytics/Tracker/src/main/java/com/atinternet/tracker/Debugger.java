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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;

/**
 * Class to manage Debugger feature
 */
public class Debugger extends GestureDetector.SimpleOnGestureListener implements OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {

    private static int viewerVisibility = View.GONE;
    private static int bubbleVisibility = View.VISIBLE;
    private static final float ALPHA_BACKGROUND = .3f;
    private static final float DELTA = 100;

    static boolean isActive;
    static int currentViewVisibleId = -1;
    private static int itemPosition = -1;
    private static final ArrayList<Debugger.DebuggerEvent> debuggerEvents = new ArrayList<>();
    private static final ArrayList<Hit> offlineHits = new ArrayList<>();

    private static WeakReference<android.content.Context> context;
    private static WeakReference<DebuggerEventListAdapter> debuggerEventListAdapter;
    private static WeakReference<ATFrameLayout> debuggerViewerLayout;
    private boolean hasMoved;

    private final Tracker tracker;

    private final GestureDetector gestureDetector;
    private final DisplayMetrics metrics;

    private LinearLayout eventViewer;
    private LinearLayout offlineViewer;
    private LinearLayout hitDetailViewer;
    private RelativeLayout noEventsLayout;
    private RelativeLayout noOfflineHitsLayout;
    private ListView eventListView;
    private ListView offlineHitsListView;
    private static WeakReference<ATImageView> bubbleImage;

    private final DebuggerOfflineHitsAdapter debuggerOfflineHitsAdapter;

    private WindowManager.LayoutParams bubbleImageLayoutParams;

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private final int ratio;

    static Context getContext() {
        if (context != null) {
            return context.get();
        }
        return null;
    }

    /**
     * Show the debugger
     *
     * @param context current Activity context
     * @param tracker tracker instance
     * @deprecated Since 2.2.2, use {@link #create(Context, Tracker)} instead.
     */
    @Deprecated
    public static void show(Context context, Tracker tracker) {
        create(context, tracker);
    }

    /**
     * Show the debugger
     *
     * @param context current Activity context
     * @param tracker tracker instance
     */
    public static void create(Context context, Tracker tracker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                ((Activity) context).startActivityForResult(intent, ATInternet.ALLOW_OVERLAY_INTENT_RESULT_CODE);
            } else {
                new Debugger(context, tracker);
            }
        } else {
            new Debugger(context, tracker);
        }
    }

    /**
     * Hide the debugger
     */
    public static void remove() {
        if (bubbleImage != null) {
            ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).removeViewImmediate(bubbleImage.get());
            bubbleImage = null;
        }
        if (debuggerViewerLayout != null) {
            ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).removeViewImmediate(debuggerViewerLayout.get());
            debuggerViewerLayout = null;
        }
    }

    /**
     * Change debugger visibility
     *
     * @param visible true if debugger should be visible
     */
    public static void setViewerVisibility(boolean visible) {
        bubbleVisibility = visible ? VISIBLE : GONE;
        bubbleImage.get().setVisibility(bubbleVisibility);
    }

    static void setDebuggerViewerLayout(boolean visible) {
        viewerVisibility = visible ? VISIBLE : GONE;
        debuggerViewerLayout.get().setVisibility(viewerVisibility);
        setAlphaBackground(visible, false);
    }

    static DebuggerEventListAdapter getDebuggerEventListAdapter() {
        return debuggerEventListAdapter.get();
    }

    static ArrayList<Debugger.DebuggerEvent> getDebuggerEvents() {
        return debuggerEvents;
    }

    private Debugger(Context ctx, Tracker tr) {
        isActive = true;
        context = new WeakReference<>(ctx);
        remove();
        if (context.get().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ratio = 7;
        } else {
            ratio = 8;
        }
        tracker = tr;
        metrics = new DisplayMetrics();

        ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        gestureDetector = new GestureDetector(context.get(), this);

        inflateViews(context.get());
        addViews();

        debuggerEventListAdapter = new WeakReference<>(new DebuggerEventListAdapter(context.get(), noEventsLayout));
        debuggerOfflineHitsAdapter = new DebuggerOfflineHitsAdapter(context.get(), tracker, noOfflineHitsLayout);
        eventListView.setAdapter(debuggerEventListAdapter.get());
        offlineHitsListView.setAdapter(debuggerOfflineHitsAdapter);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        bubbleImageLayoutParams.y = 0;
        ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(bubbleImage.get(), bubbleImageLayoutParams);
        toggleViewer();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event) || viewerVisibility == VISIBLE) {
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = bubbleImageLayoutParams.x;
                    initialY = bubbleImageLayoutParams.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if (hasMoved) {
                        if (event.getRawX() >= initialTouchX + DELTA) {
                            bubbleImageLayoutParams.x = metrics.widthPixels - bubbleImage.get().getDrawable().getMinimumWidth();
                        } else if (event.getRawX() <= initialTouchX - DELTA) {
                            bubbleImageLayoutParams.x = 0;
                        }
                        hasMoved = false;
                    }
                    bubbleImageLayoutParams.y = initialY
                            + (int) (event.getRawY() - initialTouchY);
                    ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(bubbleImage.get(), bubbleImageLayoutParams);
                    break;
                case MotionEvent.ACTION_MOVE:
                    hasMoved = true;
                    bubbleImageLayoutParams.x = initialX
                            + (int) (event.getRawX() - initialTouchX);
                    bubbleImageLayoutParams.y = initialY
                            + (int) (event.getRawY() - initialTouchY);
                    ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(bubbleImage.get(), bubbleImageLayoutParams);
                    break;
            }
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.deleteEventsImageView) {
            debuggerEvents.clear();
            debuggerEventListAdapter.get().notifyDataSetChanged();
            noEventsLayout.setVisibility(View.VISIBLE);
            eventListView.setVisibility(View.GONE);
        } else if (id == R.id.deleteOfflineHits) {
            tracker.Offline().delete();
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(View.VISIBLE);
            offlineHitsListView.setVisibility(View.GONE);
        } else if (id == R.id.goToOfflineHitsImageView) {
            currentViewVisibleId = R.id.offlineViewer;
            Tool.setVisibleViewWithAnimation(eventViewer, false);
            Tool.setVisibleViewWithAnimation(offlineViewer, true);
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            offlineHitsListView.setVisibility(!offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
        } else if (id == R.id.backToEventViewer) {
            currentViewVisibleId = R.id.eventViewer;
            debuggerEventListAdapter.get().notifyDataSetChanged();
            Tool.setVisibleViewWithAnimation(offlineViewer, false);
            Tool.setVisibleViewWithAnimation(eventViewer, true);
            noEventsLayout.setVisibility(debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            eventListView.setVisibility(!debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
        } else if (id == R.id.refreshOfflineHits) {
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            offlineHitsListView.setVisibility(!offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
        } else if (id == R.id.backToPreviousView) {
            itemPosition = -1;
            ViewGroup parametersListView = (ViewGroup) hitDetailViewer.findViewById(R.id.parametersListView);
            parametersListView.removeAllViews();
            Tool.setVisibleViewWithAnimation(hitDetailViewer, false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemPosition = position;
        onUpdateAfterItemClick(position, true);
    }

    private void onUpdateAfterItemClick(int position, boolean animate) {
        ViewGroup parametersListView = (ViewGroup) hitDetailViewer.findViewById(R.id.parametersListView);
        if (currentViewVisibleId == R.id.eventViewer) {
            DebuggerEvent event = debuggerEvents.get(position);
            if (event.isHit()) {
                createHitDetailView(parametersListView, event.getMessage());
            } else {
                ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.get().getString(R.string.event_detail));
                TextView eventDetail = new TextView(context.get());
                eventDetail.setTextColor(Tool.getColor(context.get(), android.R.color.black));
                eventDetail.setPadding(10, 10, 10, 10);
                eventDetail.setText(event.getMessage());
                parametersListView.addView(eventDetail);
            }
        } else {
            ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.get().getString(R.string.hit_detail));
            createHitDetailView(parametersListView, offlineHits.get(position).getUrl());
        }
        if (animate) {
            Tool.setVisibleViewWithAnimation(hitDetailViewer, true);
        } else {
            hitDetailViewer.setVisibility(VISIBLE);
        }
    }

    private void inflateViews(Context context) {
        debuggerViewerLayout = new WeakReference<>((ATFrameLayout) View.inflate(context, R.layout.debugger_layout, null));

        eventViewer = (LinearLayout) debuggerViewerLayout.get().findViewById(R.id.eventViewer);
        offlineViewer = (LinearLayout) debuggerViewerLayout.get().findViewById(R.id.offlineViewer);
        eventListView = (ListView) debuggerViewerLayout.get().findViewById(R.id.eventListView);
        offlineHitsListView = (ListView) debuggerViewerLayout.get().findViewById(R.id.offlineHitsListView);
        noEventsLayout = (RelativeLayout) debuggerViewerLayout.get().findViewById(R.id.noEvents);
        noOfflineHitsLayout = (RelativeLayout) debuggerViewerLayout.get().findViewById(R.id.noOfflineHits);
        hitDetailViewer = (LinearLayout) debuggerViewerLayout.get().findViewById(R.id.hitDetailViewer);

        bubbleImage = new WeakReference<>(new ATImageView(context));
        bubbleImage.get().setImageDrawable(Tool.getResizedImage(R.drawable.atinternet_logo, context, (int) (94 * metrics.density), (int) (73 * metrics.density)));
        bubbleImage.get().setVisibility(bubbleVisibility);
        bubbleImage.get().setOnTouchListener(this);

        debuggerViewerLayout.get().setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.deleteEventsImageView).setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.deleteOfflineHits).setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.goToOfflineHitsImageView).setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.backToEventViewer).setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.refreshOfflineHits).setOnClickListener(this);
        debuggerViewerLayout.get().findViewById(R.id.backToPreviousView).setOnClickListener(this);

        eventListView.setOnItemClickListener(this);
        offlineHitsListView.setOnItemClickListener(this);
    }

    private void addViews() {
        bubbleImageLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        bubbleImageLayoutParams.gravity = Gravity.TOP | Gravity.START;
        bubbleImageLayoutParams.x = 0;
        bubbleImageLayoutParams.y = 0;
        ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).addView(bubbleImage.get(), bubbleImageLayoutParams);

        if (currentViewVisibleId == -1) {
            currentViewVisibleId = R.id.eventViewer;
        }
        debuggerViewerLayout.get().findViewById(currentViewVisibleId).setVisibility(VISIBLE);
        if (viewerVisibility == VISIBLE) {
            debuggerViewerLayout.get().setVisibility(VISIBLE);
            noEventsLayout.setVisibility(debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            setAlphaBackground(true, true);
        } else if (viewerVisibility == GONE) {
            debuggerViewerLayout.get().setVisibility(GONE);
        }

        WindowManager.LayoutParams debuggerViewerLayoutParams = new WindowManager.LayoutParams(
                metrics.widthPixels - 10,
                ratio * metrics.heightPixels / 10,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        debuggerViewerLayoutParams.gravity = Gravity.TOP | Gravity.START;
        debuggerViewerLayoutParams.x = 10;
        debuggerViewerLayoutParams.y = bubbleImage.get().getDrawable().getMinimumHeight();
        debuggerViewerLayoutParams.windowAnimations = android.R.style.Animation_Translucent;
        ((WindowManager) context.get().getSystemService(Context.WINDOW_SERVICE)).addView(debuggerViewerLayout.get(), debuggerViewerLayoutParams);

        if (itemPosition != -1) {
            onUpdateAfterItemClick(itemPosition, false);
        }
    }

    /**
     * Show or hide viewer
     */
    private void toggleViewer() {
        if (viewerVisibility == GONE) {
            viewerVisibility = View.VISIBLE;
            Tool.setVisibleViewWithAnimation(debuggerViewerLayout.get(), true);
            debuggerEventListAdapter.get().notifyDataSetChanged();
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            offlineHitsListView.setVisibility(!offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            noEventsLayout.setVisibility(debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            eventListView.setVisibility(!debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            setAlphaBackground(true, true);
        } else {
            viewerVisibility = GONE;
            Tool.setVisibleViewWithAnimation(debuggerViewerLayout.get(), false);
            setAlphaBackground(false, true);
        }
    }

    private static void setAlphaBackground(boolean hasReduceAlpha, boolean withAnim) {
        AlphaAnimation animation1 = hasReduceAlpha ? new AlphaAnimation(1.f, ALPHA_BACKGROUND) : new AlphaAnimation(ALPHA_BACKGROUND, 1.f);
        animation1.setDuration(withAnim ? 500 : 0);
        animation1.setFillAfter(true);
        ((Activity) context.get()).getWindow().getDecorView().findViewById(android.R.id.content).startAnimation(animation1);
    }

    /**
     * Create the hit detail view
     *
     * @param parametersListView ViewGroup
     * @param message            String
     */
    private void createHitDetailView(ViewGroup parametersListView, String message) {
        ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.get().getString(R.string.hit_detail));
        LinkedHashMap<String, String> parameters = Tool.getParameters(message);
        Set<String> keySet = parameters.keySet();
        Boolean colorGrey = true;
        for (String key : keySet) {
            String value = parameters.get(key);
            LinearLayout hitDetail = (LinearLayout) View.inflate(context.get(), R.layout.parameter_holder, null);
            if (colorGrey) {
                hitDetail.setBackgroundColor(Tool.getColor(context.get(), R.color.at_darker_grey));
            }
            ((TextView) hitDetail.findViewById(R.id.keyView)).setText(key);
            ((TextView) hitDetail.findViewById(R.id.valueView)).setText(value);
            parametersListView.addView(hitDetail);
            colorGrey = !colorGrey;
        }
    }

    /**
     * Internal class
     */
    static class DebuggerEvent {

        /**
         * Date of event
         */
        private final Date date;

        /**
         * Event message
         */
        private final String message;

        /**
         * Type of event
         */
        private final String type;

        /**
         * Boolean to know if message is a hit
         */
        private final boolean isHit;

        /**
         * Get Type
         *
         * @return String
         */
        String getType() {
            return type;
        }

        /**
         * Get message
         *
         * @return String
         */
        String getMessage() {
            return message;
        }

        /**
         * Get date
         *
         * @return Date
         */
        Date getDate() {
            return date;
        }

        /**
         * Return true if message is a hit
         *
         * @return boolean
         */
        boolean isHit() {
            return isHit;
        }

        /**
         * Default Constructor
         */
        DebuggerEvent(String message, String type, boolean isHit) {
            date = new Date();
            this.message = message;
            this.type = type;
            this.isHit = isHit;
        }
    }

    /**
     * Event list adapter
     */
    class DebuggerEventListAdapter extends BaseAdapter {

        private static final int SIZE_IMAGE = 70;
        private static final String HOUR_FORMAT = "HH:mm:ss";

        private ArrayList<Debugger.DebuggerEvent> debuggerEvents = new ArrayList<>();
        private final LayoutInflater inflater;
        private final Context context;
        private final RelativeLayout noEventsLayout;

        DebuggerEventListAdapter(Context context, RelativeLayout noEventsLayout) {
            this.context = context;
            this.noEventsLayout = noEventsLayout;
            inflater = LayoutInflater.from(context);
            this.debuggerEvents = Debugger.debuggerEvents;
        }

        @Override
        public int getCount() {
            return debuggerEvents.size();
        }

        @Override
        public Object getItem(int position) {
            return debuggerEvents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Debugger.DebuggerEvent event = debuggerEvents.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.event_holder, parent, false);
            }
            if (position % 2 != 0) {
                convertView.setBackgroundColor(Tool.getColor(context, R.color.at_darker_grey));
            } else {
                convertView.setBackgroundColor(Tool.getColor(context, android.R.color.white));
            }

            String timeString = new SimpleDateFormat(HOUR_FORMAT, Locale.getDefault()).format(event.getDate());

            noEventsLayout.setVisibility(View.GONE);

            ImageView iconHitImageView = (ImageView) convertView.findViewById(R.id.iconHitImageView);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            TextView hitTextView = (TextView) convertView.findViewById(R.id.hitTextView);
            ImageView typeHitImageView = (ImageView) convertView.findViewById(R.id.typeHitImageView);

            timeTextView.setText(timeString);
            hitTextView.setText(event.getMessage());
            iconHitImageView.setImageDrawable(Tool.getResizedImage(context.getResources().getIdentifier(event.getType(), "drawable", context.getPackageName()), context, SIZE_IMAGE, SIZE_IMAGE));

            if (event.isHit()) {
                Hit hit = new Hit(event.getMessage());
                switch (hit.getHitType()) {
                    case Touch:
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.touch48, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                    case AdTracking:
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.adtracking48, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                    case Video:
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.video, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                    case Audio:
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.audio, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                    case ProduitImpression:
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.product, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                    default://Screen
                        typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.smartphone48, context, SIZE_IMAGE, SIZE_IMAGE));
                        break;
                }
            }

            return convertView;
        }
    }

    /**
     * Offline hits adapter
     */
    private class DebuggerOfflineHitsAdapter extends BaseAdapter {

        private static final int SIZE_IMAGE = 70;
        private static final String HOUR_FORMAT = "HH:mm:ss";
        private static final String DATE_STRING = "dd/MM/yyyy";
        private final RelativeLayout noOfflineHitsLayout;

        private ArrayList<Hit> offlineHits = new ArrayList<>();
        private final LayoutInflater inflater;
        private final Context context;
        private final Tracker tracker;

        DebuggerOfflineHitsAdapter(Context context, Tracker tracker, RelativeLayout noOfflineHitsLayout) {
            this.context = context;
            this.tracker = tracker;
            inflater = LayoutInflater.from(context);
            this.offlineHits = Debugger.offlineHits;
            this.noOfflineHitsLayout = noOfflineHitsLayout;
        }

        @Override
        public int getCount() {
            return offlineHits.size();
        }

        @Override
        public Object getItem(int position) {
            return offlineHits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            offlineHits.clear();
            ArrayList<Hit> temp = tracker.Offline().get();
            int length = tracker.Offline().count();
            for (int i = 1; i <= length; i++) {
                offlineHits.add(temp.get(length - i));
            }
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Hit offlineHit = offlineHits.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.offline_hits_holder, parent, false);
            }
            if (position % 2 == 0) {
                convertView.setBackgroundColor(Tool.getColor(context, R.color.at_darker_grey));
            } else {
                convertView.setBackgroundColor(Tool.getColor(context, android.R.color.white));
            }

            String hourString = new SimpleDateFormat(HOUR_FORMAT, Locale.getDefault()).format(offlineHit.getDate());
            String dateString = new SimpleDateFormat(DATE_STRING, Locale.getDefault()).format(offlineHit.getDate());

            noOfflineHitsLayout.setVisibility(GONE);

            TextView hitTextView = (TextView) convertView.findViewById(R.id.hitTextView);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            ImageView typeHitImageView = (ImageView) convertView.findViewById(R.id.typeHitImageView);
            ImageView removeOfflineHit = (ImageView) convertView.findViewById(R.id.removeOfflineHit);

            timeTextView.setText(hourString);
            dateTextView.setText(dateString);
            hitTextView.setText(offlineHit.getUrl());
            removeOfflineHit.setImageDrawable(Tool.getResizedImage(R.drawable.trash48, context, SIZE_IMAGE, SIZE_IMAGE));
            switch (offlineHit.getHitType()) {
                case Touch:
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.touch48, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
                case AdTracking:
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.adtracking48, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
                case Video:
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.video, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
                case Audio:
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.audio, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
                case ProduitImpression:
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.product, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
                default://Screen
                    typeHitImageView.setImageDrawable(Tool.getResizedImage(R.drawable.smartphone48, context, SIZE_IMAGE, SIZE_IMAGE));
                    break;
            }
            removeOfflineHit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Storage(Tracker.getAppContext()).deleteHit(offlineHit.getUrl());
                    notifyDataSetChanged();
                    noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
                }
            });

            return convertView;
        }
    }
}