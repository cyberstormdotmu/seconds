package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class PaymentReference {

    private final String reference;

    private PaymentReference(String reference) {
        this.reference = reference;
    }
    
    public static PaymentReference create() {
        return new PaymentReference(UUID.randomUUID().toString());
    }
    
    public static PaymentReference from(String reference) {
        return new PaymentReference(reference);
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
        PaymentReference other = (PaymentReference) obj;
        return new EqualsBuilder().append(this.reference, other.reference).isEquals();
    }
}