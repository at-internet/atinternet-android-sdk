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

import java.util.HashMap;
import java.util.Map;

public abstract class RequiredPropertiesDataObject {

    protected final Map<String, Object> properties;
    protected final Map<String, String> propertiesPrefix;

    protected RequiredPropertiesDataObject() {
        properties = new HashMap<>();
        propertiesPrefix = new HashMap<>();
    }

    /***
     * Get all properties set
     * @return Map
     */
    public Map<String, Object> getAll() {
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
        String prefix = propertiesPrefix.get(key);

        if (prefix != null) {
            properties.put(String.format("%s:%s", prefix, key), value);
        } else {
            properties.put(key, value);
        }
        return this;
    }

    /***
     * Set all properties with prefix if needed
     * @param obj Map
     * @return RequiredPropertiesDataObject instance
     */
    public RequiredPropertiesDataObject setAll(Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String entryKey = entry.getKey();
            String prefix = propertiesPrefix.get(entryKey);

            if (prefix != null) {
                properties.put(String.format("%s:%s", prefix, entryKey), entry.getValue());
            } else {
                properties.put(entryKey, entry.getValue());
            }
        }
        return this;
    }
}
