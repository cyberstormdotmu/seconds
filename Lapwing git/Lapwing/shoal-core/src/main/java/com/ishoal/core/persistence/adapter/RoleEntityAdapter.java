package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Role;
import com.ishoal.core.persistence.entity.UserRoleEntity;

public class RoleEntityAdapter {
    public Role adapt(UserRoleEntity entity) {
        if(entity == null) {
            return null;
        }
        return entity.getRole();
    }

    public UserRoleEntity adapt(Role role) {
        if (role == null) {
            return null;
        }
        UserRoleEntity entity = new UserRoleEntity();
        entity.setRole(role);
        return entity;
    }
}
