package com.ishoal.ws.buyer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PriceBandDto {

    private Long minVolume;
    private Long maxVolume;
    private MoneyDto buyerPrice;

    private PriceBandDto() {
        super();
    }

    private PriceBandDto(Builder builder) {
        this();
        minVolume = builder.minVolume;
        maxVolume = builder.maxVolume;
        buyerPrice = builder.buyerPrice;
    }

    public static Builder aPriceBand() {
        return new Builder();
    }

    public Long getMinVolume() {

        return minVolume;
    }

    public Long getMaxVolume() {

        return maxVolume;
    }

    public MoneyDto getBuyerPrice() {

        return buyerPrice;
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
        private Long minVolume;
        private Long maxVolume;
        private MoneyDto buyerPrice;

        public Builder() {

        }

        public Builder minVolume(Long val) {

            minVolume = val;
            return this;
        }

        public Builder maxVolume(Long val) {

            maxVolume = val;
            return this;
        }

        public Builder buyerPrice(MoneyDto val) {

            buyerPrice = val;
            return this;
        }

        public PriceBandDto build() {

            return new PriceBandDto(this);
        }
    }
}
