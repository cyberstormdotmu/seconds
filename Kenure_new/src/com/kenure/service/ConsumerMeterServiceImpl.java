package com.kenure.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kenure.dao.IConsumerMeterDAO;
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
@Service
public class ConsumerMeterServiceImpl implements IConsumerMeterService {

	@Autowired
	IConsumerMeterDAO consumerMeterDAO;
	
	@Override  @Transactional
	public void addConsumerMeter(ConsumerMeter consumerMeter) throws Exception {
		consumerMeterDAO.addConsumerMeter(consumerMeter);
	}

	@Override  @Transactional
	public ConsumerMeter getConsumerMeterById(int consumerMeterId) {
		return consumerMeterDAO.getConsumerMeterById(consumerMeterId);
	}

	@Override  @Transactional
	public boolean updateConsumer(ConsumerMeter consumerMeter) {
		return consumerMeterDAO.updateConsumer(consumerMeter);
	}

	@Override  @Transactional
	public boolean deleteConsumer(int consumerMeterId, int userId) {
		return consumerMeterDAO.deleteConsumer(consumerMeterId, userId);
	}

	@Override  @Transactional
	public List<ConsumerMeter> searchConsumer(String searchRegisterId, String searchConsumerAccNo,Customer customer, Consumer consumer){
		return consumerMeterDAO.searchConsumer(searchRegisterId, searchConsumerAccNo, customer, consumer);
	}
	
	@Override  @Transactional
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByRegisterId(String registerId) {
		return consumerMeterDAO.getConsumerMeterTransactionByRegisterId(registerId);
	}

	@Override @Transactional
	public boolean updateAlertAcknowledgementStatusByMeterTransactionId(int meterTransactionId, boolean resetFlag) {
		return consumerMeterDAO.updateAlertAcknowledgementStatusByMeterTransactionId(meterTransactionId, resetFlag);
	}

	@Override @Transactional
	public List<ConsumerMeter> consumerMeterListByDCId(String dcId) {
		return consumerMeterDAO.consumerMeterListByDCId(dcId);
	}

	@Override @Transactional
	public List<ConsumerMeter> consumerMeterListByDUMeterId(String duMeterId) {
		return consumerMeterDAO.consumerMeterListByDUMeterId(duMeterId);
	}

	@Override @Transactional
	public List<ConsumerMeter> getBatteryReplacementData(String reportedBy,Integer range,String installation,String siteId,String siteName,String zipcode,Integer customerId){
		// TODO Auto-generated method stub
		return consumerMeterDAO.getBatteryReplacementData(reportedBy, range,installation,siteId,siteName,zipcode,customerId);
	}

	@Override @Transactional
	public  Map<String, Date>  getBatteryAlerts(List<String> registerIds) {
		// TODO Auto-generated method stub
		return consumerMeterDAO.getBatteryAlerts(registerIds);
	}

	@Override @Transactional
	public List<ConsumerMeter> getConsumerMeterByRegisterId(
			List<String> registerIds, int customerId) {
		// TODO Auto-generated method stub
		return consumerMeterDAO.getConsumerMeterByRegisterId(registerIds, customerId);
	}

	@Override @Transactional
	public List<ConsumerMeter> getConsumerMetersByCustomerId(int customerId) {
		// TODO Auto-generated method stub
		return consumerMeterDAO.getConsumerMetersByCustomerId(customerId);
	}

	@Override @Transactional
	public List<BillingHistory> getConsumerMetersBillingByRegId(String registerId) {
		return consumerMeterDAO.getConsumerMetersBillingByRegId(registerId);
	}

	@Override @Transactional
	public List<ConsumerMeter> getConsumerMeterByAddress(String streetName,
			String add2, String add3, String add4, String zipcode,int customerId,String consumerAccNo,int siteId) {
		// TODO Auto-generated method stub
		return consumerMeterDAO.getConsumerMeterByAddress(streetName, add2, add3, add4, zipcode,customerId,consumerAccNo,siteId);
	}
}