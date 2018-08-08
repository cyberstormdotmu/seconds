package com.ishoal.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class PriceBand {

    private final PriceBandId id;
    private final long minVolume;
    private final Long maxVolume;
    private final BigDecimal buyerPrice;
    private final BigDecimal vendorPrice;
    private final BigDecimal shoalMargin;
    private final BigDecimal distributorMargin;

    private PriceBand(Builder builder) {
        id = builder.id;
        minVolume = builder.minVolume;
        maxVolume = builder.maxVolume;
        buyerPrice = builder.buyerPrice;
        vendorPrice = builder.vendorPrice;
        shoalMargin = builder.shoalMargin;
        distributorMargin = builder.distributorMargin;
    }

    public static Builder aPriceBand() {
        return new Builder();
    }

    public PriceBandId getId() {
        return id;
    }

    public long getMinVolume() {
        return minVolume;
    }

    public Long getMaxVolume() {
        return maxVolume;
    }

    public BigDecimal getBuyerPrice() {
        return buyerPrice;
    }

    public BigDecimal getVendorPrice() {
        return vendorPrice;
    }

    public BigDecimal getShoalMargin() {
        return shoalMargin;
    }

    public BigDecimal getDistributorMargin() {
        return distributorMargin;
    }

    public boolean isVolumeWithinBand(long volume) {
        return isWithinMinVolume(volume) && isWithinMaxVolume(volume);
    }

    private boolean isWithinMinVolume(long volume) {
        return getMinVolume() <= volume;
    }

    private boolean isWithinMaxVolume(long volume) {
        return getMaxVolume() == null || volume <= getMaxVolume().longValue();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceBand)) {
            return false;
        }
        PriceBand other = (PriceBand) o;
        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.id).hashCode();
    }

    public static final class Builder {
        private PriceBandId id = PriceBandId.emptyPriceBandId;
        private long minVolume;
        private Long maxVolume;
        private BigDecimal buyerPrice;
        private BigDecimal vendorPrice;
        private BigDecimal shoalMargin;
        private BigDecimal distributorMargin;

        private Builder() {
            super();
        }

        public Builder id(PriceBandId val) {
            id = val;
            return this;
        }

        public Builder minVolume(long val) {
            minVolume = val;
            return this;
        }

        public Builder maxVolume(Long val) {
            maxVolume = val;
            return this;
        }

        public Builder buyerPrice(BigDecimal val) {
            buyerPrice = val;
            return this;
        }

        public Builder vendorPrice(BigDecimal val) {
            vendorPrice = val;
            return this;
        }

        public Builder shoalMargin(BigDecimal val) {
            shoalMargin = val;
            return this;
        }

        public Builder distributorMargin(BigDecimal val) {
            distributorMargin = val;
            return this;
        }

        public PriceBand build() {
            return new PriceBand(this);
        }
    }
}
