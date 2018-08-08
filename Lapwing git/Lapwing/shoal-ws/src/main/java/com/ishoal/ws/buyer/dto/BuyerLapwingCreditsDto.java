package com.ishoal.ws.buyer.dto;

import java.math.BigDecimal;

public class BuyerLapwingCreditsDto {
    private BigDecimal pendingCreditBalance;
    private BigDecimal availableCreditBalance;
    private BigDecimal redeemableCreditBalance;

    private BuyerLapwingCreditsDto(Builder builder) {
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

        public BuyerLapwingCreditsDto build() {
            return new BuyerLapwingCreditsDto(this);
        }
    }
}
