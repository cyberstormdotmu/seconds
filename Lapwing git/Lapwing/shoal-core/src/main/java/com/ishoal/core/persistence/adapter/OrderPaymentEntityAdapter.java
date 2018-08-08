package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.OrderPayment;
import com.ishoal.core.domain.OrderPaymentId;
import com.ishoal.core.domain.PaymentReference;
import com.ishoal.core.persistence.entity.OrderEntity;
import com.ishoal.core.persistence.entity.OrderPaymentEntity;

public class OrderPaymentEntityAdapter {
    public OrderPaymentEntity adapt(OrderPayment payment, OrderEntity orderEntity) {
        OrderPaymentEntity entity = new OrderPaymentEntity();
        entity.setOrder(orderEntity);
        entity.setId(payment.getId().asLong());
        entity.setPaymentReference(payment.getPaymentReference().asString());
        entity.setDateReceived(payment.getDateReceived());
        entity.setPaymentType(payment.getPaymentType());
        entity.setAmount(payment.getAmount());
        entity.setUserReference(payment.getUserReference());
        entity.setPaymentGatewayCharges(payment.getPaymentGatewayCharges());
        entity.setPaymentRecordType(payment.getPaymentRecordType());
        entity.setCreated(payment.getCreated());

        return entity;
    }

    public OrderPayment adapt(OrderPaymentEntity entity) {
        return OrderPayment.anOrderPayment()
                .id(OrderPaymentId.from(entity.getId()))
                .paymentReference(PaymentReference.from(entity.getPaymentReference()))
                .dateReceived(entity.getDateReceived())
                .paymentType(entity.getPaymentType())
                .amount(entity.getAmount())
                .userReference(entity.getUserReference())
                .paymentGatewayCharges(entity.getPaymentGatewayCharges())
                .paymentRecordType(entity.getPaymentRecordType())
                .created(entity.getCreated())
                .build();
    }
}
