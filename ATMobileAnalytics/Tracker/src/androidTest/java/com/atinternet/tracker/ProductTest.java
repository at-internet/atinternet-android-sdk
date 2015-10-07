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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@Config(emulateSdk = 18)
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
    public void setTest() {
        assertEquals("pid", product.setProductId("pid").getProductId());
        assertEquals("1", product.setCategory1("1").getCategory1());
        assertEquals("2", product.setCategory2("2").getCategory2());
        assertEquals("3", product.setCategory3("3").getCategory3());
        assertEquals("4", product.setCategory4("4").getCategory4());
        assertEquals("5", product.setCategory5("5").getCategory5());
        assertEquals("6", product.setCategory6("6").getCategory6());
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
}
