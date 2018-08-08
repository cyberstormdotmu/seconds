package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.core.domain.Order;
import com.ishoal.core.domain.User;
import com.ishoal.ws.admin.dto.AdminAppliedCreditDto;
import com.ishoal.ws.admin.dto.AdminOrderBuyerDto;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import com.ishoal.ws.admin.dto.AdminOrderPaymentDto;
import com.ishoal.ws.common.dto.OrderSummaryDto;
import com.ishoal.ws.common.dto.adapter.AddressDtoAdapter;
import com.ishoal.ws.common.dto.adapter.OrderLineDtoAdapter;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;

import java.util.List;

import static com.ishoal.ws.admin.dto.AdminOrderDto.anOrder;

public class AdminOrderDtoAdapter {

    private final OrderSummaryDtoAdapter summaryAdapter = new OrderSummaryDtoAdapter();
    private final AdminOrderBuyerDtoAdapter buyerAdapter = new AdminOrderBuyerDtoAdapter();
    private final OrderLineDtoAdapter lineAdapter = new OrderLineDtoAdapter();
    private final AdminOrderPaymentDtoAdapter paymentAdapter = new AdminOrderPaymentDtoAdapter();
    private final AdminAppliedCreditDtoAdapter appliedCreditAdapter = new AdminAppliedCreditDtoAdapter();
    private final AddressDtoAdapter addressAdapter = new AddressDtoAdapter();

    public AdminOrderDto adapt(Order order) {
        return anOrder()
                .summary(adaptSummary(order))
                .version(order.getVersion())
                .buyer(adaptBuyer(order.getBuyer()))
                .invoiceAddress(addressAdapter.adapt(order.getInvoiceAddress()))
                .deliveryAddress(addressAdapter.adapt(order.getDeliveryAddress()))
                .lines(adaptLines(order))
                .payments(adaptPayments(order))
                .appliedCredits(adaptAppliedCredits(order))
                .build();
    }

    private List<OrderLineDto> adaptLines(Order order) {
        return lineAdapter.adapt(order.getLines());
    }

    private List<AdminOrderPaymentDto> adaptPayments(Order order) {
        return paymentAdapter.adapt(order.getPayments());
    }

    private OrderSummaryDto adaptSummary(Order order) {
        return summaryAdapter.adapt(order);
    }

    private List<AdminAppliedCreditDto> adaptAppliedCredits(Order order) {
        return appliedCreditAdapter.adapt(order.getAppliedCredits());
    }

    private AdminOrderBuyerDto adaptBuyer(User buyer) {
        return buyerAdapter.adapt(buyer);
    }
}
