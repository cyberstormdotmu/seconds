package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.User;
import com.ishoal.ws.admin.dto.AdminOrderBuyerDto;

import static com.ishoal.ws.admin.dto.AdminOrderBuyerDto.aBuyer;

public class AdminOrderBuyerDtoAdapter {

    public AdminOrderBuyerDto adapt(User buyer) {
        return aBuyer()
                .buyerUserName(buyer.getUsername())
                .buyerForename(buyer.getForename())
                .buyerSurname(buyer.getSurname())
                .build();
    }
}
