package com.ishoal.ws.common.dto.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.OrderLine;
import com.ishoal.core.domain.OrderLines;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.common.dto.OrderLineShoalCreditDto;

import java.util.List;

import static com.ishoal.ws.common.dto.OrderLineDto.anOrderLine;

public class OrderLineDtoAdapter {
    private final TaxableAmountDtoAdapter taxableAmountAdapter = new TaxableAmountDtoAdapter();
    private final OrderLineShoalCreditDtoAdapter creditAdapter = new OrderLineShoalCreditDtoAdapter();


    public List<OrderLineDto> adapt(OrderLines lines) {
        return IterableUtils.mapToList(lines, this::adapt);
    }

    public OrderLineDto adapt(OrderLine line) {
        return anOrderLine()
        		.id(line.getId().asLong())
                .productCode(line.getProduct().getCode().toString())
                .productName(line.getProduct().getName())
                .quantity(line.getQuantity())
                .initialUnitPrice(line.getInitialPriceBand().getBuyerPrice())
                .currentUnitPrice(line.getCurrentPriceBand().getBuyerPrice())
                .stock(line.getProduct().getStock())
                .amount(taxableAmountAdapter.adapt(line.getAmount()))
                .vatRate(line.getVatRate())
                .creditTotal(taxableAmountAdapter.adapt(line.getCreditTotal()))
                .credits(adaptCredits(line))
                .returnedQuantites(line.getReturnedQuantites())
                .build();
    }

    private List<OrderLineShoalCreditDto> adaptCredits(OrderLine line) {
        return creditAdapter.adapt(line.getCredits());
    }
}
