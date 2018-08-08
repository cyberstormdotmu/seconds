package com.ishoal.core.domain;

import java.math.BigDecimal;

import static com.ishoal.core.domain.PriceBand.aPriceBand;

public class PriceBandTestData {

    public static PriceBand firstPriceBand() {
        return aPriceBand().minVolume(0)
                .maxVolume(999L)
                .buyerPrice(new BigDecimal("1000.00"))
                .vendorPrice(new BigDecimal("900.00"))
                .shoalMargin(new BigDecimal("30.00"))
                .distributorMargin(new BigDecimal("20.00"))
                .build();
    }

    public static PriceBand lastPriceBand() {
        return aPriceBand().minVolume(1000)
                .buyerPrice(new BigDecimal("900.00"))
                .vendorPrice(new BigDecimal("810.00"))
                .shoalMargin(new BigDecimal("30.00"))
                .distributorMargin(new BigDecimal("20.00"))
                .build();
    }

    public static PriceBand overlappingPriceBand() {
        return aPriceBand().minVolume(999L)
            .maxVolume(999L)
            .buyerPrice(new BigDecimal("1000.00"))
            .vendorPrice(new BigDecimal("900.00"))
            .shoalMargin(new BigDecimal("30.00"))
            .distributorMargin(new BigDecimal("20.00"))
            .build();
    }

    public static PriceBand invalidPriceBand() {
        return aPriceBand().minVolume(999L)
            .maxVolume(998L)
            .buyerPrice(new BigDecimal("1000.00"))
            .vendorPrice(new BigDecimal("900.00"))
            .shoalMargin(new BigDecimal("30.00"))
            .distributorMargin(new BigDecimal("20.00"))
            .build();
    }

    public static PriceBand lastPriceBandNoUpperBound() {
        return aPriceBand().minVolume(1000L)
            .maxVolume(99999L)
            .buyerPrice(new BigDecimal("900.00"))
            .vendorPrice(new BigDecimal("810.00"))
            .shoalMargin(new BigDecimal("30.00"))
            .distributorMargin(new BigDecimal("20.00"))
            .build();
    }

    public static PriceBand lastPriceBandWithGap() {
        return aPriceBand().minVolume(1001L)
            .maxVolume(null)
            .buyerPrice(new BigDecimal("900.00"))
            .vendorPrice(new BigDecimal("810.00"))
            .shoalMargin(new BigDecimal("30.00"))
            .distributorMargin(new BigDecimal("20.00"))
            .build();
    }
}
