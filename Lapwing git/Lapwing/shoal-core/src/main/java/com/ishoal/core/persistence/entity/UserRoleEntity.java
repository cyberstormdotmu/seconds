package com.ishoal.core.persistence.entity;

import com.ishoal.core.domain.Role;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Roles")
@Table(name = "USER_ROLES",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"})
    })
@IdClass(RoleEntityId.class)
public class UserRoleEntity {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private UserEntity userId;

    @Id
    @Column(name = "ROLE_ID")
    private String roleId;

    public UserEntity getUser() {

        return userId;
    }

    public Role getRole() {
        return Role.valueOf(this.roleId);
    }

    public void setRole(Role role) {
        this.roleId = role.getName();
    }

    public void setUser(UserEntity user) {
        this.userId = user;
    }

    public String getName() {
        return roleId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserRoleEntity that = (UserRoleEntity) o;

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

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE);
    }


}
