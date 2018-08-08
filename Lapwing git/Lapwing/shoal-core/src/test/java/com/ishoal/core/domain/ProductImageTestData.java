package com.ishoal.core.domain;

import static com.ishoal.core.domain.ProductImage.aProductImage;

public class ProductImageTestData {

    public static ProductImage mainImage() {
        return aProductImage().order(0).url("http://ishoal.com/main.image").description("Main Image").build();
    }

    public static ProductImage secondaryImage() {
        return aProductImage().order(1).url("http://ishoal.com/secondary.image").description("Secondary Image").build();
    }
}
