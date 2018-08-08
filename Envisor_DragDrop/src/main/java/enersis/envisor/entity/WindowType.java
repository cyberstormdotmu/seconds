package enersis.envisor.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "WindowType")
public class WindowType implements Serializable {

	private int id;
	private String type;
	private Byte status;
	private Date transactionTime;
	private Set<Window> windows = new HashSet<Window>(0);

	public WindowType() {
	}

	public WindowType(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public WindowType(int id, String type, Byte status, Date transactionTime,
			Set<Window> windows) {
		this.id = id;
		this.type = type;
		this.status = status;
		this.transactionTime = transactionTime;
		this.windows = windows;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "type", nullable = false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "status")
	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", length = 23)
	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "windowType")
	public Set<Window> getWindows() {
		return windows;
	}

	public void setWindows(Set<Window> windows) {
		this.windows = windows;
	}

}
