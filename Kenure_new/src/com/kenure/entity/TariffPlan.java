package com.kenure.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="tariff_plan")
public class TariffPlan {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tariff_plan_id")
	private int tariffPlanId;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;

	@Column(name="tariff_plan_name")
	private String tariffPlanName;

	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="created_ts")
	private Date createdTs;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_ts")
	private Date updatedTs;

	@Column(name="deleted_by")
	private int deletedBy;

	@Column(name="deleted_ts")
	private Date deletedTs;	

	@OneToMany(fetch=FetchType.LAZY,mappedBy="tariffPlan")
	private Set<ConsumerMeter> consumerMeter;

	/*@OneToMany(fetch=FetchType.LAZY,mappedBy="tariffPlan",orphanRemoval=true,cascade=CascadeType.ALL)*/
	@OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "tariffPlan")
	private Set<TariffTransaction> tariffTransaction;

	public int getTariffPlanId() {
		return tariffPlanId;
	}

	public void setTariffPlanId(int tariffPlanId) {
		this.tariffPlanId = tariffPlanId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getTarrifPlanName() {
		return tariffPlanName;
	}

	public void setTarrifName(String tariffPlanName) {
		this.tariffPlanName = tariffPlanName;
	}

	public Set<ConsumerMeter> getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(Set<ConsumerMeter> consumerMeter) {
		this.consumerMeter = consumerMeter;
	}

	public Set<TariffTransaction> getTariffTransaction() {
		return tariffTransaction;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public int getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(int deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTs() {
		return deletedTs;
	}

	public void setDeletedTs(Date deletedTs) {
		this.deletedTs = deletedTs;
	}

	public void setTariffTransaction(Set<TariffTransaction> tariffTransaction) {
		this.tariffTransaction = tariffTransaction;
	}

	/*public void setTariffTransaction(Set<TariffTransaction> tariffTransaction) {
		this.tariffTransaction = tariffTransaction;
	}*/

}
