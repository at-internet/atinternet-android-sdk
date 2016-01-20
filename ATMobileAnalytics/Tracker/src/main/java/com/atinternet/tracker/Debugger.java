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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.atinternet.tracker.Debugger.Move.left;
import static com.atinternet.tracker.Debugger.Move.right;

public class Debugger extends GestureDetector.SimpleOnGestureListener implements OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener {

    enum Move {
        right, left, none
    }

    private static final int MARGIN = 120;
    private static final int DISTANCE_MIN = 70;
    private static int viewerVisibility = View.GONE;

    static int currentViewVisibleId = -1;
    private static int itemPosition = -1;
    private static ArrayList<Debugger.DebuggerEvent> debuggerEvents = new ArrayList<Debugger.DebuggerEvent>();
    private static ArrayList<Hit> offlineHits = new ArrayList<Hit>();

    private static Move move = Move.none;

    private static Context context;
    static Tracker tracker;


    private final GestureDetector gestureDetector;
    private final DisplayMetrics metrics;
    private int initialX;
    private final LayoutInflater inflater;
    private FrameLayout debuggerViewerLayout;
    private LinearLayout eventViewer;
    private LinearLayout offlineViewer;
    private LinearLayout hitDetailViewer;
    private static RelativeLayout bubbleLayout;
    private RelativeLayout noEventsLayout;
    private RelativeLayout noOfflineHitsLayout;
    private ListView eventListView;
    private ListView offlineHitsListView;
    private ImageView bubbleImage;

    private static DebuggerEventListAdapter debuggerEventListAdapter;
    private final DebuggerOfflineHitsAdapter debuggerOfflineHitsAdapter;

    /**
     * Get Context
     *
     * @return Context
     */
    static Context getContext() {
        return context;
    }

    /**
     * Get Tracker instance
     *
     * @return Tracker
     */
    static Tracker getTracker() {
        return tracker;
    }

    static DebuggerEventListAdapter getDebuggerEventListAdapter() {
        return debuggerEventListAdapter;
    }

    static ArrayList<Debugger.DebuggerEvent> getDebuggerEvents() {
        return debuggerEvents;
    }

    public static void show(Context context, Tracker tracker) {
        new Debugger(context, tracker);
    }

    public static void setViewerVisibility(boolean visible) {
        setVisibleViewWithAnimation(bubbleLayout, visible);
    }


