package com.ishoal.ws.common.dto;

import com.ishoal.core.domain.VendorId;

public class VendorDto {

	private final VendorId id;
    private final String name; 

    private VendorDto(Builder builder) {
        id = builder.id;
        name = builder.name;
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

 
    public static final class Builder {
        private VendorId id = VendorId.emptyVendorId;
        private String name;

        public Builder(VendorDto copy) {
        	
        	id = copy.id;
        	name=copy.name;
        }
        	
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

        public VendorDto build() {
            return new VendorDto(this);
        }
    }
}
