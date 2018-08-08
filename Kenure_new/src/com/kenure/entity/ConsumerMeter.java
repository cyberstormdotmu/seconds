package com.kenure.entity;

import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author TatvaSoft
 *
 */
@Entity
@Table(name="consumer_meter")
public class ConsumerMeter implements Cloneable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="consumer_meter_id")
	private Integer consumerMeterId;

	@Column(name="utility_meter_id")
	private Integer utilityMeterId;

	@Column(name="repeater")
	private Boolean repeater;

	@Column(name="total_endpoint_attached")
	private int totalEndpointAttached;

	@Column(name="endpoint_serial_number")
	private String endpointSerialNumber;

	@Column(name="register_id")
	private String registerId;

	@Column(name="endpoint_level")
	private Boolean endpointLevel;

	@Column(name="unit_of_measure")
	private String unitOfMeasure;

	@Column(name="endpoint_installed_date")
	private Date endpointInstalledDate;

	@Column(name="endpoint_battery_replaced_date")
	private Date endpointBatteryReplacedDate;

	@Column(name="asset_inspection_date")
	private Date assetInspectionDate;

	@Column(name="notes")
	private String notes;

	@Column(name="latitude")
	private Double latitude;

	@Column(name="longitude")
	private Double longitude;

	@Column(name="no_of_occupants")
	private Integer numberOfOccupants;

	@Column(name="hosepipe")
	private boolean isHospipe;

	@Column(name="irrigation_system")
	private boolean isIrrigationSystem;

	@Column(name="swimming_pool")
	private boolean isSwimmingPool;

	@Column(name="hot_tub")
	private boolean isHotTub;

	@Column(name="pond")
	private boolean isPond;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_ts")
	private Date createdTimeStamp;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_ts")
	private Date updatedTimeStamp;

	@Column(name="deleted_by")
	private Integer deletedBy;

	@Column(name="deleted_ts")
	private Date deletedTimeStamp;

	@Column(name="active")
	private boolean isActive;

	@Column(name="billing_frequency_in_days",nullable=true)
	private Integer billingFrequencyInDays ; // 0 is default value to map it with hibernate default value

	@Column(name="billing_start_date")
	private Date billingStartDate;

	@Column(name="last_billing_date")
	private Date lastBillingDate;

	@Column(name="current_reading")
	private Integer currentReading;

	@Column(name="last_reading")
	private Integer lastReading;

	@Column(name="bill_date")
	private Date billDate;

	@Column(name="total_no_of_latest_reads")
	private Integer totalNoOfLatestReads;
	
	@Column(name="total_no_of_reads")
	private Integer totalNoOfReads;

	@Column(name="address1")
	private String streetName;

	@Column(name="address2")
	private String address1;

	@Column(name="address3")
	private String address2;

	@Column(name="address4")
	private String address3;

	@Column(name="zipcode")
	private String zipcode;

	@Column(name="last_meter_reading")
	private Integer lastMeterReading;

	@Column(name="last_meter_reading_ts")
	private Date lastMeterReadingDate;

	@Column(name="left_billing_digit")
	private Integer leftBillingDigit;

	@Column(name="right_billing_digit")
	private Integer rightBillingDigit;

	@Column(name="decimal_position")
	private Integer DecimalPosition;

	@Column(name="leakage_threshold")
	private Integer leakageThreshold;

	@Column(name="backflow_limit")
	private Integer backflowLimit;

	@Column(name="usage_threshold")
	private Integer usageThreshold;

	@Column(name="usage_interval")
	private Integer usageInterval;

	@Column(name="direction")
	private Integer direction;

	@Column(name="kvalue")
	private Integer kvalue;

	@Column(name="utility_code")
	private Integer utilityCode;

	@Column(name="required_repeater_nodes")
	private Integer requiredRepeaterNodes;

	@Column(name="require_repeater_levels")
	private Integer requireRepeaterLevels;

	@Column(name="firmware_version")
	private String firmwareVersion;

	@Column(name="my_slot")
	private Integer mySlot;

	@Column(name="parent_slot")
	private Integer parentSlot;

	@Column(name="iscommissioned")
	private Boolean isCommissioned;
	
	@Column(name="estimated_remainaing_battery_life_in_year")
	private Double estimatedRemainaingBatteryLifeInYear;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="datacollector_id")
	private DataCollector dataCollector;

	/*@OneToMany(fetch=FetchType.LAZY,mappedBy="consumerMeter")
	private Set<ConsumerMeterTransaction> consumerMeterTransaction; */

	@OneToMany(fetch=FetchType.LAZY,mappedBy="consumerMeter")
	private Set<BillingHistory> billingHistory;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="district_utility_meter_id")
	private DistrictUtilityMeter districtUtilityMeter;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="site_id")
	private Site site;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="tariff_plan_id")
	private TariffPlan tariffPlan;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="installer_id")
	private Installer installer;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="consumer_id")
	private Consumer consumer;

	@Transient
	public Object clone()throws CloneNotSupportedException{  
		return super.clone();  
	}

	public String getEndpointSerialNumber() {
		return endpointSerialNumber;
	}

	public void setEndpointSerialNumber(String endpointSerialNumber) {
		this.endpointSerialNumber = endpointSerialNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Integer getLastMeterReading() {
		return lastMeterReading;
	}

	public void setLastMeterReading(Integer lastMeterReading) {
		this.lastMeterReading = lastMeterReading;
	}

	public Date getLastMeterReadingDate() {
		return lastMeterReadingDate;
	}

	public void setLastMeterReadingDate(Date lastMeterReadingDate) {
		this.lastMeterReadingDate = lastMeterReadingDate;
	}

	public Integer getTotalNoOfLatestReads() {
		return totalNoOfLatestReads;
	}

	public void setTotalNoOfLatestReads(Integer totalNoOfLatestReads) {
		this.totalNoOfLatestReads = totalNoOfLatestReads;
	}

	public Integer getTotalNoOfReads() {
		return totalNoOfReads;
	}

	public void setTotalNoOfReads(Integer totalNoOfReads) {
		this.totalNoOfReads = totalNoOfReads;
	}

	public void setMySlot(Integer mySlot) {
		this.mySlot = mySlot;
	}

	public void setParentSlot(Integer parentSlot) {
		this.parentSlot = parentSlot;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public TariffPlan getTariffPlan() {
		return tariffPlan;
	}

	public void setTariffPlan(TariffPlan tariffPlan) {
		this.tariffPlan = tariffPlan;
	}

	public Installer getInstaller() {
		return installer;
	}

	public void setInstaller(Installer installer) {
		this.installer = installer;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public Boolean getRepeater() {
		return repeater;
	}

	public void setRepeater(Boolean repeater) {
		this.repeater = repeater;
	}

	public DistrictUtilityMeter getDistrictUtilityMeter() {
		return districtUtilityMeter;
	}

	public void setDistrictUtilityMeter(DistrictUtilityMeter districtUtilityMeter) {
		this.districtUtilityMeter = districtUtilityMeter;
	}

	public Boolean getEndpointLevel() {
		return endpointLevel;
	}

	public void setConsumerMeterId(Integer consumerMeterId) {
		this.consumerMeterId = consumerMeterId;
	}

	public void setUtilityMeterId(Integer utilityMeterId) {
		this.utilityMeterId = utilityMeterId;
	}

	/*public void setTotalEndpointAttached(int totalEndpointAttached) {
		this.totalEndpointAttached = totalEndpointAttached;
	}*/

	public void setNumberOfOccupants(Integer numberOfOccupants) {
		this.numberOfOccupants = numberOfOccupants;
	}

	public Integer getNumberOfOccupants() {
		return numberOfOccupants;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getConsumerMeterId() {
		return consumerMeterId;
	}

	public void setConsumerMeterId(int consumerMeterId) {
		this.consumerMeterId = consumerMeterId;
	}

	public int getUtilityMeterId() {
		return utilityMeterId;
	}

	public void setUtilityMeterId(int utilityMeterId) {
		this.utilityMeterId = utilityMeterId;
	}

	/*public String getConsumerAccountNumber() {
		return consumerAccountNumber;
	}

	public void setConsumerAccountNumber(String consumerAccountNumber) {
		this.consumerAccountNumber = consumerAccountNumber;
	}*/

	/*public boolean isRepeater() {
		return repeater;
	}

	public void setRepeater(boolean repeater) {
		this.repeater = repeater;
	}
	 */
	public int getTotalEndpointAttached() {
		return totalEndpointAttached;
	}

	public void setTotalEndpointAttached(int totalEndpointAttached) {
		this.totalEndpointAttached = totalEndpointAttached;
	}

	/*public boolean isEndpointLevel() {
		return endpointLevel;
	}

	public void setEndpointLevel(Boolean endpointLevel) {
		this.endpointLevel = endpointLevel;
	}*/

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public Date getEndpointInstalledDate() {
		return endpointInstalledDate;
	}

	public void setEndpointInstalledDate(Date endpointInstalledDate) {
		this.endpointInstalledDate = endpointInstalledDate;
	}

	public Date getEndpointBatteryReplacedDate() {
		return endpointBatteryReplacedDate;
	}

	public void setEndpointBatteryReplacedDate(Date endpointBatteryReplacedDate) {
		this.endpointBatteryReplacedDate = endpointBatteryReplacedDate;
	}

	/*public Date getassetInspectionDate() {
		return assetInspectionDate;
	}

	public void setassetInspectionDate(Date assetInspectionDate) {
		this.assetInspectionDate = assetInspectionDate;
	}*/

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public boolean isHospipe() {
		return isHospipe;
	}

	public void setHospipe(boolean isHospipe) {
		this.isHospipe = isHospipe;
	}

	public boolean isIrrigationSystem() {
		return isIrrigationSystem;
	}

	public void setIrrigationSystem(boolean isIrrigationSystem) {
		this.isIrrigationSystem = isIrrigationSystem;
	}

	public boolean isSwimmingPool() {
		return isSwimmingPool;
	}

	public void setSwimmingPool(boolean isSwimmingPool) {
		this.isSwimmingPool = isSwimmingPool;
	}

	public boolean isHotTub() {
		return isHotTub;
	}

	public void setHotTub(boolean isHotTub) {
		this.isHotTub = isHotTub;
	}

	public boolean isPond() {
		return isPond;
	}

	public void setPond(boolean isPond) {
		this.isPond = isPond;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTimeStamp() {
		return updatedTimeStamp;
	}

	public void setUpdatedTimeStamp(Date updatedTimeStamp) {
		this.updatedTimeStamp = updatedTimeStamp;
	}

	public Integer getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(Integer deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedTimeStamp() {
		return deletedTimeStamp;
	}

	public void setDeletedTimeStamp(Date deletedTimeStamp) {
		this.deletedTimeStamp = deletedTimeStamp;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getBillingFrequencyInDays() {
		return billingFrequencyInDays;
	}

	public void setBillingFrequencyInDays(Integer billingFrequencyInDays) {
		this.billingFrequencyInDays = billingFrequencyInDays;
	}

	public Date getBillingStartDate() {
		return billingStartDate;
	}

	public void setBillingStartDate(Date billingStartDate) {
		this.billingStartDate = billingStartDate;
	}

	public Date getLastBillingDate() {
		return lastBillingDate;
	}

	public void setLastBillingDate(Date lastBillingDate) {
		this.lastBillingDate = lastBillingDate;
	}

	public Integer getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(Integer currentReading) {
		this.currentReading = currentReading;
	}

	public Integer getLastReading() {
		return lastReading;
	}

	public void setLastReading(Integer lastReading) {
		this.lastReading = lastReading;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public DataCollector getDataCollector() {
		return dataCollector;
	}

	public void setDataCollector(DataCollector dataCollector) {
		this.dataCollector = dataCollector;
	}

	/*public Set<ConsumerMeterTransaction> getConsumerMeterTransaction() {
		return consumerMeterTransaction;
	}

	public void setConsumerMeterTransaction(
			Set<ConsumerMeterTransaction> consumerMeterTransaction) {
		this.consumerMeterTransaction = consumerMeterTransaction;
	}*/

	public Set<BillingHistory> getBillingHistory() {
		return billingHistory;
	}

	public void setBillingHistory(Set<BillingHistory> billingHistory) {
		this.billingHistory = billingHistory;
	}

	public Integer getLeftBillingDigit() {
		return leftBillingDigit;
	}

	public void setLeftBillingDigit(Integer leftBillingDigit) {
		this.leftBillingDigit = leftBillingDigit;
	}

	public Integer getRightBillingDigit() {
		return rightBillingDigit;
	}

	public void setRightBillingDigit(Integer rightBillingDigit) {
		this.rightBillingDigit = rightBillingDigit;
	}

	public Integer getDecimalPosition() {
		return DecimalPosition;
	}

	public void setDecimalPosition(Integer decimalPosition) {
		DecimalPosition = decimalPosition;
	}

	public Integer getLeakageThreshold() {
		return leakageThreshold;
	}

	public void setLeakageThreshold(Integer leakageThreshold) {
		this.leakageThreshold = leakageThreshold;
	}

	public Integer getBackflowLimit() {
		return backflowLimit;
	}

	public void setBackflowLimit(Integer backflowLimit) {
		this.backflowLimit = backflowLimit;
	}

	public Integer getUsageThreshold() {
		return usageThreshold;
	}

	public void setUsageThreshold(Integer usageThreshold) {
		this.usageThreshold = usageThreshold;
	}

	public Integer getUsageInterval() {
		return usageInterval;
	}

	public void setUsageInterval(Integer usageInterval) {
		this.usageInterval = usageInterval;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Integer getKvalue() {
		return kvalue;
	}

	public void setKvalue(Integer kvalue) {
		this.kvalue = kvalue;
	}

	public Integer getUtilityCode() {
		return utilityCode;
	}

	public void setUtilityCode(Integer utilityCode) {
		this.utilityCode = utilityCode;
	}

	public Integer getRequiredRepeaterNodes() {
		return requiredRepeaterNodes;
	}

	public void setRequiredRepeaterNodes(Integer requiredRepeaterNodes) {
		this.requiredRepeaterNodes = requiredRepeaterNodes;
	}

	public Integer getRequireRepeaterLevels() {
		return requireRepeaterLevels;
	}

	public void setRequireRepeaterLevels(Integer requireRepeaterLevels) {
		this.requireRepeaterLevels = requireRepeaterLevels;
	}

	public void setEndpointLevel(Boolean endpointLevel) {
		this.endpointLevel = endpointLevel;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public Date getAssetInspectionDate() {
		return assetInspectionDate;
	}

	public void setAssetInspectionDate(Date assetInspectionDate) {
		this.assetInspectionDate = assetInspectionDate;
	}

	public int getMySlot() {
		return mySlot;
	}

	public void setMySlot(int mySlot) {
		this.mySlot = mySlot;
	}

	public int getParentSlot() {
		return parentSlot;
	}

	public void setParentSlot(int parentSlot) {
		this.parentSlot = parentSlot;
	}

	public Boolean getIsCommissioned() {
		return isCommissioned;
	}

	public void setIsCommissioned(Boolean isCommissioned) {
		this.isCommissioned = isCommissioned;
	}

	public Double getEstimatedRemainaingBatteryLifeInYear() {
		return estimatedRemainaingBatteryLifeInYear;
	}

	public void setEstimatedRemainaingBatteryLifeInYear(
			Double estimatedRemainaingBatteryLifeInYear) {
		this.estimatedRemainaingBatteryLifeInYear = estimatedRemainaingBatteryLifeInYear;
	}
}