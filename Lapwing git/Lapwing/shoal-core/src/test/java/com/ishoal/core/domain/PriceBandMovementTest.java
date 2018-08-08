package com.ishoal.core.domain;

import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.PriceBandMovement.aPriceBandMovement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class PriceBandMovementTest {

    private PriceBand priceBand1;
    private PriceBand priceBand2;

    private PriceBandMovement priceBandMovement;

    @Before
    public void before() {
        this.priceBand1 = aPriceBand().buyerPrice(new BigDecimal("1000")).build();
        this.priceBand2 = aPriceBand().buyerPrice(new BigDecimal("793")).build();
        this.priceBandMovement = aPriceBandMovement().startPriceBand(this.priceBand1).endPriceBand(this.priceBand2)
                .build();
    }

    @Test
    public void shouldCalculateCustomerCreditCorrectlyForPriceBandMovement() {
        assertThat(this.priceBandMovement.priceMovementForQuantity(3).getCustomerCredit(),
                is(equalTo(new BigDecimal("621.00"))));
    }
}