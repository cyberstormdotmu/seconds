package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.PriceBandEntity;

import java.util.List;
import java.util.stream.Collectors;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.core.domain.PriceBands.emptyPriceBands;

public class PriceBandsEntityAdapter {

    private PriceBandEntityAdapter priceBandEntityAdapter = new PriceBandEntityAdapter();

    public PriceBands adapt(List<PriceBandEntity> entities) {
        if (entities == null) {
            return emptyPriceBands();
        }

        return mapToCollection(entities, priceBandEntityAdapter::adapt, PriceBands::over);
    }

    public List<PriceBandEntity> adapt(PriceBands priceBands, OfferEntity offer) {

        List<PriceBandEntity> entities = priceBands.stream().map(priceBandEntityAdapter::adapt).collect(
            Collectors.toList());
        entities.forEach(e -> e.setOffer(offer));
        return entities;
    }
}
