package com.tatvasoft.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *	@author TatvaSoft
 *	This is entity (Model) class for Role.
 */
@Entity
@Table(name = "role", catalog = "orgnizational_forum")
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique = true, nullable = false)
	private long roleid;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<UserEntity> UserRole = new HashSet<UserEntity>(0);

	//Default constructor
	public RoleEntity() {}
	
	// Getter and Setter methods 	
	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserEntity> getUserRole() {
		return UserRole;
	}

	public void setUserRole(Set<UserEntity> userRole) {
		UserRole = userRole;
	}
	
}
