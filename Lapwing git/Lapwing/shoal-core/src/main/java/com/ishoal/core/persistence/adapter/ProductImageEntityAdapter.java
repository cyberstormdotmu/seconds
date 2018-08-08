package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.ProductImage;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductImageEntity;

import static com.ishoal.core.domain.ProductImage.aProductImage;

public class ProductImageEntityAdapter {

    public ProductImage adapt(ProductImageEntity entity) {
        return aProductImage()
                .order(entity.getOrder())
                .url(entity.getUrl())
                .description(entity.getDescription())
                .id(entity.getId())
                .build();
    }

    public ProductImageEntity adapt(ProductImage image, ProductEntity product) {
        ProductImageEntity imageEntity = new ProductImageEntity();
        imageEntity.setOrder(image.getOrder());
        imageEntity.setUrl(image.getUrl());
        imageEntity.setDescription(image.getDescription());
        imageEntity.setProduct(product);
        imageEntity.setId(image.getId());
        return imageEntity;
    }
}
