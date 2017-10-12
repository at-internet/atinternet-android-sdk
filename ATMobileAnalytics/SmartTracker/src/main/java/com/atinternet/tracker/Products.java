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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Wrapper class to manage Product instances
 */
public class Products extends Helper {

    private final Cart cart;

    Products(Tracker tracker) {
        super(tracker);
        cart = null;
    }

    Products(Cart cart) {
        this.cart = cart;
    }

    /**
     * Attach the product to tracking
     *
     * @param product Product instance
     * @return Product instance
     */
    public Product add(Product product) {
        if (cart != null) {
            cart.getProductsList().add(product);
        } else {
            tracker.getBusinessObjects().put(product.getId(), product);
        }
        return product;
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @return Product instance
     */
    public Product add(String productId) {
        Product product = new Product(cart != null ? cart.getTracker() : tracker).setProductId(productId);
        if (cart != null) {
            cart.getProductsList().add(product);
        } else {
            tracker.getBusinessObjects().put(product.getId(), product);
        }
        return product;
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @return Product instance
     */
    public Product add(String productId, String category1) {
        return add(productId).setCategory1(category1);
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @param category2 product second category
     * @return Product instance
     */
    public Product add(String productId, String category1, String category2) {
        return add(productId, category1).setCategory2(category2);
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @param category2 product second category
     * @param category3 product third category
     * @return Product instance
     */
    public Product add(String productId, String category1, String category2, String category3) {
        return add(productId, category1, category2).setCategory3(category3);
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @param category2 product second category
     * @param category3 product third category
     * @param category4 product fourth category
     * @return Product instance
     */
    public Product add(String productId, String category1, String category2, String category3, String category4) {
        return add(productId, category1, category2, category3).setCategory4(category4);
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @param category2 product second category
     * @param category3 product third category
     * @param category4 product fourth category
     * @param category5 product fifth category
     * @return Product instance
     */
    public Product add(String productId, String category1, String category2, String category3, String category4, String category5) {
        return add(productId, category1, category2, category3, category4).setCategory5(category5);
    }

    /**
     * Add a product
     *
     * @param productId product identifier
     * @param category1 product first category
     * @param category2 product second category
     * @param category3 product third category
     * @param category4 product fourth category
     * @param category5 product fifth category
     * @param category6 product sixth category
     * @return Product instance
     */
    public Product add(String productId, String category1, String category2, String category3, String category4, String category5, String category6) {
        return add(productId, category1, category2, category3, category4, category5).setCategory6(category6);
    }

    /**
     * Remove a product
     *
     * @param productId product identifier
     */
    public void remove(String productId) {
        int length;
        int index;

        if (cart != null) {
            ArrayList<Product> products = cart.getProductsList();
            length = products.size();
            index = -1;
            for (int i = 0; i < length; i++) {
                if (products.get(i).getProductId().equals(productId)) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                cart.getProductsList().remove(index);
            }
        } else {
            ArrayList<BusinessObject> objects = new ArrayList<BusinessObject>() {{
                addAll(tracker.getBusinessObjects().values());
            }};
            length = objects.size();
            for (int i = 0; i < length; i++) {
                if (objects.get(i) instanceof Product && ((Product) objects.get(i)).getProductId().equals(productId)) {
                    tracker.getBusinessObjects().remove(objects.get(i).getId());
                    break;
                }
            }
        }
    }

    /**
     * Remove all products
     */
    public void removeAll() {
        if (cart != null) {
            cart.getProductsList().clear();
        } else {
            ArrayList<BusinessObject> objects = new ArrayList<BusinessObject>() {{
                addAll(tracker.getBusinessObjects().values());
            }};
            for (BusinessObject obj : objects) {
                if (obj instanceof Product) {
                    tracker.getBusinessObjects().remove(obj.getId());
                }
            }
        }
    }

    /**
     * Send all viewed products
     */
    public void sendViews() {
        ArrayList<BusinessObject> views = new ArrayList<>();
        LinkedHashMap<String, BusinessObject> trackerObjects = tracker.getBusinessObjects();
        Set<String> keys = trackerObjects.keySet();

        for (String key : keys) {
            BusinessObject obj = trackerObjects.get(key);
            if (obj instanceof Product) {
                views.add(obj);
            }
        }

        tracker.getDispatcher().dispatch((BusinessObject[]) views.toArray(new BusinessObject[views.size()]));
    }
}
