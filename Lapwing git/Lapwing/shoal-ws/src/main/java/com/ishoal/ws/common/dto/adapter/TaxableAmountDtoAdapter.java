package com.ishoal.ws.common.dto.adapter;

import com.ishoal.core.domain.TaxableAmount;
import com.ishoal.ws.common.dto.TaxableAmountDto;

public class TaxableAmountDtoAdapter {
    public TaxableAmount adapt(TaxableAmountDto dto) {
        return TaxableAmount.fromNetAndVat(dto.getNet(), dto.getVat());
    }

    public TaxableAmountDto adapt(TaxableAmount domain) {
        return TaxableAmountDto.aTaxableAmount().net(domain.net()).vat(domain.vat()).gross(domain.gross()).build();
    }
}
