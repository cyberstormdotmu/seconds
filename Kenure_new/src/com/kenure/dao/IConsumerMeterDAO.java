package com.kenure.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kenure.entity.BillingHistory;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.Consumer;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IConsumerMeterDAO {

	public void addConsumerMeter(ConsumerMeter consumerMeter) throws Exception;
	
	public ConsumerMeter getConsumerMeterById(int consumerMeterId);
	
	public boolean updateConsumer(ConsumerMeter consumerMeter);
	
	public boolean deleteConsumer(int consumerMeterId, int userId);
	
	public List<ConsumerMeter> searchConsumer(String searchRegisterId, String searchConsumerAccNo,Customer customer, Consumer consumer);
	
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByRegisterId(String registerId);
	
	public boolean updateAlertAcknowledgementStatusByMeterTransactionId(int meterTransactionId, boolean resetFlag);

	public List<ConsumerMeter> consumerMeterListByDCId(String dcId);

	public List<ConsumerMeter> consumerMeterListByDUMeterId(String duMeterId);
	
	public List<ConsumerMeter> getBatteryReplacementData(String reportedBy,Integer range,String installation,String siteId,String siteName,String zipcode,Integer customerId);
	
	public  Map<String, Date>  getBatteryAlerts(List<String> registerIds);
	
	public List<ConsumerMeter> getConsumerMeterByRegisterId(List<String> registerIds,int customerId);
	
	public List<ConsumerMeter> getConsumerMetersByCustomerId(int customerId);
	
	public List<BillingHistory> getConsumerMetersBillingByRegId(String registerId);
	
	public List<ConsumerMeter> getConsumerMeterByAddress(String streetName,String add2,String add3,String add4,String zipcode,int customerId,String consumerAccNo,int siteId);
}