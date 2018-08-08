package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Roles implements Streamable<Role> {

    private final List<Role> roles;

    private Roles(Builder builder) {
        this.roles = new ArrayList<>(builder.roles);
    }

    public static Builder someRoles() {
        return new Builder();
    }

    @Override
    public Iterator<Role> iterator() {
        return roles.iterator();
    }

    public void add(Role role) {
        roles.add(role);
    }

    public void clear() {
        this.roles.clear();
    }

    public static class Builder {
        private List<Role> roles = new ArrayList<>();

        private Builder() {

        }

        public Builder role(Role role) {
            this.roles.add(role);
            return this;
        }

        public Roles build() {
            return new Roles(this);
        }
    }
}
