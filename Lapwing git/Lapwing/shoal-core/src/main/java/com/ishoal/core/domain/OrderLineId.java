package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderLineId {
    public static final OrderLineId emptyOrderLineId = new OrderLineId(null);

    private Long id;

    private OrderLineId(Long id) {
        this.id = id;
    }

    public static OrderLineId from(Long id) {
        if(id == null) {
            return emptyOrderLineId;
        }
        return new OrderLineId(id);
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
        if (!(o instanceof OrderLineId)) {
            return false;
        }
        OrderLineId other = (OrderLineId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}