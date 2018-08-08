package com.ishoal.core.domain;

public enum PaymentStatus {

    UNPAID("Unpaid"),
    PAID("Paid"),
    PART_PAID("Part Paid");

    private final String displayName;

    private PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromDisplayName(String displayName) {
        for(PaymentStatus paymentStatus : values()) {
            if(paymentStatus.displayName.equals(displayName)) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus display name: " + displayName);
    }
}