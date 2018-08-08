package com.tatva.model;


import java.sql.Time;
import java.util.Date;

/**
 * 
 * @author pci94
 *	POJO class for generating Reschedule Report
 */
public class RescheduledReport {

	private String appointmentType;
	private String transactionType;
	private String craftNo;
	private Date previousDate;
	private Time previousTime;
	private Date rescheduledDate;
	private Time rescheduledTime;
	private String contactNumber;	
	private String referenceNo;
	private String remark;
	public String getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCraftNo() {
		return craftNo;
	}
	public void setCraftNo(String craftNo) {
		this.craftNo = craftNo;
	}
	public Date getPreviousDate() {
		return previousDate;
	}
	public void setPreviousDate(Date previousDate) {
		this.previousDate = previousDate;
	}
	public Time getPreviousTime() {
		return previousTime;
	}
	public void setPreviousTime(Time previousTime) {
		this.previousTime = previousTime;
	}
	public Date getRescheduledDate() {
		return rescheduledDate;
	}
	public void setRescheduledDate(Date rescheduledDate) {
		this.rescheduledDate = rescheduledDate;
	}
	public Time getRescheduledTime() {
		return rescheduledTime;
	}
	public void setRescheduledTime(Time rescheduledTime) {
		this.rescheduledTime = rescheduledTime;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	
	
}
