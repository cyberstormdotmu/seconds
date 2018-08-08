package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Order;
import com.ishoal.ws.buyer.dto.BuyerOrderDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;
import com.ishoal.ws.common.dto.adapter.OrderLineDtoAdapter;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;

import static com.ishoal.ws.buyer.dto.BuyerOrderDto.anOrder;

public class BuyerOrderDtoAdapter {

    private OrderSummaryDtoAdapter summaryAdapter = new OrderSummaryDtoAdapter();
    private OrderLineDtoAdapter lineAdapter = new OrderLineDtoAdapter();
    private final AddressDtoAdapter addressAdapter = new AddressDtoAdapter();
    private final BuyerOrderPaymentDtoAdapter paymentAdapter = new BuyerOrderPaymentDtoAdapter();

    public BuyerOrderDto adapt(Order order) {
        return anOrder()
                .summary(summaryAdapter.adapt(order))
                .invoiceAddress(addressAdapter.adapt(order.getInvoiceAddress()))
                .deliveryAddress(addressAdapter.adapt(order.getDeliveryAddress()))
                .lines(lineAdapter.adapt(order.getLines()))
                .payments(paymentAdapter.adapt(order))
                .build();
    }
}
