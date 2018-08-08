package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Roles;
import com.ishoal.core.persistence.entity.UserRoleEntity;

import java.util.ArrayList;
import java.util.List;

import static com.ishoal.core.domain.Roles.someRoles;

public class UserRoleEntityAdapter {
    private final RoleEntityAdapter roleEntityAdapter = new RoleEntityAdapter();

    public Roles adapt(Iterable<UserRoleEntity> entities) {
        Roles.Builder builder = someRoles();
        entities.forEach(entity -> builder.role(roleEntityAdapter.adapt(entity)));
        return builder.build();
    }

    public List<UserRoleEntity> adapt(Roles roles) {

        List<UserRoleEntity> roleEntityList = new ArrayList<>();
        roles.forEach(role -> roleEntityList.add(roleEntityAdapter.adapt(role)));
        return roleEntityList;
    }
}
