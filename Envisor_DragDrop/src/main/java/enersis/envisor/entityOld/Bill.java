package enersis.envisor.entityOld;

// Generated Apr 22, 2015 10:59:54 AM by Hibernate Tools 4.3.1

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
 * Bill generated by hbm2java
 */
@Entity
@Table(name = "Bill")
public class Bill implements Serializable {

	private int id;
	private DistributionLine distributionLine;
	private Date date;
	private String billCode;
	private Double usage;
	private String unit;
	private double charge;
	private String fileName;
	private byte status;
	private Date transactionTime;
	private Set<Period> periods = new HashSet<Period>(0);

	public Bill() {
	}

	public Bill(int id, DistributionLine distributionLine, Date date,
			double charge, String fileName, byte status,
			Date transactionTime) {
		this.id = id;
		this.distributionLine = distributionLine;
		this.date = date;
		this.charge = charge;
		this.fileName = fileName;
		this.status = status;
		this.transactionTime = transactionTime;
	}

	public Bill(int id, DistributionLine distributionLine, Date date,
			String billCode, Double usage, String unit,
			double charge, String fileName, byte status,
			Date transactionTime, Set<Period> periods) {
		this.id = id;
		this.distributionLine = distributionLine;
		this.date = date;
		this.billCode = billCode;
		this.usage = usage;
		this.unit = unit;
		this.charge = charge;
		this.fileName = fileName;
		this.status = status;
		this.transactionTime = transactionTime;
		this.periods = periods;
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
	@JoinColumn(name = "distributionLineId", nullable = false)
	public DistributionLine getDistributionLine() {
		return this.distributionLine;
	}

	public void setDistributionLine(DistributionLine distributionLine) {
		this.distributionLine = distributionLine;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "billCode")
	public String getBillCode() {
		return this.billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	@Column(name = "usage", precision = 53, scale = 0)
	public Double getUsage() {
		return this.usage;
	}

	public void setUsage(Double usage) {
		this.usage = usage;
	}

	@Column(name = "unit")
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "charge", nullable = false, precision = 53, scale = 0)
	public double getCharge() {
		return this.charge;
	}

	public void setCharge(double charge) {
		this.charge = charge;
	}

	@Column(name = "fileName", nullable = false)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "bill")
	public Set<Period> getPeriods() {
		return this.periods;
	}

	public void setPeriods(Set<Period> periods) {
		this.periods = periods;
	}

}
