package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PaymentStatusTest {

    @Test
    public void fromDisplayNameReturnsPaymentStatusWhenValid() {
        assertThat(PaymentStatus.fromDisplayName(PaymentStatus.PART_PAID.getDisplayName()), is(PaymentStatus.PART_PAID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromDisplayNameThrowsWhenInvalid() {
        PaymentStatus.fromDisplayName("Not a valid credit type");
    }
}