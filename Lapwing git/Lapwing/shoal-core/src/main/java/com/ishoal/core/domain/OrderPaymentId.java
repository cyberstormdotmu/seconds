package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderPaymentId {
    public static final OrderPaymentId emptyOrderPaymentId = new OrderPaymentId(null);

    private Long id;

    private OrderPaymentId(Long id) {
        this.id = id;
    }

    public static OrderPaymentId from(Long id) {
        if(id == null) {
            return emptyOrderPaymentId;
        }
        return new OrderPaymentId(id);
    }

    public Long asLong() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.valueOf(asLong());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderPaymentId)) {
            return false;
        }
        OrderPaymentId other = (OrderPaymentId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}