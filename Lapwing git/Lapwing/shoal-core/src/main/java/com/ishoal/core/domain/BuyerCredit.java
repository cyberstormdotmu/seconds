package com.ishoal.core.domain;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class BuyerCredit {

    private final BuyerCreditId id;
    private final CreditType creditType;
    private final boolean isSpendable;
    private final boolean isRedeemable;
    private final TaxableAmount amount;
    private final DateTime created;
    private final BuyerAppliedCredits appliedCredits;
    private final String reason;

    private BuyerCredit(Builder builder) {
        this.id = builder.id;
        this.creditType = builder.creditType;
        this.isSpendable = builder.isSpendable;
        this.isRedeemable = builder.isRedeemable;
        this.amount = builder.amount;
        this.reason = builder.reason;
        this.created = builder.created;
        this.appliedCredits = BuyerAppliedCredits.over(builder.appliedCredits);
    }
    
    public static Builder aBuyerCredit() {
        return new Builder();
    }
    
    public BuyerCreditId getId() {
        return id;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public boolean isSpendable() {
        return isSpendable;
    }

    public boolean isRedeemable() {
        return isRedeemable;
    }

    public TaxableAmount getAmount() {
        return amount;
    }
    
    public TaxableAmount getRemainingBalance() {

        return getAmount().subtract(appliedCredits.getTotal());
    }

    public BuyerAppliedCredits getAppliedCredits() {
        return appliedCredits;
    }

    public TaxableAmount getConsumedCreditTotal() {
        return appliedCredits.getTotal();
    }

    public DateTime getCreated() {
        return created;
    }

    public boolean isPartConsumed() {
        return getAmount().gross().compareTo(getRemainingBalance().gross()) > 0;
    }

    public void consumeCredit(BuyerAppliedCredit newAppliedCredit) {
        appliedCredits.add(newAppliedCredit);
    }

    public String getReason() {
        return reason;
    }

    public static class Builder {
        
        private BuyerCreditId id = BuyerCreditId.EMPTY_BUYER_CREDIT_ID;
        private CreditType creditType;
        private boolean isSpendable;
        private boolean isRedeemable;
        private TaxableAmount amount;
        private String reason;
        private DateTime created;
        private List<BuyerAppliedCredit> appliedCredits = new ArrayList<>();
        
        private Builder() {
            super();
        }
        
        public Builder id(BuyerCreditId id) {
            this.id = id;
            return this;
        }

        public Builder creditType(CreditType creditType) {
            this.creditType = creditType;
            return this;
        }

        public Builder spendable(boolean isSpendable) {
            this.isSpendable = isSpendable;
            return this;
        }

        public Builder redeemable(boolean isRedeemable) {
            this.isRedeemable = isRedeemable;
            return this;
        }

        public Builder amount(TaxableAmount amount) {
            this.amount = amount;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        public Builder appliedCredit(BuyerAppliedCredit credit) {
            this.appliedCredits.add(credit);
            return this;
        }

        public Builder appliedCredits(BuyerAppliedCredits credits) {
            this.appliedCredits.addAll(credits.list());
            return this;
        }

        public BuyerCredit build() {
            return new BuyerCredit(this);
        }


    }
}