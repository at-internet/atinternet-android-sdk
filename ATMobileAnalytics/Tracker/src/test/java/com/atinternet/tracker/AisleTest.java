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
public class AisleTest extends AbstractTestClass {

    private Aisle aisle;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aisle = new Aisle(tracker);
    }

    @Test
    public void initTest() {
        assertNull(aisle.getLevel1());
        assertNull(aisle.getLevel2());
        assertNull(aisle.getLevel3());
        assertNull(aisle.getLevel4());
        assertNull(aisle.getLevel5());
        assertNull(aisle.getLevel6());
    }

    @Test
    public void multiInstancesTest() {
        assertNotSame(aisle, new Aisle(tracker));
    }

    @Test
    public void setParamsTest() {
        aisle.setLevel1("level1").setLevel2("level2").setLevel4("level4").setParams();

        assertEquals(1, buffer.getVolatileParams().size());
        assertEquals(0, buffer.getPersistentParams().size());

        assertEquals(1, buffer.getVolatileParams().get("aisl").getValues().size());
        assertEquals("level1::level2::level4", buffer.getVolatileParams().get("aisl").getValues().get(0).execute());
    }
}
