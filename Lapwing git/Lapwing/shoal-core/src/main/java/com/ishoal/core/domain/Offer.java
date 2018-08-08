package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.math.BigDecimal;

import static java.util.Comparator.comparing;

public class Offer {
	private final Long offerId;
    private final OfferId id;
    private final OfferReference offerReference;
    private final PriceBands priceBands;
    private final DateTime startDateTime;
    private final DateTime endDateTime;
    private final long currentVolume;

    private Offer(Builder builder) {
        id = builder.id;
        offerReference = builder.offerReference;
        priceBands = builder.priceBands;
        startDateTime = builder.startDateTime;
        offerId=builder.offerId;
        if (builder.endDateTime != null) {
            endDateTime = builder.endDateTime.withTime(23, 59, 59, 0);
        } else {
            endDateTime = null;
        }
        currentVolume = builder.currentVolume;
    }

    public static Builder anOffer() {
        return new Builder();
    }

    public Long getOfferId() {
		return offerId;
	}

	public OfferId getId() {
        return id;
    }

    public OfferReference getOfferReference() {
        return offerReference;
    }

    public PriceBands getPriceBands() {
        return priceBands;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public long getCurrentVolume() {
        return currentVolume;
    }

    public PriceBand getInitialPriceBand() {
        return priceBands.first();
    }

    public PriceBand getTargetPriceBand() {
        return priceBands.last();
    }

    public PriceBand getCurrentPriceBand() {
        return priceBands.getPriceBandForVolume(currentVolume);
    }

    public PriceBand getPriceBandForBuyerPrice(BigDecimal unitPrice) {
        return priceBands.getPriceBandForBuyerPrice(unitPrice);
    }

    public PriceBand getPriceBandForRequestedQuantity(long quantity) {
        return priceBands.getPriceBandForVolume(currentVolume + quantity);
    }

    public BigDecimal getVendorMinimumPaymentPerUnit() {
        PriceBand bandWithMinPayment = priceBands.stream().min(comparing((PriceBand b) -> b.getVendorPrice())).get();
        return bandWithMinPayment.getVendorPrice();
    }

    public BigDecimal getShoalMinimumPaymentPerUnit() {
        PriceBand bandWithMinPayment = priceBands.stream().min(comparing((PriceBand b) -> b.getShoalMargin())).get();
        return bandWithMinPayment.getShoalMargin();
    }

    public BigDecimal getVendorPaymentPerUnit(long volume) {
        return priceBands.getPriceBandForVolume(volume).getVendorPrice();
    }

    public BigDecimal getShoalPaymentPerUnit(long volume) {
        return priceBands.getPriceBandForVolume(volume).getShoalMargin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Offer)) {
            return false;
        }
        Offer other = (Offer) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isValidForSave() {
        return !getPriceBands().isEmpty() &&
            startsInFutureOrToday() && hasValidDateRange() &&
            priceBands.allValidForSave();
    }

    private boolean startsInFutureOrToday() {
        DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();
        return isAfterOrEqual(getStartDateTime(), startOfToday);
    }

    private static boolean isAfterOrEqual(DateTime first, DateTime second) {

        return first.isAfter(second) || first.isEqual(second);
    }

    private boolean hasValidDateRange() {

        return getEndDateTime().isAfter(getStartDateTime());
    }

    public boolean endsInPast() {
        DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();
        return getEndDateTime().isBefore(startOfToday);
    }

    public static final class Builder {
    	private Long offerId;
        private OfferId id;
        private OfferReference offerReference = OfferReference.create();
        private PriceBands priceBands;
        private DateTime startDateTime;
        private DateTime endDateTime;
        private long currentVolume;

        private Builder() {
        }

        public Builder id(OfferId val) {
            id = val;
            return this;
        }

        public Builder offerReference(OfferReference val) {
            offerReference = val;
            return this;
        }

        public Builder priceBands(PriceBands val) {
            priceBands = val;
            return this;
        }

        public Builder startDateTime(DateTime val) {
            startDateTime = val;
            return this;
        }

        public Builder endDateTime(DateTime val) {
            endDateTime = val;
            return this;
        }

        public Builder currentVolume(long val) {
            currentVolume = val;
            return this;
        }
        public Builder offerId(Long val) {
            offerId = val;
            return this;
        }

        public Offer build() {
            return new Offer(this);
        }
    }
}
