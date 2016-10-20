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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static org.junit.Assert.*;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class ParamTest extends AbstractTestClass {

    private Closure value;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        value = new Closure() {
            @Override
            public String execute() {
                return "value";
            }
        };
    }

    @Test
    public void multiInstanceTest() {
        Param defaultParam = new Param();
        assertNotSame(defaultParam, new Param("crash", value, Param.Type.Array));
    }

    // On vérifie l\'initialisation des variables membres
    @Test
    public void defaultParameterTest() {
        Param defaultParam = new Param();
        assertEquals("", defaultParam.getKey());
        assertNull(defaultParam.getValue());
        assertNull(defaultParam.getOptions());
        assertEquals(Param.Type.Unknown, defaultParam.getType());
    }

    // On vérifie la creation paramétrée
    @Test
    public void parameterWithKeyValueTest() {
        int id = new Random().nextInt(500);
        Param param = new Param("key" + id, value, Param.Type.Array);

        assertEquals("key" + id, param.getKey());
        assertEquals("value", param.getValue().execute());
        assertEquals(Param.Type.Array, param.getType());

    }

    // On vérifie la creation paramétrée
    @Test
    public void parameterWithKeyValueNotEncodeOptionsTest() {
        ParamOption options = new ParamOption().setEncode(false);
        Param param = new Param("key", value, Param.Type.Array, options);

        assertFalse(param.getOptions().isEncode());
    }

    @Test
    public void parameterWithKeyValuePersistentOptionsTest() {
        ParamOption options = new ParamOption()
                .setPersistent(true);
        Param param = new Param("key", value, Param.Type.Array, options);

        assertTrue(param.getOptions().isPersistent());
    }

    @Test
    public void parameterWithKeyValueSeparatorOptionsTest() {
        ParamOption options = new ParamOption()
                .setSeparator("::");
        Param param = new Param("key", value, Param.Type.Array, options);

        assertEquals("::", param.getOptions().getSeparator());
    }

    @Test
    public void parameterWithKeyValueRelativePositionFirstOptionsTest() {
        ParamOption options = new ParamOption();
        options.setRelativePosition(ParamOption.RelativePosition.first);
        Param param = new Param("key", value, Param.Type.Array, options);

        assertEquals(ParamOption.RelativePosition.first, param.getOptions().getRelativePosition());
        assertEquals("", param.getOptions().getRelativeParameterKey());
    }

    @Test
    public void parameterWithKeyValueRelativePositionLastOptionsTest() {
        ParamOption options = new ParamOption()
                .setRelativePosition(ParamOption.RelativePosition.last);
        Param param = new Param("key", value, Param.Type.Array, options);

        assertEquals(ParamOption.RelativePosition.last, param.getOptions().getRelativePosition());
        assertEquals("", param.getOptions().getRelativeParameterKey());
    }

    @Test
    public void parameterWithKeyValueRelativePositionBeforeOptionsTest() {
        ParamOption options = new ParamOption()
                .setRelativePosition(ParamOption.RelativePosition.before)
                .setRelativeParameterKey("stc");
        Param param = new Param("key", value, Param.Type.Array, options);

        assertEquals(ParamOption.RelativePosition.before, param.getOptions().getRelativePosition());
        assertEquals("stc", param.getOptions().getRelativeParameterKey());
    }

    @Test
    public void parameterWithKeyValueRelativePositionAfterOptionsTest() {
        ParamOption options = new ParamOption()
                .setRelativePosition(ParamOption.RelativePosition.after)
                .setRelativeParameterKey("stc");
        Param param = new Param("key", value, Param.Type.Array, options);

        assertEquals(ParamOption.RelativePosition.after, param.getOptions().getRelativePosition());
        assertEquals("stc", param.getOptions().getRelativeParameterKey());
    }

    @Test
    public void setKeyTest() {
        int id = new Random().nextInt(500);

        Param param = new Param();
        param.setKey("key" + id);
        assertEquals("key" + id, param.getKey());
    }

    @Test
    public void setValueTest() {
        Param param = new Param();
        param.setValue(new Closure() {
            @Override
            public String execute() {
                return "Value";
            }
        });
        assertEquals("Value", param.getValue().execute());
    }

    @Test
    public void setTypeTest() {
        Param p = new Param();
        p.setType(Param.Type.Array);
        assertEquals(Param.Type.Array, p.getType());
    }
}
