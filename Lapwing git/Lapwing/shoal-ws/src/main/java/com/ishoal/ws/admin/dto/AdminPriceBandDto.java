package com.ishoal.ws.admin.dto;

import com.ishoal.ws.buyer.dto.MoneyDto;

public class AdminPriceBandDto {
    private long minVolume;
    private Long maxVolume;
    private MoneyDto buyerPrice;
    private MoneyDto vendorPrice;
    private MoneyDto shoalMargin;
    private MoneyDto distributorMargin;

    private AdminPriceBandDto() {
        super();
    }

    private AdminPriceBandDto(Builder builder) {

        minVolume = builder.minVolume;
        maxVolume = builder.maxVolume;
        buyerPrice = builder.buyerPrice;
        vendorPrice = builder.vendorPrice;
        shoalMargin = builder.shoalMargin;
        distributorMargin = builder.distributorMargin;
    }

    public static Builder adminPriceBand() {
        return new Builder();
    }

    public long getMinVolume() {
        return minVolume;
    }

    public Long getMaxVolume() {
        return maxVolume;
    }

    public MoneyDto getBuyerPrice() {
        return buyerPrice;
    }

    public MoneyDto getVendorPrice() {
        return vendorPrice;
    }

    public MoneyDto getShoalMargin() {
        return shoalMargin;
    }

    public MoneyDto getDistributorMargin() {

        return distributorMargin;
    }

    public static final class Builder {
        private long minVolume;
        private Long maxVolume;
        private MoneyDto buyerPrice;
        private MoneyDto vendorPrice;
        private MoneyDto shoalMargin;
        private MoneyDto distributorMargin;

        private Builder() {

        }

        public Builder minVolume(long val) {

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

        public Builder vendorPrice(MoneyDto val) {

            vendorPrice = val;
            return this;
        }

        public Builder shoalMargin(MoneyDto val) {

            shoalMargin = val;
            return this;
        }

        public Builder distributorMargin(MoneyDto val) {

            distributorMargin = val;
            return this;
        }

        public AdminPriceBandDto build() {

            return new AdminPriceBandDto(this);
        }
    }
}
