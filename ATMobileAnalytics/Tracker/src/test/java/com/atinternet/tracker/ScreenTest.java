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

import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk =21)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest extends AbstractTestClass {

    private Screen screen;
    private Buffer buffer;
    private int i = 0;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        screen = new Screen(tracker);
        buffer = tracker.getBuffer();
        i = 0;
    }

    @Test
    public void initTest() {
        assertEquals("", screen.getName());
        assertEquals(-1, screen.getLevel2());
        assertEquals(Screen.Action.View, screen.getAction());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(screen, new Screen(tracker));
    }

    @Test
    public void CustomVarsTest() {
        CustomVars customVars = screen.CustomVars();
        Assert.assertNotNull(screen.CustomVars());
        Assert.assertEquals(screen.CustomVars(), customVars);
    }

    @Test
    public void OrderTest() {
        double id = new Random().nextInt(500);
        Order order = screen.Order("test" + id, id);

        Assert.assertNotNull(order);
        Assert.assertEquals(screen.Order("", 0), order);

        order = screen.Order("", 0);

        Assert.assertEquals(order.getOrderId(), "test" + id);
        Assert.assertEquals(order.getTurnover(), id);

    }

    @Test
    public void AisleTest() {
        int id = new Random().nextInt(500);
        Aisle aisle = screen.Aisle("test" + id);
        Assert.assertNotNull(aisle);
        Assert.assertEquals(screen.Aisle(""), aisle);

        aisle = screen.Aisle("");

        Assert.assertEquals(aisle.getLevel1(), "test" + id);
    }

    @Test
    public void CampaignTest() {
        int id = new Random().nextInt(500);
        Campaign campaign = screen.Campaign("test" + id);
        Assert.assertNotNull(campaign);
        Assert.assertEquals(screen.Campaign(""), campaign);

        campaign = screen.Campaign("");

        Assert.assertEquals(campaign.getCampaignId(), "test" + id);
    }

    @Test
    public void LocationTest() {
        double lat = new Random().nextInt(500);
        double lon = new Random().nextInt(500);
        Location location = screen.Location(lat, lon);
        Assert.assertNotNull(location);
        Assert.assertEquals(screen.Location(0, 0), location);

        location = screen.Location(0, 0);

        Assert.assertEquals(location.getLatitude(), lat);
        Assert.assertEquals(location.getLongitude(), lon);
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = screen.CustomObjects();
        Assert.assertNotNull(screen.CustomObjects());
        Assert.assertEquals(screen.CustomObjects(), customObjects);
    }

    @Test
    public void InternalSearchTest() {
        int id = new Random().nextInt(500);
        InternalSearch internalSearch = screen.InternalSearch("test" + id, id);
        Assert.assertNotNull(internalSearch);
        Assert.assertEquals(screen.InternalSearch("", 0), internalSearch);

        internalSearch = screen.InternalSearch("", 0);

        Assert.assertEquals(internalSearch.getKeyword(), "test" + id);
        Assert.assertEquals(internalSearch.getResultScreenNumber(), id);
    }

    @Test
    public void CustomTreeStructureTest() {
        int id = new Random().nextInt(500);
        CustomTreeStructure customTreeStructure = screen.CustomTreeStructure(id);
        Assert.assertNotNull(customTreeStructure);
        Assert.assertEquals(screen.CustomTreeStructure(0), customTreeStructure);

        customTreeStructure = screen.CustomTreeStructure(0);

        Assert.assertEquals(customTreeStructure.getCategory1(), id);
    }

    @Test
    public void setTest() {
        boolean rBoolean = new Random().nextBoolean();
        assertEquals("name", screen.setName("name").getName());
        assertEquals(123, screen.setLevel2(123).getLevel2());
        assertEquals(rBoolean, screen.setIsBasketScreen(rBoolean).isBasketScreen());
    }

    @Test
    public void setEventTest() {
        screen.setName("name").setChapter3("chapter3").setEvent();
        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventOnlyChaptersTest() {
        screen.setChapter1("chapter1").setChapter3("chapter3").setEvent();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithCustomVarsTest() {
        int id = new Random().nextInt(500);
        screen.setName("name").setChapter3("chapter3");

        CustomVar cv1 = screen.CustomVars().add(id, "test" + id, CustomVar.CustomVarType.Screen);
        screen.CustomVars().add(id, "test" + id, CustomVar.CustomVarType.App);

        assertEquals(id, cv1.getVarId());

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("f" + id, buffer.getVolatileParams().get(i).getKey());
        assertEquals("test" + id, buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("x" + id, buffer.getVolatileParams().get(i).getKey());
        assertEquals("test" + id, buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithCustomObjectsTest() {
        final int id = new Random().nextInt(500);
        screen.setName("name").setChapter3("chapter3");
        CustomObject co = screen.CustomObjects().add(new HashMap<String, Object>() {{
            put("key" + id, "value" + id);
        }});

        assertNotNull(co.getId());

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{\"key" + id + "\":\"value" + id + "\"}", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithLocation() {
        double longitude = new Random().nextInt(500);
        double latitude = new Random().nextInt(500);
        Location l = screen.setName("name").setChapter3("chapter3").Location(longitude, latitude);

        assertNotNull(l.getId());

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("gy", buffer.getVolatileParams().get(i).getKey());
        assertEquals(String.valueOf(longitude) + "0", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("gx", buffer.getVolatileParams().get(i).getKey());
        assertEquals(String.valueOf(latitude) + "0", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());

    }

    @Test
    public void setEventWithCustomTreeStructure() {
        screen.setName("name").setChapter3("chapter3");
        CustomTreeStructure cts = screen.CustomTreeStructure(4);

        assertNotNull(cts.getId());

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("ptype", buffer.getVolatileParams().get(i).getKey());
        assertEquals("4-0-0", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());

    }

    @Test
    public void setEventWithAisle() {
        screen.setName("name").setChapter3("chapter3");
        Aisle a = screen.Aisle("level1");

        assertNotNull(a.getId());

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("aisl", buffer.getVolatileParams().get(i).getKey());
        assertEquals("level1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithCartTest() {
        String id = String.valueOf(new Random().nextInt(500));
        Cart c = tracker.Cart().set(id);
        Product p = c.Products().add("_product");

        assertNotNull(p.getId());

        screen.setCart(c);

        screen.setEvent();

        assertEquals(7, buffer.getVolatileParams().size());

        assertEquals("tp", buffer.getVolatileParams().get(i).getKey());
        assertEquals("cart", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("idcart", buffer.getVolatileParams().get(i).getKey());
        assertEquals(id, buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("pdt1", buffer.getVolatileParams().get(i).getKey());
        assertEquals("_product", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());

    }

    @Test
    public void setEventWithInternalSearch() {
        screen.setName("name").setChapter3("chapter3");
        InternalSearch is = screen.InternalSearch("searchLabel", 1);

        assertNotNull(is.getId());

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("mc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("searchLabel", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("np", buffer.getVolatileParams().get(i).getKey());
        assertEquals("1", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithCampaign() {
        screen.setName("name").setChapter3("chapter3");
        Campaign c = screen.Campaign("camp");

        assertNotNull(c.getId());

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());

        assertEquals("xto", buffer.getVolatileParams().get(i).getKey());
        assertEquals("camp", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithPublishers() {
        screen.setName("name").setChapter3("chapter3").Publishers().add("campaign").setAction(OnAppAd.Action.Touch);
        Publisher p = screen.Publishers().add("campaign2").setAction(OnAppAd.Action.Touch);

        assertNotNull(p.getId());

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(i).getKey());
        assertEquals("PUB-campaign-------", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(i).getKey());
        assertEquals("PUB-campaign2-------", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithSelfPromotions() {
        screen.setName("name").setChapter3("chapter3").SelfPromotions().add(9).setAction(OnAppAd.Action.Touch);
        SelfPromotion s = screen.SelfPromotions().add(8).setAction(OnAppAd.Action.Touch);

        assertNotNull(s.getId());

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(i).getKey());
        assertEquals("INT-9-||", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("ati", buffer.getVolatileParams().get(i).getKey());
        assertEquals("INT-8-||", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }

    @Test
    public void setEventWithOrder() {
        double turnover = Math.abs(new Random().nextInt(500));
        screen.setName("name").setChapter3("chapter3");
        Order o = screen.Order("oid", turnover);

        assertNotNull(o.getId());

        screen.setEvent();

        assertEquals(7, buffer.getVolatileParams().size());

        assertEquals("cmd", buffer.getVolatileParams().get(i).getKey());
        assertEquals("oid", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("roimt", buffer.getVolatileParams().get(i).getKey());
        assertEquals(String.valueOf(turnover), buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("newcus", buffer.getVolatileParams().get(i).getKey());
        assertEquals("0", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("type", buffer.getVolatileParams().get(i).getKey());
        assertEquals("screen", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("action", buffer.getVolatileParams().get(i).getKey());
        assertEquals("view", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("p", buffer.getVolatileParams().get(i).getKey());
        assertEquals("chapter3::name", buffer.getVolatileParams().get(i++).getValue().execute());

        assertEquals("stc", buffer.getVolatileParams().get(i).getKey());
        assertEquals("{}", buffer.getVolatileParams().get(i++).getValue().execute());
    }
}
