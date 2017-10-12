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
 * Class to customize parameter options
 */
public class ParamOption {

    /**
     * Enum for relative position
     */
    public enum RelativePosition {
        /**
         * Undefined
         */
        none,
        /**
         * First parameter
         */
        first,
        /**
         * Last parameter (/!\ Cannot override the referrer parameter position, it must be the last if it's defined)
         */
        last,
        /**
         * Before an other parameter (use {@link #setRelativeParameterKey(String)} with that)
         */
        before,
        /**
         * After an other parameter (use {@link #setRelativeParameterKey(String)} with that)
         */
        after
    }

    private RelativePosition relativePosition;
    private String relativeParameterKey;
    private boolean encode;
    private String separator;
    private boolean persistent;
    private boolean append;

    /**
     * Get relative position parameter
     *
     * @return the relative position
     */
    public RelativePosition getRelativePosition() {
        return relativePosition;
    }

    /**
     * Get parameter key attached with relative position
     *
     * @return the parameter key
     */
    public String getRelativeParameterKey() {
        return relativeParameterKey;
    }

    /**
     * Get boolean "encode" value
     *
     * @return true if the parameter must be url encoded
     */
    public boolean isEncode() {
        return encode;
    }

    /**
     * Get separator for a parameter with multiple values
     *
     * @return values separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Get boolean "persistent" value
     *
     * @return true if the parameter must be present in all hits during session
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Get boolean "append" value
     *
     * @return true if parameter should have multiple values
     */
    public boolean isAppend() {
        return append;
    }

    /**
     * Set a new relative position
     *
     * @param relativePosition /
     * @return ParamOption instance
     */
    public ParamOption setRelativePosition(RelativePosition relativePosition) {
        this.relativePosition = relativePosition;
        return this;
    }

    /**
     * Set a new relative position parameter key
     *
     * @param relativeParameterKey /
     * @return ParamOption instance
     */
    public ParamOption setRelativeParameterKey(String relativeParameterKey) {
        this.relativeParameterKey = relativeParameterKey;
        return this;
    }

    /**
     * Set a new boolean "encode" value
     *
     * @param encode /
     * @return ParamOption instance
     */
    public ParamOption setEncode(boolean encode) {
        this.encode = encode;
        return this;
    }

    /**
     * Set a new separator
     *
     * @param separator /
     * @return ParamOption instance
     */
    public ParamOption setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * Set a new boolean "persistent" value
     *
     * @param isPermanent /
     * @return ParamOption instance
     */
    public ParamOption setPersistent(boolean isPermanent) {
        this.persistent = isPermanent;
        return this;
    }

    /**
     * Set a new boolean "append" value
     *
     * @param append /
     * @return ParamOption instance
     */
    public ParamOption setAppend(boolean append) {
        this.append = append;
        return this;
    }

    /**
     * Default constructor
     */
    public ParamOption() {
        relativePosition = RelativePosition.none;
        relativeParameterKey = "";
        separator = ",";
        encode = false;
        persistent = false;
        append = false;
    }
}
