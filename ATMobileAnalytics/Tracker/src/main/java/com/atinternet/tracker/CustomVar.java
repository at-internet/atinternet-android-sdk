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
 * Wrapper class to inject custom variables
 */
public class CustomVar extends ScreenInfo {

    /**
     * Enum with different CustomVarType
     */
    public enum CustomVarType {
        /**
         * App custom var
         */
        App("x"),
        /**
         * Screen custom var
         */
        Screen("f");

        private final String str;

        CustomVarType(String val) {
            str = val;
        }

        public String stringValue() {
            return str;
        }
    }

    private String value;
    private CustomVarType customVarType;
    private int varId;

    CustomVar setVarId(int varId) {
        this.varId = varId;
        return this;
    }

    CustomVar setValue(String value) {
        this.value = value;

        return this;
    }

    CustomVar setCustomVarType(CustomVarType customVarType) {
        this.customVarType = customVarType;

        return this;
    }

    CustomVar(Tracker tracker) {
        super(tracker);
        varId = -1;
        customVarType = CustomVarType.App;
        value = "";
    }

    /**
     * Get the custom variable identifier
     *
     * @return the custom variable identifier
     */
    public int getVarId() {
        return varId;
    }

    /**
     * Get the custom variable value
     *
     * @return the custom variable value
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the custom variable type
     *
     * @return the custom variable type
     */
    public CustomVarType getCustomVarType() {
        return customVarType;
    }

    @Override
    void setParams() {
        varId = varId < 1 ? 1 : varId;
        tracker.setParam(customVarType.stringValue() + varId, value, new ParamOption().setEncode(true));
    }
}
