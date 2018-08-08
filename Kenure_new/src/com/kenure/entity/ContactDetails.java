package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="contact_details")
public class ContactDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="contact_details_id")
	private int contactDetailsId;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="lastname")
	private String lastname;
	
	@Column(name="address1")
	private String address1;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="email1")
	private String email1;
	
	@Column(name="email2")
	private String email2;
	
	@Column(name="email3")
	private String email3;

	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="cell_number1")
	private String cell_number1;
	
	@Column(name="cell_number2")
	private String cell_number2;
	
	@Column(name="cell_number3")
	private String cell_number3;
	
	@Column(name="created_by")
	private int created_by;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_ts")
	private Date updatedTs;
	
	@Column(name="deleted_by")
	private int deletedBy;
	
	@Column(name="deleted_ts")
	private Date deletedTs;
	
	@Column(name="address3")
	private String address3;
	
	@Column(name="streetname")
	private String streetName;
	
	@Column(name="active",columnDefinition = "BIT", length = 1)
	private boolean isActive;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private MaintenanceTechnician maintenancetechnician;
	
	public MaintenanceTechnician getMaintenancetechnician() {
		return maintenancetechnician;
	}

	public void setMaintenancetechnician(MaintenanceTechnician maintenancetechnician) {
		this.maintenancetechnician = maintenancetechnician;
	}

	public int getContactDetailsId() {
		return contactDetailsId;
	}

	public void setContactDetailsId(int contactDetailsId) {
		this.contactDetailsId = contactDetailsId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCell_number1() {
		return cell_number1;
	}

	public void setCell_number1(String cell_number1) {
		this.cell_number1 = cell_number1;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	
	public String getCell_number2() {
		return cell_number2;
	}

	public void setCell_number2(String cell_number2) {
		this.cell_number2 = cell_number2;
	}

	public String getCell_number3() {
		return cell_number3;
	}

	public void setCell_number3(String cell_number3) {
		this.cell_number3 = cell_number3;
	}
	
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}