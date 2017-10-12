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

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
@SuppressWarnings("unchecked")
public class BuilderTest extends AbstractTestClass {

    private Builder builder;

    @Test
    public void buildPhoneConfigurationTest() throws Exception {
        builder = new Builder(tracker);
        String result = builder.buildConfiguration();
        assertEquals(result, "http://logp.xiti.com/hit.xiti?s=552987");

        tracker.setConfig(TrackerConfigurationKeys.LOG, "", null, true);
        builder = new Builder(tracker);
        result = builder.buildConfiguration();
        assertEquals(result, "");
    }

    @Test
    public void multiHitsFailedNotSplittableParameterTest() {
        ArrayList<String> array = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            array.add("verybigvalue" + i);
        }
        buffer.getVolatileParams().put("test", new Param("test", closureValue(Tool.convertToString(array, null))));
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        String hit = hits.get(0);

        assertTrue(hit.contains("mherr=1"));
        assertEquals(1, hits.size());
    }

    @Test
    public void multiHitsFailedNotSplittableValueTest() {
        String s = "";
        for (int i = 1; i <= 150; i++) {
            s += "verybigvalue" + i;
        }
        buffer.getVolatileParams().put("stc", new Param("stc", closureValue(s)));
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(1, hits.size());
        String hit = hits.get(0);
        assertTrue(hit.contains("mherr=1"));
    }

    @Test
    public void multiHitsOkSplittableParameterTest() {
        buffer.getPersistentParams().put("idclient", new Param("idclient", new Closure() {
            @Override
            public String execute() {
                return "CustomId";
            }
        }));
        ArrayList<String> array = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            array.add("verybigvalue" + i);
        }
        ParamOption options = new ParamOption().setSeparator("|");
        buffer.getVolatileParams().put("stc", new Param("stc", closureValue(Tool.convertToString(array, "|")), options));
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(5, hits.size());
        assertFalse(hits.get(0).contains("mherr=1"));
        assertFalse(hits.get(1).contains("mherr=1"));
        assertFalse(hits.get(2).contains("mherr=1"));

        assertTrue(hits.get(0).contains("mh="));
        assertTrue(hits.get(1).contains("mh="));
        assertTrue(hits.get(2).contains("mh="));
    }

    @Test
    public void multiHitsOkSplittableHitTest() {
        buffer.getPersistentParams().put("idclient", new Param("idclient", new Closure() {
            @Override
            public String execute() {
                return "CustomId";
            }
        }));
        for (int i = 1; i <= 220; i++) {
            buffer.getVolatileParams().put("verybigkey" + i, new Param("verybigkey" + i, closureValue("verybigvalue" + i)));
        }
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(5, hits.size());
        assertFalse(hits.get(0).contains("mherr=1"));
        assertFalse(hits.get(1).contains("mherr=1"));
        assertFalse(hits.get(2).contains("mherr=1"));
        assertFalse(hits.get(3).contains("mherr=1"));
        assertFalse(hits.get(4).contains("mherr=1"));

        assertTrue(hits.get(0).contains("mh=1-5"));
        assertTrue(hits.get(1).contains("mh=2-5"));
        assertTrue(hits.get(2).contains("mh=3-5"));
        assertTrue(hits.get(3).contains("mh=4-5"));
        assertTrue(hits.get(4).contains("mh=5-5"));
    }

    @Test
    public void makeSubQueryTest() throws Exception {
        builder = new Builder(tracker);
        String result = builder.makeSubQuery("p", "test");
        assertEquals("&p=test", result);
    }

    @Test
    public void organizeParametersTest() throws Exception {
        LinkedHashMap<String, Param> completeBuffer = new LinkedHashMap<>();
        tracker.setParam("ref", "www.atinternet.com?test1=1&test2=2&test3=<script></script>")
                .setParam("map", new HashMap())
                .setParam("refstore", "test")
                .setParam("p", "page", new ParamOption().setRelativePosition(ParamOption.RelativePosition.last))
                .setParam("int", 3, new ParamOption().setRelativePosition(ParamOption.RelativePosition.first));

        completeBuffer.putAll(buffer.getVolatileParams());
        completeBuffer.putAll(buffer.getPersistentParams());

        builder = new Builder(tracker);
        ArrayList<Param> organizeParams = builder.organizeParameters(completeBuffer);

        assertEquals(5, organizeParams.size());
        assertEquals("int", organizeParams.get(0).getKey());
        assertEquals("map", organizeParams.get(1).getKey());
        assertEquals("p", organizeParams.get(2).getKey());
        assertEquals("refstore", organizeParams.get(3).getKey());
        assertEquals("ref", organizeParams.get(4).getKey());
    }

    @Test
    public void prepareQuerySuccessTest() throws Exception {
        tracker.setParam("p", "page")
                .setParam("p", "page2", new ParamOption().setAppend(true).setSeparator("--"))
                .setParam("array", "[{\"test\":\"value\"}]")
                .setParam("array", "[{\"test1\":\"value1\"}]", new ParamOption().setAppend(true))
                .setParam("test", "value")
                .setParam("stc", new HashMap() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .setParam("stc", new HashMap() {{
                    put("key3", "value3");
                }}, new ParamOption().setAppend(true).setPersistent(true))
                .setParam("stc", new HashMap() {{
                    put("obj", new HashMap() {{
                        put("subkey", "subvalue");
                    }});
                }}, new ParamOption().setAppend(true));
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(4, formattedParameters.size());

        assertEquals("&p=page--page2", formattedParameters.get("p").first);
        assertEquals("&array=[{\"test\":\"value\"}, {\"test1\":\"value1\"}]", formattedParameters.get("array").first);
        assertEquals("&test=value", formattedParameters.get("test").first);
        assertEquals("&stc={\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"obj\":{\"subkey\":\"subvalue\"}}", formattedParameters.get("stc").first);

        buffer.getVolatileParams().clear();
        builder = new Builder(tracker);
        formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&stc={\"key3\":\"value3\"}", formattedParameters.get("stc").first);
    }

    @Test
    public void overrideParam1Test() throws Exception {
        tracker.setParam("test", "value", new ParamOption().setPersistent(true))
                .setParam("test", "value2");
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value2", formattedParameters.get("test").first);
        buffer.getVolatileParams().clear();

        builder = new Builder(tracker);
        formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value", formattedParameters.get("test").first);
    }

    @Test
    public void overrideParam2Test() throws Exception {
        tracker.setParam("test", "value", new ParamOption().setPersistent(true))
                .setParam("test", "value2", new ParamOption().setAppend(true));

        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value,value2", formattedParameters.get("test").first);
    }

    @Test
    public void overrideParam3Test() throws Exception {
        tracker.setParam("test", "value2")
                .setParam("test", "value", new ParamOption().setPersistent(true));
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value", formattedParameters.get("test").first);
    }

    @Test
    public void appendParam1Test() throws Exception {
        tracker.setParam("test", "value2")
                .setParam("test", "value", new ParamOption().setPersistent(true).setAppend(true))
                .setParam("test", "value3", new ParamOption().setAppend(true));
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value2,value,value3", formattedParameters.get("test").first);

        buffer.getVolatileParams().clear();
        builder = new Builder(tracker);
        formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value", formattedParameters.get("test").first);
    }

    @Test
    public void appendParam2Test() throws Exception {
        tracker.setParam("test", "value2")
                .setParam("test", "value", new ParamOption().setPersistent(true).setAppend(true))
                .setParam("test", "value3");
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value3", formattedParameters.get("test").first);

        buffer.getVolatileParams().clear();
        builder = new Builder(tracker);
        formattedParameters = builder.prepareQuery();

        assertEquals(1, formattedParameters.size());
        assertEquals("&test=value", formattedParameters.get("test").first);
    }

    @Test
    public void prepareQueryCannotAppendValuesTest() throws Exception {
        tracker.setParam("array", "[{\"test\":\"value\"}]")
                .setParam("array", "value", new ParamOption().setAppend(true))
                .setParam("stc", new HashMap() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .setParam("stc", 4, new ParamOption().setAppend(true));
        builder = new Builder(tracker);
        LinkedHashMap<String, Pair<String, String>> formattedParameters = builder.prepareQuery();

        assertEquals(2, formattedParameters.size());

        assertEquals("&array=[{\"test\":\"value\"}]", formattedParameters.get("array").first);
        assertEquals("&stc={\"key1\":\"value1\",\"key2\":\"value2\"}", formattedParameters.get("stc").first);
    }

    @Test
    public void buildTest() throws Exception {
        tracker.setParam("p", "page")
                .setParam("p", "page2", new ParamOption().setAppend(true).setSeparator("--"))
                .setParam("array", "[{\"test\":\"value\"}]")
                .setParam("test", "value")
                .setParam("stc", new HashMap() {{
                    put("key1", "value1");
                    put("key2", "value2");
                }})
                .setParam("stc", new HashMap() {{
                    put("key3", "value3");
                    put("obj", new HashMap() {{
                        put("subkey", "subvalue");
                    }});
                }}, new ParamOption().setAppend(true));
        builder = new Builder(tracker);
        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(1, hits.size());

        assertEquals("http://logp.xiti.com/hit.xiti?s=552987&p=page--page2&array=[{\"test\":\"value\"}]&test=value&stc={\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\",\"obj\":{\"subkey\":\"subvalue\"}}", hits.get(0));
    }

    private Closure closureValue(final Object object) {
        return new Closure() {
            @Override
            public String execute() {
                return String.valueOf(object);
            }
        };
    }
}
