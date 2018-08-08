package com.ishoal.ws.common.dto.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.Orders;
import com.ishoal.ws.common.dto.OrderSummaryDto;

import java.util.List;

import static com.ishoal.ws.common.dto.OrderSummaryDto.anOrderSummary;

public class OrderSummaryDtoAdapter {
    private final TaxableAmountDtoAdapter taxableAmountAdapter = new TaxableAmountDtoAdapter();

    public List<OrderSummaryDto> adapt(Orders orders) {
        return IterableUtils.mapToList(orders, this::adapt);
    }

    public OrderSummaryDto adapt(Order order) {
        return anOrderSummary()
                .reference(order.getReference().asString())
                .status(order.getStatus())
                .buyerOrganisationName(order.getBuyerOrganisation().getName())
                .vendorName(order.getVendor().getName())
                .orderTotal(taxableAmountAdapter.adapt(order.getTotal()))
                .created(order.getCreated())
                .paymentStatus(order.getPaymentStatus())
                .invoiceDate(order.getInvoiceDate())
                .dueDate(order.getDueDate())
                .unpaidAmount(order.getUnpaidAmount())
                .creditTotal(taxableAmountAdapter.adapt(order.getCreditTotal()))
                .dueDate(order.getDueDate())
                .build();
    }
}
