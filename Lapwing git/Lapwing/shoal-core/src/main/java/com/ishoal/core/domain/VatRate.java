package com.ishoal.core.domain;

import java.math.BigDecimal;

public class VatRate {
	private final Long id;
    private final String code;
    private final BigDecimal rate;

    private VatRate(Builder builder) {
        code = builder.code;
        rate = builder.rate;
        id=builder.id;
    }

    public static Builder aVatRate() {
        return new Builder();
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Long getId() {
		return id;
	}

	public String getCode() {
        return code;
    }

    public static final class Builder {
        private String code;
        private BigDecimal rate;
        private Long id;
        private Builder() {
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }
        
        public Builder rate(BigDecimal val) {
            rate = val;
            return this;
        }

        public VatRate build() {
            return new VatRate(this);
        }
    }
}
