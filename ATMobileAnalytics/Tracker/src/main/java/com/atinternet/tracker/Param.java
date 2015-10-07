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
 * Param
 */
class Param {

    /**
     * Param type
     */
    enum Type {
        Integer, Double, Float, String, Boolean, Array, JSON, Closure, Unknown
    }

    /**
     * Param key
     */
    private String key;

    /**
     * Param value
     */
    private Closure value;

    /**
     * Param type
     */
    private Type type;

    /**
     * Param options
     */
    private ParamOption paramOption;

    /**
     * Get key
     *
     * @return String
     */
    public String getKey() {
        return key;
    }

    /**
     * Set a new key
     *
     * @param key String
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the value
     *
     * @return Closure
     */
    public Closure getValue() {
        return value;
    }

    /**
     * Set a new value
     *
     * @param value Closure
     */
    public void setValue(Closure value) {
        this.value = value;
    }

    /**
     * Get the type
     *
     * @return Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type
     *
     * @param type Type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Get options
     *
     * @return ParamOption
     */
    public ParamOption getOptions() {
        return paramOption;
    }

    /**
     * Set new options
     *
     * @param paramOption ParamOption
     */
    public void setOptions(ParamOption paramOption) {
        this.paramOption = paramOption;
    }

    /**
     * Default constructor
     */
    public Param() {
        key = "";
        value = null;
        paramOption = null;
        type = Type.Unknown;
    }

    /**
     * Constructor
     *
     * @param key   String
     * @param value Closure
     * @param type  Type
     */
    public Param(String key, Closure value, Type type) {
        this();
        this.key = key;
        this.value = value;
        this.type = type;
    }

    /**
     * Constructor
     *
     * @param key             String
     * @param value           Closure
     * @param type            Type
     * @param paramOption ParamOption
     */
    public Param(String key, Closure value, Type type, ParamOption paramOption) {
        this(key, value, type);
        this.paramOption = paramOption;
    }
}