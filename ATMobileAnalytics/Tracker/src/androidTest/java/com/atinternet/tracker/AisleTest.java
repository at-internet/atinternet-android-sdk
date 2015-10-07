package com.atinternet.tracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AisleTest extends AbstractTestClass {

    private Aisle aisle;
    private Buffer buffer;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aisle = new Aisle(tracker);
        buffer = tracker.getBuffer();
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
    public void setTest() {
        assertEquals("level1", aisle.setLevel1("level1").getLevel1());
        assertEquals("level2", aisle.setLevel2("level2").getLevel2());
        assertEquals("level3", aisle.setLevel3("level3").getLevel3());
        assertEquals("level4", aisle.setLevel4("level4").getLevel4());
        assertEquals("level5", aisle.setLevel5("level5").getLevel5());
        assertEquals("level6", aisle.setLevel6("level6").getLevel6());
    }

    @Test
    public void setEventTest() {
        aisle.setLevel1("level1").setLevel2("level2").setLevel4("level4").setEvent();

        assertEquals(1, buffer.getVolatileParams().size());

        assertEquals("aisl", buffer.getVolatileParams().get(0).getKey());
        assertEquals("level1::level2::level4", buffer.getVolatileParams().get(0).getValue().execute());
    }
}
