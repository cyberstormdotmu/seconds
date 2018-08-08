package com.ishoal.core.domain;

public enum PaymentGateway {
	STRIPE("Stripe");

    private final String name;

    PaymentGateway(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
