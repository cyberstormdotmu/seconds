package com.kenure.service;

import java.util.Date;
import java.util.List;

import com.kenure.entity.BillingHistory;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.model.AbnormalConsumptionModel;
import com.kenure.model.TariffModel;
import com.kenure.model.TariffTransactionModel;
import com.kenure.model.WhatIfTariffModel;

/**
 * 
 * @author TatvaSoft
 *
 */

public interface IReportService {

	public List<Object> getNetworkWaterLoss(Date startDate,Date endDate, int customerId);

	public List<DataCollector> getDCListForDataUsageReport(int customerId,Integer siteId, String siteName, String installationName, String dcSerial);

	public List<BillingHistory> getBillingHistoryList(Date billingStartDate,Date billingEndDate, int customerId, boolean onlyEstimatedReadAcc);

	List<Object> getConsumerMeterAlerts(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, String alert,
			Integer siteId, String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1);

	List<DataCollectorAlerts> getNetworkAlerts(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, String alert,
			Integer siteId, String siteName, int customerId);
	
	public List<AbnormalConsumptionModel> generateInitDataForAbnormalConsumption(Customer customer);
	
	public List callAbnormalSP(Customer customer,int type);

	public StringBuilder generateActionReportDataForAbnormalConsumption(String registerID);

	public List<Object> getAggregateConsumption(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Integer noOfOccupants);

	public Double getDataUsageByCustomer(Customer customer);

	public List searchFreeReportData(Customer customer,String selectedField, String reportBy,
			String reportPeriod, Date startDate, Date endDate,
			String installatioName, String siteName, String dcSerialNumber);

	public void initalizeSiteByregion(Region n);

	public String findSameTariffBasedOnRegion(Region region,Site site,Customer customer);

	public TariffModel getTariffModelByTariffName(String siteName,Customer customer);

	public Double getRevenueForIndividualConsumerMeter(
			ConsumerMeter consumerMeter, Date startLimitDate, Date date);

	public void initalizeDCBySite(Site site);

	public void initializeConsumerMeterByDC(DataCollector dc);

	public Double getUsageForIndividualConsumerMeter(ConsumerMeter cm,
			Date startLimitDate, Date date, List<TariffTransactionModel> model,Integer consumptionVar);

	public Double getRevenueByConsumerList(List<Consumer> consumerList,Date startDate,Date endDate);

	public void initializeConsumerByCustomer(Customer customer);

	public void initializeRegionByCustomer(Customer customer);

	public void initializeConsumerMeterByConsumer(Consumer consumer);
}
