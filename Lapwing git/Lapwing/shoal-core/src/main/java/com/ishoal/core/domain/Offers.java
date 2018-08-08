package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Offers implements Streamable<Offer> {

    private final List<Offer> offers;

    private Offers(List<Offer> offers) {
        this.offers = Collections.unmodifiableList(new ArrayList<>(offers));
    }

    public static Offers over(List<Offer> offers) {
        return new Offers(offers);
    }

    public static Offers over(Offer... offers) {
        return new Offers(Arrays.asList(offers));
    }

    public Offers add(Offer offer) {
        List<Offer> extendedOffers = new ArrayList<>(this.offers);
        extendedOffers.add(offer);
        return over(extendedOffers);
    }

    public Offer current() {
        DateTime now = DateTime.now();

        for(Offer offer : offers) {
            if(!now.isBefore(offer.getStartDateTime()) && !now.isAfter(offer.getEndDateTime())) {
                return offer;
            }
        }
        return null;
    }

    public Offer find(OfferReference offerReference) {
        for(Offer offer : offers) {
            if(offer.getOfferReference().equals(offerReference)) {
                return offer;
            }
        }
        return null;
    }

    public int size() {
        return offers.size();
    }

    public static Offers emptyOffers() {

        return new Offers(Collections.emptyList());
    }

    public boolean allValidForSave() {

        return offers.stream().allMatch(Offer::isValidForSave);
    }

    public boolean hasActive() {

        return current() != null;
    }

    public boolean allExpired() {

        return !offers.isEmpty() &&
            offers.stream().allMatch(Offer::endsInPast);
    }

    @Override
    public Iterator<Offer> iterator() {
        return this.offers.iterator();
    }


    public boolean isEmpty() {

        return offers.isEmpty();
    }
}
