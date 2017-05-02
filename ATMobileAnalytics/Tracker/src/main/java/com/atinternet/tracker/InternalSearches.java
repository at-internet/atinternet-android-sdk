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
 * Wrapper class to manage InternalSearch instances
 *
 * @deprecated Since 2.3.0, InternalSearch is now only available as a screen or gesture object property.
 */
@Deprecated
public class InternalSearches extends Helper {

    InternalSearches(Tracker tracker) {
        super(tracker);
    }

    /**
     * Add an internal search
     *
     * @param keywordLabel     internal search keyword
     * @param resultPageNumber internal search result screen number
     * @return InternalSearch instance
     */
    public InternalSearch add(String keywordLabel, int resultPageNumber) {
        InternalSearch search = new InternalSearch(tracker)
                .setKeyword(keywordLabel)
                .setResultScreenNumber(resultPageNumber);

        tracker.getBusinessObjects().put(search.getId(), search);

        return search;
    }

    /**
     * Add an internal search
     *
     * @param keywordLabel     internal search keyword
     * @param resultPageNumber internal search result screen number
     * @param resultPosition   internal search result position
     * @return InternalSearch instance
     */
    public InternalSearch add(String keywordLabel, int resultPageNumber, int resultPosition) {
        return add(keywordLabel, resultPageNumber)
                .setResultPosition(resultPosition);
    }
}
