package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PaymentTypeTest {

    @Test
    public void fromDisplayNameReturnsPaymentTypeWhenValid() {
        assertThat(PaymentType.fromDisplayName(PaymentType.BANK_TRANSFER.getDisplayName()), is(PaymentType.BANK_TRANSFER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromDisplayNameThrowsWhenInvalid() {
        PaymentType.fromDisplayName("Not a valid credit type");
    }
}