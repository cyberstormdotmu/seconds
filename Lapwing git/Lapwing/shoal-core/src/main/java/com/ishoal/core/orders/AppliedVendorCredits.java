package com.ishoal.core.orders;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ishoal.common.util.Streamable;

public class AppliedVendorCredits implements Streamable<AppliedVendorCredit> {

	private final List<AppliedVendorCredit> vendorCredits;

	private AppliedVendorCredits(List<AppliedVendorCredit> vendorCredits) {
		this.vendorCredits = Collections.unmodifiableList(vendorCredits);
	}

	public static AppliedVendorCredits over(List<AppliedVendorCredit> vendorCredits) {
		return new AppliedVendorCredits(vendorCredits);
	}

	@Override
	public Iterator<AppliedVendorCredit> iterator() {
		return this.vendorCredits.iterator();
	}

	public int size() {
		return this.vendorCredits.size();
	}
}