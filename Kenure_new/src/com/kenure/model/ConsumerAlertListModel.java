package com.kenure.model;

public class ConsumerAlertListModel {

	private String consumerAccountNumber;
	private String zipcode;
	private Integer siteId;
	private String registerId;

	public String getConsumerAccountNumber() {
		return consumerAccountNumber;
	}
	public void setConsumerAccountNumber(String consumerAccountNumber) {
		this.consumerAccountNumber = consumerAccountNumber;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

}
