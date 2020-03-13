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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RequiredPropertiesDataObject {

    private final Map<String, Object> properties;

    protected RequiredPropertiesDataObject() {
        properties = new ConcurrentHashMap<>();
    }

    /***
     * Get all properties set
     * @return Map
     */
    public Map<String, Object> getProps() {
        return properties;
    }

    /***
     * Return true if properties map is empty
     * @return boolean
     */
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    /***
     * Get a property
     * @param key String
     * @return Object
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /***
     * Set a property with prefix if needed
     * @param key String
     * @param value Object
     * @return RequiredPropertiesDataObject instance
     */
    public RequiredPropertiesDataObject set(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    /***
     * Delete property
     * @param key String
     * @return RequiredPropertiesDataObject instance
     */
    public RequiredPropertiesDataObject del(String key) {
        properties.remove(key);
        return this;
    }

    /***
     * Set all properties with prefix if needed
     * @param obj Map
     * @return RequiredPropertiesDataObject instance
     */
    public RequiredPropertiesDataObject setProps(Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /***
     * Delete all properties
     * @return RequiredPropertiesDataObject instance
     */
    public RequiredPropertiesDataObject delProps() {
        properties.clear();
        return this;
    }

    /***
     * Get all properties set
     * @return Map
     * @deprecated Since 2.16.0, use getProps instead
     */
    @Deprecated
    public Map<String, Object> getAll() {
        return getProps();
    }

    /***
     * Set all properties with prefix if needed
     * @param obj Map
     * @return RequiredPropertiesDataObject instance
     * @deprecated Since 2.16.0, use setProps instead
     */
    @Deprecated
    public RequiredPropertiesDataObject setAll(Map<String, Object> obj) {
        return setProps(obj);
    }
}
