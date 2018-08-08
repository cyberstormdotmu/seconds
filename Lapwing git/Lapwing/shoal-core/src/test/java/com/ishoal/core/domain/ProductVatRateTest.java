package com.ishoal.core.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProductVatRateTest {

    @Test
    public void shouldStartVatRateWithTodaysDateIfNoneGiven() {

        DateTime todaysDate = new DateTime();
        DateTimeUtils.setCurrentMillisFixed(todaysDate.getMillis());
        ProductVatRate productVatRate = ProductVatRateTestData.vatRateFor20PercentNoStartDate();

        assertThat(productVatRate.getStartDateTime(), is(equalTo(todaysDate.withTimeAtStartOfDay())));
    }

}
