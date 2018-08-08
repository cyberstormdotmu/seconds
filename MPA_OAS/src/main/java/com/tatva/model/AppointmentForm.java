package com.tatva.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pci94
 *	POJO class for getters and setters of all the field in Appointment Form
 */
public class AppointmentForm {

	private String nricPpassportNumber;
	private String referenceNo;
	private String name;
	private String company;
	private String appointmentType;
	
	private List<String> transactionType = new ArrayList<>();
	private List<String> craftNumbers = new ArrayList<>();
	private List<String> harbourCraftCheckBox = new ArrayList<>();   //sub category checkboxes of harbour craft
	private List<String> pleasureCraftCheckBox = new ArrayList<>();
	private List<String> portClearanceCheckBox = new ArrayList<>();
	private List<String> othersCheckBox = new ArrayList<>();
	
	private String HCLNLSelect;			// value of individual dropdownbox
	private String HCLADSelect;
	private String HCLUCSelect;
	private String HCLCOSelect;
	private String HCLNMSelect;
	private String HCLRHSelect;
	private String PCLNLSelect;
	private String PCLADSelect;
	private String PCLUCSelect;
	private String PCLNPSelect;
	private String PCGDSelect;
	private String PCALSelect;
	private String PCABSelect;
	private String OTHSSelect;
	
	private String date;
	private String time;
	private String remark;
	private String contactNumber;
	private String emailAddress;
	
	public String getNricPpassportNumber() {
		return nricPpassportNumber;
	}
	public void setNricPpassportNumber(String nricPpassportNumber) {
		this.nricPpassportNumber = nricPpassportNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}
	public List<String> getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(List<String> transactionType) {
		this.transactionType = transactionType;
	}
	public List<String> getCraftNumbers() {
		return craftNumbers;
	}
	public void setCraftNumbers(List<String> craftNumbers) {
		this.craftNumbers = craftNumbers;
	}
	public List<String> getHarbourCraftCheckBox() {
		return harbourCraftCheckBox;
	}
	public void setHarbourCraftCheckBox(List<String> harbourCraftCheckBox) {
		this.harbourCraftCheckBox = harbourCraftCheckBox;
	}
	public List<String> getPleasureCraftCheckBox() {
		return pleasureCraftCheckBox;
	}
	public void setPleasureCraftCheckBox(List<String> pleasureCraftCheckBox) {
		this.pleasureCraftCheckBox = pleasureCraftCheckBox;
	}
	public List<String> getPortClearanceCheckBox() {
		return portClearanceCheckBox;
	}
	public void setPortClearanceCheckBox(List<String> portClearanceCheckBox) {
		this.portClearanceCheckBox = portClearanceCheckBox;
	}
	public List<String> getOthersCheckBox() {
		return othersCheckBox;
	}
	public void setOthersCheckBox(List<String> othersCheckBox) {
		this.othersCheckBox = othersCheckBox;
	}
	public String getHCLNLSelect() {
		return HCLNLSelect;
	}
	public void setHCLNLSelect(String hCLNLSelect) {
		HCLNLSelect = hCLNLSelect;
	}
	public String getHCLADSelect() {
		return HCLADSelect;
	}
	public void setHCLADSelect(String hCLADSelect) {
		HCLADSelect = hCLADSelect;
	}
	public String getHCLUCSelect() {
		return HCLUCSelect;
	}
	public void setHCLUCSelect(String hCLUCSelect) {
		HCLUCSelect = hCLUCSelect;
	}
	public String getHCLCOSelect() {
		return HCLCOSelect;
	}
	public void setHCLCOSelect(String hCLCOSelect) {
		HCLCOSelect = hCLCOSelect;
	}
	public String getHCLNMSelect() {
		return HCLNMSelect;
	}
	public void setHCLNMSelect(String hCLNMSelect) {
		HCLNMSelect = hCLNMSelect;
	}
	public String getHCLRHSelect() {
		return HCLRHSelect;
	}
	public void setHCLRHSelect(String hCLRHSelect) {
		HCLRHSelect = hCLRHSelect;
	}
	public String getPCLNLSelect() {
		return PCLNLSelect;
	}
	public void setPCLNLSelect(String pCLNLSelect) {
		PCLNLSelect = pCLNLSelect;
	}
	public String getPCLADSelect() {
		return PCLADSelect;
	}
	public void setPCLADSelect(String pCLADSelect) {
		PCLADSelect = pCLADSelect;
	}
	public String getPCLUCSelect() {
		return PCLUCSelect;
	}
	public void setPCLUCSelect(String pCLUCSelect) {
		PCLUCSelect = pCLUCSelect;
	}
	public String getPCLNPSelect() {
		return PCLNPSelect;
	}
	public void setPCLNPSelect(String pCLNPSelect) {
		PCLNPSelect = pCLNPSelect;
	}
	public String getPCGDSelect() {
		return PCGDSelect;
	}
	public void setPCGDSelect(String pCGDSelect) {
		PCGDSelect = pCGDSelect;
	}
	public String getPCALSelect() {
		return PCALSelect;
	}
	public void setPCALSelect(String pCALSelect) {
		PCALSelect = pCALSelect;
	}
	public String getPCABSelect() {
		return PCABSelect;
	}
	public void setPCABSelect(String pCABSelect) {
		PCABSelect = pCABSelect;
	}
	public String getOTHSSelect() {
		return OTHSSelect;
	}
	public void setOTHSSelect(String oTHSSelect) {
		OTHSSelect = oTHSSelect;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
}
