package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ProductCategory {
	private final Long id;
    private final String name;
    private final ProductCategory parent;

    private ProductCategory(Builder builder) {
        name = builder.name;
        parent = builder.parent;
        id=builder.id;
    }

    public static Builder aProductCategory() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public ProductCategory getParent() {
        return parent;
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
        private String name;
        private ProductCategory parent;
        private Long id;
        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }
        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder parent(ProductCategory val) {
            parent = val;
            return this;
        }

        public ProductCategory build() {
            return new ProductCategory(this);
        }

		
    }
}
