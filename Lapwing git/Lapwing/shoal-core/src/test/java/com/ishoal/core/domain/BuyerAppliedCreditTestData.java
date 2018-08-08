package com.ishoal.core.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;

public class BuyerAppliedCreditTestData {

    private static final BigDecimal VAT_STANDARD = new BigDecimal("20");
    
    public static BuyerAppliedCredit.Builder randomBuyerAppliedCredit() {
        return BuyerAppliedCredit.aBuyerAppliedCredit().amount(randomTaxableAmount());
    }
    
    private static TaxableAmount randomTaxableAmount() {
        return TaxableAmount.fromNetAndVat(randomNetAmount(), VAT_STANDARD);
    }
    
    private static BigDecimal randomNetAmount() {
        return new BigDecimal(RandomUtils.nextInt(1, 1000));
    }
}