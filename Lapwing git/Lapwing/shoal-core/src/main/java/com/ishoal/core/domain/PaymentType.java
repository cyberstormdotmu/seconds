package com.ishoal.core.domain;

public enum PaymentType {
    SHOAL_CREDIT("Shoal Credit"),
    BANK_TRANSFER("Bank Transfer"),
    CARD_PAYMENT("Card Payment"),
    DIRECT_DEBIT("Direct Debit"),
    OTHER("Other");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentType fromDisplayName(String displayName) {
        for(PaymentType paymentType : values()) {
            if(paymentType.displayName.equals(displayName)) {
                return paymentType;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentType display name: " + displayName);
    }
}
