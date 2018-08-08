package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.ProductImages.someProductImages;

import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.ProductImages;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductImageEntity;

public class ProductImagesEntityAdapter {

    private ProductImageEntityAdapter imageAdapter = new ProductImageEntityAdapter();

    public ProductImages adapt(List<ProductImageEntity> entities) {
        if (entities == null) {
            return ProductImages.emptyProductImages();
        }
        ProductImages.Builder builder = someProductImages();
        entities.forEach(entity -> builder.productImage(imageAdapter.adapt(entity)));
        return builder.build();
    }

    public List<ProductImageEntity> adapt(ProductImages images, ProductEntity entity) {
        return IterableUtils.mapToList(images, image -> imageAdapter.adapt(image, entity));
    }
}
