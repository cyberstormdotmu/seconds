package enersis.envisor.entity;

// Generated Apr 28, 2015 10:59:09 AM by Hibernate Tools 4.3.1

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DistributionLineBill generated by hbm2java
 */
@Entity
@Table(name = "DistributionLine_Bill")
public class DistributionLineBill implements java.io.Serializable {

	private int id;
	private Bill bill;
	private DistributionLine distributionLine;
	private byte status;
	private Date transactionTime;

	public DistributionLineBill() {
	}

	public DistributionLineBill(int id, Bill bill,
			DistributionLine distributionLine, byte status, Date transactionTime) {
		this.id = id;
		this.bill = bill;
		this.distributionLine = distributionLine;
		this.status = status;
		this.transactionTime = transactionTime;
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
	@JoinColumn(name = "billId", nullable = false)
	public Bill getBill() {
		return this.bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributionLineId", nullable = false)
	public DistributionLine getDistributionLine() {
		return this.distributionLine;
	}

	public void setDistributionLine(DistributionLine distributionLine) {
		this.distributionLine = distributionLine;
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

}
