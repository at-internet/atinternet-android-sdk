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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;

import static com.atinternet.tracker.Tracker.OfflineMode.always;
import static com.atinternet.tracker.Tracker.OfflineMode.never;
import static com.atinternet.tracker.Tracker.OfflineMode.required;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ToolTest extends AbstractTestClass {

    @Test
    public void convertStringToOfflineModeTest() {
        assertEquals(always, Tool.convertStringToOfflineMode("always"));
        assertEquals(required, Tool.convertStringToOfflineMode("required"));
        assertEquals(never, Tool.convertStringToOfflineMode("never"));
    }

    @Test
    public void removeCharactersTest() {
        String test = "i want to remove spa ces";
        assertEquals("iwanttoremovespaces", Tool.removeCharacters(test, " ", ","));
    }

    @Test
    public void formatNumberLengthTest() {
        assertEquals("007", Tool.formatNumberLength("7", 3));
    }

    @Test
    public void parseJSONTest() {
        assertFalse(Tool.isJSONObject("test"));
        assertTrue(Tool.isJSONObject("{\"test\": [1,2,3]}"));
        assertTrue(Tool.isJSONArray("[1,2,3]"));
    }

    @Test
    public void toMapTest() throws JSONException {
        JSONObject json = new JSONObject("{\"test\":\"123\"}");
        assertNotNull(json);
        Map map = Tool.toMap(json);
        assertNotNull(map);
        assertEquals("123", map.get("test"));
    }

    @Test
    public void percentEncodeTest() {
        String rawHit = "mfmd=[google]-[nexus5]&os=[android]-[5.0.1]&apvr=[1.0]";
        String encodingHit = "mfmd%3D%5Bgoogle%5D%2D%5Bnexus5%5D%26os%3D%5Bandroid%5D%2D%5B5%2E0%2E1%5D%26apvr%3D%5B1%2E0%5D";
        assertEquals(encodingHit, Tool.percentEncode(rawHit));
    }

    @Test
    public void percentDecodeTest() {
        String encodingHit = "s%3D552987%26vtag%3D2.0.0%26ptag%3DAndroid%26lng%3Dfr_fr%26mfmd%3D%5Bgoogle%5D-%5Bnexus5%5D%26os%3D%5Bandroid%5D-%5B5.0.1%5D%26apvr%3D%5B1.0%5D%26hl%3D13x36x23%26r%3D1080x1776%26cn%3Doffline%26na%3D1422880583.9140000343322754%26olt%3D1422880583.9089999198913574%26idclient%3Df54d728a-2db6-4b59-9511-48d01c0175ac%26p%3DScreenName%26stc%3D%7B%22Lifecycle%22%3A%7B%22fl%22%3A0%2C%22fld%22%3A20150202%2C%22dsfl%22%3A0%2C%22flau%22%3A0%2C%22dsu%22%3A0%2C%22dslu%22%3A0%2C%22lc%22%3A5%2C%22lcsu%22%3A0%2C%22ldc%22%3A5%2C%22lwc%22%3A5%2C%22lmc%22%3A5%7D%7D%26ref%3Dwww.atinternet.com";
        String decodingHit = "s=552987&vtag=2.0.0&ptag=Android&lng=fr_fr&mfmd=[google]-[nexus5]&os=[android]-[5.0.1]&apvr=[1.0]&hl=13x36x23&r=1080x1776&cn=offline&na=1422880583.9140000343322754&olt=1422880583.9089999198913574&idclient=f54d728a-2db6-4b59-9511-48d01c0175ac&p=ScreenName&stc={\"Lifecycle\":{\"fl\":0,\"fld\":20150202,\"dsfl\":0,\"flau\":0,\"dsu\":0,\"dslu\":0,\"lc\":5,\"lcsu\":0,\"ldc\":5,\"lwc\":5,\"lmc\":5}}&ref=www.atinternet.com";
        assertEquals(decodingHit, Tool.percentDecode(encodingHit));
    }

    @Test
    public void appendParameterValuesTest() {
        final Param p = new Param("test", new Closure() {
            @Override
            public String execute() {
                return "test";
            }
        });
        p.getValues().add(new Closure() {
            @Override
            public String execute() {
                return "value";
            }
        });

        assertEquals("test,value", Tool.appendParameterValues(p));
    }
}
