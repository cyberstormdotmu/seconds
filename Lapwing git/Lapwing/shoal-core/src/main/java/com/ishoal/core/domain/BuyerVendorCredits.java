package com.ishoal.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ishoal.common.util.Streamable;

public class BuyerVendorCredits implements Streamable<BuyerVendorCredit> {
	
	private final List<BuyerVendorCredit> buyerVendorCredits;
	
	private BuyerVendorCredits(Builder builder) {
		this.buyerVendorCredits = new ArrayList<>(builder.buyerVendorCredits); 
	}
	
	private BuyerVendorCredits(List<BuyerVendorCredit> buyerVendorCredits) {
		this.buyerVendorCredits = Collections.unmodifiableList(buyerVendorCredits);
	}
	
	public static Builder somebuyerVendorCredits() {
		return new Builder();
	}

	public static BuyerVendorCredits emptybuyerVendorCredits() {
		return new Builder().build();
	}

	public int size() {
		return buyerVendorCredits.size();
	}
	
	public static BuyerVendorCredits over(List<BuyerVendorCredit> add) {
		return new BuyerVendorCredits(add);
	}
	
	public Iterator<BuyerVendorCredit> iterator() {
		return buyerVendorCredits.iterator();
	}

	public BuyerVendorCredit primary() {
		return buyerVendorCredits.get(0);
	}

	public boolean isEmpty() {

		return buyerVendorCredits.isEmpty();
	}
	
	public static class Builder {
		private List<BuyerVendorCredit> buyerVendorCredits = new ArrayList<>();

		private Builder() {
		}

		public Builder buyerVendorCredit(BuyerVendorCredit add) {
			this.buyerVendorCredits.add(add);
			return this;
		}

		public BuyerVendorCredits build() {
			//addresses.sort((Address a1, Address a2) -> a1.getOrganisationName().compareTo(a2.getOrganisationName()));
			return new BuyerVendorCredits(this);
		}
	}
	
}
