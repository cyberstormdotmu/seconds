package com.ishoal.ws.common.dto;

import java.math.BigDecimal;

public class TaxableAmountDto {

    private BigDecimal net;
    private BigDecimal vat;
    private BigDecimal gross;

    private TaxableAmountDto() {
        super();
    }

    private TaxableAmountDto(Builder builder) {
        this();
        net = builder.net;
        vat = builder.vat;
        gross = builder.gross;
    }

    public static Builder aTaxableAmount() {
        return new Builder();
    }

    public BigDecimal getNet() {
        return net;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public static final class Builder {
        private BigDecimal net;
        private BigDecimal vat;
        private BigDecimal gross;

        private Builder() {
        }

        public Builder net(BigDecimal val) {
            net = val;
            return this;
        }

        public Builder vat(BigDecimal val) {
            vat = val;
            return this;
        }

        public Builder gross(BigDecimal val) {
            gross = val;
            return this;
        }

        public TaxableAmountDto build() {
            return new TaxableAmountDto(this);
        }
    }
}
