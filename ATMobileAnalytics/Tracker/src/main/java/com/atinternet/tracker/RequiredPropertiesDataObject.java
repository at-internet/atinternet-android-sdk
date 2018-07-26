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

import java.util.HashMap;
import java.util.Map;

public abstract class RequiredPropertiesDataObject extends HashMap<String, Object> {

    protected final Map<String, String> propertiesPrefixMap;

    protected RequiredPropertiesDataObject() {
        propertiesPrefixMap = new HashMap<>();
    }

    /***
     * Override base method to set prefix if key are known
     * @param key String
     * @param value Object
     * @return Object
     */
    @Override
    public Object put(String key, Object value) {
        String prefix = propertiesPrefixMap.get(key);

        if (prefix != null) {
            key = String.format("%s:%s", prefix, key);
        }
        return super.put(key, value);
    }

    /***
     * Override base method to check if properties are known and add prefix key
     * @param m Map
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        Map<String, Object> result = new HashMap<>();

        for (Entry<? extends String, ?> entry : m.entrySet()) {
            String entryKey = entry.getKey();
            String prefix = propertiesPrefixMap.get(entryKey);

            if (prefix != null) {
                entryKey = String.format("%s:%s", prefix, entryKey);
            }
            result.put(entryKey, entry.getValue());
        }
        super.putAll(result);
    }

    /***
     * Alias to putAll
     * @param obj Map
     */
    public void set(Map<String, Object> obj) {
        putAll(obj);
    }
}
