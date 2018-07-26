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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class EventsTest extends AbstractTestClass {

    private Events events;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        events = new Events(tracker);
    }

    @Test
    public void setParamsOneTest() throws JSONException {
        events.add("act", new HashMap<String, Object>() {{
            put("test1", "value1");
        }});

        events.setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("events").getValues().size());

        JSONArray events = new JSONArray(buffer.getVolatileParams().get("events").getValues().get(0).execute());
        assertEquals(1, events.length());

        JSONObject event = events.getJSONObject(0);
        assertEquals(2, event.length());
        assertEquals("act", event.getString("action"));

        JSONObject data = event.getJSONObject("data");
        assertEquals(1, data.length());
        assertEquals("value1", data.getString("test1"));


        assertEquals(1, buffer.getVolatileParams().get("col").getValues().size());
        assertEquals("2", buffer.getVolatileParams().get("col").getValues().get(0).execute());
    }

    @Test
    public void setParamsTwoTest() throws JSONException {
        Event e = events.add("act", new HashMap<String, Object>() {{
            put("test1", true);
        }});
        e.setData(new HashMap<String, Object>() {{
            put("test2", "value");
        }});
        e.setAction("actionn");

        events.setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("events").getValues().size());

        JSONArray events = new JSONArray(buffer.getVolatileParams().get("events").getValues().get(0).execute());
        assertEquals(1, events.length());

        JSONObject event = events.getJSONObject(0);
        assertEquals(2, event.length());
        assertEquals("actionn", event.getString("action"));

        JSONObject data = event.getJSONObject("data");
        assertEquals(1, data.length());
        assertEquals("value", data.getString("test2"));


        assertEquals(1, buffer.getVolatileParams().get("col").getValues().size());
        assertEquals("2", buffer.getVolatileParams().get("col").getValues().get(0).execute());
    }
}
