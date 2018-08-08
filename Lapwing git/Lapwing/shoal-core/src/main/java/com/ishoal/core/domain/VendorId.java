package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class VendorId {
    public static final VendorId emptyVendorId = new VendorId(null);

    private Long id;

    public VendorId(Long id) {
        this.id = id;
    }

    public static VendorId from(Long id) {
        if(id == null) {
            return emptyVendorId;
        }
        return new VendorId(id);
    }

    public Long asLong() {
        return id;
    }
    
    public Long getId() {
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
        if (!(o instanceof VendorId)) {
            return false;
        }
        VendorId other = (VendorId) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }    
}