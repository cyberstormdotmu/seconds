package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductSpecDto {
    private String name;
    private String description;

    private ProductSpecDto() {
        super();
    }

    public ProductSpecDto(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    private ProductSpecDto(Builder builder) {

        name = builder.name;
        description = builder.description;
    }

    public static Builder aProductSpec() {

        return new Builder();
    }

    public static Builder aProductSpec(ProductSpecDto copy) {

        Builder builder = new Builder();
        builder.name = copy.name;
        builder.description = copy.description;
        return builder;
    }

    public String getName() {

        return name;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getDescription() {

        return description;
    }

    public static class Builder {
        private String name;
        private String description;

        private Builder() {

        }

        public Builder name(String name) {

            this.name = name;
            return this;
        }

        public Builder description(String description) {

            this.description = description;
            return this;
        }

        public ProductSpecDto build() {

            return new ProductSpecDto(this);
        }
    }


}
