package com.kenure.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.dao.IReportDAO;
import com.kenure.dao.IUserDAO;
import com.kenure.entity.BillingHistory;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.DistrictMeterTransaction;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.entity.Installer;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.entity.TariffPlan;
import com.kenure.model.AbnormalConsumptionModel;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.model.TariffModel;
import com.kenure.model.TariffTransactionModel;
import com.kenure.utils.LoggerUtils;

/**
 * 
 * @author TatvaSoft
 *
 */
@Service
@Transactional
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IReportDAO reportDAO;

	@Autowired 
	private IUserService userService;

	private Logger log = LoggerUtils.getInstance(ReportServiceImpl.class);


	@Override
	public List<Object> getNetworkWaterLoss(Date startDate,
			Date endDate, int customerId) {

		List<DistrictMeterTransaction> districtMeterTransactionList = new ArrayList<DistrictMeterTransaction>();
		List<Object> objectList = new ArrayList<Object>();
		List<Long> aggregateOfConMetList = new ArrayList<Long>();

		List searchedList = reportDAO.getDistrictMeterTransaction(customerId,startDate,endDate);
		if(searchedList!=null && !searchedList.isEmpty()){
			for(int i=0;i<searchedList.size();i++){
				Object[] listObj = (Object[]) searchedList.get(i);
				DistrictMeterTransaction districtMeterTransaction = new DistrictMeterTransaction();
				DistrictUtilityMeter districtUtilityMeter = new DistrictUtilityMeter();
				districtMeterTransaction.setDistrictMeterTransactionId((Integer)listObj[0]);
				districtUtilityMeter.setDistrictUtilityMeterId((Integer)listObj[1]);
				districtMeterTransaction.setCurrentReading((Integer)listObj[2]);
				districtMeterTransaction.setStartBillingDate((Date)listObj[3]);
				districtMeterTransaction.setEndBillingDate((Date)listObj[4]);
				districtUtilityMeter.setDistrictUtilityMeterSerialNumber(String.valueOf(listObj[5]));
				districtMeterTransaction.setDistrictUtilityMeter(districtUtilityMeter);
				districtMeterTransactionList.add(districtMeterTransaction);
			}
		}

		for (DistrictMeterTransaction districtMeterTransaction : districtMeterTransactionList) {
			long aggregateOfConMet = 0;

			List<Object> objList = reportDAO.getConsumerMeterTransactionByDistrictMeterId(districtMeterTransaction.getDistrictUtilityMeter().getDistrictUtilityMeterId(),customerId,districtMeterTransaction.getStartBillingDate(),districtMeterTransaction.getEndBillingDate());
			List startDateList = (List) objList.get(0);
			List endDateList = (List) objList.get(1);
			Map<String,BigInteger> startDateMap = new HashMap<String, BigInteger>();
			Map<String,BigInteger> endDateMap = new HashMap<String, BigInteger>();
			for(int i=0;i<startDateList.size();i++){
				Object[] listObjStartDate = (Object[]) startDateList.get(i);
				if(!(startDateMap.containsKey(listObjStartDate[0]))){
					startDateMap.put((String) listObjStartDate[0], (BigInteger)listObjStartDate[1]);
				}
			}
			for(int i=0;i<endDateList.size();i++){
				Object[] listObjEndDate = (Object[]) endDateList.get(i);
				if(!(endDateMap.containsKey(listObjEndDate[0]))){
					endDateMap.put((String)listObjEndDate[0], (BigInteger)listObjEndDate[1]);
				}
			}
			for(Map.Entry<String, BigInteger> entry: startDateMap.entrySet()){
				String registerId = entry.getKey();
				if(endDateMap.containsKey(registerId)){
					aggregateOfConMet = aggregateOfConMet + (endDateMap.get(registerId).longValue() - startDateMap.get(registerId).longValue());
					/*System.out.println(aggregateOfConMet);*/
				}
			}

			aggregateOfConMetList.add(aggregateOfConMet);
		}
		objectList.add(districtMeterTransactionList);
		objectList.add(aggregateOfConMetList);

		return objectList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getConsumerMeterAlerts(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, String alert,
			Integer siteId, String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1) {

		Instant periodStartDate = null;
		Instant periodEndDate = null;

		//Minus number of Weeks to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Week") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.WEEKS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Month") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.MONTHS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Select Dates") && startDate !=null && endDate !=null){
			periodStartDate = startDate.toInstant();
			periodEndDate = endDate.toInstant();
		}

		List<Object> searchedList = reportDAO.getConsumerMeterAlerts(alert,siteId,siteName,zipCode,customerId,consumerAccNo,address1,periodStartDate,periodEndDate);		

		if(searchedList!=null){
			List<ConsumerMeterTransaction> consumerAlertList = (List<ConsumerMeterTransaction>) searchedList.get(0);
			List<ConsumerAlertListModel> consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
			Iterator<ConsumerMeterTransaction> conMeterTranIterator = consumerAlertList.iterator();
			List<ConsumerMeterTransaction> consumerMeterTransactionList = new ArrayList<ConsumerMeterTransaction>();
			List<Object> objectList = new ArrayList<Object>();

			while(conMeterTranIterator.hasNext()){
				ConsumerMeterTransaction consumerMeterTransaction = conMeterTranIterator.next();
				consumerMeterTransactionList.add(consumerMeterTransaction);
			}
			objectList.add(consumerMeterTransactionList);
			objectList.add(consumerAlertModelList);
			return objectList;
		}

		return null;
	}


	@Override
	public List<DataCollectorAlerts> getNetworkAlerts(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, String alert,
			Integer siteId, String siteName, int customerId) {

		Instant periodStartDate = null;
		Instant periodEndDate = null;

		//Minus number of Weeks to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Week") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.WEEKS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Month") && reportPeriod!=null){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.MONTHS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Select Dates") && startDate !=null && endDate !=null){
			periodStartDate = startDate.toInstant();
			periodEndDate = endDate.toInstant();
		}

		List<DataCollectorAlerts> searchedList = reportDAO.getNetworkAlerts(alert,siteId,siteName,customerId,periodStartDate,periodEndDate);
		return searchedList;
	}


	@Override
	public List<DataCollector> getDCListForDataUsageReport(int customerId,Integer siteId, String siteName, String installationName, String dcSerial) {

		List<DataCollector> dcList = new ArrayList<DataCollector>();
		Region region = null;
		Site site = null;
		boolean siteExist = false;

		if(installationName!=null && !(installationName.trim().equals(""))){
			if(siteId != null || siteName != null){
				siteExist = true;
			}
			Customer customer = userDAO.getCustomerDetailsByCustomerId(customerId);

			region = userDAO.getRegionByName(customer,installationName);
			List<DataCollector> dcUsageList = null;

			if(region != null){
				List<Site> siteList = userDAO.getSiteListByRegion(region);
				Iterator<Site> siteIterator = siteList.iterator();
				while(siteIterator.hasNext()){
					site = siteIterator.next();
					if(siteExist && siteId.equals(site.getSiteId())){
						dcList = reportDAO.getDCListForDataUsageReport(customerId,siteId,siteName,installationName,dcSerial);
					}
					if(!siteExist){
						dcUsageList = reportDAO.getDCListForDataUsageReport(customerId,site.getSiteId(),siteName,installationName,dcSerial);
						if(dcUsageList != null && dcUsageList.size() > 0){
							Iterator<DataCollector> tempDataCollectorIterator = dcUsageList.iterator();
							while (tempDataCollectorIterator.hasNext()) {
								dcList.add(tempDataCollectorIterator.next());
							}
						}
					}
				}
			}
		}else{
			dcList = reportDAO.getDCListForDataUsageReport(customerId,siteId,siteName,installationName,dcSerial);
		}
		return dcList;
	}

	@Override
	public List callAbnormalSP(Customer customer,int spType){
		return reportDAO.callAbnormalSO(customer,spType);
	}

	@Override
	public List<AbnormalConsumptionModel> generateInitDataForAbnormalConsumption(Customer customer) {

		List<?> abnormalConsModelList =  callAbnormalSP(customer,7); // called SP with 7 day records
		Map<AbnormalConsumptionModel, List<BigInteger>> map = new HashMap<AbnormalConsumptionModel, List<BigInteger>>();

		if(abnormalConsModelList != null && !abnormalConsModelList.isEmpty()){
			List<BigInteger> mapList = null;
			for(int i=0;i<abnormalConsModelList.size();i++){
				Object[] listObj = (Object[]) abnormalConsModelList.get(i);
				AbnormalConsumptionModel abnormalConsObject =  new AbnormalConsumptionModel();
				abnormalConsObject.setConsumerId(String.valueOf(listObj[3]));
				abnormalConsObject.setRegisterId(String.valueOf(listObj[1]));

				if(map.containsKey(abnormalConsObject)){
					mapList = map.get(abnormalConsObject);
				}else{
					mapList = new ArrayList<BigInteger>();
				}
				mapList.add((BigInteger)listObj[0]);
				map.put(abnormalConsObject, mapList);
			}
		}
		List<AbnormalConsumptionModel> responseList = new ArrayList<AbnormalConsumptionModel>();

		for (Map.Entry<AbnormalConsumptionModel, List<BigInteger>> entry : map.entrySet()) {
			AbnormalConsumptionModel keyObj = entry.getKey();
			List<BigInteger> mapConsumptionList = entry.getValue();
			Consumer cons = userService.getConsumerByConsumerID(Integer.parseInt(keyObj.getConsumerId()));
			keyObj.setAverageConsumption(findAverageOfConsumptionList(mapConsumptionList));
			keyObj.setConsumerAccountNumber(cons.getConsumerAccountNumber());
			keyObj.setLast24hrUsage(0); // initally we make its value 0 and after another SP call its value updated
			responseList.add(keyObj);
		}

		List<?> last24hrConsumptionUsageList = callAbnormalSP(customer, 1);

		if(last24hrConsumptionUsageList != null && !last24hrConsumptionUsageList.isEmpty()){
			for(int i=0;i<last24hrConsumptionUsageList.size();i++){
				Object[] listObj = (Object[]) last24hrConsumptionUsageList.get(i);
				AbnormalConsumptionModel model = new AbnormalConsumptionModel();
				model.setRegisterId(String.valueOf(listObj[1]));
				model.setConsumerId(String.valueOf(listObj[2]));
				responseList.forEach(n ->{
					if(n.equals(model)){
						n.setLast24hrUsage(((BigInteger) listObj[0]).intValue());
					}
				});
				/*responseList.stream().filter(n -> n.equals(model)).findFirst().get().setLast24hrUsage(Integer.valueOf((Integer) listObj[0]));*/
			}
		}

		return responseList;
	}

	private int findAverageOfConsumptionList(List<BigInteger> mapConsumptionList) {
		int sum = mapConsumptionList.stream().filter(Objects::nonNull).mapToInt(n -> n.intValue()).sum();
		return (sum/mapConsumptionList.size());
	}

	@Override
	public StringBuilder generateActionReportDataForAbnormalConsumption(String registerID) {
		/*System.out.println("Printing here >>"+registerID);*/

		if(registerID != null){
			List<ConsumerMeterTransaction> transactionList =  reportDAO.getConsumerMeterTransactionByRegisterId(registerID);
			if(transactionList != null && !transactionList.isEmpty()){
				transactionList.sort((ConsumerMeterTransaction cm1,ConsumerMeterTransaction cm2) -> cm1.getTimeStamp().compareTo(cm2.getTimeStamp())); // Sorting list based on timestamp
				return generateGraphResponse(transactionList);
			}
		}
		return null;
	}

	private StringBuilder generateGraphResponse(List<ConsumerMeterTransaction> list){
		StringBuilder graphData = new StringBuilder();
		Calendar calender = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("HH.mm");
		graphData.append("[");
		for (int i = 0; i < list.size(); i++)
		{
			ConsumerMeterTransaction cmt = list.get(i);
			calender.setTime(cmt.getTimeStamp());
			if (i == list.size()-1)
			{
				graphData.append(" [\"" + format.format(calender.getTime()) + "\","  + cmt.getMeterReading() + "] ");
			}
			else
			{
				graphData.append(" [\"" + format.format(calender.getTime()) + "\"," + cmt.getMeterReading() + "], ");
			}
		}
		graphData.append("]");
		return graphData;
	}


	@Override
	public List<BillingHistory> getBillingHistoryList(Date billingStartDate,Date billingEndDate, int customerId, boolean onlyEstimatedReadAcc) {
		return reportDAO.getBillingHistoryList(billingStartDate, billingEndDate, customerId, onlyEstimatedReadAcc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAggregateConsumption(String reportType,
			Integer reportPeriod, Date startDate, Date endDate, Integer siteId,
			String siteName, String zipCode, int customerId,
			String consumerAccNo, String address1, Integer noOfOccupants) {

		Instant periodStartDate = null;
		Instant periodEndDate = null;

		//Minus number of Weeks to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Week")){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.WEEKS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Month")){
			periodStartDate = ZonedDateTime.now().minus(reportPeriod,ChronoUnit.MONTHS).toInstant();
			periodEndDate = Instant.now();
		}
		//Minus number of Months to the current date and calculate expected end date according to the value entered
		if(reportType!= null && reportType.equalsIgnoreCase("Select Dates")){
			periodStartDate = startDate.toInstant();
			periodEndDate = endDate.toInstant();
		}

		List<Object> searchedList = reportDAO.getAggregateConsumption(siteId,siteName,zipCode,customerId,consumerAccNo,address1,periodStartDate,periodEndDate,noOfOccupants);
		if(searchedList != null){
			return searchedList;		
		}
		return null;
	}

	@Override
	public Double getDataUsageByCustomer(Customer customer) {

		return reportDAO.getDataUsageByCustomer(customer);
	}

	@Override
	public List searchFreeReportData(Customer customer,String selectedField, String reportBy,
			String reportPeriod, Date startDate, Date endDate,
			String installatioName, String siteName, String dcSerialNumber) {

		List<?> dataList = null;
		List<JsonObject> responseList = new ArrayList<JsonObject>();

		if(selectedField != null && !selectedField.trim().isEmpty()){
			if(reportBy == null)
				reportBy = ApplicationConstants.DEFAULT;
			// Logic related setting of report By Week and Period
			if(reportBy.equalsIgnoreCase(ApplicationConstants.DEFAULT)){
				reportPeriod = "0D";
				startDate = endDate = null;
			}else if(reportBy.equalsIgnoreCase(ApplicationConstants.MONTH)){
				reportPeriod += "M";
				startDate = endDate = null;
			}else if(reportBy.equalsIgnoreCase(ApplicationConstants.WEEK)){
				reportPeriod += "W";
				startDate = endDate = null;
			}else if(reportBy.equalsIgnoreCase(ApplicationConstants.DATES)){
				reportPeriod = "0D";
			}else{
				/*System.out.println("Error");*/
			}
			dataList =  reportDAO.searchFreeReportData(customer, selectedField, reportPeriod, startDate, endDate, installatioName, siteName, dcSerialNumber);
			/*System.out.println(dataList.size());*/
			if(dataList != null && !dataList.isEmpty()){
				Object obj = dataList.get(0);
				if(obj instanceof DataCollector){
					dataList.stream().forEach(n -> {
						String locationString = "";
						JsonObject jObject = new JsonObject();
						DataCollector dcObj = ((DataCollector)n);

						// initialize all property with - 
						jObject.addProperty("dcSerialNumber", "-");
						jObject.addProperty("location", "-");
						jObject.addProperty("simNumber", "-");
						jObject.addProperty("siteName", "-");
						jObject.addProperty("installerName", "-");

						if(dcObj.getDcSerialNumber() != null)
							jObject.addProperty("dcSerialNumber", dcObj.getDcSerialNumber());
						if(dcObj.getLatitude() != null)
							locationString = dcObj.getLatitude().toString()+"         ";
						else
							locationString = "-"+"         ";
						if(dcObj.getLongitude() != null)
							locationString = locationString +dcObj.getLongitude().toString();
						else
							locationString = locationString + "-";
						jObject.addProperty("location", locationString );
						if(dcObj.getDcSimcardNo() != null)
							jObject.addProperty("simNumber", dcObj.getDcSimcardNo());
						if(dcObj.getSite().getSiteName() != null)
							jObject.addProperty("siteName", dcObj.getSite().getSiteName());
						if(dcObj.getSite().getRegion().getRegionName() != null)
							jObject.addProperty("installerName", dcObj.getSite().getRegion().getRegionName());
						responseList.add(jObject);
					});
				}else if(obj instanceof ConsumerMeter){
					dataList.stream().forEach(n ->{
						String locationString = "";
						ConsumerMeter dcObj = ((ConsumerMeter)n);
						JsonObject jObject = new JsonObject();
						
						jObject.addProperty("siteName", "-");
						jObject.addProperty("consumerName", "-");
						jObject.addProperty("tariffName", "-");
						jObject.addProperty("epSerailnumber", "-");
						jObject.addProperty("location", "-");
						
						if(dcObj.getSite().getSiteName() != null)
							jObject.addProperty("siteName", dcObj.getSite().getSiteName());
						jObject.addProperty("consumerName", dcObj.getConsumer().getUser().getDetails().getFirstName()+" "+dcObj.getConsumer().getUser().getDetails().getLastname());
						if(dcObj.getTariffPlan().getTarrifPlanName() != null)
							jObject.addProperty("tariffName", dcObj.getTariffPlan().getTarrifPlanName());
						if(dcObj.getEndpointSerialNumber() != null)
							jObject.addProperty("epSerailnumber", dcObj.getEndpointSerialNumber());
						if(dcObj.getLatitude() != null)
							locationString = dcObj.getLatitude().toString()+"         ";
						else
							locationString = "-"+"         ";
						if(dcObj.getLongitude() != null)
							locationString = locationString +dcObj.getLongitude().toString();
						else
							locationString = locationString + "-";
						jObject.addProperty("location", locationString );
						responseList.add(jObject);
					});
				}else if(obj instanceof Site){
					dataList.stream().forEach(n ->{
						Site site = (Site) n;
						JsonObject jObject = new JsonObject();
						
						jObject.addProperty("name", "-");
						jObject.addProperty("region", "-");
						jObject.addProperty("commissioningType", "-");
						jObject.addProperty("status", "-");
						
						if(site.getSiteName() != null)
							jObject.addProperty("name", site.getSiteName());
						if(site.getRegion().getRegionName() != null)
							jObject.addProperty("region", site.getRegion().getRegionName());
						if(site.getCommissioningType() != null)
							jObject.addProperty("commissioningType", site.getCommissioningType());
						if(site.getCurrentStatus() != null)
							jObject.addProperty("status", site.getCurrentStatus());
						responseList.add(jObject);
					});
				}else if(obj instanceof Installer){
					dataList.stream().forEach(n ->{
						Installer ins = (Installer)n;
						JsonObject jObject = new JsonObject();
						jObject.addProperty("installerName", ins.getInstallerName());
						jObject.addProperty("name", ins.getUser().getDetails().getFirstName());
						
						if(ins.getInstallerName() != null)
							jObject.addProperty("installerName", ins.getInstallerName());
						if(ins.getUser().getDetails().getFirstName() != null)
							jObject.addProperty("name", ins.getUser().getDetails().getFirstName());
						
						responseList.add(jObject);
					});
				}else if(obj instanceof Consumer){
					dataList.stream().forEach(n ->{
						Consumer cons = (Consumer)n;
						JsonObject jObject = new JsonObject();
						
						jObject.addProperty("consumerAccountNumber", "-");
						jObject.addProperty("firstName", "-");
						jObject.addProperty("lastName", "-");
						jObject.addProperty("address", "-");
						jObject.addProperty("streetName", "-");
						jObject.addProperty("zip", "-");
						
						if(cons.getConsumerAccountNumber() != null)
							jObject.addProperty("consumerAccountNumber", cons.getConsumerAccountNumber());
						if(cons.getUser().getDetails().getFirstName() != null)
							jObject.addProperty("firstName", cons.getUser().getDetails().getFirstName());
						if(cons.getUser().getDetails().getLastname() != null)
							jObject.addProperty("lastName", cons.getUser().getDetails().getLastname());
						if(cons.getUser().getDetails().getAddress1() != null)
							jObject.addProperty("address", cons.getUser().getDetails().getAddress1());
						if(cons.getUser().getDetails().getStreetName() != null)
							jObject.addProperty("streetName", cons.getUser().getDetails().getStreetName());
						if(cons.getUser().getDetails().getZipcode() != null)
							jObject.addProperty("zip", cons.getUser().getDetails().getZipcode());
						
						responseList.add(jObject);
					});
				}
			}else{
				// Empty list
			}

		}else{

		}
		return responseList;

	}

	@Override
	public void initalizeSiteByregion(Region n) {
		reportDAO.initalizeSiteByregion(n);
	}

	@Override
	public String findSameTariffBasedOnRegion(Region region,Site site,Customer customer) {

		if(site == null && region != null){
			Map<String, Integer> map = new HashMap<String, Integer>();
			reportDAO.initalizeSiteByregion(region);
			List<Site> siteList = new ArrayList<Site>(region.getSite());
			siteList.stream().forEach(streamSite -> {
				List<DataCollector> dcList = new ArrayList<>(streamSite.getDatacollector());
				dcList.stream().forEach(dc ->{
					List<ConsumerMeter> consumerMeterList = new ArrayList<>(dc.getConsumerMeters());
					consumerMeterList.stream().forEach(consumerMeter -> {
						if(consumerMeter.getTariffPlan() != null){
							System.out.println("Printing Meter Id >"+consumerMeter.getConsumerMeterId());
							if(map.containsKey(consumerMeter.getTariffPlan().getTarrifPlanName())){
								map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), 
										map.get(consumerMeter.getTariffPlan().getTarrifPlanName())+1);
							}else{
								map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), 
										1);
							}
						}
					});	
				});
			});
			String response = "";
			if(map.size() > 1){
				log.info("Not efficient search related to Region .. Have to search By Site");
				response = "noUniqueTariff";
			}else if(map.size() == 1){
				// all consumer meter have same tariff plan under this region
				// get tariff and its threshold by its id
				String tariffName = "";
				for (Map.Entry<String, Integer> entry : map.entrySet()){
					tariffName = entry.getKey();
				}
				response = "found:"+tariffName;
			}else {
				// No ConsumerMeter allocated under this region !!
				response = "noSiteUnderRegion";
			}
			return response;
		}else if(site != null && region == null){
			Map<String, Integer> map = new HashMap<String, Integer>();
			reportDAO.initializeDatacollectorBySite(site);
			site.getDatacollector().stream().forEach(dc ->{
				/*System.out.println("Dc id is >"+dc.getDatacollectorId());*/
				List<ConsumerMeter> consumerMeterList = new ArrayList<>(dc.getConsumerMeters());
				consumerMeterList.stream().forEach(consumerMeter -> {
					if(consumerMeter.getTariffPlan() != null){
						if(map.containsKey(consumerMeter.getTariffPlan().getTarrifPlanName())){
							map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), 
									map.get(consumerMeter.getTariffPlan().getTarrifPlanName())+1);
						}else{
							map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), 
									1);
						}
					}
				});	
			});
			/*System.out.println("Size >>"+site.getDatacollector().size());*/
			String response="";
			if(map.size() > 1){
				response = "noUniqueTariff";
				log.error("No need to display this report ... no unique tariff found under {} site",site.getSiteName());
			}else if(map.size() == 1){
				String tariffName = "";
				for (Map.Entry<String, Integer> entry : map.entrySet()){
					tariffName = entry.getKey();
				}
				response = "found:"+tariffName;
				log.info("Common tariff found under {} site",site.getSiteName());
			}else{
				response = "notAnyConsumerAllocated";
			}
			return response;
		}else
			return null;
	}

	@Override
	public TariffModel getTariffModelByTariffName(String tariffName,Customer customer) {

		/*System.out.println("TariffName is >"+tariffName);*/
		TariffModel tariffArray = new TariffModel();
		tariffArray.setTariffName(tariffName);
		List<TariffTransactionModel> tariffTransactionList = new ArrayList<TariffTransactionModel>();
		if(!tariffName.isEmpty()){
			TariffPlan tp = userDAO.getTariffByCustomerAndTariffName(customer,tariffName);
			tp.getTariffTransaction().forEach(n -> {
				TariffTransactionModel tModel = new TariffTransactionModel();
				tModel.setCost(n.getRate());
				tModel.setEnd(n.getEndBand());
				tModel.setStart(n.getStartBand());
				tariffTransactionList.add(tModel);
			});
			tariffTransactionList.sort((TariffTransactionModel t1,TariffTransactionModel t2) -> t1.getStart() - t2.getStart() );
			tariffArray.setTariffArray(tariffTransactionList);
		}
		/*System.out.println("Size >> "+tariffTransactionList.size());*/
		return tariffArray;
	}

	@Override
	public Double getRevenueForIndividualConsumerMeter(
			ConsumerMeter consumerMeter, Date startLimitDate, Date endDate) {

		Calendar calender = Calendar.getInstance();
		calender.setTime(startLimitDate);
		calender.set(Calendar.DAY_OF_MONTH, 1);

		Double revenue = reportDAO.getRevenueForIndividualConsumerMeter(consumerMeter,calender.getTime(),endDate);

		return revenue;
	}

	@Override
	public void initalizeDCBySite(Site site) {
		reportDAO.initializeDatacollectorBySite(site);
	}

	@Override
	public void initializeConsumerMeterByDC(DataCollector dc) {
		reportDAO.initializeConsumerMeterByDC(dc);
	}

	@Override
	public Double getUsageForIndividualConsumerMeter(ConsumerMeter cm,
			Date startLimitDate, Date endDate, List<TariffTransactionModel> model,Integer consumptionVar) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(startLimitDate);
		calender.set(Calendar.DAY_OF_MONTH, 1);

		List<BillingHistory> list = reportDAO.getUsageForIndividualConsumerMeter(cm,calender.getTime(),endDate);
		List<BillingHistory> newList = null;
		if(consumptionVar != null && consumptionVar != 0){
			newList = new ArrayList<BillingHistory>(list.size());

			for(BillingHistory history : list){
				newList.add(history);
			}

			for(BillingHistory bg : newList){
				int difference = Math.abs(bg.getCurrentReading() - bg.getLastReading()) ;
				int incrementBasedOnVar = (int) (difference * consumptionVar * 0.01);
				bg.setConsumedUnit(bg.getConsumedUnit() + incrementBasedOnVar);
			}
		}
		Double revenue = 0.0D;	
		if(newList != null){
			for(int i=0;i<newList.size();i++){
				BillingHistory bg = newList.get(i);
				for(int j=0;j<model.size();j++){
					if(betweenTwoValue(bg.getConsumedUnit(), model.get(j).getStart(), model.get(j).getEnd()))
						revenue += (bg.getConsumedUnit() * model.get(j).getCost().doubleValue());
				}
			}
		}else{
			for(int i=0;i<list.size();i++){
				BillingHistory bg = list.get(i);
				for(int j=0;j<model.size();j++){
					if(betweenTwoValue(bg.getConsumedUnit(), model.get(j).getStart(), model.get(j).getEnd())){
						revenue += (bg.getConsumedUnit() * model.get(j).getCost().doubleValue());
						break;
					}	
				}
			}
		}

		return revenue;
	}

	private boolean betweenTwoValue(int value,int lower, int upper){
		return lower <= value && value <= upper;
	}

	@Override
	public Double getRevenueByConsumerList(List<Consumer> consumerList,Date startDate,Date endDate) {
		int loop,size=consumerList.size();
		Double totalRevenue = 0.0D;
		for(loop=0;loop<size;loop++){
			Consumer cm = consumerList.get(loop);
			reportDAO.initalizeConsumerMeter(cm);
			for(ConsumerMeter consumerMeter : cm.getConsumerMeter()){
				totalRevenue +=	getRevenueForIndividualConsumerMeter(consumerMeter, startDate, endDate);
			}
		}
		/*System.out.println("Total Revenue >> "+totalRevenue);*/
		return totalRevenue;
	}

	@Override
	public void initializeConsumerByCustomer(Customer customer) {
		reportDAO.initializeConsumerByCustomer(customer);
	}

	@Override
	public void initializeRegionByCustomer(Customer customer) {
		reportDAO.initializeRegionByCustomer(customer);
	}

	@Override
	public void initializeConsumerMeterByConsumer(Consumer consumer) {
		reportDAO.initializeConsumerMeterByConsumer(consumer);
	}

}
