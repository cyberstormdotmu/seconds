package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="maintenance_technician")
public class MaintenanceTechnician {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="maintenance_technician_id")
	private int maintenanceTechnicianId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by",nullable = true)
	private Integer updatedBy;
	
	@Column(name="updated_ts",nullable = true)
	private Date updatedTs;
	
	@Column(name="deleted_by",nullable = true)
	private Integer deletedBy;
	
	@Column(name="deleted_ts",nullable = true)
	private Date deletedTs;
	
	@Column(name="active",nullable = true,columnDefinition = "BIT", length = 1)
	private boolean activeStatus = true;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToOne
	/*@JoinColumn(name="region_id")*/
	@PrimaryKeyJoinColumn
	@Cascade({CascadeType.ALL})
	private Region region;
	
	@OneToOne
	@JoinColumn(name="contact_detail_id")
	@Cascade({CascadeType.ALL})
	private ContactDetails contactdetails;

	public int getMaintenanceTechnicianId() {
		return maintenanceTechnicianId;
	}

	public void setMaintenanceTechnicianId(int maintenanceTechnicianId) {
		this.maintenanceTechnicianId = maintenanceTechnicianId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public ContactDetails getContactdetails() {
		return contactdetails;
	}

	public void setContactdetails(ContactDetails contactdetails) {
		this.contactdetails = contactdetails;
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
