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
import javax.persistence.OneToOne;
import javax.persistence.Table;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="installer")
public class Installer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="installer_id")
	private int installerId;;

	@Column(name="installer_name",nullable=false)
	private String installerName;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="created_ts")
	private Date createdTS;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_ts")
	private Date updatedTS;
	
	@Column(name="deleted_by")
	private Integer deletedBy;
	
	@Column(name="deleted_ts")
	private Date deletedTS;
	
	@Column(name="active")
	private boolean isActive;

	@OneToMany(fetch=FetchType.LAZY,mappedBy="installer")
	private Set<ConsumerMeter> consumerMeter;

	@OneToMany(fetch=FetchType.LAZY,mappedBy="installer")
	private Set<DataCollector> dataCollector;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="installer")
	private Set<SiteInstallationFiles> siteInstallationFiles;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<SiteInstallationFiles> getSiteInstallationFiles() {
		return siteInstallationFiles;
	}

	public void setSiteInstallationFiles(
			Set<SiteInstallationFiles> siteInstallationFiles) {
		this.siteInstallationFiles = siteInstallationFiles;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}

	public Date getUpdatedTS() {
		return updatedTS;
	}

	public void setUpdatedTS(Date updatedTS) {
		this.updatedTS = updatedTS;
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

	public Date getDeletedTS() {
		return deletedTS;
	}

	public void setDeletedTS(Date deletedTS) {
		this.deletedTS = deletedTS;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public int getInstallerId() {
		return installerId;
	}

	public void setInstallerId(int installerId) {
		this.installerId = installerId;
	}

	public Set<ConsumerMeter> getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(Set<ConsumerMeter> consumerMeter) {
		this.consumerMeter = consumerMeter;
	}

	public Set<DataCollector> getDataCollector() {
		return dataCollector;
	}

	public void setDataCollector(Set<DataCollector> dataCollector) {
		this.dataCollector = dataCollector;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getInstallerName() {
		return installerName;
	}

	public void setInstallerName(String installerName) {
		this.installerName = installerName;
	}
	
}
