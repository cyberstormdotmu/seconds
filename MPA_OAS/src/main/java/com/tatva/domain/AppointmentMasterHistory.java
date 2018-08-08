package com.tatva.domain;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_master_hist")
public class AppointmentMasterHistory {
	// ------------------------------------------------------------------------------------------------------------------

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "HIST_ID" )
	private int historyId;

	@Column(name = "REF_NO")
	private String referenceNo;

	@Column(name= "ID_NO")
	private String idNo;

	@Column(name= "ID_TYPE")
	private String idType;


	@Column(name= "APT_TYPE")
	private String appointmentType;

	@Column(name= "APT_DATE")
	private Date appointmentDate;

	@Column(name= "APT_TIME")
	private Time appointmentTime;
	
	@Column(name= "PRE_APT_DATE")
	private Date previousAppointmentDate;

	@Column(name= "PRE_APT_TIME")
	private Time previousAppointmentTime;

	@Column(name= "NAME")
	private String 	name;

	@Column(name= "CONTACT_NO")
	private String contactNo;

	@Column(name= "EMAIL")
	private String 	email;

	@Column(name= "COMPANY")
	private String 	company;

	@Column(name= "REMARK")
	private String 	remark;

	@Column(name= "APT_STATUS")
	private String 	appointmentStatus;

	@Column(name= "CHECK_IN_DATE")
	private Date 	checkInDate;

	@Column(name= "CHECK_IN_TIME")
	private Time 	checkInTime;

	@Column(name= "PROCESS_DATE")
	private Date 	processDate;

	@Column(name= "PROCESS_TIME")
	private Time 	processTime;

	@Column(name= "COMPLETE_DATE")
	private Date 	completeDate;

	@Column(name= "COMPLETE_TIME")
	private Time 	completeTime;

	@Column(name= "QUEUE_NO")
	private String  queueNo;

	@Column(name= "UPDATE_USERID")
	private String 	updateUserId;

	@Column(name= "LAST_ACTION")
	private String 	lastAction;

	@Column(name= "TIME_STAMP")
	private Date timestamp;
	
	
	public String getReferenceNo() {
		return referenceNo;
	}
	
	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public Time getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(Time appointmentTime) {
		this.appointmentTime = appointmentTime;
	}	
	public Date getPreviousAppointmentDate() {
		return previousAppointmentDate;
	}
	public void setPreviousAppointmentDate(Date previousAppointmentDate) {
		this.previousAppointmentDate = previousAppointmentDate;
	}
	public Time getPreviousAppointmentTime() {
		return previousAppointmentTime;
	}
	public void setPreviousAppointmentTime(Time previousAppointmentTime) {
		this.previousAppointmentTime = previousAppointmentTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public Time getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(Time checkInTime) {
		this.checkInTime = checkInTime;
	}
	public Date getProcessDate() {
		return processDate;
	}
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
	public Time getProcessTime() {
		return processTime;
	}
	public void setProcessTime(Time processTime) {
		this.processTime = processTime;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public Time getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Time completeTime) {
		this.completeTime = completeTime;
	}
	public String getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getLastAction() {
		return lastAction;
	}
	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}