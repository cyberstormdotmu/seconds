package com.kenure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="consumer_meter_transaction")
public class ConsumerMeterTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "consumer_meter_transaction_id", unique = true, nullable = false)
	int id;
	
	@Column(name="current_status")
	Integer status;
	
	@Column(name="current_meter_reading")
	Long meterReading;
	
	@Column(name="current_battery_voltage")
	Integer batteryVoltage;
	
	@Column(name="current_reading_timestamp")
	Date timeStamp;
	
	@Column(name="alerts")
	String alerts;

	@Column(name="register_id")
	private String registerId;
	
	@Column(name="alerts_ack")
	boolean alerts_ack;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getMeterReading() {
		return meterReading;
	}

	public void setMeterReading(Long meterReading) {
		this.meterReading = meterReading;
	}

	public Integer getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Integer batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAlerts() {
		return alerts;
	}

	public void setAlerts(String alerts) {
		this.alerts = alerts;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public boolean isAlerts_ack() {
		return alerts_ack;
	}

	public void setAlerts_ack(boolean alerts_ack) {
		this.alerts_ack = alerts_ack;
	}
}
