package com.ishoal.core.persistence.adapter;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;

import java.util.List;

import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.ProductVatRates;
import com.ishoal.core.domain.VatRate;
import com.ishoal.core.persistence.entity.ProductEntity;
import com.ishoal.core.persistence.entity.ProductVatRateEntity;
import com.ishoal.core.persistence.entity.VatRateEntity;

public class ProductVatRateEntityAdapter {

    private final VatRateEntityAdapter vatRateEntityAdapter = new VatRateEntityAdapter();

    public ProductVatRates adapt(List<ProductVatRateEntity> entities) {
        if (entities == null) {
            return ProductVatRates.emptyVatRates();
        }
        return mapToCollection(entities, this::adapt, ProductVatRates::over);
    }

    private ProductVatRate adapt(ProductVatRateEntity entity) {
        return aProductVatRate()
                .vatRate(adapt(entity.getVatRate()))
                .startDateTime(entity.getStartDateTime())
                .endDateTime(entity.getEndDateTime())
                .id(entity.getId())
                .build();
    }

    private VatRate adapt(VatRateEntity vatRate) {
        return vatRateEntityAdapter.adapt(vatRate);
    }

    public List<ProductVatRateEntity> adapt(ProductVatRates vatRates,ProductEntity product) {

        return mapToList(vatRates,v->adapt(v,product));
    }
 
    private ProductVatRateEntity adapt(ProductVatRate productVatRate, ProductEntity productEntity) {

        ProductVatRateEntity entity = new ProductVatRateEntity();
        
        entity.setProduct(productEntity);
        entity.setId(productVatRate.getId());
        entity.setStartDateTime(productVatRate.getStartDateTime());
        entity.setEndDateTime(productVatRate.getEndDateTime());
        entity.setVatRate(vatRateEntityAdapter.adapt(productVatRate.getVatRate()));
        return entity;
    }
}
