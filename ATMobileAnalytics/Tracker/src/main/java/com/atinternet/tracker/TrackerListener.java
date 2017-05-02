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

/**
 * Send different events during hit building
 */
public interface TrackerListener {

    /**
     * Status enum
     */
    enum HitStatus {
        /**
         * Failed status
         */
        Failed,
        /**
         * Success status
         */
        Success
    }

    /**
     * Notify when tracker needs first launched approval
     *
     * @param message String: approval message for confidentiality
     */
    void trackerNeedsFirstLaunchApproval(String message);

    /**
     * Notify when hit has been built
     *
     * @param status  HitStatus: status of hit building
     * @param message String: query string or error message
     */
    void buildDidEnd(HitStatus status, String message);

    /**
     * Notify when hit has been sent
     *
     * @param status  HitStatus: status of hit sending
     * @param message String: query string or http response message
     */
    void sendDidEnd(HitStatus status, String message);

    /**
     * Notify when a partner has been called
     *
     * @param response String: the partner response
     */
    void didCallPartner(String response);

    /**
     * Notify when a warning has been detected
     *
     * @param message String: the warning message
     */
    void warningDidOccur(String message);


    /**
     * Notify when hit has been saved in device local storage
     *
     * @param message String: the saved hit
     */
    void saveDidEnd(String message);

    /**
     * Notify when a critical error has been detected
     *
     * @param message String: the error message
     */
    void errorDidOccur(String message);
}
