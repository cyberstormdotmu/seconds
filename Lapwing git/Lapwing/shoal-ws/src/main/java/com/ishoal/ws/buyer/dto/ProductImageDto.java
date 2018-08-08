package com.ishoal.ws.buyer.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductImageDto implements Serializable {

    private int order;
    private String url;
    private String description;

    private ProductImageDto() {
        super();
    }

    public ProductImageDto(int order, String url, String description) {
        this();
        this.order = order;
        this.url = url;
        this.description = description;
    }

    private ProductImageDto(Builder builder) {

        order = builder.order;
        url = builder.url;
        description = builder.description;
    }

    public static Builder aProductImageDto() {

        return new Builder();
    }
    
    public int getOrder() {
        return order;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final class Builder {
        private int order;
        private String url;
        private String description;

        private Builder() {

        }

        public Builder order(int val) {

            order = val;
            return this;
        }

        public Builder url(String val) {

            url = val;
            return this;
        }

        public Builder description(String val) {

            description = val;
            return this;
        }

        public ProductImageDto build() {

            return new ProductImageDto(this);
        }
    }
}
