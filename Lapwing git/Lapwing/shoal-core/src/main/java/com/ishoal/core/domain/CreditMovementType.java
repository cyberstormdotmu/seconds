package com.ishoal.core.domain;

public enum CreditMovementType {
    EARN("Earn"),
    SPEND("Spend"),
    REDEEM("Redeem"),
    V_SPEND("V_Spend");
	

    private final String displayName;

    CreditMovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
