package com.ishoal.ws.common.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LoginResponseDto {

    private final String username;
    private final boolean activated;
    private final Set<String> roles;

    private LoginResponseDto(Builder builder) {
        this.username = builder.username;
        this.activated = builder.activated;
        this.roles = new HashSet<>(builder.roles);
    }

    public static Builder aLoginResponse() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public boolean isActivated() {
        return activated;
    }

    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public static class Builder {
        private String username;
        private boolean activated;
        private Set<String> roles = new HashSet<>();

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder activated(boolean activated) {
            this.activated = activated;
            return this;
        }

        public Builder role(String role) {
            this.roles.add(role);
            return this;
        }

        public LoginResponseDto build() {
            return new LoginResponseDto(this);
        }
    }

}
