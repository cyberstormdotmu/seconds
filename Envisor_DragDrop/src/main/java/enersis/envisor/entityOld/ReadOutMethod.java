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
 * ReadOutMethod generated by hbm2java
 */
@Entity
@Table(name = "ReadOutMethod")
public class ReadOutMethod implements java.io.Serializable {

	private byte id;
	private String type;
	private byte status;
	private Date transactiontime;
	private Set<DistributionLineReadOutMethod> distributionLineReadOutMethods = new HashSet<DistributionLineReadOutMethod>(
			0);

	public ReadOutMethod() {
	}

	public ReadOutMethod(byte id, String type, byte status, Date transactiontime) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.transactiontime = transactiontime;
	}

	public ReadOutMethod(byte id, String type, byte status,
			Date transactiontime,
			Set<DistributionLineReadOutMethod> distributionLineReadOutMethods) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.transactiontime = transactiontime;
		this.distributionLineReadOutMethods = distributionLineReadOutMethods;
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

	@Column(name = "type", nullable = false, length = 15)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactiontime", nullable = false, length = 23)
	public Date getTransactiontime() {
		return this.transactiontime;
	}

	public void setTransactiontime(Date transactiontime) {
		this.transactiontime = transactiontime;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "readOutMethod")
	public Set<DistributionLineReadOutMethod> getDistributionLineReadOutMethods() {
		return this.distributionLineReadOutMethods;
	}

	public void setDistributionLineReadOutMethods(
			Set<DistributionLineReadOutMethod> distributionLineReadOutMethods) {
		this.distributionLineReadOutMethods = distributionLineReadOutMethods;
	}

}
