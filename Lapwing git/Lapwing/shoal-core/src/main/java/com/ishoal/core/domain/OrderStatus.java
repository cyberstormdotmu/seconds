package com.ishoal.core.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderStatus {

    PROCESSING("Processing"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled");

    private static final Map<OrderStatus, List<OrderStatus>> VALID_TRANSITIONS = new HashMap<>();

    private final String displayName;

    static {
        VALID_TRANSITIONS.put(PROCESSING, Arrays.asList(CONFIRMED, CANCELLED));
        VALID_TRANSITIONS.put(CONFIRMED, Collections.emptyList());
        VALID_TRANSITIONS.put(CANCELLED, Collections.emptyList());
    }

    private OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public boolean canTransitionTo(OrderStatus targetStatus) {
        return VALID_TRANSITIONS.get(this).contains(targetStatus);
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromDisplayName(String displayName) {
        for(OrderStatus orderStatus : values()) {
            if(orderStatus.displayName.equals(displayName)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus display name: " + displayName);
    }
}