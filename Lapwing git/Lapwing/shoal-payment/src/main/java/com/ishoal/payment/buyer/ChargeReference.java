package com.ishoal.payment.buyer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ChargeReference {

    public static final ChargeReference EMPTY_CHARGE_REFERENCE = new ChargeReference(null);

    private final String reference;

    private ChargeReference(String reference) {
        this.reference = reference;
    }
    
    public static ChargeReference from(String reference) {
        if(reference == null) {
            return EMPTY_CHARGE_REFERENCE;
        }
        return new ChargeReference(reference);
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
        ChargeReference other = (ChargeReference) obj;
        return new EqualsBuilder().append(this.reference, other.reference).isEquals();
    }
}