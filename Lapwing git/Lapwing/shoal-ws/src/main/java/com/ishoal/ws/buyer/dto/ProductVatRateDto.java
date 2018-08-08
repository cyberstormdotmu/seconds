package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class ProductVatRateDto {
    private String code;
    private BigDecimal rate;

    public ProductVatRateDto() {

    }

    private ProductVatRateDto(Builder builder) {

        code = builder.code;
        rate = builder.rate;
    }

    public String getCode() {

        return code;
    }

    public BigDecimal getRate() {

        return rate;
    }

    public static Builder aVatRateDto() {

        return new Builder();
    }

    public static final class Builder {
        private String code;
        private BigDecimal rate;

        private Builder() {

        }

        public Builder code(String val) {

            code = val;
            return this;
        }

        public Builder rate(BigDecimal val) {

            rate = val;
            return this;
        }

        public ProductVatRateDto build() {

            return new ProductVatRateDto(this);
        }
    }
}
