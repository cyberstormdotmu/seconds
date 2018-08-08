package com.tatva.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;



@Entity
@Table(name="tk_user")
public class User {

	
	@Id
	@GeneratedValue
	@Column(name="userId")
	private int userId;
	
	@NotEmpty(message="user name is required")
	@Column(name="userName")
	private String userName;
	
	/*@NotEmpty(message="password is required")
	@Size(max=10,min=5)*/
	@Column(name="password")
	private String password;
	
	@NotEmpty(message="first name is required")
	@Column(name="firstName")
	private String firstName;
	
	@NotEmpty(message="last name is required")
	@Column(name="lastName")
	private String lastName;
	
	/*@NotEmpty(message="select any one")*/
	@Column(name="isActive")
	private String active;
	
	/*@NotEmpty(message="role is required")
	@Size(min=1,max=1)*/
	@Column(name="roleId")
	private int roleId;
	/*
	@OneToMany(fetch = FetchType.LAZY,mappedBy="user")
	private List<Vehicle> vehicles;
	
	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}*/

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
}
