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
import javax.persistence.Table;

@Entity
@Table(name="district_meter_transaction")
public class DistrictMeterTransaction {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="district_meter_transaction_id")
	private Integer districtMeterTransactionId;
	
	@Column(name="current_reading")
	private Integer currentReading;
	
	@Column(name="start_billing_date")
	private Date startBillingDate;

	@Column(name="end_billing_date")
	private Date endBillingDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="district_meter_id")
	private DistrictUtilityMeter districtUtilityMeter;

	public Integer getDistrictMeterTransactionId() {
		return districtMeterTransactionId;
	}

	public void setDistrictMeterTransactionId(Integer districtMeterTransactionId) {
		this.districtMeterTransactionId = districtMeterTransactionId;
	}

	public Integer getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(Integer currentReading) {
		this.currentReading = currentReading;
	}

	public Date getStartBillingDate() {
		return startBillingDate;
	}

	public void setStartBillingDate(Date startBillingDate) {
		this.startBillingDate = startBillingDate;
	}

	public Date getEndBillingDate() {
		return endBillingDate;
	}

	public void setEndBillingDate(Date endBillingDate) {
		this.endBillingDate = endBillingDate;
	}

	public DistrictUtilityMeter getDistrictUtilityMeter() {
		return districtUtilityMeter;
	}

	public void setDistrictUtilityMeter(DistrictUtilityMeter districtUtilityMeter) {
		this.districtUtilityMeter = districtUtilityMeter;
	}
	
}
