package com.ishoal.core.domain;

public enum CreditType {
    PRICE_MOVEMENT("Price Movement");

    private final String displayName;

    private CreditType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CreditType fromDisplayName(String displayName) {
        for(CreditType creditType : values()) {
            if(creditType.displayName.equals(displayName)) {
                return creditType;
            }
        }
        throw new IllegalArgumentException("Invalid CreditType display name: " + displayName);
    }
}
