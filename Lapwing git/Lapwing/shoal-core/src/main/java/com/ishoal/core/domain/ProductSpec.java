package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ProductSpec {
	 private final Long id;
    private final String type;
    private final String value;

    private ProductSpec(Builder builder) {
        type = builder.type;
        value = builder.value;
        id=builder.id;
    }

    public static Builder aProductSpec() {
        return new Builder();
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
   

    public Long getId() {
		return id;
	}

	@Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static final class Builder {
        private String type;
        private String value;
        private Long id;

        private Builder() {
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder value(String val) {
            value = val;
            return this;
        }
        
        public Builder id(Long val) {
            id = val;
            return this;
        }

        public ProductSpec build() {
            return new ProductSpec(this);
        }
    }
}