    /**
     * Constructor
     *
     * @param ctx Context
     * @param tr  Tracker
     */
    Debugger(Context ctx, Tracker tr) {
        context = ctx;
        tracker = tr;
        inflater = LayoutInflater.from(context);
        metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        gestureDetector = new GestureDetector(context, this);

        ViewGroup userView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        inflateViews(context, inflater);
        addViews(userView);

        debuggerEventListAdapter = new DebuggerEventListAdapter(context, debuggerEvents, noEventsLayout);
        debuggerOfflineHitsAdapter = new DebuggerOfflineHitsAdapter(context, offlineHits, noOfflineHitsLayout);
        eventListView.setAdapter(debuggerEventListAdapter);
        offlineHitsListView.setAdapter(debuggerOfflineHitsAdapter);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        toggleViewer();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) bubbleImage.getLayoutParams();
        int x = (int) event.getRawX();

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (x < (metrics.widthPixels - MARGIN) && x > MARGIN && Math.abs(x - initialX) > DISTANCE_MIN) {
                        if (x < (mParams.leftMargin + (bubbleImage.getWidth() / 2))) {
                            move = left;
                        } else if (x > (mParams.rightMargin + (bubbleImage.getWidth() / 2))) {
                            move = right;
                        }
                        mParams.leftMargin = x - (bubbleImage.getWidth() / 2);
                        bubbleImage.setLayoutParams(mParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (move == left) {
                        mParams.leftMargin = 0;
                    } else if (move == right) {
                        mParams.leftMargin = metrics.widthPixels - bubbleImage.getWidth();
                    }
                    bubbleImage.setLayoutParams(mParams);
                    break;
                case MotionEvent.ACTION_DOWN:
                    initialX = x;
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
            debuggerEventListAdapter.notifyDataSetChanged();
            noEventsLayout.setVisibility(View.VISIBLE);
            eventListView.setVisibility(View.GONE);
        } else if (id == R.id.deleteOfflineHits) {
            Debugger.tracker.Offline().delete();
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(View.VISIBLE);
            offlineHitsListView.setVisibility(View.GONE);
        } else if (id == R.id.goToOfflineHitsImageView) {
            currentViewVisibleId = R.id.offlineViewer;
            setVisibleViewWithAnimation(eventViewer, false);
            setVisibleViewWithAnimation(offlineViewer, true);
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            offlineHitsListView.setVisibility(!offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
        } else if (id == R.id.backToEventViewer) {
            currentViewVisibleId = R.id.eventViewer;
            debuggerEventListAdapter.notifyDataSetChanged();
            setVisibleViewWithAnimation(offlineViewer, false);
            setVisibleViewWithAnimation(eventViewer, true);
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
            setVisibleViewWithAnimation(hitDetailViewer, false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemPosition = position;
        onUpdateAfterItemClick(position, true);
    }

    /**
     * Callback call after onItemClick
     *
     * @param position int
     * @param animate  boolean
     */
    private void onUpdateAfterItemClick(int position, boolean animate) {
        ViewGroup parametersListView = (ViewGroup) hitDetailViewer.findViewById(R.id.parametersListView);
        if (currentViewVisibleId == R.id.eventViewer) {
            DebuggerEvent event = debuggerEvents.get(position);
            if (event.isHit()) {
                createHitDetailView(parametersListView, event.getMessage());
            } else {
                ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.getString(R.string.event_detail));
                TextView eventDetail = new TextView(context);
                eventDetail.setTextColor(Tool.getColor(context, android.R.color.black));
                eventDetail.setPadding(10, 10, 10, 10);
                eventDetail.setText(event.getMessage());
                parametersListView.addView(eventDetail);
            }
        } else {
            ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.getString(R.string.hit_detail));
            createHitDetailView(parametersListView, offlineHits.get(position).getUrl());
        }
        if (animate) {
            setVisibleViewWithAnimation(hitDetailViewer, true);
        } else {
            hitDetailViewer.setVisibility(VISIBLE);
        }
    }

    /**
     * Inflate views
     *
     * @param context  Context
     * @param inflater LayoutInflater
     */
    private void inflateViews(Context context, LayoutInflater inflater) {
        debuggerViewerLayout = (FrameLayout) inflater.inflate(R.layout.debugger_layout, null);
        bubbleLayout = (RelativeLayout) inflater.inflate(R.layout.debugger_bubble_layout, null);

        eventViewer = (LinearLayout) debuggerViewerLayout.findViewById(R.id.eventViewer);
        offlineViewer = (LinearLayout) debuggerViewerLayout.findViewById(R.id.offlineViewer);
        bubbleImage = (ImageView) bubbleLayout.findViewById(R.id.debugBubble);
        eventListView = (ListView) debuggerViewerLayout.findViewById(R.id.eventListView);
        offlineHitsListView = (ListView) debuggerViewerLayout.findViewById(R.id.offlineHitsListView);
        noEventsLayout = (RelativeLayout) debuggerViewerLayout.findViewById(R.id.noEvents);
        noOfflineHitsLayout = (RelativeLayout) debuggerViewerLayout.findViewById(R.id.noOfflineHits);
        hitDetailViewer = (LinearLayout) debuggerViewerLayout.findViewById(R.id.hitDetailViewer);

        bubbleImage.setImageDrawable(Tool.getResizedImage(R.drawable.atinternet_logo, context, (int) (94 * metrics.density), (int) (73 * metrics.density)));

        bubbleImage.setOnTouchListener(this);

        debuggerViewerLayout.setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.deleteEventsImageView).setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.deleteOfflineHits).setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.goToOfflineHitsImageView).setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.backToEventViewer).setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.refreshOfflineHits).setOnClickListener(this);
        debuggerViewerLayout.findViewById(R.id.backToPreviousView).setOnClickListener(this);

        eventListView.setOnItemClickListener(this);
        offlineHitsListView.setOnItemClickListener(this);
    }

    /**
     * Create debugger layout
     *
     * @param userView ViewGroup
     */
    private void addViews(ViewGroup userView) {
        bubbleLayout.setGravity(Gravity.BOTTOM);
        RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) bubbleImage.getLayoutParams();
        if (move == left) {
            mParams.leftMargin = 0;
        } else if (move == right) {
            mParams.leftMargin = metrics.widthPixels - bubbleImage.getDrawable().getMinimumWidth();
        }
        bubbleImage.setLayoutParams(mParams);
        userView.addView(bubbleLayout);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = 10;
        params.topMargin = 10;
        params.rightMargin = 10;
        params.bottomMargin = bubbleImage.getDrawable().getMinimumHeight() + 10;
        debuggerViewerLayout.setLayoutParams(params);
        if (currentViewVisibleId == -1) {
            currentViewVisibleId = R.id.eventViewer;
        }
        debuggerViewerLayout.findViewById(currentViewVisibleId).setVisibility(VISIBLE);
        if (viewerVisibility == VISIBLE) {
            debuggerViewerLayout.setVisibility(VISIBLE);
            noEventsLayout.setVisibility(debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
        } else if (viewerVisibility == GONE) {
            debuggerViewerLayout.setVisibility(GONE);
        }
        userView.addView(debuggerViewerLayout);

        if (itemPosition != -1) {
            onUpdateAfterItemClick(itemPosition, false);
        }
    }

    /**
     * Helper to create alpha animation
     *
     * @param view    View
     * @param visible boolean
     */
    static void setVisibleViewWithAnimation(View view, boolean visible) {
        AlphaAnimation animation;
        if (visible) {
            view.setVisibility(View.VISIBLE);
            animation = new AlphaAnimation(0.f, 1.f);
        } else {
            view.setVisibility(View.GONE);
            animation = new AlphaAnimation(1.f, 0.f);
        }
        animation.setDuration(400);
        view.startAnimation(animation);
    }

    /**
     * Show or hide viewer
     */
    private void toggleViewer() {
        if (viewerVisibility == GONE) {
            viewerVisibility = View.VISIBLE;
            setVisibleViewWithAnimation(debuggerViewerLayout, true);
            debuggerEventListAdapter.notifyDataSetChanged();
            debuggerOfflineHitsAdapter.notifyDataSetChanged();
            noOfflineHitsLayout.setVisibility(offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            offlineHitsListView.setVisibility(!offlineHits.isEmpty() ? View.VISIBLE : View.GONE);
            noEventsLayout.setVisibility(debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
            eventListView.setVisibility(!debuggerEvents.isEmpty() ? View.VISIBLE : View.GONE);
        } else {
            viewerVisibility = GONE;
            setVisibleViewWithAnimation(debuggerViewerLayout, false);
        }
    }

    /**
     * Get parameters
     *
     * @param hit String
     * @return HashMap
     */
    private LinkedHashMap<String, String> getParameters(String hit) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            URL url = new URL(hit);
            map.put("ssl", url.getProtocol().equals("http") ? "Off" : "On");
            map.put("log", url.getHost());
            String[] queryComponents = url.getQuery().split("&");
            for (String queryComponent : queryComponents) {
                String[] elem = queryComponent.split("=");
                if (elem.length > 1) {
                    elem[1] = Tool.percentDecode(elem[1]);
                    if (Tool.parseJSON(elem[1]) instanceof JSONObject) {
                        JSONObject json = (JSONObject) Tool.parseJSON(elem[1]);
                        if (json != null && elem[0].equals(Hit.HitParam.JSON.stringValue())) {
                            map.put(elem[0], json.toString(3));
                        } else {
                            map.put(elem[0], elem[1]);
                        }
                    } else {
                        map.put(elem[0], elem[1]);
                    }
                } else {
                    map.put(elem[0], "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Create the hit detail view
     *
     * @param parametersListView ViewGroup
     * @param message            String
     */
    private void createHitDetailView(ViewGroup parametersListView, String message) {
        ((TextView) hitDetailViewer.findViewById(R.id.headerDetailView)).setText(context.getString(R.string.hit_detail));
        LinkedHashMap<String, String> parameters = getParameters(message);
        Set<String> keySet = parameters.keySet();
        Boolean colorGrey = true;
        for (String key : keySet) {
            String value = parameters.get(key);
            LinearLayout hitDetail = (LinearLayout) inflater.inflate(R.layout.parameter_holder, null);
            if (colorGrey) {
                hitDetail.setBackgroundColor(Tool.getColor(context, R.color.at_darker_grey));
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

        private ArrayList<Debugger.DebuggerEvent> debuggerEvents = new ArrayList<Debugger.DebuggerEvent>();
        private final LayoutInflater inflater;
        private final Context context;
        private final RelativeLayout noEventsLayout;

        DebuggerEventListAdapter(Context context, ArrayList<Debugger.DebuggerEvent> debuggerEvents, RelativeLayout noEventsLayout) {
            this.context = context;
            this.noEventsLayout = noEventsLayout;
            inflater = LayoutInflater.from(context);
            this.debuggerEvents = debuggerEvents;
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

        private ArrayList<Hit> offlineHits = new ArrayList<Hit>();
        private final LayoutInflater inflater;
        private final Context context;

        DebuggerOfflineHitsAdapter(Context context, ArrayList<Hit> offlineHits, RelativeLayout noOfflineHitsLayout) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.offlineHits = offlineHits;
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
            ArrayList<Hit> temp = Debugger.getTracker().Offline().get();
            int length = Debugger.getTracker().Offline().count();
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