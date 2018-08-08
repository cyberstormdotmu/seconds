package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.ProductSpec;
import com.ishoal.core.domain.ProductSpecs;
import com.ishoal.ws.buyer.dto.ProductSpecDto;

import java.util.List;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.ws.buyer.dto.ProductSpecDto.aProductSpec;

public class ProductSpecDtoAdapter {

    public List<ProductSpecDto> adapt(ProductSpecs productSpecs) {

        return mapToList(productSpecs, this::adapt);
    }

    private ProductSpecDto adapt(ProductSpec productSpec) {
        return aProductSpec()
                .name(productSpec.getType())
                .description(productSpec.getValue())
                .build();
    }

    public ProductSpecs adapt(List<ProductSpecDto> specifications) {

        return mapToCollection(specifications, this::adapt, ProductSpecs::over);
    }

    private ProductSpec adapt(ProductSpecDto spec) {
        return ProductSpec.aProductSpec()
            .type(spec.getName())
            .value(spec.getDescription())
            .build();
    }
}
