package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class BuyerAppliedCreditId {
    public static final BuyerAppliedCreditId EMPTY_BUYER_APPLIED_CREDIT_ID = new BuyerAppliedCreditId(null);

    private Long id;

    private BuyerAppliedCreditId(Long id) {
        this.id = id;
    }

    public static BuyerAppliedCreditId from(Long id) {
        if(id == null) {
            return EMPTY_BUYER_APPLIED_CREDIT_ID;
        }
        return new BuyerAppliedCreditId(id);
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
        if (!(o instanceof BuyerAppliedCreditId)) {
            return false;
        }
        BuyerAppliedCreditId other = (BuyerAppliedCreditId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}