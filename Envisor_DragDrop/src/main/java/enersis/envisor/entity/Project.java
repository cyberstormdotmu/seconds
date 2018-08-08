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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Project generated by hbm2java
 */
@Entity
@Table(name = "Project")
public class Project implements Serializable {

	private short id;
	private byte projectType;
	private String projectCode;
	private String projectName;
	private String name;
	private String surname;
	private String address;
	private String authorized;
	private String phone;
	private String email;
	private Date registryDate;
	private String explanations;
	private String dataServices;
	private String operator;
	private Date transactionTime;
	private byte status;
	private Set<MeasurementTypeProject> measurementTypeProjects = new HashSet<MeasurementTypeProject>(
			0);
	private Set<DistributionLine> distributionLines = new HashSet<DistributionLine>(
			0);
	private Set<Period> periods = new HashSet<Period>(0);
	
	private Set<Building> buildings = new HashSet<Building>(0); // Added by TatvaSoft, used to set Primary Key- Foreign Key relation between Project and Building table.

	public Project() {
	}

	public Project(short id, byte projectType, String projectCode,
			String projectName, String name, String surname, String address,
			String authorized, String phone, String email, Date registryDate,
			String dataServices, String operator, Date transactionTime,
			byte status) {
		this.id = id;
		this.projectType = projectType;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.authorized = authorized;
		this.phone = phone;
		this.email = email;
		this.registryDate = registryDate;
		this.dataServices = dataServices;
		this.operator = operator;
		this.transactionTime = transactionTime;
		this.status = status;
	}

	public Project(short id, byte projectType, String projectCode,
			String projectName, String name, String surname, String address,
			String authorized, String phone, String email, Date registryDate,
			String explanations, String dataServices, String operator,
			Date transactionTime, byte status,
			Set<MeasurementTypeProject> measurementTypeProjects,
			Set<DistributionLine> distributionLines, Set<Period> periods) {
		this.id = id;
		this.projectType = projectType;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.authorized = authorized;
		this.phone = phone;
		this.email = email;
		this.registryDate = registryDate;
		this.explanations = explanations;
		this.dataServices = dataServices;
		this.operator = operator;
		this.transactionTime = transactionTime;
		this.status = status;
		this.measurementTypeProjects = measurementTypeProjects;
		this.distributionLines = distributionLines;
		this.periods = periods;
	}

	// Added by TatvaSoft, Following constructor can use with DragDrop functionalities.
	public Project(short id, byte projectType, String projectCode,
			String projectName, String name, String surname, String address,
			String authorized, String phone, String email, Date registryDate,
			String explanations, String dataServices, String operator,
			Date transactionTime, byte status,
			Set<MeasurementTypeProject> measurementTypeProjects,
			Set<DistributionLine> distributionLines, Set<Period> periods, Set<Building> buildings) {
		this.id = id;
		this.projectType = projectType;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.name = name;
		this.surname = surname;
		this.address = address;
		this.authorized = authorized;
		this.phone = phone;
		this.email = email;
		this.registryDate = registryDate;
		this.explanations = explanations;
		this.dataServices = dataServices;
		this.operator = operator;
		this.transactionTime = transactionTime;
		this.status = status;
		this.measurementTypeProjects = measurementTypeProjects;
		this.distributionLines = distributionLines;
		this.periods = periods;
		this.buildings = buildings;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public short getId() {
		return this.id;
	}

	public void setId(short id) {
		this.id = id;
	}

	@Column(name = "projectType", nullable = false)
	public byte getProjectType() {
		return this.projectType;
	}

	public void setProjectType(byte projectType) {
		this.projectType = projectType;
	}

	@Column(name = "projectCode", nullable = false, length = 10)
	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Column(name = "projectName", nullable = false, length = 20)
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "name", nullable = false, length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "surname", nullable = false, length = 25)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "address", nullable = false, length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "authorized", nullable = false, length = 10)
	public String getAuthorized() {
		return this.authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	@Column(name = "phone", nullable = false, length = 42)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "email", nullable = false, length = 40)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registryDate", nullable = false, length = 23)
	public Date getRegistryDate() {
		return this.registryDate;
	}

	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}

	@Column(name = "explanations", length = 100)
	public String getExplanations() {
		return this.explanations;
	}

	public void setExplanations(String explanations) {
		this.explanations = explanations;
	}

	@Column(name = "dataServices", nullable = false, length = 4)
	public String getDataServices() {
		return this.dataServices;
	}

	public void setDataServices(String dataServices) {
		this.dataServices = dataServices;
	}

	@Column(name = "operator", nullable = false, length = 10)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "transactionTime", nullable = false, length = 23)
	public Date getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public Set<MeasurementTypeProject> getMeasurementTypeProjects() {
		return this.measurementTypeProjects;
	}

	public void setMeasurementTypeProjects(
			Set<MeasurementTypeProject> measurementTypeProjects) {
		this.measurementTypeProjects = measurementTypeProjects;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public Set<DistributionLine> getDistributionLines() {
		return this.distributionLines;
	}

	public void setDistributionLines(Set<DistributionLine> distributionLines) {
		this.distributionLines = distributionLines;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public Set<Period> getPeriods() {
		return this.periods;
	}

	public void setPeriods(Set<Period> periods) {
		this.periods = periods;
	}

	//Added by TatvaSoft, Following getter-setter used to set Primary Key- Foreign Key relation between Project and Building table.
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
	public Set<Building> getBuildings() {
		return this.buildings;
	}

	public void setBuildings(Set<Building> buildings) {
		this.buildings = buildings;
	}
}
