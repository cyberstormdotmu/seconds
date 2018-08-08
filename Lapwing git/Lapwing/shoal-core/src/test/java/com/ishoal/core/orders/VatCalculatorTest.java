package com.ishoal.core.orders;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class VatCalculatorTest {

    private static final String VAT_RATE = "20.00";
    private final VatCalculator calculator = new VatCalculator();

    @Test
    public void shouldReturn20WhenNetIs100AndRateIs20Percent() {
        assertThat(calculateVatAmount("100.00", VAT_RATE), is("20.00"));
    }

    @Test
    public void shouldReturn1_33WhenNetIs6_66AndRateIs20Percent() {
        assertThat(calculateVatAmount("6.66", VAT_RATE), is("1.33"));
    }

    @Test
    public void shouldReturn0_41WhenNetIs2_03AndRateIs20Percent() {
        assertThat(calculateVatAmount("2.03", VAT_RATE), is("0.41"));
    }

    @Test
    public void shouldReturnZeroWhenVatRateIsNull() {
        assertThat(calculator.calculateVatAmount(new BigDecimal("100.00"), (BigDecimal)null), is(new BigDecimal("0.00")));
    }

    @Test
    public void remainderOnNetFromGrossIsZeroWhenExact() {
        assertThat(remainderOnNetFromGross("120.00", "100.00", VAT_RATE), is("0.000000"));
    }

    @Test
    public void remainderOnNetFromGrossIsPositiveWhenNetWasRoundedDown() {
        assertThat(remainderOnNetFromGross("100.00", "83.33", VAT_RATE), is("0.003333"));
    }

    @Test
    public void remainderOnNetFromGrossIsNegativeWhenNetWasRoundedUp() {
        assertThat(remainderOnNetFromGross("100.00", "83.34", VAT_RATE), is("-0.006667"));
    }

    @Test
    public void netFromGrossRoundsArithmeticallyWhenNoCumulativeRemainder() {
        assertThat(netFromGross("100.00", VAT_RATE, "0"), is("83.33"));
    }

    @Test
    public void netFromGrossRoundingTakesIntoAccountCumulativeRemainderToRoundUp() {
        assertThat(netFromGross("100.00", "20.00", "0.003333"), is("83.34"));
    }

    @Test
    public void netFromGrossReturnsGrossWhenNullVatRate() {
        assertThat(calculator.calculateNetFromGross(new BigDecimal("100.00"), null, BigDecimal.ZERO), is(new BigDecimal("100.00")));
    }

    @Test
    public void seriesOfNetCalculationsGiveTheExpectedResults() {
        String firstNet = netFromGross("50.00", VAT_RATE, "0");

        String firstRemainder = remainderOnNetFromGross("50.00", firstNet, VAT_RATE);

        String secondNet = netFromGross("50.00", VAT_RATE, firstRemainder);

        String secondRemainder = remainderOnNetFromGross(sum("50.00", "50.00"), sum(firstNet, secondNet), VAT_RATE);

        String finalNet = netFromGross("20.00", VAT_RATE, secondRemainder);

        String finalRemainder = remainderOnNetFromGross(sum("50.00", "50.00", "20.00"), sum(firstNet, secondNet, finalNet), VAT_RATE);

        assertThat(firstNet, is("41.67"));
        assertThat(secondNet, is("41.66"));
        assertThat(finalNet, is("16.67"));
        assertThat(finalRemainder, is("0.000000"));
    }

    private String sum(String first, String second) {
        return new BigDecimal(first).add(new BigDecimal(second)).toPlainString();
    }

    private String sum(String first, String second, String third) {
        return new BigDecimal(first).add(new BigDecimal(second)).add(new BigDecimal(third)).toPlainString();
    }

    private String calculateVatAmount(String net, String vatRate) {
        return calculator.calculateVatAmount(new BigDecimal(net), new BigDecimal(vatRate)).toPlainString();
    }

    private String netFromGross(String gross, String vatRate, String cumulativeRemainder) {
        return calculator.calculateNetFromGross(new BigDecimal(gross), new BigDecimal(vatRate), new BigDecimal(cumulativeRemainder)).toPlainString();
    }

    private String remainderOnNetFromGross(String gross, String net, String vatRate) {
        return calculator.calculateRemainderOnNetFromGross(new BigDecimal(gross), new BigDecimal(net), new BigDecimal(vatRate)).toPlainString();
    }
}