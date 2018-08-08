package com.ishoal.core.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class RoleEntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private String roleId;

    public RoleEntityId() {
        super();
    }

    public RoleEntityId(Long userId, String roleId) {
        this();
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleEntityId that = (RoleEntityId) o;

        return new EqualsBuilder()
            .append(userId, that.userId)
            .append(roleId, that.roleId)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(userId)
            .append(roleId)
            .toHashCode();
    }

    public Long getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }
}