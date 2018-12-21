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

import org.json.JSONObject;

import java.util.Map;

/**
 * Wrapper class to inject custom data in tracking
 */
public class CustomObject extends BusinessObject {

    private String value;
    private boolean isPersistent;

    CustomObject setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Specify if CustomObject have to be permanent
     *
     * @param persistent /
     * @return CustomObject instance
     */
    public CustomObject setPersistent(boolean persistent) {
        this.isPersistent = persistent;
        return this;
    }

    CustomObject(String customObjectString) {
        super();
        value = customObjectString;
    }

    CustomObject(Map<String, Object> customObject) {
        this(new JSONObject(customObject).toString());
    }

    CustomObject(Tracker tracker) {
        super(tracker);
        value = new JSONObject().toString();
    }

    /**
     * Get the custom object value
     *
     * @return the custom data as string - json formatted
     */
    public String getValue() {
        return value;
    }

    @Override
    void setParams() {
        tracker.setParam(Hit.HitParam.JSON.stringValue(), value, new ParamOption().setAppend(true).setEncode(true).setPersistent(isPersistent).setType(ParamOption.Type.JSON));
    }
}
