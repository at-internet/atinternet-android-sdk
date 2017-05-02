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
import static org.junit.Assert.assertNull;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ProductsTest extends AbstractTestClass {

    private Products productsTracker;
    private Products productsCart;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        productsTracker = new Products(tracker);
        productsCart = new Products(tracker.Cart());
    }

    @Test
    public void initTest() throws NoSuchFieldException, IllegalAccessException {
        assertNull(getAccessibleField(productsTracker, "cart"));
        assertNotNull(getAccessibleField(productsCart, "cart"));
    }

    @Test
    public void addOneTest() {
        Product p = new Product(tracker).setProductId("pdtID");
        p = productsTracker.add(p);
        productsCart.add(p);

        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(1, tracker.Cart().getProductsList().size());
    }

    @Test
    public void addTwoTest() {
        productsTracker.add("p1");
        productsCart.add("p1");

        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(1, tracker.Cart().getProductsList().size());
    }

    @Test
    public void removeTest() {
        Product p = new Product(tracker).setProductId("pdtID");
        Product p2 = new Product(tracker).setProductId("pdtID2");
        productsTracker.add(p);
        productsTracker.add(p2);
        productsCart.add(p);
        productsCart.add(p2);

        assertEquals(2, tracker.getBusinessObjects().size());
        assertEquals(2, tracker.Cart().getProductsList().size());

        productsTracker.remove("pdtID");
        productsTracker.remove("test");
        productsCart.remove("pdtID2");

        assertEquals(1, tracker.getBusinessObjects().size());
        assertEquals(1, tracker.Cart().getProductsList().size());
    }

    @Test
    public void removeAllTest() {
        Product p = new Product(tracker).setProductId("toto");
        Product p2 = new Product(tracker).setProductId("tata");
        productsTracker.add(p);
        productsTracker.add(p2);
        productsCart.add(p);
        productsCart.add(p2);

        assertEquals(2, tracker.getBusinessObjects().size());
        assertEquals(2, tracker.Cart().getProductsList().size());

        productsTracker.removeAll();
        productsCart.removeAll();

        assertEquals(0, tracker.getBusinessObjects().size());
        assertEquals(0, tracker.Cart().getProductsList().size());
    }
}
