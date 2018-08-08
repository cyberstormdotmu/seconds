package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BuyerCredits implements Streamable<BuyerCredit> {
    private final List<BuyerCredit> credits;

    private BuyerCredits(List<BuyerCredit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public static BuyerCredits over(List<BuyerCredit> credits) {
        return new BuyerCredits(credits);
    }

    public void add(BuyerCredit credit) {
        this.credits.add(credit);
    }

    @Override
    public Iterator<BuyerCredit> iterator() {
        return this.credits.iterator();
    }

    public int size() {
        return this.credits.size();
    }

    public TaxableAmount getPendingCreditBalance() {
        return TaxableAmount.sum(stream().filter(this::pendingCreditFilter), BuyerCredit::getAmount);
    }

    public TaxableAmount getAvailableCreditBalance() {
        return TaxableAmount.sum(stream().filter(this::availableCreditFilter), BuyerCredit::getRemainingBalance);
    }

    public TaxableAmount getRedeemableCreditBalance() {
        return TaxableAmount.sum(stream().filter(this::redeemableCreditFilter), BuyerCredit::getRemainingBalance);
    }

    private boolean pendingCreditFilter(BuyerCredit credit) {
        return !credit.isSpendable();
    }

    private boolean availableCreditFilter(BuyerCredit credit) {
        return credit.isSpendable();
    }

    private boolean redeemableCreditFilter(BuyerCredit credit) {
        return credit.isRedeemable();
    }

    public TaxableAmount getTotal() {
        return TaxableAmount.sum(stream(), BuyerCredit::getAmount);
    }

    public TaxableAmount getConsumedCreditTotal() {
        return TaxableAmount.sum(stream(), BuyerCredit::getConsumedCreditTotal);
    }

    public boolean hasPartConsumedCredit() {
        for(BuyerCredit credit : credits) {
            if(credit.isPartConsumed()) {
                return true;
            }
        }

        return false;
    }

    public List<BuyerCredit> list() {
        return Collections.unmodifiableList(credits);
    }
}