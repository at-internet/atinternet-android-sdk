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

public class ParamOption {

    /**
     * Enum for relative position
     */
    public enum RelativePosition {
        none, first, last, before, after
    }

    /**
     * Relative position parameter
     */
    private RelativePosition relativePosition;

    /**
     * Relative position parameter key
     */
    private String relativeParameterKey;

    /**
     * Encoding parameter
     */
    private boolean encode;

    /**
     * Separator for list
     */
    private String separator;

    /**
     * Persistent or volatile
     */
    private boolean persistent;

    /**
     * Option to append data into a same parameter
     */
    private boolean append;

    /**
     * Get relative position
     *
     * @return RelativePosition
     */
    public RelativePosition getRelativePosition() {
        return relativePosition;
    }

    /**
     * Set a new relative position
     *
     * @param relativePosition RelativePosition
     * @return ParamOption
     */
    public ParamOption setRelativePosition(RelativePosition relativePosition) {
        this.relativePosition = relativePosition;
        return this;
    }

    /**
     * Get relative position parameter key
     *
     * @return String
     */
    public String getRelativeParameterKey() {
        return relativeParameterKey;
    }

    /**
     * Set a new relative position parameter key
     *
     * @param relativeParameterKey String
     * @return ParamOption
     */
    public ParamOption setRelativeParameterKey(String relativeParameterKey) {
        this.relativeParameterKey = relativeParameterKey;
        return this;
    }

    /**
     * Encoding parameter
     *
     * @return boolean
     */
    public boolean isEncode() {
        return encode;
    }

    /**
     * Set encoding
     *
     * @param encode boolean
     * @return ParamOption
     */
    public ParamOption setEncode(boolean encode) {
        this.encode = encode;
        return this;
    }

    /**
     * Get separator
     *
     * @return String
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Set a new separator
     *
     * @param separator String
     * @return ParamOption
     */
    public ParamOption setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * Persistent or volatile
     *
     * @return boolean
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Set persistent value
     *
     * @param isPermanent boolean
     * @return ParamOption
     */
    public ParamOption setPersistent(boolean isPermanent) {
        this.persistent = isPermanent;
        return this;
    }

    /**
     * Get append value
     *
     * @return boolean
     */
    public boolean isAppend() {
        return append;
    }

    /**
     * Set append value
     *
     * @param append boolean
     * @return ParamOption
     */
    public ParamOption setAppend(boolean append) {
        this.append = append;
        return this;
    }

    /**
     * Init a parameter option
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
