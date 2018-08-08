package com.kenure.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.Consumer;
import com.kenure.entity.ConsumerMeter;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Customer;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.User;
import com.kenure.model.KDLEmailModel;
import com.kenure.service.IConsumerMeterService;
import com.kenure.service.IDistrictUtilityMeterService;
import com.kenure.service.ITariffPlanService;
import com.kenure.service.IUserService;
import com.kenure.utils.DateTimeConversionUtils;
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
@RequestMapping(value="/consumerOperation")
public class ConsumerController {

	private static final org.slf4j.Logger logger = LoggerUtils.getInstance(ConsumerController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	ITariffPlanService tariffPlanService;

	@Autowired
	IConsumerMeterService consumerMeterService;

	@Autowired
	private IDistrictUtilityMeterService duService;

	@Autowired
	ServletContext servletContext;

	@Autowired
	MailSender mailSender;

/*	@RequestMapping(value="/consumerManagementData",method=RequestMethod.POST)
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
					json.addProperty("conAccNum", consumerMeter.getConsumer().getConsumerAccountNumber());
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
	}*/


	/*@RequestMapping(value="/consumerUserManagementData",method=RequestMethod.POST)
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
	}*/


	/*@RequestMapping(value="/addConsumerUserRedirect",method=RequestMethod.GET)
	public ModelAndView addConsumerUserRedirect(HttpServletRequest request, HttpServletResponse response){

		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		if(currentUser.getRole().getRoleName().equalsIgnoreCase("customer"))
			return new ModelAndView("addConsumerUser");
		else
			return new ModelAndView("consumerUserManagement");
	}*/


	/*//getting DataPlan data
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
	}*/


	/*@RequestMapping(value="/checkAlerts",method=RequestMethod.GET)
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
					if (alerts.length > 0) {
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
					}

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
	}*/


	// Kept this code for future use
	/*@RequestMapping(value="/resetAlerts",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> resetAlerts(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true, value="consumerMeterId") String consumerMeterId){

		ConsumerMeter consumerObj = consumerMeterService.getConsumerMeterById(Integer.parseInt(consumerMeterId));

		if (consumerObj.getRegisterId() != null || consumerObj.getRegisterId() != 0) {

			List<ConsumerMeterTransaction> meterTransactionList = consumerMeterService.getConsumerMeterTransactionByRegisterId(consumerObj.getRegisterId());

			// Code to find meter transaction with latest time stamp
			Collections.sort(meterTransactionList, new Comparator<ConsumerMeterTransaction>() {
				public int compare(ConsumerMeterTransaction m1, ConsumerMeterTransaction m2) {
					return m2.getTimeStamp().compareTo(m1.getTimeStamp());
				}
			});

			if (meterTransactionList != null && meterTransactionList.size() > 0) {

				if (meterTransactionList.get(0).getAlerts() != null) {

					if(consumerMeterService.updateAlertAcknowledgementStatusByMeterTransactionId(meterTransactionList.get(0).getId(), true)){

						return new ResponseEntity<String>(new Gson().toJson("Alerts reset completed successfully"), HttpStatus.OK);
					}
					else{

						return new ResponseEntity<String>(new Gson().toJson("No such meter transaction found"), HttpStatus.OK);
					}
				}
			}
		}

		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);		
	}*/


	/*@RequestMapping(value="/uploadNote",method=RequestMethod.POST)
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
					}finally{
						try {
							if(fos != null)
								fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
*/

	/*@RequestMapping(value="/tariffPlanAndBillingFrequencyDataAndSite",method=RequestMethod.POST)
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
	}*/


/*	@RequestMapping(value="/addConsumerRedirect",method=RequestMethod.GET)
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

			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

			Customer customer = userService.getCustomerDetailsByUser(loggedUser.getUserId());

			JsonObject jObject = new JsonObject();
			jObject.addProperty("consumerMeterId", consumerMeterObj.getConsumerMeterId());
			jObject.addProperty("registerId", consumerMeterObj.getRegisterId());

			if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")) {
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
	}*/


	/*@RequestMapping(value="/searchConsumer",method=RequestMethod.POST)
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
	}*/


	@RequestMapping(value="/consumerProfileInit",method=RequestMethod.POST)
	public ResponseEntity<String> consumerProfileInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());
		List<JsonObject> tariffNames = new ArrayList<JsonObject>();
		List<Object> responseList = new ArrayList<Object>();

