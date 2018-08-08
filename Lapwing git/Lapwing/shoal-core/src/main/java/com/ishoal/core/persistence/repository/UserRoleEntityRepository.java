package com.ishoal.core.persistence.repository;

import com.ishoal.core.persistence.entity.RoleEntityId;
import com.ishoal.core.persistence.entity.UserRoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleEntityRepository extends CrudRepository<UserRoleEntity, RoleEntityId> {
}