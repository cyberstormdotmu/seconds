package com.kenure.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Customer;
import com.kenure.entity.NormalCustomer;
import com.kenure.entity.Role;
import com.kenure.entity.User;
import com.kenure.model.KDLEmailModel;
import com.kenure.service.INormalCustomerService;
import com.kenure.service.IUserService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;
import com.kenure.utils.MD5Encoder;
import com.kenure.utils.MailSender;
import com.kenure.utils.RandomNumberGenerator;

@Controller
@RequestMapping(value="/customerOperation/normalCustomerOperation")
public class NormalCustomerController {

	private static Logger log = LoggerUtils.getInstance(NormalCustomerController.class);
	
	@Autowired
	private IUserService userService;

	@Autowired 
	INormalCustomerService normalCustomerService;

	@Autowired
	private MailSender mailSender;
	
	@RequestMapping(value="/getNormalCustomerOfSuperCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getNormalCustomerOfSuperCustomer(HttpServletRequest request,HttpServletResponse response){

		// Lets Check Current User is super customer or not
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		User loggedUser = null;

		if(isNormalCustomer == null){
			loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.currentUser);

			if(loggedUser.getRole().getRoleName().equalsIgnoreCase("customer")){
				Customer customer = (Customer) userService.initializeNormalCustomer(loggedUser.getUserId());

				List<NormalCustomer> nCustomerList = new ArrayList<NormalCustomer>(customer.getNormalCustomer());
				List<JsonObject> responseList = new ArrayList<JsonObject>();
				nCustomerList.stream().forEach(n -> {
					JsonObject jObject = new JsonObject();
					if(n.getUser() != null ){
						jObject.addProperty("userName", n.getUser().getUserName());
						jObject.addProperty("cell", n.getUser().getDetails().getCell_number1());
						jObject.addProperty("email", n.getUser().getDetails().getEmail1());
						jObject.addProperty("userId", n.getUser().getUserId());
						if(n.getUser().getActiveStatus()){
							jObject.addProperty("activeStatus", "Active");
						}else{
							jObject.addProperty("activeStatus", "Inactive");
						}
						
						responseList.add(jObject);
					}
				});
				responseList.sort((JsonObject jObject1,JsonObject jObject2) -> jObject1.get("userName").getAsString().compareTo(jObject2.get("userName").getAsString())); // Response List Sorted As Based On Name
				return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
			}else
				return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}else
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
	}

	@RequestMapping(value="/addNormalCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addNormalCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="phone") String phone,
			@RequestParam(value="email") String email,
			@RequestParam(value="address") String address,
			@RequestParam(value="zip") String zip){

		// Lets check weather UserName is exist or not 
		String responseString = "";
		/*if(!userService.checkForCustomerCode(customerCode) ){
			responseString="codeExist";
		}else*/ 
		if (userService.availableUserName(userName) != null) {
			responseString="userExist";
		}else{
			User loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.currentUser);
			Customer currenctCustomer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());

			User user = new User();
			user.setFirstTimeLogin(Boolean.TRUE);
			user.setUserName(userName);
			char[] password = RandomNumberGenerator.generatePswd();
			user.setPassword(MD5Encoder.MD5Encryptor(password.toString()));
			user.setCreatedBy(loggedUser.getUserId());
			user.setActiveStatus(Boolean.TRUE);
			user.setCreatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			Role role = userService.getRoleByName("normalcustomer");
			user.setRole(role);
			ContactDetails normalCustomerDetails = new ContactDetails();
			normalCustomerDetails.setFirstName(firstName);
			normalCustomerDetails.setLastname(lastName);
			normalCustomerDetails.setCell_number1(phone);
			normalCustomerDetails.setEmail1(email);
			normalCustomerDetails.setAddress1(address);
			normalCustomerDetails.setZipcode(zip);
			normalCustomerDetails.setActive(Boolean.TRUE);
			user.setDetails(normalCustomerDetails);

			NormalCustomer nc = new NormalCustomer();
			nc.setSuperCustomer(currenctCustomer);
			//nc.setUser(user);
			normalCustomerService.saveNormalCustomerByNormal(nc);
			
			user.setNormalCustomer(nc);

			responseString = normalCustomerService.saveNormalCustomer(user);

			if(responseString.equalsIgnoreCase("success")){
				String subject = "New account created.";
				
				new Thread( () ->{
					try {
						
						KDLEmailModel model = new KDLEmailModel();
						model.setSubject(subject);
						model.setName(firstName+" "+lastName);
						model.setUserName(userName);
						model.setPassword(password.toString());
						model.setProjectPath(ApplicationConstants.getAppConstObject().getFullPath());
						
						Map map = new HashMap<String,KDLEmailModel>();
						map.put("emailConst", model);
						
						mailSender.htmlEmailSender(email, subject, map,"newAccount",null);
						
						log.info("mail sent successfully to "+email);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				}).start();
			}
		}
		
		return new ResponseEntity<>(new Gson().toJson(responseString),HttpStatus.OK);
	}

	@RequestMapping(value="/editNormalCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String>  editNormalEditCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="rowObject") int userId){

		User editNormalUser = userService.getUserDetailsByUserID(userId);
		JsonObject jObject = new JsonObject();
		if(editNormalUser != null){
			/*jObject.addProperty("customerCode", editNormalUser.getCustomerCode());*/
			jObject.addProperty("firstName", editNormalUser.getDetails().getFirstName());
			jObject.addProperty("lastName", editNormalUser.getDetails().getLastname());
			jObject.addProperty("userName", editNormalUser.getUserName());
			jObject.addProperty("phone", editNormalUser.getDetails().getCell_number1());
			jObject.addProperty("email", editNormalUser.getDetails().getEmail1());
			jObject.addProperty("address", editNormalUser.getDetails().getAddress1());
			jObject.addProperty("zip", editNormalUser.getDetails().getZipcode());
			if(editNormalUser.getActiveStatus()){
				jObject.addProperty("activeStatus", "Active");
			}else{
				jObject.addProperty("activeStatus", "Inactive");
			}
			jObject.addProperty("userId", userId);
		}
		request.getSession().setAttribute("editNormalustomerData", jObject);
		return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
	}

	@RequestMapping(value="/getEditNormalCustomerDetails",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String>  editNormalEditCustomer(HttpServletRequest request,HttpServletResponse response){
		JsonObject jObject = new JsonObject();
		if(((JsonObject) request.getSession().getAttribute("editNormalustomerData")) != null){
			jObject = (JsonObject) request.getSession().getAttribute("editNormalustomerData");
			request.getSession().removeAttribute("editNormalustomerData");
		}
		return new ResponseEntity<>(new Gson().toJson(jObject),HttpStatus.OK);
	}

	@RequestMapping(value="/updateNormalCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> updateNormalCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="userId") String userId,
			@RequestParam(value="firstName") String firstName,
			@RequestParam(value="lastName") String lastName,
			@RequestParam(value="userName") String userName,
			@RequestParam(value="phone") String phone,
			@RequestParam(value="email") String email,
			@RequestParam(value="address") String address,
			@RequestParam(value="activeStatus") String activeStatus,
			@RequestParam(value="zip") String zip){

		User loggedUser = (User) request.getSession().getAttribute(KenureUtilityContext.currentUser);
		User editNormalUser = userService.getUserDetailsByUserID(Integer.parseInt(userId));
		editNormalUser.getDetails().setFirstName(firstName);
		editNormalUser.getDetails().setLastname(lastName);
		editNormalUser.getDetails().setCell_number1(phone);
		editNormalUser.getDetails().setEmail1(email);
		editNormalUser.getDetails().setAddress1(address);
		editNormalUser.getDetails().setZipcode(zip);
		if(activeStatus.equalsIgnoreCase("active")){
			editNormalUser.setActiveStatus(Boolean.TRUE);
			editNormalUser.setUpdatedBy(loggedUser.getUserId());
			editNormalUser.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			editNormalUser.setDeletedBy(null);
			editNormalUser.setDeletedTs(null);

		}else{	
			editNormalUser.setActiveStatus(Boolean.FALSE);
			editNormalUser.setUpdatedBy(loggedUser.getUserId());
			editNormalUser.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			editNormalUser.setDeletedBy(loggedUser.getUserId());
			editNormalUser.setDeletedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}
		userService.updateUserEntity(editNormalUser);
		return null;
	}

	@RequestMapping(value="/deleteNormalCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteNormalCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String userId){
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		int id;
		if(customer != null){
			id = customer.getCustomerId();
		}else{
			id= loggedUser.getUserId();
		}
		
		int customerId = customer.getCustomerId();
		User editNormalUser = userService.getUserDetailsByUserID(Integer.parseInt(userId));
		try{
			if(userService.deleteUser(Integer.parseInt(userId), editNormalUser.getRole().getRoleId(),id)){
				return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
			}else{
				return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
			}
		}catch(Exception e){
			e.getMessage();
			return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
		}
	}
	@RequestMapping(value="/searchNormalCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchNormalCustomer(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="searchCriteria") String userId){
		
		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		Customer customer = userService.getOnlyCustomerByUserId(loggedUser.getUserId());
		
		List<User> userList = userService.searchUserListByUserId(userId,customer);
		
		List<JsonObject> responseList = new ArrayList<JsonObject>();
		userList.stream().forEach(n -> {
			JsonObject jObject = new JsonObject();
			if(n != null ){
				jObject.addProperty("userName", n.getUserName());
				jObject.addProperty("cell", n.getDetails().getCell_number1());
				jObject.addProperty("email", n.getDetails().getEmail1());
				if(n.getActiveStatus()){
					jObject.addProperty("activeStatus", "Active");
				}else{
					jObject.addProperty("activeStatus", "Inactive");
				}
				jObject.addProperty("userId", n.getUserId());
				responseList.add(jObject);
			}
		});
		
		if(responseList.size() > 0){
			responseList.sort((JsonObject jObject1,JsonObject jObject2) -> jObject1.get("userName").getAsString().compareTo(jObject2.get("userName").getAsString())); // Response List Sorted As Based On Name
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Gson().toJson("empty"),HttpStatus.OK);
		}
		
	}
	
	//*****************************************************************************************************//
	
}
