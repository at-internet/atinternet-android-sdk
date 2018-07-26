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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ProductTest extends AbstractTestClass {

    private Product product;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        product = new Product(tracker);
    }

    @Test
    public void initTest() {
        assertEquals("", product.getProductId());
        assertEquals(Product.Action.View, product.getAction());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(product, new Product(tracker));
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = product.CustomObjects();
        Assert.assertNotNull(product.CustomObjects());
        Assert.assertEquals(product.CustomObjects(), customObjects);
    }

    @Test
    public void setParamsTest() {
        product.setProductId("pdtID").setCategory4("cat4").setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("pdt", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pdtl").getValues().size());
        assertEquals("cat4::pdtID", buffer.getVolatileParams().get("pdtl").getValues().get(0).execute());
    }

    @Test
    public void setParamsWithCOTest() {
        product.setProductId("pdtID").CustomObjects().add("{}");
        product.setParams();

        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("pdt", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pdtl").getValues().size());
        assertEquals("pdtID", buffer.getVolatileParams().get("pdtl").getValues().get(0).execute());

    }

    @Test
    public void multiplesValuesTest() {
        new Product(tracker).setProductId("pdtID").setParams();
        new Product(tracker).setProductId("pdtID1").setParams();
        new Product(tracker).setProductId("pdtID3").setParams();

        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("pdt", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(3, buffer.getVolatileParams().get("pdtl").getValues().size());
        assertEquals("pdtID", buffer.getVolatileParams().get("pdtl").getValues().get(0).execute());
        assertEquals("pdtID1", buffer.getVolatileParams().get("pdtl").getValues().get(1).execute());
        assertEquals("pdtID3", buffer.getVolatileParams().get("pdtl").getValues().get(2).execute());

    }
}
