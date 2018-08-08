package com.kenure.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DistrictMeterTransaction;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.entity.Installer;
import com.kenure.entity.Region;
import com.kenure.entity.Site;
import com.kenure.entity.SiteInstallationFiles;
import com.kenure.entity.User;
import com.kenure.model.DCModel;
import com.kenure.model.GenerateDcInstallationModel;
import com.kenure.model.GenerateInsModel;
import com.kenure.model.GenerateRepeaterInstallationModal;
import com.kenure.model.KDLEmailModel;
import com.kenure.model.RegionModel;
import com.kenure.model.SiteModel;
import com.kenure.service.ICommissioningService;
import com.kenure.service.IConsumerMeterService;
import com.kenure.service.IDistrictUtilityMeterService;
import com.kenure.service.IUserService;
import com.kenure.utils.LoggerUtils;
import com.kenure.utils.MD5Encoder;
import com.kenure.utils.MailSender;
import com.kenure.utils.RandomNumberGenerator;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

@Controller
public class CommissioningController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICommissioningService commissioningService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	IConsumerMeterService consumerMeterService;

	@Autowired
	private IDistrictUtilityMeterService duService;

	@Autowired
	MailSender mailSender;
	
	private static final org.slf4j.Logger logger = LoggerUtils.getInstance(LoginController.class);

	//private List<Site> siteIdList = null;


	@RequestMapping(value="/initInstallationAndCommissioningData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initInstallationAndCommissioningData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){

			int totalSpareDC = 0;

			Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

			// For 3rd Table data
			List<DataCollector> dataCollectors = commissioningService.getSpareDataCollectorListByCustomer(customer);
			List<Object> objectList = new ArrayList<Object>();
			List<JsonObject> jObjetcList = new ArrayList<JsonObject>();
			if(dataCollectors.size() > 0){
				Iterator<DataCollector> dcIterator = dataCollectors.iterator();

				while(dcIterator.hasNext()){

					DataCollector dataCollector = dcIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("datacollectorId",dataCollector.getDatacollectorId());
					json.addProperty("dcSerialNumber",dataCollector.getDcSerialNumber());
					json.addProperty("dcIp",dataCollector.getDcIp());
					jObjetcList.add(json);
					totalSpareDC++;
				}
			}	
			objectList.add(jObjetcList);

			// For 2nd Table Data

			//List<RegionModel> regionListObj = new ArrayList<RegionModel>();
			Iterator<Region> regionIteratorObj = customer.getRegion().iterator();

			List<JsonObject> siteJObjetcList = new ArrayList<JsonObject>();

			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

			while(regionIteratorObj.hasNext()){

				Region region = regionIteratorObj.next();

				Iterator<Site> siteIterator = region.getSite().iterator();
				while(siteIterator.hasNext()){

					Site site = siteIterator.next();

					if(site.getCurrentStatus()!=null && !site.getCurrentStatus().equalsIgnoreCase("installed")){
						JsonObject json = new JsonObject();
						json.addProperty("siteId",site.getSiteId());
						json.addProperty("currentStatus",site.getCurrentStatus());
						if(site.getCommissioningStartTime()!=null)
							json.addProperty("commissioningStartTime",sdf.format(site.getCommissioningStartTime()));
						else
							json.addProperty("commissioningStartTime","-");

						siteJObjetcList.add(json);
					}
				}
			}


			//*****************

			// For 1st Tree Structure Data
			List<RegionModel> regionList = new ArrayList<RegionModel>();

			Iterator<Region> regionIterator = customer.getRegion().iterator();

			while(regionIterator.hasNext()){
				Set<SiteModel> setModel = new HashSet<SiteModel>();
				Set<DCModel> setDCModel = null ;
				RegionModel rm = new RegionModel();

				Region region = regionIterator.next();

				Iterator<Site> siteIterator = region.getSite().iterator();
				if(region.getSite().size() > 0){
					rm.setRegionId(region.getRegionId());
					rm.setRegionName(region.getRegionName());
					while(siteIterator.hasNext()){
						SiteModel sm = new SiteModel();
						Site site = siteIterator.next();
						Iterator<DataCollector> dcIterator = site.getDatacollector().iterator();	
						if(site.getDatacollector().size() > 0){
							sm.setSiteId(site.getSiteId());
							sm.setSiteName(site.getSiteName());
							setDCModel = new HashSet<DCModel>();
							while(dcIterator.hasNext()){
								DCModel dcModel = new DCModel();
								DataCollector dc = dcIterator.next();
								dcModel.setDcId(dc.getDatacollectorId());
								dcModel.setDcSerialNumber(dc.getDcSerialNumber());
								setDCModel.add(dcModel);
							}
							sm.setDc(setDCModel);
							setModel.add(sm);
						}
					}
					rm.setSiteList(setModel);
					regionList.add(rm);
				}
			}
			
			regionList.sort((RegionModel r1,RegionModel r2) -> r1.getRegionId() - r2.getRegionId());
			regionList.sort((RegionModel r1, RegionModel r2) -> r1.getRegionId() - r2.getRegionId());

			objectList.add(regionList);
			objectList.add(siteJObjetcList);
			objectList.add(totalSpareDC);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessDataCollectorList"), HttpStatus.OK);
	}

	@RequestMapping(value="/initStartContinueinstallation", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initStartContinueinstallation(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="siteId")String siteId){
		try{

			User loggedUser = (User) request.getSession().getAttribute(
					"currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser
					.getUserId());

			Site site = userService.getSiteDataBySiteId(siteId);
			if(site.getCurrentStatus()==null || site.getCurrentStatus().isEmpty()){
				site.setCurrentStatus("planning");
				userService.updateSite(site);
			}
			Integer tag = site.getTag();
			String flag;
			List<Object> responseList = new ArrayList<Object>();
			if(tag!=null){
				flag = tag.toString();
			}else{
				flag = "1";
			}
			responseList.add(flag);

			JsonObject jsonObject = userService.getHeaderDetailsBySiteId(site,customer);
			responseList.add(jsonObject);

			if(site.getRouteFileName()!=null && site.getTag()!=null && site.getTag()>1 && site.getTag()<5){
				List<String[]> csvlineList = new ArrayList<String[]>();
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String tempFilePath = resourcesPath+File.separator+ApplicationConstants.MASTERFILEPATH;
				File tempServerFile = new File(tempFilePath+"\\"+site.getRouteFileName());
				CSVReader reader = new CSVReader(new FileReader(tempServerFile.getAbsolutePath()),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER,1);
				for(String[] nextLine : reader){
					String[] line = new String[(nextLine[27]!=null && !nextLine[27].trim().equals(""))?20:19];
					line[0]=nextLine[2];//Consumer Acc NO
					line[1]=nextLine[3];//StreetName
					line[2]=nextLine[4];//Address2
					line[3]=nextLine[5];//Address3
					line[4]=nextLine[6];//Address4
					line[5]=nextLine[7];//Zipcode
					line[6]=nextLine[28];//District utility id
					line[7]=nextLine[10];//last Reading
					line[8]=nextLine[11];//last ReadingTime
					line[9]=nextLine[19];//KValue
					line[10]=nextLine[20];//Direction
					line[11]=nextLine[21];//UtilityCode
					line[12]=nextLine[17];//UsageThreshold
					line[13]=nextLine[18];//UsageInterval
					line[14]=nextLine[12];//Left billing
					line[15]=nextLine[13];//Right billing
					line[16]=nextLine[15];//Decimal position
					line[17]=nextLine[16];//Leakage
					line[18]=nextLine[14];//Backflow
					if(nextLine[27]!=null && !nextLine[27].trim().equals(""))
						line[19]=nextLine[27];//Scheduled Date
					csvlineList.add(line);
				}
				reader.close();
				responseList.add(csvlineList);
			}
			return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
		}catch(Exception e){
			//e.printStackTrace();
			logger.warn(e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/uploadRouteFile", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> uploadRouteFile(HttpServletRequest request,HttpServletResponse response,@RequestParam("file") MultipartFile file,@RequestParam(value="isHeader",required=false) boolean isheader, @RequestParam(value="scheduledDate",required=false)String scheduledDate, final RedirectAttributes redirectAttributes){
		if (!file.isEmpty() && file.getOriginalFilename().toLowerCase().lastIndexOf(".csv")!=-1) {
			CSVReader csvReader = null;
			CSVWriter csvWriter = null;
			boolean directoryExists = false;
			int counter = 0;
			Date date = null;
			SimpleDateFormat outputFormat = null;
			String startDate = null;
			String endDate = null;
			String siteId = (String) request.getSession().getAttribute("selectedSiteId");
			request.getSession().removeAttribute("scheduledDate");
			try {
				Site site = userService.getSiteDataBySiteId(siteId);
				if(scheduledDate!=null && !scheduledDate.equalsIgnoreCase("null") && !scheduledDate.trim().equals("")){
					SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.ENGLISH);
					date = sdf.parse(scheduledDate);
					outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
					outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					startDate = outputFormat.format(date);
					endDate = outputFormat.format(DateUtils.addHours(date, 6));
					site.setCommissioningStartTime(outputFormat.parse(startDate));
					site.setCommissioningEndTime(outputFormat.parse(endDate));
					site.setScheduleTime(outputFormat.parse(startDate));
					site.setCommissioningType("scheduled");
					userService.updateSite(site);
					request.getSession().setAttribute("scheduledDate", outputFormat.parse(startDate));
				}else{
					site.setCommissioningType("manual");
					site.setCommissioningStartTime(null);
					site.setCommissioningEndTime(null);
					site.setScheduleTime(null);
					userService.updateSite(site);
				}
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String tempFilePath = resourcesPath+File.separator+ApplicationConstants.TEMPFILEPATH;
				logger.info("Temp folder path - {}",tempFilePath);
				directoryExists = InstallerController.createMediaDirectoresIfNotExists(tempFilePath);
				logger.info("Directory Created or Not - {}",directoryExists);
				String tempFileName =file.getOriginalFilename();
				if(directoryExists){   
					FileOutputStream fos = new FileOutputStream(tempFilePath+File.separator+tempFileName);
					fos.write(file.getBytes());
					fos.close();
				}
				File tempServerFile = new File(tempFilePath+File.separator+tempFileName);
				if(isheader){
					csvReader = new CSVReader(new FileReader(tempServerFile.getAbsolutePath()),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER, 1);
					counter = 1; 
				}else{
					csvReader = new CSVReader(new FileReader(tempServerFile.getAbsolutePath()));
				}
				File modifiedServerFile = new File(tempFilePath+File.separator+"temp_".concat(tempFileName));
				csvWriter = new CSVWriter(new FileWriter(modifiedServerFile.getAbsolutePath()),',');
				String[] line=null;
				while((line = csvReader.readNext()) != null){
					ArrayList<String> list = new ArrayList(Arrays.asList(line));
					int i;
					for(i=0;i<10;i++){
						list.add(null); // Add the new element here
					}
					if(scheduledDate!=null && !scheduledDate.equalsIgnoreCase("null") && !scheduledDate.trim().equals("")){
						list.add(startDate);
					}
					csvWriter.writeNext(list.toArray(new String[0]));
				}
				csvWriter.close();
				csvReader.close();
				tempServerFile.delete();
				csvReader = new CSVReader(new FileReader(modifiedServerFile.getAbsolutePath()));
				List<String[]> csvlineList = csvReader.readAll();
				csvReader.close();
				modifiedServerFile.delete();
				return new ResponseEntity<String>(new Gson().toJson(csvlineList),HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error occured in /uploadRouteFile method - {}",e.getMessage());
				redirectAttributes.addFlashAttribute("errorMsg", e.getMessage()+" error occured in Uploaded File at Line "+counter);
				return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMsg", "Failed to upload File because the file was empty or its not a csv file!");
			return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/generateMasterFile", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateMasterFile(HttpServletRequest request,HttpServletResponse response,@RequestBody List<String[]> csvlineList){
		try{
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			String siteId = (String) request.getSession().getAttribute("selectedSiteId");
			boolean directoryExists = false;
			String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
			String tempFilePath = resourcesPath+File.separator+ApplicationConstants.MASTERFILEPATH;
			directoryExists = InstallerController.createMediaDirectoresIfNotExists(tempFilePath);
			File finalServerFile = null;
			if(directoryExists){
				finalServerFile = new File(tempFilePath+File.separator+customer.getCustomerCode()+"_"+siteId+"_"+"1"+".csv");
				CSVWriter csvWriter = new CSVWriter(new FileWriter(finalServerFile.getAbsolutePath()),CSVWriter.DEFAULT_SEPARATOR,CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.NO_ESCAPE_CHARACTER);
				String[] headerLine = ("Serial,RegisterID,AccountNo,Street Name,Address2,Address3,Address4,Zipcode"
						+ ",Latitude,Longitude,Reading,ReadingTime,BillingLeft,BillingRight,DecimalPos,Leakage,Backflow,"
						+ "UsageThreshold,UsageInterval,KValue,Direction,UtilityCode,RepeaterNodes,RepeaterLevels,EndpointMode,"
						+ "FWVersion,SiteID,Scheduled Date,District utility id").split(",");
				csvWriter.writeNext(headerLine);
				for(String[] nextLine : csvlineList){
					ArrayList<String> list = new ArrayList(Arrays.asList(nextLine));
					int lineLength = list.size();
					String[] line = new String[lineLength==20?lineLength+9:lineLength+10];
					line[2]=list.get(0);//Consumer Acc NO
					line[3]=list.get(1);//StreetName
					line[4]=list.get(2);//Address2
					line[5]=list.get(3);//Address3
					line[6]=list.get(4);//Address4
					line[7]=list.get(5);//Zipcode
					line[10]=list.get(7);//last Reading
					line[11]=list.get(8);//last ReadingTime
					line[12]=list.get(14);//BillingLeft
					line[13]=list.get(15);//BillingRight
					line[14]=list.get(16);//DecimalPos
					line[15]=list.get(17);//Leakage
					line[16]=list.get(18);//Backflow
					line[17]=list.get(12);//UsageThreshold
					line[18]=list.get(13);//UsageInterval
					line[19]=list.get(9);//KValue
					line[20]=list.get(10);//Direction
					line[21]=list.get(11);//UtilityCode
					line[22]=String.valueOf(0);//RepeaterNodes
					line[23]=String.valueOf(0);//RepeaterLevels
					line[24]=String.valueOf(0);//EndpointMode
					line[26]=siteId;//SiteId
					if(lineLength==20)
						line[27]=list.get(19);//Scheduled Date
					line[28]=list.get(6);//District utility id
					csvWriter.writeNext(line);
				}
				//csvWriter.writeAll(csvlineList);
				csvWriter.close();
			}
			CSVReader csvReader =new CSVReader(new FileReader(finalServerFile.getAbsolutePath()),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER,1);
			String[] line;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			//List<String> consumerAccNoList = new ArrayList<String>();
			while((line = csvReader.readNext()) != null){
				//counter++;
				if(line[2]!=null && !line[2].trim().equals("")){
					Consumer consumer = userService.getConsumerByConsumerAccNo(line[2].trim());
					if(consumer==null){
						ContactDetails consumerDetails = new ContactDetails();
						consumerDetails.setStreetName(line[3]);
						consumerDetails.setAddress1(line[4]);
						consumerDetails.setAddress2(line[5]);
						consumerDetails.setAddress3(line[6]);
						consumerDetails.setZipcode(line[7].replaceAll("\\s",""));
						consumerDetails.setCreated_by(loggedUser.getUserId());
						consumerDetails.setActive(true);
						//consumerDetails.setCell_number1(cell_number1);
						//consumerDetails.setFirstName(firstName);
						//consumerDetails.setLastname(lastname);
						//consumerDetails.setEmail1(email1);
						User user = new User();
						user.setUserName(line[2]);
						user.setPassword(MD5Encoder.MD5Encryptor(RandomNumberGenerator.generatePswd().toString()));
						user.setRole(userService.getRoleByName("Consumer"));
						user.setCreatedBy(loggedUser.getUserId());
						user.setCreatedTs(new Date());
						user.setFirstTimeLogin(true);
						user.setActiveStatus(true);
						user.setDetails(consumerDetails);

						consumer = new Consumer();
						consumer.setConsumerAccountNumber(line[2]);
						consumer.setCreatedBy(loggedUser.getUserId());
						consumer.setCreatedTimeStamp(new Date());
						consumer.setActive(true);
						consumer.setUser(user);
						consumer.setCustomer(customer);

						ConsumerMeter endpoint = new ConsumerMeter();
						endpoint.setStreetName(line[3]);
						endpoint.setAddress1(line[4]);
						endpoint.setAddress2(line[5]);
						endpoint.setAddress3(line[6]);
						endpoint.setZipcode(line[7].replaceAll("\\s",""));
						if(line[10]!=null && !line[10].trim().equals("")){
							endpoint.setLastMeterReading(Integer.parseInt(line[10].trim()));
						}else{
							endpoint.setLastMeterReading(0);
						}
						if(line[11]!=null && !line[11].trim().equals("")){
							endpoint.setLastMeterReadingDate(sdf.parse(line[11].trim()));
						}else{
							endpoint.setLastMeterReadingDate(null);
						}
						if(line[19]!=null && !line[19].trim().equals("")){
							endpoint.setKvalue(Integer.parseInt(line[19]));
						}else{
							endpoint.setKvalue(null);
						}
						if(line[20]!=null && !line[20].trim().equals("")){
							endpoint.setDirection(Integer.parseInt(line[20]));
						}else{
							endpoint.setDirection(null);
						}
						if(line[21]!=null && !line[21].trim().equals("")){
							endpoint.setUtilityCode(Integer.parseInt(line[21]));
						}else{
							endpoint.setUtilityCode(null);
						}
						if(line[17]!=null && !line[17].trim().equals("")){
							endpoint.setUsageThreshold(Integer.parseInt(line[17]));
						}else{
							endpoint.setUsageThreshold(null);
						}
						if(line[18]!=null && !line[18].trim().equals("")){
							endpoint.setUsageInterval(Integer.parseInt(line[18]));
						}else{
							endpoint.setUsageInterval(null);
						}
						if(line[12]!=null && !line[12].trim().equals("")){
							endpoint.setLeftBillingDigit(Integer.parseInt(line[12]));
						}else{
							endpoint.setLeftBillingDigit(null);
						}
						if(line[13]!=null && !line[13].trim().equals("")){
							endpoint.setRightBillingDigit(Integer.parseInt(line[13]));
						}else{
							endpoint.setRightBillingDigit(null);
						}
						if(line[14]!=null && !line[14].trim().equals("")){
							endpoint.setDecimalPosition(Integer.parseInt(line[14]));
						}else{
							endpoint.setDecimalPosition(null);
						}
						if(line[15]!=null && !line[15].trim().equals("")){
							endpoint.setLeakageThreshold(Integer.parseInt(line[15]));
						}else{
							endpoint.setLeakageThreshold(null);
						}
						if(line[16]!=null && !line[16].trim().equals("")){
							endpoint.setBackflowLimit(Integer.parseInt(line[16]));
						}else{
							endpoint.setBackflowLimit(null);
						}
						endpoint.setIsCommissioned(false);
						endpoint.setCreatedBy(loggedUser.getUserId());
						endpoint.setCreatedTimeStamp(new Date());
						endpoint.setActive(true);
						endpoint.setConsumer(consumer);
						endpoint.setCustomer(customer);

						if(line[28]!=null && !line[28].trim().equals("")){
							DistrictUtilityMeter districtMeter = duService.getDUMeterByDUSerialNo(line[28]);
							if(districtMeter!=null){
								endpoint.setDistrictUtilityMeter(districtMeter);
							}else{
								districtMeter = new DistrictUtilityMeter();
								districtMeter.setDistrictUtilityMeterSerialNumber(line[28].trim());
								districtMeter.setCustomer(customer);
								
								DistrictMeterTransaction districtMeterTrans = new DistrictMeterTransaction();
								districtMeterTrans.setCurrentReading(0);
								districtMeterTrans.setStartBillingDate(null);
								districtMeterTrans.setEndBillingDate(null);
								districtMeterTrans.setDistrictUtilityMeter(districtMeter);
								
								Set<DistrictMeterTransaction> setTransaction = new HashSet<DistrictMeterTransaction>();
								setTransaction.add(districtMeterTrans);
								districtMeter.setDistrictMeterTransactions(setTransaction);
								
								userService.addDUMeter(districtMeter);
								endpoint.setDistrictUtilityMeter(districtMeter);
							}
						}
						userService.insertNewConsumer(consumer);
						consumerMeterService.addConsumerMeter(endpoint);

					}else{
						ConsumerMeter endpoint = new ConsumerMeter();
						endpoint.setStreetName(line[3]);
						endpoint.setAddress1(line[4]);
						endpoint.setAddress2(line[5]);
						endpoint.setAddress3(line[6]);
						endpoint.setZipcode(line[7].replaceAll("\\s",""));
						if(line[10]!=null && !line[10].trim().equals("")){
							endpoint.setLastMeterReading(Integer.parseInt(line[10].trim()));
						}else{
							endpoint.setLastMeterReading(0);
						}
						if(line[11]!=null && !line[11].trim().equals("")){
							endpoint.setLastMeterReadingDate(sdf.parse(line[11].trim()));
						}else{
							endpoint.setLastMeterReadingDate(null);
						}
						if(line[19]!=null && !line[19].trim().equals("")){
							endpoint.setKvalue(Integer.parseInt(line[19]));
						}else{
							endpoint.setKvalue(null);
						}
						if(line[20]!=null && !line[20].trim().equals("")){
							endpoint.setDirection(Integer.parseInt(line[20]));
						}else{
							endpoint.setDirection(null);
						}
						if(line[21]!=null && !line[21].trim().equals("")){
							endpoint.setUtilityCode(Integer.parseInt(line[21]));
						}else{
							endpoint.setUtilityCode(null);
						}
						if(line[17]!=null && !line[17].trim().equals("")){
							endpoint.setUsageThreshold(Integer.parseInt(line[17]));
						}else{
							endpoint.setUsageThreshold(null);
						}
						if(line[18]!=null && !line[18].trim().equals("")){
							endpoint.setUsageInterval(Integer.parseInt(line[18]));
						}else{
							endpoint.setUsageInterval(null);
						}
						if(line[12]!=null && !line[12].trim().equals("")){
							endpoint.setLeftBillingDigit(Integer.parseInt(line[12]));
						}else{
							endpoint.setLeftBillingDigit(null);
						}
						if(line[13]!=null && !line[13].trim().equals("")){
							endpoint.setRightBillingDigit(Integer.parseInt(line[13]));
						}else{
							endpoint.setRightBillingDigit(null);
						}
						if(line[14]!=null && !line[14].trim().equals("")){
							endpoint.setDecimalPosition(Integer.parseInt(line[14]));
						}else{
							endpoint.setDecimalPosition(null);
						}
						if(line[15]!=null && !line[15].trim().equals("")){
							endpoint.setLeakageThreshold(Integer.parseInt(line[15]));
						}else{
							endpoint.setLeakageThreshold(null);
						}
						if(line[16]!=null && !line[16].trim().equals("")){
							endpoint.setBackflowLimit(Integer.parseInt(line[16]));
						}else{
							endpoint.setBackflowLimit(null);
						}
						endpoint.setIsCommissioned(false);
						endpoint.setCreatedBy(loggedUser.getUserId());
						endpoint.setCreatedTimeStamp(new Date());
						endpoint.setActive(true);
						endpoint.setConsumer(consumer);
						endpoint.setCustomer(customer);

						if(line[28]!=null && !line[28].trim().equals("")){
							DistrictUtilityMeter districtMeter = duService.getDUMeterByDUSerialNo(line[28]);
							if(districtMeter!=null){
								endpoint.setDistrictUtilityMeter(districtMeter);
							}else{
								districtMeter = new DistrictUtilityMeter();
								districtMeter.setDistrictUtilityMeterSerialNumber(line[28].trim());
								districtMeter.setCustomer(customer);
								
								DistrictMeterTransaction districtMeterTrans = new DistrictMeterTransaction();
								districtMeterTrans.setCurrentReading(0);
								districtMeterTrans.setStartBillingDate(null);
								districtMeterTrans.setEndBillingDate(null);
								districtMeterTrans.setDistrictUtilityMeter(districtMeter);
								
								Set<DistrictMeterTransaction> setTransaction = new HashSet<DistrictMeterTransaction>();
								setTransaction.add(districtMeterTrans);
								districtMeter.setDistrictMeterTransactions(setTransaction);
								
								userService.addDUMeter(districtMeter);
								endpoint.setDistrictUtilityMeter(districtMeter);
							}
						}
						consumerMeterService.addConsumerMeter(endpoint);
					}
				}
			}
			csvReader.close();
			Site site = userService.getSiteDataBySiteId(siteId);
			site.setTag(3);
			site.setRouteFileName(finalServerFile.getName());
			Date date = new Date();
			site.setRouteFileLastUpdate(date);
			site.setRouteFileLastUpdateByName(customer.getCustomerName());
			userService.updateSite(site);
			JsonObject json = new JsonObject();
			json.addProperty("updatedDate",date.toString());
			json.addProperty("updatedBy",customer.getCustomerName());
			json.addProperty("tag", site.getTag());

			return new ResponseEntity<String>(new Gson().toJson(json),HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<String>(new Gson().toJson("Error"),HttpStatus.OK);
		}
	}


	@RequestMapping(value="/initdcEpConfigTest",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initdcEpConfigTest(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId") String siteId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){

			// For Table 1
			Site site = userService.getSiteDataBySiteId(siteId);
			List<SiteInstallationFiles> siteInstallationFiles = commissioningService.getSiteInstallationFilesBySiteId(site);
			List<String> filenameList = new ArrayList<String>();
			List<Object> objectList = new ArrayList<Object>();
			List<JsonObject> jObjetcList = new ArrayList<JsonObject>();
			if(siteInstallationFiles!=null && siteInstallationFiles.size() > 0){
				Iterator<SiteInstallationFiles> iteratorObj = siteInstallationFiles.iterator();

				while(iteratorObj.hasNext()){

					SiteInstallationFiles siteFiles = iteratorObj.next();
					JsonObject json = new JsonObject();
					json.addProperty("siteInstallationFileId", siteFiles.getSiteInstallationFileId());
					json.addProperty("fileName", siteFiles.getFileName());
					filenameList.add(siteFiles.getFileName());
					if(siteFiles.getNoOfEndPoints() != null)
						json.addProperty("noOfEndPoints", siteFiles.getNoOfEndPoints());
					else
						json.addProperty("noOfEndPoints", -1);

					if(siteFiles.getNoOfDatacollectors() != null)
						json.addProperty("noOfDatacollectors", siteFiles.getNoOfDatacollectors());
					else
						json.addProperty("noOfDatacollectors", -1);

					if(siteFiles.getIsFileUploaded() == true)
						json.addProperty("isFileUploaded", "Yes");
					else
						json.addProperty("isFileUploaded", "No");

					if(siteFiles.getIsFileVerified() == true)
						json.addProperty("isFileVerified", "Yes");
					else
						json.addProperty("isFileVerified", "No");


					Installer installer = userService.getInstallerById(siteFiles.getInstaller().getInstallerId());
					json.addProperty("installerName", installer.getInstallerName());

					jObjetcList.add(json);
				}
			}	
			objectList.add(jObjetcList);
			
			JsonObject jsonObject = userService.getHeaderDetailsBySiteId(site,customer);
			objectList.add(jsonObject);
			List<JsonObject> jsonObjectList = new ArrayList<JsonObject>();
			if(site.getTag()>=8 && site.getTag()<=9)
				jsonObjectList = commissioningService.getInstalledDCsList(filenameList,customer,siteId,"INSTALLATION");
			
			objectList.add(jsonObjectList);
			objectList.add(site.getTag());
			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessThisPage"), HttpStatus.OK);
	}

	// pct24 Code :

	@SuppressWarnings({ "unchecked", "resource" })
	@RequestMapping(value="/generatePhase2InitData",method=RequestMethod.POST)
	public ResponseEntity<String> generatePhase2InitData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId") String siteId){
		List<Object> responseList = new ArrayList<Object>();
		List<String> installername = new ArrayList<String>();
		CSVReader csvReader ;
		Site site = userService.getSiteDataBySiteId(siteId); 
		File masterFile = new File(servletContext.getRealPath(ApplicationConstants.MASTERFILEPATH)+File.separator+site.getRouteFileName()); // Needs master file generated by server(phase 1)
		// Reading Master Installation CSV file
		try(FileReader fileReader = new FileReader(masterFile.getAbsolutePath())){
			csvReader = new CSVReader(new FileReader(masterFile.getAbsolutePath()),CSVParser.DEFAULT_SEPARATOR,CSVParser.DEFAULT_QUOTE_CHARACTER,1);
			String[] line;


			Map<String, Integer> map = new HashMap<String, Integer>();
			int totalNoOfEndpointsToBeInstalled = 0;
			while((line = csvReader.readNext()) != null){
				totalNoOfEndpointsToBeInstalled++;
				if(map.containsKey(line[3])){
					map.put(line[3],map.get(line[3])+1);
				}else{
					map.put(line[3],1);
				}
			}
			responseList.add(map);
			// Getting dependency of customer
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			List<Installer> insList = commissioningService.getAssignInstallerData(currentCustomer); 
			insList.forEach(n -> {
				if(n.isActive())
					installername.add(n.getInstallerName());
			});
			responseList.add(installername);
			responseList.add(totalNoOfEndpointsToBeInstalled);
			responseList.add(site.getTag());

			if(site.getTag() == 4){
				// We have completed installer selection phase and generated its files .. 
				List<SiteInstallationFiles> list = null;
				List<String> fileNameList = null;
				if((list = commissioningService.getSIFBySiteAndInstaller(site,null)) != null){
					fileNameList = list.stream().filter(filt -> filt.getNoOfDatacollectors()==null && !filt.getFileName().startsWith("DC_")).map(n -> n.getFileName()).collect(Collectors.toList());
					responseList.add(fileNameList);
				}
			}

		}catch(Exception e){
			logger.error("Error occured in /generatePhase2InitData - {}",e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	@RequestMapping(value="/generateInstFiles",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> generateInstFiles(HttpServletRequest request,HttpServletResponse response,
			@RequestBody GenerateInsModel model){

		/*insList =  insList.stream().filter(n -> n.getGroupValue().size() > 0).collect(Collectors.toList());*/
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());	
		List<Object> generatedFileList = new ArrayList<Object>();
		if(model.getResponseArray().size() > 0){
			generatedFileList = commissioningService.generateInstFiles(model.getResponseArray(),currentCustomer,model.getSiteId());
		}
		return  new ResponseEntity<>(new Gson().toJson(generatedFileList),HttpStatus.OK);
	}

	@RequestMapping(value="/generateFileDataToDisplay",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateFileDataToDisplay(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="fileName") String fileName){
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
		String filePath = EpInsFilePath+File.separator+fileName;
		File file = new File(filePath);
		List<String[]> dataList = null ;
		if(file.exists()){
			try(CSVReader csvReader = new CSVReader(new FileReader(file))){
				dataList = csvReader.readAll();
			}catch(Exception e){
				logger.error("Error occured while reading CSV file in /generateFileDataToDisplay method - {}",e.getMessage());
			}
		}else{
			logger.warn("Error !! No such file exist !!");
		}
		return new ResponseEntity<>(new Gson().toJson(dataList),HttpStatus.OK);
	}

	@RequestMapping(value="/assignToInstaller",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> assignToInstaller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="list") List<String> fileNameList,
			@RequestParam(value="siteId") String siteId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Site site = userService.getSiteDataBySiteId(siteId);

		List<String> installerFileNameList = new ArrayList<String>(fileNameList);
		
		for(int i=0;i<fileNameList.size();i++){
			String  filename = fileNameList.get(i);
			String installerName = filename.substring(0,filename.lastIndexOf("_"));
			installerName = installerName.substring((currentCustomer.getCustomerCode()+"_"+siteId+"_").length());
			fileNameList.set(i, installerName);
		}
		String prefix = "";
		int tag = 5;
		commissioningService.updateInstallerForSIF(fileNameList,site,currentCustomer,prefix,tag);

		// Mail sending to installer
				
				for(int i=0;i<installerFileNameList.size();i++){
					Installer ins = userService.getInstallerByNameAndCustomer(fileNameList.get(i), currentCustomer);
					String emailAddress = ins.getUser().getDetails().getEmail1();
					String subject = "New Site Assigned !";
					//String[] mailBody = { "New site is assigned" };
					String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
					String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
					String filePath = EpInsFilePath+File.separator+installerFileNameList.get(i);
					new Thread(() ->{
						
						KDLEmailModel model = new KDLEmailModel();
						model.setSubject(subject);
						model.setName(ins.getUser().getDetails().getFirstName());
						model.setResource("Site");
						model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
						
						Map<String,KDLEmailModel> map = new HashMap<>();
						map.put("emailConst", model);
						try {
							mailSender.htmlEmailSender(emailAddress, subject, map, "site", filePath);
						} catch (Exception e) {
							logger.error("Error occured in /assignToInstaller method - {}",e.getMessage());
						}
						
					}).start();
				}
				
				return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
	}

	@RequestMapping(value="/discardChanges",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> discardChanges(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="siteId") String siteId){
		try{
			Site site = userService.getSiteDataBySiteId(siteId);
	
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
	
			List<SiteInstallationFiles> list = commissioningService.getSIFBySiteAndInstaller(site,null);
			String prefix = "";
			list.forEach(n -> {
				String  filename = n.getFileName();
				String installerName = filename.substring(0,filename.lastIndexOf("_"));
				installerName = installerName.substring((customer.getCustomerCode()+"_"+siteId+"_").length());
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String EpInsFilePath = resourcesPath+File.separator+ApplicationConstants.EPINSTALLATIONFILEPATH;
				commissioningService.deleteINSFilesFromDirectory(site,customer,installerName,EpInsFilePath,prefix);
				commissioningService.deleteSIF(n);
			});
			site.setTag(3);
			userService.updateSite(site);
			return new ResponseEntity<>(new Gson().toJson(site.getTag()),HttpStatus.OK);
		}catch(Exception e){
			logger.error("Error occured in /discardChanges method - {}",e.getMessage());
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}

	@RequestMapping(value="/initDcLocaionInstallation",method=RequestMethod.POST)
	public ResponseEntity<String> initDcLocaionInstallation(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="siteId") String siteId){
		List<Object> responseList = new ArrayList<Object>();
		try{
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer currentCustomer = userService.getCustomerDetailsByUser(loggedUser.getUserId());
			Site site = userService.getSiteDataBySiteId(siteId); 

			responseList = commissioningService.initDcLocaionInstallation(currentCustomer, site);
			JsonObject jsonObject = userService.getHeaderDetailsBySiteId(site,currentCustomer);
			responseList.add(jsonObject);
			// Preparing Data for BDC
			responseList.addAll(commissioningService.generateBDCDataBasedOnCustomerAndSite(currentCustomer,site));
			if(site.getTag() == 6){
				// We have completed installer selection phase and generated its files .. 
				List<SiteInstallationFiles> list = null;
				List<String> fileNameList = null;
				String prefix = "DC_";
				if((list = commissioningService.getSIFBySiteAndInstaller(site,null)) != null){
					fileNameList = list.stream().filter(filt -> filt.getFileName().startsWith(prefix) && filt.getNoOfEndPoints()==null).map(n -> n.getFileName()).collect(Collectors.toList());
					responseList.add(fileNameList);
				}
			}
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}

	@RequestMapping(value="/generateDCFileData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getGeneratedDCFileDataByName(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="fileName") String fileName){
		// Logic here to generate file and related file path ... Currently we are taking a static path 
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
		File tempServerFile = new File(DcInsFilePath+File.separator+fileName);
		List<String[]> csvlineList = null;
		try(CSVReader reader = new CSVReader(new FileReader(tempServerFile))) {
			csvlineList = reader.readAll();
		} catch (IOException e) {
			logger.error("Error occured in /generateDCFileData method - {}",e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(csvlineList),HttpStatus.OK);
	}

	@RequestMapping(value="/addBDCForDCPlanning",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addBDCForDCPlanning(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId") String siteId,
			@RequestParam(value="list") List<String> bdcDcNumber){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<Object> responseList = new ArrayList<>();
		try{
			commissioningService.assignBDCToSite(siteId,bdcDcNumber,currentCustomer);
			Site site = userService.getSiteDataBySiteId(siteId);
			responseList = commissioningService.generateBDCDataBasedOnCustomerAndSite(currentCustomer, site);
		}catch(Exception e){
			logger.error("Error occured in /addBDCForDCPlanning method - {}",e.getMessage());
		}

		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}


	@RequestMapping(value="/removeBDCForDCPlanning",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> removeBDCForDCPlanning(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId") String siteId,
			@RequestParam(value="list") List<String> bdcDcSerialNumber){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> responseList = new ArrayList<>();

		try{
			Site site = commissioningService.removeBDCFromSite(siteId,bdcDcSerialNumber,currentCustomer);
			if(site != null)
				responseList = commissioningService.generateBDCDataBasedOnCustomerAndSite(currentCustomer, site);
		}catch(Exception e){
			logger.error("Error occured in /removeBDCForDCPlanning method - {}",e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	@RequestMapping(value="/generateDcInstallationFiles",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> generateDcInstallationFiles(HttpServletRequest request,HttpServletResponse response,@RequestBody GenerateDcInstallationModel model){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());	
		List<Object> generatedFileList = new ArrayList<Object>();
		Date scheduleDate = (Date) request.getSession().getAttribute("scheduledDate");
		if(model.getResponseArray().size() > 0){
			generatedFileList = commissioningService.generateDcInstallationFiles(model.getResponseArray(),currentCustomer,model.getSiteId(),scheduleDate);
		}
		return  new ResponseEntity<>(new Gson().toJson(generatedFileList),HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/assignDcToInstaller",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> assignDcToInstaller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="list") List<String> fileNameList,@RequestParam(value="siteId") String siteId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		Site site = userService.getSiteDataBySiteId(siteId);
		List<String> installerFileNameList = new ArrayList<String>(fileNameList);
		for(int i=0;i<fileNameList.size();i++){
			String  filename = fileNameList.get(i);
			String installerName = filename.substring(0,filename.lastIndexOf("_"));
			installerName = installerName.substring(("DC_"+currentCustomer.getCustomerCode()+"_"+siteId+"_").length());
			fileNameList.set(i, installerName);
		}
		String prefix = "DC_";
		int tag = 7;
		commissioningService.updateInstallerForSIF(fileNameList,site,currentCustomer,prefix,tag);
		
		//Mail send code
		
		for(int i=0;i<installerFileNameList.size();i++){
			Installer ins = userService.getInstallerByNameAndCustomer(fileNameList.get(i), currentCustomer);
			String emailAddress = ins.getUser().getDetails().getEmail1();
			String subject = "New Site Assigned.";
			String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
			String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
			String filePath = DcInsFilePath+File.separator+installerFileNameList.get(i);
			
			new Thread(() ->{
				try {
					//String insName = ins.getInstallerName();
					KDLEmailModel model = new KDLEmailModel();
					model.setSubject(subject);
					model.setName("Installer");
					model.setResource("Installer");
					model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
					
					Map map = new HashMap<String,KDLEmailModel>();
					map.put("emailConst", model);
					
					mailSender.htmlEmailSender(emailAddress, subject, map, "installer", filePath);
				} catch (Exception e) {
					logger.error("Error occured in /assignDcToInstaller method - {}",e.getMessage());
				}
				
			}).start();
		}
		
		return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
	}

	@RequestMapping(value="/discardInstallationFiles",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> discardInstallationFiles(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="siteId") String siteId){
		try{
			Site site = userService.getSiteDataBySiteId(siteId);
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			List<SiteInstallationFiles> sifList = commissioningService.getSIFBySiteAndInstaller(site,null);
			String prefix = "DC_";
			sifList.forEach(n -> {
				String  filename = n.getFileName();
				String installerName = filename.substring(0,filename.lastIndexOf("_"));
				installerName = installerName.substring((prefix+customer.getCustomerCode()+"_"+siteId+"_").length());
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String DcInsFilePath = resourcesPath+File.separator+ApplicationConstants.DCINSTALLATIONFILEPATH;
				commissioningService.deleteINSFilesFromDirectory(site,customer,installerName,DcInsFilePath,prefix);
				commissioningService.deleteSIF(n);
			});
			site.setTag(5);
			userService.updateSite(site);
			return new ResponseEntity<>(new Gson().toJson(site.getTag()),HttpStatus.OK);
		}catch(Exception e){
			logger.error("Error occured in /discardInstallationFiles method - {}",e.getMessage());
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/initRepeaterLocaionInstallation",method=RequestMethod.POST)
	public ResponseEntity<String> initRepeaterLocaionInstallation(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="siteId") String siteId){
		List<Object> responseList = new ArrayList<Object>();
		try{
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			Site site = userService.getSiteDataBySiteId(siteId); 
			responseList = commissioningService.initDcLocaionInstallation(currentCustomer, site);
			if(site.getTag() == 8){
				// We have completed installer selection phase and generated its files .. 
				List<SiteInstallationFiles> list = null;
				List<String> fileNameList = null;
				String prefix = "REP_";
				if((list = commissioningService.getSIFBySiteAndInstaller(site,null)) != null){
					fileNameList = list.stream().filter(filt -> filt.getFileName().startsWith(prefix) && filt.getNoOfDatacollectors()==null && filt.getNoOfEndPoints()==null).map(n -> n.getFileName()).collect(Collectors.toList());
					responseList.add(fileNameList);
				}
			}
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/generateRepeaterInstallationFiles",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> generateRepeaterInstallationFiles(HttpServletRequest request,HttpServletResponse response,@RequestBody GenerateRepeaterInstallationModal model){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());	
		List<Object> generatedFileList = new ArrayList<Object>();
		if(model.getResponseArray().size() > 0){
			generatedFileList = commissioningService.generateRepeaterInstallationFiles(model.getResponseArray(),currentCustomer,model.getSiteId());
		}
		return  new ResponseEntity<>(new Gson().toJson(generatedFileList),HttpStatus.OK);
	}

	@RequestMapping(value="/generateRepeatersFileData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> generateRepeatersFileData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="fileName") String fileName){
		String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
		String RepInsFilePath = resourcesPath+File.separator+ApplicationConstants.REPINSTALLATIONFILEPATH;
		File tempServerFile = new File(RepInsFilePath+File.separator+fileName);
		List<String[]> csvlineList = null;
		try(CSVReader reader = new CSVReader(new FileReader(tempServerFile))) {
			csvlineList = reader.readAll();
		} catch (IOException e) {
			logger.error("Error occured in /generateRepeatersFileData method - {}",e.getMessage());
		}
		return new ResponseEntity<>(new Gson().toJson(csvlineList),HttpStatus.OK);
	}
	
	@RequestMapping(value="/assignRepeatersToInstaller",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> assignRepeatersToInstaller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="list") List<String> fileNameList,@RequestParam(value="siteId") String siteId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		Site site = userService.getSiteDataBySiteId(siteId);
		List<String> installerFileNameList = new ArrayList<String>(fileNameList);
		String prefix = "REP_";
		int tag = 9;
		for(int i=0;i<fileNameList.size();i++){
			String  filename = fileNameList.get(i);
			String installerName = filename.substring(0,filename.lastIndexOf("_"));
			installerName = installerName.substring((prefix+currentCustomer.getCustomerCode()+"_"+siteId+"_").length());
			fileNameList.set(i, installerName);
		}
		commissioningService.updateInstallerForSIF(fileNameList,site,currentCustomer,prefix,tag);
		
		//Mail send code

		for(int i=0;i<installerFileNameList.size();i++){
			Installer ins = userService.getInstallerByNameAndCustomer(fileNameList.get(i), currentCustomer);
			String emailAddress = ins.getUser().getDetails().getEmail1();
			String subject = "New Repeater Assigned.";
			String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
			String filePath = resourcesPath+File.separator+ApplicationConstants.REPINSTALLATIONFILEPATH+File.separator+installerFileNameList.get(i);
			new Thread(() ->{
				
				KDLEmailModel model = new KDLEmailModel();
				model.setSubject(subject);
				model.setName(ins.getUser().getDetails().getFirstName());
				model.setResource("Repeater");
				model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
				
				Map<String,KDLEmailModel> map = new HashMap<>();
				map.put("emailConst", model);
				try {
					mailSender.htmlEmailSender(emailAddress, subject, map, "repeater", filePath);
				} catch (Exception e) {
					logger.error("Error occured in /assignRepeatersToInstaller method - {}",e.getMessage());
				}
				
			}).start();
		}
		return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
	}

	@RequestMapping(value="/discardRepeaterInstallationFiles",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> discardRepeaterInstallationFiles(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="siteId") String siteId){
		try{
			Site site = userService.getSiteDataBySiteId(siteId);
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			List<SiteInstallationFiles> sifList = commissioningService.getSIFBySiteAndInstaller(site,null);
			String prefix = "REP_";
			sifList.forEach(n -> {
				String  filename = n.getFileName();
				String installerName = filename.substring(0,filename.lastIndexOf("_"));
				installerName = installerName.substring((prefix+customer.getCustomerCode()+"_"+siteId+"_").length());
				String resourcesPath = servletContext.getRealPath(ApplicationConstants.RESOURCESPATH);
				String RepInsFilePath = resourcesPath+File.separator+ApplicationConstants.REPINSTALLATIONFILEPATH;
				commissioningService.deleteINSFilesFromDirectory(site,customer,installerName,RepInsFilePath,prefix);
				commissioningService.deleteSIF(n);
			});
			site.setTag(7);
			userService.updateSite(site);
			return new ResponseEntity<>(new Gson().toJson(site.getTag()),HttpStatus.OK);
		}catch(Exception e){
			logger.error("Error occured in /discardRepeaterInstallationFiles method - {}",e.getMessage());
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	// Dhaval Code start

	@RequestMapping(value = "/initCommissioningPage", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initCommissioningPage(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		List<Object> responseList = new ArrayList<Object>();
		try {
			Site site = userService.getSiteDataBySiteId(siteId);
			if (site.getCurrentStatus() == null) {
				site.setCurrentStatus("planning");
				userService.updateSite(site);
			}
			//Integer tag = site.getTag();
			/*String flag;

			if (tag != null) {
				flag = tag.toString();
			} else {
				flag = "1";
			}*/
			JsonObject jsonObject = userService.getHeaderDetailsBySiteId(site,customer);
			responseList.add(jsonObject);
			responseList.add(site.getTag());
			responseList.add(site.getCommissioningType());
			List<JsonObject> jsonObjectList = new ArrayList<JsonObject>();
			if(site.getTag()>=10 && site.getTag()<=13){
				List<SiteInstallationFiles> siteInstallationFiles = commissioningService.getInstalledDCsFileNamesBySite(site);
				List<String> filenameList = new ArrayList<String>();
				siteInstallationFiles.stream().forEach(x -> {
					filenameList.add(x.getFileName());
				});
				if(site.getTag()>10 && site.getTag()<=13){
					jsonObjectList = commissioningService.getInstalledDCsList(filenameList,customer,siteId,"LEVEL1-COMMISSIONING");
					responseList.add(jsonObjectList);
				}if(site.getTag()==13){
					jsonObjectList = commissioningService.getInstalledDCsList(filenameList,customer,siteId,"LEVELN-COMMISSIONING");
					responseList.add(jsonObjectList);
				}
			}
			
			return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occured in /initCommissioningPage method - {}",e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	 // Dhaval Code end
	
	@RequestMapping(value = "/configureAndTestDataCollectors", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> configureAndTestDataCollectors(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "fileNameList") List<String> fileNameList,@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		Date scheduledDate = (Date) request.getSession().getAttribute("scheduledDate");
		request.getSession().removeAttribute("scheduledDate");
		try {
			List<Object> responseList = commissioningService.configureAndTestDataCollectors(fileNameList,customer,siteId,scheduledDate);
			responseList.add("DataCollectors Connection and Configuration is in progress, please refresh your page after few minutes");
			return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error occured in /configureAndTestDataCollectors method - {}",e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/startLevel1Commissioning", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> startLevel1Commissioning(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(customer!=null){
			Site site = userService.getSiteDataBySiteId(siteId);
			//List<Object> responseList = new ArrayList<Object>();
			try {
				String dcStage = "LEVEL1-COMMISSIONING";
				List<Object> responseList = commissioningService.StartLevel1OrNComm(site,dcStage);
				return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error occured in /startLevel1Commissioning method - {}",e.getMessage());
				return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/startLevelNCommissioning", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> startLevelNCommissioning(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(customer!=null){
			Site site = userService.getSiteDataBySiteId(siteId);
			//List<Object> responseList = new ArrayList<Object>();
			try {
				String dcStage = "LEVELN-COMMISSIONING";
				List<Object> responseList = commissioningService.StartLevel1OrNComm(site,dcStage);
				return new ResponseEntity<String>(new Gson().toJson(responseList),HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error occured in /startLevel1Commissioning method - {}",e.getMessage());
				return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/setTagValue", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> setTagValue(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(customer!=null){
			Site site = userService.getSiteDataBySiteId(siteId);
			try {
				site.setTag(12);
				userService.updateSite(site);
				return new ResponseEntity<String>(new Gson().toJson(site.getTag()),HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error occured in /setTagValue method - {}",e.getMessage());
				return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/initVerification", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initVerification(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "siteId") String siteId) {
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(customer!=null){
			Site site = userService.getSiteDataBySiteId(siteId);
			try {
				/*site.setTag(12);
				userService.updateSite(site);*/
				return new ResponseEntity<String>(new Gson().toJson(site.getTag()),HttpStatus.OK);
			} catch (Exception e) {
				logger.error("Error occured in /initVerification method - {}",e.getMessage());
				return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	
}
