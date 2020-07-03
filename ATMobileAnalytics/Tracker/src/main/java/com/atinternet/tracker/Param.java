/*
 * This SDK is licensed under the MIT license (MIT)
 * Copyright (c) 2015- Applied Technologies Internet SAS (registration number B 403 261 258 - Trade and Companies Register of Bordeaux – France)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.atinternet.tracker;

import java.util.ArrayList;
import java.util.List;

public class Param {

    private String key;
    private List<Closure> values;
    private ParamOption paramOption;

    Param() {
        key = "";
        values = new ArrayList<>();
        paramOption = null;
    }

    Param(String key, final Closure value) {
        this();
        this.key = key;
        this.values.add(value);
    }

    Param(String key, Closure value, ParamOption paramOption) {
        this(key, value);
        this.paramOption = paramOption;
    }

    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    public List<Closure> getValues() {
        return values;
    }

    void setValue(Closure value) {
        values.clear();
        values.add(value);
    }

    void setValues(List<Closure> values) {
        this.values = values;
    }

    ParamOption getOptions() {
        return paramOption;
    }

    void setOptions(ParamOption paramOption) {
        this.paramOption = paramOption;
    }

    public boolean isPersistent() {
        return paramOption != null && paramOption.isPersistent();
    }

    boolean isProperty() {
        return paramOption != null && paramOption.isProperty();
    }
}
