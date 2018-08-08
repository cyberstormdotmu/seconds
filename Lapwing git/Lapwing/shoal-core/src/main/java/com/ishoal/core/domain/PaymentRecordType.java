package com.ishoal.core.domain;

public enum PaymentRecordType {
	ORDER_CHECKOUT_PAYMENT("Order Checkout Payment"),
	SUPPLIER_CREDIT_PAYMENT("Supplier Credit Payment");

	private final String displayName;

	PaymentRecordType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
