package com.tatva.domain;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="booking_period_block")
public class BookingPeriodBlock implements Serializable {
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "REF_NO" , nullable = false , unique = true)
	private Integer referenceNo;
	
	@Column(name = "PERIOD_START_DATE")
	private Date periodStartDate;
	
	@Column(name = "PERIOD_END_DATE")
	private Date periodEndDate;
	
	@Column(name = "PERIOD_START_TIME")
	private Time periodStartTime;
	
	@Column(name = "PERIOD_END_TIME")
	private Time periodEndTime;
	
	@Column(name = "PERIOD_STATUS")
	private String periodStatus;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "REASON")
	private String reason;
	
	@Transient
	private List<String> timeList;
	
	private String periodStartDateString;
	
	@Transient
	private String periodStartTimeString;
	
	@Transient
	private String periodEndTimeString;
	
	

	

	public Integer getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(Integer referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getPeriodStartDate() {
		return periodStartDate;
	}

	public void setPeriodStartDate(Date periodStartDate) {
		this.periodStartDate = periodStartDate;
	}

	public Date getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(Date periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public Time getPeriodStartTime() {
		return periodStartTime;
	}

	public void setPeriodStartTime(Time periodStartTime) {
		this.periodStartTime = periodStartTime;
	}

	public Time getPeriodEndTime() {
		return periodEndTime;
	}

	public void setPeriodEndTime(Time periodEndTime) {
		this.periodEndTime = periodEndTime;
	}

	public String getPeriodStatus() {
		return periodStatus;
	}

	public void setPeriodStatus(String periodStatus) {
		this.periodStatus = periodStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public String getPeriodStartDateString() {
		return periodStartDateString;
	}

	public void setPeriodStartDateString(String periodStartDateString) {
		this.periodStartDateString = periodStartDateString;
	}

	public String getPeriodStartTimeString() {
		return periodStartTimeString;
	}

	public void setPeriodStartTimeString(String periodStartTimeString) {
		this.periodStartTimeString = periodStartTimeString;
	}

	public String getPeriodEndTimeString() {
		return periodEndTimeString;
	}

	public void setPeriodEndTimeString(String periodEndTimeString) {
		this.periodEndTimeString = periodEndTimeString;
	}
	
	
	
	
}
