package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.util.Random;

import static com.ishoal.core.domain.Offer.anOffer;
import static com.ishoal.core.domain.PriceBandTestData.firstPriceBand;
import static com.ishoal.core.domain.PriceBandTestData.invalidPriceBand;
import static com.ishoal.core.domain.PriceBandTestData.lastPriceBand;
import static com.ishoal.core.domain.PriceBandTestData.lastPriceBandNoUpperBound;
import static com.ishoal.core.domain.PriceBandTestData.lastPriceBandWithGap;
import static com.ishoal.core.domain.PriceBandTestData.overlappingPriceBand;
import static com.ishoal.core.domain.PriceBands.somePriceBands;

public class OfferTestData {

    public static final DateTime NEW_OFFER_START_DATE = new DateTime().withTimeAtStartOfDay();
    public static final DateTime NEW_OFFER_END_DATE = NEW_OFFER_START_DATE.plusMonths(1);

    public static final DateTime OFFER_START_DATE = new DateTime().minusDays(1).withTimeAtStartOfDay();
    public static final DateTime OFFER_END_DATE = OFFER_START_DATE.plusMonths(1);

    public static final DateTime FUTURE_OFFER_START_DATE = new DateTime().plusDays(1).withTimeAtStartOfDay();
    public static final DateTime FUTURE_OFFER_END_DATE = FUTURE_OFFER_START_DATE.plusMonths(1).withTimeAtStartOfDay();

    public static final DateTime PAST_OFFER_START_DATE = new DateTime().minusMonths(1).withTimeAtStartOfDay();
    public static final DateTime PAST_OFFER_END_DATE = new DateTime().minusDays(1).withTimeAtStartOfDay();

    public static final DateTime OVERLAPPED_OFFER_START_DATE = new DateTime().plusDays(7).withTimeAtStartOfDay();
    public static final DateTime OVERLAPPED_OFFER_END_DATE = OVERLAPPED_OFFER_START_DATE.minusDays(1).withTimeAtStartOfDay();

    public static Offer.Builder aNewOfferToday() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(NEW_OFFER_START_DATE)
            .endDateTime(NEW_OFFER_END_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder anExistingOpenOffer() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(OFFER_START_DATE)
                .endDateTime(OFFER_END_DATE)
                .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder aFutureOffer() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(FUTURE_OFFER_START_DATE)
            .endDateTime(FUTURE_OFFER_END_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder aPastOffer() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(PAST_OFFER_START_DATE)
            .endDateTime(PAST_OFFER_END_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder anOfferWithOverlappedDateRange() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(OVERLAPPED_OFFER_START_DATE)
            .endDateTime(OVERLAPPED_OFFER_END_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder anOfferWithSameDayRange() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(FUTURE_OFFER_START_DATE)
            .endDateTime(FUTURE_OFFER_START_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBand()).build());
    }

    public static Offer.Builder anOfferWithInvalidPriceBand() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(NEW_OFFER_START_DATE)
            .endDateTime(NEW_OFFER_START_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(invalidPriceBand()).build());
    }

    public static Offer.Builder anOfferWithOverlappingPriceBand() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(NEW_OFFER_START_DATE)
            .endDateTime(NEW_OFFER_START_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(overlappingPriceBand()).build());
    }

    public static Offer.Builder anOfferWithNoUpperBound() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(NEW_OFFER_START_DATE)
            .endDateTime(NEW_OFFER_START_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBandNoUpperBound()).build());
    }

    public static Offer.Builder anOfferWithPriceBandGap() {
        return anOffer().id(generateOfferId())
            .currentVolume(0L)
            .startDateTime(NEW_OFFER_START_DATE)
            .endDateTime(NEW_OFFER_START_DATE)
            .priceBands(somePriceBands().priceBand(firstPriceBand()).priceBand(lastPriceBandWithGap()).build());
    }

    private static OfferId generateOfferId() {
        return OfferId.from(new Random().nextLong());
    }
}
