package com.kenure.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="normal_customer")
public class NormalCustomer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="normal_customer_id")
	private int normalCustomerId;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="customer_id")
	private Customer superCustomer;
	
	@OneToOne(mappedBy="normalCustomer",orphanRemoval=true,cascade=CascadeType.ALL)
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getNormalCustomerId() {
		return normalCustomerId;
	}

	public void setNormalCustomerId(int normalCustomerId) {
		this.normalCustomerId = normalCustomerId;
	}

	public Customer getSuperCustomer() {
		return superCustomer;
	}

	public void setSuperCustomer(Customer superCustomer) {
		this.superCustomer = superCustomer;
	}

}