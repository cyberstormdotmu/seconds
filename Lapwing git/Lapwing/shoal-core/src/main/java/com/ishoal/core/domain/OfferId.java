package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class OfferId {
    private long id;

    public OfferId(long id) {
        this.id = id;
    }

    public static OfferId from(Long id) {
        if(id == null) {
            return null;
        }
        return new OfferId(id);
    }

    public long asLong() {
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
        if (!(o instanceof OfferId)) {
            return false;
        }
        OfferId other = (OfferId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}