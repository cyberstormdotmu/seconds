package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBandId;
import com.ishoal.core.persistence.entity.PriceBandEntity;

import static com.ishoal.core.domain.PriceBand.aPriceBand;

public class PriceBandEntityAdapter {

    public PriceBand adapt(PriceBandEntity entity) {
        if(entity == null) {
            return null;
        }
        return aPriceBand().id(PriceBandId.from(entity.getId()))
                .minVolume(entity.getMinVolume())
                .maxVolume(entity.getMaxVolume())
                .buyerPrice(entity.getBuyerPrice())
                .vendorPrice(entity.getVendorPrice())
                .shoalMargin(entity.getShoalMargin())
                .distributorMargin(entity.getDistributorMargin())
                .build();
    }

    public PriceBandEntity adapt(PriceBand priceBand) {

        if (priceBand == null) {
            return null;
        }
        PriceBandEntity entity = new PriceBandEntity();
        entity.setId(priceBand.getId().asLong());
        entity.setMinVolume(priceBand.getMinVolume());
        entity.setMaxVolume(priceBand.getMaxVolume());
        entity.setBuyerPrice(priceBand.getBuyerPrice());
        entity.setDistributorMargin(priceBand.getDistributorMargin());
        entity.setVendorPrice(priceBand.getVendorPrice());
        entity.setShoalMargin(priceBand.getShoalMargin());
        return entity;
    }
}
