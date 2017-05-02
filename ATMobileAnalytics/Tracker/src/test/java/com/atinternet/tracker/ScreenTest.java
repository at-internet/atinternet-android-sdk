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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

@Config(sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class ScreenTest extends AbstractTestClass {

    private Screen screen;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        screen = new Screen(tracker);
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
        Order order = screen.Order("test", 1.);
        Assert.assertNotNull(order);
        Assert.assertEquals(screen.Order("", 0), order);
    }

    @Test
    public void AisleTest() {
        Aisle aisle = screen.Aisle("test");
        Assert.assertNotNull(aisle);
        Assert.assertEquals(screen.Aisle(""), aisle);
    }

    @Test
    public void CampaignTest() {
        Campaign campaign = screen.Campaign("test");
        Assert.assertNotNull(campaign);
        Assert.assertEquals(screen.Campaign(""), campaign);
    }

    @Test
    public void LocationTest() {
        Location location = screen.Location(1, 1);
        Assert.assertNotNull(location);
        Assert.assertEquals(screen.Location(0, 0), location);
    }

    @Test
    public void CustomObjectsTest() {
        CustomObjects customObjects = screen.CustomObjects();
        Assert.assertNotNull(screen.CustomObjects());
        Assert.assertEquals(screen.CustomObjects(), customObjects);
    }

    @Test
    public void InternalSearchTest() {
        InternalSearch internalSearch = screen.InternalSearch("test", 1);
        Assert.assertNotNull(internalSearch);
        Assert.assertEquals(screen.InternalSearch("", 0), internalSearch);
    }

    @Test
    public void CustomTreeStructureTest() {
        CustomTreeStructure customTreeStructure = screen.CustomTreeStructure(1);
        Assert.assertNotNull(customTreeStructure);
        Assert.assertEquals(screen.CustomTreeStructure(0), customTreeStructure);
    }

    @Test
    public void setEventTest() {
        screen.setName("name").setChapter3("chapter3").setEvent();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventOnlyChaptersTest() {
        screen.setChapter1("chapter1").setChapter3("chapter3").setEvent();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter1::chapter3::", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithCustomVarsTest() {
        screen.setName("name").setChapter3("chapter3");

        screen.CustomVars().add(1, "test", CustomVar.CustomVarType.Screen);
        screen.CustomVars().add(1, "test", CustomVar.CustomVarType.App);
        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("f1").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("f1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("x1").getValues().size());
        assertEquals("test", buffer.getVolatileParams().get("x1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithCustomObjectsTest() {
        screen.setName("name").setChapter3("chapter3");
        CustomObject co = screen.CustomObjects().add(new HashMap<String, Object>() {{
            put("key", "value");
        }});

        assertNotNull(co.getId());

        screen.setEvent();

        assertEquals(4, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(2, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{\"key\":\"value\"}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(1).execute());
    }

    @Test
    public void setEventWithLocation() {
        screen.setName("name").setChapter3("chapter3").Location(1, 1);

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("gy").getValues().size());
        assertEquals("1.00", buffer.getVolatileParams().get("gy").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("gx").getValues().size());
        assertEquals("1.00", buffer.getVolatileParams().get("gx").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

    }

    @Test
    public void setEventWithCustomTreeStructure() {
        screen.setName("name").setChapter3("chapter3");
        screen.CustomTreeStructure(4);

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("ptype").getValues().size());
        assertEquals("4-0-0", buffer.getVolatileParams().get("ptype").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

    }

    @Test
    public void setEventWithAisle() {
        screen.setName("name").setChapter3("chapter3");
        screen.Aisle("level1");

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("aisl").getValues().size());
        assertEquals("level1", buffer.getVolatileParams().get("aisl").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithCartTest() {
        Cart c = tracker.Cart().set("1");
        c.Products().add("_product");

        screen.setName("name").setChapter3("chapter3");
        screen.setCart(c);

        screen.setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("tp").getValues().size());
        assertEquals("cart", buffer.getVolatileParams().get("tp").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("idcart").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("idcart").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("pdt1").getValues().size());
        assertEquals("_product", buffer.getVolatileParams().get("pdt1").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());

    }

    @Test
    public void setEventWithInternalSearch() {
        screen.setName("name").setChapter3("chapter3");
        screen.InternalSearch("searchLabel", 1);

        screen.setEvent();

        assertEquals(6, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("mc").getValues().size());
        assertEquals("searchLabel", buffer.getVolatileParams().get("mc").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("np").getValues().size());
        assertEquals("1", buffer.getVolatileParams().get("np").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithCampaign() {
        screen.setName("name").setChapter3("chapter3");
        screen.Campaign("camp");

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("xto").getValues().size());
        assertEquals("camp", buffer.getVolatileParams().get("xto").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithPublishers() {
        screen.setName("name").setChapter3("chapter3")
                .Publishers().add("campaign").setAction(OnAppAd.Action.Touch);
        screen.Publishers().add("campaign2").setAction(OnAppAd.Action.Touch);
        screen.Publishers().add("campaign3").setAction(OnAppAd.Action.Touch);

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());


        assertEquals(3, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("PUB-campaign-------", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
        assertEquals("PUB-campaign2-------", buffer.getVolatileParams().get("ati").getValues().get(1).execute());
        assertEquals("PUB-campaign3-------", buffer.getVolatileParams().get("ati").getValues().get(2).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithSelfPromotions() {
        screen.setName("name").setChapter3("chapter3")
                .SelfPromotions().add(9).setAction(OnAppAd.Action.Touch);
        screen.SelfPromotions().add(8).setAction(OnAppAd.Action.Touch);

        screen.setEvent();

        assertEquals(5, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());


        assertEquals(2, buffer.getVolatileParams().get("ati").getValues().size());
        assertEquals("INT-9-||", buffer.getVolatileParams().get("ati").getValues().get(0).execute());
        assertEquals("INT-8-||", buffer.getVolatileParams().get("ati").getValues().get(1).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }

    @Test
    public void setEventWithOrder() {
        screen.setName("name").setChapter3("chapter3");
        screen.Order("oid", 1.);

        screen.setEvent();

        assertEquals(7, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("cmd").getValues().size());
        assertEquals("oid", buffer.getVolatileParams().get("cmd").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("roimt").getValues().size());
        assertEquals("1.0", buffer.getVolatileParams().get("roimt").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("newcus").getValues().size());
        assertEquals("0", buffer.getVolatileParams().get("newcus").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("type").getValues().size());
        assertEquals("screen", buffer.getVolatileParams().get("type").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("action").getValues().size());
        assertEquals("view", buffer.getVolatileParams().get("action").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("p").getValues().size());
        assertEquals("chapter3::name", buffer.getVolatileParams().get("p").getValues().get(0).execute());

        assertEquals(1, buffer.getVolatileParams().get("stc").getValues().size());
        assertEquals("{}", buffer.getVolatileParams().get("stc").getValues().get(0).execute());
    }
}
