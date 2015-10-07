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

public interface TrackerListener {

    /**
     * Enum for Tracker callbacks
     */
    enum HitStatus {
        Failed, Success
    }

    /**
     * First launched approbation callback
     *
     * @param message String
     */
    void trackerNeedsFirstLaunchApproval(String message);

    /**
     * End hit construction callback
     *
     * @param status  HitStatus
     * @param message String
     */
    void buildDidEnd(HitStatus status, String message);

    /**
     * End sending hit callback
     *
     * @param status  HitStatus
     * @param message String
     */
    void sendDidEnd(HitStatus status, String message);

    /**
     * Call partner callback
     *
     * @param response String
     */
    void didCallPartner(String response);

    /**
     * Warning callback
     *
     * @param message String
     */
    void warningDidOccur(String message);


    /**
     * Save did end callback
     *
     * @param message String
     */
    void saveDidEnd(String message);

    /**
     * Error callback
     *
     * @param message String
     */
    void errorDidOccur(String message);
}
