package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BuyerCreditId {
    public static final BuyerCreditId EMPTY_BUYER_CREDIT_ID = new BuyerCreditId(null);

    private Long id;

    private BuyerCreditId(Long id) {
        this.id = id;
    }

    public static BuyerCreditId from(Long id) {
        if(id == null) {
            return EMPTY_BUYER_CREDIT_ID;
        }
        return new BuyerCreditId(id);
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
        if (!(o instanceof BuyerCreditId)) {
            return false;
        }
        BuyerCreditId other = (BuyerCreditId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}