		if(consumer != null){

			User user = userService.getUserDetailsByUserID(consumer.getUser().getUserId());
			ContactDetails contactDetails = userService.getDetailsByUserID(consumer.getUser().getUserId());
			Customer customer = userService.getCustomerDetailsByCustomerId(consumer.getCustomer().getCustomerId());
			TariffPlan tariff = userService.getTariffPlanById(consumer.getTariffPlan().getTariffPlanId());

			JsonObject jObject = new JsonObject();
			jObject.addProperty("consumerId", consumer.getConsumerId());
			jObject.addProperty("consumerAccountNumber", consumer.getConsumerAccountNumber());
			jObject.addProperty("firstName", contactDetails.getFirstName());
			jObject.addProperty("lastName", contactDetails.getLastname());
			jObject.addProperty("userName", user.getUserName());
			jObject.addProperty("cell_number1", contactDetails.getCell_number1());
			jObject.addProperty("cell_number2", contactDetails.getCell_number2());
			jObject.addProperty("cell_number3", contactDetails.getCell_number3());
			jObject.addProperty("email1", contactDetails.getEmail1());
			jObject.addProperty("email2", contactDetails.getEmail2());
			jObject.addProperty("email3", contactDetails.getEmail3());
			jObject.addProperty("address1", contactDetails.getAddress1());
			jObject.addProperty("address2", contactDetails.getAddress2());
			jObject.addProperty("address3", contactDetails.getAddress3());
			jObject.addProperty("streetName", contactDetails.getStreetName());
			jObject.addProperty("zipcode", contactDetails.getZipcode());
			jObject.addProperty("tariffPlanId", tariff.getTariffPlanId());
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

			//request.getSession().setAttribute("responseJsonObject", responseList);

			return 	new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
	}


