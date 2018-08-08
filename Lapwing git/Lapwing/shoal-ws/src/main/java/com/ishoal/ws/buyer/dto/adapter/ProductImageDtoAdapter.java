package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.ProductImage;
import com.ishoal.core.domain.ProductImages;
import com.ishoal.ws.buyer.dto.ProductImageDto;

import java.util.Collections;
import java.util.List;

import static com.ishoal.common.util.IterableUtils.mapToCollection;

public class ProductImageDtoAdapter {
    public List<ProductImageDto> adapt(ProductImages images) {

        if (images == null) {
            return Collections.emptyList();
        }
        return IterableUtils.mapToList(images, this::adapt);
    }

    public ProductImageDto adapt(ProductImage image) {

        if (image == null) {
            return null;
        }
        return new ProductImageDto(image.getOrder(), image.getUrl(), image.getDescription());
    }

    public ProductImages adapt(List<ProductImageDto> images) {

        return mapToCollection(images, this::adapt, ProductImages::over);
    }

    private ProductImage adapt(ProductImageDto productImage) {

        return ProductImage.aProductImage().order(productImage.getOrder()).url(productImage.getUrl()).description(
            productImage.getDescription()).build();
    }
}
