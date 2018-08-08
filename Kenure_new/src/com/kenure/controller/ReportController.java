package com.kenure.controller;

import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.BillingHistory;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.DistrictMeterTransaction;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.entity.User;
import com.kenure.model.AbnormalConsumptionModel;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.model.TariffModel;
import com.kenure.model.WhatIfTariffModel;
import com.kenure.service.IConsumerMeterService;
import com.kenure.service.IConsumerMeterTransactionService;
import com.kenure.service.IReportService;
import com.kenure.service.IUserService;
import com.kenure.service.IVPNService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;


/**
 * 
 * @author TatvaSoft
 *
 */
@Controller
@RequestMapping(value="/customerOperation/reportsOperation")
public class ReportController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IConsumerMeterTransactionService cmtService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private IVPNService vpnService;

	@Autowired
	private IConsumerMeterService consumerMeterService;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerUtils.getInstance(ReportController.class);


	// variable declarations
	List<String> regionNameList = null;
	Double totalRevenue = 0.0D;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchNetworkWaterLoss",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchNetworkWaterLoss(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="startDate",required=true) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=true) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="acceptableLoss",required=false) Integer acceptableLoss){

		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();
		List<JsonObject> networkWaterLossList = new ArrayList<JsonObject>();
		List<Object> objectList = reportService.getNetworkWaterLoss(startDate,endDate,customerId);
		List<DistrictMeterTransaction> districtMeterTransactionList = (List<DistrictMeterTransaction>) objectList.get(0);
		List<Long> aggregateConMetList = (List<Long>) objectList.get(1);
		if(districtMeterTransactionList!=null && districtMeterTransactionList.size() > 0){
			for (int i =0;i<districtMeterTransactionList.size();i++) {

				DistrictMeterTransaction districtMeterTransaction = districtMeterTransactionList.get(i);
				JsonObject  json = new JsonObject();
				json.addProperty("districtMeterSerialNo", districtMeterTransaction.getDistrictUtilityMeter().getDistrictUtilityMeterSerialNumber());
				json.addProperty("districtMeterReading", districtMeterTransaction.getCurrentReading());
				json.addProperty("billingStartDate", sdf.format(districtMeterTransaction.getStartBillingDate()));
				json.addProperty("billingEndDate", sdf.format(districtMeterTransaction.getEndBillingDate()));
				json.addProperty("totalConsumerConsumption", aggregateConMetList.get(i));
				Long waterLoss = (long) (districtMeterTransaction.getCurrentReading()-aggregateConMetList.get(i));
				json.addProperty("waterLoss",waterLoss);
				if(acceptableLoss!=null){
					if(acceptableLoss < waterLoss){
						double percentage = (100*(waterLoss - acceptableLoss))/acceptableLoss;
						json.addProperty("percentage",percentage);
					}
				}
				networkWaterLossList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(networkWaterLossList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("noNetworkWaterLossFound"), HttpStatus.OK);
	}

	//Method to search and generate consumer meter alerts report
	//Method to search and generate consumer meter alerts report
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/generateAlertReportForConsumerMeter",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateAlertReportForConsumerMeter(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String reportType,
			@RequestParam(value="reportPeriod",required=false) Integer reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="alerts",required=false) String alert,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="installationName",required=false) String installationName,
			@RequestParam(value="zipCode",required=false) String zipCode,
			@RequestParam(value="consumerAccNo",required=false) String consumerAccNo,
			@RequestParam(value="address1",required=false) String address1){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();

		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		List<Object> searchedList = new ArrayList<Object>();
		List<JsonObject> consumerMeterAlertsList = new ArrayList<JsonObject>();
		List<ConsumerAlertListModel> consumerAlertModelList =  null; 
		List<ConsumerMeterTransaction> consumerMeterTransactionList = new ArrayList<ConsumerMeterTransaction>();

		Region region = null;
		Site site = null;
		boolean siteExist = false;
		if(installationName!=null && !(installationName.trim().equals(""))){
			if(siteId != null || siteName != null){
				siteExist = true;
			}
			region = userService.getRegionByName(customer,installationName);

			List<Object> tempConsumerMeterAlertsList = null;
			List<ConsumerMeterTransaction> consumerMeterTransactions = null;

			if(region != null){
				List<Site> siteList = userService.getSiteListByRegion(region);
				Iterator<Site> siteIterator = siteList.iterator();
				while(siteIterator.hasNext()){
					site = siteIterator.next();
					if(siteExist && siteId.equals(site.getSiteId())){
						searchedList = reportService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,alert,siteId,siteName,zipCode,customerId,consumerAccNo,address1);
						consumerMeterTransactionList = (List<ConsumerMeterTransaction>) searchedList.get(0);
						consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
					}
					if(!siteExist){
						tempConsumerMeterAlertsList = reportService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,alert,site.getSiteId(),siteName,zipCode,customerId,consumerAccNo,address1);
						if(tempConsumerMeterAlertsList != null){
							consumerMeterTransactions = (List<ConsumerMeterTransaction>) tempConsumerMeterAlertsList.get(0);
							consumerAlertModelList = (List<ConsumerAlertListModel>) tempConsumerMeterAlertsList.get(1);
						}
						if(consumerMeterTransactions != null && consumerMeterTransactions.size() > 0){
							Iterator<ConsumerMeterTransaction> tempConsumerMeterAlertsIterator = consumerMeterTransactions.iterator();
							while (tempConsumerMeterAlertsIterator.hasNext()) {
								consumerMeterTransactionList.add(tempConsumerMeterAlertsIterator.next());
							}
						}

					}
				}
			}
		}else{
			searchedList = reportService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,alert,siteId,siteName,zipCode,customerId,consumerAccNo,address1);
			if(searchedList != null){
				consumerMeterTransactionList = (List<ConsumerMeterTransaction>) searchedList.get(0);
				consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
			}
		}

		if(consumerMeterTransactionList!= null && consumerMeterTransactionList.size() > 0){

			Iterator<ConsumerMeterTransaction> consumerMeterTransactionIterator = consumerMeterTransactionList.iterator();

			while(consumerMeterTransactionIterator.hasNext()){
				JsonObject json = new JsonObject();
				Iterator<ConsumerAlertListModel> consumerAlertModelListIterator = consumerAlertModelList.iterator();
				ConsumerMeterTransaction conMeterTransaction = consumerMeterTransactionIterator.next();
				while(consumerAlertModelListIterator.hasNext()){
					ConsumerAlertListModel conAlertObj = consumerAlertModelListIterator.next();
					if(conAlertObj.getRegisterId().equals(conMeterTransaction.getRegisterId())){
						json.addProperty("consumerAccNo", conAlertObj.getConsumerAccountNumber());
						if(conAlertObj.getZipcode() != null){
							json.addProperty("zipcode",conAlertObj.getZipcode());
						}
						else{
							json.addProperty("zipcode","-");
						}
						json.addProperty("siteId", conAlertObj.getSiteId());
					}
				}
				if(conMeterTransaction.getAlerts()!=null){
					json.addProperty("alert", conMeterTransaction.getAlerts());
					json.addProperty("dateFlagged",sdf.format(conMeterTransaction.getTimeStamp()));
					consumerMeterAlertsList.add(json);
				} 
			}
			if(consumerMeterAlertsList.size() > 0){
				return new ResponseEntity<String>(new Gson().toJson(consumerMeterAlertsList), HttpStatus.OK);
			}
		}  
		return new ResponseEntity<String>(new Gson().toJson("noConsumerMeterAlertsFound"), HttpStatus.OK);
	}	
	//Method to search and generate network alerts report
	//Method to search and generate network alerts report
	@RequestMapping(value="/generateAlertReportForNetwork",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateAlertReportForNetwork(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String reportType,
			@RequestParam(value="reportPeriod",required=false) Integer reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="alerts",required=false) String alert,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="installationName",required=false) String installationName){

		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();
		List<DataCollectorAlerts> dataCollectorList = new ArrayList<DataCollectorAlerts>();
		List<JsonObject> networkAlertsList = new ArrayList<JsonObject>();
		Region region = null;
		Site site = null;
		boolean siteExist = false;
		if(installationName!=null && !(installationName.trim().equals(""))){
			if(siteId != null || siteName != null){
				siteExist = true;
			}
			region = userService.getRegionByName(customer,installationName);
			List<DataCollectorAlerts> tempDataCollectorList = null;

			if(region != null){
				List<Site> siteList = userService.getSiteListByRegion(region);
				Iterator<Site> siteIterator = siteList.iterator();
				while(siteIterator.hasNext()){
					site = siteIterator.next();
					if(siteExist && siteId.equals(site.getSiteId())){
						dataCollectorList = reportService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,alert,siteId,siteName,customerId);
					}
					if(!siteExist){
						tempDataCollectorList = reportService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,alert,site.getSiteId(),siteName,customerId);
						if(tempDataCollectorList != null && tempDataCollectorList.size() > 0){
							Iterator<DataCollectorAlerts> tempDataCollectorIterator = tempDataCollectorList.iterator();
							while (tempDataCollectorIterator.hasNext()) {
								dataCollectorList.add(tempDataCollectorIterator.next());
							}
						}
					}
				}
			}
		}else{
			dataCollectorList = reportService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,alert,siteId,siteName,customerId);
		}

		if(dataCollectorList!=null && dataCollectorList.size() > 0){
			Iterator<DataCollectorAlerts> dataCollectorIterator = dataCollectorList.iterator();
			while(dataCollectorIterator.hasNext()){
				DataCollectorAlerts dataCollectorAlerts = dataCollectorIterator.next();
				JsonObject  json = new JsonObject();
				if(dataCollectorAlerts.getAlert()!=null){
					json.addProperty("siteId", dataCollectorAlerts.getDataCollector().getSite().getSiteId());
					json.addProperty("dcSerialNo", dataCollectorAlerts.getDataCollector().getDcSerialNumber());
					json.addProperty("alert", dataCollectorAlerts.getAlert());
					json.addProperty("dateFlagged", sdf.format(dataCollectorAlerts.getAlertDate()));
					networkAlertsList.add(json);
				}
			}
			return new ResponseEntity<String>(new Gson().toJson(networkAlertsList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("noNetworkAlertsFound"), HttpStatus.OK);
	}

	// Dhaval Code Start

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/generateAggregateConsumptionReport",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateAggregateConsumptionReport (HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String reportType,
			@RequestParam(value="reportPeriod",required=false) Integer reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="installationName",required=false) String installationName,
			@RequestParam(value="zipCode",required=false) String zipCode,
			@RequestParam(value="consumerAccNo",required=false) String consumerAccNo,
			@RequestParam(value="address1",required=false) String address1,
			@RequestParam(value="noOfOccupants",required=false) Integer noOfOccupants){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();
		List<JsonObject> aggregateConsumptionList = new ArrayList<JsonObject>();
		List<Object> consumerMeterTransactionList = new ArrayList<Object>();

		Region region = null;
		Site site = null;
		boolean siteExist = false;
		if(installationName!=null && !(installationName.trim().equals(""))){
			if(siteId != null || siteName != null){
				siteExist = true;
			}
			region = userService.getRegionByName(customer,installationName);

			List<Object> tempConsumerMeterAlertsList = null;

			if(region != null){
				List<Site> siteList = userService.getSiteListByRegion(region);
				Iterator<Site> siteIterator = siteList.iterator();
				while(siteIterator.hasNext()){
					site = siteIterator.next();
					if(siteExist && siteId.equals(site.getSiteId())){
						consumerMeterTransactionList = reportService.getAggregateConsumption(reportType,reportPeriod,startDate,endDate,siteId,siteName,zipCode,customerId,consumerAccNo,address1,noOfOccupants);
					}
					if(!siteExist){
						tempConsumerMeterAlertsList = reportService.getAggregateConsumption(reportType,reportPeriod,startDate,endDate,site.getSiteId(),siteName,zipCode,customerId,consumerAccNo,address1,noOfOccupants);
						if(tempConsumerMeterAlertsList != null && tempConsumerMeterAlertsList.size() > 0){
							Iterator<Object> tempConsumerMeterAlertsIterator = tempConsumerMeterAlertsList.iterator();
							while (tempConsumerMeterAlertsIterator.hasNext()) {
								consumerMeterTransactionList.add(tempConsumerMeterAlertsIterator.next());
							}
						}
					}
				}
			}
		}else{
			consumerMeterTransactionList = reportService.getAggregateConsumption(reportType,reportPeriod,startDate,endDate,siteId,siteName,zipCode,customerId,consumerAccNo,address1,noOfOccupants);
		}

		if(consumerMeterTransactionList!= null && consumerMeterTransactionList.size() > 0){

			for(int i=0;i<consumerMeterTransactionList.size();i++){
				List<Object> objList= (List<Object>) consumerMeterTransactionList.get(i);
				if(objList.size() > 0){
					Object[] respList = (Object[]) objList.get(0);
					JsonObject json = new JsonObject();

					json.addProperty("consumerAccNo", respList[0].toString());
					json.addProperty("totalConsumption", respList[1].toString());
					aggregateConsumptionList.add(json);
				}
			}

			if(aggregateConsumptionList.size() > 0){
				return new ResponseEntity<String>(new Gson().toJson(aggregateConsumptionList), HttpStatus.OK);
			}
		}  
		return new ResponseEntity<String>(new Gson().toJson("noSuchRecordsFound"), HttpStatus.OK);
	}

	@RequestMapping(value = "/initDataUsageReportData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initDataUsageReportData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Date dataPlanStartDate = customer.getDataPlanActivatedDate();
		Date dataPlanEndDate = customer.getDataPlanExpiryDate();
		
		Calendar startDate = Calendar.getInstance();
		Calendar currentDate = Calendar.getInstance();
		Date today = new Date();
		currentDate.setTime(today);
	    Calendar endDate = Calendar.getInstance();
	    startDate.setTime(dataPlanStartDate);
	    endDate.setTime(dataPlanEndDate);
	   
	    int totalDuration = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
	    if (startDate.get(Calendar.MONTH) > endDate.get(Calendar.MONTH) || 
	        (startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) && startDate.get(Calendar.DATE) > endDate.get(Calendar.DATE))) {
	    	totalDuration --;
	    }
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    
	    Long diffInDays = null;
	    if(currentDate.after(startDate) && currentDate.before(endDate)){
	    	
	    	for(int i=1;i<=totalDuration;i++){

	    		Date tempYear = dataPlanStartDate;
	    		Calendar tempDate = Calendar.getInstance();
	    		tempDate.setTime(tempYear);
	    		tempDate.add(Calendar.YEAR,i);
	    		
	    		if(currentDate.after(startDate) && currentDate.before(tempDate)){
	    			long miliSecondForDate1 = startDate.getTimeInMillis();
	    			long miliSecondForDate2 = currentDate.getTimeInMillis();
	    			
	    			long diffInMilis = miliSecondForDate2 - miliSecondForDate1;
	    			
	    			diffInDays = diffInMilis / (24 * 60 * 60 * 1000);
	    			
	    			System.out.println("Days : " + diffInDays);
	    			
	    			break;
	    		}
	    		
	    		//System.out.println("Value of i : " +i);
	    		//System.out.println("Start Date : " + startDate.getTime() + "\n EndDate : " + tempDate.getTime());
	    		startDate = tempDate;
	    	}
	    }
	    
	    if(sdf.format(dataPlanStartDate).equalsIgnoreCase(sdf.format(today)) || sdf.format(dataPlanEndDate).equalsIgnoreCase(sdf.format(today))){
	    	diffInDays = (long) 1;
	    }
	    
	    if(diffInDays != null){
		    Integer usageDays = diffInDays.intValue();
		    Double totalUsage = reportService.getDataUsageByCustomer(customer);
		    Double actualUsage = null;
		    
		    if(usageDays != null){
		    	actualUsage = (usageDays*totalUsage)/365;
		    }
		    
			DecimalFormat df = new DecimalFormat("#.00");
	
			int dataPlan = customer.getDataPlan().getMbPerMonth();
			Double usagePer = null;
	
			if(actualUsage != null && actualUsage > -1){
				usagePer = (actualUsage/dataPlan)*100;
			}
	
			JsonObject json = new JsonObject();
			json.addProperty("dataPlan", dataPlan);
			json.addProperty("usagePer", df.format(usagePer));
			json.addProperty("totalUsage", totalUsage);
			
			return new ResponseEntity<String>(new Gson().toJson(json), HttpStatus.OK);
	    }else{
	    	return new ResponseEntity<String>(new Gson().toJson("dataPlanExpired"), HttpStatus.OK);
	    }
	}

	@RequestMapping(value = "/searchDataUsageReport",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchDataUsageReport(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId",required=false) String siteIdInput,
			@RequestParam(value="siteName",required=false) String siteNameInput,
			@RequestParam(value="installationName",required=false) String installationNameInput,
			@RequestParam(value="dcSerial",required=false) String dcSerialInput){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Integer siteId = null;
		String installationName = null, siteName = null, dcSerial = null ;

		if(siteIdInput != null && siteIdInput.trim() != "")
			siteId = Integer.parseInt(siteIdInput);

		if(siteNameInput != null && siteNameInput.trim() != "")
			siteName = siteNameInput;

		if(installationNameInput != null && installationNameInput.trim() != "")
			installationName = installationNameInput;

		if(dcSerialInput != null && dcSerialInput.trim() != "")
			dcSerial = dcSerialInput;

		List<Object> objectList = new ArrayList<Object>();
		List<DataCollector> dataCollectorsList = new ArrayList<DataCollector>();

		dataCollectorsList = reportService.getDCListForDataUsageReport(customer.getCustomerId(),siteId,siteName,installationName,dcSerial);
		List<JsonObject> dcJsonObjects = new ArrayList<JsonObject>();

		if(dataCollectorsList != null && dataCollectorsList.size() > 0){

			Iterator<DataCollector> dcListIterator = dataCollectorsList.iterator();
			while(dcListIterator.hasNext()){

				DataCollector dataCollector = dcListIterator.next();

				JsonObject  json = new JsonObject();
				json.addProperty("dcSerialNumber", dataCollector.getDcSerialNumber());
				json.addProperty("dcDataUsage", dataCollector.getMbPerMonth());

				dcJsonObjects.add(json);
			}
			objectList.add(dcJsonObjects);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);

		}
		return new ResponseEntity<String>(new Gson().toJson("nosuchdcusagefound"), HttpStatus.OK);
	}

	@RequestMapping(value = "/initBillingDataReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initBillingDataReport(HttpServletRequest request, HttpServletResponse response) {
		return new ResponseEntity<String>(new Gson().toJson(KenureUtilityContext.billingColumnFields), HttpStatus.OK);
	}

	@RequestMapping(value = "/searchBillingDataReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchBillingDataReport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="billingStartDate",required=true) @DateTimeFormat(pattern = "dd/MM/yyyy") Date billingStartDate,
			@RequestParam(value="billingEndDate",required=true) @DateTimeFormat(pattern = "dd/MM/yyyy") Date billingEndDate,
			@RequestParam(value="onlyEstimatedReadAcc",required=false) boolean onlyEstimatedReadAcc) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
		List<BillingHistory> billingHistories = reportService.getBillingHistoryList(billingStartDate,billingEndDate,customer.getCustomerId(),onlyEstimatedReadAcc);

		if(billingHistories != null && billingHistories.size() > 0){

			Iterator<BillingHistory> billingDataIterator = billingHistories.iterator();
			while(billingDataIterator.hasNext()){

				BillingHistory billingHistory = billingDataIterator.next();

				JsonObject  json = new JsonObject();
				json.addProperty("isEstimated", "-");
				json.addProperty("consumerAccNum", "-");
				json.addProperty("registerId", "-");
				json.addProperty("billingFrequency", "-");
				json.addProperty("billingStartDate", "-");
				json.addProperty("billingEndDate", "-");
				json.addProperty("currentReading", "-");
				json.addProperty("lastReading", "-");
				json.addProperty("consumedUnit", "-");
				json.addProperty("totalAmount", "-");
				json.addProperty("billDate", "-");

				if(billingHistory.getIsEstimated())
					json.addProperty("isEstimated", "Yes");
				if(billingHistory.getConsumerMeter().getConsumer().getConsumerAccountNumber() != null)
					json.addProperty("consumerAccNum", billingHistory.getConsumerMeter().getConsumer().getConsumerAccountNumber());
				if(billingHistory.getConsumerMeter().getRegisterId() != null)	
					json.addProperty("registerId", billingHistory.getConsumerMeter().getRegisterId());
				if(billingHistory.getConsumerMeter().getBillingFrequencyInDays() != null)	
					json.addProperty("billingFrequency", billingHistory.getConsumerMeter().getBillingFrequencyInDays());
				if(billingHistory.getBillingStartDate().toString() != null)
					json.addProperty("billingStartDate", billingHistory.getBillingStartDate().toString().substring(0, 10));
				if(billingHistory.getBillingEndDate().toString() != null)
					json.addProperty("billingEndDate", billingHistory.getBillingEndDate().toString().substring(0, 10));
				if(billingHistory.getCurrentReading() != null)
					json.addProperty("currentReading", billingHistory.getCurrentReading());
				if(billingHistory.getLastReading() != null)
					json.addProperty("lastReading", billingHistory.getLastReading());
				if(billingHistory.getConsumedUnit() != null)
					json.addProperty("consumedUnit", billingHistory.getConsumedUnit());
				if(billingHistory.getTotalAmount() != null)
					json.addProperty("totalAmount", billingHistory.getTotalAmount());
				if(billingHistory.getBillDate().toString() != null)
					json.addProperty("billDate", billingHistory.getBillDate().toString().substring(0, 10));

				jsonObjects.add(json);
			}
			objectList.add(jsonObjects);
			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
	}

	// Dhaval Code End

	@RequestMapping(value = "/initNetworkConsumption", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initNetworkConsumption(HttpServletRequest request,
			HttpServletResponse response) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();
		for (DistrictUtilityMeter duMeter : customer.getDistrictUtilityMeter()) {
			JsonObject json = new JsonObject();
			json.addProperty("duMeterId", duMeter.getDistrictUtilityMeterId());
			json.addProperty("duMeterSerialNo", duMeter.getDistrictUtilityMeterSerialNumber());
			responselist.add(json);
		}
		return new ResponseEntity<String>(new Gson().toJson(responselist), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getNWConsumptionGraph",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getNWConsumptionGraph(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String duMeterId, @RequestParam String periodType, @RequestParam String periodTime){

		List nwConsumptionList = cmtService.getNWConsumptionCMTList(periodTime, periodType, duMeterId);
		StringBuilder graphData = new StringBuilder();

		graphData.append("[");
		for (int i = 0; i < nwConsumptionList.size(); i++)
		{
			Object[] row = (Object[]) nwConsumptionList.get(i);

			if(periodType != null && periodType.equals("month")) {
				BigInteger b = (BigInteger) row[3];
				row[3] = new DateFormatSymbols().getMonths()[ b.intValue() - 1];
			}

			if (i == nwConsumptionList.size()-1)
			{
				graphData.append(" [\"" + row[3] + "\","  + row[2] + "] ");
			}
			else
			{
				graphData.append(" [\"" + row[3] + "\"," + row[2] + "], ");
			}
		}
		graphData.append("]");

		return new ResponseEntity<String>(new Gson().toJson(graphData),HttpStatus.OK);
	}


	@RequestMapping(value="/getInstallationIds",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getInstallationIds(HttpServletRequest request,HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();
		for(Region region : customer.getRegion()){
			JsonObject  json = new JsonObject();
			json.addProperty("regionId", region.getRegionId());
			json.addProperty("regionName", region.getRegionName());
			responselist.add(json);
		}
		return new ResponseEntity<String>(new Gson().toJson(responselist),HttpStatus.OK);
	}


	@RequestMapping(value="/getConsumerAccNo",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getConsumerAccNo(HttpServletRequest request,HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();
		for(Consumer consumer : customer.getConsumer()){
			JsonObject  json = new JsonObject();
			json.addProperty("consumerId", consumer.getConsumerId());
			json.addProperty("consumerAccNo", consumer.getConsumerAccountNumber());
			responselist.add(json);
		}
		return new ResponseEntity<String>(new Gson().toJson(responselist),HttpStatus.OK);
	}


	@RequestMapping(value="/getRegisterIds",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getRegisterIds(HttpServletRequest request,HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();
		customer.getConsumer().stream().forEach(x -> {
			x.getConsumerMeter().stream().filter(consumerMeterList -> consumerMeterList.getRegisterId() != null ).forEach(y ->{
				JsonObject  json = new JsonObject();
				json.addProperty("registerId", y.getRegisterId());
				responselist.add(json);
			});
		});
		return new ResponseEntity<String>(new Gson().toJson(responselist),HttpStatus.OK);
	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getConsumptionGraph",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<List<String>> getConsumptionGraph(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String baseTypeId, @RequestParam(value="baseType") String baseType,
			@RequestParam(value="periodType") String periodType, @RequestParam(value="periodTime") String periodTime){

		List consumptionList = cmtService.getConsumptionCMTList(periodTime, periodType, baseTypeId, baseType);

		Double avg = calculateAverage(consumptionList);

		StringBuilder graphData = new StringBuilder();
		StringBuilder graphAvgData = new StringBuilder();
		StringBuilder graphTariffData = new StringBuilder();
		String tariff = "";

		graphData.append("[");
		graphAvgData.append("[");
		graphTariffData.append("[");
		for (int i = 0; i < consumptionList.size(); i++)
		{
			Object[] row = (Object[]) consumptionList.get(i);

			if(periodType != null && periodType.equals("month")) {
				BigInteger b = (BigInteger) row[7];
				row[7] = new DateFormatSymbols().getMonths()[ b.intValue() - 1];
			}

			if (i == consumptionList.size()-1)
			{
				graphData.append(" [\"" + row[7] + "\","  + row[3] + "] ");
				graphAvgData.append(" [\"" + row[7] + "\","  + row[3] + ","  + avg + "] ");

				if((boolean) row[6])	tariff = "TC";	
				graphTariffData.append(" [\"" + row[7] + "\","  + row[3] + ",\""  + tariff + "\"] ");
			}
			else
			{
				graphData.append(" [\"" + row[7] + "\"," + row[3] + "], ");
				graphAvgData.append(" [\"" + row[7] + "\"," + row[3] + ","  + avg + "], ");

				if((boolean) row[6])	tariff = "TC";
				graphTariffData.append(" [\"" + row[7] + "\"," + row[3] + ",\""  + tariff + "\"], ");
			}
			tariff = "";
		}
		graphData.append("]");
		graphAvgData.append("]");
		graphTariffData.append("]");

		List<String> responseList = new ArrayList<String>();
		responseList.add(graphData.toString());
		responseList.add(graphAvgData.toString());
		responseList.add(graphTariffData.toString());

		return new ResponseEntity<List<String>>(responseList, HttpStatus.OK);
	}


	@RequestMapping(value="/initAbnormalConsumptionInitData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initAbnormalConsumptionInitData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		List<Object> responseList = new ArrayList<Object>();

		List<AbnormalConsumptionModel> abnormalConsModelList = reportService.generateInitDataForAbnormalConsumption(customer); // generated Consumer<eter List based on Customer
		StringBuilder exportCSVData =  generateExportCSVData(abnormalConsModelList);
		Integer threshold =  vpnService.getVPNDetails().getAbnormalThreshold().intValue();

		responseList.add(abnormalConsModelList);
		responseList.add(exportCSVData);
		responseList.add(threshold);

		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	@RequestMapping(value="/abnormalConsumptionReportActionGraph",method=RequestMethod.POST)
	public ResponseEntity<String> abnormalConsumptionReportGenerator(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="registerId") String registerID){

		StringBuilder jsonResponseStringForGraph =  reportService.generateActionReportDataForAbnormalConsumption(registerID);
		return new ResponseEntity<>(new Gson().toJson(jsonResponseStringForGraph),HttpStatus.OK);
	}

	private StringBuilder generateExportCSVData(List<AbnormalConsumptionModel> list){
		StringBuilder graphData = new StringBuilder();

		graphData.append("[");
		for (int i = 0; i < list.size(); i++)
		{
			AbnormalConsumptionModel cmt = list.get(i);
			if (i == list.size()-1)
			{
				graphData.append(" [\"" + cmt.getConsumerAccountNumber() + "\",\""  + cmt.getAverageConsumption() + "\",\""  + (cmt.getLast24hrUsage() > 0 ? cmt.getLast24hrUsage(): ApplicationConstants.LAST24HRRECORDNOTFOUND ) + "\"  ] ");
			}
			else
			{
				graphData.append(" [\"" + cmt.getConsumerAccountNumber() + "\",\"" + cmt.getAverageConsumption() + "\",\""  + (cmt.getLast24hrUsage() > 0 ? cmt.getLast24hrUsage(): ApplicationConstants.LAST24HRRECORDNOTFOUND ) + "\"  ], ");
			}
		}
		graphData.append("]");
		return graphData;
	}

	// Free-Report initial data
	@RequestMapping(value="/initFreeReportData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initFreeReportData(HttpServletRequest request,HttpServletResponse response){
		List<Object> responseList = new ArrayList<>();
		responseList.add(KenureUtilityContext.freereportDCMap);
		responseList.add(KenureUtilityContext.freereportConsumerEPMap);
		responseList.add(KenureUtilityContext.freereportSiteMap);
		responseList.add(KenureUtilityContext.freereportInstallerMap);
		responseList.add(KenureUtilityContext.freereportConsumerMap);

		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	@RequestMapping(value="/searchFreeReportData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchFreeReportData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedField",required=true) String selectedField,
			@RequestParam(value="reportBy",required=false) String reportBy,
			@RequestParam(value="reportPeriod",required=false) String reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
			@RequestParam(value="installatioName",required=false) String installatioName,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="dcSerialNumber",required=false) String dcSerialNumber)  {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List list =  reportService.searchFreeReportData(customer,selectedField,reportBy,reportPeriod,
				startDate,endDate,installatioName,siteName,dcSerialNumber);

		return new ResponseEntity<>(new Gson().toJson(list),HttpStatus.OK);
	}

	@SuppressWarnings({"rawtypes"})
	private double calculateAverage(List data) {
		Integer sum = 0;

		if(!data.isEmpty()) {
			for (int i = 0; i < data.size(); i++) {
				Object[] row = (Object[]) data.get(i);
				Integer b = (Integer) row[3];
				sum += b != null ? b : 0;
			}
			return sum.doubleValue() / data.size();
		}
		return sum;
	}


	@RequestMapping(value = "/initConsumerUsageReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initConsumerUsageReport(HttpServletRequest request,
			HttpServletResponse response) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());

		JsonObject json = new JsonObject();
		json.addProperty("consumerId", consumer.getConsumerId());
		json.addProperty("consumerAccNo", consumer.getConsumerAccountNumber());

		return new ResponseEntity<String>(new Gson().toJson(json), HttpStatus.OK);
	}

	@RequestMapping(value="/getRegisterIdsForConsumer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getRegisterIdsForConsumer(HttpServletRequest request,HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();

		consumer.getConsumerMeter().stream().filter(consumerMeterList -> consumerMeterList.getRegisterId() != null ).forEach(y ->{
			JsonObject  json = new JsonObject();
			json.addProperty("registerId", y.getRegisterId());
			responselist.add(json);
		});

		return new ResponseEntity<String>(new Gson().toJson(responselist),HttpStatus.OK);
	}


	@RequestMapping(value = "/initBillingReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initBillingReport(HttpServletRequest request,
			HttpServletResponse response) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());

		List<Object> responselist = new ArrayList<>();

		consumer.getConsumerMeter().stream().filter(consumerMeterList -> consumerMeterList.getRegisterId() != null ).forEach(y ->{
			JsonObject  json = new JsonObject();
			json.addProperty("registerId", y.getRegisterId());
			responselist.add(json);
		});

		return new ResponseEntity<String>(new Gson().toJson(responselist), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getBillingReportGraph",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<List<String>> getBillingReportGraph(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="registerId") String registerId, @RequestParam(value="amountType") String amountType) throws ParseException{

		List billingHistoryList = consumerMeterService.getConsumerMetersBillingByRegId(registerId);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		StringBuilder graphData = new StringBuilder();
		StringBuilder graphEstimatedData = new StringBuilder();
		String isEstimated = "";

		graphData.append("[");
		graphEstimatedData.append("[");
		for (int i = 0; i < billingHistoryList.size(); i++)
		{
			BillingHistory billingHistory = (BillingHistory) billingHistoryList.get(i);

			if (i == billingHistoryList.size()-1)
			{
				if(amountType.equals("Consumed Unit")) {
					graphData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\","  + billingHistory.getConsumedUnit() + "] ");

					if(billingHistory.getIsEstimated())		isEstimated = "Est";
					graphEstimatedData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\","  + billingHistory.getConsumedUnit() + ",\""  + isEstimated + "\"] ");
				} else if(amountType.equals("Total Amount")) {
					graphData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\","  + billingHistory.getTotalAmount() + "] ");

					if(billingHistory.getIsEstimated())		isEstimated = "Est";
					graphEstimatedData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\","  + billingHistory.getTotalAmount() + ",\""  + isEstimated + "\"] ");
				}
			}
			else
			{
				if(amountType.equals("Consumed Unit")) {
					graphData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\"," + billingHistory.getConsumedUnit() + "], ");

					if(billingHistory.getIsEstimated())		isEstimated = "Est";
					graphEstimatedData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\"," + billingHistory.getConsumedUnit() + ",\""  + isEstimated + "\"], ");
				} else if(amountType.equals("Total Amount")) { 
					graphData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\"," + billingHistory.getTotalAmount() + "], ");

					if(billingHistory.getIsEstimated())		isEstimated = "Est";
					graphEstimatedData.append(" [\"" + format.format(billingHistory.getBillDate()) + "\"," + billingHistory.getTotalAmount() + ",\""  + isEstimated + "\"], ");
				}
			}
			isEstimated = "";
		}
		graphData.append("]");
		graphEstimatedData.append("]");

		List<String> responseList = new ArrayList<String>();
		responseList.add(graphData.toString());
		responseList.add(graphEstimatedData.toString());

		return new ResponseEntity<List<String>>(responseList, HttpStatus.OK);
	}



	// Financial What-If report starts from here
	@RequestMapping(value="/initFinancialWhatIfData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initFinancialWhatIfData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());
		List<Object> responseList = new ArrayList<Object>();

		Map<String, Integer> map = new HashMap<String, Integer>();
		Set<Consumer> consumerSet = customer.getConsumer();
		for(Consumer consumer : consumerSet){
			for(ConsumerMeter consumerMeter : consumer.getConsumerMeter()){
				if(consumerMeter.getTariffPlan() != null){
					if(map.containsKey(consumerMeter.getTariffPlan().getTarrifPlanName())){
						map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), map.get(consumerMeter.getTariffPlan().getTarrifPlanName()) + 1);
					}else{
						map.put(consumerMeter.getTariffPlan().getTarrifPlanName(), 1);
					}
				}
			}
		}

		// if size would be greater 1 than it means that this customer has different tariff.
		if(map.size() > 1){
			List<Region> regionList = userService.getRegionListByCustomerId(customer.getCustomerId());
			regionNameList = new ArrayList<String>();

			regionList.stream().forEach(n -> {	
				regionNameList.add(n.getRegionName());
			});
			/*System.out.println(regionNameList.size());*/

			responseList.add("noUniqueTariff");
			responseList.add(regionNameList);

			return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
		}else{
			String tariffName = "";
			for (Map.Entry<String, Integer> entry : map.entrySet()){
				tariffName = entry.getKey();
			}
			// Now Count Total Number No Consumers Under This Customer
			int totalConsumerMeters = 0;
			for(Consumer cm : customer.getConsumer()){
				totalConsumerMeters += cm.getConsumerMeter().size();
			}
			/*System.out.println("Size >>"+totalConsumerMeters);*/
			TariffModel tariffModel = reportService.getTariffModelByTariffName(tariffName,customer);
			responseList.add("sameTariff");
			responseList.add(tariffModel);
			responseList.add(totalConsumerMeters);

			// setting session
			request.getSession().setAttribute("financialSearchBasedOn", "customerBasedSearch");

			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}

	}

	@RequestMapping(value="/checkForConsumerWithSameTariffUnderRegion",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> checkForConsumerWithSameTariff(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="region") String regionName){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> responseList = new ArrayList<Object>();

		Region region =  userService.getRegionByName(customer,regionName);
		TariffModel tariffModel = null;
		if(region != null){
			String responseString =  reportService.findSameTariffBasedOnRegion(region,null,customer);
			responseList.add(responseString);

			if(!responseString.contains(":")){
				if(responseString.equals("noUniqueTariff")){
					List<?> siteNameListUnderRegion = new ArrayList<>(region.getSite());
					siteNameListUnderRegion = siteNameListUnderRegion.stream().map(n ->((Site)n).getSiteName()).collect(Collectors.toList());
					responseList.add(siteNameListUnderRegion);
				}
			}else{
				String tariffName = responseString.split(":")[1];
				tariffModel = reportService.getTariffModelByTariffName(tariffName,customer);

				int totalConsumerMeters = 0;
				reportService.initializeConsumerByCustomer(customer);
				for(Consumer cm : customer.getConsumer()){
					reportService.initializeConsumerMeterByConsumer(cm);
					totalConsumerMeters += cm.getConsumerMeter().size();
				}
				responseList.add(tariffModel);
				responseList.add(totalConsumerMeters);
			}
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return null;
	}

	@RequestMapping(value="/checkForConsumerWithSameTariffUnderSite",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> checkForConsumerWithSameTariffUnderSite(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="site") String siteName){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> responseList = new ArrayList<Object>();

		Site site = userService.getSiteByCustomerAndSiteName(customer,siteName);
		if(site != null){
			String responseString = reportService.findSameTariffBasedOnRegion(null,site,customer);
			responseList.add(responseString);
			if(responseString.equals("noUniqueTariff")){
				responseList.add("No Unique Tariff Under Site.");
			}else if(responseString.equals("notAnyConsumerAllocated")){
				responseList.add("No Consumer Allocated Under this Site "+siteName);	
			}else if(responseString.equals("noConsumerMeterUnderRegion")){

			}else if(responseString.contains(":")){
				String tariffName = responseString.split(":")[1];
				TariffModel tariffModel = reportService.getTariffModelByTariffName(tariffName,customer);
				responseList.add(tariffModel);
				int totalConsumerMeters = 0;
				reportService.initializeConsumerByCustomer(customer);
				for(Consumer cm : customer.getConsumer()){
					reportService.initializeConsumerMeterByConsumer(cm);
					totalConsumerMeters += cm.getConsumerMeter().size();
				}
				responseList.add(totalConsumerMeters);
			}
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return null;
	}
	@RequestMapping(value="/generateFinancialTimePeriodData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateFinancialTimePeriodData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="monthDuration") int month,
			@RequestParam(value="regionName",required=false) String regionName,
			@RequestParam(value="siteName",required=false) String siteName){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		Date startLimitDate = DateTimeConversionUtils.getDateFromNowToAppliedMonthDuration(month);	
		totalRevenue = 0.0D;

		if(regionName != null ){
			if(siteName != null && !siteName.equalsIgnoreCase("default")){
				Site site = userService.getSiteByCustomerAndSiteName(customer, siteName);
				if(site != null){
					List<ConsumerMeter> consumerMeterList = userService.getConsumerMeterByListOfSiteId(Arrays.asList(site.getSiteId()));
					consumerMeterList.forEach(n -> {
						totalRevenue += reportService.getRevenueForIndividualConsumerMeter(n, startLimitDate, new Date());
					});
				}
			}else{
				reportService.initializeConsumerByCustomer(customer);
				List<Consumer> consumerList = new ArrayList<Consumer>(customer.getConsumer());
				Region region = userService.getRegionByName(customer, regionName);

				reportService.initalizeSiteByregion(region);
				List<Integer> siteId = region.getSite().stream().map(n->n.getSiteId()).collect(Collectors.toList());
				List<ConsumerMeter> consumerMeterList = userService.getConsumerMeterByListOfSiteId(siteId);
				consumerMeterList.forEach(n -> {
					totalRevenue += reportService.getRevenueForIndividualConsumerMeter(n, startLimitDate, new Date());
				});
				/*System.out.println("Revenue >> "+totalRevenue);*/
			}
			return new ResponseEntity<>(new Gson().toJson(totalRevenue),HttpStatus.OK);
		}else{
			reportService.initializeConsumerByCustomer(customer);
			Double totalRevenue =  reportService.getRevenueByConsumerList(new ArrayList<>(customer.getConsumer()),startLimitDate,new Date());

			/*System.out.println("Revenue >>"+totalRevenue);*/
			return new ResponseEntity<>(new Gson().toJson(totalRevenue),HttpStatus.OK);
		}
	}

	@RequestMapping(value="/applyNewTariff",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> applyNewTariff(HttpServletRequest request,HttpServletResponse response,
			@RequestBody WhatIfTariffModel whatIfTariffModel){

		int month = 0;

		if(whatIfTariffModel.getMonth().equalsIgnoreCase("lastYear"))
			month = 12;
		else if(whatIfTariffModel.getMonth().equalsIgnoreCase("lastQuarter"))
			month = 3;
		else if(whatIfTariffModel.getMonth().equalsIgnoreCase("lastMonth"))
			month = 1;

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		Date startLimitDate = DateTimeConversionUtils.getDateFromNowToAppliedMonthDuration(month);
		totalRevenue = 0.0D;

		if(whatIfTariffModel.getRegionName() != null){
			if(whatIfTariffModel.getSiteName() != null && !whatIfTariffModel.getSiteName().equalsIgnoreCase("default")){
				Site site = userService.getSiteByCustomerAndSiteName(customer, whatIfTariffModel.getSiteName());
				if(site != null){
					List<ConsumerMeter> consumerMeterList = userService.getConsumerMeterByListOfSiteId(Arrays.asList(site.getSiteId()));
					consumerMeterList.forEach(n -> {
						totalRevenue += reportService.getUsageForIndividualConsumerMeter(n,startLimitDate,new Date(),whatIfTariffModel.getTariffModel(),whatIfTariffModel.getConsumptionVar());
					});
				}

			}else{
				Region region = userService.getRegionByName(customer, whatIfTariffModel.getRegionName());
				reportService.initalizeSiteByregion(region);
				totalRevenue = 0.0D;

				List<Integer> siteId = region.getSite().stream().map(n->n.getSiteId()).collect(Collectors.toList());
				List<ConsumerMeter> consumerMeterList = userService.getConsumerMeterByListOfSiteId(siteId);
				consumerMeterList.forEach(n -> {
					totalRevenue += reportService.getUsageForIndividualConsumerMeter(n,startLimitDate,new Date(),whatIfTariffModel.getTariffModel(),whatIfTariffModel.getConsumptionVar());
				});
			}
			/*System.out.println("Total >>"+totalRevenue+" Here is List >");*/
			return new ResponseEntity<String>(new Gson().toJson(totalRevenue),HttpStatus.OK);
		}else {
			totalRevenue = 0.0D;
			reportService.initializeRegionByCustomer(customer);

			reportService.initializeConsumerByCustomer(customer);
			List<Consumer> cmList = new ArrayList<Consumer>(customer.getConsumer());

			for(Consumer cm : customer.getConsumer()){
				reportService.initializeConsumerMeterByConsumer(cm);
				List<ConsumerMeter> consumerMeterList = new ArrayList<>(cm.getConsumerMeter());
				for(int j=0;j<consumerMeterList.size();j++){
					ConsumerMeter consumerMeter = consumerMeterList.get(j);
					totalRevenue +=  reportService.getUsageForIndividualConsumerMeter(consumerMeter,startLimitDate,new Date(),whatIfTariffModel.getTariffModel(),whatIfTariffModel.getConsumptionVar());
				}
			}
			/*System.out.println("Total >>"+totalRevenue);*/
			return new ResponseEntity<String>(new Gson().toJson(totalRevenue),HttpStatus.OK);
		}
	}
}

