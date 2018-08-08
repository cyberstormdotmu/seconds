package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderPayments;
import com.ishoal.core.domain.PaymentType;
import com.ishoal.ws.admin.dto.AdminOrderPaymentDto;

import java.util.List;

public class AdminOrderPaymentDtoAdapter {

    public List<AdminOrderPaymentDto> adapt(OrderPayments payments) {
        return IterableUtils.mapToList(payments, this::adapt);
    }

    public AdminOrderPaymentDto adapt(OrderPayment payment) {
        return AdminOrderPaymentDto.anOrderPayment()
                .paymentReference(payment.getPaymentReference().asString())
                .dateReceived(payment.getDateReceived())
                .paymentType(payment.getPaymentType())
                .amount(payment.getAmount())
                .userReference(payment.getUserReference())
                .paymentGatewayCharges(payment.getPaymentGatewayCharges())
                .paymentRecordType(payment.getPaymentRecordType())
                .build();
    }

    public OrderPayment adapt(AdminOrderPaymentDto payment) {
        return OrderPayment.anOrderPayment()
                .dateReceived(payment.getDateReceived())
                .paymentType(PaymentType.fromDisplayName(payment.getPaymentType()))
                .amount(payment.getAmount())
                .userReference(payment.getUserReference())
                .paymentGatewayCharges(payment.getPaymentGatewayCharges())
                .paymentRecordType(payment.getPaymentRecordType())
                .build();
    }
}
