package com.kenure.entity;

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
import javax.persistence.Table;
/** 
 * 
 * @author TatvaSoft
 *
 */

@Entity
@Table(name="district_utility_meter")
public class DistrictUtilityMeter {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="district_utility_meter_id")
	private Integer districtUtilityMeterId;

	@Column(name="district_utility_meter_serial_number")
	private String districtUtilityMeterSerialNumber;
	
	@OneToMany(fetch= FetchType.LAZY,mappedBy="districtUtilityMeter")
	private Set<ConsumerMeter> consumerMeter;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="districtUtilityMeter",cascade=CascadeType.ALL)
	private Set<DistrictMeterTransaction> districtMeterTransactions;

	public Integer getDistrictUtilityMeterId() {
		return districtUtilityMeterId;
	}

	public void setDistrictUtilityMeterId(Integer districtUtilityMeterId) {
		this.districtUtilityMeterId = districtUtilityMeterId;
	}

	public String getDistrictUtilityMeterSerialNumber() {
		return districtUtilityMeterSerialNumber;
	}

	public void setDistrictUtilityMeterSerialNumber(
			String districtUtilityMeterSerialNumber) {
		this.districtUtilityMeterSerialNumber = districtUtilityMeterSerialNumber;
	}

	public Set<ConsumerMeter> getConsumerMeter() {
		return consumerMeter;
	}

	public void setConsumerMeter(Set<ConsumerMeter> consumerMeter) {
		this.consumerMeter = consumerMeter;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<DistrictMeterTransaction> getDistrictMeterTransactions() {
		return districtMeterTransactions;
	}

	public void setDistrictMeterTransactions(
			Set<DistrictMeterTransaction> districtMeterTransactions) {
		this.districtMeterTransactions = districtMeterTransactions;
	}
	
}
