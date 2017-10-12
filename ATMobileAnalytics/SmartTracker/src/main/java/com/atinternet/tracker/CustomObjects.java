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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper class to manage CustomObject instances
 */
public class CustomObjects extends Helper {

    private AbstractScreen screen;

    private Gesture gesture;

    private Publisher publisher;

    private SelfPromotion selfPromotion;

    private Product product;

    CustomObjects(Tracker tracker) {
        super(tracker);
    }

    CustomObjects(AbstractScreen screen) {
        super(screen.tracker);
        this.screen = screen;
    }

    CustomObjects(Gesture gesture) {
        super(gesture.tracker);
        this.gesture = gesture;
    }

    CustomObjects(Publisher publisher) {
        super(publisher.tracker);
        this.publisher = publisher;
    }

    CustomObjects(SelfPromotion selfPromotion) {
        super(selfPromotion.tracker);
        this.selfPromotion = selfPromotion;
    }

    CustomObjects(Product product) {
        super(product.tracker);
        this.product = product;
    }

    /**
     * Add a CustomObject
     *
     * @param customObject json string value
     * @return a CustomObject instance
     */
    public CustomObject add(String customObject) {
        CustomObject obj = new CustomObject(tracker).setValue(customObject);

        if (screen != null) {
            screen.getCustomObjectsMap().put(obj.getId(), obj);
        } else if (gesture != null) {
            gesture.getCustomObjectsMap().put(obj.getId(), obj);
        } else if (publisher != null) {
            publisher.getCustomObjectsMap().put(obj.getId(), obj);
        } else if (selfPromotion != null) {
            selfPromotion.getCustomObjectsMap().put(obj.getId(), obj);
        } else if (product != null) {
            product.getCustomObjectsMap().put(obj.getId(), obj);
        } else {
            tracker.getBusinessObjects().put(obj.getId(), obj);
        }

        return obj;
    }

    /**
     * Add a CustomObject
     *
     * @param customObject map with custom data
     * @return a CustomObject instance
     */
    public CustomObject add(Map<String, Object> customObject) {
        return add(new JSONObject(customObject).toString());
    }

    /**
     * Remove a CustomObject
     *
     * @param customObjectId business object identifier
     */
    public void remove(String customObjectId) {
        if (screen != null) {
            screen.getCustomObjectsMap().remove(customObjectId);
        } else if (gesture != null) {
            gesture.getCustomObjectsMap().remove(customObjectId);
        } else if (publisher != null) {
            publisher.getCustomObjectsMap().remove(customObjectId);
        } else if (selfPromotion != null) {
            selfPromotion.getCustomObjectsMap().remove(customObjectId);
        } else if (product != null) {
            product.getCustomObjectsMap().remove(customObjectId);
        } else {
            tracker.getBusinessObjects().remove(customObjectId);
        }
    }

    /**
     * Remove all CustomObjects
     */
    public void removeAll() {
        if (screen != null) {
            screen.getCustomObjectsMap().clear();
        } else if (gesture != null) {
            gesture.getCustomObjectsMap().clear();
        } else if (publisher != null) {
            publisher.getCustomObjectsMap().clear();
        } else if (selfPromotion != null) {
            selfPromotion.getCustomObjectsMap().clear();
        } else if (product != null) {
            product.getCustomObjectsMap().clear();
        } else {
            List<String> ids = new ArrayList<>();
            for (Map.Entry<String, BusinessObject> entry : tracker.getBusinessObjects().entrySet()) {
                if (entry.getValue() instanceof CustomObject) {
                    ids.add(entry.getKey());
                }
            }
            for (String id : ids) {
                tracker.getBusinessObjects().remove(id);
            }
        }
    }
}
