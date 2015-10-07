package com.atinternet.tracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class AudiosTest extends AbstractTestClass {

    private Audios audios;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MediaPlayer player = new MediaPlayer(tracker);
        audios = new Audios(player);
    }

    @Test
    public void initTest() {
        assertTrue(true);
    }

    @Test
    public void addOneTest() {
        audios.add("name", 80);
        assertEquals(1, audios.list.size());
        assertEquals("name", audios.list.get(0).getName());
        assertEquals(80, audios.list.get(0).getDuration());
    }

    @Test
    public void addTwoTest() {
        audios.add("name", "chapter1", 80);
        assertEquals(1, audios.list.size());
        assertEquals("name", audios.list.get(0).getName());
        assertEquals("chapter1", audios.list.get(0).getChapter1());
        assertEquals(80, audios.list.get(0).getDuration());
    }

    @Test
    public void addThreeTest() {
        audios.add("name", "chapter1", "chapter2", 80);
        assertEquals(1, audios.list.size());
        assertEquals("name", audios.list.get(0).getName());
        assertEquals("chapter1", audios.list.get(0).getChapter1());
        assertEquals("chapter2", audios.list.get(0).getChapter2());
        assertEquals(80, audios.list.get(0).getDuration());
    }

    @Test
    public void addFourTest() {
        audios.add("name", "chapter1", "chapter2", "chapter3", 80);
        assertEquals(1, audios.list.size());
        assertEquals("name", audios.list.get(0).getName());
        assertEquals("chapter1", audios.list.get(0).getChapter1());
        assertEquals("chapter2", audios.list.get(0).getChapter2());
        assertEquals("chapter3", audios.list.get(0).getChapter3());
        assertEquals(80, audios.list.get(0).getDuration());
    }

    @Test
    public void removeTest() {
        Audio a1 = audios.add("name", 80);
        Audio a2 = audios.add("test", 10);
        audios.add("test2", 40);
        assertEquals(3, audios.list.size());

        audios.remove("name");
        assertEquals(2, audios.list.size());
        assertFalse(audios.list.contains(a1));
        assertTrue(audios.list.contains(a2));
    }

    @Test
    public void removeAllTest() {
        audios.add("name", 80);
        audios.add("test", 10);
        audios.add("test2", 40);
        assertEquals(3, audios.list.size());

        audios.removeAll();
        assertEquals(0, audios.list.size());

    }
}
