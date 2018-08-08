package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.VatRate.aVatRate;

public class ProductVatRateTestData {

    public static ProductVatRates vatRatesWith20PercentRate() {
        return ProductVatRates.over(vatRateFor20Percent());
    }

    public static ProductVatRate vatRateFor20Percent() {
        return aProductVatRate()
                .vatRate(aVatRate().code("STANDARD").rate(new BigDecimal("20.00")).build())
                .startDateTime(DateTime.now().minusYears(1))
                .build();
    }

    public static ProductVatRate vatRateFor20PercentNoStartDate() {
        return aProductVatRate()
            .vatRate(aVatRate().code("STANDARD").rate(new BigDecimal("20.00")).build())
            .build();
    }
}
