package com.ishoal.core.domain;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;

import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.PriceBands.somePriceBands;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceBandsTest {

    private PriceBand priceBand1;
    private PriceBand priceBand2;
    private PriceBand priceBand3;
    private PriceBand priceBand4;

    private PriceBands priceBands;

    @Before
    public void before() {
        PriceBand.Builder priceBandBuilder = aPriceBand();
        this.priceBand1 = priceBandBuilder.id(PriceBandId.from(1L)).minVolume(0L).maxVolume(750L).buyerPrice(new BigDecimal("1000.00")).build();
        this.priceBand2 = priceBandBuilder.id(PriceBandId.from(2L)).minVolume(751L).maxVolume(1250L).buyerPrice(new BigDecimal("900.00")).build();
        this.priceBand3 = priceBandBuilder.id(PriceBandId.from(3L)).minVolume(1251L).maxVolume(1500L).buyerPrice(new BigDecimal("800.00")).build();
        this.priceBand4 = priceBandBuilder.id(PriceBandId.from(4L)).minVolume(1501L).buyerPrice(new BigDecimal("700.00")).build();
        this.priceBands = somePriceBands().priceBand(priceBand1).priceBand(priceBand2).priceBand(priceBand3)
                .priceBand(priceBand4).build();
    }
    
    @Test
    public void rangeShouldReturnAllPriceBandsWhenStartAndEndAreAtEachEnd() {
        PriceBands range = this.priceBands.range(this.priceBand1, this.priceBand4);
        assertThat(range.size(), is(4));
    }
    
    @Test
    public void rangeShouldReturnSinglePriceBandWhenStartAndEndAreTheSame() {
        PriceBands range = rangeWithSinglePriceBand(this.priceBand2);
        assertThat(range.size(), is(1));
        assertThat(range.iterator().next(), is(this.priceBand2));
    }
    
    @Test
    public void rangeShouldReturnMultiplePriceBandsWhenStartAndEndAreApart() {
        PriceBands range = this.priceBands.range(this.priceBand2, this.priceBand3);
        assertThat(range.size(), is(2));
        Iterator<PriceBand> iterator = range.iterator();
        assertThat(iterator.next(), is(this.priceBand2));
        assertThat(iterator.next(), is(this.priceBand3));
    }
    
    @Test
    public void shouldBeNoPriceBandMovementsIfRangeHasSinglePriceBand() {
        assertThat(rangeWithSinglePriceBand().movements().size(), is(0));
    }
    
    @Test
    public void shouldBePriceBandMovementsIfRangeHasMultiplePriceBands() {
        assertThat(this.priceBands.movements().size(), is(3));
    }
    
    @Test
    public void movementShouldHaveStartAndEndBand() {
        PriceBandMovement movement = this.priceBands.movements().iterator().next();
        assertThat(movement.getStartPriceBand(), is(this.priceBand1));
        assertThat(movement.getEndPriceBand(), is(this.priceBand2));
    }
    
    @Test
    public void priceBandForVolumeShouldIdentifyCorrectPriceBand() {
        assertThat(this.priceBands.getPriceBandForVolume(1302L), is(this.priceBand3));
    }

    @Test
    public void priceBandForBuyerPriceShouldIdentifyCorrectPriceBand() {
        assertThat(this.priceBands.getPriceBandForBuyerPrice(new BigDecimal("800.00")), is(this.priceBand3));
    }
    
    private PriceBands rangeWithSinglePriceBand() {
        return rangeWithSinglePriceBand(this.priceBand2);
    }
    
    private PriceBands rangeWithSinglePriceBand(PriceBand priceBand) {
        return this.priceBands.range(priceBand, priceBand);
    }
}