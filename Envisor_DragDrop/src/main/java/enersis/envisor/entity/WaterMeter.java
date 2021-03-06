package enersis.envisor.entity;

// Generated Apr 28, 2015 10:59:09 AM by Hibernate Tools 4.3.1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * WaterMeter generated by hbm2java
 */
@Entity
@Table(name = "WaterMeter")
public class WaterMeter implements Serializable {

	private int id;
	private Flat flat;
	private MeterType meterType;
	private String serialNo;
	private byte status;
	private Date transactionTime;
	private Set<WaterMeterReading> waterMeterReadings = new HashSet<WaterMeterReading>(
			0);

	public WaterMeter() {
	}

	public WaterMeter(int id, Flat flat, String serialNo, byte status,
			Date transactionTime) {
		this.id = id;
		this.flat = flat;
		this.serialNo = serialNo;
		this.status = status;
		this.transactionTime = transactionTime;
	}

	public WaterMeter(int id, Flat flat, MeterType meterType, String serialNo,
			byte status, Date transactionTime,
			Set<WaterMeterReading> waterMeterReadings) {
		this.id = id;
		this.flat = flat;
		this.meterType = meterType;
		this.serialNo = serialNo;
		this.status = status;
		this.transactionTime = transactionTime;
		this.waterMeterReadings = waterMeterReadings;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flatId", nullable = false)
	public Flat getFlat() {
		return this.flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "meterTypeId")
	public MeterType getMeterType() {
		return this.meterType;
	}

	public void setMeterType(MeterType meterType) {
		this.meterType = meterType;
	}

	@Column(name = "serialNo", nullable = false)
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", nullable = false, length = 23)
	public Date getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "waterMeter")
	public Set<WaterMeterReading> getWaterMeterReadings() {
		return this.waterMeterReadings;
	}

	public void setWaterMeterReadings(Set<WaterMeterReading> waterMeterReadings) {
		this.waterMeterReadings = waterMeterReadings;
	}

}
