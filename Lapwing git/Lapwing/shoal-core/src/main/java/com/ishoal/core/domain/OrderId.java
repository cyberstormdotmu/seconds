package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderId {
    public static final OrderId emptyOrderId = new OrderId(null);

    private Long id;

    private OrderId(Long id) {
        this.id = id;
    }

    public static OrderId from(Long id) {
        if(id == null) {
            return emptyOrderId;
        }
        return new OrderId(id);
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
        if (!(o instanceof OrderId)) {
            return false;
        }
        OrderId other = (OrderId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}