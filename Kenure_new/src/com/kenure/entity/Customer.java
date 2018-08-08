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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name="customer")
public class Customer {

	@Id
	@Column(name="customer_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int customerId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	private User user;

	@Column(name="customer_code")
	private String customerCode;

	@Column(name="customername")
	private String customerName;

	@Column(name="dataplan_activated_date")
	private Date dataPlanActivatedDate;

	@Column(name="dataplan_expiry_date")
	private Date dataPlanExpiryDate;

	@Column(name="time_zone")
	private String timeZone;

	@Column(name="active",nullable = true,columnDefinition = "BIT", length = 1)
	private boolean activeStatus = true;

	@Column(name="contract_start_date")
	private Date portalPlanStartDate;
	
	@Column(name="contract_end_date")
	private Date portalPlanExpiryDate;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="status")
	private String status;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_ts")
	private Date updatedTS;
	
	@Column(name="deleted_by")
	private Integer deletedBy;
	
	@Column(name="deleted_ts")
	private Date deletedTS;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	private Set<Consumer> consumer;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<DistrictUtilityMeter> districtUtilityMeter;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<Installer> installer;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<Region> region;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<MaintenanceTechnician> maintenancetechnician;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<DataCollector> dataCollector;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="customer")
	@BatchSize(size = 4)
	private Set<TariffPlan> tariffPlan;
	
	@OneToOne
	@JoinColumn(name = "data_plan_id")
	private DataPlan dataPlan;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="currency_id")
	private Currency currency;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="country_id")
	private Country country;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="superCustomer")
	private Set<NormalCustomer> normalCustomer;
	
/*	public Set<ConsumerMeter> getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(Set<ConsumerMeter> consumerMeter) {
		this.consumerMeter = consumerMeter;
	}*/

	public Set<DistrictUtilityMeter> getDistrictUtilityMeter() {
		return districtUtilityMeter;
	}

	public void setDistrictUtilityMeter(
			Set<DistrictUtilityMeter> districtUtilityMeter) {
		this.districtUtilityMeter = districtUtilityMeter;
	}

	public Set<Installer> getInstaller() {
		return installer;
	}

	public void setInstaller(Set<Installer> installer) {
		this.installer = installer;
	}

	public Set<Region> getRegion() {
		return region;
	}

	public void setRegion(Set<Region> region) {
		this.region = region;
	}

	public Set<MaintenanceTechnician> getMaintenancetechnician() {
		return maintenancetechnician;
	}

	public void setMaintenancetechnician(
			Set<MaintenanceTechnician> maintenancetechnician) {
		this.maintenancetechnician = maintenancetechnician;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getDataPlanActivatedDate() {
		return dataPlanActivatedDate;
	}

	public void setDataPlanActivatedDate(Date dataPlanActivatedDate) {
		this.dataPlanActivatedDate = dataPlanActivatedDate;
	}

	public Date getDataPlanExpiryDate() {
		return dataPlanExpiryDate;
	}

	public void setDataPlanExpiryDate(Date dataPlanExpiryDate) {
		this.dataPlanExpiryDate = dataPlanExpiryDate;
	}
	
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public DataPlan getDataPlan() {
		return dataPlan;
	}

	public void setDataPlan(DataPlan dataPlan) {
		this.dataPlan = dataPlan;
	}
	public Set<DataCollector> getDataCollector() {
		return dataCollector;
	}

	public void setDataCollector(Set<DataCollector> dataCollector) {
		this.dataCollector = dataCollector;
	}
	
	public Date getPortalPlanStartDate() {
		return portalPlanStartDate;
	}

	public void setPortalPlanStartDate(Date contractStartDate) {
		this.portalPlanStartDate = contractStartDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPortalPlanExpiryDate() {
		return portalPlanExpiryDate;
	}

	public void setPortalPlanExpiryDate(Date portalPlanExpiryDate) {
		this.portalPlanExpiryDate = portalPlanExpiryDate;
	}
	
	public Set<Consumer> getConsumer() {
		return consumer;
	}

	public void setConsumer(Set<Consumer> consumer) {
		this.consumer = consumer;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTS() {
		return updatedTS;
	}

	public void setUpdatedTS(Date updatedTS) {
		this.updatedTS = updatedTS;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Set<TariffPlan> getTariffPlan() {
		return tariffPlan;
	}

	public void setTariffPlan(Set<TariffPlan> tariffPlan) {
		this.tariffPlan = tariffPlan;
	}

	public Set<NormalCustomer> getNormalCustomer() {
		return normalCustomer;
	}

	public void setNormalCustomer(Set<NormalCustomer> normalCustomer) {
		this.normalCustomer = normalCustomer;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTS() {
		return deletedTS;
	}

	public void setDeletedTS(Date deletedTS) {
		this.deletedTS = deletedTS;
	}

}