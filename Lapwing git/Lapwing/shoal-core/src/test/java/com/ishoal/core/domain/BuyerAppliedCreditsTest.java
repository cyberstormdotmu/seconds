package com.ishoal.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BuyerAppliedCreditsTest {

    @Test
    public void totalShouldExcludeCancelled() {
        BuyerAppliedCredits appliedCredits = BuyerAppliedCredits.over(
                asList(reservedCredit(), confirmedCredit(), cancelledCredit()));

        assertThat(appliedCredits.getTotal(), is(equalTo(fromNetAndVat(new BigDecimal("30.00"), new BigDecimal("15.00")))));
    }

    @Test
    public void takesAccountOfAppliedCreditAdded() {
        BuyerAppliedCredits appliedCredits = BuyerAppliedCredits.over(asList(confirmedCredit()));
        appliedCredits.add(confirmedCredit());

        assertThat(appliedCredits.getTotal(), is(equalTo(fromNetAndVat(new BigDecimal("40.00"), new BigDecimal("20.00")))));
    }

    private BuyerAppliedCredit reservedCredit() {
        return BuyerAppliedCredit.aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal("10.00"), new BigDecimal("5.00")))
                .status(BuyerAppliedCreditStatus.RESERVED)
                .build();
    }

    private BuyerAppliedCredit confirmedCredit() {
        return BuyerAppliedCredit.aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal("20.00"), new BigDecimal("10.00")))
                .status(BuyerAppliedCreditStatus.CONFIRMED)
                .build();
    }

    private BuyerAppliedCredit cancelledCredit() {
        return BuyerAppliedCredit.aBuyerAppliedCredit()
                .amount(fromNetAndVat(new BigDecimal("40.00"), new BigDecimal("20.00")))
                .status(BuyerAppliedCreditStatus.CANCELLED)
                .build();
    }

}