	@RequestMapping(value="/updateConsumerProfileDetails", method=RequestMethod.POST)
	public ResponseEntity<String> updateConsumerProfile(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="consumerId") String consumerId,
			@RequestParam(value="consumerAccountNumber") String consumerAccountNumber,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="address1") String address1,
			@RequestParam(required=false ,value="address2") String address2,
			@RequestParam(required=false ,value="address3") String address3,
			@RequestParam(value="streetName") String streetName,
			@RequestParam(value="zipcode") String zipcode,
			@RequestParam(value="cell_number1") String cell_number1,
			@RequestParam(required=false ,value="cell_number2") String cell_number2,
			@RequestParam(required=false ,value="cell_number3") String cell_number3,
			@RequestParam(value="email1") String email1,
			@RequestParam(required=false ,value="email2") String email2,
			@RequestParam(required=false ,value="email3") String email3,
			@RequestParam(value="activeStatus") String activeStatus,
			@RequestParam(value="tariffid") String tariffid){


		//Removing previous session attribute that will not not used in future
		Consumer consumer = userService.getConsumerByConsumerID(Integer.parseInt(consumerId));

		TariffPlan oldPlan = userService.getTariffPlanById(consumer.getTariffPlan().getTariffPlanId());
		if(activeStatus.equalsIgnoreCase("active")){
			consumer.setActive(true);
		} else{
			consumer.setActive(false);
		}

		ContactDetails contactDetails = consumer.getUser().getDetails();
		contactDetails.setFirstName(firstName);
		contactDetails.setLastname(lastName);
		contactDetails.setAddress1(address1);
		contactDetails.setAddress2(address2);
		contactDetails.setAddress3(address3);
		contactDetails.setStreetName(streetName);
		contactDetails.setEmail1(email1);
		contactDetails.setEmail2(email2);
		contactDetails.setEmail3(email3);
		contactDetails.setCell_number1(cell_number1);
		contactDetails.setCell_number2(cell_number2);
		contactDetails.setCell_number3(cell_number3);
		contactDetails.setZipcode(zipcode);
		contactDetails.setUpdatedBy(Integer.parseInt(consumerId));
		contactDetails.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

		int newTariffId = Integer.parseInt(tariffid);

		TariffPlan tp = userService.getTariffPlanById(newTariffId);

		consumer.setTariffPlan(tp);
		consumer.setUpdatedBy(Integer.parseInt(consumerId));
		consumer.setUpdatedTimeStamp(DateTimeConversionUtils.getDateInUTC(new Date()));

		userService.updateConsumerUser(consumer);

		User user = new User();
		user.setDetails(contactDetails);

		if(userService.updateConsumerUser(consumer)){

			userService.updateConsumerMeterUsingConsumerIdAndTariffId(consumer,oldPlan,tp);

			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
	}

	//***********************************************

	@RequestMapping(value="/initCustomerCodeData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initCustomerCodeData(HttpServletRequest request, HttpServletResponse response){

		List<JsonObject> customerCodeList = new ArrayList<JsonObject>();

		List<Object> objectList = new ArrayList<Object>();

		List<Customer> customerList = userService.getCustomerList();

		if(!customerList.isEmpty() && customerList != null){

			Iterator<Customer> customerIterator = customerList.iterator();

			while(customerIterator.hasNext()){

				Customer customer = customerIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("customerCode", customer.getCustomerCode());

				customerCodeList.add(json);
			}
		}		
		objectList.add(customerCodeList);

		return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
	}


	@RequestMapping(value="/validateConsumerRegistration",method=RequestMethod.POST,produces={"application/json"})
	public @ResponseBody ResponseEntity<String> validateConsumerRegistration(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="customerCode") String customerCode,
			@RequestParam(value="consumerAccountNumber") String consumerAccountNumber,
			@RequestParam(value="zipcode") String zipcode){

		try{
			Consumer con = userService.getConsumerByConsumerAccNo(consumerAccountNumber);

			if(con != null){
				User userObj = userService.getUserDetailsByUserID(con.getUser().getUserId());
				String email = null;

				Consumer consumer = userService.getValidConsumer(customerCode, consumerAccountNumber, zipcode);

				if(consumer != null){

					User user = userService.getUserDetailsByUserID(userObj.getUserId());

					email = user.getDetails().getEmail1();


					if(email == null){

						request.getSession().setAttribute("customerCode", customerCode);
						request.getSession().setAttribute("consumerAccountNumber", consumerAccountNumber);

						return new ResponseEntity<String>(new Gson().toJson("validconsumer"), HttpStatus.OK);

					}else{
						return new ResponseEntity<String>(new Gson().toJson("consumeralreadyregistered"), HttpStatus.OK);
					}

				}

				boolean isZipcode = userService.checkForZipcode(zipcode.trim(), consumerAccountNumber.trim());

				if(isZipcode){
					return new ResponseEntity<String>(new Gson().toJson("nosuchconsumerfound"), HttpStatus.OK);
				}else{
					return new ResponseEntity<String>(new Gson().toJson("nosuchzipcodefound"), HttpStatus.OK);
				}
			}	      

		}catch(Exception e){
			return new ResponseEntity<String>(new Gson().toJson("nosuchconsumerfound"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("nosuchconsumerfound"), HttpStatus.OK);
	}

	@RequestMapping(value="/initConsumerRegiPage",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> initConsumerRegiPage(HttpServletRequest request, HttpServletResponse response){

		List<JsonObject> objectList = new ArrayList<JsonObject>();

		String customerCode = (String) request.getSession().getAttribute("customerCode");
		String consumerAccountNumber = (String) request.getSession().getAttribute("consumerAccountNumber");

		Consumer consumer = userService.getConsumerByConsumerAccNo(consumerAccountNumber);
		ContactDetails contactDetails = userService.getDetailsByUserID(consumer.getUser().getUserId());

		JsonObject json = new JsonObject();
		json.addProperty("consumerAccountNumber", consumerAccountNumber);
		json.addProperty("customerCode", customerCode);
		json.addProperty("address1", contactDetails.getAddress1());
		json.addProperty("address2", contactDetails.getAddress2());
		json.addProperty("address3", contactDetails.getAddress3());
		json.addProperty("streetName", contactDetails.getStreetName());

		objectList.add(json);

		request.getSession().removeAttribute("customerCode");
		request.getSession().removeAttribute("consumerAccountNumber");

		return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
	}

	@RequestMapping(value="/registerNewConsumer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> registerNewConsumer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="customerCode") String customerCode,
			@RequestParam(value="consumerAccountNumber") String consumerAccountNumber,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="cell_number1") String cell_number1,
			@RequestParam(value="cell_number2", required=false) String cell_number2,
			@RequestParam(value="cell_number3", required=false) String cell_number3,
			@RequestParam(value="email1") String email1,
			@RequestParam(value="email2", required=false) String email2,
			@RequestParam(value="email3", required=false) String email3,
			@RequestParam(value="address1") String address1,
			@RequestParam(value="address2", required=false) String address2,
			@RequestParam(value="address3", required=false) String address3,
			@RequestParam(value="streetName") String streetName,
			@RequestParam(value="zipcode") String zipcode){


		Customer customer = userService.getCustomerByCustomerCode(customerCode);

		if(userName.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullusername"),HttpStatus.OK);
		}

		User user = userService.availableUserName(userName);
		if(user != null){
			// UseName already exist.Sending error ?
			return new ResponseEntity<String>(new Gson().toJson("usernameerror"),HttpStatus.OK);
		}

		Consumer consumer = userService.getConsumerByConsumerAccNo(consumerAccountNumber);
		//consumer.setConsumerId(12);
		consumer.setConsumerAccountNumber(consumerAccountNumber);
		consumer.setCustomer(customer);

		ContactDetails details = userService.getDetailsByUserID(consumer.getUser().getUserId());

		details.setAddress1(address1);
		details.setStreetName(streetName);
		details.setAddress2(address2);
		details.setAddress3(address3);

		details.setCell_number1(cell_number1);
		details.setCell_number2(cell_number2);
		details.setCell_number3(cell_number3);

		details.setEmail1(email1);
		details.setEmail2(email2);
		details.setEmail3(email3);

		details.setFirstName(firstName);
		details.setLastname(lastName);
		details.setZipcode(zipcode);

		com.kenure.entity.Role role = userService.getRoleById(3);

		request.getSession().removeAttribute("customerCode");
		request.getSession().removeAttribute("consumerAccountNumber");

		User newUser = userService.getUserDetailsByUserID(consumer.getUser().getUserId());
		newUser.setActiveStatus(Boolean.TRUE);
		newUser.setFirstTimeLogin(Boolean.TRUE);

		String generatedPassword = RandomNumberGenerator.generatePswd().toString();

		newUser.setPassword(MD5Encoder.MD5Encryptor(generatedPassword));
		newUser.setRole(role);

		newUser.setUserName(userName);
		newUser.setDetails(details);

		boolean consumerStatus = true ;

		consumer.setUser(newUser);
		consumer.setCreatedTimeStamp(new Date());
		consumer.setActive(consumerStatus);
		consumer.setCreatedBy(customer.getCustomerId());

		//userService.updateUser(customer, details);

		request.getSession().setAttribute("registerCompleted", true);

		userService.updateConsumerUser(consumer);

		String subject = "New Registration";
		try {
			KDLEmailModel model = new KDLEmailModel();
			model.setSubject(subject);
			model.setName(firstName+" "+lastName);
			model.setUserName(userName);
			model.setPassword(generatedPassword);
			model.setResourceName("Test");
			model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());

			Map<String, KDLEmailModel> map = new HashMap<String,KDLEmailModel>();
			map.put("emailConst", model);

			mailSender.htmlEmailSender(email1, subject, map,"newAccount",null);
			return new ResponseEntity<String>(new Gson().toJson("added"),HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}


	/*@RequestMapping(value="/billingData",method=RequestMethod.POST)
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
	}*/
	/*
	 * This method checks whether given input directory path and/or file exists or not, if not
	 * then it creates directory path and/or file
	 
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
	}*/

	// Dhaval code start

	@RequestMapping(value="/initConsumerDashboardData",method=RequestMethod.POST)
	public ResponseEntity<String> initConsumerDashboardData(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());

		List<Object> objectList = new ArrayList<Object>();
		List<JsonObject> jsonObjects = new ArrayList<JsonObject>();

		List<ConsumerMeter> consumerMeters = userService.getConsumerMeterListByConsumerId(consumer.getConsumerId());

		if(consumerMeters != null && consumerMeters.size() > 1){

			Iterator<ConsumerMeter> meterIterator = consumerMeters.iterator();
			while(meterIterator.hasNext()){

				ConsumerMeter consumerMeter = meterIterator.next();

				JsonObject  json = new JsonObject();
				json.addProperty("consumerMeterId", consumerMeter.getConsumerMeterId());
				json.addProperty("registerId", consumerMeter.getRegisterId());
				jsonObjects.add(json);
			}
			objectList.add(jsonObjects);
		}else if(consumerMeters != null && consumerMeters.size() == 1){

			String registerId = consumerMeters.get(0).getRegisterId();
			Integer consumerMeterId = consumerMeters.get(0).getConsumerMeterId();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = DateTimeConversionUtils.getPreviousDateBasedOnCurrentDateFromMidNight();
			Date endDate = new Date();

			List<Object> resultList = userService.getConsumerDashboardData(sdf.format(startDate),sdf.format(endDate),consumerMeterId,registerId);

			Integer totalConsumption = (Integer) ((Object[]) resultList.get(0))[1];
			Integer estimatedBill = (Integer) ((Object[]) resultList.get(1))[1];
			Integer lastBillData = (Integer) ((Object[]) resultList.get(2))[1];
			Integer readingDiff = (Integer) ((Object[]) resultList.get(3))[1];

			Date billingStartDate = consumerMeters.get(0).getBillingStartDate();
			Date billingEndDate = consumerMeters.get(0).getLastBillingDate();
			Date currentDate = new Date();

			List<Object> currentData = null;
			List<Object> futureData = null;
			if(billingStartDate != null){
				long diff = currentDate.getTime() - billingStartDate.getTime();
				long diffDays = diff / (24 * 60 * 60 * 1000);

				Integer lastUsageDiff = (int) ((consumerMeters.get(0).getBillingFrequencyInDays() * readingDiff) / diffDays);

				currentData = userService.getTariffTransactionDataByTariffId(consumerMeters.get(0).getTariffPlan().getTariffPlanId(),readingDiff);
				futureData = userService.getTariffTransactionDataByTariffId(consumerMeters.get(0).getTariffPlan().getTariffPlanId(),lastUsageDiff);
			}
			JsonObject json = new JsonObject();

			if(totalConsumption != null){
				json.addProperty("totalConsumption", totalConsumption);
			}else{
				json.addProperty("totalConsumption", "NA");
			}

			if(estimatedBill != null){
				json.addProperty("estimatedBill", estimatedBill);
			}else{
				json.addProperty("estimatedBill", "NA");
			}

			if(lastBillData != null){
				json.addProperty("lastBillData", lastBillData);
			}else{
				json.addProperty("lastBillData", "NA");
			}

			if(currentData != null){
				json.addProperty("currentRate", (String) ((Object[]) currentData.get(0))[1]);
			}else{
				json.addProperty("currentRate", "NA");
			}

			if(futureData != null){
				json.addProperty("futureRate", (String) ((Object[]) futureData.get(0))[1]);
			}else{
				json.addProperty("futureRate", "NA");
			}

			Date nextBillingDate = null;
			if(billingEndDate != null){
				long daysRemainingDiff = billingEndDate.getTime() - currentDate.getTime();
				long remainingDays = daysRemainingDiff / (24 * 60 * 60 * 1000);

				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE,(int)remainingDays);

				nextBillingDate = c.getTime();
			}
			if(nextBillingDate != null){
				SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
				json.addProperty("nextBillingDate", sdf1.format(nextBillingDate));
			}else{
				json.addProperty("nextBillingDate", "NA");
			}
			/*long remainingDays = -1; 
			if(billingEndDate != null){
				long daysRemainingDiff = billingEndDate.getTime() - currentDate.getTime();
				remainingDays = daysRemainingDiff / (24 * 60 * 60 * 1000);
			}
			if(remainingDays > -1){
				json.addProperty("remainingDays", remainingDays);
			}else{
				json.addProperty("remainingDays", "NA");
			}*/

			objectList.add(json);


		}

		List<String> notificationList= userService.getConsumerAlertNotifications(consumer.getConsumerId());
		objectList.add(notificationList);
		return new ResponseEntity<String>(new Gson().toJson(objectList),HttpStatus.OK);
	}

	@RequestMapping(value="/showDashboardData",method=RequestMethod.POST)
	public ResponseEntity<String> showDashboardData(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="registerId") String registerId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		//Consumer consumer = userService.getConsumerDetailsByUserId(loggedUser.getUserId());

		ConsumerMeter meter = userService.getConsumerMetetByRegisterId(registerId, 0);

		List<Object> objectList = new ArrayList<Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = DateTimeConversionUtils.getPreviousDateBasedOnCurrentDateFromMidNight();
		Date endDate = new Date();

		List<Object> resultList = userService.getConsumerDashboardData(sdf.format(startDate),sdf.format(endDate),meter.getConsumerMeterId(),registerId);

		Integer totalConsumption = (Integer) ((Object[]) resultList.get(0))[1];
		Integer estimatedBill = (Integer) ((Object[]) resultList.get(1))[1];
		Integer lastBillData = (Integer) ((Object[]) resultList.get(2))[1];
		Integer readingDiff = (Integer) ((Object[]) resultList.get(3))[1];

		Date billingStartDate = meter.getBillingStartDate();
		Date billingEndDate = meter.getLastBillingDate();
		Date currentDate = new Date();

		List<Object> currentData = null;
		List<Object> futureData = null;

		Double currentRate = null, futureRate = null;

		if(billingStartDate != null){
			long diff = currentDate.getTime() - billingStartDate.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			Integer lastUsageDiff = (int) ((meter.getBillingFrequencyInDays() * readingDiff) / diffDays);

			currentData = userService.getTariffTransactionDataByTariffId(meter.getTariffPlan().getTariffPlanId(),readingDiff);
			futureData = userService.getTariffTransactionDataByTariffId(meter.getTariffPlan().getTariffPlanId(),lastUsageDiff);

			Iterator<Object> itr = currentData.iterator();
			while(itr.hasNext()){
				Object obj = (Object) itr.next();
				String currentRateStr = String.valueOf(obj);
				currentRate = Double.parseDouble(currentRateStr);
			}

			Iterator<Object> itr1 = futureData.iterator();
			while(itr1.hasNext()){
				Object obj = (Object) itr1.next();
				String futureRateStr = String.valueOf(obj);
				futureRate = Double.parseDouble(futureRateStr);
			}

		}

		JsonObject json = new JsonObject();

		if(totalConsumption != null){
			json.addProperty("totalConsumption", totalConsumption);
		}else{
			json.addProperty("totalConsumption", "NA");
		}

		if(estimatedBill != null){
			json.addProperty("estimatedBill", estimatedBill);
		}else{
			json.addProperty("estimatedBill", "NA");
		}

		if(lastBillData != null){
			json.addProperty("lastBillData", lastBillData);
		}else{
			json.addProperty("lastBillData", "NA");
		}

		if(currentRate != null){
			json.addProperty("currentRate", currentRate);
		}else{
			json.addProperty("currentRate", "NA");
		}

		if(futureRate != null){
			json.addProperty("futureRate", futureRate);
		}else{
			json.addProperty("futureRate", "NA");
		}

		Date nextBillingDate = null;
		if(billingEndDate != null){
			long daysRemainingDiff = billingEndDate.getTime() - currentDate.getTime();
			long remainingDays = daysRemainingDiff / (24 * 60 * 60 * 1000);

			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE,(int)remainingDays);

			nextBillingDate = c.getTime();
		}
		if(nextBillingDate != null){
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
			json.addProperty("nextBillingDate", sdf1.format(nextBillingDate));
		}else{
			json.addProperty("nextBillingDate", "NA");
		}

		objectList.add(json);

		return new ResponseEntity<String>(new Gson().toJson(objectList),HttpStatus.OK);
	}

	// Dhaval code end


}