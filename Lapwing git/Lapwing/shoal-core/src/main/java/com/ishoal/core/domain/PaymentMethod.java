package com.ishoal.core.domain;

public enum PaymentMethod {
    CARD_PAYMENT("Card Payment"),
    INVOICE("On Invoice");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMethod fromDisplayName(String displayName) {
        for(PaymentMethod paymentType : values()) {
            if(paymentType.displayName.equals(displayName)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod display name: " + displayName);
    }
}
