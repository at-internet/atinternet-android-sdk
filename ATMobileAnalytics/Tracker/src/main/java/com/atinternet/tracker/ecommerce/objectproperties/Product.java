package com.atinternet.tracker.ecommerce.objectproperties;

import com.atinternet.tracker.RequiredPropertiesDataObject;

import java.util.Map;

public class Product extends RequiredPropertiesDataObject {

    public Product() {
        super();
        /// STRING
        propertiesPrefix.put("id", "s");
        propertiesPrefix.put("name", "s");
        propertiesPrefix.put("brand", "s");
        propertiesPrefix.put("currency", "s");
        propertiesPrefix.put("variant", "s");
        propertiesPrefix.put("category1", "s");
        propertiesPrefix.put("category2", "s");
        propertiesPrefix.put("category3", "s");
        propertiesPrefix.put("category4", "s");
        propertiesPrefix.put("category5", "s");
        propertiesPrefix.put("category6", "s");
        propertiesPrefix.put("list", "s");

        /// BOOLEAN
        propertiesPrefix.put("discount", "b");
        propertiesPrefix.put("stock", "b");
        propertiesPrefix.put("cartcreation", "b");

        /// FLOAT
        propertiesPrefix.put("priceTaxIncluded", "f");
        propertiesPrefix.put("priceTaxFree", "f");

        /// LONG
        propertiesPrefix.put("position", "n");
        propertiesPrefix.put("quantity", "n");
    }

    public Product(Map<String, Object> obj) {
        this();
        setAll(obj);
    }
}