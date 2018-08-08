package com.kenure.dao;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.kenure.entity.BillingHistory;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.DistrictMeterTransaction;

import java.util.List;

import com.kenure.entity.DataCollector;

import java.util.List;

import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.model.AbnormalConsumptionModel;

/**
 * 
 * @author TatvaSoft
 *
 */
public interface IReportDAO {

	public List<DistrictMeterTransaction> getDistrictMeterTransaction(int customerId, Date startDate, Date endDate);
	
	public List<Object> getConsumerMeterTransactionByDistrictMeterId(
			int districtMeterId, int customerId, Date startDate, Date endDate);

	public List<DataCollector> getDCListForDataUsageReport(int customerId,Integer siteId, String siteName, String installationName, String dcSerial);

	public List<BillingHistory> getBillingHistoryList(Date billingStartDate,Date billingEndDate, int customerId, boolean onlyEstimatedReadAcc);

	public List<Object> getConsumerMeterAlerts(String alert, Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Instant periodStartDate,
			Instant periodEndDate);

	public List<DataCollectorAlerts> getNetworkAlerts(String alert, Integer siteId,
			String siteName, int customerId, Instant periodStartDate,
			Instant periodEndDate);
	
	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByConsumerMeterRegisterId(Integer registerId);

	void initalizeConsumerMeter(Consumer thisConsumer);

	public List<ConsumerMeter> getConsumerMeterByCustomer(Customer customer);

	public List<ConsumerMeterTransaction> getConsumerTransactionByConsumerMeletList(List<Integer> list);

	public List<ConsumerMeterTransaction> transactionListBasedOnSingleConsumerMeterRegisterId(Integer registerId);

	public List<AbnormalConsumptionModel> callAbnormalSO(Customer customer,int type);

	public List<ConsumerMeterTransaction> getConsumerMeterTransactionByRegisterId(String registerID);

	public List<Object> getAggregateConsumption(Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Instant periodStartDate,
			Instant periodEndDate, Integer noOfOccupants);

	public Double getDataUsageByCustomer(Customer customer);

	public List searchFreeReportData(Customer customer,String selectedField,String reportPeriod, Date startDate, Date endDate,
			String installatioName, String siteName, String dcSerialNumber);

	public void initalizeSiteByregion(Region n);

	public void initializeDatacollectorBySite(Site site);

	public Double getRevenueForIndividualConsumerMeter(
			ConsumerMeter consumerMeter, Date startLimitDate, Date endDate);

	public void initializeConsumerMeterByDC(DataCollector dc);

	public List<BillingHistory> getUsageForIndividualConsumerMeter(ConsumerMeter cm,
			Date time, Date endDate);

	public void initializeConsumerByCustomer(Customer customer);

	public void initializeRegionByCustomer(Customer customer);

	public void initializeConsumerMeterByConsumer(Consumer consumer);

}
