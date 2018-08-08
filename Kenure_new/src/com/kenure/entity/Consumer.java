package com.kenure.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="consumer")
public class Consumer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="consumer_id")
	private Integer consumerId;
	
	@Column(name="consumer_acc_number")
	private String consumerAccountNumber;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="deleted_by")
	private Integer deletedBy;
	
	@Column(name="created_ts")
	private Date createdTimeStamp;
	
	@Column(name="updated_ts")
	private Date updatedTimeStamp;
	
	@Column(name="deleted_ts")
	private Date deletedTimeStamp;
	
	@Column(name="active",columnDefinition = "BIT", length = 1)
	private boolean isActive;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="tariff_plan_id")
	private TariffPlan tariffPlan;
	
	
	public TariffPlan getTariffPlan() {
		return tariffPlan;
	}

	public void setTariffPlan(TariffPlan tariffPlan) {
		this.tariffPlan = tariffPlan;
	}

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	@Cascade({CascadeType.ALL})
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="consumer")
	private Set<ConsumerMeter> consumerMeter;

	public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}

	public String getConsumerAccountNumber() {
		return consumerAccountNumber;
	}

	public void setConsumerAccountNumber(String consumerAccountNumber) {
		this.consumerAccountNumber = consumerAccountNumber;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public Date getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(Date updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public Date getDeletedTimeStamp() {
		return deletedTimeStamp;
	}

	public void setDeletedTimeStamp(Date deletedTimeStamp) {
		this.deletedTimeStamp = deletedTimeStamp;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean getActive() {
		return isActive;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<ConsumerMeter> getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(Set<ConsumerMeter> consumerMeter) {
		this.consumerMeter = consumerMeter;
	}
	
	
	
	
	
	
}
