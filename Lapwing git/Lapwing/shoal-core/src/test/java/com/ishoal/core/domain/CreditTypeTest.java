package com.ishoal.core.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CreditTypeTest {

    @Test
    public void fromDisplayNameReturnsCreditTypeWhenValid() {
        assertThat(CreditType.fromDisplayName(CreditType.PRICE_MOVEMENT.getDisplayName()), is(CreditType.PRICE_MOVEMENT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromDisplayNameThrowsWhenInvalid() {
        CreditType.fromDisplayName("Not a valid credit type");
    }
}