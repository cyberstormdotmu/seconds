package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.VatRate;
import com.ishoal.core.persistence.entity.VatRateEntity;

import static com.ishoal.core.domain.VatRate.aVatRate;

public class VatRateEntityAdapter {

    public VatRate adapt(VatRateEntity entity) {
        if (entity == null) {
            return null;
        }
        return aVatRate()
                .code(entity.getVatCode())
                .rate(entity.getVatRate())
                .id(entity.getId())
                .build();
    }

    public VatRateEntity adapt(VatRate vatRate) {
        VatRateEntity entity = new VatRateEntity();
        entity.setId(vatRate.getId());
        entity.setVatCode(vatRate.getCode());
        entity.setVatRate(vatRate.getRate());
        return entity;
    }
}
