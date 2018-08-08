package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OrderPayments implements Streamable<OrderPayment> {
    private final List<OrderPayment> payments;

    private OrderPayments(List<OrderPayment> payments) {
        this.payments = new ArrayList<>(payments);
    }
    
    public static OrderPayments over(List<OrderPayment> payments) {
        return new OrderPayments(payments);
    }

    public void add(OrderPayment payment) {
        this.payments.add(payment);
    }

    public boolean remove(PaymentReference paymentReference) {
        for(int i = 0; i < payments.size(); i++) {
            OrderPayment payment = payments.get(i);
            if(payment.getPaymentReference().equals(paymentReference)) {
                payments.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<OrderPayment> iterator() {
        return this.payments.iterator();
    }

    public int size() {
        return this.payments.size();
    }

    public List<OrderPayment> list() {
        return Collections.unmodifiableList(payments);
    }

    public BigDecimal getTotal() {
        return stream().map(OrderPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}