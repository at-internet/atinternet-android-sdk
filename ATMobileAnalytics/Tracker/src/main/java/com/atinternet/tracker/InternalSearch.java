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
 * Wrapper class for internal search tracking
 */
public class InternalSearch extends BusinessObject {

    private String keyword;
    private int resultScreenNumber;
    private int resultPosition;

    InternalSearch(Tracker tracker) {
        super(tracker);
        keyword = null;
        resultScreenNumber = 1;
        resultPosition = -1;
    }

    /**
     * Get keyword
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Get result screen number
     *
     * @return the result screen number
     */
    public int getResultScreenNumber() {
        return resultScreenNumber;
    }

    /**
     * Get result position
     *
     * @return the result position
     */
    public int getResultPosition() {
        return resultPosition;
    }

    /**
     * Set a new keyword
     *
     * @param keyword /
     * @return InternalSearch instance
     */
    public InternalSearch setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    /**
     * Set a new result screen number
     *
     * @param resultScreenNumber /
     * @return InternalSearch instance
     */
    public InternalSearch setResultScreenNumber(int resultScreenNumber) {
        this.resultScreenNumber = resultScreenNumber;
        return this;
    }

    /**
     * Set a new result position
     *
     * @param resultPosition /
     * @return InternalSearch instance
     */
    public InternalSearch setResultPosition(int resultPosition) {
        this.resultPosition = resultPosition;
        return this;
    }

    @Override
    void setParams() {
        tracker.setParam(Hit.HitParam.InternalSearchKeyword.stringValue(), keyword == null ? "" : keyword.replaceAll("\\W", ""))
                .setParam(Hit.HitParam.InternalSearchResultScreenNumber.stringValue(), resultScreenNumber);

        if (resultPosition > -1) {
            tracker.setParam(Hit.HitParam.InternalSearchResultPosition.stringValue(), resultPosition);
        }
    }
}
