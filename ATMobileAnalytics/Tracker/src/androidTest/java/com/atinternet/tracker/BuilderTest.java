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

import android.text.format.DateFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class BuilderTest extends AbstractTestClass {

    private Buffer buffer;
    private Builder builder;
    private Param intParam;
    private Param floatParam;
    private Param booleanParam;
    private Param arrayParam;
    private Param mapParam;
    private Param closureParam;
    private Param stringParam;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        prepareBuilderForTest();
    }

    @Test
    public void buildPhoneConfigurationTest() throws Exception {
        builder = new Builder(tracker);
        String result = (String) executePrivateMethod(builder, "buildConfiguration", new Object[0]);
        assertEquals(result, "http://logp.xiti.com/hit.xiti?s=552987");
    }

    @Test
    public void buildClosureTest() {
        String today = getDate().execute();

        buffer.getVolatileParams().add(stringParam);
        buffer.getVolatileParams().add(closureParam);
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);

        String homeFormat = hit.split("&")[1];
        String dateFormat = hit.split("&")[2];

        assertEquals("home", homeFormat.split("=")[1]);
        assertEquals(today, dateFormat.split("=")[1]);
    }

    @Test
    public void buildFloatTest() {
        buffer.getVolatileParams().add(floatParam);

        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String floatFormat = hit.split("&")[1];

        assertEquals("3.145", floatFormat.split("=")[1]);
    }

    @Test
    public void buildIntTest() {
        buffer.getVolatileParams().add(intParam);
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String intFormat = hit.split("&")[1];

        assertEquals("20", intFormat.split("=")[1]);
    }

    @Test
    public void buildBooleanTest() {
        buffer.getVolatileParams().add(booleanParam);
        buffer.getVolatileParams().add(new Param("falseBoolean", closureValue(false), Param.Type.Array));
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String trueBooleanFormat = hit.split("&")[1];
        String falseBooleanFormat = hit.split("&")[2];

        assertEquals("true", trueBooleanFormat.split("=")[1]);
        assertEquals("false", falseBooleanFormat.split("=")[1]);
    }

    @Test
    public void buildMapTest() {
        buffer.getVolatileParams().add(mapParam);
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String mapFormat = hit.split("&")[1];

        assertEquals("{\"fruit\":\"orange\"}", mapFormat.split("=")[1]);
    }

    @Test
    public void buildArrayTest() {
        buffer.getVolatileParams().add(arrayParam);
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String arrayFormat = hit.split("&")[1];

        assertEquals("jeu,dvd,bluray", arrayFormat.split("=")[1]);
    }

    @Test
    public void buildArrayWithOptionTest() {
        arrayParam = new Param("array", closureValue(Tool.convertToString(new ArrayList<Object>() {{
            add("jeu");
            add("dvd");
            add("bluray");
        }}, "/")), Param.Type.Array);
        buffer.getVolatileParams().add(arrayParam);
        builder = new Builder(tracker);
        String hit = ((ArrayList<String>) builder.build()[0]).get(0);
        String arrayFormat = hit.split("&")[1];

        assertEquals("jeu/dvd/bluray", arrayFormat.split("=")[1]);
    }

    @Test
    public void multiHitsFailedNotSplittableParameterTest() {
        ArrayList<String> array = new ArrayList<String>();
        for (int i = 1; i <= 150; i++) {
            array.add("verybigvalue" + i);
        }
        buffer.getVolatileParams().add(new Param("test", closureValue(Tool.convertToString(array, null)), Param.Type.Array));
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
        buffer.getVolatileParams().add(new Param("stc", closureValue(s), Param.Type.String));
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(1, hits.size());
        String hit = hits.get(0);
        assertTrue(hit.contains("mherr=1"));
    }

    @Test
    public void multiHitsOkSplittableParameterTest() {
        ArrayList<String> array = new ArrayList<String>();
        for (int i = 1; i <= 200; i++) {
            array.add("verybigvalue" + i);
        }
        ParamOption options = new ParamOption().setSeparator("|");
        buffer.getVolatileParams().add(new Param("stc", closureValue(Tool.convertToString(array, "|")), Param.Type.Array, options));
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(5, hits.size());
        assertFalse(hits.get(0).contains("mherr=1"));
        assertFalse(hits.get(1).contains("mherr=1"));
        assertFalse(hits.get(2).contains("mherr=1"));
    }

    @Test
    public void multiHitsOkSplittableHitTest() {
        for (int i = 1; i <= 200; i++) {
            buffer.getVolatileParams().add(new Param("verybigkey" + i, closureValue("verybigvalue" + i), Param.Type.Array));
        }
        builder = new Builder(tracker);

        ArrayList<String> hits = (ArrayList<String>) builder.build()[0];
        assertEquals(4, hits.size());
        assertFalse(hits.get(0).contains("mherr=1"));
        assertFalse(hits.get(1).contains("mherr=1"));
        assertFalse(hits.get(2).contains("mherr=1"));
        assertFalse(hits.get(3).contains("mherr=1"));
    }

    @Test
    public void makeSubQueryTest() throws Exception {
        builder = new Builder(tracker);
        String result = (String) executePrivateMethod(builder, "makeSubQuery", new Object[]{"p", "test"});
        assertEquals("&p=test", result);
    }

    @Test
    public void organizeParametersTest() throws Exception {
        ArrayList<Param> completeBuffer = new ArrayList<Param>();
        buffer.getPersistentParams().add(stringParam);
        buffer.getPersistentParams().add(mapParam);
        buffer.getPersistentParams().add(arrayParam);
        buffer.getPersistentParams().add(booleanParam);
        buffer.getPersistentParams().add(intParam);
        completeBuffer.addAll(buffer.getVolatileParams());
        completeBuffer.addAll(buffer.getPersistentParams());
        builder = new Builder(tracker);
        ArrayList<Param> organizeParams = (ArrayList<Param>) executePrivateMethod(builder, "organizeParameters", new Object[]{completeBuffer});

        assertEquals("p", organizeParams.get(0).getKey());
        assertEquals("map", organizeParams.get(1).getKey());
        assertEquals("array", organizeParams.get(2).getKey());
        assertEquals("trueBoolean", organizeParams.get(3).getKey());
        assertEquals("int", organizeParams.get(4).getKey());
        assertEquals(5, organizeParams.size());
    }

    @Test
    public void prepareQueryVolatileTest() throws Exception {
        buffer.getVolatileParams().add(stringParam);
        buffer.getVolatileParams().add(mapParam);
        buffer.getVolatileParams().add(arrayParam);
        buffer.getVolatileParams().add(booleanParam);
        buffer.getVolatileParams().add(intParam);
        builder = new Builder(tracker);
        LinkedHashMap<String, Object[]> result = (LinkedHashMap<String, Object[]>) executePrivateMethod(builder, "prepareQuery", new Object[0]);

        assertEquals(5, result.size());
        assertEquals("&p=home", result.get("p")[1]);
        assertEquals("&map=" + "{\"fruit\":\"orange\"}", result.get("map")[1]);
        assertEquals("&array=" +"jeu,dvd,bluray", result.get("array")[1]);
        assertEquals("&trueBoolean=true", result.get("trueBoolean")[1]);
        assertEquals("&int=20", result.get("int")[1]);
    }

    @Test
    public void prepareQueryPersistentTest() throws Exception {
        buffer.getPersistentParams().add(stringParam);
        buffer.getPersistentParams().add(mapParam);
        buffer.getPersistentParams().add(arrayParam);
        buffer.getPersistentParams().add(booleanParam);
        buffer.getPersistentParams().add(intParam);
        buffer.getPersistentParams().add(new Param("car", closureValue(""), Param.Type.Closure));
        builder = new Builder(tracker);
        LinkedHashMap<String, Object[]> result = (LinkedHashMap<String, Object[]>) executePrivateMethod(builder, "prepareQuery", new Object[0]);

        assertEquals(6, result.size());
        assertEquals("&p=home", result.get("p")[1]);
        assertEquals("&map=" + "{\"fruit\":\"orange\"}", result.get("map")[1]);
        assertEquals("&array=" + "jeu,dvd,bluray", result.get("array")[1]);
        assertEquals("&trueBoolean=true", result.get("trueBoolean")[1]);
        assertEquals("&int=20", result.get("int")[1]);
        assertEquals("&car=", result.get("car")[1]);
    }

    private void prepareBuilderForTest() {
        buffer = tracker.getBuffer();

        // Virer les variables de contexte pour les tests
        buffer.getPersistentParams().clear();

        today = String.valueOf(DateFormat.format(pattern, System.currentTimeMillis()));
        closureParam = new Param("date", getDate(), Param.Type.Closure);
        stringParam = new Param("p", closureValue("home"), Param.Type.String);
        floatParam = new Param("float", closureValue(3.145), Param.Type.Float);
        intParam = new Param("int", closureValue(20), Param.Type.Integer);
        booleanParam = new Param("trueBoolean", closureValue(true), Param.Type.Boolean);
        mapParam = new Param("map", closureValue(Tool.convertToString(new LinkedHashMap<String, String>() {{
            put("fruit", "orange");
        }}, null)), Param.Type.JSON);
        arrayParam = new Param("array", closureValue(Tool.convertToString(new ArrayList<Object>() {{
            add("jeu");
            add("dvd");
            add("bluray");
        }}, null)), Param.Type.Array);
    }

    private Closure getDate() {
        return new Closure() {
            @Override
            public String execute() {
                return String.valueOf(DateFormat.format(pattern, System.currentTimeMillis()));
            }
        };
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
