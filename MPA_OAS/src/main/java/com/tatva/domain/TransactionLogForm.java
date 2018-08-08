package com.tatva.domain;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction_log")
public class TransactionLogForm {

	public TransactionLogForm(){
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "LOG_ID" , nullable = false , unique = true)
	private long logId;
	
	@Column(name= "REF_NO")
	private String referenceNo;
	
	@Column(name= "LOG_TYPE")
	private String logType;
	
	@Column(name= "STAFF_ID")
	private String staffId;
	
	@Column(name= "LOG_DATE")
	private Date logDate;
	
	@Column(name= "LOG_TIME")
	private Time logTime;
	
	public long getLogId() {
		return logId;
	}
	
	public void setLogId(long logId) {
		this.logId = logId;
	}
	
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public Time getLogTime() {
		return logTime;
	}
	public void setLogTime(Time logTime) {
		this.logTime = logTime;
	}
	
}
