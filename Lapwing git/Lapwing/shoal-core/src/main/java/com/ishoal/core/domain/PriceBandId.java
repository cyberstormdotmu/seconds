package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PriceBandId {
    public static final PriceBandId emptyPriceBandId = new PriceBandId(null);

    private Long id;

    private PriceBandId(Long id) {
        this.id = id;
    }

    public static PriceBandId from(Long id) {
        if (id == null) {
            return emptyPriceBandId;
        }
        return new PriceBandId(id);
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
        if (!(o instanceof PriceBandId)) {
            return false;
        }
        PriceBandId other = (PriceBandId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }
}