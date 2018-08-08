package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BuyerAppliedCredits implements Streamable<BuyerAppliedCredit> {
    private final List<BuyerAppliedCredit> credits;

    private BuyerAppliedCredits(List<BuyerAppliedCredit> credits) {
        this.credits = new ArrayList<>(credits);
    }

    public static BuyerAppliedCredits over(List<BuyerAppliedCredit> credits) {
        return new BuyerAppliedCredits(credits);
    }

    public void add(BuyerAppliedCredit credit) {
        this.credits.add(credit);
    }

    @Override
    public Iterator<BuyerAppliedCredit> iterator() {
        return this.credits.iterator();
    }

    public List<BuyerAppliedCredit> list() {
        return Collections.unmodifiableList(credits);
    }

    public TaxableAmount getTotal() {
        return TaxableAmount.sum(stream().filter(this::notCancelledFilter), BuyerAppliedCredit::getAmount);
    }

    private boolean notCancelledFilter(BuyerAppliedCredit appliedCredit) {
        return appliedCredit.getStatus() != BuyerAppliedCreditStatus.CANCELLED;
    }
}
