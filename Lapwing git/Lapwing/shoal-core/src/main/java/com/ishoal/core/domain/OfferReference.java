package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

public class OfferReference {

    public static final OfferReference EMPTY_OFFER_REFERENCE = new OfferReference(null);

    private final String reference;

    private OfferReference(String reference) {
        this.reference = reference;
    }
    
    public static OfferReference create() {
        return new OfferReference(UUID.randomUUID().toString());
    }
    
    public static OfferReference from(String reference) {
        if(reference == null) {
            return EMPTY_OFFER_REFERENCE;
        }
        return new OfferReference(reference);
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
        OfferReference other = (OfferReference) obj;
        return new EqualsBuilder().append(this.reference, other.reference).isEquals();
    }
}