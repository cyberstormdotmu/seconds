package enersis.envisor.entityOld;

// Generated Feb 3, 2015 10:24:48 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MeterType generated by hbm2java
 */
@Entity
@Table(name = "MeterType")
public class MeterType implements java.io.Serializable {

	private byte id;
	private String abbreviation;
	private String name;
	private byte status;
	private Date transactionTime;
	private Set<DistributionLineMeterType> distributionLineMeterTypes = new HashSet<DistributionLineMeterType>(
			0);

	public MeterType() {
	}

	public MeterType(byte id, String abbreviation, String name, byte status,
			Date transactionTime) {
		this.id = id;
		this.abbreviation = abbreviation;
		this.name = name;
		this.status = status;
		this.transactionTime = transactionTime;
	}

	public MeterType(byte id, String abbreviation, String name, byte status,
			Date transactionTime,
			Set<DistributionLineMeterType> distributionLineMeterTypes) {
		this.id = id;
		this.abbreviation = abbreviation;
		this.name = name;
		this.status = status;
		this.transactionTime = transactionTime;
		this.distributionLineMeterTypes = distributionLineMeterTypes;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Column(name = "abbreviation", nullable = false, length = 5)
	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "meterType")
	public Set<DistributionLineMeterType> getDistributionLineMeterTypes() {
		return this.distributionLineMeterTypes;
	}

	public void setDistributionLineMeterTypes(
			Set<DistributionLineMeterType> distributionLineMeterTypes) {
		this.distributionLineMeterTypes = distributionLineMeterTypes;
	}

}