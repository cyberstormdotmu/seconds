package com.ishoal.core.domain;

import java.math.BigDecimal;

import static com.ishoal.core.domain.PriceMovement.aPriceMovement;

public class PriceBandMovement {

    private final PriceBand startPriceBand;
    private final PriceBand endPriceBand;

    private PriceBandMovement(Builder builder) {
        this.startPriceBand = builder.startPriceBand;
        this.endPriceBand = builder.endPriceBand;
    }

    public static Builder aPriceBandMovement() {
        return new Builder();
    }

    public PriceBand getStartPriceBand() {
        return startPriceBand;
    }

    public PriceBand getEndPriceBand() {
        return endPriceBand;
    }

    public PriceMovement priceMovementForQuantity(long quantity) {
        return aPriceMovement()
                .customerCredit(calculateCustomerCredit(quantity))
                .reason(createReason())
                .build();
    }

    private BigDecimal calculateCustomerCredit(long quantity) {
        return buyerPriceMovement().multiply(BigDecimal.valueOf(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal buyerPriceMovement() {
        return getStartPriceBand().getBuyerPrice().subtract(getEndPriceBand().getBuyerPrice());
    }

    private String createReason() {
        return String.format("Price movement from £%s to £%s per unit",
                startPriceBand.getBuyerPrice().toPlainString(),
                endPriceBand.getBuyerPrice().toPlainString());
    }

    public static class Builder {

        private PriceBand startPriceBand;
        private PriceBand endPriceBand;

        private Builder() {
            super();
        }

        public Builder startPriceBand(PriceBand priceBand) {
            this.startPriceBand = priceBand;
            return this;
        }

        public Builder endPriceBand(PriceBand priceBand) {
            this.endPriceBand = priceBand;
            return this;
        }

        public PriceBandMovement build() {
            return new PriceBandMovement(this);
        }
    }
}