package com.kenure.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.constants.ApplicationConstants;
import com.kenure.entity.Consumer;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Country;
import com.kenure.entity.Currency;
import com.kenure.entity.Customer;
import com.kenure.entity.DataPlan;
import com.kenure.entity.User;
import com.kenure.model.KDLEmailModel;
import com.kenure.model.UserLoginCredentials;
import com.kenure.service.AppConstInitializeService;
import com.kenure.service.IUserService;
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
public class LoginController {

	@Autowired
	private IUserService userService;

	@Autowired
	private AppConstInitializeService appService;

	@Autowired
	private MailSender mailSender;
	/**
	 * Logger object
	 */
	private static final Logger logger = LoggerUtils.getInstance(LoginController.class);

	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request) {
		return new ModelAndView("container");
	}

	//For authenticating login loginCredentials 
	@RequestMapping(value="/authenticateUser",method=RequestMethod.POST,produces={"application/json"})
	public @ResponseBody ResponseEntity<String> authenticateUser(HttpServletRequest request,HttpServletResponse response,@RequestParam String userName,@RequestParam String password){

		if(userName.trim().isEmpty() || password.trim().isEmpty() || userName == null || password == null){
			logger.info("Server Side Validation Failed >> Sending again to Login Page");
			return new ResponseEntity<String>(new Gson().toJson("Validation failed"), HttpStatus.OK);
		}

		User currentUser = null;

		logger.info("Server side basic validation passed .. Checking for authentication ..");

		UserLoginCredentials loginCredentials = new UserLoginCredentials();
		loginCredentials.setUserName(userName);
		loginCredentials.setPassword(password);
		logger.info("Making a DataBase Call for checking username and password");

		if((currentUser = userService.authenticateUser(loginCredentials)) != null){
			logger.info("authentication success for user with role >> ",currentUser.getRole().getRoleName());

			HttpSession session = request.getSession();
			/*session.setAttribute(KenureUtilityContext.currentUser,currentUser);
			logger.info("current user in session {}",currentUser.getUserName());*/

			if(currentUser.isFirstTimeLogin()){
				session.setAttribute(KenureUtilityContext.currentUser,currentUser);
				logger.info("current user in session {}",currentUser.getUserName());
				logger.info("user is first time login......");
				return new ResponseEntity<String>(new Gson().toJson("isFirstTImeLoginTrue"), HttpStatus.OK);

			}
			if(currentUser.getRole().getRoleName().equalsIgnoreCase("admin")){
				session.setAttribute(KenureUtilityContext.currentUser,currentUser);
				logger.info("current user in session {}",currentUser.getUserName());
				return new ResponseEntity<String>(new Gson().toJson("loginAsCustomer"), HttpStatus.OK);
			}

			logger.info("user is not first time login...");

			if(currentUser != null && currentUser.getRole().getRoleName().equalsIgnoreCase("customer")){
				Customer customer = userService.getOnlyCustomerByUserId(currentUser.getUserId());
				if(customer.getStatus().equalsIgnoreCase("inactive-dataplan")){
					return new ResponseEntity<String>(new Gson().toJson("inactiveDataPlan"), HttpStatus.OK);
				}else if(customer.getStatus().equalsIgnoreCase("inactive-portalplan")){
					return new ResponseEntity<String>(new Gson().toJson("inactivePortalPlan"), HttpStatus.OK);
				}else if(customer.getStatus().equalsIgnoreCase("inactive")){
					return new ResponseEntity<String>(new Gson().toJson("inactiveuser"), HttpStatus.OK);
				}else{
					session.setAttribute(KenureUtilityContext.currentUser,currentUser);
					logger.info("current user in session {}",currentUser.getUserName());
					return new ResponseEntity<String>(new Gson().toJson("loginAsConsumer"), HttpStatus.OK);
				}
			}
			if(currentUser != null && currentUser.getRole().getRoleName().equalsIgnoreCase("normalcustomer")){
				/*Customer superCustomer = userService.getCustomerDetailsByCustomerId(currentUser.getNormalCustomer());
				session.setAttribute("superCustomer", superCustomer);*/
				session.setAttribute("isNormalCustomer",Boolean.TRUE);
				session.setAttribute(KenureUtilityContext.currentUser,currentUser.getNormalCustomer().getSuperCustomer().getUser()); // NormalCustomer ? Overwrite its session by its parent User
				session.setAttribute(KenureUtilityContext.normalCustomer, currentUser);
				return new ResponseEntity<String>(new Gson().toJson("loginAsCustomer"), HttpStatus.OK);
			}

			if(currentUser != null && currentUser.getRole().getRoleName().equalsIgnoreCase("consumer")){
				session.setAttribute(KenureUtilityContext.currentUser,currentUser);
				logger.info("current user in session {}",currentUser.getUserName());
				return new ResponseEntity<String>(new Gson().toJson("consumer"), HttpStatus.OK);

			}else if(currentUser != null && currentUser.getRole().getRoleName().equalsIgnoreCase("installer")){
				session.setAttribute(KenureUtilityContext.currentUser,currentUser);
				logger.info("current user in session {}",currentUser.getUserName());
				return new ResponseEntity<String>(new Gson().toJson("installer"), HttpStatus.OK);
			}else{
				session.setAttribute(KenureUtilityContext.currentUser,currentUser);
				logger.info("current user in session {}",currentUser.getUserName());
				return new ResponseEntity<String>(new Gson().toJson("isFirstTImeLoginFalse"), HttpStatus.OK);
			}

		}else{
			logger.info("Authentication failed .. ");
			return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.AUTHENTICATIONFAILED), HttpStatus.OK);
		}
	}

	/*@RequestMapping(value="/deleteUser",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteUser(HttpServletRequest request,HttpServletResponse response,@RequestParam("userId") String customerId){	
		if(customerId.matches("^-?\\d+$") && (customerId != null || !customerId.trim().isEmpty())){
			User loggedUser = (User) request.getSession().getAttribute("currentUser");
			int id = loggedUser.getUserId();
			try{
				Customer cus = userService.getCustomerDetailsByCustomerId(Integer.parseInt(customerId));
				boolean isUserDeleted = userService.deleteUser(Integer.parseInt(customerId),cus.getUser().getRole().getRoleId(),id);
				if(isUserDeleted){
					logger.info("user successfully deleted with id "+customerId);
					return new ResponseEntity<String>(new Gson().toJson("userdeletedsuccessfully"), HttpStatus.OK);
				}else{
					logger.info("no such user found with user id"+customerId);
					return new ResponseEntity<String>(new Gson().toJson("userismapped"), HttpStatus.OK);
				}
			}catch(Exception e){
				logger.info("Error while deleting Customer with CustomerId"+customerId);
				return new ResponseEntity<String>(new Gson().toJson("userismapped"), HttpStatus.OK);
			}

		}

		logger.warn("error while parsing query parameter with userId "+customerId);
		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
	}*/

	@RequestMapping(value="/listUser",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> listUser(HttpServletRequest request,HttpServletResponse response){
		List<User> userList = userService.getUserList();
		return new ResponseEntity<String>(new Gson().toJson(userList), HttpStatus.OK);
	}

	/*@RequestMapping(value="/userDashboardData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> adminDashBoardData(HttpServletRequest request, HttpServletResponse response){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		String userListGsonId = "";
		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){
			List<Customer> customerList = userService.getCustomerList();
			List<DataPlan> dataPlanList = userService.getDataPlanList();
			List<JsonObject> customerListRequiredData = new ArrayList<JsonObject>();
			List<JsonObject> dataPlanListRequiredData = new ArrayList<JsonObject>();
			List<Object> objectList = new ArrayList<Object>();
			if(!customerList.isEmpty() && customerList != null){
				SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
				int forIndex ;
				for(forIndex=0;forIndex<customerList.size();forIndex++){
					if(customerList.get(forIndex).getUser().getNormalCustomer() == null){
						JsonObject  jsObject = new JsonObject();
						jsObject.addProperty("customerId", customerList.get(forIndex).getCustomerId());
						jsObject.addProperty("status", customerList.get(forIndex).getStatus());
						jsObject.addProperty("companyName", customerList.get(forIndex).getUser().getDetails().getFirstName());
						jsObject.addProperty("contactName", customerList.get(forIndex).getUser().getDetails().getLastname());
						jsObject.addProperty("contractExpiry", sdf.format(customerList.get(forIndex).getPortalPlanExpiryDate()));
						jsObject.addProperty("activeDate", sdf.format(customerList.get(forIndex).getDataPlanActivatedDate()));
						jsObject.addProperty("expiryDate", sdf.format(customerList.get(forIndex).getDataPlanExpiryDate()));

						customerListRequiredData.add(jsObject);
					}
				}
			}

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

			// Setting response data
			objectList.add(loggedUser.getUserName());
			objectList.add(customerListRequiredData);
			objectList.add(dataPlanListRequiredData);


			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(new Gson().toJson(loggedUser));

			ContactDetails contactdetails = userService.getDetailsByUserID(loggedUser.getUserId());
			Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			userListGsonId = new Gson().toJson(contactdetails);
			String customerGson = new Gson().toJson(customer);

			objectList.add(userListGsonId);
			objectList.add(customerGson);

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("consumer")){
			List<Object> objectList = new ArrayList<Object>();
			objectList.add(new Gson().toJson(loggedUser));
			List<User> userList = userService.getUserList();
			if(!userList.isEmpty() && userList != null)
				userListGsonId = new Gson().toJson(userList);
			objectList.add(userListGsonId);
			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}


		return new ResponseEntity<String>(new Gson().toJson("userRoleNotMatch"), HttpStatus.OK);
	}*/



	@RequestMapping(value="/customerProfileInit",method=RequestMethod.POST)
	public ResponseEntity<String> customerProfileInit(HttpServletRequest request,HttpServletResponse response){

		JsonObject jObject = new JsonObject();
		List<Object> responseList = new ArrayList<Object>();
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;

		if(isNormalCustomer != null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute("normalCustomer");
		}else{
			loggedUser = (User) request.getSession().getAttribute("currentUser");
		}

		// Adding details first by user 
		if(loggedUser != null){
			jObject.addProperty("firstName", loggedUser.getDetails().getFirstName());
			jObject.addProperty("lastName", loggedUser.getDetails().getLastname());
			jObject.addProperty("userName", loggedUser.getUserName());
			jObject.addProperty("cell_number1", loggedUser.getDetails().getCell_number1());
			jObject.addProperty("cell_number2", loggedUser.getDetails().getCell_number2());
			jObject.addProperty("cell_number3", loggedUser.getDetails().getCell_number3());
			jObject.addProperty("email1", loggedUser.getDetails().getEmail1());
			jObject.addProperty("email2", loggedUser.getDetails().getEmail2());
			jObject.addProperty("email3", loggedUser.getDetails().getEmail3());
			jObject.addProperty("address1", loggedUser.getDetails().getAddress1());
			jObject.addProperty("address2", loggedUser.getDetails().getAddress2());
			jObject.addProperty("address3", loggedUser.getDetails().getAddress3());
			jObject.addProperty("streetName", loggedUser.getDetails().getStreetName());
			jObject.addProperty("zipcode", loggedUser.getDetails().getZipcode());
		}else{
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
		}
		// Is current customer is Super Customer than we have to show him his dataplan but we don't have to show it to normal customer !
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		if(customer != null){
			DataPlan dataPlan = userService.getDataPlanById(customer.getDataPlan().getDataPlanId());
			jObject.addProperty("mbPerMonth", dataPlan.getMbPerMonth());
			jObject.addProperty("dataPlanActivatedDate", customer.getDataPlanActivatedDate().toString().substring(0, 10));
			jObject.addProperty("dataPlanExpiryDate", customer.getDataPlanExpiryDate().toString().substring(0, 10));
		}else{
			jObject.addProperty("mbPerMonth", "-");
			jObject.addProperty("dataPlanActivatedDate", "-");
			jObject.addProperty("dataPlanExpiryDate", "-");
		}
		responseList.add(jObject);
		return new ResponseEntity<>(new Gson().toJson(jObject),HttpStatus.OK);
	}


	@RequestMapping(value="/updateCustomerProfile", method=RequestMethod.POST)
	public ResponseEntity<String> updateCustomerProfile(HttpServletRequest request,HttpServletResponse response,
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



		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;
		if(isNormalCustomer != null && isNormalCustomer){
			loggedUser = (User) request.getSession().getAttribute("normalCustomer");
		}else{
			loggedUser = (User) request.getSession().getAttribute("currentUser");
		}

		if(loggedUser != null){
			loggedUser.getDetails().setFirstName(firstName);
			loggedUser.getDetails().setLastname(lastName);
			loggedUser.getDetails().setAddress1(address1);
			loggedUser.getDetails().setAddress2(address2);
			loggedUser.getDetails().setAddress3(address3);
			loggedUser.getDetails().setStreetName(streetName);
			loggedUser.getDetails().setEmail1(email1);
			loggedUser.getDetails().setEmail2(email2);
			loggedUser.getDetails().setEmail3(email3);
			loggedUser.getDetails().setCell_number1(cell_number1);
			loggedUser.getDetails().setCell_number2(cell_number2);
			loggedUser.getDetails().setCell_number3(cell_number3);
			loggedUser.getDetails().setZipcode(zipcode);
			loggedUser.getDetails().setUpdatedBy(loggedUser.getUserId());
			loggedUser.getDetails().setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			
			userService.updateUserEntity(loggedUser);
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.UPDATEDSUCCESSFULLY),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.ERRORWHILEUPDATING),HttpStatus.OK);
	}

	//getting DataPlan data
	@RequestMapping(value="/dataPlanData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> dataPlanData(HttpServletRequest request, HttpServletResponse response){

		List<JsonObject> dataPlanListRequiredData = new ArrayList<JsonObject>();

		List<Object> objectList = new ArrayList<Object>();

		//objectList.add(new Gson().toJson(loggedUser));
		List<DataPlan> dataPlanList = userService.getDataPlanList();

		//lets add currency and country to add customer page
		List<Currency> currencyList = userService.getCurrencyList();
		List<Country> countryList = userService.getCountryList();
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
		objectList.add(dataPlanListRequiredData);
		objectList.add(currencyList);
		objectList.add(countryList);
		objectList.add(KenureUtilityContext.timeZone);
		return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
	}


	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logoutUser(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		session.removeAttribute("currentUser");
		session.invalidate();
		logger.info("User logged out");
		return "redirect:loginForm";
	}


	//sending password mail to customer
	@RequestMapping(value = "/sendMail",method=RequestMethod.POST,produces={"application/json"})
	public ResponseEntity<String> sendMail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("email") String emailAddress, @RequestParam("userName") String userName) throws Exception {

		if(userName.trim().isEmpty() || emailAddress.trim().isEmpty() || userName == null || emailAddress == null){

			logger.info("Server Side Validation Failed >> Sending again to Login Page");
			return new ResponseEntity<String>(new Gson().toJson("validationfailed"), HttpStatus.OK);
		}

		User foundUser = userService.availableUserName(userName);

		if(foundUser != null){

			ContactDetails userDetails = userService.getDetailsByUserID(foundUser.getUserId());

			if(emailAddress.equalsIgnoreCase(userDetails.getEmail1()) 
					|| emailAddress.equalsIgnoreCase(userDetails.getEmail2()) 
					|| emailAddress.equalsIgnoreCase(userDetails.getEmail3())){

				char[] password = RandomNumberGenerator.generatePswd();

				String subject = "Password Changed !";

				KDLEmailModel model = new KDLEmailModel();
				model.setSubject(subject);
				model.setName(userDetails.getFirstName()+" "+userDetails.getLastname());
				model.setUserName(userName);
				model.setPassword(password.toString());
				model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());

				Map map = new HashMap<String,KDLEmailModel>();
				map.put("emailConst", model);

				mailSender.htmlEmailSender(emailAddress, subject, map, "forgotPassword", null);

				logger.info("mail successfully send to your email...."+emailAddress);
				foundUser.setFirstTimeLogin(Boolean.TRUE);
				userService.updatePassword(foundUser,password.toString());
				request.getSession().setAttribute("mailSentNotify","Your New Password Sent Successfully. Please Login with New Password.");
				return new ResponseEntity<String>(new Gson().toJson("mailSend"), HttpStatus.OK);
			}
			else{
				logger.info("your email address is not mapped with your username..");
				return new ResponseEntity<String>(new Gson().toJson("emailNotAvailable"), HttpStatus.OK);
			}

		}else{
			logger.info("user not available "+userName);
			return new ResponseEntity<String>(new Gson().toJson("userNotAvailable"), HttpStatus.OK);
		}

	}

	//for resetting password
	@RequestMapping(value = "/resetpassword",method=RequestMethod.POST,produces={"application/json"})
	public ResponseEntity<String> resetPassword(@RequestParam("newPassword") String newPassword, 
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("confirmnewpwd") String confirmnewpwd,
			HttpSession session){
		if(newPassword.equals(confirmnewpwd)){
			logger.info("password matched with confirmed password");
			User user = (User) session.getAttribute(KenureUtilityContext.currentUser);
			//	User foundUser = userService.availableUserName(user.getUserName());
			String encryptedPwd = MD5Encoder.MD5Encryptor(oldPassword);

			if(encryptedPwd.equals(user.getPassword())){
				user.setFirstTimeLogin(Boolean.FALSE);

				userService.updatePassword(user, newPassword);
				if(user.getRole().getRoleName().equalsIgnoreCase("admin")){
					logger.info("password changed successfully.....");
					return new ResponseEntity<String>(new Gson().toJson("adminpwdchangedSuccessfully"), HttpStatus.OK);
				}
				if(user.getRole().getRoleName().equalsIgnoreCase("customer")){
					logger.info("password changed successfully.....");
					return new ResponseEntity<String>(new Gson().toJson("customerpwdchangedSuccessfully"), HttpStatus.OK);
				}
				if(user.getRole().getRoleName().equalsIgnoreCase("consumer")){
					logger.info("password changed successfully.....");
					return new ResponseEntity<String>(new Gson().toJson("consumerpwdchangedSuccessfully"), HttpStatus.OK);
				}
				if(user.getRole().getRoleName().equalsIgnoreCase("installer")){
					logger.info("password changed successfully.....");
					return new ResponseEntity<String>(new Gson().toJson("installerpwdchangedSuccessfully"), HttpStatus.OK);
				}
				if(user.getRole().getRoleName().equalsIgnoreCase("normalcustomer")){
					logger.info("password changed successfully.....");
					return new ResponseEntity<String>(new Gson().toJson("customerpwdchangedSuccessfully"), HttpStatus.OK);
				}

				return new ResponseEntity<String>(new Gson().toJson("changedSuccessfully"), HttpStatus.OK);
			}
			else{
				logger.info("your password is incorrect.....");

				return new ResponseEntity<String>(new Gson().toJson("passwordIsIncorrect"), HttpStatus.OK);
			}
		}else{
			logger.info("your password not match with confirm password");
			return new ResponseEntity<String>(new Gson().toJson("passwordNotMatch"), HttpStatus.OK);
		}
	}

	//get customer list
	@RequestMapping(value="/getCustomerListData",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> switchUser(HttpServletRequest request, HttpServletResponse response) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){


			List<Customer> customerList = userService.getActiveCustomerList();

			List<JsonObject> customerJsonList = new ArrayList();
			if(customerList != null){
				Iterator<Customer> customerIterator = customerList.iterator();

				while(customerIterator.hasNext()){


					Customer customer = customerIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("customerName", customer.getCustomerName());
					json.addProperty("customerId", customer.getCustomerId());

					customerJsonList.add(json);
				}	
			}

			return new ResponseEntity<Object>(new Gson().toJson(customerJsonList), HttpStatus.OK);

		}

		return null;

	}

	//setting session when admin login as customer
	@RequestMapping(value="/loginAsCustomer",method=RequestMethod.GET)
	public ResponseEntity<String> loginAsCustomer(HttpServletRequest request, HttpServletResponse response,@RequestParam String userId){
		HttpSession session= request.getSession();

		//User user = userService.getUserDetailsByUserID(userId);
		Customer customer = userService.getCustomerDetailsByCustomerName(userId);

		session.setAttribute(KenureUtilityContext.currentUser,customer.getUser());

		logger.info("current user in session" +customer.getUser().getUserName());

		return new ResponseEntity<String>(new Gson().toJson("loginAsCustomer"), HttpStatus.OK);
	}

	//setting session when Customer login as Consumer
	@RequestMapping(value="/loginAsConsumer",method=RequestMethod.GET)
	public ResponseEntity<String> loginAsConsumer(HttpServletRequest request, HttpServletResponse response,@RequestParam String userId){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		List<Consumer> consumerList = userService.searchConsumerByAccNum(userId, customer.getCustomerId());
		HttpSession session= request.getSession();
		session.setAttribute(KenureUtilityContext.currentUser,consumerList.get(0).getUser());

		logger.info("current user in session" +customer.getUser().getUserName());

		return new ResponseEntity<String>(new Gson().toJson("loginAsConsumer"), HttpStatus.OK);
	}

	// Sending Back To XSS error page
	@RequestMapping(value="/XSSError",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView maliciousParametersDetected(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("XSSPage");
	}

}