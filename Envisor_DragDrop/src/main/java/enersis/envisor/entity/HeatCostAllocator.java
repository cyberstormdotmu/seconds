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
 * HeatCostAllocator generated by hbm2java
 */
@Entity
@Table(name = "HeatCostAllocator")
public class HeatCostAllocator implements Serializable {

	private int id;
	private Room room;
	private String serialNo;
	private String radiatorBrand;
	private String radiatorType;
	private double kges;
	private Integer measurement;
	private Byte status;
	private Date transactionTime;
	private Set<HeatCostAllocatorReading> heatCostAllocatorReadings = new HashSet<HeatCostAllocatorReading>(
			0);

	public HeatCostAllocator() {
	}

	public HeatCostAllocator(int id, Room room, String serialNo,
			double kges) {
		this.id = id;
		this.room = room;
		this.serialNo = serialNo;
		this.kges = kges;
	}

	public HeatCostAllocator(int id, Room room, String serialNo,
			String radiatorBrand, String radiatorType, double kges,
			Integer measurement, Byte status, Date transactionTime,
			Set<HeatCostAllocatorReading> heatCostAllocatorReadings) {
		this.id = id;
		this.room = room;
		this.serialNo = serialNo;
		this.radiatorBrand = radiatorBrand;
		this.radiatorType = radiatorType;
		this.kges = kges;
		this.measurement = measurement;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
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
	@JoinColumn(name = "roomId", nullable = false)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Column(name = "serialNo", nullable = false)
	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@Column(name = "radiatorBrand")
	public String getRadiatorBrand() {
		return this.radiatorBrand;
	}

	public void setRadiatorBrand(String radiatorBrand) {
		this.radiatorBrand = radiatorBrand;
	}

	@Column(name = "radiatorType")
	public String getRadiatorType() {
		return this.radiatorType;
	}

	public void setRadiatorType(String radiatorType) {
		this.radiatorType = radiatorType;
	}

	@Column(name = "kges", nullable = false, precision = 53, scale = 0)
	public double getKges() {
		return this.kges;
	}

	public void setKges(double kges) {
		this.kges = kges;
	}

	@Column(name = "measurement")
	public Integer getMeasurement() {
		return this.measurement;
	}

	public void setMeasurement(Integer measurement) {
		this.measurement = measurement;
	}

	@Column(name = "status")
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", length = 23)
	public Date getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "heatCostAllocator")
	public Set<HeatCostAllocatorReading> getHeatCostAllocatorReadings() {
		return this.heatCostAllocatorReadings;
	}

	public void setHeatCostAllocatorReadings(
			Set<HeatCostAllocatorReading> heatCostAllocatorReadings) {
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
	}

}
