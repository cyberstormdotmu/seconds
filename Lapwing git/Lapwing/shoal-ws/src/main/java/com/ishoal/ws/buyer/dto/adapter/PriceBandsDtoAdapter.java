package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.ws.buyer.dto.MoneyDto;
import com.ishoal.ws.buyer.dto.PriceBandDto;

import java.util.List;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.ws.buyer.dto.PriceBandDto.aPriceBand;

public class PriceBandsDtoAdapter {

    public List<PriceBandDto> adapt(PriceBands priceBands) {

        return mapToList(priceBands, this::adaptPriceBand);
    }

    public PriceBands adapt(List<PriceBandDto> priceBandList) {

        return mapToCollection(priceBandList, this::adaptPriceBandDto, PriceBands::over);
    }

    private PriceBand adaptPriceBandDto(PriceBandDto priceBandDto) {
        return PriceBand.aPriceBand().minVolume(priceBandDto.getMinVolume()).maxVolume(priceBandDto.getMaxVolume())
            .buyerPrice(priceBandDto.getBuyerPrice().toBigDecimal()).build();
    }

    private PriceBandDto adaptPriceBand(PriceBand priceBand) {

        return aPriceBand()
            .minVolume(priceBand.getMinVolume())
            .maxVolume(priceBand.getMaxVolume())
            .buyerPrice(new MoneyDto(priceBand.getBuyerPrice()))
            .build();
    }
}
