package com.atinternet.tracker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(sdk =21)
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
        Random r = new Random();
        int[] vals = {r.nextInt(500), r.nextInt(500), r.nextInt(500), r.nextInt(500), r.nextInt(500)};
        int i = 0;

        Audio a = audios.add("name" + vals[i++], "chapter" + vals[i++], "chapter" + vals[i++], "chapter" + vals[i++], vals[i]);

        assertNotNull(a.getId());

        i = 0;
        assertEquals(1, audios.list.size());
        assertEquals("name" + vals[i++], audios.list.get(0).getName());
        assertEquals("chapter" + vals[i++], audios.list.get(0).getChapter1());
        assertEquals("chapter" + vals[i++], audios.list.get(0).getChapter2());
        assertEquals("chapter" + vals[i++], audios.list.get(0).getChapter3());
        assertEquals(vals[i], audios.list.get(0).getDuration());
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
