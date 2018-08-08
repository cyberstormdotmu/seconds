package com.kenure.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.BillingHistory;
import com.kenure.entity.BoundryDataCollector;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ConsumerMeterTransaction;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Country;
import com.kenure.entity.Currency;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataCollectorAlerts;
import com.kenure.entity.DatacollectorMessageQueue;
import com.kenure.entity.DistrictMeterTransaction;
import com.kenure.entity.DistrictUtilityMeter;
import com.kenure.entity.Installer;
import com.kenure.entity.MaintenanceTechnician;
import com.kenure.entity.Region;
import com.kenure.entity.Role;
import com.kenure.entity.Site;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.User;
import com.kenure.entity.VPNConfiguration;
import com.kenure.model.AbnormalConsumptionModel;
import com.kenure.model.ConsumerAlertListModel;
import com.kenure.model.KDLEmailModel;
import com.kenure.service.IConsumerMeterService;
import com.kenure.service.IReportService;
import com.kenure.service.ITariffPlanService;
import com.kenure.service.IUserService;
import com.kenure.service.IVPNService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;
import com.kenure.utils.MD5Encoder;
import com.kenure.utils.MailSender;
import com.kenure.utils.RandomNumberGenerator;
/**
 * 
 * @author TatvaSoft
 *
 */
@Controller
@RequestMapping(value="/customerOperation")
public class CustomerController {

	private static String TEMP_NOTES_PATH = "WEB-INF/resources/notes";
	private static String WEB_INF_PATH = "WEB-INF";
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private IUserService userService;

	@Autowired
	private IVPNService vpnService;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IConsumerMeterService consumerMeterService;

	@Autowired
	private MailSender mailSender;

	@Autowired
	ITariffPlanService tariffPlanService;
	
	String selectedInstallerName = "";

	@SuppressWarnings("unused")
	private Gson gsonInstance = new Gson();

	private static final Logger logger = LoggerUtils.getInstance(CustomerController.class);
	
	// Default Mapping
	@RequestMapping(method=RequestMethod.GET)
	public String defaultMapping(){
		return "redirect:/customerOperation/customerDashboard";
	}
	
	//for updating customer profile by customer
	@RequestMapping(value = "/updateProfile",method=RequestMethod.POST,produces={"application/json"})
	public @ResponseBody ResponseEntity<String> updateProfile(@RequestParam("firstName") String firstName, 
			@RequestParam(value = "address1",required=false)String address1,
			@RequestParam(value = "address2",required=false) String address2,
			@RequestParam(value = "address3" ,required=false) String address3,
			@RequestParam(value = "streetname" ,required=false) String streetName,
			@RequestParam(value = "cell_number1",required=false) String cell_number1,
			@RequestParam(value ="email1",required=false) String email1,
			@RequestParam(value ="email2" ,required=false) String email2,
			@RequestParam(value ="email3",required=false) String email3,
			HttpServletRequest request,
			HttpSession session){

		logger.info("in update method");

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		ContactDetails details =	userService.getDetailsByUserID(loggedUser.getUserId());
		details.setFirstName(firstName);
		details.setAddress1(address1);
		details.setAddress2(address2);
		details.setAddress3(address3);
		details.setStreetName(streetName);
		details.setCell_number1(cell_number1);
		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);

