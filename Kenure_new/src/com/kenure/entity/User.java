package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * 
 * @author TatvaSoft
 *
 */
@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private int userId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="role_id")
	private Role role;

	@Column(name="username",nullable=false)
	private String userName;

	@Column(name="password",nullable=false)
	private String password;

	@Column(name="first_time_login",nullable = false, columnDefinition = "BIT", length = 1)
	private boolean  isFirstTimeLogin = true;

	@Column(name="active",nullable = true,columnDefinition = "BIT", length = 1)
	private boolean activeStatus = true;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_ts")
	private Date createdTs;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_ts")
	private Date updatedTs;

	@Column(name="deleted_by")
	private Integer deletedBy;

	@Column(name="deleted_ts")
	private Date deletedTs;		

	/*@OneToOne(fetch=FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Customer customer;

	@OneToOne(fetch=FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private ConsumerMeter consumer;*/

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contact_details_id")
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	private ContactDetails details;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="normal_customer_id")
	private NormalCustomer normalCustomer;

	public ContactDetails getDetails() {
		return details;
	}

	public void setDetails(ContactDetails details) {
		this.details = details;
	}

	/*public Customer customer() {
		return customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}*/

	public boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public boolean isFirstTimeLogin() {
		return isFirstTimeLogin;
	}

	public void setFirstTimeLogin(boolean isFirstTimeLogin) {
		this.isFirstTimeLogin = isFirstTimeLogin;
	}

	/*public ConsumerMeter getConsumer() {
		return consumer;
	}

	public void setConsumer(ConsumerMeter consumer) {
		this.consumer = consumer;
	}*/

	public NormalCustomer getNormalCustomer() {
		return normalCustomer;
	}

	public void setNormalCustomer(NormalCustomer normalCustomer) {
		this.normalCustomer = normalCustomer;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTs() {
		return deletedTs;
	}

	public void setDeletedTs(Date deletedTs) {
		this.deletedTs = deletedTs;
	}

}