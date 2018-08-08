package com.kenure.entity;

import java.io.Serializable;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="region")
@BatchSize(size = 4)
public class Region implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="region_id")
	private int regionId;

	@Column(name="region_name")
	private String regionName;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="country_id")
	private Country country;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="currency_id")
	private Currency currency;

	@Column(name="time_zone")
	private String timeZone;
	
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

	@OneToMany(fetch=FetchType.LAZY,mappedBy="region")
	private Set<Site> site;

	@OneToOne
	@PrimaryKeyJoinColumn
	private MaintenanceTechnician maintenancetechnician; 

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Site> getSite() {
		return site;
	}

	public void setSite(Set<Site> site) {
		this.site = site;
	}

	public MaintenanceTechnician getMaintenancetechnician() {
		return maintenancetechnician;
	}

	public void setMaintenancetechnician(MaintenanceTechnician maintenancetechnician) {
		this.maintenancetechnician = maintenancetechnician;
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
	
}
