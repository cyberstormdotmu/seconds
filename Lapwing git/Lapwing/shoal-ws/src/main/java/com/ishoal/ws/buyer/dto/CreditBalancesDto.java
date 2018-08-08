package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class CreditBalancesDto {
    private BigDecimal pendingCreditBalance;
    private BigDecimal availableCreditBalance;
    private BigDecimal redeemableCreditBalance;

    private CreditBalancesDto(Builder builder) {
        pendingCreditBalance = builder.pendingCreditBalance;
        availableCreditBalance = builder.availableCreditBalance;
        redeemableCreditBalance = builder.redeemableCreditBalance;
    }

    public static Builder someCreditBalances() {
        return new Builder();
    }

    public BigDecimal getPendingCreditBalance() {
        return pendingCreditBalance;
    }

    public BigDecimal getAvailableCreditBalance() {
        return availableCreditBalance;
    }

    public BigDecimal getRedeemableCreditBalance() {
        return redeemableCreditBalance;
    }

    public static final class Builder {
        private BigDecimal pendingCreditBalance;
        private BigDecimal availableCreditBalance;
        private BigDecimal redeemableCreditBalance;

        private Builder() {
        }

        public Builder pendingCreditBalance(BigDecimal val) {
            pendingCreditBalance = val;
            return this;
        }

        public Builder availableCreditBalance(BigDecimal val) {
            availableCreditBalance = val;
            return this;
        }

        public Builder redeemableCreditBalance(BigDecimal val) {
            redeemableCreditBalance = val;
            return this;
        }

        public CreditBalancesDto build() {
            return new CreditBalancesDto(this);
        }
    }
}
