package com.ishoal.core.persistence.adapter;

import static com.ishoal.core.domain.ProductSpecs.someProductSpecs;

import java.util.Collections;
import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.ProductSpecs;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductSpecEntity;

public class ProductSpecsEntityAdapter {

    private final ProductSpecEntityAdapter productSpecAdapter = new ProductSpecEntityAdapter();

    public ProductSpecs adapt(List<ProductSpecEntity> entities) {
        if (entities == null) {
            return ProductSpecs.emptyProductSpecs();
        }
        ProductSpecs.Builder builder = someProductSpecs();
        entities.forEach(entity -> builder.productSpec(productSpecAdapter.adapt(entity)));
        return builder.build();
    }

    public List<ProductSpecEntity> adapt(ProductSpecs productSpecs, ProductEntity productEntity) {
        if (productSpecs == null) {
            return Collections.emptyList();
        }
        return IterableUtils.mapToList(productSpecs, spec -> productSpecAdapter.adapt(spec, productEntity));
    }
}
