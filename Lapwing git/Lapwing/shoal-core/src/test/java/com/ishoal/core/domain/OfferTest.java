package com.ishoal.core.domain;

import org.joda.time.DateTime;
import org.junit.Test;

import static com.ishoal.core.domain.Offer.anOffer;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class OfferTest {

    @Test
    public void shouldBeEqualIfSameInstance() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        assertThat(offer, is(equalTo(offer)));
    }

    @Test
    public void shouldBeEqualIfSameIdButDifferentInstance() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        Offer other = anOffer().id(OfferId.from(123L)).build();
        assertThat(offer, is(equalTo(other)));
    }

    @Test
    public void shouldNotBeEqualIfNotSameId() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        Offer other = anOffer().id(OfferId.from(456L)).build();
        assertThat(offer, is(not(equalTo(other))));
    }

    @Test
    public void shouldNotBeEqualIfToNull() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        assertThat(offer, is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualIfToOtherObjectType() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        assertThat(offer, is(not(equalTo("Not an offer"))));
    }

    @Test
    public void shouldHaveSameHashCodeIfSameId() {
        Offer offer = anOffer().id(OfferId.from(123L)).build();
        Offer other = anOffer().id(OfferId.from(123L)).build();
        assertThat(offer.hashCode(), is(equalTo(other.hashCode())));
    }
    
    @Test
    public void shouldAlwaysSetOfferEndDateToEndOfDay() {
        String isoDate = "2016-01-20T00:00:00.000Z";
        String isoEndDay = "2016-01-20T23:59:59.000Z";

        Offer offer = anOffer().endDateTime(DateTime.parse(isoDate)).build();

        assertThat(offer.getEndDateTime(), is(DateTime.parse(isoEndDay)));
    }

}