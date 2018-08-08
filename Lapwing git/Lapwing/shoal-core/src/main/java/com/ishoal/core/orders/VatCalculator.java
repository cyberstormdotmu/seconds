package com.ishoal.core.orders;

import com.ishoal.core.domain.VatRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class VatCalculator {

    public static final int MONEY_DPS = 2;
    public static final int UNROUNDED_DPS = 6;
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public BigDecimal calculateVatAmount(BigDecimal net, VatRate vatRate) {
        return calculateVatAmount(net, vatRate.getRate());
    }

    public BigDecimal calculateVatAmount(BigDecimal net, BigDecimal vatRate) {
        BigDecimal vatAmount;
        if(vatRate != null) {
            vatAmount = net.multiply(vatRate).divide(ONE_HUNDRED, MONEY_DPS, RoundingMode.HALF_UP);
        } else {
            vatAmount = BigDecimal.ZERO.setScale(MONEY_DPS);
        }
        return vatAmount;
    }

    public BigDecimal calculateNetFromGross(BigDecimal gross, BigDecimal vatRate, BigDecimal cumulativeRemainder) {
        if(vatRate != null) {
            return applyCumulativeRounding(calculateUnroundedNetFromGross(gross, vatRate), cumulativeRemainder);
        }
        return gross;
    }

    public BigDecimal calculateRemainderOnNetFromGross(BigDecimal gross, BigDecimal net, BigDecimal vatRate) {
        BigDecimal unrounded = calculateUnroundedNetFromGross(gross, vatRate);

        return unrounded.subtract(net);
    }

    public BigDecimal calculateUnroundedNetFromGross(BigDecimal gross, BigDecimal vatRate) {
        if(vatRate != null) {
            return gross.multiply(ONE_HUNDRED).divide(vatRate.add(ONE_HUNDRED), UNROUNDED_DPS, RoundingMode.HALF_UP);
        }
        return gross;
    }

    private BigDecimal applyCumulativeRounding(BigDecimal unrounded, BigDecimal cumulativeRemainder) {
        BigDecimal unroundedWithCumulativeRoundingError = unrounded.add(Optional.ofNullable(cumulativeRemainder).orElse(BigDecimal.ZERO));

        return unroundedWithCumulativeRoundingError.setScale(MONEY_DPS, RoundingMode.HALF_UP);
    }
}
