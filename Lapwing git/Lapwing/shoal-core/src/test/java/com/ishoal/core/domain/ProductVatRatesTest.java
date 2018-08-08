package com.ishoal.core.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.ishoal.core.domain.ProductVatRate.aProductVatRate;
import static com.ishoal.core.domain.VatRate.aVatRate;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertThat;

public class ProductVatRatesTest {

    @Test
    public void sizeReportsTheCorrectSize() {
        ProductVatRates vatRates = ProductVatRates.over(Arrays.asList(vatRate(), vatRate()));
        assertThat(vatRates.size(), is(2));
    }

    @Test
    public void currentReturnsTheRateIfItIsCurrent() {
        ProductVatRates vatRates = ProductVatRates.over(vatRate(now(), now().plusDays(1)));
        assertThat(vatRates.current(), is(not(nullValue())));
    }

    @Test
    public void currentDoesNotReturnTheRateIfItIsNotYetCurrent() {
        ProductVatRates vatRates = ProductVatRates.over(vatRate(now().plusHours(1), now().plusDays(1)));
        assertThat(vatRates.current(), is(nullValue()));
    }

    @Test
    public void currentDoesNotReturnTheRateIfItHasExpired() {
        ProductVatRates vatRates = ProductVatRates.over(vatRate(now().minusDays(1), now().minusSeconds(1)));
        assertThat(vatRates.current(), is(nullValue()));
    }

    @Test
    public void shouldReturnEmptyListWhenGivenNull() {

        ProductVatRates rates = ProductVatRates.over((ProductVatRate) null);

        assertThat(rates.size(), is(0));
    }

    private ProductVatRate vatRate(DateTime startDate, DateTime endDate) {
        return aProductVatRate()
                .startDateTime(startDate)
                .endDateTime(endDate)
                .vatRate(aVatRate().rate(BigDecimal.TEN).build())
                .build();
    }

    private ProductVatRate vatRate() {
        return aProductVatRate().build();
    }

}