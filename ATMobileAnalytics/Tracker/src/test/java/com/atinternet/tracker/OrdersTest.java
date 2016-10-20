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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class OrdersTest extends AbstractTestClass {

    private Orders orders;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        orders = new Orders(tracker);
    }

    @Test
    public void addOneTest() {
        Order order = orders.add("orderID", 78.0);
        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals("orderID", ((Order) tracker.getBusinessObjects().get(order.getId())).getOrderId());
        assertEquals(78.0, ((Order) tracker.getBusinessObjects().get(order.getId())).getTurnover(), 0);
    }

    @Test
    public void addTwoTest() {
        Random r = new Random();
        int[] vals = {
                r.nextInt(500),
                r.nextInt(500),
                r.nextInt(500)
        };
        int i = 0;

        Order order = orders.add("order" + vals[i++], (double) vals[i++], vals[i]);
        assertEquals(1, tracker.getBusinessObjects().size());
        i = 0;

        assertEquals("order" + vals[i++], ((Order) tracker.getBusinessObjects().get(order.getId())).getOrderId());
        assertEquals((double) vals[i++], ((Order) tracker.getBusinessObjects().get(order.getId())).getTurnover(), 0);
        assertEquals(vals[i], ((Order) tracker.getBusinessObjects().get(order.getId())).getStatus());
        assertSame(order, tracker.getBusinessObjects().get(order.getId()));
    }
}
