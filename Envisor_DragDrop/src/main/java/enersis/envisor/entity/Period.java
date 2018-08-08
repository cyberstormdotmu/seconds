package enersis.envisor.entity;

// Generated May 27, 2015 8:01:12 PM by Hibernate Tools 4.3.1

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
 * Period generated by hbm2java
 */
@Entity
@Table(name = "Period")
public class Period implements Serializable {

	private int id;
	private Project project;
	private Date date;
	private byte status;
	private Date transactionTime;
	private Set<HeatCostAllocatorReading> heatCostAllocatorReadings = new HashSet<HeatCostAllocatorReading>(0);
	private Set<HeatMeterReading> heatMeterReadings = new HashSet<HeatMeterReading>(0);
	private Set<WaterMeterReading> waterMeterReadings = new HashSet<WaterMeterReading>(0);
	private Set<Bill> bills = new HashSet<Bill>(0);

	public Period() {
	}

	public Period(int id, Date date, byte status, Date transactionTime) {
		this.id = id;
		this.date = date;
		this.status = status;
		this.transactionTime = transactionTime;
	}

	public Period(int id, Project project, Date date, byte status, Date transactionTime, Set<HeatCostAllocatorReading> heatCostAllocatorReadings,
			Set<HeatMeterReading> heatMeterReadings, Set<WaterMeterReading> waterMeterReadings, Set<Bill> bills) {
		this.id = id;
		this.project = project;
		this.date = date;
		this.status = status;
		this.transactionTime = transactionTime;
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
		this.heatMeterReadings = heatMeterReadings;
		this.waterMeterReadings = waterMeterReadings;
		this.bills = bills;
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
	@JoinColumn(name = "projectId")
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, length = 23)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "period")
	public Set<HeatCostAllocatorReading> getHeatCostAllocatorReadings() {
		return this.heatCostAllocatorReadings;
	}

	public void setHeatCostAllocatorReadings(Set<HeatCostAllocatorReading> heatCostAllocatorReadings) {
		this.heatCostAllocatorReadings = heatCostAllocatorReadings;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "period")
	public Set<HeatMeterReading> getHeatMeterReadings() {
		return this.heatMeterReadings;
	}

	public void setHeatMeterReadings(Set<HeatMeterReading> heatMeterReadings) {
		this.heatMeterReadings = heatMeterReadings;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "period")
	public Set<WaterMeterReading> getWaterMeterReadings() {
		return this.waterMeterReadings;
	}

	public void setWaterMeterReadings(Set<WaterMeterReading> waterMeterReadings) {
		this.waterMeterReadings = waterMeterReadings;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "period")
	public Set<Bill> getBills() {
		return this.bills;
	}

	public void setBills(Set<Bill> bills) {
		this.bills = bills;
	}

}