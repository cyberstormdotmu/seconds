package com.ishoal.common.domain;

import com.ishoal.common.util.OrderReferenceGenerator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OrderReference {

    public static final OrderReference EMPTY_ORDER_REFERENCE = new OrderReference(null);

    private final String reference;

    private OrderReference(String reference) {
        this.reference = reference;
    }
    
    public static OrderReference create() {
        return OrderReferenceGenerator.generate();
    }
    
    public static OrderReference from(String reference) {
        if(reference == null) {
            return EMPTY_ORDER_REFERENCE;
        }
        return new OrderReference(reference);
    }

    public String asString() {
        return this.reference;
    }

    @Override
    public String toString() {
        return asString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.reference).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderReference other = (OrderReference) obj;
        return new EqualsBuilder().append(this.reference, other.reference).isEquals();
    }
}