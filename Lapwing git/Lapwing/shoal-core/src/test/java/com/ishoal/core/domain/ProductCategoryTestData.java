package com.ishoal.core.domain;

public class ProductCategoryTestData {

    public static ProductCategory aProductCategory() {
        return ProductCategory.aProductCategory().name(ProductTestData.MAIN_CATEGORY).build();
    }

    public static ProductCategory aProductCategoryWithParent() {
        return ProductCategory.aProductCategory().name(ProductTestData.SUB_CATEGORY).parent(aProductCategory()).build();
    }

}
