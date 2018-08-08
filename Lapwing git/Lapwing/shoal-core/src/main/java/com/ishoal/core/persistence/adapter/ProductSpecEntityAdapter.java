package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.ProductSpec.aProductSpec;

import com.ishoal.core.domain.ProductSpec;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductSpecEntity;

public class ProductSpecEntityAdapter {

    public ProductSpec adapt(ProductSpecEntity entity) {
        if(entity == null) {
            return null;
        }
        return aProductSpec().type(entity.getType())
                .value(entity.getValue())
                .id(entity.getId())
                .build();
    }

    public ProductSpecEntity adapt(ProductSpec productSpec, ProductEntity product) {
        if (productSpec == null) {
            return null;
        }
        ProductSpecEntity entity = new ProductSpecEntity();
        entity.setId(productSpec.getId());
        entity.setProduct(product);
        entity.setType(productSpec.getType());
        entity.setValue(productSpec.getValue());
        return entity;
    }
}
