package com.ishoal.core.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ishoal.common.util.Streamable;

public class BuyerWithdrawCredits implements Streamable<BuyerWithdrawCredit> {

	private final List<BuyerWithdrawCredit> credits;
	
	private BuyerWithdrawCredits(Builder builder) {
		this.credits = new ArrayList<>(builder.credits);
	}
	
	public static BuyerWithdrawCredits emptyBuyerWithdrawCredits() {
		return new Builder().build();
	}
	
	public static Builder someBuyerWithdrawCredits() {
		return new Builder();
	}
	
	@Override
    public Iterator<BuyerWithdrawCredit> iterator() {
        return this.credits.iterator();
    }
	
	public static class Builder {
		private List<BuyerWithdrawCredit> credits = new ArrayList<>();

		private Builder() {
		}

		public Builder buyerWithdrawCredits(BuyerWithdrawCredit credit) {
			this.credits.add(credit);
			return this;
		}

		public BuyerWithdrawCredits build() {
			return new BuyerWithdrawCredits(this);
		}
	}
}
