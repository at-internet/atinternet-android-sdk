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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class OrderTest extends AbstractTestClass {

    private Order order;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        order = new Order(tracker);
    }

    @Test
    public void initTest() {
        assertEquals("", order.getOrderId());
        assertEquals(-1.0, order.getTurnover(), 0);
        assertEquals(-1, order.getStatus());
        assertEquals(-1, order.getPaymentMethod());
        assertFalse(order.isNewCustomer());
        assertNull(order.getCustomVariables());
        assertFalse(order.isConfirmationRequired());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(order, new Order(tracker));
    }

    @Test
    public void getDiscountTest() {
        OrderDiscount orderDiscount = order.Discount();
        assertNotNull(order.Discount());
        assertEquals(orderDiscount, order.Discount());
    }

    @Test
    public void getAmountTest() {
        OrderAmount orderAmount = order.Amount();
        assertNotNull(order.Amount());
        assertEquals(orderAmount, order.Amount());
    }

    @Test
    public void getDeliveryTest() {
        OrderDelivery orderDelivery = order.Delivery();
        assertNotNull(order.Delivery());
        assertEquals(orderDelivery, order.Delivery());
    }

    @Test
    public void setParamsTest() {
        order.Amount().set(34.6, 6.7, 8).CustomVars().add(4, "test");
        order.setParams();

        assertEquals(6, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("cmd").getValues().size());
        assertEquals("", buffer.getVolatileParams().get("cmd").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("newcus").getValues().size());
        assertEquals("0", buffer.getVolatileParams().get("newcus").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mtht").getValues().size());
        assertEquals("34.6", buffer.getVolatileParams().get("mtht").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("mtttc").getValues().size());
        assertEquals("6.7", buffer.getVolatileParams().get("mtttc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("tax").getValues().size());
        assertEquals("8.0", buffer.getVolatileParams().get("tax").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("o4").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("o4").getValues().get(0).execute());
    }
}
