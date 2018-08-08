package com.kenure.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Country;
import com.kenure.entity.Currency;
import com.kenure.entity.Customer;
import com.kenure.entity.DataPlan;
import com.kenure.entity.NormalCustomer;
import com.kenure.entity.Role;
import com.kenure.entity.User;
import com.kenure.model.KDLEmailModel;
import com.kenure.service.IUserService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;
import com.kenure.utils.MD5Encoder;
import com.kenure.utils.MailSender;
import com.kenure.utils.RandomNumberGenerator;

@Controller
public class UserController {

	@Autowired
	IUserService userService;
	private static final Logger logger = LoggerUtils.getInstance(CustomerController.class);
	
	@Autowired
	private MailSender mailSender;

	@RequestMapping(value="/insertUser",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addUser(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="customerCode") String customerCode,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="dataplanActivedate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataplanActivedate,
			@RequestParam(value="dataPlanDuration") String dataPlanDuration,
			@RequestParam(value="portalplanActiveDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date portalplanActiveDate,
			@RequestParam(value="portalplanExpirydate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date portalplanExpirydate,
			@RequestParam(value="dataplan") int dataPlanId,
			@RequestParam(value="phone") String phoneNumber,
			@RequestParam(value="email") String email,
			@RequestParam(value="address") String address,
			@RequestParam(value="zipcode") String zip,
			@RequestParam(value="status") String status,
			@RequestParam(value="countryId") String countryId,
			@RequestParam(value="currencyId") String currencyId,
			@RequestParam(value="selectedTimeZone") String selectedTimeZone
			){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		boolean isCustomerCodeValid = userService.checkForCustomerCode(customerCode);

		if(!isCustomerCodeValid){
			return new ResponseEntity<String>(new Gson().toJson("customercodeerror"),HttpStatus.OK);
		}

		// Get Currency By its Id
		com.kenure.entity.Currency cur = userService.getCurrencyById(currencyId);

		// Get country by its id
		Country country = userService.getCountryById(countryId);

		User user = userService.availableUserName(userName);
		if(user != null){
			// UseName already exist.Sending error ?
			return new ResponseEntity<String>(new Gson().toJson("usernameerror"),HttpStatus.OK);
		}

		ContactDetails details = new ContactDetails();
		details.setAddress1(address);
		details.setCell_number1(phoneNumber);
		details.setEmail1(email);
		details.setFirstName(firstName);
		details.setLastname(lastName);
		details.setZipcode(zip);
		details.setCreated_by(loggedUser.getUserId());

		Customer customer = new Customer();

		customer.setCustomerCode(customerCode);
		customer.setCustomerName(firstName);

		customer.setDataPlanActivatedDate(dataplanActivedate);
		
		if(dataPlanDuration != null && dataPlanDuration.length() > 0){
			String regex = "[0-9]+";
			boolean isNUmber = dataPlanDuration.matches(regex);
		
			if(isNUmber){
				Calendar cal = Calendar.getInstance();
				cal.setTime(dataplanActivedate);
				cal.add(Calendar.YEAR, Integer.parseInt(dataPlanDuration)); 
				Date dataplanExpirydate = cal.getTime();
				customer.setDataPlanExpiryDate(dataplanExpirydate);
			}else{
				return new ResponseEntity<>(new Gson().toJson("unvaliddataplanduration"),HttpStatus.OK);
			}
		}else{
			return new ResponseEntity<>(new Gson().toJson("unvaliddataplanduration"),HttpStatus.OK);
		}
		
		customer.setPortalPlanStartDate(portalplanActiveDate);
		customer.setPortalPlanExpiryDate(portalplanExpirydate);

		customer.setStatus(status);
		customer.setCreatedBy(loggedUser.getUserId()); // Setting current logged user's userid
		customer.setTimeZone(selectedTimeZone);	

		customer.setCurrency(cur);
		customer.setCountry(country);

		DataPlan dataPlan = userService.getDataPlanById(dataPlanId);
		customer.setDataPlan(dataPlan);

		Role role = new Role();
		role.setRoleName("customer");
		role.setRoleId(2);

		User newUser = new User();
		newUser.setActiveStatus(Boolean.TRUE);
		newUser.setFirstTimeLogin(Boolean.TRUE);

		String generatedPassword = RandomNumberGenerator.generatePswd().toString();

		customer.setCreatedBy(loggedUser.getUserId()); // Setting current logged user's userid
		/*	newUser.setPassword(MD5Encoder.MD5Encryptor(generatedPassword));
		newUser.setRole(role);*/

		newUser.setPassword(MD5Encoder.MD5Encryptor(generatedPassword));
		newUser.setRole(role);

		newUser.setUserName(userName);
		newUser.setDetails(details);
		customer.setUser(newUser);

		userService.insertNewUser(customer);

		String subject = "New Account Created.";
		try {
			
			KDLEmailModel model = new KDLEmailModel();
			model.setSubject(subject);
			model.setName(firstName+" "+lastName);
			model.setUserName(userName);
			model.setPassword(generatedPassword);
			model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
			
			Map map = new HashMap<String,KDLEmailModel>();
			map.put("emailConst", model);
			
			mailSender.htmlEmailSender(email, subject, map,"newAccount",null);
			
			return new ResponseEntity<String>(new Gson().toJson("added"),HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}

	@RequestMapping(value="/getEditUserEntity",method=RequestMethod.POST)
	public ResponseEntity<String> getEditUserEntity(HttpServletRequest request,HttpServletResponse response){

		JsonObject jObject = (JsonObject) request.getSession().getAttribute("responseJsonObject");
		List<Object> responseObjList = new ArrayList<Object>();
		if(jObject != null ){
			request.getSession().removeAttribute("responseJsonObject");
			jObject.addProperty("timeZoneList", new Gson().toJson(KenureUtilityContext.timeZone));
			responseObjList.add(jObject);
			
			List<DataPlan> dataPlanList = userService.getDataPlanList();
			List<JsonObject> dataPlanListRequiredData = new ArrayList<JsonObject>();
			if(!dataPlanList.isEmpty() && dataPlanList != null){
				Iterator<DataPlan> dataPlanIterator = dataPlanList.iterator();
				while(dataPlanIterator.hasNext()){
					DataPlan dataPlan = dataPlanIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("dataPlanId", dataPlan.getDataPlanId());
					json.addProperty("mbPerMonth", dataPlan.getMbPerMonth());
					//json.addProperty("createdDate", dataPlan.getCreatedDate().toString());
					dataPlanListRequiredData.add(json);
				}
			}		
			responseObjList.add(dataPlanListRequiredData);
			return 	new ResponseEntity<>(new Gson().toJson(responseObjList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
	}

	@RequestMapping(value="/updateCustomer",method=RequestMethod.POST)
	public ResponseEntity<String> updateCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="customerCode") String customerCode,
			@RequestParam(value="lastname") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="cell_number1") String cell_number1,
			@RequestParam(value="email1") String email1,
			@RequestParam(value="address1") String address1,
			@RequestParam(value="zipcode") String zipcode,
			@RequestParam(value="userId") String userId,
			@RequestParam(value="status") String status,
			@RequestParam(value="editDataPlanActiveDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date editDataPlanActiveDate,
			@RequestParam(value="dataPlanDuration",required=false) String dataPlanDuration,
			@RequestParam(value="editPortalActiveDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date editPortalActiveDate,
			@RequestParam(value="editPortalExpiryDate",required=false) @DateTimeFormat(pattern="dd/MM/yyyy") Date editPortalExpiryDate,
			@RequestParam(value="selectedCurrency",required=false) String selectedCurrency,
			@RequestParam(value="selectedCountry",required=false) String selectedCountry,
			@RequestParam(value="selectedTimeZone") String selectedTimeZone,
			@RequestParam(value="customerId") String customerId,
			@RequestParam(value="currentDataPlanId") Integer newDataPlanId){

		//Removing previous session attribute that will not not used in future
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getCustomerDetailsByUser(((Customer) userService.getCustomerDetailsByCustomerId(Integer.parseInt(customerId))).getUser().getUserId());
		boolean canDataPlanChange;
		// Setting all 4 dates

		if(!status.isEmpty() || status != null){
			if(status.equalsIgnoreCase("InActive") || status.equalsIgnoreCase("Inactive-DataPlan") || status.equalsIgnoreCase("Inactive-portalplan")){
				customer.setActiveStatus(false);
				customer.setDeletedBy(loggedUser.getUserId());
				customer.setDeletedTS(DateTimeConversionUtils.getDateInUTC(new Date()));
				customer.getUser().setActiveStatus(false);
				customer.getUser().setDeletedBy(loggedUser.getUserId());
				customer.getUser().setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
				
				List<NormalCustomer> nCustomerList = new ArrayList<NormalCustomer>(customer.getNormalCustomer());
				nCustomerList.stream().forEach(n -> {
					if(n.getUser() != null ){
						
						User user = n.getUser();
						user.setActiveStatus(false);
						user.setDeletedBy(loggedUser.getUserId());
						user.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
						user.setUpdatedBy(loggedUser.getUserId());
						user.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
						userService.updateUserEntity(user);
					}
				});
				
			}
			if(status.equalsIgnoreCase("Active")){
				customer.setActiveStatus(true);
				customer.setDeletedBy(null);
				customer.setDeletedTS(null);
				customer.getUser().setActiveStatus(true);
				customer.getUser().setDeletedBy(null);
				customer.getUser().setDeletedTs(null);
				DataPlan newDataPlan = userService.getDataPlanById(newDataPlanId);
				if(newDataPlan!=null){
					if(newDataPlan.getMbPerMonth()<customer.getDataPlan().getMbPerMonth()){
						canDataPlanChange = userService.updateDCsBySiteId(null, null, null,customer, null,newDataPlan);
						if(canDataPlanChange)
							customer.setDataPlan(newDataPlan);
						else
							return new ResponseEntity<>(new Gson().toJson("cannotSetTheSelectedDataplan"),HttpStatus.OK);
					}
					else{
						customer.setDataPlan(newDataPlan);
					}
				}
			}
			customer.setStatus(status);
		}

		if(customerCode != null )
			customer.setCustomerCode(customerCode);

		if(firstName !=null ){
			customer.getUser().getDetails().setFirstName(firstName);
		}

		if(lastName !=null )
			customer.getUser().getDetails().setLastname(lastName);
		
		if(firstName != null && lastName != null)
			customer.setCustomerName(firstName+" "+lastName);
		
		if(userName != null )
			customer.getUser().setUserName(userName);

		if(cell_number1 != null)
			customer.getUser().getDetails().setCell_number1(cell_number1);

		if(email1 != null )	
			customer.getUser().getDetails().setEmail1(email1);

		if(address1 != null )
			customer.getUser().getDetails().setAddress1(address1);

		if(zipcode != null )
			customer.getUser().getDetails().setZipcode(zipcode);	

		if(editPortalExpiryDate != null)
			customer.setPortalPlanExpiryDate(editPortalExpiryDate);

		if(editPortalActiveDate != null)
			customer.setPortalPlanStartDate(editPortalActiveDate);

		if(editDataPlanActiveDate!=null){
			
			if(dataPlanDuration != null && dataPlanDuration.length() > 0){
				String regex = "[0-9]+";
				boolean isNUmber = dataPlanDuration.matches(regex);
			
				if(isNUmber){
					customer.setDataPlanActivatedDate(editDataPlanActiveDate);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(editDataPlanActiveDate);
					cal.add(Calendar.YEAR, Integer.parseInt(dataPlanDuration)); 
					Date dataplanExpirydate = cal.getTime();
					customer.setDataPlanExpiryDate(dataplanExpirydate);
				}else{
					return new ResponseEntity<>(new Gson().toJson("unvaliddataplanduration"),HttpStatus.OK);
				}
			}else{
				return new ResponseEntity<>(new Gson().toJson("unvaliddataplanduration"),HttpStatus.OK);
			}
			
		}	

		if(selectedCurrency != null && !selectedCurrency.trim().isEmpty()){
			Currency cur = userService.getCurrencyById(selectedCurrency);
			customer.setCurrency(cur);
		}

		if(selectedCountry != null && !selectedCountry.trim().isEmpty()){
			Country country = userService.getCountryById(selectedCountry);
			customer.setCountry(country);
		}
		if(selectedTimeZone != null && !selectedTimeZone.trim().isEmpty()){
			customer.setTimeZone(selectedTimeZone);
		}
		// set updated by and updated time
		customer.setUpdatedBy(loggedUser.getUserId());
		customer.setUpdatedTS(new Date());

		if(userService.updateUser(customer)){
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
	}

	@RequestMapping(value="/adminProfileInit",method=RequestMethod.POST)
	public ResponseEntity<String> adminProfileInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		List<Object> responseList = new ArrayList<Object>();

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			ContactDetails contactDetails = userService.getDetailsByUserID(loggedUser.getUserId());

			JsonObject jObject = new JsonObject();
			jObject.addProperty("firstName", contactDetails.getFirstName());
			jObject.addProperty("lastName", contactDetails.getLastname());
			jObject.addProperty("userName", loggedUser.getUserName());
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

			responseList.add(jObject);

			return 	new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
	}


	@RequestMapping(value="/updateAdminProfile", method=RequestMethod.POST)
	public ResponseEntity<String> updateAdminProfile(HttpServletRequest request,HttpServletResponse response,
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
			@RequestParam(required=false ,value="email3") String email3){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser != null){
			ContactDetails contactDetails = userService.getDetailsByUserID(loggedUser.getUserId());
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
			contactDetails.setUpdatedBy(loggedUser.getUserId());
			contactDetails.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));

			userService.updateAdminDetails(contactDetails);

			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
	}


	@RequestMapping(value="/checkUserName",method=RequestMethod.POST)
	public ResponseEntity<String> checkUserName(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="userName",required=true) String userName){

		logger.info(" checking Username "+userName);
		User user = userService.availableUserName(userName);
		if(user != null){
			// UseName already exist.Sending error ?
			return new ResponseEntity<String>(new Gson().toJson("usernameerror"),HttpStatus.OK);
		}

		return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);
	}

}