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
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="billing_history")
public class BillingHistory {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="billing_history_id")
	private int billingHistoryId;
	
	@Column(name="billing_start_date")
	private Date billingStartDate;
	
	@Column(name="billing_end_date")
	private Date billingEndDate;
	
	@Column(name="current_reading")
	private Integer currentReading;
	
	@Column(name="last_reading")
	private Integer lastReading;
	
	@Column(name="consumed_unit")
	private Integer consumedUnit;
	
	@Column(name="total_amount")
	private Double totalAmount;
	
	@Column(name="bill_date")
	private Date billDate;
	
	@Column(name="is_estimated")
	private Boolean isEstimated;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="consumer_meter_id")
	private ConsumerMeter consumerMeter;

	public int getBillingHistoryId() {
		return billingHistoryId;
	}

	public void setBillingHistoryId(int billingHistoryId) {
		this.billingHistoryId = billingHistoryId;
	}

	public Date getBillingStartDate() {
		return billingStartDate;
	}

	public void setBillingStartDate(Date billingStartDate) {
		this.billingStartDate = billingStartDate;
	}

	public Date getBillingEndDate() {
		return billingEndDate;
	}

	public void setBillingEndDate(Date billingEndDate) {
		this.billingEndDate = billingEndDate;
	}

	public Integer getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(Integer currentReading) {
		this.currentReading = currentReading;
	}

	public Integer getLastReading() {
		return lastReading;
	}

	public void setLastReading(Integer lastReading) {
		this.lastReading = lastReading;
	}

	public Integer getConsumedUnit() {
		return consumedUnit;
	}

	public void setConsumedUnit(Integer consumedUnit) {
		this.consumedUnit = consumedUnit;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Boolean getIsEstimated() {
		return isEstimated;
	}

	public void setIsEstimated(Boolean isEstimated) {
		this.isEstimated = isEstimated;
	}

	public ConsumerMeter getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(ConsumerMeter consumerMeter) {
		this.consumerMeter = consumerMeter;
	}
}
