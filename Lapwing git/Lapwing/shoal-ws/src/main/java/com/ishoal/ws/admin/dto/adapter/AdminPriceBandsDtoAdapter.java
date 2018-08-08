package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.PriceBands;
import com.ishoal.ws.admin.dto.AdminPriceBandDto;

import java.util.List;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.common.util.IterableUtils.mapToList;

public class AdminPriceBandsDtoAdapter {

    private AdminPriceBandDtoAdapter priceBandAdapter = new AdminPriceBandDtoAdapter();

    public List<AdminPriceBandDto> adapt(PriceBands priceBands) {

        return mapToList(priceBands, priceBandAdapter::adapt);
    }

    public PriceBands adapt(List<AdminPriceBandDto> priceBandList) {

        return mapToCollection(priceBandList, priceBandAdapter::adapt, PriceBands::over);
    }
}
