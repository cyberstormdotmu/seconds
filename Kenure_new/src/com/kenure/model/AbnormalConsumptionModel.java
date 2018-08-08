package com.kenure.model;



/**
 * @author pct24
 *
 */
public class AbnormalConsumptionModel {
	
	private String registerId;

	private String consumerId;

	private Integer averageConsumption;
	
	private String consumerAccountNumber;
	
	private Integer last24hrUsage;
	
	public Integer getLast24hrUsage() {
		return last24hrUsage;
	}

	public void setLast24hrUsage(Integer last24hrUsage) {
		this.last24hrUsage = last24hrUsage;
	}

	public String getConsumerAccountNumber() {
		return consumerAccountNumber;
	}

	public void setConsumerAccountNumber(String consumerAccountNumber) {
		this.consumerAccountNumber = consumerAccountNumber;
	}

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String customerId) {
		this.consumerId = customerId;
	}
	
	public Integer getAverageConsumption() {
		return averageConsumption;
	}

	public void setAverageConsumption(Integer averageConsumption) {
		this.averageConsumption = averageConsumption;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return (this.consumerId+this.registerId).hashCode()*prime;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AbnormalConsumptionModel){
			AbnormalConsumptionModel object = (AbnormalConsumptionModel) obj;
			
			return this.registerId.equals(object.getRegisterId()) && 
					this.consumerId.equals(object.getConsumerId());
		}else{
			throw new ClassCastException("Not "+AbnormalConsumptionModel.class.getName()+" of object as parameter");
		}
	}


}
