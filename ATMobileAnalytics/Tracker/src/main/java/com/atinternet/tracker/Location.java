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

import java.text.DecimalFormat;

/**
 * Wrapper class for gps location tracking
 */
public class Location extends ScreenInfo {

    private static final String DECIMAL_FORMAT = "#.00";

    private double latitude;
    private double longitude;

    Location(Tracker tracker) {
        super(tracker);
        latitude = -1.;
        longitude = -1.;
    }

    /**
     * Get the latitude value
     *
     * @return the latitude value
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Get the longitude value
     *
     * @return the longitude value
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Set a new latitude
     *
     * @param latitude /
     * @return Location instance
     */
    public Location setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Set a new longitude
     *
     * @param longitude /
     * @return Location instance
     */
    public Location setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    @Override
    void setParams() {
        DecimalFormat format = new DecimalFormat(DECIMAL_FORMAT);
        tracker.setParam(Hit.HitParam.GPSLatitude.stringValue(), format.format(latitude).replace(",", "."))
                .setParam(Hit.HitParam.GPSLongitude.stringValue(), format.format(longitude).replace(",", "."));
    }
}
