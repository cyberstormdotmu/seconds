package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.ProductVatRate;
import com.ishoal.core.domain.VatRate;
import com.ishoal.ws.buyer.dto.ProductVatRateDto;

import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static com.ishoal.ws.buyer.dto.ProductVatRateDto.aVatRateDto;

public class ProductVatRateDtoAdapter {

    public ProductVatRateDto adapt(VatRate vatRate) {
        if (vatRate == null) {
            return null;
        }
        return aVatRateDto().code(vatRate.getCode()).rate(vatRate.getRate()).build();
    }

    public ProductVatRate adapt(ProductVatRateDto productVatRateDto) {
        if (productVatRateDto == null) {
            return null;
        }
        return aProductVatRate()
            .vatRate(aVatRate()
                .code(productVatRateDto.getCode())
                .rate(productVatRateDto.getRate())
                .build())
            .build();
    }
}
