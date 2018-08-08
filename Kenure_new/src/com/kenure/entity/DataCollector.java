package com.kenure.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="datacollector")
public class DataCollector implements Cloneable{

	@Id
	@Column(name="datacollector_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer datacollectorId;

	@Column(name="dc_serial_number")
	private String dcSerialNumber;

	@Column(name="dc_ip", nullable=false)
	private String dcIp;

	@Column(name="dc_simcard_no")
	private String dcSimcardNo;

	@Column(name="dc_user_id", nullable=false)
	private String dcUserId;

	@Column(name="iscommissioned")
	private Boolean isCommissioned; 
	
	@Column(name="mb_per_month")
	private Double mbPerMonth;

	@Column(name="dc_user_password", nullable=false)
	private String dcUserPassword;

	@Column(name="dc_port")
	private Integer port;

	@Column(name="boundry_datacollector")
	private String boundryDatacollector;

	@Column(name="network_id")
	private Integer networkId;

	@Column(name="network_backup_data")
	private String network_backup_data;

	@Column(name="latitude")
	private Double latitude;

	@Column(name="longitude")
	private Double longitude;

	@Column(name="total_endpoints")
	private Integer totalEndpoints;

	@Column(name="active")
	private Boolean active;

	@Column(name="meter_reading_interval")
	private Integer meterReadingInterval;

	@Column(name="network_reading_interval")
	private Integer networkReadingInterval;

	@Column(name="network_status_interval")
	private Integer networkStatusInterval;

	@Column(name="type")
	private String type;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_ts")
	private Date createdTs;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_ts")
	private Date updatedTs;

	@Column(name="deleted_by")
	private Integer deletedBy;

	@Column(name="deleted_ts")
	private Date deletedTs;

	@Column(name="mode")
	private String dcMode;
	
	@Column(name="isconnection_ok")
	private Boolean isConnectionOk;
	
	@Column(name="isconfig_ok")
	private Boolean isConfigOk;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="dataCollector",cascade=CascadeType.MERGE)
	private Set<ConsumerMeter> consumerMeters;

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "datacollector_id", referencedColumnName = "datacollector_id")
	private Set<DatacollectorMessageQueue> datacollectorMessageQueues;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="site_id")
	private Site site;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="installer_id")
	private Installer installer;

	@OneToOne
	@PrimaryKeyJoinColumn
	private BoundryDataCollector boundrydatacollector;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@Column(name="asset_inspection_date")
	private Date assetInspectionDate;
	
	@Column(name="alerts")
	private String alerts;
	
	@Column(name="alerts_ack")
	private boolean alerts_ack;
	
	@Column(name="battery_voltage")
	private Integer batteryVoltage;
	
	@Column(name="islevel1comm_started")
	private Boolean isLevel1CommStarted;
	
	@Column(name="islevelncomm_started")
	private Boolean isLevelnCommStarted;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="dataCollector", cascade=CascadeType.ALL)
	private Set<DataCollectorAlerts> dataCollectorAlerts;

	@Transient
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}

	public Integer getDatacollectorId() {
		return datacollectorId;
	}

	public void setDatacollectorId(Integer datacollectorId) {
		this.datacollectorId = datacollectorId;
	}

	public String getDcSerialNumber() {
		return dcSerialNumber;
	}

	public void setDcSerialNumber(String dcSerialNumber) {
		this.dcSerialNumber = dcSerialNumber;
	}

	public String getDcIp() {
		return dcIp;
	}

	public void setDcIp(String dcIp) {
		this.dcIp = dcIp;
	}

	public String getDcSimcardNo() {
		return dcSimcardNo;
	}

	public void setDcSimcardNo(String dcSimcardNo) {
		this.dcSimcardNo = dcSimcardNo;
	}

	public String getDcUserId() {
		return dcUserId;
	}

	public void setDcUserId(String dcUserId) {
		this.dcUserId = dcUserId;
	}

	public String getDcUserPassword() {
		return dcUserPassword;
	}

	public void setDcUserPassword(String dcUserPassword) {
		this.dcUserPassword = dcUserPassword;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getBoundryDatacollector() {
		return boundryDatacollector;
	}

	public void setBoundryDatacollector(String boundryDatacollector) {
		this.boundryDatacollector = boundryDatacollector;
	}

	public Integer getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public String getNetwork_backup_data() {
		return network_backup_data;
	}

	public void setNetwork_backup_data(String network_backup_data) {
		this.network_backup_data = network_backup_data;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getTotalEndpoints() {
		return totalEndpoints;
	}

	public void setTotalEndpoints(Integer totalEndpoints) {
		this.totalEndpoints = totalEndpoints;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getMeterReadingInterval() {
		return meterReadingInterval;
	}

	public void setMeterReadingInterval(Integer meterReadingInterval) {
		this.meterReadingInterval = meterReadingInterval;
	}

	public Integer getNetworkReadingInterval() {
		return networkReadingInterval;
	}

	public void setNetworkReadingInterval(Integer networkReadingInterval) {
		this.networkReadingInterval = networkReadingInterval;
	}

	public Integer getNetworkStatusInterval() {
		return networkStatusInterval;
	}

	public void setNetworkStatusInterval(Integer networkStatusInterval) {
		this.networkStatusInterval = networkStatusInterval;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTs() {
		return createdTs;
	}

	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTs() {
		return deletedTs;
	}

	public void setDeletedTs(Date deletedTs) {
		this.deletedTs = deletedTs;
	}

	public Set<ConsumerMeter> getConsumerMeters() {
		return consumerMeters;
	}

	public void setConsumerMeters(Set<ConsumerMeter> consumerMeters) {
		this.consumerMeters = consumerMeters;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	public BoundryDataCollector getBoundrydatacollector() {
		return boundrydatacollector;
	}

	public void setBoundrydatacollector(BoundryDataCollector boundrydatacollector) {
		this.boundrydatacollector = boundrydatacollector;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Double getMbPerMonth() {
		return mbPerMonth;
	}

	public void setMbPerMonth(Double mbPerMonth) {
		this.mbPerMonth = mbPerMonth;
	}

	public Boolean getIsCommissioned() {
		return isCommissioned;
	}

	public void setIsCommissioned(Boolean isCommissioned) {
		this.isCommissioned = isCommissioned;
	}
	
	public Date getAssetInspectionDate() {
		return assetInspectionDate;
	}

	public void setAssetInspectionDate(Date assetInspectionDate) {
		this.assetInspectionDate = assetInspectionDate;
	}
	
	public boolean isAlerts_ack() {
		return alerts_ack;
	}

	public void setAlerts_ack(boolean alerts_ack) {
		this.alerts_ack = alerts_ack;
	}
	
	public String getAlerts() {
		return alerts;
	}

	public void setAlerts(String alerts) {
		this.alerts = alerts;
	}

	public String getDcMode() {
		return dcMode;
	}

	public void setDcMode(String dcMode) {
		this.dcMode = dcMode;
	}

	public Boolean getIsConnectionOk() {
		return isConnectionOk;
	}

	public void setIsConnectionOk(Boolean isConnectionOk) {
		this.isConnectionOk = isConnectionOk;
	}

	public Boolean getIsConfigOk() {
		return isConfigOk;
	}

	public void setIsConfigOk(Boolean isConfigOk) {
		this.isConfigOk = isConfigOk;
	}

	public Set<DatacollectorMessageQueue> getDatacollectorMessageQueues() {
		return datacollectorMessageQueues;
	}

	public void setDatacollectorMessageQueues(
			Set<DatacollectorMessageQueue> datacollectorMessageQueues) {
		this.datacollectorMessageQueues = datacollectorMessageQueues;
	}

	public Integer getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Integer batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public Set<DataCollectorAlerts> getDataCollectorAlerts() {
		return dataCollectorAlerts;
	}

	public void setDataCollectorAlerts(Set<DataCollectorAlerts> dataCollectorAlerts) {
		this.dataCollectorAlerts = dataCollectorAlerts;
	}

	public Boolean getIsLevel1CommStarted() {
		return isLevel1CommStarted;
	}

	public void setIsLevel1CommStarted(Boolean isLevel1CommStarted) {
		this.isLevel1CommStarted = isLevel1CommStarted;
	}

	public Boolean getIsLevelnCommStarted() {
		return isLevelnCommStarted;
	}

	public void setIsLevelnCommStarted(Boolean isLevelnCommStarted) {
		this.isLevelnCommStarted = isLevelnCommStarted;
	}
}