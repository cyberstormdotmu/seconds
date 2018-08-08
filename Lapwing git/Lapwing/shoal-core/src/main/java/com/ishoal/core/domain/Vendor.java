package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Vendor {

    private final VendorId id;
    private final String name;
    private final String termsAndCondition;
    

    private Vendor(Builder builder) {
        id = builder.id;
        name = builder.name;
        termsAndCondition = builder.termsAndCondition;
    }

    public static Builder aVendor() {
        return new Builder();
    }

    public VendorId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTermsAndCondition() {
		return termsAndCondition;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vendor vendor = (Vendor) o;

        return new EqualsBuilder()
                .append(id, vendor.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    public static final class Builder {
        private VendorId id = VendorId.emptyVendorId;
        private String name;
        private String termsAndCondition;

        private Builder() {
        }

        public Builder id(VendorId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }
        
        public Builder termsAndCondition(String val) {
        	termsAndCondition = val;
            return this;
        }

        public Vendor build() {
            return new Vendor(this);
        }
    }
}
