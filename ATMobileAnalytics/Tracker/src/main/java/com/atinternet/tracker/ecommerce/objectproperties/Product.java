package com.atinternet.tracker.ecommerce.objectproperties;

import com.atinternet.tracker.RequiredPropertiesDataObject;

import java.util.Map;

public class Product extends RequiredPropertiesDataObject {

    public Product() {
        super();
        /// STRING
        propertiesPrefixMap.put("id", "s");
        propertiesPrefixMap.put("name", "s");
        propertiesPrefixMap.put("brand", "s");
        propertiesPrefixMap.put("currency", "s");
        propertiesPrefixMap.put("variant", "s");
        propertiesPrefixMap.put("category1", "s");
        propertiesPrefixMap.put("category2", "s");
        propertiesPrefixMap.put("category3", "s");
        propertiesPrefixMap.put("category4", "s");
        propertiesPrefixMap.put("category5", "s");
        propertiesPrefixMap.put("category6", "s");
        propertiesPrefixMap.put("list", "s");

        /// BOOLEAN
        propertiesPrefixMap.put("discount", "b");
        propertiesPrefixMap.put("stock", "b");
        propertiesPrefixMap.put("cart.creation", "b");

        /// FLOAT
        propertiesPrefixMap.put("priceTaxIncluded", "f");
        propertiesPrefixMap.put("priceTaxFree", "f");

        /// LONG
        propertiesPrefixMap.put("position", "n");
        propertiesPrefixMap.put("quantity", "n");
    }

    public Product(Map<String, Object> obj) {
        this();
        for (Entry<String, Object> entry : obj.entrySet()) {
            super.put(entry.getKey(), entry.getValue());
        }
    }
}