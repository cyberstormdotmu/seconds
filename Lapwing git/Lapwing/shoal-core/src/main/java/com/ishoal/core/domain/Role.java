package com.ishoal.core.domain;

public enum Role {

    BUYER("BUYER"),
    VENDOR("VENDOR"),
    SUPPLIER("SUPPLIER"),
    ADMIN("ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
