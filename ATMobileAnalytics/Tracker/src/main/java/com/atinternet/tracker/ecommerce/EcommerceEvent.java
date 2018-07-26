package com.atinternet.tracker.ecommerce;

import com.atinternet.tracker.Event;
import com.atinternet.tracker.Screen;

import java.util.HashMap;
import java.util.Map;

public abstract class EcommerceEvent extends Event {

    protected Screen screen;

    protected EcommerceEvent(String action, Screen screen) {
        super(action);
        this.screen = screen;
    }

    @Override
    protected Map<String, Object> getData() {
        Map<String, Object> screenObj = parseScreenNameForEvent(screen);
        if (screenObj.size() != 0) {
            data.put("page", screenObj);
        }
        Map<String, Object> level2Obj = parseLevel2ForEvent(screen);
        if (level2Obj.size() != 0) {
            data.put("level2", level2Obj);
        }
        return super.getData();
    }

    protected Map<String, Object> parseScreenNameForEvent(Screen screen) {
        Map<String, Object> result = new HashMap<>();
        if (screen == null) {
            return result;
        }

        if (screen.getName() != null) {
            result.put("s:name", screen.getName());
        }
        if (screen.getChapter1() != null) {
            result.put("s:chapter1", screen.getChapter1());
        }
        if (screen.getChapter2() != null) {
            result.put("s:chapter2", screen.getChapter2());
        }
        if (screen.getChapter3() != null) {
            result.put("s:chapter3", screen.getChapter3());
        }

        return result;
    }

    protected Map<String, Object> parseLevel2ForEvent(Screen screen) {
        Map<String, Object> level2 = new HashMap<>();
        if (screen.getLevel2() > 0) {
            level2.put("s:name", String.valueOf(screen.getLevel2()));
        }
        return level2;
    }
}
