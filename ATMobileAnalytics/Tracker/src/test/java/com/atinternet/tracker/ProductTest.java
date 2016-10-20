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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class ProductTest extends AbstractTestClass {

    private Product product;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        product = new Product(tracker);
        buffer = tracker.getBuffer();
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
    public void setTest() {
        Random r = new Random();
        String[] vals = {
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500)),
                String.valueOf(r.nextInt(500))
        };
        int i = 0;

        assertEquals("pid" + vals[i], product.setProductId("pid" + vals[i++]).getProductId());
        assertEquals(vals[i], product.setCategory1(vals[i++]).getCategory1());
        assertEquals(vals[i], product.setCategory2(vals[i++]).getCategory2());
        assertEquals(vals[i], product.setCategory3(vals[i++]).getCategory3());
        assertEquals(vals[i], product.setCategory4(vals[i++]).getCategory4());
        assertEquals(vals[i], product.setCategory5(vals[i++]).getCategory5());
        assertEquals(vals[i], product.setCategory6(vals[i]).getCategory6());
        assertEquals(Product.Action.View, product.setAction(Product.Action.View).getAction());
        assertNull(product.setAction(null).getAction());

        int[] others = {
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500)
        };
        i = 0;
        assertEquals(others[i], product.setQuantity(others[i++]).getQuantity());
        assertEquals(others[i], product.setUnitPriceTaxIncluded(others[i++]).getUnitPriceTaxIncluded(), 0);
        assertEquals(others[i], product.setUnitPriceTaxFree(others[i++]).getUnitPriceTaxFree(), 0);
        assertEquals(others[i], product.setDiscountTaxIncluded(others[i++]).getDiscountTaxIncluded(), 0);
        assertEquals(others[i], product.setDiscountTaxFree(others[i++]).getDiscountTaxFree(), 0);
        assertEquals(String.valueOf(others[i]), product.setPromotionalCode(String.valueOf(others[i])).getPromotionalCode());

    }

    @Test
    public void setEventTest() {
        product.setProductId("pdtID").setCategory4("cat4").setEvent();
        assertEquals(2, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("pdt", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("pdtl", buffer.getVolatileParams().get(1).getKey());
        assertEquals("cat4::pdtID", buffer.getVolatileParams().get(1).getValue().execute());
    }

    @Test
    public void setEventWithCOTest() {
        product.setProductId("pdtID").CustomObjects().add("{}");
        product.setEvent();
        assertEquals(3, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(0).getKey());
        assertEquals("pdt", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(1).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("pdtl", buffer.getVolatileParams().get(2).getKey());
        assertEquals("pdtID", buffer.getVolatileParams().get(2).getValue().execute());


    }
}
