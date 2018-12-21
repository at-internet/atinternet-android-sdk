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
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class CartTest extends AbstractTestClass {

    private Cart cart;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        cart = new Cart(tracker);
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
    public void setParamsTest() {
        cart.setCartId("76")
                .Products().add("pdtid")
                .setCategory1("cat")
                .setCategory2("chap1")
                .setQuantity(2)
                .setUnitPriceTaxFree(6.0)
                .setDiscountTaxIncluded(4)
                .setDiscountTaxFree(4)
                .setPromotionalCode("promo")
                .setUnitPriceTaxIncluded(7.0);

        cart.setParams();
        assertEquals(8, buffer.getVolatileParams().size());

        assertEquals(1, buffer.getVolatileParams().get("idcart").getValues().size());
        assertEquals("76", buffer.getVolatileParams().get("idcart").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pdt1").getValues().size());
        assertEquals("cat::chap1::pdtid", buffer.getVolatileParams().get("pdt1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("qte1").getValues().size());
        assertEquals("2", buffer.getVolatileParams().get("qte1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mtht1").getValues().size());
        assertEquals("6.0", buffer.getVolatileParams().get("mtht1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mt1").getValues().size());
        assertEquals("7.0", buffer.getVolatileParams().get("mt1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("dscht1").getValues().size());
        assertEquals("4.0", buffer.getVolatileParams().get("dscht1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("dsc1").getValues().size());
        assertEquals("4.0", buffer.getVolatileParams().get("dsc1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pcode1").getValues().size());
        assertEquals("promo", buffer.getVolatileParams().get("pcode1").getValues().get(0).execute());
    }
}
