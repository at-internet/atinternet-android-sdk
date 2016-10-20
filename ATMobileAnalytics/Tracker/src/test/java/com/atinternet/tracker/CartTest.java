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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class CartTest extends AbstractTestClass {

    private Cart cart;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        cart = new Cart(tracker);
        buffer = tracker.getBuffer();
    }

    @Test
    public void initTest() {
        assertNull(cart.getCartId());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(cart, new Cart(tracker));
    }

    @Test
    public void setTest() {
        assertEquals("90", cart.setCartId("90").getCartId());
    }

    @Test
    public void setEventTest() {
        Product p = cart.setCartId("76").Products().add("pdtid")
                .setCategory1("cat")
                .setCategory2("chap1")
                .setQuantity(2)
                .setUnitPriceTaxFree(6.0)
                .setDiscountTaxIncluded(4)
                .setDiscountTaxFree(4)
                .setPromotionalCode("promo")
                .setUnitPriceTaxIncluded(7.0);

        assertNotNull(p.getId());


        cart.setEvent();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals("idcart", buffer.getVolatileParams().get(0).getKey());
        assertEquals("76", buffer.getVolatileParams().get(0).getValue().execute());

        assertEquals("pdt1", buffer.getVolatileParams().get(1).getKey());
        assertEquals("cat::chap1::pdtid", buffer.getVolatileParams().get(1).getValue().execute());

        assertEquals("qte1", buffer.getVolatileParams().get(2).getKey());
        assertEquals("2", buffer.getVolatileParams().get(2).getValue().execute());

        assertEquals("mtht1", buffer.getVolatileParams().get(3).getKey());
        assertEquals("6.0", buffer.getVolatileParams().get(3).getValue().execute());

        assertEquals("mt1", buffer.getVolatileParams().get(4).getKey());
        assertEquals("7.0", buffer.getVolatileParams().get(4).getValue().execute());

        assertEquals("dscht1", buffer.getVolatileParams().get(5).getKey());
        assertEquals("4.0", buffer.getVolatileParams().get(5).getValue().execute());

        assertEquals("dsc1", buffer.getVolatileParams().get(6).getKey());
        assertEquals("4.0", buffer.getVolatileParams().get(6).getValue().execute());

        assertEquals("pcode1", buffer.getVolatileParams().get(7).getKey());
        assertEquals("promo", buffer.getVolatileParams().get(7).getValue().execute());
    }
}