		userService.updateCustomerDetails(details);
		return new ResponseEntity<String>(new Gson().toJson("updatedSuccessfully"), HttpStatus.OK);
	}

	@RequestMapping(value="/consumerUserManagementData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> consumerUserManagement(HttpServletRequest request, HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> consumerJsonList = new ArrayList<JsonObject>();

		if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
			List<Consumer> consumerList = userService.getConsumerUserListByCustomerId(customer.getCustomerId());
			if(!consumerList.isEmpty() && consumerList != null) {
				Iterator<Consumer> consumerIterator = consumerList.iterator();
				while(consumerIterator.hasNext()){
					Consumer consumer = (Consumer) consumerIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("consumerId", consumer.getConsumerId());
					json.addProperty("consumerAccNo", consumer.getConsumerAccountNumber());
					json.addProperty("firstName", consumer.getUser().getDetails().getFirstName()+" "+consumer.getUser().getDetails().getLastname());
					json.addProperty("isActive", consumer.getActive());
					consumerJsonList.add(json);
				}
				objectList.add(consumerJsonList);
				
				Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
				if(isNormalCustomer != null && isNormalCustomer){
					objectList.add(Boolean.FALSE);
				}else{
					objectList.add(Boolean.TRUE);
				}
				
			}
		}
		return new ResponseEntity<Object>(new Gson().toJson(objectList), HttpStatus.OK);
	}
	
	//getting DataPlan data
		@RequestMapping(value="/tariffPlanData",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> getTariffPlan(HttpServletRequest request, HttpServletResponse response){

			User currentUser = (User) request.getSession().getAttribute("currentUser");

			Customer customer = userService.getOnlyCustomerByUserId(currentUser.getUserId());

			List<JsonObject> tariffPlanListRequiredData = new ArrayList<JsonObject>();

			List<Object> objectList = new ArrayList<Object>();

			List<TariffPlan> tariffPlanList = userService.getTariffPlanList(customer.getCustomerId());

			if(!tariffPlanList.isEmpty() && tariffPlanList != null){

				Iterator<TariffPlan> tariffPlanIterator = tariffPlanList.iterator();

				while(tariffPlanIterator.hasNext()){

					TariffPlan tariffPlan = tariffPlanIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("consumerMeterId", tariffPlan.getTariffPlanId()); 
					json.addProperty("tariffPlanName", tariffPlan.getTarrifPlanName());
					tariffPlanListRequiredData.add(json);
				}
				logger.info("Fetching tariff plan details for logged in customer.");
			}		
			objectList.add(tariffPlanListRequiredData);
			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@RequestMapping(value="/insertConsumerUser",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> insertConsumerUser(HttpServletRequest request,HttpServletResponse response,
				@RequestParam(value="consumerAccountNumber") String consumerAccountNumber,
				@RequestParam(value="firstName") String firstName,
				@RequestParam(value="lastName") String lastName,
				@RequestParam(value="userName") String userName,
				@RequestParam(value="cell_number1") String cell_number1,
				@RequestParam(value="email1") String email1,
				@RequestParam(value="address1") String address1,
				@RequestParam(value="streetName") String streetName,
				@RequestParam(value="tariffPlan") String tariffPlan,
				@RequestParam(value="zipcode") String zip,
				@RequestParam(value="activeStatus") String activeStatus){

			User loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.currentUser);

			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			boolean isconsumerAccNumberValid = userService.checkForConsumerAccNumber(consumerAccountNumber);

			if(!isconsumerAccNumberValid){
				return new ResponseEntity<String>(new Gson().toJson("consumeraccnumbererror"),HttpStatus.OK);
			}

			User user = userService.availableUserName(userName);
			if(user != null){
				// UseName already exist.Sending error ?
				return new ResponseEntity<String>(new Gson().toJson("usernameerror"),HttpStatus.OK);
			}

			ContactDetails details = new ContactDetails();

			details.setAddress1(address1);
			details.setStreetName(streetName);

			details.setCell_number1(cell_number1);

			details.setEmail1(email1);

			details.setFirstName(firstName);
			details.setLastname(lastName);
			details.setZipcode(zip);

			com.kenure.entity.Role role = userService.getRoleByName("Consumer");

			User newUser = new User();
			newUser.setActiveStatus(Boolean.TRUE);
			newUser.setFirstTimeLogin(Boolean.TRUE);

			String generatedPassword = RandomNumberGenerator.generatePswd().toString();

			newUser.setPassword(MD5Encoder.MD5Encryptor(generatedPassword));
			newUser.setRole(role);

			newUser.setUserName(userName);
			newUser.setDetails(details);

			TariffPlan plan = userService.getTariffPlanById(Integer.parseInt(tariffPlan));

			boolean consumerStatus;
			if(activeStatus.equalsIgnoreCase("active")){
				consumerStatus = true;
			}else{
				consumerStatus = false;
			}

			Consumer consumer = new Consumer();
			consumer.setConsumerAccountNumber(consumerAccountNumber);
			consumer.setCustomer(customer);
			consumer.setUser(newUser);
			consumer.setCreatedTimeStamp(new Date());
			consumer.setTariffPlan(plan);
			consumer.setActive(consumerStatus);
			consumer.setCreatedBy(customer.getCustomerId());

			userService.insertNewConsumer(consumer);

			String subject = "New account created.";
			try {
				
				KDLEmailModel model = new KDLEmailModel();
				model.setSubject(subject);
				model.setName(firstName+" "+lastName);
				model.setUserName(userName);
				model.setPassword(generatedPassword);
				model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
				
				Map map = new HashMap<String,KDLEmailModel>();
				map.put("emailConst", model);
				
				mailSender.htmlEmailSender(email1, subject, map,"newAccount",null);
				return new ResponseEntity<String>(new Gson().toJson("added"),HttpStatus.OK);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}


		@RequestMapping(value="/searchConsumerByAccNum",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<Object> searchConsumerByAccNum(HttpServletRequest req,HttpServletResponse res,
				@RequestParam(value="consumerAccNumInput") String consumerAccNumInput){

			User loggedUser = (User) req.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			List<Consumer> searchedList = userService.searchConsumerByAccNum(consumerAccNumInput, customer.getCustomerId());

			List<JsonObject> consumerUserList = new ArrayList<JsonObject>();

			if(searchedList.size() > 0){

				for(int i=0;i<searchedList.size();i++){

					JsonObject json = new JsonObject();
					json.addProperty("consumerId", searchedList.get(i).getConsumerId());
					json.addProperty("consumerAccNo", searchedList.get(i).getConsumerAccountNumber());
					json.addProperty("firstName", searchedList.get(i).getUser().getDetails().getFirstName());
					json.addProperty("isActive", searchedList.get(i).getActive());
					consumerUserList.add(json);
				}
				return new ResponseEntity<Object>(new Gson().toJson(consumerUserList), HttpStatus.OK);
			}
			return new ResponseEntity<Object>(new Gson().toJson(ApplicationConstants.NOCONSUMERUSERFOUND), HttpStatus.OK);
		}


		@RequestMapping(value="/deleteConsumerUser",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> deleteUser(HttpServletRequest request,HttpServletResponse response,@RequestParam("consumerId") String consumerId){	
			if(consumerId.matches("^-?\\d+$") && (consumerId != null || !consumerId.trim().isEmpty())){
				User loggedUser = (User) request.getSession().getAttribute("currentUser");
				Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
				int customerId = customer.getCustomerId();
				try{

					Consumer consumer = userService.getConsumerByConsumerID(Integer.parseInt(consumerId));
					int roleId = consumer.getUser().getRole().getRoleId();

					boolean isConsumerDeleted = userService.deleteUser(Integer.parseInt(consumerId), roleId, customerId);
					if(isConsumerDeleted){
						return new ResponseEntity<String>(new Gson().toJson("Consumer successfully deleted"), HttpStatus.OK);
					}else{
						return new ResponseEntity<String>(new Gson().toJson("No such Consumer found"), HttpStatus.OK);
					}
				}catch(Exception e){
					return new ResponseEntity<String>(new Gson().toJson("consumerismapped"), HttpStatus.OK);
				}

			}
			return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
		}


		@RequestMapping(value="/editConsumerUser",method=RequestMethod.POST)
		public ResponseEntity<String> editConsumerUser(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="consumerId") String consumerId){

			Consumer consumer = userService.getConsumerByConsumerID(Integer.parseInt(consumerId));

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			List<JsonObject> tariffNames = new ArrayList<JsonObject>();

			List<Object> responseList = new ArrayList<Object>();

			if(consumer != null){

				User user = userService.getUserDetailsByUserID(consumer.getUser().getUserId());
				ContactDetails contactDetails = userService.getDetailsByUserID(consumer.getUser().getUserId());
				


				JsonObject jObject = new JsonObject();
				jObject.addProperty("consumerId", consumer.getConsumerId());
				jObject.addProperty("consumerAccountNumber", consumer.getConsumerAccountNumber());
				jObject.addProperty("firstName", contactDetails.getFirstName());
				jObject.addProperty("lastName", contactDetails.getLastname());
				jObject.addProperty("userName", user.getUserName());
				jObject.addProperty("cell_number1", contactDetails.getCell_number1());
				jObject.addProperty("email1", contactDetails.getEmail1());
				jObject.addProperty("address1", contactDetails.getAddress1());
				jObject.addProperty("streetName", contactDetails.getStreetName());
				jObject.addProperty("zipcode", contactDetails.getZipcode());
				Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
				if (consumer.getTariffPlan() != null) {
					TariffPlan tariff = userService.getTariffPlanById(consumer.getTariffPlan().getTariffPlanId());	
					jObject.addProperty("tariffPlanId", tariff.getTariffPlanId());
				}
				
				jObject.addProperty("activeStatus", consumer.getActive());

				List<TariffPlan> tariffPlanList = userService.getTariffPlanList(customer.getCustomerId());

				Iterator<TariffPlan> tariffPlanIterator = tariffPlanList.iterator();

				while(tariffPlanIterator.hasNext()){
					TariffPlan tariffPlan = (TariffPlan) tariffPlanIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("tariffId", tariffPlan.getTariffPlanId());
					json.addProperty("tariffName", tariffPlan.getTarrifPlanName());
					tariffNames.add(json);
				}

				responseList.add(jObject);
				responseList.add(tariffNames);

				request.getSession().setAttribute("responseJsonObject", responseList);

				return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.DATAFETCHED),HttpStatus.OK);
			}
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
		}


		@SuppressWarnings("unchecked")
		@RequestMapping(value="/getEditConsumerUserEntity",method=RequestMethod.POST)
		public ResponseEntity<String> getEditConsumerUserEntity(HttpServletRequest request,HttpServletResponse response){

			List<Object> jObject = new ArrayList<Object>();
			jObject = (List<Object>) request.getSession().getAttribute("responseJsonObject");

			if(jObject != null ){
				request.getSession().removeAttribute("responseJsonObject");
				return 	new ResponseEntity<>(new Gson().toJson(jObject),HttpStatus.OK);
			}
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
		}


		@RequestMapping(value="/updateConsumerUserDetails", method=RequestMethod.POST)
		public ResponseEntity<String> updateConsumerUser(HttpServletRequest request,HttpServletResponse response,
				@RequestParam(value="consumerId") String consumerId,
				@RequestParam(value="consumerAccountNumber") String consumerAccountNumber,
				@RequestParam(value="firstName") String firstName,
				@RequestParam(value="lastName") String lastName,
				@RequestParam(value="userName") String userName,
				@RequestParam(value="address1") String address1,
				@RequestParam(value="streetName") String streetName,
				@RequestParam(value="zipcode") String zipcode,
				@RequestParam(value="cell_number1") String cell_number1,
				@RequestParam(value="email1") String email1,
				@RequestParam(value="activeStatus") String activeStatus,
				@RequestParam(required=false ,value="tariffid") String tariffid){


			//Removing previous session attribute that will not not used in future
			Consumer consumer = userService.getConsumerByConsumerID(Integer.parseInt(consumerId));
			TariffPlan oldPlan = null;
			//if(consumer.getTariffPlan()!=null)
				//oldPlan = userService.getTariffPlanById(consumer.getTariffPlan().getTariffPlanId());
			
			ContactDetails contactDetails = consumer.getUser().getDetails();
			contactDetails.setFirstName(firstName);
			contactDetails.setLastname(lastName);
			contactDetails.setAddress1(address1);
			contactDetails.setStreetName(streetName);
			contactDetails.setEmail1(email1);
			contactDetails.setCell_number1(cell_number1);
			contactDetails.setZipcode(zipcode);

			Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
			User loggedUser;
			// If current customer is normal customer than we have to set its id as updated id else as SuperCustomer
			if(isNormalCustomer != null && isNormalCustomer){
				loggedUser = (User) request.getSession().getAttribute("normalCustomer");
			}else{
				loggedUser = (User) request.getSession().getAttribute("currentUser");
			}
			contactDetails.setUpdatedBy(loggedUser.getUserId());
			contactDetails.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			
			if(activeStatus.equalsIgnoreCase("active")){
				consumer.setActive(true);
				consumer.setDeletedBy(null);
				consumer.setDeletedTimeStamp(null);
				User user = consumer.getUser();
				user.setActiveStatus(true);
				user.setUpdatedBy(loggedUser.getUserId());
				user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				user.setDeletedBy(null);
				user.setDeletedTs(null);
				consumer.setUser(user);
			}else{
				consumer.setActive(false);
				consumer.setDeletedBy(loggedUser.getUserId());
				consumer.setDeletedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
				User user = consumer.getUser();
				user.setActiveStatus(false);
				user.setUpdatedBy(loggedUser.getUserId());
				user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				user.setDeletedBy(loggedUser.getUserId());
				user.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				consumer.setUser(user);
			}
			
			consumer.setUpdatedBy(loggedUser.getUserId());
			consumer.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
			int newTariffId = Integer.parseInt(tariffid);

			TariffPlan tp = userService.getTariffPlanById(newTariffId);

			consumer.setTariffPlan(tp);
			consumer.getUser().setDetails(contactDetails);
			consumer.getUser().setUpdatedBy(loggedUser.getUserId());
			consumer.getUser().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

			userService.updateConsumerUser(consumer);

			if(userService.updateConsumerUser(consumer)){
				userService.updateConsumerMeterUsingConsumerIdAndTariffId(consumer,oldPlan,tp);
				return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
			}
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
		}
		
		@RequestMapping(value="/consumerManagementData",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> consumerManagement(HttpServletRequest request, HttpServletResponse response){

			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			List<Object> objectList = new ArrayList<Object>();
			List<JsonObject> consumerList = new ArrayList<JsonObject>();
			List<ConsumerMeter> userList = null;
			if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
				Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
				int customerId = customer.getCustomerId();
				userList = userService.getConsumerList(customerId,loggedUser.getRole().getRoleName());
			}
			if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("consumer")){
				Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());
				int consumerId = consumer.getConsumerId();
				userList = userService.getConsumerList(consumerId,loggedUser.getRole().getRoleName());
			}

			if(!userList.isEmpty() && userList != null) {

				SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
				
				Iterator<ConsumerMeter> meterIterator = userList.iterator();

				while(meterIterator.hasNext()){
					ConsumerMeter consumerMeter = (ConsumerMeter) meterIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("consumerMeterId", consumerMeter.getConsumerMeterId());
					if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
						if(consumerMeter.getConsumer() != null){
							json.addProperty("conAccNum", consumerMeter.getConsumer().getConsumerAccountNumber());
						}else{
							json.addProperty("conAccNum", "-");
						}
					}
					json.addProperty("registerId", consumerMeter.getRegisterId());
					json.addProperty("billingFrequencyInDays", consumerMeter.getBillingFrequencyInDays());
					json.addProperty("lastReading", consumerMeter.getLastReading());
					json.addProperty("currentReading", consumerMeter.getCurrentReading());
					if (consumerMeter.getLastBillingDate() != null) {
						json.addProperty("lastBillingDate", sdf.format(consumerMeter.getLastBillingDate()));
					}
					json.addProperty("isActive", consumerMeter.isActive());
					consumerList.add(json);
				}

				JsonObject jObject = new JsonObject();

				if (loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {
					jObject.addProperty("isCustomer", true);
					logger.info("User role is Customer.");
				} else {
					jObject.addProperty("isCustomer", false);
					logger.info("User role is Consumer.");
				}

				objectList.add(jObject);

				objectList.add(consumerList);
				logger.info("Getting data for all endpoints.");
			}

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		
		@RequestMapping(value="/searchConsumer",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> searchConsumer(HttpServletRequest req,HttpServletResponse res,
				@RequestParam(required=false,value="searchRegisterId") String searchRegisterId,
				@RequestParam(required=false,value="searchConsumerAccNo") String searchConsumerAccNo) {
			
			User loggedUser = (User) req.getSession().getAttribute("currentUser");
			
			List<ConsumerMeter> searchedList = new ArrayList<ConsumerMeter>();
			if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
				Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
				searchedList = consumerMeterService.searchConsumer(searchRegisterId, searchConsumerAccNo,customer,null);
			}
			if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("consumer")){
				Consumer consumer = userService.getOnlyConsumerByUserId(loggedUser.getUserId());
				searchedList = consumerMeterService.searchConsumer(searchRegisterId, searchConsumerAccNo,null,consumer);
			}
			
			logger.info("Searching end point based on Register Id : '"+searchRegisterId+"' and Consumer Account Number : '"+searchConsumerAccNo+"'.");
			List<Object> objectList = new ArrayList<Object>();
			List<JsonObject> consumerList = new ArrayList<JsonObject>();

			if(searchedList != null && searchedList.size() > 0){

				Iterator<ConsumerMeter> meterIterator = searchedList.iterator();

				while(meterIterator.hasNext()){
					ConsumerMeter consumerMeter = (ConsumerMeter) meterIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("consumerMeterId", consumerMeter.getConsumerMeterId());
					if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
						json.addProperty("conAccNum", consumerMeter.getConsumer().getConsumerAccountNumber());
					}
					json.addProperty("registerId", consumerMeter.getRegisterId());
					json.addProperty("billingFrequencyInDays", consumerMeter.getBillingFrequencyInDays());
					json.addProperty("lastReading", consumerMeter.getLastReading());
					json.addProperty("currentReading", consumerMeter.getCurrentReading());
					if (consumerMeter.getLastBillingDate() != null) {
						json.addProperty("lastBillingDate", consumerMeter.getLastBillingDate().toString());
					}
					json.addProperty("isActive", consumerMeter.isActive());
					consumerList.add(json);
				}

				objectList.add(consumerList);

				logger.info("End point(s) found based on above search criteria.");
				return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
			}

			logger.info("No end point(s) found based on above search criteria.");
			return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
		}
		
		@RequestMapping(value="/tariffPlanAndBillingFrequencyDataAndSite",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> tariffPlanAndSiteData(HttpServletRequest request, HttpServletResponse response){

			User loggedUser = (User) request.getSession().getAttribute("currentUser"); 

			List<Object> objectList = new ArrayList<Object>();
			List<JsonObject> tariffNames = new ArrayList<JsonObject>();
			List<JsonObject> billingFrequencyNames = new ArrayList<JsonObject>();
			List<JsonObject> siteList = new ArrayList<JsonObject>();

			//if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){

			Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

			Set<TariffPlan> tariffPlanSet = customer.getTariffPlan();

			Iterator<TariffPlan> tariffPlanIterator = tariffPlanSet.iterator();

			while(tariffPlanIterator.hasNext()){
				TariffPlan tariffPlan = (TariffPlan) tariffPlanIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("tariffId", tariffPlan.getTariffPlanId());
				json.addProperty("tariffName", tariffPlan.getTarrifPlanName());
				tariffNames.add(json);
			}

			logger.info("Getting tariff plan details for customer : " + customer.getCustomerName());
			objectList.add(tariffNames);

			for (Entry<String, Integer> entry : KenureUtilityContext.billingFrequency.entrySet()) {
				JsonObject json = new JsonObject();
				json.addProperty("billingFrequencyName", entry.getKey());
				json.addProperty("billingFrequencyId", entry.getValue());
				billingFrequencyNames.add(json);
			}

			logger.info("Getting billing frequency details.");
			objectList.add(billingFrequencyNames);

			//if (loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {

			// Commented By Dhaval 
			//Customer currentCustomer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

			if(customer.getRegion() != null && customer.getRegion().size() > 0){

				Set<Region> regionSet = customer.getRegion();

				Iterator<Region> regionIterator = regionSet.iterator();

				while(regionIterator.hasNext()) {

					Region regionObj = (Region) regionIterator.next();

					if (regionObj.getSite() != null && regionObj.getSite().size() > 0) {

						Set<Site> siteSet = regionObj.getSite();

						Iterator<Site> siteIterator = siteSet.iterator();

						while(siteIterator.hasNext()) {

							Site siteObj = (Site) siteIterator.next();

							JsonObject json = new JsonObject();
							json.addProperty("siteId", siteObj.getSiteId());
							json.addProperty("siteName", siteObj.getSiteName());
							siteList.add(json);

						}
					}
				}
			}

			logger.info("Getting site related details for customer : " + customer.getCustomerName());
			objectList.add(siteList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		
		@RequestMapping(value="/addConsumerRedirect",method=RequestMethod.GET)
		public ModelAndView addConsumerRedirect(HttpServletRequest request, HttpServletResponse response){

			User currentUser = (User) request.getSession().getAttribute("currentUser");	
			if(currentUser.getRole().getRoleName().equalsIgnoreCase("customer"))
				return new ModelAndView("addConsumer");
			else
				return new ModelAndView("consumerManagement");
		}


		@RequestMapping(value="/insertConsumer", method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> insertConsumer(HttpServletRequest request,HttpServletResponse response,
				@RequestParam(required=true,value="registerId") String registerId,

				@RequestParam(required=true,value="endpointSerialNumber") String endpointSerialNumber,
				@RequestParam(required=false,value="latitude") String latitude,
				@RequestParam(required=false,value="longitude") String longitude,
				@RequestParam(required=false,value="endpointIntegrity") String endpointIntegrity,
				@RequestParam(required=false,value="isRepeater") boolean isRepeater,
				@RequestParam(required=false,value="address1") String address1,
				@RequestParam(required=false,value="address2") String address2,
				@RequestParam(required=false,value="address3") String address3,
				@RequestParam(required=false,value="streetName") String streetName,
				@RequestParam(required=false,value="zipcode") String zipcode,

				@RequestParam(required=true,value="selectedBillingFrequency") String selectedBillingFrequency,
				@RequestParam(required=true,value="lastMeterReading") String lastMeterReading,
				@RequestParam(required=true,value="latsMeterReadingDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date latsMeterReadingDate,
				@RequestParam(required=true,value="meterReadingDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date meterReadingDate,
				@RequestParam(required=true,value="selectedTariffId") String selectedTariffId,
				@RequestParam(required=true,value="selectedSiteId") String selectedSiteId,
				@RequestParam(required=true,value="billingStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date billingStartDate,
				@RequestParam(required=false,value="unitOfMeasure") String unitOfMeasure,
				@RequestParam(required=false,value="noOfOccupants") Integer noOfOccupants){

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			ConsumerMeter consumerMeterObj = new ConsumerMeter();

			consumerMeterObj.setRegisterId(registerId);

			consumerMeterObj.setEndpointSerialNumber(endpointSerialNumber);
			if (latitude != null) {
				consumerMeterObj.setLatitude(Double.parseDouble(latitude));			
			}
			if (longitude != null) {
				consumerMeterObj.setLongitude(Double.parseDouble(longitude));
			}
			//consumerMeterObj.setEndpointIntegrity(endpointIntegrity);
			consumerMeterObj.setRepeater(isRepeater);
			consumerMeterObj.setStreetName(streetName);
			consumerMeterObj.setAddress1(address1);
			consumerMeterObj.setAddress2(address2);
			consumerMeterObj.setAddress3(address3);
			consumerMeterObj.setZipcode(zipcode);

			consumerMeterObj.setCustomer(customer);
			consumerMeterObj.setBillingFrequencyInDays(Integer.parseInt(selectedBillingFrequency));
			// consumerMeterObj.setLastReading(Integer.parseInt(lastMeterReading));

			consumerMeterObj.setLastMeterReading(Integer.parseInt(lastMeterReading));
			consumerMeterObj.setLastMeterReadingDate(latsMeterReadingDate);

			consumerMeterObj.setAssetInspectionDate(DateTimeConversionUtils.getDateInUTC(new Date()));
			
			consumerMeterObj.setTariffPlan(tariffPlanService.getTariffPlanById(Integer.parseInt(selectedTariffId)));
			consumerMeterObj.setSite(userService.getSiteDataBySiteId(selectedSiteId));
			consumerMeterObj.setCreatedBy(loggedUser.getUserId());
			consumerMeterObj.setCreatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
			consumerMeterObj.setBillDate(meterReadingDate);
			consumerMeterObj.setBillingStartDate(billingStartDate);
			consumerMeterObj.setActive(Boolean.TRUE);
			consumerMeterObj.setUnitOfMeasure(unitOfMeasure);
			consumerMeterObj.setNumberOfOccupants(noOfOccupants);
			consumerMeterObj.setIsCommissioned(Boolean.FALSE);

			try {
				consumerMeterService.addConsumerMeter(consumerMeterObj);
			} catch(Exception e) {

				if (e.getCause() != null){
						if(e.getCause().getMessage().contains("Duplicate entry")) {

					if (e.getCause().getMessage().contains("register_id_UNIQUE")) {
						logger.warn("Register Id '" + registerId + "' already exists.");
						return new ResponseEntity<String>(new Gson().toJson("consumercodeerror"),HttpStatus.OK);
					}

					if (e.getCause().getMessage().contains("endpoint_serial_number_UNIQUE")) {
						logger.warn("End point serial number '" + endpointSerialNumber + "' already exists.");
						return new ResponseEntity<String>(new Gson().toJson("serialnumbererror"),HttpStatus.OK);
					}

					return new ResponseEntity<String>(new Gson().toJson("consumercodeerror"),HttpStatus.OK);
				}
				} else {
					return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.UNKNOWNERROR),HttpStatus.OK);
				}
			}

			logger.info("Endp point successfully added.");
			return new ResponseEntity<String>(new Gson().toJson("added"),HttpStatus.OK);
		}


		@RequestMapping(value="/editConsumer",method=RequestMethod.POST)
		public ResponseEntity<String> editConsumer(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="consumerMeterId") String consumerMeterId){

			ConsumerMeter consumerMeterObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			List<JsonObject> tariffNames = new ArrayList<JsonObject>();
			List<JsonObject> billingFrequencyNames = new ArrayList<JsonObject>();
			List<JsonObject> siteList = new ArrayList<JsonObject>();

			if(consumerMeterObj != null){

				
				Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

				JsonObject jObject = new JsonObject();
				jObject.addProperty("consumerMeterId", consumerMeterObj.getConsumerMeterId());
				jObject.addProperty("registerId", consumerMeterObj.getRegisterId());

				if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {
					if(consumerMeterObj.getConsumer() != null)
						jObject.addProperty("conAccNumber", consumerMeterObj.getConsumer().getConsumerAccountNumber());
				}

				jObject.addProperty("endpointSerialNumber", consumerMeterObj.getEndpointSerialNumber());
				jObject.addProperty("latitude", consumerMeterObj.getLatitude());
				jObject.addProperty("longitude", consumerMeterObj.getLongitude());
				jObject.addProperty("endpointIntegrity", (consumerMeterObj.getTotalNoOfReads()!=null && consumerMeterObj.getTotalNoOfLatestReads()!=null && consumerMeterObj.getTotalNoOfReads()>0)?(100*consumerMeterObj.getTotalNoOfLatestReads()/consumerMeterObj.getTotalNoOfReads()):0);
				jObject.addProperty("isRepeater", consumerMeterObj.getRepeater());

				jObject.addProperty("address1", consumerMeterObj.getAddress1());
				jObject.addProperty("address2", consumerMeterObj.getAddress2());
				jObject.addProperty("address3", consumerMeterObj.getAddress3());
				jObject.addProperty("streetName", consumerMeterObj.getStreetName());
				jObject.addProperty("zipcode", consumerMeterObj.getZipcode());
				jObject.addProperty("unitOfMeasure", consumerMeterObj.getUnitOfMeasure());
				jObject.addProperty("noOfOccupants", consumerMeterObj.getNumberOfOccupants());

				jObject.addProperty("savedBillingFrequency", consumerMeterObj.getBillingFrequencyInDays());
				if (consumerMeterObj.getTariffPlan() != null) {
					jObject.addProperty("savedTariffId", consumerMeterObj.getTariffPlan().getTariffPlanId());
				}

				if (consumerMeterObj.getSite() != null) {
					jObject.addProperty("savedSiteId", consumerMeterObj.getSite().getSiteId());				
				}

				if (consumerMeterObj.getBillDate() != null) {
					jObject.addProperty("meterReadingDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getBillDate()));				
				}

				jObject.addProperty("totalEndpointAttached", consumerMeterObj.getTotalEndpointAttached());				

				if (consumerMeterObj.getRegisterId() != null) {

					List<ConsumerMeterTransaction> meterTransactionList = consumerMeterService.getConsumerMeterTransactionByRegisterId(consumerMeterObj.getRegisterId());

					Collections.sort(meterTransactionList, new Comparator<ConsumerMeterTransaction>() {
						public int compare(ConsumerMeterTransaction m1, ConsumerMeterTransaction m2) {
							return m2.getTimeStamp().compareTo(m1.getTimeStamp());
						}
					});

					if (meterTransactionList != null && meterTransactionList.size() > 0) {
						jObject.addProperty("batteryVoltage", meterTransactionList.get(0).getBatteryVoltage());
						jObject.addProperty("currentReadingDate", DateTimeConversionUtils.getStringFromDate(meterTransactionList.get(0).getTimeStamp()));
					}
				}

				if (consumerMeterObj.getEndpointInstalledDate() != null) {
					jObject.addProperty("installDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getEndpointInstalledDate()));
				}

				if (consumerMeterObj.getEndpointBatteryReplacedDate() != null) {
					jObject.addProperty("batteryReplacedDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getEndpointBatteryReplacedDate()));
				}

				if (consumerMeterObj.getAssetInspectionDate() != null) {
					jObject.addProperty("assetInspectionDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getAssetInspectionDate()));
				}

				if (consumerMeterObj.getDistrictUtilityMeter() != null) {
					jObject.addProperty("districtMeterSerialNo", consumerMeterObj.getDistrictUtilityMeter().getDistrictUtilityMeterSerialNumber());
				}

				jObject.addProperty("lastMeterReading", consumerMeterObj.getLastMeterReading());

				if (consumerMeterObj.getLastMeterReadingDate() != null) {
					jObject.addProperty("latsMeterReadingDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getLastMeterReadingDate()));				
				}

				if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("consumer")) {
					jObject.addProperty("hosepipe", consumerMeterObj.isHospipe());
					jObject.addProperty("irrigationSystem", consumerMeterObj.isIrrigationSystem());
					jObject.addProperty("swimmingPool", consumerMeterObj.isSwimmingPool());
					jObject.addProperty("hotTub", consumerMeterObj.isHotTub());
					jObject.addProperty("pond", consumerMeterObj.isPond());
				}

				if (consumerMeterObj.getBillingStartDate() != null) {
					jObject.addProperty("billingStartDate", DateTimeConversionUtils.getStringFromDate(consumerMeterObj.getBillingStartDate()));				
				}

				if (consumerMeterObj.getNotes() != null) {
					if (consumerMeterObj.getNotes().contains("],")) {

						String[] notes = consumerMeterObj.getNotes().split("],");
						jObject.addProperty("textNote", notes[0].substring(1, notes[0].length()));
						jObject.addProperty("filePath", notes[1].substring(1, (notes[1].length() - 1)));
						if (notes[1].indexOf("_") >= 0) {
							jObject.addProperty("originalFileName", notes[1].substring(notes[1].indexOf("_") + 1, notes[1].length() - 1));						
						}
					}
				}

				jObject.addProperty("isActive", consumerMeterObj.isActive());

				if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {

					Set<TariffPlan> tariffPlanSet = customer.getTariffPlan();

					Iterator<TariffPlan> tariffPlanIterator = tariffPlanSet.iterator();

					while(tariffPlanIterator.hasNext()){
						TariffPlan tariffPlan = (TariffPlan) tariffPlanIterator.next();

						JsonObject json = new JsonObject();
						json.addProperty("tariffId", tariffPlan.getTariffPlanId());
						json.addProperty("tariffName", tariffPlan.getTarrifPlanName());
						tariffNames.add(json);
					}

					jObject.addProperty("tariffList", new Gson().toJson(tariffNames));

					for (Entry<String, Integer> entry : KenureUtilityContext.billingFrequency.entrySet()) {
						JsonObject json = new JsonObject();
						json.addProperty("billingFrequencyName", entry.getKey());
						json.addProperty("billingFrequencyId", entry.getValue());
						billingFrequencyNames.add(json);
					}

					jObject.addProperty("billingFrequencyList", new Gson().toJson(billingFrequencyNames));

					Set<Region> regionSet = customer.getRegion();

					Iterator<Region> regionIterator = regionSet.iterator();

					while(regionIterator.hasNext()) {

						Region regionObj = (Region) regionIterator.next();

						if (regionObj.getSite() != null && regionObj.getSite().size() > 0) {

							Set<Site> siteSet = regionObj.getSite();

							Iterator<Site> siteIterator = siteSet.iterator();

							while(siteIterator.hasNext()) {

								Site siteObj = (Site) siteIterator.next();

								JsonObject json = new JsonObject();
								json.addProperty("siteId", siteObj.getSiteId());
								json.addProperty("siteName", siteObj.getSiteName());
								siteList.add(json);

							}
						}
					}

					jObject.addProperty("siteList", new Gson().toJson(siteList));

				} else {

					JsonObject json1 = new JsonObject();
					if (consumerMeterObj.getTariffPlan() != null) {
						json1.addProperty("tariffId", consumerMeterObj.getTariffPlan().getTariffPlanId());
						json1.addProperty("tariffName", consumerMeterObj.getTariffPlan().getTarrifPlanName());					
					}
					tariffNames.add(json1);

					jObject.addProperty("tariffList", new Gson().toJson(tariffNames));

					for (Entry<String, Integer> entry : KenureUtilityContext.billingFrequency.entrySet()) {

						if (consumerMeterObj.getBillingFrequencyInDays() == entry.getValue()) {
							JsonObject json2 = new JsonObject();
							json2.addProperty("billingFrequencyName", entry.getKey());
							json2.addProperty("billingFrequencyId", entry.getValue());
							billingFrequencyNames.add(json2);
						}

					}

					jObject.addProperty("billingFrequencyList", new Gson().toJson(billingFrequencyNames));

					JsonObject json3 = new JsonObject();

					if (consumerMeterObj.getSite() != null) {
						json3.addProperty("siteId", consumerMeterObj.getSite().getSiteId());
						json3.addProperty("siteName", consumerMeterObj.getSite().getSiteName());					
					}
					siteList.add(json3);

					jObject.addProperty("siteList", new Gson().toJson(siteList));
				} 

				request.getSession().setAttribute("responseJsonObject", jObject);

				logger.info("Getting data for this end point.");
				return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.DATAFETCHED),HttpStatus.OK);
			}

			logger.warn("No such end point found.");
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
		}


		@RequestMapping(value="/getEditConsumerEntity",method=RequestMethod.POST)
		public ResponseEntity<String> getEditConsumerEntity(HttpServletRequest request,HttpServletResponse response){

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			JsonObject jObject = (JsonObject) request.getSession().getAttribute("responseJsonObject");

			if(jObject != null ){

				if (loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {
					jObject.addProperty("isCustomer", true);
					logger.info("User role is customer.");
				} else {
					jObject.addProperty("isCustomer", false);
					logger.info("User role is consumer.");
				}

				request.getSession().removeAttribute("responseJsonObject");
				return 	new ResponseEntity<>(new Gson().toJson(jObject),HttpStatus.OK);
			}

			logger.warn("No such end point found.");
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
		}


		@RequestMapping(value="/updateConsumer",method=RequestMethod.POST)
		public ResponseEntity<String> updateConsumer(HttpServletRequest request,HttpServletResponse response,
				@RequestParam(required=true,value="consumerMeterId") String consumerMeterId,
				@RequestParam(required=false,value="registerId") String registerId,

				@RequestParam(required=false,value="endpointSerialNumber") String endpointSerialNumber,
				@RequestParam(required=false,value="latitude") String latitude,
				@RequestParam(required=false,value="longitude") String longitude,
				@RequestParam(required=false,value="endpointIntegrity") String endpointIntegrity,
				@RequestParam(required=false,value="isRepeater") boolean isRepeater,
				@RequestParam(required=false,value="address1") String address1,
				@RequestParam(required=false,value="address2") String address2,
				@RequestParam(required=false,value="address3") String address3,
				@RequestParam(required=false,value="streetName") String streetName,
				@RequestParam(required=false,value="zipcode") String zipcode,			

				@RequestParam(required=true,value="savedBillingFrequency") String savedBillingFrequency,
				@RequestParam(required=true,value="savedTariffId") String savedTariffId,
				@RequestParam(required=true,value="savedSiteId") String savedSiteId,
				@RequestParam(required=true,value="lastMeterReading") String lastMeterReading,
				@RequestParam(required=true,value="latsMeterReadingDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date latsMeterReadingDate,

				@RequestParam(required=false,value="hosepipe") boolean hosepipe,
				@RequestParam(required=false,value="irrigationSystem") boolean irrigationSystem,
				@RequestParam(required=false,value="swimmingPool") boolean swimmingPool,
				@RequestParam(required=false,value="hotTub") boolean hotTub,
				@RequestParam(required=false,value="pond") boolean pond,

				@RequestParam(value="meterReadingDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date meterReadingDate,
				@RequestParam(required=true,value="billingStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date billingStartDate,
				@RequestParam(required=false,value="isActive") boolean isActive,
				@RequestParam(required=false,value="unitOfMeasure") String unitOfMeasure,
				@RequestParam(required=false,value="noOfOccupants") Integer noOfOccupants){

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			//Removing previous session attribute that will not not used in future
			ConsumerMeter consumerMeterObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

			if (latitude != null) {
				consumerMeterObj.setLatitude(Double.parseDouble(latitude));
			}

			if (longitude != null) {
				consumerMeterObj.setLongitude(Double.parseDouble(longitude));
			}

			//consumerMeterObj.setEndpointIntegrity(endpointIntegrity);
			consumerMeterObj.setRepeater(isRepeater);
			consumerMeterObj.setAddress1(address1);
			consumerMeterObj.setAddress2(address2);
			consumerMeterObj.setAddress3(address3);
			consumerMeterObj.setStreetName(streetName);
			consumerMeterObj.setZipcode(zipcode);
			consumerMeterObj.setNumberOfOccupants(noOfOccupants);
			consumerMeterObj.setUnitOfMeasure(unitOfMeasure);
			consumerMeterObj.setBillingFrequencyInDays(Integer.parseInt(savedBillingFrequency));

			consumerMeterObj.setTariffPlan(tariffPlanService.getTariffPlanById(Integer.parseInt(savedTariffId)));
			consumerMeterObj.setSite(userService.getSiteDataBySiteId(savedSiteId));

			if(lastMeterReading !=null)
				consumerMeterObj.setLastMeterReading(Integer.parseInt(lastMeterReading));

			consumerMeterObj.setLastMeterReadingDate(latsMeterReadingDate);

			if (loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("consumer")) {
				consumerMeterObj.setHospipe(hosepipe);
				consumerMeterObj.setIrrigationSystem(irrigationSystem);
				consumerMeterObj.setSwimmingPool(swimmingPool);
				consumerMeterObj.setHotTub(hotTub);
				consumerMeterObj.setPond(pond);
			}
			consumerMeterObj.setBillDate(meterReadingDate);
			consumerMeterObj.setBillingStartDate(billingStartDate);
			if(isActive){
				consumerMeterObj.setActive(true);
				consumerMeterObj.setDeletedBy(null);
				consumerMeterObj.setDeletedTimeStamp(null);
			}else{
				consumerMeterObj.setActive(false);
				consumerMeterObj.setUpdatedBy(loggedUser.getUserId());
				consumerMeterObj.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
				consumerMeterObj.setDeletedBy(loggedUser.getUserId());
				consumerMeterObj.setDeletedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));
			}
			Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer");

			if(isNormalCustomer != null && isNormalCustomer){
				loggedUser = (User) request.getSession().getAttribute("normalCustomer");
			}else{
				loggedUser = (User) request.getSession().getAttribute("currentUser");
			}

			consumerMeterObj.setUpdatedBy(loggedUser.getUserId());
			consumerMeterObj.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));

			if(consumerMeterService.updateConsumer(consumerMeterObj)){
				logger.info("Details successfully updated for this endpoint.");
				return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
			}

			logger.error("Error occurred while updating end point details.");
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
		}

		@RequestMapping(value="/checkAlerts",method=RequestMethod.GET)
		public @ResponseBody ResponseEntity<String> checkAlerts(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(required = true, value="consumerMeterId") String consumerMeterId){

			ConsumerMeter consumerObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

			if (consumerObj.getRegisterId() != null) {

				List<ConsumerMeterTransaction> meterTransactionList = consumerMeterService.getConsumerMeterTransactionByRegisterId(consumerObj.getRegisterId());

				// Code to find meter transaction with latest time stamp
				Collections.sort(meterTransactionList, new Comparator<ConsumerMeterTransaction>() {
					public int compare(ConsumerMeterTransaction m1, ConsumerMeterTransaction m2) {
						return m2.getTimeStamp().compareTo(m1.getTimeStamp());
					}
				});

				if (meterTransactionList != null && meterTransactionList.size() > 0) {

					if (meterTransactionList.get(0).getAlerts() != null) {

						JsonObject json = new JsonObject();

						json.addProperty("meterTransactionId", meterTransactionList.get(0).getId());
						json.addProperty("alerts", meterTransactionList.get(0).getAlerts());
						// Commented out below known code
						/*if (alerts.length > 0) {
							json.addProperty("leak", alerts[0]);						
						}

						if (alerts.length > 1) {
							json.addProperty("tamper", alerts[1]);
						}

						if (alerts.length > 2) {
							json.addProperty("backFlow", alerts[2]);
						}

						if (alerts.length > 3) {
							json.addProperty("battery", alerts[3]);
						}*/

						json.addProperty("isAlertAcknowledged", meterTransactionList.get(0).isAlerts_ack());

						logger.info("Checking alerts for this end point.");

						return new ResponseEntity<String>(new Gson().toJson(json), HttpStatus.OK);
					}
				}
			}

			logger.info("No alerts found.");
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOALERTS),HttpStatus.OK);
		}


		@RequestMapping(value="/alertAcknowledgement",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> alertAcknowledgement(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(required = true, value="meterTransactionId") String meterTransactionId){

			if(meterTransactionId.matches("^-?\\d+$") && (meterTransactionId != null || !meterTransactionId.trim().isEmpty())){

				if(consumerMeterService.updateAlertAcknowledgementStatusByMeterTransactionId(Integer.parseInt(meterTransactionId), false)){
					logger.info("Alert details successfully acknowledged.");
					return new ResponseEntity<String>(new Gson().toJson("Successfully acknowledged"), HttpStatus.OK);
				}
				else{
					logger.error("Alert details not successfully acknowledged.");
					return new ResponseEntity<String>(new Gson().toJson("No such meter transaction found"), HttpStatus.OK);
				}
			}

			logger.error("Errors occurred while updating alert acknowledgement details.");
			return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);		
		}
		
		@RequestMapping(value="/uploadNote",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> uploadNote(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(required = true, value="consumerMeterId") String consumerMeterId,
				@RequestParam(value="textNote") String textNote,
				@RequestParam("file") MultipartFile file,
				@RequestParam("removeUpload") boolean removeUpload) {

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			if (file.getContentType().contains("image/") || file.getContentType().contains("text/") 
					|| file.getContentType().contains("notype")) {

				logger.info("Supported note file.");

				if (textNote.equalsIgnoreCase("undefined")) {
					textNote = "";
					logger.info("Text note is empty.");
				}

				ConsumerMeter consumerObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

				String uploadFileName = file.getOriginalFilename();

				if (uploadFileName != null) {
					logger.info("Original file name is : "+uploadFileName);
				}

				// For future use: for getting extension of file
				//String extension = uploadFileName.substring(uploadFileName.lastIndexOf('.'));

				String tempFileName = consumerMeterId + "_" + uploadFileName;

				if (createMediaDirectoresIfNotExists(servletContext.getRealPath(TEMP_NOTES_PATH))) {

					if (removeUpload && file.getOriginalFilename().equalsIgnoreCase("noFile")) {

						if (consumerObj.getNotes() != null) {
							if (consumerObj.getNotes().contains("],")) {

								String[] notes = consumerObj.getNotes().split("],");

								if (notes[1] != null && notes[1].length() > 2) {

									File deleteFile = null;

									try {

										deleteFile = new File(servletContext.getRealPath(WEB_INF_PATH)+"\\"+notes[1].substring(1, (notes[1].length() - 1)));

										if (deleteFile.exists()) {
											deleteFile.delete();						    		
										}

									}catch(Exception e){
										logger.error(e.getMessage());
									}
								}
							}
						}

						consumerObj.setNotes("["+textNote.trim()+"],[]");

						consumerObj.setUpdatedBy(loggedUser.getUserId());
						consumerObj.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));

						if(consumerMeterService.updateConsumer(consumerObj)){
							logger.info("Note successfully uploaded.");
							return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
						}
					} else if ((removeUpload && !file.getOriginalFilename().equalsIgnoreCase("noFile")) || 
							!removeUpload && !file.getOriginalFilename().equalsIgnoreCase("noFile")) {

						if (consumerObj.getNotes() != null) {
							if (consumerObj.getNotes().contains("],")) {

								String[] notes = consumerObj.getNotes().split("],");

								if (notes[1] != null && notes[1].length() > 2) {

									File deleteFile = null;

									try {

										deleteFile = new File(servletContext.getRealPath(WEB_INF_PATH)+"\\"+notes[1].substring(1, (notes[1].length() - 1)));

										if (deleteFile.exists()) {
											deleteFile.delete();						    		
										}

									}catch(Exception e){
										logger.error(e.getMessage());
									}
								}
							}
						}

						FileOutputStream fos = null;
						try {
							File newFile = new File(servletContext.getRealPath(TEMP_NOTES_PATH)+"\\"+tempFileName);
							fos = new FileOutputStream(newFile);
						} catch (FileNotFoundException e) {
							logger.error(e.getMessage());
						}
						try {
							if (fos != null) {
								fos.write(file.getBytes());
								fos.close();
							}
						} catch (IOException e) {
							logger.error(e.getMessage());
						}

						consumerObj.setNotes("["+textNote.trim()+"],[resources\\notes\\"+tempFileName+"]");

						consumerObj.setUpdatedBy(loggedUser.getUserId());
						consumerObj.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));

						if(consumerMeterService.updateConsumer(consumerObj)){
							logger.info("Note successfully uploaded.");
							return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
						}

					} else {

						if (consumerObj.getNotes() != null) {
							if (consumerObj.getNotes().contains("],")) {

								String[] notes = consumerObj.getNotes().split("],");

								if (notes[1] != null && notes[1].length() > 2) {

									File deleteFile = null;

									try {

										deleteFile = new File(servletContext.getRealPath(WEB_INF_PATH)+"\\"+notes[1].substring(1, (notes[1].length() - 1)));

										if (deleteFile.exists()) {
											deleteFile.delete();						    		
										}

									}catch(Exception e){
										logger.error(e.getMessage());
									}
								}
							}
						}

						consumerObj.setNotes("["+textNote.trim()+"],[]");

						consumerObj.setUpdatedBy(loggedUser.getUserId());
						consumerObj.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));

						if(consumerMeterService.updateConsumer(consumerObj)){
							logger.info("Note successfully uploaded.");
							return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
						}
					}
				}
			} else {
				logger.warn("Un supported file type.");
				return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UNSUPPORTEDFILETYPE),HttpStatus.OK);
			}

			logger.error("Note successfully uploaded.");
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
		}


		@RequestMapping(value="/getNotes",method=RequestMethod.GET)
		public @ResponseBody ResponseEntity<String> getNotes(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(required = true, value="consumerMeterId") String consumerMeterId){

			ConsumerMeter consumerObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

			JsonObject jObject = new JsonObject();

			if (consumerObj.getNotes() != null) {
				if (consumerObj.getNotes().contains("],")) {

					String[] notes = consumerObj.getNotes().split("],");
					jObject.addProperty("textNote", notes[0].substring(1, notes[0].length()));
					jObject.addProperty("filePath", notes[1].substring(1, (notes[1].length() - 1)));
					if (notes[1].indexOf("_") >= 0) {
						jObject.addProperty("originalFileName", notes[1].substring(notes[1].indexOf("_") + 1, notes[1].length() - 1));		
						logger.info("Original file name is : " + notes[1].substring(notes[1].indexOf("_") + 1, notes[1].length() - 1));
					}
				}
			}

			logger.info("Getting notes details for this end point.");
			return 	new ResponseEntity<>(new Gson().toJson(jObject),HttpStatus.OK);
		}

		@RequestMapping(value="/billingData",method=RequestMethod.POST)
		public ResponseEntity<String> billingData(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="consumerMeterId") String consumerMeterId,
				@RequestParam(value="registerId") String registerId){
			
			request.getSession().setAttribute("consumerMeterId", consumerMeterId);
			request.getSession().setAttribute("registerId",registerId);
			return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
		}

		@RequestMapping(value="/getbillingHistoryData",method=RequestMethod.POST)
		public ResponseEntity<String> getbillingHistoryData(HttpServletRequest request,HttpServletResponse response){
			String consumerMeterId = (String) request.getSession().getAttribute("consumerMeterId");
			String registerId = (String) request.getSession().getAttribute("registerId");
			List<BillingHistory> billingHistoryList = userService.getBillingHistoryByConsumerMeterId(consumerMeterId);
			
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			List<JsonObject> billingDataList = new ArrayList<JsonObject>();
			List<Object> objectList = new ArrayList<Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
			
				Iterator<BillingHistory> billingIterator = billingHistoryList.iterator();
				while(billingIterator.hasNext()){
					
					BillingHistory billingHistory = billingIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("billingStartDate", sdf.format(billingHistory.getBillingStartDate()));
					json.addProperty("billingEndDate", sdf.format(billingHistory.getBillingEndDate()));
					json.addProperty("currentReading", billingHistory.getCurrentReading().toString());
					json.addProperty("lastReading", billingHistory.getLastReading().toString());
					json.addProperty("consumedUnit",billingHistory.getConsumedUnit().toString());
					json.addProperty("totalAmount", billingHistory.getTotalAmount().toString());
					json.addProperty("billDate", sdf.format(billingHistory.getBillDate()));
					json.addProperty("registerId", registerId);
					billingDataList.add(json);
				}
				JsonObject jObject = new JsonObject();

				if (loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {
					jObject.addProperty("isCustomer", true);
					logger.info("User role is Customer.");
				} else {
					jObject.addProperty("isCustomer", false);
					logger.info("User role is Consumer.");
				}

				objectList.add(jObject);
				objectList.add(billingDataList);
				return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		
		
		/*
		 * This method checks whether given input directory path and/or file exists or not, if not
		 * then it creates directory path and/or file
		 */
		private boolean createMediaDirectoresIfNotExists(String directoryPath)
		{
			boolean result = false;
			File dir = new File(directoryPath);
			if(!dir.exists())
			{
				try
				{
					result = dir.mkdirs();
					if (result) {
						logger.info("Directory successfully created for saving notes.");
					}
				}
				catch(Exception e)
				{
					result = false;
					logger.error(e.getMessage());
				}
			}
			else
			{
				result = true;
				logger.info("Directory already exists for saving notes.");
			}

			return result;
		}
		
		@RequestMapping(value="/deleteConsumer",method=RequestMethod.POST)
		public @ResponseBody ResponseEntity<String> deleteConsumer(HttpServletRequest request,HttpServletResponse response,@RequestParam("consumerMeterId") String consumerMeterId){

			User loggedUser = (User) request.getSession().getAttribute("currentUser");

			if(consumerMeterId.matches("^-?\\d+$") && (consumerMeterId != null || !consumerMeterId.trim().isEmpty())){

				if(consumerMeterService.deleteConsumer(Integer.parseInt(consumerMeterId), loggedUser.getUserId())){

					logger.info("End point successfully deleted.");
					return new ResponseEntity<String>(new Gson().toJson("Endpoint successfully deleted"), HttpStatus.OK);
				}
				else{

					logger.warn("No such end point found.");
					return new ResponseEntity<String>(new Gson().toJson("No such meter found"), HttpStatus.OK);
				}
			}

			logger.error("Error occurred while deleting end point.");
			return new ResponseEntity<String>(new Gson().toJson("endpointismapped"), HttpStatus.OK);
		}
		
	//Customer Site ManagementData triggered from here
	@RequestMapping(value="/customersitedata",method=RequestMethod.POST)
	public ResponseEntity<String> customerSiteData(HttpServletRequest request,HttpServletResponse response){

		// Fetching current user from session
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		List<Region> list = new ArrayList<>();
		List<String> customerRegionName = new ArrayList<String>();
		List<String> dataCollectorNameList = new ArrayList<String>();
		List<String> assignCollectorList = new ArrayList<String>();
		List<String> installerName = new ArrayList<String>();
		List<String> dumNames = new ArrayList<>();

		if(currentUser != null){
			Customer currentCustomer = userService.getCustomerDetailsByUser(currentUser.getUserId());
			// Getting All Region
			if(currentCustomer.getRegion() != null){

				list.addAll(currentCustomer.getRegion());
				customerRegionName = list.stream().map(Region::getRegionName).collect(Collectors.toList());

				// Getting Site and Than All DC
				if(currentCustomer.getDataCollector() != null){

					Iterator<DataCollector> iteratorDataCollector = currentCustomer.getDataCollector().iterator();

					while(iteratorDataCollector.hasNext()){
						DataCollector dc = (DataCollector) iteratorDataCollector.next();
						if(dc.getSite() != null)
							dataCollectorNameList.add(dc.getDcSerialNumber());
						else
							assignCollectorList.add(dc.getDcSerialNumber());
					}
				}
			}

			// Getting All Installers
			if(currentCustomer.getInstaller() != null){
				currentCustomer.getInstaller().iterator().forEachRemaining(n ->{
					installerName.add(n.getInstallerName());
				});
			}

			// Getting all customer UM
			if(currentCustomer.getDistrictUtilityMeter() != null){
				currentCustomer.getDistrictUtilityMeter().iterator().forEachRemaining(n -> {
					dumNames.add(String.valueOf(n.getDistrictUtilityMeterSerialNumber()));
				});
			}

		}

		// Final Object Creation to send response
		List<Object> responseList = new ArrayList<>();

		responseList.add(customerRegionName);
		responseList.add(dataCollectorNameList);
		responseList.add(assignCollectorList);
		responseList.add(installerName);
		responseList.add(dumNames);

		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	// Search site by site name
	@RequestMapping(value="/searchSiteByNameOrRegionName",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> searchUser(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="siteSearchCriteria",required=false) String siteSearchCriteria,
			@RequestParam(value="siteSearchRegon",required=false) String siteSearchRegon){

		List<Site> searchedList = userService.searchSiteByNameOrRegion(siteSearchCriteria,siteSearchRegon);

		if(searchedList != null && searchedList.size() > 0){

			List<JsonObject> siteList = new ArrayList<JsonObject>();
			/*Iterator<Site> siteIterator = searchedList.iterator();*/

			searchedList.forEach(n -> {
				JsonObject json = new JsonObject();

				json.addProperty("siteId", n.getSiteId());
				json.addProperty("siteName", n.getSiteName());
				json.addProperty("createdBy", n.getCreatedBy());
				json.addProperty("isActive", n.isActiveStatus());
				json.addProperty("region", n.getRegion().getRegionName().toString());
				siteList.add(json);
			});
			return new ResponseEntity<Object>(new Gson().toJson(siteList), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOSITEFOUND),HttpStatus.OK);
	}

	// Adding New Site
	@RequestMapping(value="/addNewSite" , method=RequestMethod.POST)
	public void addNewSite(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="siteId",required=false) String siteId,
			@RequestParam(value="siteName") String siteName,
			@RequestParam(value="selectedRegion") String selectedRegion,
			@RequestParam(value="boundryDC",required=false) String[] boundryDC,
			@RequestParam(value="assignDC",required=false) String[] assignDC,
			@RequestParam(value="installerName",required=false) String installerName,
			@RequestParam(value="dmsn",required=false) String dmsn){

		// Getting dependency of customer
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		// Getting selected region 
		Region selectRegion = userService.getSelectedRegionByName(selectedRegion,customer);

		// Getting Installer for newDC only if assigned dc available
		Installer installer = null;
		if(installerName != null && !installerName.isEmpty()) {
			installer = userService.getInstallerByNameAndCustomer(installerName,customer);
		}

		/*Set<DataCollector> dcSet = new HashSet<>(Arrays.asList());*/
		Site site = new Site();
		site.setActiveStatus(Boolean.TRUE);
		site.setCreatedBy(customer.getUser().getUserId()); // Need to discuss
		site.setSiteName(siteName);
		site.setRegion(selectRegion);
		site.setTag(1);
		site.setCreatedTs(new Date());
		site.setNoOfDcFile(0);
		site.setNoOfEndpointFile(0);

		List<DataCollector> dcList = new ArrayList<DataCollector>();

		// Setting assigned DC for new site

		if(assignDC != null ){
			dcList = userService.getDCBySerialNumber(assignDC);
			dcList.stream().forEach(n -> {
				n.setSite(site);
			});
			site.setDatacollector(new HashSet<>(dcList));
			/*for(int i=0;i<assignDC.length;i++){
				DataCollector thisCollector = dcList.get(i);
				thisCollector.setCustomer(customer);

				if(thisCollector.getCreatedTs() == null){

					thisCollector.setCreatedTs(new Date());
				}else{
					thisCollector.setUpdatedTs(new Date());
				}
				thisCollector.setSite(site);
				if(installer != null)
				thisCollector.setInstaller(installer);
				userService.addNewCollector(thisCollector);
			}*/
		}

		// Setting boundry data collector
		if(boundryDC != null){
			Set<BoundryDataCollector> bdcSet = new HashSet<>();
			dcList = userService.getDCBySerialNumber(boundryDC);
			for(int i=0;i<boundryDC.length;i++){
				BoundryDataCollector bdc = new BoundryDataCollector();
				bdc.setSite(site);
				DataCollector thisBoundryDCCollector = dcList.get(i);
				bdc.setDatacollector(thisBoundryDCCollector);
				/*userService.addNewBDC(bdc);*/
				bdcSet.add(bdc);
			}
			site.setBoundrydatacollector(bdcSet);
			System.out.println("Size >"+bdcSet.size());
			userService.saveSite(site);	
		}else{
			userService.saveSite(site);
		}


	}


	//for getting Region list
	@RequestMapping(value="/getRegion",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getRegion(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<JsonObject> regionJsonList = new ArrayList<JsonObject>();

		List<Region> regionList = userService.getRegionListByCustomerId(customer.getCustomerId());

		if(regionList != null){
			Iterator<Region> regionIterator = regionList.iterator();

			while(regionIterator.hasNext()){
				Region region = regionIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("regionId", region.getRegionId());
				json.addProperty("regionName", region.getRegionName());
				json.addProperty("countryName", region.getCountry().getCountryName());
				json.addProperty("currencyName", region.getCurrency().getCurrencyName());
				json.addProperty("timezoneValue", region.getTimeZone());
				regionJsonList.add(json);
			}

			return new ResponseEntity<Object>(new Gson().toJson(regionJsonList), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new Gson().toJson("youCanNotAccessRegionList"), HttpStatus.OK);
	}

	//for adding region
	@RequestMapping(value="/addRegion",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addRegion(HttpServletRequest request,HttpServletResponse response,@RequestParam(required=true,value="region") String regionName,
			@RequestParam(required=true,value="selectedCountryId") String selectedCountryId,
			@RequestParam(required=true,value="selectedCurrencyId") String selectedCurrencyId,
			@RequestParam(required=true,value="selectedTimeZone") String selectedTimeZone){

		boolean isRegionValid = userService.checkForRegion(regionName);

		if(!isRegionValid){
			return new ResponseEntity<String>(new Gson().toJson("regionalreadyexist"),HttpStatus.OK);
		}

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Region region = new Region();
		region.setCustomer(customer);
		region.setRegionName(regionName);
		region.setCountry(userService.getCountryById(selectedCountryId));
		region.setCurrency(userService.getCurrencyById(selectedCurrencyId));
		region.setTimeZone(selectedTimeZone);

		userService.addNewRegion(region);
		logger.info("Region successfully added...");
		return new ResponseEntity<String>(new Gson().toJson("regionSuccessfullyadded"), HttpStatus.OK);

	}

	@RequestMapping(value = "/editRegion",method = RequestMethod.GET)
	public ResponseEntity<String> editRegion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="regionId",required= false) String regionId) {
		if(regionId != null){
			Region region = userService.getRegionById(Integer.parseInt(regionId));

			request.getSession().setAttribute("editRegion", region);

			return new ResponseEntity<String>(new Gson().toJson("editRegionData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchRegionFound"), HttpStatus.OK);
	}


	@RequestMapping(value="/getEditRegion",method=RequestMethod.GET)
	public ResponseEntity<String> getEditRegion(HttpServletRequest request,HttpServletResponse response){

		Region region = (Region) request.getSession().getAttribute("editRegion");

		// List<JsonObject> objectList = new ArrayList<JsonObject>();
		List<Object> addObjectList = new ArrayList<Object>();

		List<JsonObject> countryList = new ArrayList<JsonObject>();
		List<JsonObject> currencyList = new ArrayList<JsonObject>();

		List<Country> countries = userService.getCountryList();

		if(countries != null && !countries.isEmpty()){

			Iterator<Country> countryIterator = countries.iterator();

			while(countryIterator.hasNext()){

				Country country = countryIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("countryId", country.getCountryId()); 
				json.addProperty("countryName", country.getCountryName());
				countryList.add(json);
			}
		}

		List<Currency> currencies = userService.getCurrencyList();

		if(currencies != null && !currencies.isEmpty()){

			Iterator<Currency> currencyIterator = currencies.iterator();

			while(currencyIterator.hasNext()){

				Currency currency = currencyIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("currencyId", currency.getCurrencyId()); 
				json.addProperty("currencyName", currency.getCurrencyName());
				currencyList.add(json);
			}
		}

		if(region!= null){

			JsonObject json = new JsonObject();
			json.addProperty("regionName", region.getRegionName()); 
			json.addProperty("regionId",  region.getRegionId());
			json.addProperty("addEditFlag", "edit");
			json.addProperty("savedCountryId",  region.getCountry().getCountryId());
			json.addProperty("savedCurrencyId",  region.getCurrency().getCurrencyId());
			json.addProperty("savedTimeZone", region.getTimeZone());
			addObjectList.add(json);
			request.getSession().removeAttribute("editRegion");

			addObjectList.add(countryList);
			addObjectList.add(currencyList);
			addObjectList.add(new Gson().toJson(KenureUtilityContext.timeZone));

		} else {
			JsonObject json = new JsonObject();
			json.addProperty("addEditFlag", "add");
			addObjectList.add(json);
			addObjectList.add(countryList);
			addObjectList.add(currencyList);
			addObjectList.add(new Gson().toJson(KenureUtilityContext.timeZone));
		}

		return new ResponseEntity<>(new Gson().toJson(addObjectList),HttpStatus.OK);
	}

	@RequestMapping(value = "/updateRegion",method = RequestMethod.POST)
	public ResponseEntity<String> updateRegion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required=true,value="regionId") String regionId,
			@RequestParam(required=true,value="regionName") String regionName,
			@RequestParam(required=true,value="savedCountryId") String savedCountryId,
			@RequestParam(required=true,value="savedCurrencyId") String savedCurrencyId,
			@RequestParam(required=true,value="savedTimeZone") String savedTimeZone) {

		User loggedUser;

		if(regionId != null){
			Region regionObj = userService.getRegionById(Integer.parseInt(regionId));

			if(regionObj.getRegionName().equalsIgnoreCase(regionName)){

				regionObj.setCountry(userService.getCountryById(savedCountryId));
				regionObj.setCurrency(userService.getCurrencyById(savedCurrencyId));
				regionObj.setTimeZone(savedTimeZone);
				if((loggedUser = KenureUtilityContext.isNormalCustomer(request)) != null){
					regionObj.setUpdatedBy(loggedUser.getUserId());
					regionObj.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				}
				userService.updateRegion(regionObj);
				return new ResponseEntity<String>(new Gson().toJson("regionUpdatedSuccessfully"), HttpStatus.OK);

			}else{

				boolean isRegionValid = userService.checkForRegion(regionName);

				if(!isRegionValid){
					return new ResponseEntity<String>(new Gson().toJson("regionalreadyexist"),HttpStatus.OK);
				}

				regionObj.setRegionName(regionName);
				regionObj.setCountry(userService.getCountryById(savedCountryId));
				regionObj.setCurrency(userService.getCurrencyById(savedCurrencyId));
				regionObj.setTimeZone(savedTimeZone);
				if((loggedUser = KenureUtilityContext.isNormalCustomer(request)) != null){
					regionObj.setUpdatedBy(loggedUser.getUserId());
					regionObj.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				}
				userService.updateRegion(regionObj);
				return new ResponseEntity<String>(new Gson().toJson("regionUpdatedSuccessfully"), HttpStatus.OK);

			}


		}
		return new ResponseEntity<String>(new Gson().toJson("Region id can not be null"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchRegion",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> searchRegion(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="regionName") String regionName){

		User loggedUser = (User) req.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();

		logger.info("searching region name : "+ regionName);
		List<Region> searchedList = userService.searchRegion(regionName, customerId);
		List<JsonObject> regionList = new ArrayList<JsonObject>();

		if(searchedList.size() > 0){

			for(int i=0;i<searchedList.size();i++){

				JsonObject json = new JsonObject();
				json.addProperty("regionId", searchedList.get(i).getRegionId());
				json.addProperty("regionName", searchedList.get(i).getRegionName());
				json.addProperty("countryName", searchedList.get(i).getCountry().getCountryName());
				json.addProperty("currencyName", searchedList.get(i).getCurrency().getCurrencyName());
				json.addProperty("timezoneValue", searchedList.get(i).getTimeZone());
				regionList.add(json);
			}
			return new ResponseEntity<Object>(new Gson().toJson(regionList), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new Gson().toJson(ApplicationConstants.NOREGIONFOUND), HttpStatus.OK);
	}

	@RequestMapping(value="/customerSiteInitialData",method=RequestMethod.POST)
	public ResponseEntity<Object> customerInitData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.initializeSiteAndDCForCustomer(loggedUser.getUserId());

		List<JsonObject> siteList = new ArrayList<JsonObject>();
		List<Object> responseList = new ArrayList<Object>();
		// Getting All Region
		if(customer.getRegion() != null){

			Iterator<Region> regionIterator = customer.getRegion().iterator();

			while(regionIterator.hasNext()){
				Region currentRegion = (Region) regionIterator.next();
				Iterator<Site> iteratorSite = currentRegion.getSite().iterator();

				while(iteratorSite.hasNext()){

					Site currentSite = (Site) iteratorSite.next();
					currentSite.getRegion().setMaintenancetechnician(new MaintenanceTechnician());
					JsonObject json = new JsonObject();
					json.addProperty("siteId", currentSite.getSiteId());
					json.addProperty("siteName", currentSite.getSiteName());
					json.addProperty("createdBy", currentSite.getCreatedBy());
					json.addProperty("isActive", currentSite.isActiveStatus());
					json.addProperty("region", currentSite.getRegion().getRegionName().toString());
					siteList.add(json);
				}
			}
		}	
		siteList.sort((JsonObject jObject1, JsonObject jObject2) -> jObject1.get("siteName").getAsString().compareTo(jObject2.get("siteName").getAsString())); // Sortinglist for better front end view result.
		responseList.add(siteList);
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		JsonObject jObject = new JsonObject();
		if(isNormalCustomer != null && isNormalCustomer){
			responseList.add(Boolean.FALSE);
		}else{
			responseList.add(Boolean.TRUE);
		}

		return new ResponseEntity<Object>(new Gson().toJson(responseList), HttpStatus.OK);
	}

	@RequestMapping(value="/genereateCustomerData",method=RequestMethod.POST)
	public ResponseEntity<Object> customerEditSiteData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId",required=false) String siteId){

		JsonObject json = new JsonObject();
		List<String> installerName = new ArrayList<>();

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getCustomerDetailsByUser(currentUser.getUserId());

		// Customer Installer
		if(currentUser != null && currentCustomer.getInstaller() != null){
			currentCustomer.getInstaller().iterator().forEachRemaining(n ->{
				installerName.add(n.getInstallerName());
			});
		}	

		Site siteData = userService.getSiteDataBySiteId(siteId);

		if(!(siteData.getDatacollector().size() > 0)){
			json.addProperty("isInstallerActive", new Gson().toJson(Boolean.FALSE));
		}else{
			json.addProperty("isInstallerActive", new Gson().toJson(Boolean.TRUE));
		}

		if(siteData != null){

			// Here we set response of current site
			List<String> dataCollectorNameList = new ArrayList<String>();
			List<String> assignCollectorList = new ArrayList<String>();

			Iterator<DataCollector> iteratorDataCollector = currentCustomer.getDataCollector().iterator();

			while(iteratorDataCollector.hasNext()){
				DataCollector dc = (DataCollector) iteratorDataCollector.next();
				if(dc.getSite() != null)
					dataCollectorNameList.add(dc.getDcSerialNumber());
				else
					assignCollectorList.add(dc.getDcSerialNumber());
			}

			// Getting all current customer's selected option
			List<String> selectedBdc = new ArrayList<String>();

			if(siteData.getBoundrydatacollector()!= null){
				selectedBdc = siteData.getBoundrydatacollector().stream().map(n -> n.getDatacollector().getDcSerialNumber()).
						collect(Collectors.toList());

				if(siteData.getDatacollector().stream().findFirst().isPresent()){
					if(siteData.getDatacollector().stream().findFirst().get().getInstaller() != null){
						selectedInstallerName = siteData.getDatacollector().stream().findFirst().get().getInstaller().getInstallerName();
					}else{
						selectedInstallerName="";
					}
				}else{
					selectedInstallerName="";
				}


			}

			// Maintain ordering of selectedBdc first in dataCollectorNameList
			for(int i=0;i<selectedBdc.size();i++){
				if(dataCollectorNameList.contains(selectedBdc.get(i))){
					dataCollectorNameList.remove(selectedBdc.get(i));
					dataCollectorNameList.add(0, selectedBdc.get(i));
				}
			}


			// Getting all region name

			List<String> customerRegionName = new ArrayList<String>();
			List<Region> list = new ArrayList<>();
			list.addAll(currentCustomer.getRegion());
			customerRegionName = list.stream().map(Region::getRegionName).collect(Collectors.toList());
			json.addProperty("siteId", siteData.getSiteId());
			json.addProperty("siteName", siteData.getSiteName());
			json.addProperty("status", siteData.isActiveStatus());
			json.addProperty("installerName", new Gson().toJson(installerName));
			json.addProperty("dcList", new Gson().toJson(dataCollectorNameList));
			json.addProperty("assignCollector", new Gson().toJson(assignCollectorList));
			json.addProperty("customerRegion", new Gson().toJson(customerRegionName));

			String selectedRegion = siteData.getRegion().getRegionName();

			json.addProperty("selectedBdc", new Gson().toJson(selectedBdc));
			/*json.addProperty("selectedDc", new Gson().toJson(selectedDc));*/
			json.addProperty("selectedInstallerName", new Gson().toJson(selectedInstallerName));
			json.addProperty("selectedRegion", new Gson().toJson(selectedRegion));

			request.getSession().setAttribute("responseData", json);

			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.DATAFETCHED),HttpStatus.OK);	
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
	}

	@RequestMapping(value="/setGenerateCustomerEditData",method=RequestMethod.GET)
	public ResponseEntity<String> setGenerateCustomerEditData(HttpServletRequest request,HttpServletResponse response){
		JsonObject json = new JsonObject();
		json = (JsonObject) request.getSession().getAttribute("responseData");
		request.getSession().removeAttribute("responseData");
		return new ResponseEntity<>(new Gson().toJson(json),HttpStatus.OK);
	}

	@RequestMapping(value="/deleteSite",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteSite(HttpServletRequest request,HttpServletResponse response,@RequestParam("siteId") String siteId){	

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(currentUser.getUserId());

		if(siteId.matches("^-?\\d+$") && (siteId != null || !siteId.trim().isEmpty())){
			if(userService.deleteSite(Integer.parseInt(siteId),customer.getCustomerId())){
				logger.info("Site successfully deleted with id " + siteId);
				return new ResponseEntity<String>(new Gson().toJson("Site successfully deleted"), HttpStatus.OK);
			}
			else{
				logger.info("no such Site found with Site Id " + siteId);
				return new ResponseEntity<String>(new Gson().toJson("No such Site found"), HttpStatus.OK);
			}
		}
		logger.warn("error while parsing query parameter with siteId "+siteId);
		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
	}

	@RequestMapping(value="/editCurrentSite",method=RequestMethod.POST)
	public ResponseEntity<String> editCurrentSiteData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId") String siteId,
			@RequestParam(value="siteName") String siteName,
			@RequestParam(value="boundryDC", required=false) String[] boundryDC,
			@RequestParam(value="assignDC", required=false) String[] assignDC,
			@RequestParam(value="installer", required=false) String installer,
			@RequestParam(value="region") String regionName,
			@RequestParam(value="status") String status){


		/*User currentUser = (User) request.getSession().getAttribute("currentUser");*/

		// Get Site by its ID and setting up new name :D and new Updated TS and Id etc...
		Site site = userService.loadSiteDataBySiteIdAndDeleteBDC(siteId);

		Set<DataCollector> dcSet = site.getDatacollector();
		userService.initializeConsumerMeters(dcSet);

		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;
		if(isNormalCustomer != null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute("normalCustomer");
		}else{
			loggedUser = (User) request.getSession().getAttribute("currentUser");
		}
		Customer currentCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Region region = userService.getRegionByName(currentCustomer,regionName);	
		Installer ins = userService.getInstallerByNameAndCustomer(installer, currentCustomer);

		// GET DC BY SERIAL NUMBER
		List<DataCollector> dc = new ArrayList<DataCollector>();
		if(assignDC != null)
			dc = userService.getDCBySerialNumber(assignDC);

		dc.stream().forEach(n -> {
			n.setInstaller(ins);
		});

		// First Adding new dc with our new edited site.

		if(dc.size() > 0){
			for(int i=0;i<dc.size();i++){
				DataCollector thisCollector = dc.get(i);
				thisCollector.setCustomer(currentCustomer);
				thisCollector.setSite(site);
				if(thisCollector.getCreatedTs() == null){
					thisCollector.setCreatedTs(new Date());
				}else{
					thisCollector.setUpdatedTs(new Date());
				}

				if(ins != null){
					thisCollector.setInstaller(ins);
				}
				userService.addNewCollector(thisCollector);
			}
		}

		// GET BDC BY SERIAL NUMBER (get dc by its boundry dc and than get its bdc)
		List<DataCollector> dcListToGetBDC = new ArrayList<DataCollector>();
		/*site.setUpdatedBy(Integer.valueOf(loggedUser.getUserId()));
		site.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));	
		site.setSiteName(siteName);
		site.setRegion(region);*/
		if(boundryDC != null){
			dcListToGetBDC = userService.getDCBySerialNumber(boundryDC);
			for(int i=0;i<dcListToGetBDC.size();i++){
				BoundryDataCollector bdc = new BoundryDataCollector();
				bdc.setSite(site);
				bdc.setDatacollector(dcListToGetBDC.get(i));
				/*setBDC.add(bdc);*/
				userService.addNewEditedBDC(bdc);
				/*site.setBoundrydatacollector(setBDC);*/
			}
		}	
		reportService.initalizeDCBySite(site);

		site.getDatacollector().forEach(n -> {
			n.setInstaller(ins);
		});
		site.setSiteName(siteName);
		site.setUpdatedBy(currentCustomer.getCustomerId());
		site.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

		//List<DataCollector> dataCollectorsList = new ArrayList<DataCollector>();
		if(status.equalsIgnoreCase("active")){
			site.setActiveStatus(true);
			site.setDeletedBy(null);
			site.setDeletedTs(null);

		}else if(status.equalsIgnoreCase("inactive")){
			site.setActiveStatus(Boolean.FALSE);
			site.setDeletedBy(currentCustomer.getCustomerId());
			site.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

			boolean isSuccess = userService.inactiveDCandMeterBySiteId(site.getSiteId(),currentCustomer.getCustomerId(),DateTimeConversionUtils.getDateInUTC(new Date()));
		}
		site.setRegion(region);
		userService.mergeSite(site);

		return null; 
	}
	@RequestMapping(value = "/getCustomerDataCollector",method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getDataCollectorForCustomer(HttpServletRequest request, HttpServletResponse response) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.initializeSiteAndDCForCustomer(loggedUser.getUserId());
		request.getSession().setAttribute("customerId", customer.getCustomerId());
		List<JsonObject> datacollectorList = new ArrayList<JsonObject>();
		List<Object> responseList = new ArrayList<Object>();
		if(customer.getDataCollector()!= null){
			Iterator<DataCollector> datacollectorIterator = customer.getDataCollector().iterator();
			while(datacollectorIterator.hasNext()){
				DataCollector currentdatacollector = (DataCollector)datacollectorIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("datacollectorId", currentdatacollector.getDatacollectorId());
				json.addProperty("dcSerialNumber", currentdatacollector.getDcSerialNumber());
				json.addProperty("dcIp", currentdatacollector.getDcIp());
				json.addProperty("totalEndpoints", currentdatacollector.getTotalEndpoints());
				json.addProperty("latitude", currentdatacollector.getLatitude());
				json.addProperty("longitude", currentdatacollector.getLongitude());
				json.addProperty("type", currentdatacollector.getType());
				json.addProperty("iscommissioned", currentdatacollector.getIsCommissioned());
				if(!(currentdatacollector.getSite() == null)){
					json.addProperty("site", currentdatacollector.getSite().getSiteId());
				}else{
					json.addProperty("site", "-");
				}
				datacollectorList.add(json);
			}
			datacollectorList.sort((JsonObject jObject1,JsonObject jObject2) -> jObject1.get("datacollectorId").getAsInt() - jObject2.get("datacollectorId").getAsInt());
			responseList.add(datacollectorList);
			Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
			if(isNormalCustomer != null && isNormalCustomer){
				responseList.add(Boolean.FALSE);
			}else{
				responseList.add(Boolean.TRUE);
			}
			return new ResponseEntity<Object>(new Gson().toJson(responseList), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new Gson().toJson("nodatacollectorfound"), HttpStatus.OK);
	}

	@RequestMapping(value = "/editDataCollectorForCustomer",method = RequestMethod.GET)
	public ResponseEntity<String> editDataCollectorForCustomer(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("datacollectorId") String datacollectorId) {
		if(datacollectorId != null){

			//List<Object> objectList = new ArrayList<Object>();

			DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));
			if(datacollector!= null)
				//	objectList.add(datacollector);
				request.getSession().setAttribute("editdatacollectorList", datacollector);
			return new ResponseEntity<String>(new Gson().toJson("editDatacollectorforcustomer"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}


	@RequestMapping(value="/getEditdatacollectorByCustomer",method=RequestMethod.POST)
	public ResponseEntity<String> getEditdatacollectorByCustomer(HttpServletRequest request,HttpServletResponse response){

		DataCollector datacollector = (DataCollector) request.getSession().getAttribute("editdatacollectorList");
		List<JsonObject> objectList = new ArrayList<JsonObject>();
		JsonObject json = new JsonObject();
		if(datacollector!= null){
			json.addProperty("datacollectorId", datacollector.getDatacollectorId());
			json.addProperty("dcSerialNumber", datacollector.getDcSerialNumber());
			json.addProperty("dcIp", datacollector.getDcIp());
			json.addProperty("totalEndpoints", datacollector.getTotalEndpoints());
			json.addProperty("latitude", datacollector.getLatitude());
			json.addProperty("longitude", datacollector.getLongitude());
			json.addProperty("meterReadingInterval", datacollector.getMeterReadingInterval());
			json.addProperty("networkStatusInterval", datacollector.getNetworkStatusInterval());
			json.addProperty("type", datacollector.getType());
			json.addProperty("iscommissioned", datacollector.getIsCommissioned());
			if(!(datacollector.getSite() == null)){
				json.addProperty("site", datacollector.getSite().getSiteId());
			}else{
				json.addProperty("site", "-");
			}
			if(datacollector.getDcSimcardNo() != null && !datacollector.getDcSimcardNo().trim().isEmpty()){
				json.addProperty("simcardNo", datacollector.getDcSimcardNo());
			}else{
				json.addProperty("simcardNo", "-");
			}

			Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
			if(isNormalCustomer != null && isNormalCustomer){
				json.addProperty("isSuperCustomer", new Gson().toJson(Boolean.FALSE));
			}else{
				json.addProperty("isSuperCustomer", new Gson().toJson(Boolean.TRUE));
			}

			objectList.add(json);
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	
		return new ResponseEntity<>(new Gson().toJson("nodatacollectorFound"),HttpStatus.OK);
	}

	@RequestMapping(value = "/updateDataCollectorByCustomer",method = RequestMethod.GET)
	public ResponseEntity<String> updateDataCollectorByCustomer(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="latitude", required=false) String latitude,
			@RequestParam(value="longitude", required=false) String longitude,
			@RequestParam("datacollectorId") String datacollectorId){
		if(datacollectorId != null){

			DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));

			Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
			User loggedUser = null;
			if(isNormalCustomer != null && isNormalCustomer){
				loggedUser = (User) request.getSession().getAttribute("normalCustomer");
			}else{
				loggedUser = (User) request.getSession().getAttribute("currentUser");
			}

			datacollector.setUpdatedBy(loggedUser.getUserId());
			datacollector.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

			if(latitude == null || latitude.isEmpty())
				datacollector.setLatitude(null);

			if(latitude != null && !latitude.isEmpty())
				datacollector.setLatitude(Double.parseDouble(latitude));

			if(longitude == null || longitude.isEmpty())
				datacollector.setLongitude(null);

			if(longitude != null && !longitude.isEmpty())
				datacollector.setLongitude(Double.parseDouble(longitude));

			userService.updateDataCollector(datacollector);
			return new ResponseEntity<String>(new Gson().toJson("dataCollectorUpdatedSuccessfully"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchDataCollectorByCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchDataCollectorByCustomer(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="dcIp" ,required=false) String dcIp,
			@RequestParam(value="dcSerialNumber",required=false)String dcSerialNumber){

		int customerId=  (int) req.getSession().getAttribute("customerId");
		logger.info("getting customer Id"+customerId);
		logger.info("searching dc Ip"+dcIp);
		logger.info("searching dc serial number" +dcSerialNumber);

		List<DataCollector> searchedList = userService.searchDataCollectorByCustomer(dcIp, dcSerialNumber,customerId);

		List<JsonObject> datacollectorList = new ArrayList<JsonObject>();

		if(searchedList!= null){
			Iterator<DataCollector> datacollectorIterator = searchedList.iterator();

			while(datacollectorIterator.hasNext()){
				DataCollector currentdatacollector = (DataCollector)datacollectorIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("datacollectorId", currentdatacollector.getDatacollectorId());
				json.addProperty("dcSerialNumber", currentdatacollector.getDcSerialNumber());
				json.addProperty("dcIp", currentdatacollector.getDcIp());
				json.addProperty("totalEndpoints", currentdatacollector.getTotalEndpoints());
				json.addProperty("latitude", currentdatacollector.getLatitude());
				json.addProperty("longitude", currentdatacollector.getLongitude());
				json.addProperty("type", currentdatacollector.getType());
				json.addProperty("iscommissioned", currentdatacollector.getIsCommissioned());
				if(!(currentdatacollector.getSite() == null)){
					json.addProperty("site", currentdatacollector.getSite().getSiteId());
				}else{
					json.addProperty("site", "-");
				}

				datacollectorList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(datacollectorList), HttpStatus.OK);
		}


		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
	}


	//for getting DU Meter list
	@RequestMapping(value="/getDUMeterData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getDUMeterList(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<JsonObject> DUMeterJsonList = new ArrayList<JsonObject>();

		List<DistrictMeterTransaction> DUMeterList = userService.getDUMeterListByCustomerId(customer.getCustomerId());

		if(DUMeterList.size() > 0){
			Iterator<DistrictMeterTransaction> DUMeterIterator = DUMeterList.iterator();

			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");

			while(DUMeterIterator.hasNext()){
				DistrictMeterTransaction districtUtilityMeter = DUMeterIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("districtUtilityMeterId", districtUtilityMeter.getDistrictUtilityMeter().getDistrictUtilityMeterId());
				json.addProperty("districtUtilityMeterSerialNumber", districtUtilityMeter.getDistrictUtilityMeter().getDistrictUtilityMeterSerialNumber());


				json.addProperty("districtMeterTransactionId", districtUtilityMeter.getDistrictMeterTransactionId());

				if(districtUtilityMeter.getCurrentReading() != null){
					json.addProperty("currentReading", districtUtilityMeter.getCurrentReading());
				}
				if(districtUtilityMeter.getStartBillingDate() != null){
					json.addProperty("startBillingDate", sdf.format(districtUtilityMeter.getStartBillingDate()));
				}
				if(districtUtilityMeter.getEndBillingDate() != null){
					json.addProperty("endBillingDate", sdf.format(districtUtilityMeter.getEndBillingDate()));
				}

				DUMeterJsonList.add(json);
			}

			return new ResponseEntity<Object>(new Gson().toJson(DUMeterJsonList), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new Gson().toJson("youCanNotAccessRegionList"), HttpStatus.OK);
	}


	//for adding region
	@RequestMapping(value="/addDUMeter",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addDUMeter(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="DUMeterSerialNumber") String DUMeterSerialNumber,
			@RequestParam(value="DUMeterReading") String DUMeterReading,
			@RequestParam(value="startBillingDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date startBillingDate,
			@RequestParam(value="endBillingDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date endBillingDate){

		if(DUMeterSerialNumber.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullserialnumber"),HttpStatus.OK);
		}

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		boolean isDUMeterValid = userService.checkForDUMeter(DUMeterSerialNumber);
		if(!isDUMeterValid){
			DistrictUtilityMeter districtUtilityMeter = userService.getDUMeterBySerialNumber(DUMeterSerialNumber);

			DistrictMeterTransaction districtMeterTransaction = new DistrictMeterTransaction();
			districtMeterTransaction.setCurrentReading(Integer.parseInt(DUMeterReading));
			districtMeterTransaction.setStartBillingDate(startBillingDate);
			districtMeterTransaction.setEndBillingDate(endBillingDate);
			districtMeterTransaction.setDistrictUtilityMeter(districtUtilityMeter);

			Set<DistrictMeterTransaction> data = districtUtilityMeter.getDistrictMeterTransactions();
			data.add(districtMeterTransaction);

			districtUtilityMeter.setDistrictMeterTransactions(data);

			userService.addDUMeter(districtUtilityMeter);

		} else {

			DistrictUtilityMeter districtUtilityMeter = new DistrictUtilityMeter();

			districtUtilityMeter.setDistrictUtilityMeterSerialNumber(DUMeterSerialNumber.trim());
			districtUtilityMeter.setCustomer(customer);

			DistrictMeterTransaction districtMeterTransaction = new DistrictMeterTransaction();
			districtMeterTransaction.setCurrentReading(Integer.parseInt(DUMeterReading));
			districtMeterTransaction.setStartBillingDate(startBillingDate);
			districtMeterTransaction.setEndBillingDate(endBillingDate);
			districtMeterTransaction.setDistrictUtilityMeter(districtUtilityMeter);

			Set<DistrictMeterTransaction> transactionSet = new HashSet<DistrictMeterTransaction>();
			transactionSet.add(districtMeterTransaction);

			districtUtilityMeter.setDistrictMeterTransactions(transactionSet);

			userService.addDUMeter(districtUtilityMeter);


		}


		logger.info("DU Meter successfully added...");
		return new ResponseEntity<String>(new Gson().toJson("dumeterSuccessfullyadded"), HttpStatus.OK);

	}

	@RequestMapping(value = "/editDUMeter",method = RequestMethod.GET)
	public ResponseEntity<String> editDUMeter(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="districtMeterTransactionId", required=false) String districtMeterTransactionId) {

		if(districtMeterTransactionId != null){

			//DistrictUtilityMeter districtUtilityMeter = userService.getDUMeterListById(Integer.parseInt(districtUtilityMeterId));

			DistrictMeterTransaction districtMeterTransaction = userService.getDistrictMeterTransactionById(Integer.parseInt(districtMeterTransactionId));

			request.getSession().setAttribute("editDUMeter", districtMeterTransaction);

			return new ResponseEntity<String>(new Gson().toJson("editDUMeterData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDUMeterFound"), HttpStatus.OK);
	}


	@RequestMapping(value="/getEditDUMeter",method=RequestMethod.GET)
	public ResponseEntity<String> getEditDUMeter(HttpServletRequest request,HttpServletResponse response){

		DistrictMeterTransaction districtMeterTransaction = (DistrictMeterTransaction) request.getSession().getAttribute("editDUMeter");

		List<JsonObject> objectList = new ArrayList<JsonObject>();
		if(districtMeterTransaction != null){

			JsonObject json = new JsonObject();
			json.addProperty("districtMeterTransactionId", districtMeterTransaction.getDistrictMeterTransactionId());
			json.addProperty("districtUtilityMeterSerialNumber", districtMeterTransaction.getDistrictUtilityMeter().getDistrictUtilityMeterSerialNumber());
			json.addProperty("currentReading", districtMeterTransaction.getCurrentReading());
			json.addProperty("startBillingDate", districtMeterTransaction.getStartBillingDate().toString().substring(0,10));
			json.addProperty("endBillingDate", districtMeterTransaction.getEndBillingDate().toString().substring(0,10));

			objectList.add(json);
			request.getSession().removeAttribute("editDUMeter");
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	

		return new ResponseEntity<>(new Gson().toJson("noDUMeterFound"),HttpStatus.OK);
	}

	@RequestMapping(value = "/updateDUMeter",method = RequestMethod.POST)
	public ResponseEntity<String> updateDUMeter(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="districtMeterTransactionId") String districtMeterTransactionId,
			@RequestParam(value="DUMeterSerialNumber") String DUMeterSerialNumber,
			@RequestParam(value="DUMeterReading") String DUMeterReading,
			@RequestParam(required=false,value="startBillingDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date startBillingDate,
			@RequestParam(required=false,value="endBillingDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date endBillingDate){

		if(DUMeterSerialNumber.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullserialnumber"),HttpStatus.OK);
		}

		DistrictMeterTransaction districtMeterTransaction = userService.getDistrictMeterTransactionById(Integer.parseInt(districtMeterTransactionId));
		districtMeterTransaction.setCurrentReading(Integer.parseInt(DUMeterReading));
		districtMeterTransaction.setStartBillingDate(startBillingDate);
		districtMeterTransaction.setEndBillingDate(endBillingDate);

		userService.updateDUMeterTransaction(districtMeterTransaction);

		return new ResponseEntity<>(new Gson().toJson("DUMeterUpdatedSuccessfully"),HttpStatus.OK);

	}

	@RequestMapping(value="/searchDUMeter",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> searchDUMeter(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="DUMeterSerialNumber") String DUMeterSerialNumber){

		User loggedUser = (User) req.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<JsonObject> DUMeterJsonList = new ArrayList<JsonObject>();

		List<DistrictMeterTransaction> DUMeterList = userService.searchDUMeter(DUMeterSerialNumber, customer.getCustomerId());

		if(DUMeterList.size() > 0){
			Iterator<DistrictMeterTransaction> DUMeterIterator = DUMeterList.iterator();

			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");

			while(DUMeterIterator.hasNext()){
				DistrictMeterTransaction districtUtilityMeter = DUMeterIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("districtUtilityMeterId", districtUtilityMeter.getDistrictUtilityMeter().getDistrictUtilityMeterId());
				json.addProperty("districtUtilityMeterSerialNumber", districtUtilityMeter.getDistrictUtilityMeter().getDistrictUtilityMeterSerialNumber());


				json.addProperty("districtMeterTransactionId", districtUtilityMeter.getDistrictMeterTransactionId());

				if(districtUtilityMeter.getCurrentReading() != null){
					json.addProperty("currentReading", districtUtilityMeter.getCurrentReading());
				}
				if(districtUtilityMeter.getStartBillingDate() != null){
					json.addProperty("startBillingDate", sdf.format(districtUtilityMeter.getStartBillingDate()));
				}
				if(districtUtilityMeter.getEndBillingDate() != null){
					json.addProperty("endBillingDate", sdf.format(districtUtilityMeter.getEndBillingDate()));
				}

				DUMeterJsonList.add(json);
			}
			return new ResponseEntity<Object>(new Gson().toJson(DUMeterJsonList), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new Gson().toJson("emptryList"), HttpStatus.OK);
	}

	@RequestMapping(value="/deleteDUMeter",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteDUMeter(HttpServletRequest request,HttpServletResponse response,@RequestParam("districtUtilityMeterId") String districtUtilityMeterId){	
		if(districtUtilityMeterId.matches("^-?\\d+$") && (districtUtilityMeterId != null || !districtUtilityMeterId.trim().isEmpty())){

			try{
				boolean isDUMeterDeleted = userService.deleteDUMeter(Integer.parseInt(districtUtilityMeterId));
				if(isDUMeterDeleted){
					logger.info("Meter successfully deleted with id "+districtUtilityMeterId);
					return new ResponseEntity<String>(new Gson().toJson("Meter successfully deleted"), HttpStatus.OK);
				}else{
					logger.info("No such Meter found with Meter id"+districtUtilityMeterId);
					return new ResponseEntity<String>(new Gson().toJson("No such Meter found"), HttpStatus.OK);
				}
			}catch(Exception e){
				logger.info("Error while deleting District Utility Meter with districtUtilityMeterId"+districtUtilityMeterId);
				return new ResponseEntity<String>(new Gson().toJson("meterismapped"), HttpStatus.OK);
			}

		}

		logger.warn("error while parsing query parameter with MeterId "+districtUtilityMeterId);
		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
	}


	//Redirecting to Assign Installer
	@RequestMapping(value="/assigninstallerToConsumer",method=RequestMethod.GET)
	public ModelAndView assigninstaller(HttpServletRequest request, HttpServletResponse response){
		logger.info("Redirecting to Assign Installer");
		ModelAndView model = new ModelAndView("assignInstaller");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		model.addObject("customerId",customer.getCustomerId());
		return model;
	}

	// Search consumer by streetname and of customer
	@SuppressWarnings("null")
	@RequestMapping(value="/getConsumerofCustomer",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> getConsumerofCustomer(HttpServletRequest request,HttpServletResponse response,@RequestParam String customerId,@RequestParam String streetName) {

		logger.info("Inside getConsumerofCustomer method to search consumer");
		int customerIdInt =Integer.parseInt(customerId);
		List<ConsumerMeter> consumerList = new ArrayList<ConsumerMeter>();
		consumerList = userService.searchConsumerMeterByRegisterId(customerIdInt, Integer.parseInt(streetName));
		List<JsonObject> finalConsumerList = new ArrayList<JsonObject>();

		if (consumerList != null || !consumerList.isEmpty()) {
			Iterator<ConsumerMeter> conIterator = consumerList.iterator();
			while (conIterator.hasNext()) {
				JsonObject json = new JsonObject();
				ConsumerMeter cm= (ConsumerMeter)conIterator.next();
				json.addProperty("meter_id", cm.getConsumerMeterId());
				json.addProperty("acc_no", cm.getRegisterId());
				finalConsumerList.add(json);
			}
		}

		return new ResponseEntity<Object>(new Gson().toJson(finalConsumerList), HttpStatus.OK);
	}

	// Get all installer of customer
	@SuppressWarnings("null")
	@RequestMapping(value="/getCustomerInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> getCustomerInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam String customerId) {

		logger.info("Inside getCustomerInstaller method");
		List<Installer> list = userService.getInstallerByCustomerId(Integer.parseInt(customerId));

		List<JsonObject> finalList = new ArrayList<JsonObject>();

		if (list != null || !list.isEmpty()) {
			Iterator<Installer> insIterator = list.iterator();
			while (insIterator.hasNext()) {
				JsonObject json = new JsonObject();
				Installer ins= (Installer)insIterator.next();
				json.addProperty("name", ins.getUser().getDetails().getFirstName()+" "+ins.getUser().getDetails().getLastname());
				json.addProperty("id", ins.getInstallerId());
				finalList.add(json);
			}
		}

		return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
	}

	//Assign All consumer to installer
	@RequestMapping(value="/assignConsumertoInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> assignConsumertoInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam String customerId,
			@RequestParam String installerId,
			@RequestParam String streetName){

		logger.info("Inside assignConsumertoInstaller method");
		List<ConsumerMeter> consumerList = userService.getConsumerListByCustomerId(Integer.parseInt(customerId));
		boolean installerAssign = userService.assignInstallerToConumers(consumerList, Integer.parseInt(installerId));

		return new ResponseEntity<Object>(new Gson().toJson(installerAssign), HttpStatus.OK);
	}

	// Get all consumer which was assigned to installer
	@SuppressWarnings("null")
	@RequestMapping(value="/getInstallersConsumers",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getInstallersConsumers(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="installerId",required=true) String installerId){

		logger.info("Inside assignConsumertoInstaller method");
		List<ConsumerMeter> consumerList = userService.getConsumersByInstaller(Integer.parseInt(installerId));

		List<JsonObject> finalList = new ArrayList<JsonObject>();

		if (consumerList != null || !consumerList.isEmpty()) {
			Iterator<ConsumerMeter> iterator = consumerList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				ConsumerMeter co= (ConsumerMeter)iterator.next();
				json.addProperty("meter_id", co.getConsumerMeterId());
				json.addProperty("acc_no", co.getRegisterId());
				finalList.add(json);
			}
		}


		return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
	}
	// Assign selected consumer to installer
	@RequestMapping(value="/assignSelectedConsumertoInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> assignSelectedConsumertoInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam String consumerIds,
			@RequestParam String installerId,
			@RequestParam String customerId,
			@RequestParam String streetName){

		logger.info("Inside assignSelectedConsumertoInstaller method");
		String s[] = consumerIds.split("/");

		List<ConsumerMeter> consumerList = new ArrayList<ConsumerMeter>();


		for (int i = 0; i < s.length; i++) {
			consumerList.add(consumerMeterService.getConsumerMeterById(Integer.parseInt(s[i])));
		}
		boolean installerAssign = userService.assignInstallerToConumers(consumerList, Integer.parseInt(installerId));


		return new ResponseEntity<Object>(new Gson().toJson(installerAssign), HttpStatus.OK);
	}

	@RequestMapping(value = "assignInstallerToDC",method = RequestMethod.GET)
	public ModelAndView assignInstallerToDC(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Redirecting to Assign Installer to Datacollector");
		ModelAndView model = new ModelAndView("assignInstallerToDC");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		model.addObject("customerId",customer.getCustomerId());
		return model;
	}


	// Get all DC  which was assigned to installer
	@SuppressWarnings("null")
	@RequestMapping(value="/getInstallersDC",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> getInstallersDC(HttpServletRequest request,HttpServletResponse response,@RequestParam String installerId){

		logger.info("Inside getInstallersDC  method");
		List<DataCollector> DCList = userService.getDCByInstallerId(Integer.parseInt(installerId));

		List<JsonObject> finalList = new ArrayList<JsonObject>();

		if (DCList != null || !DCList.isEmpty()) {
			Iterator<DataCollector> iterator = DCList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				DataCollector dc= (DataCollector)iterator.next();
				json.addProperty("datacollectorId", dc.getDatacollectorId());
				json.addProperty("dcIp", dc.getDcIp());

				finalList.add(json);
			}
		}

		return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
	}


	@RequestMapping(value="/searchDataCollectorByCustomerAndIp",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchDataCollectorByCustomerAndIp(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="dcIp" ,required=false) String dcIp,
			@RequestParam(value="customerId") String customerId){

		logger.info("searching dc Ip"+dcIp);
		logger.info("searching Customer id" +customerId);

		List<DataCollector> searchedList = userService.searchDataCollectorByCustomerAndIp(dcIp, customerId);

		List<JsonObject> datacollectorList = new ArrayList<JsonObject>();

		if(searchedList!= null){
			Iterator<DataCollector> datacollectorIterator = searchedList.iterator();

			while(datacollectorIterator.hasNext()){
				DataCollector currentdatacollector = (DataCollector)datacollectorIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("datacollectorId", currentdatacollector.getDatacollectorId());
				json.addProperty("dcIp", currentdatacollector.getDcIp());

				datacollectorList.add(json);
			}

		}else{
			return new ResponseEntity<String>(new Gson().toJson("nosuchDCfound"), HttpStatus.OK);
		}


		return new ResponseEntity<String>(new Gson().toJson(datacollectorList), HttpStatus.OK);
	}



	@RequestMapping(value="/assignSelectedDCToInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> assignSelectedDCToInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam String customerId,
			@RequestParam String selectedDcId,
			@RequestParam String installerId
			){

		logger.info("Inside assignSelectedDCtoInstaller method");


		boolean installerAssign = userService.assignSelectedDCToInstaller(Integer.parseInt(selectedDcId),Integer.parseInt(installerId));


		return new ResponseEntity<Object>(new Gson().toJson(installerAssign), HttpStatus.OK);
	}

	@RequestMapping(value="/customerSiteInitialDataForScheduler",method=RequestMethod.POST)
	public ResponseEntity<Object> customerSiteInitialDataForScheduler(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

		List<JsonObject> siteList = new ArrayList<JsonObject>();
		List<Object> responseList = new ArrayList<Object>();
		// Getting All Region
		if(customer.getRegion() != null){

			Iterator<Region> regionIterator = customer.getRegion().iterator();

			while(regionIterator.hasNext()){
				Region currentRegion = (Region) regionIterator.next();
				Iterator<Site> iteratorSite = currentRegion.getSite().iterator();
				while(iteratorSite.hasNext()){
					Site currentSite = (Site) iteratorSite.next();
					if(currentSite.getDatacollector() != null && currentSite.getDatacollector().stream().findFirst().isPresent()){
						JsonObject json = new JsonObject();
						json.addProperty("siteId", currentSite.getSiteId());
						json.addProperty("regionName", currentSite.getRegion().getRegionName().toString());
						json.addProperty("NRI", currentSite.getDatacollector().stream().filter(filt -> filt.getNetworkReadingInterval()!=null).findAny().get().getNetworkReadingInterval());
						//json.addProperty("MRI", currentSite.getDatacollector().stream().filter(filt -> filt.getMeterReadingInterval()!=null).findAny().get().getMeterReadingInterval());
						json.addProperty("MRI",currentSite.getDummyMriToDisplay()!=null?(currentSite.getDummyMriToDisplay().length()>8?currentSite.getDummyMriToDisplay().substring(8)+"th":currentSite.getDummyMriToDisplay()):"-");
						siteList.add(json);
					}
				}
			}
		}	
		siteList.sort((JsonObject jObj1,JsonObject jObj2) -> jObj1.get("regionName").getAsString().compareTo(jObj2.get("regionName").getAsString()));
		responseList.add(siteList);
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		JsonObject jObject = new JsonObject();
		if(isNormalCustomer != null && isNormalCustomer){
			responseList.add(Boolean.FALSE);
		}else{
			responseList.add(Boolean.TRUE);
		}
		return new ResponseEntity<Object>(new Gson().toJson(responseList), HttpStatus.OK);
	}

	@RequestMapping(value="/updateMeterReadingInterval",method=RequestMethod.POST)
	public ResponseEntity<String> updateMeterReadingInterval(HttpServletRequest request,HttpServletResponse response,@RequestParam String sitId,
			@RequestParam(required=false) Integer mriInMins,@RequestParam(required=false)@DateTimeFormat(pattern="yyyy-MM-dd") Date startDate){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());
		String strDate = null;
		if(startDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			strDate = sdf.format(startDate);
		}
		boolean flag = userService.updateDCsBySiteId(sitId,mriInMins,strDate,customer,null,null);
		if(flag)
			return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
		else
			return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
	}

	// Search by siteId or region name 
	@RequestMapping(value="/searchBysiteIdOrRegionName",method=RequestMethod.POST)
	public ResponseEntity<Object> searchBysiteIdOrRegionName(HttpServletRequest req,HttpServletResponse res,@RequestParam(value="sitId",required=false) String sitId, @RequestParam(value="regionName",required=false) String regionName){
		int siteid = 0;
		String regionname =null;
		if(sitId!=null && !sitId.equals("") &&sitId.length()>0){
			siteid = Integer.parseInt(sitId.trim());
		}
		if(regionName!= null){
			regionname = regionName.trim();
		}
		List<Site> searchedList = userService.searchBysiteIdOrRegionName(siteid, regionname);

		if(searchedList!=null && searchedList.size() > 0){

			List<JsonObject> siteList = new ArrayList<JsonObject>();

			Iterator<Site> siteIterator = searchedList.iterator();

			while(siteIterator.hasNext()){

				Site currentSite = siteIterator.next();

				if(currentSite.getDatacollector().stream().findFirst().isPresent()){
					JsonObject json = new JsonObject();
					json.addProperty("siteId", currentSite.getSiteId());
					json.addProperty("regionName", currentSite.getRegion().getRegionName().toString());
					json.addProperty("NRI", currentSite.getDatacollector().stream().filter(filt -> filt.getNetworkReadingInterval()!=null).findAny().get().getNetworkReadingInterval());
					json.addProperty("MRI", currentSite.getDatacollector().stream().filter(filt -> filt.getMeterReadingInterval()!=null).findAny().get().getMeterReadingInterval());
					siteList.add(json);
				}
			}
			return new ResponseEntity<Object>(new Gson().toJson(siteList), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOSITEFOUND),HttpStatus.OK);
	}	


	@SuppressWarnings("null")
	@RequestMapping(value="/getInstallerByCustomerId",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> getInstallerByCustomerId(HttpServletRequest request,HttpServletResponse response,@RequestParam String customerId){

		logger.info("Inside getInstallerByCustomerId method");

		List<JsonObject> finalList = new ArrayList<JsonObject>();
		List<Installer> installerList = new ArrayList<Installer>();	
		installerList = userService.getInstallerByCustomerId(Integer.parseInt(customerId));
		if (installerList != null || !installerList.isEmpty()) {
			Iterator<Installer> iterator = installerList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				Installer ins= (Installer)iterator.next();
				json.addProperty("id",ins.getInstallerId());
				json.addProperty("name",ins.getUser().getDetails().getFirstName()+" "+ins.getUser().getDetails().getLastname());
				json.addProperty("status", ins.isActive());
				finalList.add(json);
			}
			return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>(new Gson().toJson("noSuchInstallerFound"), HttpStatus.OK);
		}



	}


	// Redirect to Add New Installer
	@RequestMapping(value="/addInstallerRedirect",method=RequestMethod.GET)
	public ModelAndView addInstallerRedirect(HttpServletRequest request, HttpServletResponse response){
		logger.info("Redirecting to Add new Installer Management");
		ModelAndView model = new ModelAndView("addInstaller");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		model.addObject("customerId",customer.getCustomerId());
		return model;
	}


	@RequestMapping(value="/addInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> addInstaller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname,
			@RequestParam(value="username", required=true) String username,
			@RequestParam(value="address1", required=true) String address1,
			@RequestParam(value="address2", required=false) String address2,
			@RequestParam(value="address3", required=false) String address3,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="email1", required=true) String email1,
			@RequestParam(value="email2", required=false) String email2,
			@RequestParam(value="email3", required=false) String email3,
			@RequestParam(value="zipcode", required=true) String zipcode,
			@RequestParam(value="streetname", required=true) String streetname,
			@RequestParam(value="customerId", required=true) String customerId){



		logger.info("Inside insert installer method");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		ContactDetails details = new ContactDetails();
		details.setFirstName(firstname);
		details.setLastname(lastname);
		details.setZipcode(zipcode);
		details.setCreated_by(customer.getCustomerId());
		details.setAddress1(address1);
		details.setAddress2(address2);
		details.setAddress3(address3);
		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);
		details.setCell_number1(phone);
		details.setStreetName(streetname);

		Role role = new Role();
		role.setRoleName("Installer");
		role.setRoleId(4);

		User newUser = new User();

		String generatedPassword = RandomNumberGenerator.generatePswd().toString();

		newUser.setUserName(username);
		newUser.setPassword(MD5Encoder.MD5Encryptor(generatedPassword));
		newUser.setRole(role);
		newUser.setActiveStatus(Boolean.TRUE);
		newUser.setFirstTimeLogin(Boolean.TRUE);
		newUser.setCreatedBy(loggedUser.getUserId());
		newUser.setCreatedTs(new Date());

		newUser.setDetails(details);

		Installer newInstaller = new Installer();
		newInstaller.setActive(Boolean.TRUE);
		newInstaller.setUser(newUser);
		newInstaller.setCustomer(customer);
		newInstaller.setCreatedBy(loggedUser.getUserId());
		newInstaller.setInstallerName(firstname+" "+lastname);
		newInstaller.setCreatedTS(new Date());

		userService.insertInstaller(newInstaller);

		return new ResponseEntity<Object>(new Gson().toJson("installerAddedSuccessfully"), HttpStatus.OK);
	}


	@RequestMapping(value="/editInstaller",method=RequestMethod.GET)
	public ModelAndView editInstaller(HttpServletRequest request, HttpServletResponse response,@RequestParam("installerId") String installerId){
		logger.info("Redirecting to Edit Installer");
		ModelAndView model = new ModelAndView("editInstaller");

		Installer installer= userService.getInstallerById(Integer.parseInt(installerId));
		request.getSession().setAttribute("installer", installer);
		model.addObject("installertobeEdit",installer);
		return model;
	}


	@RequestMapping(value="/updateInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> updateInstaller(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstname", required=true) String firstName,
			@RequestParam(value="lastname", required=true) String lastName,
			@RequestParam(value="address1", required=true) String address1,
			@RequestParam(value="address2", required=false) String address2,
			@RequestParam(value="address3", required=false) String address3,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="email1", required=true) String email1,
			@RequestParam(value="email2", required=false) String email2,
			@RequestParam(value="email3", required=false) String email3,
			@RequestParam(value="zipcode", required=true) String zipcode,
			@RequestParam(value="streetname", required=true) String streetName,
			@RequestParam(value="installerUserId", required=true) String installerUserId,
			@RequestParam(value="status", required=true) String status){		


		logger.info("Inside Update Installer "+installerUserId);

		ContactDetails details =	userService.getDetailsByUserID(Integer.parseInt(installerUserId));
		Installer installer = (Installer) request.getSession().getAttribute("installer");

		details.setFirstName(firstName);
		details.setLastname(lastName);
		details.setAddress1(address1);
		details.setAddress2(address2);
		details.setAddress3(address3);
		details.setCell_number1(phone);
		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);
		details.setStreetName(streetName);
		details.setZipcode(zipcode);

		User loggedUser = null;
		if((loggedUser = KenureUtilityContext.isNormalCustomer(request)) != null){
			details.setUpdatedBy(loggedUser.getUserId());
			details.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}

		User user = installer.getUser();
		user.setDetails(details);

		if(status.equalsIgnoreCase("Active")){
			installer.setActive(true);
			installer.setDeletedBy(null);
			installer.setDeletedTS(null);
			installer.setUpdatedBy(loggedUser.getUserId());
			installer.setUpdatedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
			user.setActiveStatus(true);
			user.setUpdatedBy(loggedUser.getUserId());
			user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			user.setDeletedBy(null);
			user.setDeletedTs(null);
			installer.setUser(user);
		}
		if(status.equalsIgnoreCase("Inactive")){
			installer.setActive(false);
			installer.setDeletedBy(loggedUser.getUserId());
			installer.setDeletedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
			installer.setUpdatedBy(loggedUser.getUserId());
			installer.setUpdatedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
			user.setActiveStatus(false);
			user.setUpdatedBy(loggedUser.getUserId());
			user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			user.setDeletedBy(loggedUser.getUserId());
			user.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			installer.setUser(user);
		}
		try{
			userService.updateInstaller(installer);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return new ResponseEntity<Object>(new Gson().toJson("installerUpdatedSuccessfully"), HttpStatus.OK);
	}

	@RequestMapping(value="/deleteInstaller",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam("userId") String installerId){	
		if(installerId.matches("^-?\\d+$") && (installerId != null || !installerId.trim().isEmpty())){
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			int customerId = customer.getCustomerId();
			try{
				//userService.getInstallerById(installerId)
				boolean isUserDeleted = userService.deleteUser(Integer.parseInt(installerId),4,customerId);
				if(isUserDeleted){
					logger.info("Installer successfully deleted with id "+installerId);
					return new ResponseEntity<String>(new Gson().toJson("installerdeletedsuccessfully"), HttpStatus.OK);
				}else{
					logger.info("no such user found with user id"+installerId);
					return new ResponseEntity<String>(new Gson().toJson("No such user found"), HttpStatus.OK);
				}
			}catch(Exception e){
				logger.info("Error while deleting Customer with installerId"+installerId);
				return new ResponseEntity<String>(new Gson().toJson("userismapped"), HttpStatus.OK);
			}

		}
		logger.warn("error while parsing query parameter with userId "+installerId);
		return new ResponseEntity<String>(new Gson().toJson("userismapped"), HttpStatus.OK);
	}



	@RequestMapping(value="/searchInstaller",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> searchInstaller(HttpServletRequest request,HttpServletResponse response,@RequestParam String criteria){

		logger.info("Searching Installer : "+criteria);
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		List<Installer> installerList = userService.searchInstallerByCriteria(criteria,customer.getCustomerId());
		List<JsonObject> finalList = new ArrayList<JsonObject>();

		if (installerList != null) {
			logger.info(" Installer Found");
			Iterator<Installer> iterator = installerList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				Installer ins= (Installer)iterator.next();
				json.addProperty("id",ins.getInstallerId());
				json.addProperty("status",ins.isActive());
				json.addProperty("name",ins.getUser().getDetails().getFirstName()+" "+ins.getUser().getDetails().getLastname());
				finalList.add(json);
			}

			return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
		}

		logger.info("Installer Not Found"+installerList);
		return new ResponseEntity<Object>(new Gson().toJson("noSuchInstallerFound"), HttpStatus.OK);

	}

	// Technician Management CRUD

	@SuppressWarnings("null")
	@RequestMapping(value="/getTechnicianByCustomerId",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> getTechnicianByCustomerId(HttpServletRequest request,HttpServletResponse response){

		logger.info("Inside getTechnicianByCustomerId method");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		List<JsonObject> finalList = new ArrayList<JsonObject>();
		List<MaintenanceTechnician> technicianList = new ArrayList<MaintenanceTechnician>();	
		technicianList = userService.getTechnicianListByCustomerId(customer.getCustomerId());
		if (technicianList != null || !technicianList.isEmpty()) {
			Iterator<MaintenanceTechnician> iterator = technicianList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				MaintenanceTechnician ins= iterator.next();
				json.addProperty("id",ins.getMaintenanceTechnicianId());
				json.addProperty("name",ins.getContactdetails().getFirstName()+" "+ins.getContactdetails().getLastname());
				json.addProperty("status",ins.isActiveStatus());
				finalList.add(json);
			}
			return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>(new Gson().toJson("noSuchTechnicianFound"), HttpStatus.OK);
		}


	}


	// Redirect to Add New Technician
	@RequestMapping(value="/addTechnicianRedirect",method=RequestMethod.GET)
	public ModelAndView addTechnicianRedirect(HttpServletRequest request, HttpServletResponse response){
		logger.info("Redirecting to Add new Technician ");
		ModelAndView model = new ModelAndView("addTechnician");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		model.addObject("customerId",customer.getCustomerId());
		return model;
	}	


	@RequestMapping(value="/editTechnicianAction",method=RequestMethod.GET)
	public ModelAndView editTechnicianAction(HttpServletRequest request, HttpServletResponse response,@RequestParam("editTechId") String editTechId){
		logger.info("Redirecting to Edit Technician");
		ModelAndView model = new ModelAndView("editTechnician");

		MaintenanceTechnician technician= userService.getTechnicianById(Integer.parseInt(editTechId));
		request.getSession().setAttribute("technician", technician);
		model.addObject("techniciantobeEdit",technician);
		return model;
	}



	@RequestMapping(value="/addTechnician",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> addTechnician(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname,
			@RequestParam(value="address1", required=true) String address1,
			@RequestParam(value="address2", required=false) String address2,
			@RequestParam(value="address3", required=false) String address3,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="email1", required=true) String email1,
			@RequestParam(value="email2", required=false) String email2,
			@RequestParam(value="email3", required=false) String email3,
			@RequestParam(value="zipcode", required=true) String zipcode,
			@RequestParam(value="streetname", required=true) String streetname,
			@RequestParam(value="customerId", required=true) String customerId){



		logger.info("Inside insert installer method");
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		ContactDetails details = new ContactDetails();
		details.setFirstName(firstname);
		details.setLastname(lastname);
		details.setZipcode(zipcode);
		details.setCreated_by(customer.getCustomerId());
		details.setAddress1(address1);
		details.setAddress2(address2);
		details.setAddress3(address3);
		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);
		details.setCell_number1(phone);
		details.setStreetName(streetname);
		details.setCreated_by(loggedUser.getUserId());

		MaintenanceTechnician technician = new MaintenanceTechnician();
		technician.setActiveStatus(Boolean.TRUE);
		technician.setContactdetails(details);
		technician.setCreatedBy(loggedUser.getUserId());
		technician.setCustomer(customer);
		technician.setCreatedBy(loggedUser.getUserId());

		userService.insertTechnician(technician);

		return new ResponseEntity<Object>(new Gson().toJson("technicianAddedSuccessfully"), HttpStatus.OK);
	}

	@RequestMapping(value="/updateTechnician",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> updateTechnician(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstname", required=true) String firstName,
			@RequestParam(value="lastname", required=true) String lastName,
			@RequestParam(value="address1", required=true) String address1,
			@RequestParam(value="address2", required=false) String address2,
			@RequestParam(value="address3", required=false) String address3,
			@RequestParam(value="phone", required=true) String phone,
			@RequestParam(value="email1", required=true) String email1,
			@RequestParam(value="email2", required=false) String email2,
			@RequestParam(value="email3", required=false) String email3,
			@RequestParam(value="zipcode", required=true) String zipcode,
			@RequestParam(value="streetname", required=true) String streetName,
			@RequestParam(value="detailId", required=true) String contactDetailsId,
			@RequestParam(value="status", required=true) String status){

		logger.info("Inside Update Technician "+contactDetailsId);

		ContactDetails details = userService.getContactDetailsByID(Integer.parseInt(contactDetailsId));
		MaintenanceTechnician technician = (MaintenanceTechnician) request.getSession().getAttribute("technician");

		details.setFirstName(firstName);
		details.setLastname(lastName);
		details.setAddress1(address1);
		details.setAddress2(address2);
		details.setAddress3(address3);
		details.setCell_number1(phone);
		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);
		details.setStreetName(streetName);
		details.setZipcode(zipcode);

		User loggedUser = KenureUtilityContext.isNormalCustomer(request);
		if(loggedUser != null){
			details.setUpdatedBy(loggedUser.getUserId());
			details.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}

		technician.setContactdetails(details);

		if(status.equals("Active")){
			technician.setActiveStatus(true);
			technician.setDeletedBy(null);
			technician.setDeletedTs(null);
			technician.setUpdatedBy(loggedUser.getUserId());
			technician.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}
		if(status.equals("Inactive")){
			technician.setActiveStatus(false);
			technician.setDeletedBy(loggedUser.getUserId());
			technician.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			technician.setUpdatedBy(loggedUser.getUserId());
			technician.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}

		try{
			userService.updateTechnician(technician);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return new ResponseEntity<Object>(new Gson().toJson("technicianUpdatedSuccessfully"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchTechnician",method=RequestMethod.POST,headers = "Accept=*/*", produces="application/json")
	public @ResponseBody ResponseEntity<Object> searchTechnician(HttpServletRequest request,HttpServletResponse response,@RequestParam String criteria){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		logger.info("Searching Technician : "+criteria);
		List<MaintenanceTechnician> technicianList = userService.searchTechnicianByCriteria(criteria,customer.getCustomerId());

		List<JsonObject> finalList = new ArrayList<JsonObject>();

		if (technicianList != null) {
			logger.info(" Technician Found");
			Iterator<MaintenanceTechnician> iterator = technicianList.iterator();
			while (iterator.hasNext()) {
				JsonObject json = new JsonObject();
				MaintenanceTechnician ins= (MaintenanceTechnician)iterator.next();
				json.addProperty("id",ins.getMaintenanceTechnicianId());
				json.addProperty("status",ins.isActiveStatus());
				json.addProperty("name",ins.getContactdetails().getFirstName()+" "+ins.getContactdetails().getLastname());
				finalList.add(json);
			}

			return new ResponseEntity<Object>(new Gson().toJson(finalList), HttpStatus.OK);
		}

		logger.info(" technician Not Found"+technicianList);
		return new ResponseEntity<Object>(new Gson().toJson("noSuchTechnicianFound"), HttpStatus.OK);

	}

	@RequestMapping(value="/setDefaultMeterReadingInterval",method=RequestMethod.POST)
	public ResponseEntity<String> setDefaultMeterReadingInterval(HttpServletRequest request,HttpServletResponse response,@RequestParam List<String> siteList,
			@RequestParam(required=false) Integer mriInMins,@RequestParam(required=false)@DateTimeFormat(pattern="yyyy-MM-dd") Date startDate){
		if(!siteList.isEmpty() && siteList.size()>0){
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());
			String strDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(startDate!=null)
				strDate = sdf.format(startDate);
			boolean flag = userService.updateDCsBySiteId(null, mriInMins, strDate,customer,siteList,null);
			if(flag)
				return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
			else
				return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
	}

	@RequestMapping(value="/deleteTechnician",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteTechnician(HttpServletRequest request,HttpServletResponse response,@RequestParam("userId") String technicianId){	
		if(technicianId.matches("^-?\\d+$") && (technicianId != null || !technicianId.trim().isEmpty())){
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			int customerId = customer.getCustomerId();
			try{
				boolean isUserDeleted = userService.deleteUser(Integer.parseInt(technicianId),5,customerId);
				if(isUserDeleted){
					logger.info("user successfully deleted with id "+technicianId);
					return new ResponseEntity<String>(new Gson().toJson("Technician successfully deleted"), HttpStatus.OK);
				}else{
					logger.info("no such user found with user id"+technicianId);
					return new ResponseEntity<String>(new Gson().toJson("No such user found"), HttpStatus.OK);
				}
			}catch(Exception e){
				logger.info("Error while deleting Customer with TechnicianId"+technicianId);
				return new ResponseEntity<String>(new Gson().toJson("userismapped"), HttpStatus.OK);
			}

		}
		logger.warn("error while parsing query parameter with userId "+technicianId);
		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
	}

	@RequestMapping(value="/updateDcSerialNumber",method=RequestMethod.POST)
	public ResponseEntity<String> updateDcSerialNumber(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String datacollectorId,
			@RequestParam(value="serialNumber") String dcSerialNumber){

		// First lets check if this serial number already exist or not
		String[] strDcSerialNimber = {dcSerialNumber};
		List<DataCollector> dcExist = userService.getDCBySerialNumber(strDcSerialNimber);

		if(dcExist == null || dcExist.size() > 0 ){
			return new ResponseEntity<String>(new Gson().toJson("exist"),HttpStatus.OK); 
		}

		DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));

		if(datacollector != null && !datacollector.getDcSerialNumber().equals(dcSerialNumber)){
			List<JsonObject> objectList = new ArrayList<JsonObject>();
			String simCardNo = new String();
			simCardNo = datacollector.getDcSimcardNo();

			datacollector.setDcSimcardNo(null);
			userService.updateDataCollector(datacollector); // DC changed so we first save old record and make sim number null and than new record with sim number
			DataCollector newDc = null;
			try{
				newDc = (DataCollector) datacollector.clone();
			}catch(Exception e){
				logger.error(e.getMessage());
			}

			if(newDc != null){
				newDc.setDcSerialNumber(dcSerialNumber);
				newDc.setDcSimcardNo(simCardNo);
				userService.insertDataCollector(newDc);
			}

			JsonObject json = new JsonObject();
			json.addProperty("datacollectorId", newDc.getDatacollectorId());
			json.addProperty("dcSerialNumber", newDc.getDcSerialNumber());
			json.addProperty("dcIp", newDc.getDcIp());
			json.addProperty("totalEndpoints", newDc.getTotalEndpoints());
			json.addProperty("latitude", newDc.getLatitude());
			json.addProperty("longitude", newDc.getLongitude());
			json.addProperty("meterReadingInterval", newDc.getMeterReadingInterval());
			json.addProperty("networkStatusInterval", newDc.getNetworkStatusInterval());
			json.addProperty("type", newDc.getType());
			json.addProperty("iscommissioned", newDc.getIsCommissioned());
			if(!(newDc.getSite() == null) ){
				json.addProperty("site", newDc.getSite().getSiteId());
			}else{
				json.addProperty("site", "-");
			}
			if(newDc.getDcSimcardNo() != null && !newDc.getDcSimcardNo().trim().isEmpty()){
				json.addProperty("simcardNo", newDc.getDcSimcardNo());
			}else{
				json.addProperty("simcardNo", "-");
			}
			objectList.add(json);
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);

		}
		return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);

	}

	@RequestMapping(value="/updateDcSimNumber")
	public @ResponseBody ResponseEntity<String> updateDcSimNumber(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String datacollectorId,
			@RequestParam(value="simNo") String dcSimNo){

		DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));
		datacollector.setDcSimcardNo(dcSimNo);
		userService.updateDataCollector(datacollector);

		DataCollector updatedDc = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));

		if(updatedDc != null){
			List<JsonObject> objectList = new ArrayList<JsonObject>();
			JsonObject json = new JsonObject();
			json.addProperty("datacollectorId", updatedDc.getDatacollectorId());
			json.addProperty("dcSerialNumber", updatedDc.getDcSerialNumber());
			json.addProperty("dcIp", updatedDc.getDcIp());
			json.addProperty("totalEndpoints", updatedDc.getTotalEndpoints());
			json.addProperty("latitude", updatedDc.getLatitude());
			json.addProperty("longitude", updatedDc.getLongitude());
			json.addProperty("meterReadingInterval", updatedDc.getMeterReadingInterval());
			json.addProperty("networkStatusInterval", updatedDc.getNetworkStatusInterval());
			json.addProperty("type", updatedDc.getType());
			json.addProperty("iscommissioned", updatedDc.getIsCommissioned());
			if(!(updatedDc.getSite() == null)){
				json.addProperty("site", updatedDc.getSite().getSiteId());
			}else{
				json.addProperty("site", "-");
			}
			if(updatedDc.getDcSimcardNo() != null && !updatedDc.getDcSimcardNo().trim().isEmpty()){
				json.addProperty("simcardNo", updatedDc.getDcSimcardNo());
			}else{
				json.addProperty("simcardNo", "-");
			}
			objectList.add(json);
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);

	}

	@RequestMapping(value="/getCustomerNotifications")
	public @ResponseBody ResponseEntity<String> getCustomerNotifications(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="customerId") String customerId){

		List<Object> objectList = new ArrayList<Object>();

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		Date startDate = DateTimeConversionUtils.getPreviousDateBasedOnCurrentDateFromMidNight();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
		Date endDate = new Date();

		int abnormalUsers = 0;
		VPNConfiguration vpnConfiguration = vpnService.getVPNDataById("1");

		List<Object> resultList = userService.getCustomerDashboardData(customerId,sdf.format(startDate),sdf.format(endDate));

		List<AbnormalConsumptionModel> abnormalConsumptionModels = reportService.generateInitDataForAbnormalConsumption(customer);

		if(abnormalConsumptionModels != null && abnormalConsumptionModels.size() > 0){

			Iterator<AbnormalConsumptionModel> abnormalConsumptionModelIterator = abnormalConsumptionModels.iterator();
			while(abnormalConsumptionModelIterator.hasNext()){

				AbnormalConsumptionModel abnormalConsumptionModel = abnormalConsumptionModelIterator.next();

				if(abnormalConsumptionModel.getLast24hrUsage() == 0){
					if(abnormalConsumptionModel.getLast24hrUsage() > (abnormalConsumptionModel.getAverageConsumption() + (vpnConfiguration.getAbnormalThreshold() * abnormalConsumptionModel.getAverageConsumption())/100)){
						abnormalUsers++;
					}
				}
			}
		}

		Integer totalAlert = (Integer) ((Object[]) resultList.get(0))[1];
		Integer perCapita = (Integer) ((Object[]) resultList.get(1))[1];
		Integer totalEP = (Integer) ((Object[]) resultList.get(2))[1];
		Integer totalDataUsage = (Integer) ((Object[]) resultList.get(3))[1];
		Integer totalBillData = (Integer) ((Object[]) resultList.get(4))[1];
		Integer dailyConsumption = (Integer) ((Object[]) resultList.get(5))[1];
		Integer totalRevenue = (Integer) ((Object[]) resultList.get(6))[1];
		Integer usagePer = null;

		int dataPlan = customer.getDataPlan().getMbPerMonth();

		if(totalDataUsage != null && totalDataUsage > -1){
			usagePer = (totalDataUsage/dataPlan)*100;
		}

		JsonObject json = new JsonObject();

		if(totalAlert != null){
			json.addProperty("totalAlert", totalAlert);
		}else{
			json.addProperty("totalAlert", "NA");
		}

		if(perCapita != null){
			json.addProperty("perCapita", perCapita);
		}else{
			json.addProperty("perCapita", "NA");
		}

		if(totalEP != null){
			json.addProperty("totalEP", totalEP);
		}else{
			json.addProperty("totalEP", "NA");
		}

		if(totalDataUsage != null){
			json.addProperty("totalDataUsage", totalDataUsage);
		}else{
			json.addProperty("totalDataUsage", "NA");
		}

		if(usagePer != null){
			json.addProperty("usagePer", usagePer);
		}else{
			json.addProperty("usagePer", "NA");
		}

		if(totalBillData != null){
			json.addProperty("totalBillData", totalBillData);
		}else{
			json.addProperty("totalBillData", "NA");
		}

		if(startDate != null){
			json.addProperty("billDate", sdf1.format(startDate));
		}else{
			json.addProperty("billDate", "NA");
		}

		if(abnormalUsers > 0){
			json.addProperty("abnormalUsers", abnormalUsers);
		}else{
			json.addProperty("abnormalUsers", 0);
		}

		if(dailyConsumption != null){
			json.addProperty("dailyConsumption", dailyConsumption);
		}else{
			json.addProperty("dailyConsumption", "NA");
		}

		if(totalRevenue != null){
			json.addProperty("totalRevenue", totalRevenue);
		}else{
			json.addProperty("totalRevenue", "NA");
		}

		List<String> notificationList= userService.getCustomerNotifications(Integer.parseInt(customerId));

		objectList.add(notificationList);
		objectList.add(json);

		if(notificationList != null){
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Gson().toJson("nonotification"),HttpStatus.OK);
		}

	}

	//Method to search and generate report for asset(Endpoints or DC) according to the inspection date
	@RequestMapping(value="/searchAssetInspectionSchedule",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchAssetInspectionSchedule(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=true) String reportType,
			@RequestParam(value="selectedAsset",required=true) String assetType,
			@RequestParam(value="selectedInspectionInterval",required=true) String inspectionInterval,
			@RequestParam(value="inspectionDueWithin",required=true) String inspectionDueWithin,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();
		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
		Date currentDate = new Date();
		List<ConsumerMeter> searchedList = null;
		List<DataCollector> dcSearchedList = null;
		List<JsonObject> assetInspectionScheduleList = new ArrayList<JsonObject>();
		List<JsonObject> assetInspectionScheduleListForDC = new ArrayList<JsonObject>();
		if(assetType.equalsIgnoreCase("Endpoints")){

			searchedList = userService.getAssetInspectionScheduleForEndpoints(siteId,siteName,inspectionDueWithin,inspectionInterval,reportType,customerId);

			if(searchedList.size() > 0){

				Iterator<ConsumerMeter> consumerMeterIterator = searchedList.iterator();
				while(consumerMeterIterator.hasNext()){

					ConsumerMeter conusmerMeter = consumerMeterIterator.next();

					JsonObject  json = new JsonObject();
					json.addProperty("assetSerialNumber", conusmerMeter.getEndpointSerialNumber());
					json.addProperty("assetInspectionDate", sdf.format(conusmerMeter.getAssetInspectionDate()));
					json.addProperty("streetName", conusmerMeter.getStreetName());
					json.addProperty("address1", conusmerMeter.getAddress1());
					json.addProperty("address2", conusmerMeter.getAddress2());
					json.addProperty("address3", conusmerMeter.getAddress3());
					json.addProperty("zipcode", conusmerMeter.getZipcode());
					if(conusmerMeter.getAssetInspectionDate().before(currentDate)){
						json.addProperty("changeCss", true);
					}else{
						json.addProperty("changeCss", false);
					}

					assetInspectionScheduleList.add(json);
				}
				return new ResponseEntity<String>(new Gson().toJson(assetInspectionScheduleList), HttpStatus.OK);
			}
		}	

		if(assetType.equalsIgnoreCase("DataCollectors")){

			dcSearchedList = userService.getAssetInspectionScheduleForDataCollectors(siteId,siteName,inspectionDueWithin,inspectionInterval,reportType,customerId);

			if(dcSearchedList.size() > 0){

				Iterator<DataCollector> dataCollectorIterator = dcSearchedList.iterator();
				while(dataCollectorIterator.hasNext()){

					DataCollector dataCollector = dataCollectorIterator.next();

					JsonObject  json = new JsonObject();

					json.addProperty("assetSerialNumber", dataCollector.getDcSerialNumber());
					json.addProperty("assetInspectionDate", sdf.format(dataCollector.getAssetInspectionDate()));

					json.addProperty("latitude", dataCollector.getLatitude());
					json.addProperty("longitude", dataCollector.getLongitude());

					if(dataCollector.getAssetInspectionDate().before(currentDate)){
						json.addProperty("changeCss", true);
					}else{
						json.addProperty("changeCss", false);
					}
					assetInspectionScheduleListForDC.add(json);
				}
				return new ResponseEntity<String>(new Gson().toJson(assetInspectionScheduleListForDC), HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>(new Gson().toJson("noAssetInspectionScheduleFound"), HttpStatus.OK);		
	}

	//Method to search and generate consumer meter alerts report
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchConsumerMeterAlerts",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchConsumerMeterAlerts(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String reportType,
			@RequestParam(value="reportPeriod",required=false) Integer reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="selectedAlertType",required=false) String selectedAlertType,
			@RequestParam(value="alerts",required=false) String alert,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="installationName",required=false) String installationName,
			@RequestParam(value="zipCode",required=false) String zipCode){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int customerId = customer.getCustomerId();

		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		List<Object> searchedList = new ArrayList<Object>();
		List<Object> objectList = new ArrayList<Object>();
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
						searchedList = userService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,siteId,siteName,zipCode,customerId);
						if(searchedList != null){
							consumerMeterTransactionList = (List<ConsumerMeterTransaction>) searchedList.get(0);
							consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
						}
					}
					if(!siteExist){
						tempConsumerMeterAlertsList = userService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,site.getSiteId(),siteName,zipCode,customerId);
						if(tempConsumerMeterAlertsList != null){
							consumerMeterTransactions = (List<ConsumerMeterTransaction>) tempConsumerMeterAlertsList.get(0);
							consumerAlertModelList = (List<ConsumerAlertListModel>) searchedList.get(1);
						}
						if(tempConsumerMeterAlertsList != null && tempConsumerMeterAlertsList.size() > 0){
							Iterator<ConsumerMeterTransaction> tempConsumerMeterAlertsIterator = consumerMeterTransactions.iterator();
							while (tempConsumerMeterAlertsIterator.hasNext()) {
								consumerMeterTransactionList.add(tempConsumerMeterAlertsIterator.next());
							}
						}
					}
				}
			}
		}else{
			searchedList = userService.getConsumerMeterAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,siteId,siteName,zipCode,customerId);
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
						if(conAlertObj.getZipcode() != null){
							json.addProperty("zipcode",conAlertObj.getZipcode());
						}
						else{
							json.addProperty("zipcode","-");
						}
					}
				}
				if(conMeterTransaction.getAlerts()!=null){
					json.addProperty("registerId", conMeterTransaction.getRegisterId());
					json.addProperty("alert", conMeterTransaction.getAlerts());
					if(!conMeterTransaction.isAlerts_ack()){
						json.addProperty("ack", "N");
					} else{
						json.addProperty("ack", "Y");
					}
					json.addProperty("dateFlagged",sdf.format(conMeterTransaction.getTimeStamp()));
					consumerMeterAlertsList.add(json);
				}	
			}
			if(consumerMeterAlertsList.size()>0){
				return new ResponseEntity<String>(new Gson().toJson(consumerMeterAlertsList), HttpStatus.OK);
			}
		}		
		return new ResponseEntity<String>(new Gson().toJson("noConsumerMeterAlertsFound"), HttpStatus.OK);
	}

	//Method to update alert status according to the criteria selected in the popup box of consumer meter alerts
	@RequestMapping(value="/updateAlertStatusForConsumerMeters",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updateAlertStatusForConsumerMeters(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="acknowledge",required=false) boolean acknowledge,
			@RequestParam(value="requestToResetAllAlerts",required=false) boolean requestToResetAllAlerts,
			@RequestParam(value="assignToTechnician",required=false) boolean assignToTechnician,
			@RequestParam(value="technicianName",required=false) String technicianName,
			@RequestParam(value="registerId",required=false) String registerId,
			@RequestParam(value="dateFlagged",required=false)@DateTimeFormat(pattern="MM-dd-yyyy HH:mm:ss") Date dateFlagged){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		if(acknowledge || requestToResetAllAlerts || assignToTechnician){
			if(acknowledge){
				userService.updateAlertAckForConsumerMeter(registerId,dateFlagged);
			}
			if(assignToTechnician){

				if(technicianName == null){
					return new ResponseEntity<String>(new Gson().toJson("namecannotbenull"), HttpStatus.OK);
				}

				MaintenanceTechnician technician = userService.getTechnicianById(Integer.parseInt(technicianName));
				String email = technician.getContactdetails().getEmail1();
				String subject = "New Endpoint Alert Received.";
				ConsumerMeter consumerMeter = userService.getConsumerMetetByRegisterId(registerId, 0);

				KDLEmailModel model = new KDLEmailModel();
				model.setSubject(subject);
				model.setName(technician.getContactdetails().getFirstName()+" "+technician.getContactdetails().getLastname());
				model.setEndPointRegId(Integer.parseInt(registerId));
				model.setEndLatitude(consumerMeter.getLatitude());
				model.setEndLongitude(consumerMeter.getLongitude());
				model.setEndAddress1(consumerMeter.getAddress1());
				model.setEndAddress2(consumerMeter.getAddress2());
				model.setEndAddress3(consumerMeter.getAddress3());
				model.setStrretName(consumerMeter.getStreetName());
				model.setZip(consumerMeter.getZipcode());
				model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());

				Map<String, KDLEmailModel> map = new HashMap();
				map.put("emailConst", model);

				try {
					mailSender.htmlEmailSender(email, subject, map, "endPointMail", null);
				} catch (Exception e) {
					logger.error(e.getMessage());
					return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
				}

			}
			if(requestToResetAllAlerts){

				ConsumerMeter consumerMeter = userService.getConsumerMetetByRegisterId(registerId, 0);

				if(consumerMeter.getDataCollector() != null){
					int dcId = consumerMeter.getDataCollector().getDatacollectorId();

					DatacollectorMessageQueue messageQueue = new DatacollectorMessageQueue();
					messageQueue.setDatacollectorId(dcId);
					messageQueue.setRegisterId(registerId);
					messageQueue.setType("Reset");
					messageQueue.setMessageColor(ApplicationConstants.NEWMESSAGE);
					messageQueue.setCreatedBy(customer.getCustomerId());
					messageQueue.setTimeAdded(DateTimeConversionUtils.getDateInUTC(new Date()));
					messageQueue.setCreatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

					userService.addNewDCMessage(messageQueue);
					return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
				}
				return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
			}

			return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
	}

	//Method to search and generate network alerts report
	@RequestMapping(value="/searchNetworkAlerts",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchNetworkAlerts(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String reportType,
			@RequestParam(value="reportPeriod",required=false) Integer reportPeriod,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date endDate,
			@RequestParam(value="selectedAlertType",required=false) String selectedAlertType,
			@RequestParam(value="alerts",required=false) String alert,
			@RequestParam(value="siteId",required=false) Integer siteId,
			@RequestParam(value="siteName",required=false) String siteName,
			@RequestParam(value="installationName",required=false) String installationName,
			@RequestParam(value="dcSerialNo",required=false) String dcSerialNo){

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
						dataCollectorList = userService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,siteId,siteName,dcSerialNo,customerId);
					}
					if(!siteExist){
						tempDataCollectorList = userService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,site.getSiteId(),siteName,dcSerialNo,customerId);
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
			dataCollectorList = userService.getNetworkAlerts(reportType,reportPeriod,startDate,endDate,selectedAlertType,alert,siteId,siteName,dcSerialNo,customerId);
		}

		if(dataCollectorList!=null && dataCollectorList.size() > 0){
			Iterator<DataCollectorAlerts> dataCollectorIterator = dataCollectorList.iterator();
			while(dataCollectorIterator.hasNext()){
				DataCollectorAlerts dataCollectorAlerts = dataCollectorIterator.next();
				JsonObject  json = new JsonObject();
				if(dataCollectorAlerts.getAlert()!=null){
					json.addProperty("dcSerialNo", dataCollectorAlerts.getDataCollector().getDcSerialNumber());
					json.addProperty("alert", dataCollectorAlerts.getAlert());
					json.addProperty("dateFlagged", sdf.format(dataCollectorAlerts.getAlertDate()));
					if(!dataCollectorAlerts.isAlertAck()){
						json.addProperty("ack", "N");
					} else{
						json.addProperty("ack", "Y");
					}
					networkAlertsList.add(json);
				}
			}
			return new ResponseEntity<String>(new Gson().toJson(networkAlertsList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("noNetworkAlertsFound"), HttpStatus.OK);
	}

	//Method to update alert status according to the criteria selected in the popup box of network alerts
	@RequestMapping(value="/updateAlertStatusForDC",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updateAlertStatusForDC(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="acknowledge",required=false) boolean acknowledge,
			@RequestParam(value="assignToTechnician",required=false) boolean assignToTechnician,
			@RequestParam(value="technicianName",required=false) String technicianName,
			@RequestParam(value="dcSerialNo",required=false) String dcSerialNo){

		if(acknowledge || assignToTechnician){
			if(acknowledge){
				userService.updateAlertAckForDC(dcSerialNo);
			}
			if(assignToTechnician){

				if(technicianName == null){
					return new ResponseEntity<String>(new Gson().toJson("namecannotbenull"), HttpStatus.OK);
				}

				MaintenanceTechnician technician = userService.getTechnicianById(Integer.parseInt(technicianName));
				String email = technician.getContactdetails().getEmail1();
				String subject = "New DataCollector Alert Received.";
				String[] dcSerialNoList = {dcSerialNo};
				List<DataCollector> dcList = userService.getDCBySerialNumber(dcSerialNoList);

				KDLEmailModel model = new KDLEmailModel();
				model.setSubject(subject);
				model.setName(technician.getContactdetails().getFirstName()+" "+technician.getContactdetails().getLastname());
				model.setDcSerialNo(dcSerialNo);
				model.setDcNetworkId(dcList.get(0).getNetworkId());
				model.setDcLatitude(dcList.get(0).getLatitude());
				model.setDcLongitude(dcList.get(0).getLongitude());
				model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());

				Map<String, KDLEmailModel> map = new HashMap();
				map.put("emailConst", model);

				try {
					mailSender.htmlEmailSender(email, subject, map, "dcAlert", null);
				} catch (Exception e) {
					logger.error(e.getMessage());
					return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
				}

			}
			return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);
	}


	// Dhaval Code Start

	@RequestMapping(value="/initDCMessageQueueData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initDCMessageQueueData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> regionJsonList = new ArrayList<JsonObject>();
		List<Region> regionList = userService.getRegionListByCustomerId(customer.getCustomerId());

		if(regionList != null){
			Iterator<Region> regionIterator = regionList.iterator();

			while(regionIterator.hasNext()){
				Region region = regionIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("regionId", region.getRegionId());
				json.addProperty("regionName", region.getRegionName());
				regionJsonList.add(json);
			}

			objectList.add(regionJsonList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessRegionList"), HttpStatus.OK);
	}

	@RequestMapping(value="/getSiteList",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getSiteList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="regionId",required=false) String regionId){

		List<Object> objectList = new ArrayList<Object>();
		if(regionId != null && regionId.trim() != ""){

			Region region = userService.getRegionById(Integer.parseInt(regionId));
			List<JsonObject> siteList = userService.getSiteWithDCs(region);
			objectList.add(siteList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("error"), HttpStatus.OK);

	}

	@RequestMapping(value="/getDCList",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getDCList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId",required=false) String siteId){

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> dcJsonList = new ArrayList<JsonObject>();
		Site site = userService.getSiteDataBySiteId(siteId);
		List<DataCollector> dcList = userService.getDataCollectorBySite(site);

		if(dcList != null){
			Iterator<DataCollector> dcIterator = dcList.iterator();

			while(dcIterator.hasNext()){
				DataCollector dataCollector = dcIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("datacollectorId", dataCollector.getDatacollectorId());
				json.addProperty("dcSerialNumber", dataCollector.getDcSerialNumber());
				dcJsonList.add(json);
			}

			objectList.add(dcJsonList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessDCList"), HttpStatus.OK);
	}

	@RequestMapping(value="/getMessageList",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getMessageList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="datacollectorId",required=false) String datacollectorId){

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> dcMessageQueueJsonList = new ArrayList<JsonObject>();

		List<DatacollectorMessageQueue> dataCollectorMessageQueueList = userService.getDCMessageQueueByDCId(Integer.parseInt(datacollectorId));

		if(dataCollectorMessageQueueList != null){
			Iterator<DatacollectorMessageQueue> dcMessageQueueIterator = dataCollectorMessageQueueList.iterator();

			while(dcMessageQueueIterator.hasNext()){
				DatacollectorMessageQueue datacollectorMessageQueue = dcMessageQueueIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("datacollectorMessageQueueId", datacollectorMessageQueue.getDatacollectorMessageQueueId());
				json.addProperty("registerId", datacollectorMessageQueue.getRegisterId());
				json.addProperty("type", datacollectorMessageQueue.getType());
				json.addProperty("value", 0);
				json.addProperty("messageColor", datacollectorMessageQueue.getMessageColor());
				json.addProperty("timeAdded", datacollectorMessageQueue.getTimeAdded().toString());

				dcMessageQueueJsonList.add(json);
			}

			objectList.add(dcMessageQueueJsonList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessDCList"), HttpStatus.OK);
	}


	// Dhaval Code End

	// Battery Replacement Schedule Code Start 
	@RequestMapping(value="/getBatteryReplacementData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getBatteryReplacementData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="selectedReport",required=false) String selectedReport,@RequestParam(value="reportPeriod",required=false) String reportPeriod,
			@RequestParam(value="installation",required=false) String installation,@RequestParam(value="siteId",required=false) String siteId,
			@RequestParam(value="siteName",required=false) String siteName,@RequestParam(value="zipcode",required=false) String zipcode){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> jsonList = new ArrayList<JsonObject>();
		List<String> registerIdWithAlerts = new ArrayList<String>();
		Map<String, Date> alertMeterMap = new HashMap<>();
		List<ConsumerMeter> consumerMeterWithAlerts = new ArrayList<ConsumerMeter>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

		// Get all meter for specific criteria
		List<ConsumerMeter> meterList = consumerMeterService.getBatteryReplacementData(selectedReport,Integer.parseInt(reportPeriod), installation,siteId,siteName,zipcode,customer.getCustomerId());

		List<String> registerIds = new ArrayList<String>();

		if(meterList != null){
			// fill up registerids
			meterList.forEach(m->{
				if(m.getRegisterId()!= null){
					registerIds.add(m.getRegisterId());
				}
			});
		}else {
			//if date criteria not match still find meter who has alerts from transaction table
			//take all meter of customer
			List<ConsumerMeter> cm = consumerMeterService.getConsumerMetersByCustomerId(customer.getCustomerId());
			if(cm!=null){
				cm.forEach(m->{
					// fill up registerids
					if(m.getRegisterId()!= null){
						registerIds.add(m.getRegisterId());
					}
				});
			}else {
				return new ResponseEntity<String>(new Gson().toJson("NoBatteryData"), HttpStatus.OK);
			}

		}

		// get meters registers from registerIds who has alert
		if(registerIds != null){
			alertMeterMap = consumerMeterService.getBatteryAlerts(registerIds);
			if(alertMeterMap!=null){
				registerIdWithAlerts.addAll(alertMeterMap.keySet());
			}
		}

		// get meter obj from reg. id
		if(registerIdWithAlerts != null){
			consumerMeterWithAlerts = consumerMeterService.getConsumerMeterByRegisterId(registerIdWithAlerts, customer.getCustomerId());
		}
		// Calculate due
		if(meterList != null){

			Iterator<ConsumerMeter> meterListIterator = meterList.iterator();
			Calendar rangeDate = Calendar.getInstance(); 
			if(selectedReport.equals("Week")){
				rangeDate.add(Calendar.WEEK_OF_MONTH, Integer.parseInt(reportPeriod));
				rangeDate.setTime(rangeDate.getTime());
			}
			if(selectedReport.equals("Month")){
				rangeDate.add(Calendar.MONTH, Integer.parseInt(reportPeriod));
				rangeDate.setTime(rangeDate.getTime());
			}
			while(meterListIterator.hasNext()){
				boolean due = false;

				ConsumerMeter meter = meterListIterator.next();

				Calendar scheduledReplacementDate = Calendar.getInstance();

				scheduledReplacementDate.setTime(meter.getEndpointBatteryReplacedDate());
				String yearToBeAdded = meter.getEstimatedRemainaingBatteryLifeInYear().toString();

				if(yearToBeAdded.contains(".")){
					String[] yearArray = yearToBeAdded.split(Pattern.quote("."));
					if(yearArray.length > 0){
						int year = Integer.parseInt(yearArray[0]);
						int month = Integer.parseInt(yearArray[1]);
						scheduledReplacementDate.add(Calendar.YEAR, year);
						scheduledReplacementDate.add(Calendar.MONTH, month);
						scheduledReplacementDate.setTime(scheduledReplacementDate.getTime());
					}
				}else{
					scheduledReplacementDate.add(Calendar.YEAR,Integer.parseInt(yearToBeAdded));
				}

				Calendar currentDate = Calendar.getInstance();

				if(scheduledReplacementDate.before(currentDate)){
					due = true;
					JsonObject json = new JsonObject();
					json.addProperty("meterId", meter.getConsumerMeterId());
					json.addProperty("registerId", meter.getRegisterId());
					json.addProperty("address1", meter.getAddress1());
					json.addProperty("address2", meter.getAddress2());
					json.addProperty("address3", meter.getAddress3());
					json.addProperty("address4", meter.getStreetName());
					json.addProperty("zipcode", meter.getZipcode());
					json.addProperty("scheduledReplacementDate", dateFormat.format(scheduledReplacementDate.getTime()));
					json.addProperty("due", 1);
					json.addProperty("dateFlagged", "-");
					json.addProperty("batteryAlert", 0);

					jsonList.add(json);

				}else if(scheduledReplacementDate.after(currentDate) && scheduledReplacementDate.before(rangeDate)){
					due = false;
					JsonObject json = new JsonObject();
					json.addProperty("meterId", meter.getConsumerMeterId());
					json.addProperty("registerId", meter.getRegisterId());
					json.addProperty("address1", meter.getAddress1());
					json.addProperty("address2", meter.getAddress2());
					json.addProperty("address3", meter.getAddress3());
					json.addProperty("address4", meter.getStreetName());
					json.addProperty("zipcode", meter.getZipcode());
					json.addProperty("dateFlagged", "-");
					json.addProperty("scheduledReplacementDate", dateFormat.format(scheduledReplacementDate.getTime()));
					json.addProperty("due", 0);
					json.addProperty("batteryAlert", 0);
					jsonList.add(json);
				}
			}

			// add alert meter to list and remove duplicate
			if(consumerMeterWithAlerts!=null){
				//alertMeterMap.keySet();
				consumerMeterWithAlerts.forEach(consumerMeterAlert->{
					JsonObject json = new JsonObject();

					Calendar scheduledReplacementDate = Calendar.getInstance();

					scheduledReplacementDate.setTime(consumerMeterAlert.getEndpointBatteryReplacedDate());
					String yearToBeAdded = consumerMeterAlert.getEstimatedRemainaingBatteryLifeInYear().toString();

					if(yearToBeAdded.contains(".")){
						String[] yearArray = yearToBeAdded.split(Pattern.quote("."));
						if(yearArray.length > 0){
							int year = Integer.parseInt(yearArray[0]);
							int month = Integer.parseInt(yearArray[1]);
							scheduledReplacementDate.add(Calendar.YEAR, year);
							scheduledReplacementDate.add(Calendar.MONTH, month);
							scheduledReplacementDate.setTime(scheduledReplacementDate.getTime());
						}
					}

					json.addProperty("meterId", consumerMeterAlert.getConsumerMeterId());
					json.addProperty("registerId", consumerMeterAlert.getRegisterId());
					json.addProperty("address1", consumerMeterAlert.getAddress1());
					json.addProperty("address2", consumerMeterAlert.getAddress2());
					json.addProperty("address3", consumerMeterAlert.getAddress3());
					json.addProperty("address4", consumerMeterAlert.getStreetName());
					json.addProperty("zipcode", consumerMeterAlert.getZipcode());
					json.addProperty("scheduledReplacementDate",dateFormat.format(scheduledReplacementDate.getTime()));
					json.addProperty("due", 0);
					json.addProperty("batteryAlert", 1);
					jsonList.add(json);
				});
			}

			//Remove duplicate from json list
			for(int i=0;i<jsonList.size();i++){

				for(int j=i+1;j<jsonList.size();j++){

					if(jsonList.get(i).get("registerId").toString().replace("\"", "").equals(jsonList.get(j).get("registerId").toString().replace("\"", ""))){
						//set flagged date if duplicate found 
						Calendar cal = Calendar.getInstance();
						String mapKeyValue = jsonList.get(i).get("registerId").toString().replace("\"", "");
						cal.setTime(alertMeterMap.get(mapKeyValue));
						jsonList.get(j).addProperty("dateFlagged", dateFormat.format(cal.getTime()));
						// delete old one
						jsonList.remove(i);
						j--;
					}
				}

			}
			objectList.add(jsonList);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);

		}else if (consumerMeterWithAlerts != null) {
			// all meter having battery alert
			for (int j = 0; j < consumerMeterWithAlerts.size(); j++) {

				ConsumerMeter consumerMeterAlert = consumerMeterWithAlerts.get(j);
				JsonObject json = new JsonObject();

				Calendar scheduledReplacementDate = Calendar.getInstance();

				scheduledReplacementDate.setTime(consumerMeterAlert.getEndpointBatteryReplacedDate());
				String yearToBeAdded = consumerMeterAlert.getEstimatedRemainaingBatteryLifeInYear().toString();
				//set scheduled replacement date
				if(yearToBeAdded.contains(".")){
					String[] yearArray = yearToBeAdded.split(Pattern.quote("."));
					if(yearArray.length > 0){
						int year = Integer.parseInt(yearArray[0]);
						int month = Integer.parseInt(yearArray[1]);
						scheduledReplacementDate.add(Calendar.YEAR, year);
						scheduledReplacementDate.add(Calendar.MONTH, month);
						scheduledReplacementDate.setTime(scheduledReplacementDate.getTime());
					}
				}

				Calendar cal = Calendar.getInstance();
				Object mapKeyValue = consumerMeterAlert.getRegisterId();
				cal.setTime(alertMeterMap.get(mapKeyValue));

				json.addProperty("meterId", consumerMeterAlert.getConsumerMeterId());
				json.addProperty("registerId", consumerMeterAlert.getRegisterId());
				json.addProperty("address1", consumerMeterAlert.getAddress1());
				json.addProperty("address2", consumerMeterAlert.getAddress2());
				json.addProperty("address3", consumerMeterAlert.getAddress3());
				json.addProperty("address4", consumerMeterAlert.getStreetName());
				json.addProperty("zipcode", consumerMeterAlert.getZipcode());
				json.addProperty("scheduledReplacementDate",dateFormat.format(scheduledReplacementDate.getTime()));
				json.addProperty("due", 0);
				json.addProperty("batteryAlert", 1);
				json.addProperty("dateFlagged", dateFormat.format(cal.getTime()));
				jsonList.add(json);
			}
			objectList.add(jsonList);
			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}

		return new ResponseEntity<String>(new Gson().toJson("NoBatteryData"), HttpStatus.OK);
	}

	//Battery Replacement Schedule Code End

	@RequestMapping(value="/initdcConnectionMap",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initdcConnectionMap(HttpServletRequest request,HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());
		List<Object> responselist = new ArrayList<>();
		for(DataCollector dc : customer.getDataCollector()){
			JsonObject  json = new JsonObject();
			json.addProperty("dcId", dc.getDatacollectorId());
			json.addProperty("dcSerialNo", dc.getDcSerialNumber());
			responselist.add(json);
		}
		return new ResponseEntity<String>(new Gson().toJson(responselist),HttpStatus.OK);
	}

	@RequestMapping(value="/getConnectionMap",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getConnectionMap(HttpServletRequest request,HttpServletResponse response, @RequestParam String dcId){
		List<ConsumerMeter> consumerMeters = consumerMeterService.consumerMeterListByDCId(dcId);
		StringBuilder graphData = new StringBuilder();

		graphData.append("[");
		for (int i = 0; i < consumerMeters.size(); i++)
		{
			Integer parentSlot = (Integer)(consumerMeters.get(i).getParentSlot());

			if (i == consumerMeters.size()-1)
			{
				graphData.append(" [\"" + consumerMeters.get(i).getEndpointSerialNumber() + "\", \"" + getEndPointSerialNumber(parentSlot == null ? 0 : parentSlot, consumerMeters) + "\", \"" + consumerMeters.get(i).getConsumerMeterId() + "\"] ");
			}
			else
			{
				graphData.append(" [\"" + consumerMeters.get(i).getEndpointSerialNumber() + "\", \"" + getEndPointSerialNumber(parentSlot == null ? 0 : parentSlot, consumerMeters) + "\", \"" + consumerMeters.get(i).getConsumerMeterId() + "\"], ");
			}
		}
		graphData.append("]");


		return new ResponseEntity<String>(new Gson().toJson(graphData),HttpStatus.OK);
	}

	public String getEndPointSerialNumber(int parentSlot, List<ConsumerMeter> consumerMeters) {
		if(parentSlot == 0 )	return "";

		for(ConsumerMeter consumerMeter : consumerMeters) {
			if(consumerMeter.getMySlot() == parentSlot) {
				return consumerMeter.getEndpointSerialNumber();
			}
		}
		return "";

	}
	
	// Request Mapping for Customer Controller 
	
	@RequestMapping(value = "customerDashboard",method = RequestMethod.GET)
	public ModelAndView customerDashboard(HttpServletRequest request) {
		User currentUser =(User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(currentUser.getUserId());
		ModelAndView model = new ModelAndView();
		if(customer != null)
			model.addObject("customerId", customer.getCustomerId());
		model.setViewName("customerDashboard");
		return model;
	}
	
	@RequestMapping(value = "customerleftNavigationbar",method = RequestMethod.GET)
	public ModelAndView customerleftNavigationbar(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("customerLeftNavigationbar");
	}
	
	@RequestMapping(value = "customerProfile",method = RequestMethod.GET)
	public ModelAndView customerProfile(HttpServletRequest request) {
		return new ModelAndView("customerProfile");
	}

	@RequestMapping(value = "consumerUserManagement",method = RequestMethod.GET)
	public ModelAndView consumerUserManagement(HttpServletRequest request) {
		return new ModelAndView("consumerUserManagement");
	}
	
	@RequestMapping(value="/addConsumerUserRedirect",method=RequestMethod.GET)
	public ModelAndView addConsumerUserRedirect(HttpServletRequest request, HttpServletResponse response){

		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		if(currentUser.getRole().getRoleName().equalsIgnoreCase("customer"))
			return new ModelAndView("addConsumerUser");
		else
			return new ModelAndView("consumerUserManagement");
	}
	
	@RequestMapping(value="/editCustomerPageRedirect",method=RequestMethod.GET)
	public ModelAndView editCustomerPageRedirect(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("editCustomerEP");
	}
	
	@RequestMapping(value="/editConsumerPageRedirect",method=RequestMethod.GET)
	public ModelAndView editConsumerPageRedirect(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("editConsumer");
	}

	@RequestMapping(value="/editConsumerUserPageRedirect",method=RequestMethod.GET)
	public ModelAndView editConsumerUserPageRedirect(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("editConsumerUser");
	}
	
	@RequestMapping(value="/normalUserManagement",method=RequestMethod.GET)
	public ModelAndView normalUserPageRedirect(HttpServletRequest request){
		return new ModelAndView("normalUserManagement");
	}
	
	@RequestMapping(value="/addNormalCustomer",method=RequestMethod.GET)
	public ModelAndView normalCustomerAddPageRedirect(HttpServletRequest request){
		return new ModelAndView("addNormalCustomer");
	}
	

	@RequestMapping(value="/editNormalCustomer",method=RequestMethod.GET)
	public ModelAndView normalCustomerEditPageRedirect(HttpServletRequest request){
		return new ModelAndView("editNormalCustomer");
	}
	
	@RequestMapping(value = "customerEPManagement",method = RequestMethod.GET)
	public ModelAndView customerEPManagement(HttpServletRequest request) {
		return new ModelAndView("consumerManagement");
	}
	
	@RequestMapping(value="/siteManagement",method=RequestMethod.GET)
	public ModelAndView redirectToCommissioning(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("sitepage");
	}

	@RequestMapping(value="/addNewSiteRedirect",method=RequestMethod.GET)
	public ModelAndView redirectToAddNewSite(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("addNewSite");
	}

	@RequestMapping(value="/editSiteRedirect",method = RequestMethod.GET)
	public ModelAndView editSiteRedirect(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("editSite");
	}
	
	@RequestMapping(value = "/addRegionForm",method = RequestMethod.GET)
	public ModelAndView regionAddUpdateForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditRegion");
	}

	@RequestMapping(value = "/editRegionForm",method = RequestMethod.GET)
	public ModelAndView editRegionForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditRegion");
	}
	
	@RequestMapping(value = "addDUMeterForm",method = RequestMethod.GET)
	public ModelAndView addDUMeter(HttpServletRequest request) {
		return new ModelAndView("addEditDUMeter");
	}

	@RequestMapping(value = "/editDUMeterForm",method = RequestMethod.GET)
	public ModelAndView editDUMeterForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditDUMeter");
	}
	
	@RequestMapping(value = "dcManagement",method = RequestMethod.GET)
	public ModelAndView dcManagement(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dcManagement");
	}
	
	@RequestMapping(value = "regionManagement",method = RequestMethod.GET)
	public ModelAndView RegionManagement(HttpServletRequest request) {
		return new ModelAndView("regionManagement");
	}
	
	@RequestMapping(value = "districtUtilityMeter",method = RequestMethod.GET)
	public ModelAndView districtUtilityMeter(HttpServletRequest request) {
		return new ModelAndView("districtUtilityMeterPage");
	}
	
	@RequestMapping(value="/tariffManagement",method=RequestMethod.GET)
	public ModelAndView tariffManagementRedirect(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="message",required=false) String message){
		return new ModelAndView("tariffManagement");
	}
	
	@RequestMapping(value="/addNewTariff",method=RequestMethod.GET)
	public ModelAndView addNewTariff(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("addNewTariff");
	}

	@RequestMapping(value="/editTariff",method=RequestMethod.GET)
	public ModelAndView editTariff(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("editTariff");
	}
	
	//Redirecting to Installer Management
		@RequestMapping(value="/installerManagement",method=RequestMethod.GET)
		public ModelAndView installerManagement(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "message", required = false) String message){
			logger.info("Redirecting to Installer Management");
			ModelAndView model = new ModelAndView("installerManagement");
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
			model.addObject("customerId",customer.getCustomerId());
			if(message!=null){
				model.addObject("messageFlag",true);
				if(message.equals("update")){
					model.addObject("message","Installer Updated Successfully");
				}
				if(message.equals("success")){
					model.addObject("message","Installer Added Successfully");
				}

			}
			return model;
		}
	
		//Redirecting to technicianManagement
		@RequestMapping(value="/technicianManagement",method=RequestMethod.GET)
		public ModelAndView technicianManagement(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "message", required = false) String message){
			logger.info("Redirecting Technician Management");
			ModelAndView model = new ModelAndView("technicianManagement");
			if(message!=null){
				model.addObject("messageFlag",true);
				if(message.equals("update")){
					model.addObject("message","Technician Updated Successfully");
				}
				if(message.equals("success")){
					model.addObject("message","Technician Added Successfully");
				}

			}
			return model;
		}

	@RequestMapping(value="/schedulerManagement",method=RequestMethod.GET)
	public ModelAndView schedulerManagement(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("schedulerPage");
	}
	
	//*************** Report & Analytics ********************************
	
	@RequestMapping(value = "dataUsageReport",method = RequestMethod.GET)
	public ModelAndView dataUsageReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dataUsageReport");
	}
	
	@RequestMapping(value = "billingDataReport",method = RequestMethod.GET)
	public ModelAndView billingDataReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("billingDataReport");
	}
	@RequestMapping(value="/abnormalConsumptionReport",method = RequestMethod.GET)
	public ModelAndView abnormalConsumptionReportPageRedirect(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("abnormalConsumptionReport");
	}
	
	@RequestMapping(value = "/networkWaterLossReport",method = RequestMethod.GET)
	public ModelAndView networkWaterLossReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkWaterLossReport");
	}
	
	@RequestMapping(value = "/alertReport",method = RequestMethod.GET)
	public ModelAndView alertReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("alertReport");
	}
	
	@RequestMapping(value = "/aggregateConsumptionReport",method = RequestMethod.GET)
	public ModelAndView aggregateConsumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("aggregateConsumptionReport");
	}
	
	@RequestMapping(value = "/dcConnectionMap",method = RequestMethod.GET)
	public ModelAndView dcConnectionMap(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dcConnectionMapPage");
	}
	
	@RequestMapping(value = "/networkConsumptionReport",method = RequestMethod.GET)
	public ModelAndView networkConsumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkConsumptionReportPage");
	}
	@RequestMapping(value="/abnormalConsumptionActionGraph",method=RequestMethod.GET)
	public ModelAndView abnormalConsumptionActionGraph(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("abnormalConsumptionActionGraph");
	}
	
	@RequestMapping(value="/freeReport",method=RequestMethod.GET)
	public ModelAndView freereport(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("freeReport");
	}
	
	@RequestMapping(value="/whatIfReport",method=RequestMethod.GET)
	public ModelAndView whatIfReport(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("finanicialWhatIfReport");
	}
	
	@RequestMapping(value = "/consumptionReport",method = RequestMethod.GET)
	public ModelAndView consumptionReport(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("consumptionReportPage");
	}
	

	//********** Installation and Commissioning **************

	@RequestMapping(value="/startContinueInstallation",method=RequestMethod.GET)
	public ModelAndView redirectTo(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("installationAndCommissioningDashBoard");
	}
	
	@RequestMapping(value="/startContinueInstallation",method=RequestMethod.POST)
	public ModelAndView uploadRouteFileTemplatePopup(HttpServletRequest request,@RequestParam(value="siteID")String siteid){
		ModelAndView mav = new ModelAndView();
		request.getSession().setAttribute("selectedSiteId", siteid);
		mav.addObject("selectedSiteId", siteid);
		Site selectedSite = userService.getSiteDataBySiteId(siteid);
		if(selectedSite.getTag()<5)
			mav.setViewName("/startContinueInstallationPage");
		else if(selectedSite.getTag()<7)
			mav.setViewName("redirect:/dataCollectorInstallation");
		else if(selectedSite.getTag()<10)
			mav.setViewName("redirect:/dcEpConfigurationAndTest");
		else if(selectedSite.getTag()<14)
			mav.setViewName("redirect:/commissioning");
		else
			mav.setViewName("redirect:/customerDashboard");
		return mav;
	}
	
	@RequestMapping(value="/installationAndCommissioning",method=RequestMethod.GET)
	public ModelAndView installationAndCommissioning(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("installationAndCommissioningDashBoard");
	}

	@RequestMapping(value="/commissioningphase2",method=RequestMethod.GET)
	public ModelAndView commissioningphase2(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("commissioningphase2");
	}

	@RequestMapping(value="/dcEpConfigurationAndTest",method=RequestMethod.GET)
	public ModelAndView dcEpConfigurationAndTest(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("dcEpConfigurationAndTest");
	}
	
	@RequestMapping(value="/selectedLinesPopup",method=RequestMethod.GET)
	public ModelAndView selectedLinesPopup(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("selectedLinesPopup");
	}
	
	@RequestMapping(value="/addNewLinePopup",method=RequestMethod.GET)
	public ModelAndView addNewLinePopup(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("addNewLinePopup");
	}
	
	@RequestMapping(value="/commissioning",method=RequestMethod.GET)
	public ModelAndView commissioning(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("commissioning");
	}
	
	@RequestMapping(value="/verification",method=RequestMethod.GET)
	public ModelAndView verification(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("verification");
	}
	
	@RequestMapping(value="/dataCollectorInstallation",method=RequestMethod.GET)
	public ModelAndView dataCollectorInstallation(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("dataCollectorInstallationPage");
	}
	
	//********** Maintenance **************
	
	@RequestMapping(value = "assetInspectionSchedule",method = RequestMethod.GET)
	public ModelAndView assetInspectionSchedule(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("assetInspectionSchedule");
	}
	
	@RequestMapping(value = "batteryReplacementSchedule",method = RequestMethod.GET)
	public ModelAndView batteryReplacementSchedule(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("batteryReplacementSchedule");
	}
	
	@RequestMapping(value = "consumerMeterAlerts",method = RequestMethod.GET)
	public ModelAndView consumerMeterAlerts(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("consumerMeterAlerts");
	}
	
	@RequestMapping(value = "networkAlerts",method = RequestMethod.GET)
	public ModelAndView networkAlerts(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("networkAlerts");
	}
	
	@RequestMapping(value = "dcMessageQueue",method = RequestMethod.GET)
	public ModelAndView dcMessageQueue(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dcMessageQueue");
	}
	
	@RequestMapping(value = "/repeatersPlanning",method = RequestMethod.GET)
	public ModelAndView repeatersPlanning(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("repeatersPlanningPage");
	}
	
	@RequestMapping(value="/alerts",method=RequestMethod.GET)
	public ModelAndView alerts(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("checkAlerts");
	}

	@RequestMapping(value="/notes",method=RequestMethod.GET)
	public ModelAndView notes(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("viewUploadNotes");
	}
	
	@RequestMapping(value="/showBillingDataPageRedirect",method=RequestMethod.GET)
	public ModelAndView showBillingDataPageRedirect(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("billingData");
	}
	
	@RequestMapping(value = "editDataCollectorFormForCustomer",method = RequestMethod.GET)
	public ModelAndView editDataCollectorFormForCustomer(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("editDataCollectorByCustomer");
	}
	
	@RequestMapping(value="/consumerAlertsActionPopup",method=RequestMethod.GET)
	public ModelAndView consumerAlertActionpopup(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("ConsumerAlertsActionPopup");
	}
	
	@RequestMapping(value="/networkAlertsActionPopup",method=RequestMethod.GET)
	public ModelAndView networkAlertActionpopup(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("NetworkAlertsActionPopup");
	}

}
