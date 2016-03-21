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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.atinternet.tracker.Tracker.*;

class Storage extends SQLiteOpenHelper {

    /**
     * Offline mode
     */
    private OfflineMode offlineMode;

    /**
     * SQLITEDatabase requirements
     */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TrackerDatabase";
    private static final String HITS_STORAGE_TABLE = "StoredOfflineHit";
    private static final String ID = "id";
    private static final String HIT = "hit";
    private static final String RETRY = "retry";
    private static final String DATE = "date";

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS " + HITS_STORAGE_TABLE + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    HIT + " TEXT NOT NULL , " +
                    DATE + " INTEGER NOT NULL , " +
                    RETRY + " INTEGER NOT NULL);";

    /**
     * Get offline mode
     *
     * @return OfflineMode
     */
    public OfflineMode getOfflineMode() {
        return offlineMode;
    }

    /**
     * Set offline mode
     *
     * @param offlineMode OfflineMode
     */
    public void setOfflineMode(OfflineMode offlineMode) {
        this.offlineMode = offlineMode;
    }

    /**
     * Init a Storage
     *
     * @param context Context
     */
    public Storage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.offlineMode = OfflineMode.required;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HITS_STORAGE_TABLE);
        onCreate(db);
    }

    /**
     * Save hit
     *
     * @param hit String
     * @return String
     */
    public String saveHit(String hit, long time, String oltParameter) {
        hit = buildHitToStore(hit, oltParameter);
        ContentValues values = new ContentValues();
        values.put(HIT, hit);
        values.put(RETRY, 0);
        values.put(DATE, time);
        getWritableDatabase().insert(HITS_STORAGE_TABLE, null, values);
        return hit;
    }

    /**
     * Delete one hit from database
     *
     * @param hit String
     */
    public void deleteHit(String hit) {
        getWritableDatabase().delete(HITS_STORAGE_TABLE, HIT + "='" + hit + "'", null);
    }

    /**
     * Add olt and change cn into the hit
     *
     * @param hit String
     * @param olt String
     * @return String
     */
    String buildHitToStore(String hit, String olt) {
        String[] hitComponents = hit.split("&");
        String newHit = hitComponents[0];

        for (int i = 1; i < hitComponents.length; i++) {
            String[] parameterComponents = hitComponents[i].split("=");

            if (parameterComponents[0].equals("cn")) {
                newHit += "&cn=offline";
            } else {
                newHit += "&" + hitComponents[i];
            }

            if (parameterComponents[0].equals("ts") || parameterComponents[0].equals("mh")) {
                newHit += "&olt=" + olt;
            }
        }
        return newHit;
    }

    /**
     * Update retry for one record from database
     *
     * @param hit   String
     * @param retry int
     */
    public void updateRetry(String hit, int retry) {
        ContentValues values = new ContentValues();
        values.put(RETRY, retry);
        getWritableDatabase().update(HITS_STORAGE_TABLE, values, HIT + "='" + hit + "'", null);
    }

    /**
     * Get count of hits offline
     *
     * @return int
     */
    public int getCountOfflineHits() {
        int result = -1;
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + HITS_STORAGE_TABLE, null);
        if (c != null) {
            result = c.getCount();
            c.close();
        }
        return result;
    }

    /**
     * Remove all offline hits
     */
    public void removeAllOfflineHits() {
        getWritableDatabase().delete(HITS_STORAGE_TABLE, null, null);
    }

    /**
     * Remove offline hit older than storage duration
     */
    public void removeOldOfflineHits(int storageDuration) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -storageDuration);
        long maxOldDate = cal.getTime().getTime();
        getWritableDatabase().delete(HITS_STORAGE_TABLE, DATE + " < " + maxOldDate, null);
    }

    /**
     * Get offline hits
     *
     * @return ArrayList<OfflineHit>
     */
    public ArrayList<Hit> getOfflineHits() {
        ArrayList<Hit> hits = new ArrayList<Hit>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + HITS_STORAGE_TABLE + " ORDER BY " + ID + " ASC", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                String hit = c.getString(c.getColumnIndex(HIT));
                String time = c.getString(c.getColumnIndex(DATE));
                int retry = c.getInt(c.getColumnIndex(RETRY));
                hits.add(new Hit(hit, new Date(Long.parseLong(time)), retry, true));
            }
            while (c.moveToNext());
            c.close();
        }
        return hits;
    }

    /**
     * Get the oldest saved hit
     *
     * @return OfflineHit
     */
    public Hit getOldestOfflineHit() {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + HITS_STORAGE_TABLE + " WHERE " + DATE + " = (SELECT MIN(" + DATE + ") FROM " + HITS_STORAGE_TABLE + " )", null);
        if (c != null && c.moveToFirst()) {
            String hit = c.getString(c.getColumnIndex(HIT));
            String time = c.getString(c.getColumnIndex(DATE));
            int retry = c.getInt(c.getColumnIndex(RETRY));
            if (hit != null && time != null) {
                c.close();
                return new Hit(hit, new Date(Long.parseLong(time)), retry, true);
            }
            c.close();
        }
        return null;
    }

    /**
     * Get the latest saved hit
     *
     * @return OfflineHit
     */
    public Hit getLatestOfflineHit() {
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + HITS_STORAGE_TABLE + " WHERE " + DATE + " = (SELECT MAX(" + DATE + ") FROM " + HITS_STORAGE_TABLE + " )", null);
        if (c != null && c.moveToFirst()) {
            String hit = c.getString(c.getColumnIndex(HIT));
            String time = c.getString(c.getColumnIndex(DATE));
            int retry = c.getInt(c.getColumnIndex(RETRY));
            if (hit != null && time != null) {
                c.close();
                return new Hit(hit, new Date(Long.parseLong(time)), retry, true);
            }
            c.close();
        }
        return null;
    }
}
