package com.ishoal.core.domain;

import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.core.domain.TaxableAmount.fromNetAndVat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TaxableAmountTest {

    @Test
    public void taxableAmountsAreEqualIfHaveSameNetAndVat() {
        assertThat(taxableAmount("100.00", "20.00"), is(equalTo(taxableAmount("100.00", "20.00"))));
    }

    @Test
    public void taxableAmountsAreNotEqualIfHaveDifferentNet() {
        assertThat(taxableAmount("100.01", "20.00"), is(not(equalTo(taxableAmount("100.00", "20.00")))));
    }

    @Test
    public void taxableAmountsAreNotEqualIfHaveDifferentVat() {
        assertThat(taxableAmount("100.00", "20.01"), is(not(equalTo(taxableAmount("100.00", "20.00")))));
    }

    @Test
    public void notEqualToNull() {
        assertThat(taxableAmount("100.00", "20.00"), is(not(equalTo(null))));
    }

    @Test
    public void notEqualToOtherObjectType() {
        assertThat(taxableAmount("100.00", "20.00"), is(not(equalTo("hello"))));
    }

    @Test
    public void grossIsCalculatedFromNetAndVat() {
        assertThat(taxableAmount("100.00", "20.00").gross(), is(equalTo(new BigDecimal("120.00"))));
    }

    @Test
    public void netIsTheValuePassedIn() {
        assertThat(taxableAmount("100.00", "20.00").net(), is(equalTo(new BigDecimal("100.00"))));
    }

    @Test
    public void vatIsTheValuePassedIn() {
        assertThat(taxableAmount("100.00", "20.00").vat(), is(equalTo(new BigDecimal("20.00"))));
    }

    @Test
    public void canAddTwoAmounts() {
        TaxableAmount first = taxableAmount("100.00", "20.00");
        TaxableAmount second = taxableAmount("50.00", "10.00");
        assertThat(first.add(second), is(equalTo(taxableAmount("150.00", "30.00"))));
    }

    @Test
    public void canSubtractTwoAmounts() {
        TaxableAmount first = taxableAmount("100.00", "20.00");
        TaxableAmount second = taxableAmount("10.00", "2.00");
        assertThat(first.subtract(second), is(equalTo(taxableAmount("90.00", "18.00"))));
    }

    private TaxableAmount taxableAmount(String net, String vat) {
        return fromNetAndVat(new BigDecimal(net), new BigDecimal(vat));
    }

}