package com.ishoal.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.core.domain.BuyerAppliedCredit.aBuyerAppliedCredit;
import static com.ishoal.core.domain.BuyerCredit.aBuyerCredit;
import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class BuyerCreditTest {

    @Test
    public void shouldGiveRemainingBalance() {

        BuyerCredit credit = aBuyerCredit().amount(fromNetAndVat(new BigDecimal("100.00"), new BigDecimal("20.00"))).build();

        assertThat(credit.getRemainingBalance().gross(), is(new BigDecimal("120.00")));
    }

    @Test
    public void shouldGiveAllocatedAmountWhenSomeCreditIsSpent() {

        BuyerAppliedCredit appliedCredit = aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal("50.00"), new BigDecimal("10.00")))
                .build();

        BuyerCredit credit = aBuyerCredit().amount(fromNetAndVat(new BigDecimal("100.00"), new BigDecimal("20.00")))
                .appliedCredit(appliedCredit).build();

        assertThat(credit.getRemainingBalance().gross(), is(new BigDecimal("60.00")));
    }

    @Test
    public void shouldGiveZeroAmountWhenAllCreditSpent() {

        BuyerAppliedCredit appliedCredit = aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal("50.00"), new BigDecimal("10.00"))).build();

        BuyerCredit credit = aBuyerCredit().amount(fromNetAndVat(new BigDecimal("100.00"), new BigDecimal("20.00")))
            .appliedCredit(appliedCredit).appliedCredit(appliedCredit).build();

        assertThat(credit.getRemainingBalance().gross(), is(new BigDecimal("0.00")));
    }
}