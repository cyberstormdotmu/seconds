package com.ishoal.core.domain;

import static com.ishoal.core.domain.TaxableAmount.ZERO;

public class OfferPayment {
    private final TaxableAmount paymentPerUnit;
    private final TaxableAmount pendingAmount;
    private final TaxableAmount paidAmount;
    private final TaxableAmount unpaidAmount;

    private OfferPayment(Builder builder) {
        paymentPerUnit = builder.paymentPerUnit;
        pendingAmount = builder.pendingAmount;
        paidAmount = builder.paidAmount;
        unpaidAmount = builder.unpaidAmount;
    }

    public static Builder aPayment() {
        return new Builder();
    }

    public TaxableAmount getPaymentPerUnit() {
        return paymentPerUnit;
    }

    public TaxableAmount getPendingAmount() {
        return pendingAmount;
    }

    public TaxableAmount getPaidAmount() {
        return paidAmount;
    }

    public TaxableAmount getUnpaidAmount() {
        return unpaidAmount;
    }

    public static final class Builder {
        private TaxableAmount paymentPerUnit = ZERO;
        private TaxableAmount pendingAmount = ZERO;
        private TaxableAmount paidAmount = ZERO;
        private TaxableAmount unpaidAmount = ZERO;

        private Builder() {
        }

        public Builder paymentPerUnit(TaxableAmount val) {
            paymentPerUnit = val;
            return this;
        }

        public Builder pendingAmount(TaxableAmount val) {
            pendingAmount = val;
            return this;
        }

        public Builder paidAmount(TaxableAmount val) {
            paidAmount = val;
            return this;
        }

        public Builder unpaidAmount(TaxableAmount val) {
            unpaidAmount = val;
            return this;
        }

        public OfferPayment build() {
            return new OfferPayment(this);
        }
    }
}
