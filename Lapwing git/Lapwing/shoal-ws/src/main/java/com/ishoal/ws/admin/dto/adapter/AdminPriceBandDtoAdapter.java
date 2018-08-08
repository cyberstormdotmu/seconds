package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.PriceBand;
import com.ishoal.ws.admin.dto.AdminPriceBandDto;
import com.ishoal.ws.buyer.dto.MoneyDto;

import static com.ishoal.ws.admin.dto.AdminPriceBandDto.adminPriceBand;

public class AdminPriceBandDtoAdapter {

    public AdminPriceBandDto adapt(PriceBand priceBand) {
        return adminPriceBand()
                .minVolume(priceBand.getMinVolume())
                .maxVolume(priceBand.getMaxVolume())
                .buyerPrice(new MoneyDto(priceBand.getBuyerPrice()))
                .vendorPrice(new MoneyDto(priceBand.getVendorPrice()))
                .shoalMargin(new MoneyDto(priceBand.getShoalMargin()))
                .distributorMargin(new MoneyDto(priceBand.getDistributorMargin()))
                .build();
    }

    public PriceBand adapt(AdminPriceBandDto priceBandDto) {
        return PriceBand.aPriceBand()
            .minVolume(priceBandDto.getMinVolume())
            .maxVolume(priceBandDto.getMaxVolume())
            .buyerPrice(priceBandDto.getBuyerPrice().toBigDecimal())
            .vendorPrice(priceBandDto.getVendorPrice().toBigDecimal())
            .shoalMargin(priceBandDto.getShoalMargin().toBigDecimal())
            .distributorMargin(priceBandDto.getDistributorMargin().toBigDecimal())
            .build();
    }
}
