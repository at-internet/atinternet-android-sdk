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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.robolectric.Robolectric;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class AbstractTestClass {

    final String pattern = "dd-MM-yyyy";
    String today;
    Tracker tracker;

    @Before
    public void setUp() throws Exception {
        Configuration configuration = new Configuration(createDefaultConfiguration(false));
        tracker = new Tracker(Robolectric.application, configuration);
    }

    // Méthode rendant accessible un attribut privé
    Object getAccessibleField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    // Méthode rendant accessible une méthode privée
    Object executePrivateMethod(Object instance, String name, Object[] params) throws Exception {
        Class c = instance.getClass();
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method m = c.getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m.invoke(instance, params);
    }

    // Méthode de simulation de récupération de la configuration par défaut
    LinkedHashMap<String, Object> createDefaultConfiguration(boolean isTablet) throws IOException, JSONException {
        String jsonString;
        InputStream is = Robolectric.application.getAssets().open("defaultConfiguration.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        jsonString = new String(buffer, "UTF-8");

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject config;
        if (isTablet) {
            config = jsonObject.getJSONObject("tablet");
        } else {
            config = jsonObject.getJSONObject("phone");
        }

        LinkedHashMap<String, Object> dictionary = new LinkedHashMap<String, Object>();
        Iterator<String> iterator = config.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            dictionary.put(key, config.get(key));
        }

        return dictionary;
    }
}
