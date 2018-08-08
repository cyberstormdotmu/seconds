package com.ishoal.core.domain;

public enum BuyerAppliedCreditStatus {
    RESERVED("Reserved"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    V_RESERVED("V_Reserved"),
    V_CONFIRMED("V_Confirmed"),
    V_CANCELLED("V_Cancelled");

    private final String displayName;

    BuyerAppliedCreditStatus(String displayName) {

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
