package com.kenure.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.kenure.entity.BatteryLife;
import com.kenure.entity.ContactDetails;
import com.kenure.entity.Country;
import com.kenure.entity.Currency;
import com.kenure.entity.Customer;
import com.kenure.entity.DataCollector;
import com.kenure.entity.DataPlan;
import com.kenure.entity.User;
import com.kenure.service.IUserService;
import com.kenure.utils.LoggerUtils;

@Controller
@RequestMapping(value="/adminOperation")
public class AdminController {

	@Autowired
	IUserService userService;

	private Gson gsonInstance = new Gson();

	private static final org.slf4j.Logger logger = LoggerUtils.getInstance(LoginController.class);

	@RequestMapping(value="/adminDashboardData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> adminDashBoardData(HttpServletRequest request, HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		String userListGsonId = "";

		List<Object> objectList = new ArrayList<Object>();

		if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){
			objectList.add(new Gson().toJson(loggedUser));
			List<User> userList = userService.getUserList();
			if(!userList.isEmpty() && userList != null)
				userListGsonId = new Gson().toJson(userList);
			objectList.add(userListGsonId);
		}
		return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
	}

	@RequestMapping(value = "/userManagement",method = RequestMethod.GET)
	public ModelAndView userManagement(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("AdminUserManagement");
	}

	@RequestMapping(value="/addUserRedirect",method=RequestMethod.GET)
	public ModelAndView addUserRedirect(HttpServletRequest request, HttpServletResponse response){

		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		if(currentUser.getRole().getRoleName().equalsIgnoreCase("admin"))
			return new ModelAndView("addUser");
		else
			return new ModelAndView("userManagement");
	}

	@RequestMapping(value="/searchCustomer",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> searchUser(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="customerNameCriteria",required=false) String customerNameCriteria,
			@RequestParam(value="portalPlanActiveDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date portalPlanActiveDate,
			@RequestParam(value="portalPlanExpiryDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date portalPlanExpiryDate,
			@RequestParam(value="dataPlanActiveDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date dataPlanActiveDate,
			@RequestParam(value="dataPlanExpiryDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date dataPlanExpiryDate){

		List<Customer> searchedList = userService.searchCustomer(customerNameCriteria,portalPlanActiveDate,portalPlanExpiryDate,
				dataPlanActiveDate,dataPlanExpiryDate);

		/*List<Customer> searchedList = userService.searchCustomer(customerNameCriteria);*/

		List<JsonObject> customerList = new ArrayList<JsonObject>();
		SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
		if(searchedList.size() > 0){

			for(int i=0;i<searchedList.size();i++){
				JsonObject  jsObject = new JsonObject();
				jsObject.addProperty("customerId", searchedList.get(i).getCustomerId());
				jsObject.addProperty("status", searchedList.get(i).getStatus());
				jsObject.addProperty("companyName", searchedList.get(i).getUser().getDetails().getFirstName());
				jsObject.addProperty("contactName", searchedList.get(i).getUser().getDetails().getLastname());
				jsObject.addProperty("contractExpiry", sdf.format(searchedList.get(i).getPortalPlanExpiryDate()));
				jsObject.addProperty("activeDate",  sdf.format(searchedList.get(i).getDataPlanActivatedDate()));
				jsObject.addProperty("expiryDate",  sdf.format(searchedList.get(i).getDataPlanExpiryDate()));

				customerList.add(jsObject);
			}

			return new ResponseEntity<Object>(new Gson().toJson(customerList), HttpStatus.OK);
		}

		return new ResponseEntity<Object>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
	}


	//for getting data plan list
	@RequestMapping(value="/getDataPlan",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getDataPlan(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			List<Object> objectList = new ArrayList<Object>();
			List<DataPlan> dataPlanList = userService.getDataPlanList();
			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

			if(dataPlanList != null){
				Iterator<DataPlan> dataPlanIterator = dataPlanList.iterator();

				while(dataPlanIterator.hasNext()){
					DataPlan dataplan = dataPlanIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("dataPlanId", dataplan.getDataPlanId());
					json.addProperty("mbPerMonth", dataplan.getMbPerMonth());
					json.addProperty("createdDate", sdf.format(dataplan.getCreatedDate()));
					objectList.add(json);
				}
			}

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessDataPlanList"), HttpStatus.OK);
	}

	//for adding data plan
	@RequestMapping(value="/addDataPlan",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> addDataPlan(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="dataplan") int mbPerMonth){

		boolean isDataPlanValid = userService.checkForDataPlan((mbPerMonth));

		if(!isDataPlanValid){
			return new ResponseEntity<String>(new Gson().toJson("dataplanalreadyexist"),HttpStatus.OK);
		}
		Date date = new Date();
		DataPlan dataplan = new DataPlan();
		dataplan.setMbPerMonth(mbPerMonth);
		dataplan.setCreatedDate(date);
		userService.addNewDataPlan(dataplan);
		logger.info("data plan successfully added...");
		return new ResponseEntity<String>(new Gson().toJson("dataPlanSuccessfullyadded"), HttpStatus.OK);

		//return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessDataPlanList"), HttpStatus.OK);
	}

	@RequestMapping(value = "/editDataPlan",method = RequestMethod.GET)
	public ResponseEntity<String> editDataPlan(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="dataPlanId",required=false) String dataPlanId) {
		if(dataPlanId != null){
			DataPlan dataplan = userService.getDataPlanById(Integer.parseInt(dataPlanId));

			request.getSession().setAttribute("editdataPlan", dataplan);

			return new ResponseEntity<String>(new Gson().toJson("editDataPlanData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/getMyData",method=RequestMethod.POST)
	public ResponseEntity<String> getEditdataPlan(HttpServletRequest request,HttpServletResponse response){

		DataPlan dataplan = (DataPlan) request.getSession().getAttribute("editdataPlan");

		List<JsonObject> objectList = new ArrayList<JsonObject>();
		if(dataplan!= null){

			JsonObject json = new JsonObject();
			json.addProperty("mbPerMonth", dataplan.getMbPerMonth());
			json.addProperty("dataPlanId", dataplan.getDataPlanId());
			objectList.add(json);
			request.getSession().removeAttribute("editdataPlan");
			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	

		return new ResponseEntity<>(new Gson().toJson("nodataPlanFound"),HttpStatus.OK);
	}

	@RequestMapping(value = "/editDataPlanForm",method = RequestMethod.GET)
	public ModelAndView editDataPlanForm(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("addEditDataPlan");
	}

	
	//for updating data plan
	@RequestMapping(value = "/updateDataPlan",method = RequestMethod.GET)
	public ResponseEntity<String> updateDataPlan(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("dataplanId") String dataPlanId,
			@RequestParam("mbPerMonth") String mbPerMonth) {

		boolean isDataPlanValid = userService.checkForDataPlan(Integer.parseInt(mbPerMonth));

		if(!isDataPlanValid){
			return new ResponseEntity<String>(new Gson().toJson("dataplanalreadyexist"),HttpStatus.OK);
		}

		if(dataPlanId != null){

			DataPlan dataplanobj = userService.getDataPlanById(Integer.parseInt(dataPlanId));
			dataplanobj.setMbPerMonth(Integer.parseInt(mbPerMonth));
			userService.updateDataPlan(dataplanobj);
			return new ResponseEntity<String>(new Gson().toJson("dataPlanUpdatedSuccessfully"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("data plan id can not be null"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchDataPlan",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchDataPlan(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="searchInput" ,required=false) int searchInput){

		if(searchInput < 1 || searchInput > 30){
			return new ResponseEntity<String>(new Gson().toJson("emptyValue"), HttpStatus.OK);
		}

		logger.info("searching dataplan mb per month "+ searchInput);
		List<DataPlan> searchedList = userService.searchDataplan(searchInput);
		List<JsonObject> dataplanList = new ArrayList<JsonObject>();

		if(searchedList!= null){
			Iterator<DataPlan> dataplanIterator = searchedList.iterator();

			while(dataplanIterator.hasNext()){
				DataPlan currentdataplan = dataplanIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("dataPlanId", currentdataplan.getDataPlanId());
				json.addProperty("mbPerMonth", currentdataplan.getMbPerMonth());
				json.addProperty("createdDate", currentdataplan.getCreatedDate().toString());

				dataplanList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(dataplanList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
	}

	@RequestMapping(value = "/getSpareDataCollector",method = RequestMethod.GET)
	public ResponseEntity<String> getSpareDataCollector(HttpServletRequest request, HttpServletResponse response) {

		User loggedUser = (User) request.getSession().getAttribute("currentUser");


		//List<Object> objectList = new ArrayList<Object>();
		//	objectList.add(new Gson().toJson(loggedUser));
		if(loggedUser != null && loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){


			List<DataCollector> dataCollectorList = userService.getSpareDataCollectorList();
			List<JsonObject> datacollectorList = new ArrayList<JsonObject>();

			if(dataCollectorList!= null){
				Iterator<DataCollector> datacollectorIterator = dataCollectorList.iterator();

				while(datacollectorIterator.hasNext()){
					DataCollector currentdatacollector = (DataCollector)datacollectorIterator.next();
					JsonObject json = new JsonObject();
					json.addProperty("datacollectorId", currentdatacollector.getDatacollectorId());
					json.addProperty("dcSerialNumber", currentdatacollector.getDcSerialNumber());
					json.addProperty("dcIp", currentdatacollector.getDcIp());
					json.addProperty("dcUserPassword", currentdatacollector.getDcUserPassword());
					json.addProperty("dcSimcardNo", currentdatacollector.getDcSimcardNo());
					json.addProperty("dcUserId", currentdatacollector.getDcUserId());
					json.addProperty("totalEndpoints", currentdatacollector.getTotalEndpoints());
					json.addProperty("longitude", currentdatacollector.getLongitude());

					if(!(currentdatacollector.getCustomer() == null)){
						json.addProperty("customerName", currentdatacollector.getCustomer().getCustomerName());
					}
					else{
						json.addProperty("customerName", "-");
					}


					datacollectorList.add(json);
				}	
			}
			return new ResponseEntity<String>(new Gson().toJson(datacollectorList), HttpStatus.OK);	

		}
		return null;


	}

	@RequestMapping(value = "/editSpareDataCollector",method = RequestMethod.GET)
	public ResponseEntity<String> editSpareDataCollector(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("datacollectorId") String datacollectorId) {
		if(datacollectorId != null){
			DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));

			request.getSession().setAttribute("editdatacollectorList", datacollector);

			return new ResponseEntity<String>(new Gson().toJson("editDatacollectorData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/getEditdatacollector",method=RequestMethod.POST)
	public ResponseEntity<String> getEditdatacollector(HttpServletRequest request,HttpServletResponse response){

		DataCollector datacollector = (DataCollector) request.getSession().getAttribute("editdatacollectorList");

		List<JsonObject> objectList = new ArrayList<JsonObject>();
		//List<JsonObject> objectListFinal = new ArrayList<JsonObject>();

		//	List<Object> customerList = (List<Object>) request.getSession().getAttribute("customerList");
		if(datacollector!= null){

			JsonObject json = new JsonObject();
			json.addProperty("datacollectorId", datacollector.getDatacollectorId());
			json.addProperty("dcSerialNumber", datacollector.getDcSerialNumber());
			json.addProperty("dcIp", datacollector.getDcIp());
			json.addProperty("dcUserPassword", datacollector.getDcUserPassword());
			json.addProperty("dcSimcardNo", datacollector.getDcSimcardNo());
			json.addProperty("totalEndpoints", datacollector.getTotalEndpoints());
			json.addProperty("dcUserId", datacollector.getDcUserId());
			json.addProperty("latitude", datacollector.getLatitude());
			json.addProperty("longitude", datacollector.getLongitude());
			if(!(datacollector.getCustomer() == null)){
				json.addProperty("customerName", datacollector.getCustomer().getCustomerName());
				objectList.add(json);
			}
			else{

				List<Customer> customerList = userService.getActiveCustomerList();

				objectList.add(json);

				if(customerList != null){
					Iterator<Customer> customerIterator = customerList.iterator();
					while(customerIterator.hasNext()){

						Customer customer = customerIterator.next();
						JsonObject json2 = new JsonObject();
						json2.addProperty("customerName", customer.getCustomerName());
						//	json2.addProperty("customerId", customer.getCustomerId());

						objectList.add(json2);
					}
				}

			}


			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	


		return new ResponseEntity<>(new Gson().toJson("nodatacollectorFound"),HttpStatus.OK);
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/updateDataCollector",method = RequestMethod.GET)
	public ResponseEntity<String> updateDataCollector(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("dcSerialNumber") String dcSerialNumber,
			@RequestParam("dcIp") String dcIp,
			@RequestParam("dcSimcardNo") String dcSimcardNo,
			@RequestParam("dcUserId") String dcUserId,
			@RequestParam("dcUserPassword") String dcUserPassword,
			@RequestParam(value="latitude",required=false) String latitude,
			@RequestParam(value="longitude",required=false) String longitude,
			@RequestParam("datacollectorId") String datacollectorId,
			@RequestParam(value ="customerName",required=false) String customerName){
		if(datacollectorId != null){

			DataCollector datacollector = userService.getSpareDataCollectorById(Integer.parseInt(datacollectorId));


			if(dcIp != null || !dcIp.isEmpty())
				datacollector.setDcIp(dcIp);

			if(dcUserId != null || !dcUserId.isEmpty())
				datacollector.setDcUserId(dcUserId);

			if(dcUserPassword != null || !dcUserPassword.isEmpty())
				datacollector.setDcUserPassword(dcUserPassword);

			if(latitude == null || latitude.isEmpty())
				datacollector.setLatitude(null);

			if(latitude != null && !latitude.isEmpty())
				datacollector.setLatitude(Double.parseDouble(latitude));

			if(longitude == null || longitude.isEmpty())
				datacollector.setLongitude(null);

			if(longitude != null && !longitude.isEmpty())
				datacollector.setLongitude(Double.parseDouble(longitude));

			if(customerName!= null){
				Customer customer = userService.getCustomerDetailsByCustomerName(customerName);
				datacollector.setCustomer(customer);
			}

			if(datacollector.getDcSerialNumber().equalsIgnoreCase(dcSerialNumber)){

				if(datacollector.getDcSimcardNo().equalsIgnoreCase(dcSimcardNo)){

					if(dcSerialNumber != null || !dcSerialNumber.isEmpty())
						datacollector.setDcSerialNumber(dcSerialNumber);
					if(dcSimcardNo != null || !dcSimcardNo.isEmpty())
						datacollector.setDcSimcardNo(dcSimcardNo);

					userService.updateDataCollector(datacollector);
					return new ResponseEntity<String>(new Gson().toJson("dataCollectorUpdatedSuccessfully"), HttpStatus.OK);

				}else{

					boolean isSimcardNumValid = userService.checkForSimcardNumber(dcSimcardNo);

					if(!isSimcardNumValid){
						return new ResponseEntity<String>(new Gson().toJson("simcardnumberalreadyexists"),HttpStatus.OK);
					}

					if(dcSimcardNo != null || !dcSimcardNo.isEmpty())
						datacollector.setDcSimcardNo(dcSimcardNo);

					userService.updateDataCollector(datacollector);
					return new ResponseEntity<String>(new Gson().toJson("dataCollectorUpdatedSuccessfully"), HttpStatus.OK);

				}

			}else {

				boolean isSerialNumValid = userService.checkForSerialNumber(dcSerialNumber);
				boolean isSimcardNumValid = userService.checkForSimcardNumber(dcSimcardNo);

				if(!isSerialNumValid){
					return new ResponseEntity<String>(new Gson().toJson("dcserialnumberalreadyexists"),HttpStatus.OK);
				}
				if(!isSimcardNumValid){
					return new ResponseEntity<String>(new Gson().toJson("simcardnumberalreadyexists"),HttpStatus.OK);
				}

				if(dcSerialNumber != null || !dcSerialNumber.isEmpty())
					datacollector.setDcSerialNumber(dcSerialNumber);
				if(dcSimcardNo != null || !dcSimcardNo.isEmpty())
					datacollector.setDcSimcardNo(dcSimcardNo);


				userService.updateDataCollector(datacollector);
				return new ResponseEntity<String>(new Gson().toJson("dataCollectorUpdatedSuccessfully"), HttpStatus.OK);			}


		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value = "/addDataCollector",method = RequestMethod.POST)
	public ResponseEntity<String> addDataCollector(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("dcSerialNumber") String dcSerialNumber,
			@RequestParam("dcIp") String dcIp,
			@RequestParam(value="dcSimcardNo", required=false) String dcSimcardNo,
			@RequestParam("dcUserId") String dcUserId,
			@RequestParam("dcUserPassword") String dcUserPassword,
			/*@RequestParam(value="latitude",required=false) String latitude,
			@RequestParam(value="longitude",required=false) String longitude,*/
			@RequestParam(value="customerId", required=false) String customerId){

		DataCollector datacollector = new DataCollector();

		if(dcSerialNumber != null && !dcSerialNumber.trim().equals(""))
			datacollector.setDcSerialNumber(dcSerialNumber);

		if(dcIp != null && !dcIp.trim().equals(""))
			datacollector.setDcIp(dcIp);

		if(dcSimcardNo != null && !dcSimcardNo.trim().equals(""))
			datacollector.setDcSimcardNo(dcSimcardNo);

		if(dcUserId != null && !dcUserId.trim().equals(""))
			datacollector.setDcUserId(dcUserId);

		boolean isSerialNumValid = userService.checkForSerialNumber(dcSerialNumber);
		boolean isSimcardNumValid = userService.checkForSimcardNumber(dcSimcardNo);

		if(!isSerialNumValid){
			return new ResponseEntity<String>(new Gson().toJson("dcserialnumberalreadyexists"),HttpStatus.OK);
		}
		if(!isSimcardNumValid){
			return new ResponseEntity<String>(new Gson().toJson("simcardnumberalreadyexists"),HttpStatus.OK);
		}


		if(dcUserPassword != null && !dcUserPassword.trim().equals(""))
			datacollector.setDcUserPassword(dcUserPassword);

		/*if(latitude != null && !latitude.trim().equals(""))
			datacollector.setLatitude(Double.parseDouble(latitude));


		if(longitude != null && !longitude.trim().equals(""))
			datacollector.setLongitude(Double.parseDouble(longitude));*/

		if(customerId != null && !customerId.trim().equals("")){
			Customer customer = userService.getCustomerDetailsByCustomerId(Integer.parseInt(customerId));
			datacollector.setCustomer(customer);
		}
		datacollector.setIsCommissioned(false);
		datacollector.setActive(true);
		datacollector.setMeterReadingInterval(0);
		datacollector.setNetworkReadingInterval(0);
		datacollector.setNetworkStatusInterval(0);

		userService.addNewDataCollector(datacollector);
		return new ResponseEntity<String>(new Gson().toJson("dataCollectorAddedSuccessfully"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchDataCollector",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchDataCollector(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="customerName", required = false) String customerName,
			@RequestParam(value="dcSerialNumber", required = false)String dcSerialNumber){

		logger.info("searching customer name"+customerName);
		logger.info("searching dc serial number" +dcSerialNumber);

		//	Customer customer = userService.getCustomerDetailsByCustomerName(customerName);

		List<DataCollector> searchedList = userService.searchDataCollector(customerName, dcSerialNumber);

		List<JsonObject> datacollectorList = new ArrayList<JsonObject>();

		if(searchedList!= null){
			Iterator<DataCollector> datacollectorIterator = searchedList.iterator();

			while(datacollectorIterator.hasNext()){
				DataCollector currentdatacollector = (DataCollector)datacollectorIterator.next();
				JsonObject json = new JsonObject();
				json.addProperty("datacollectorId", currentdatacollector.getDatacollectorId());
				json.addProperty("dcSerialNumber", currentdatacollector.getDcSerialNumber());
				json.addProperty("dcIp", currentdatacollector.getDcIp());
				json.addProperty("dcUserPassword", currentdatacollector.getDcUserPassword());
				json.addProperty("dcSimcardNo", currentdatacollector.getDcSimcardNo());
				json.addProperty("dcUserId", currentdatacollector.getDcUserId());
				json.addProperty("latitude", currentdatacollector.getLatitude());
				json.addProperty("longitude", currentdatacollector.getLongitude());

				if(!(currentdatacollector.getCustomer() == null)){
					json.addProperty("customerName", currentdatacollector.getCustomer().getCustomerName());
				}
				else{
					json.addProperty("customerName", "-");
				}


				datacollectorList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(datacollectorList), HttpStatus.OK);
		}

		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
	}


	//******************************************************

	@RequestMapping(value="/getCountryInit",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getCountryInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			List<Object> objectList = new ArrayList<Object>();

			List<Country> countryList = userService.getCountryList();

			if(countryList != null){

				Iterator<Country> countryIterator = countryList.iterator();

				while(countryIterator.hasNext()){

					Country country = countryIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("countryId", country.getCountryId());
					json.addProperty("countryName", country.getCountryName());
					json.addProperty("countryCode", country.getCountryCode());

					objectList.add(json);
				}
			}

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessCountryList"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchCountry",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchCountry(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="searchCountryName") String searchCountryName){

		logger.info("Searching Country "+ searchCountryName);

		List<Country> searchedList = userService.searchCountryByName(searchCountryName);

		List<JsonObject> countryList = new ArrayList<JsonObject>();

		if(searchedList.size() > 0){			
			Iterator<Country> countryIterator = searchedList.iterator();

			while(countryIterator.hasNext()){

				Country country = countryIterator.next();

				JsonObject json = new JsonObject();

				json.addProperty("countryId", country.getCountryId());
				json.addProperty("countryName", country.getCountryName());
				json.addProperty("countryCode", country.getCountryCode());

				countryList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(countryList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOCOUNTRYFOUND), HttpStatus.OK);
	}


	@RequestMapping(value="/addCountry",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addCountry(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="countryName") String countryName,
			@RequestParam(value="countryCode") String countryCode){

		if(countryName.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcountryname"),HttpStatus.OK);
		}

		if(countryCode.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcountrycode"),HttpStatus.OK);
		}

		boolean isCountryNameValid = userService.checkForCountryName(countryName);
		boolean isCountryCodeValid = userService.checkForCountryCode(countryCode);

		if(!isCountryNameValid){
			return new ResponseEntity<String>(new Gson().toJson("countrynamealreadyexist"),HttpStatus.OK);
		}
		if(!isCountryCodeValid){
			return new ResponseEntity<String>(new Gson().toJson("countrycodealreadyexist"),HttpStatus.OK);
		}

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			Country country = new Country();
			country.setCountryName(countryName.trim());
			country.setCountryCode(countryCode.trim());

			userService.addNewCountry(country);

			logger.info("Country successfully added...");

			return new ResponseEntity<String>(new Gson().toJson("countrySuccessfullyadded"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessCountryList"), HttpStatus.OK);
	}

	@RequestMapping(value = "/editCountry",method = RequestMethod.POST)
	public ResponseEntity<String> editCountry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="countryId",required=false) String countryId) {

		if(countryId != null){

			Country country = userService.getCountryById(countryId);
			request.getSession().setAttribute("editCountry", country);

			return new ResponseEntity<String>(new Gson().toJson("editCountryData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/getCountryData",method=RequestMethod.POST)
	public ResponseEntity<String> getCountryData(HttpServletRequest request,HttpServletResponse response){

		Country country = (Country) request.getSession().getAttribute("editCountry");

		List<JsonObject> objectList = new ArrayList<JsonObject>();

		if(country!= null){

			JsonObject json = new JsonObject();
			json.addProperty("countryId", country.getCountryId());
			json.addProperty("countryName", country.getCountryName()); 
			json.addProperty("countryCode", country.getCountryCode());
			objectList.add(json);

			request.getSession().removeAttribute("editCountry");

			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	

		return new ResponseEntity<>(new Gson().toJson("noCountryFound"),HttpStatus.OK);
	}

	//for updating data plan
	@RequestMapping(value = "/updateCountry",method = RequestMethod.POST)
	public ResponseEntity<String> updateCountry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="countryId") String countryId,
			@RequestParam(value="countryName") String countryName,
			@RequestParam(value="countryCode") String countryCode) {

		if(countryName.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcountryname"),HttpStatus.OK);
		}
		if(countryCode.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcountrycode"),HttpStatus.OK);
		}

		Country countryobj = userService.getCountryById(countryId);

		if(countryobj.getCountryName().equalsIgnoreCase(countryName)){

			if(countryobj.getCountryCode().equalsIgnoreCase(countryCode)){

				return new ResponseEntity<String>(new Gson().toJson("countryUpdatedSuccessfully"), HttpStatus.OK);

			}else{
				boolean isCountryCodeValid = userService.checkForCountryCode(countryCode);
				if(!isCountryCodeValid){
					return new ResponseEntity<String>(new Gson().toJson("countrycodealreadyexist"),HttpStatus.OK);
				}

				countryobj.setCountryCode(countryCode.trim());

				userService.updateCountry(countryobj);

				return new ResponseEntity<String>(new Gson().toJson("countryUpdatedSuccessfully"), HttpStatus.OK);

			}

		}else {
			boolean isCountryNameValid = userService.checkForCountryName(countryName);
			boolean isCountryCodeValid = userService.checkForCountryCode(countryCode);

			if(!isCountryNameValid){
				return new ResponseEntity<String>(new Gson().toJson("countrynamealreadyexist"),HttpStatus.OK);
			}
			if(!isCountryCodeValid){
				return new ResponseEntity<String>(new Gson().toJson("countrycodealreadyexist"),HttpStatus.OK);
			}

			countryobj.setCountryName(countryName.trim());
			countryobj.setCountryCode(countryCode.trim());

			userService.updateCountry(countryobj);

			return new ResponseEntity<String>(new Gson().toJson("countryUpdatedSuccessfully"), HttpStatus.OK);
		}

	}

	//******************************************************

	@RequestMapping(value="/getCurrencyInit",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getCurrencyInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");
		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			List<Object> objectList = new ArrayList<Object>();

			List<Currency> currencyList = userService.getCurrencyList();

			if(currencyList != null){

				Iterator<Currency> currencyIterator = currencyList.iterator();

				while(currencyIterator.hasNext()){

					Currency currency = currencyIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("currencyId", currency.getCurrencyId());
					json.addProperty("currencyName", currency.getCurrencyName());
					json.addProperty("currencySymbol", currency.getCurrencySymbol());

					objectList.add(json);
				}
			}

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessCurrencyList"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchCurrency",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchCurrency(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="searchCurrencyName") String searchCurrencyName){

		logger.info("Searching Currency "+ searchCurrencyName);

		List<Currency> searchedList = userService.searchCurrencyByName(searchCurrencyName);

		List<JsonObject> currencyList = new ArrayList<JsonObject>();

		if(searchedList.size() > 0){

			Iterator<Currency> currencyIterator = searchedList.iterator();

			while(currencyIterator.hasNext()){

				Currency currency = currencyIterator.next();

				JsonObject json = new JsonObject();
				json.addProperty("currencyId", currency.getCurrencyId());
				json.addProperty("currencyName", currency.getCurrencyName());
				json.addProperty("currencySymbol", currency.getCurrencySymbol());

				currencyList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(currencyList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOUSERFOUND), HttpStatus.OK);
	}


	@RequestMapping(value="/addCurrency",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addCurrency(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="currencyName") String currencyName,
			@RequestParam(value="currencySymbol") String currencySymbol){

		if(currencyName.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcurrencyname"),HttpStatus.OK);
		}

		if(currencySymbol.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcurrencysymbol"),HttpStatus.OK);
		}

		boolean isCurrencyNameValid = userService.checkForCurrencyName(currencyName);

		if(!isCurrencyNameValid){
			return new ResponseEntity<String>(new Gson().toJson("currencynamealreadyexist"),HttpStatus.OK);
		}

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			Currency currency = new Currency();
			currency.setCurrencyName(currencyName.trim());
			currency.setCurrencySymbol(currencySymbol);

			userService.addNewCurrency(currency);

			logger.info("Currency successfully added...");

			return new ResponseEntity<String>(new Gson().toJson("countrySuccessfullyadded"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessCurrencyList"), HttpStatus.OK);
	}

	@RequestMapping(value = "/editCurrency",method = RequestMethod.POST)
	public ResponseEntity<String> editCurrency(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="currencyId",required=false) String currencyId) {

		if(currencyId != null){

			Currency currency = userService.getCurrencyById(currencyId);

			request.getSession().setAttribute("editCurrency", currency);

			return new ResponseEntity<String>(new Gson().toJson("editCurrencyData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchDataCollectorFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/getCurrencyData",method=RequestMethod.POST)
	public ResponseEntity<String> getCurrencyData(HttpServletRequest request,HttpServletResponse response){

		Currency currency = (Currency) request.getSession().getAttribute("editCurrency");

		List<JsonObject> objectList = new ArrayList<JsonObject>();

		if(currency!= null){

			JsonObject json = new JsonObject();
			json.addProperty("currencyId", currency.getCurrencyId());
			json.addProperty("currencyName", currency.getCurrencyName());
			json.addProperty("currencySymbol", currency.getCurrencySymbol());
			objectList.add(json);

			request.getSession().removeAttribute("editCurrency");

			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	

		return new ResponseEntity<>(new Gson().toJson("noCurrencyFound"),HttpStatus.OK);
	}

	//for updating data plan
	@RequestMapping(value = "/updateCurrency",method = RequestMethod.POST)
	public ResponseEntity<String> updateCurrency(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="currencyId") String currencyId,
			@RequestParam(value="currencyName") String currencyName,
			@RequestParam(value="currencySymbol") String currencySymbol) {

		if(currencyName.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcurrencyname"),HttpStatus.OK);
		}

		if(currencySymbol.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("nullcurrencysymbol"),HttpStatus.OK);
		}

		Currency currencyobj = userService.getCurrencyById(currencyId);

		if(currencyobj.getCurrencyName().equalsIgnoreCase(currencyName)){

			currencyobj.setCurrencySymbol(currencySymbol);
			userService.updateCurrency(currencyobj);

			return new ResponseEntity<String>(new Gson().toJson("currencyUpdatedSuccessfully"), HttpStatus.OK);

		}else{

			boolean isCurrencyNameValid = userService.checkForCurrencyName(currencyName);

			if(!isCurrencyNameValid){
				return new ResponseEntity<String>(new Gson().toJson("currencynamealreadyexist"),HttpStatus.OK);
			}

			currencyobj.setCurrencyName(currencyName);
			currencyobj.setCurrencySymbol(currencySymbol);

			userService.updateCurrency(currencyobj);

			return new ResponseEntity<String>(new Gson().toJson("currencyUpdatedSuccessfully"), HttpStatus.OK);

		}
	}

	// ********************* Battery Life ************************


	@RequestMapping(value="/getBatteryInit",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getBatteryInit(HttpServletRequest request,HttpServletResponse response){

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			List<Object> objectList = new ArrayList<Object>();

			List<BatteryLife> batteryLifeList = userService.getBatteryLifeList();

			if(batteryLifeList != null){

				DecimalFormat df = new DecimalFormat("#.00");

				Iterator<BatteryLife> batteryLifeIterator = batteryLifeList.iterator();

				while(batteryLifeIterator.hasNext()){

					BatteryLife batteryLife = batteryLifeIterator.next();

					JsonObject json = new JsonObject();
					json.addProperty("batteryLifeId", batteryLife.getBatteryLifeId());
					json.addProperty("numberOfChildNodes", batteryLife.getNumberOfChildNodes());
					json.addProperty("estimatedBatteryLifeInYears", df.format(batteryLife.getEstimatedBatteryLifeInYears()));

					objectList.add(json);
				}
			}

			return new ResponseEntity<String>(new Gson().toJson(objectList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessBatteryList"), HttpStatus.OK);
	}

	@RequestMapping(value="/searchBattery",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchBattery(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value="childNodeInput") String childNodeInput){

		logger.info("Searching Battery Life "+ childNodeInput);

		if(childNodeInput.trim() == "" || childNodeInput.length() < 1){
			return new ResponseEntity<String>(new Gson().toJson("emptyfield"), HttpStatus.OK);
		}

		List<BatteryLife> searchedList = userService.searchBatteryByChildNode(childNodeInput);

		List<JsonObject> batteryList = new ArrayList<JsonObject>();

		if(searchedList != null && searchedList.size() > 0){			
			Iterator<BatteryLife> batteryIterator = searchedList.iterator();

			while(batteryIterator.hasNext()){

				BatteryLife batteryLife = batteryIterator.next();

				JsonObject json = new JsonObject();

				json.addProperty("batteryLifeId", batteryLife.getBatteryLifeId());
				json.addProperty("numberOfChildNodes", batteryLife.getNumberOfChildNodes());
				json.addProperty("estimatedBatteryLifeInYears", batteryLife.getEstimatedBatteryLifeInYears());

				batteryList.add(json);
			}
			return new ResponseEntity<String>(new Gson().toJson(batteryList), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson(ApplicationConstants.NOBATTERYFOUND), HttpStatus.OK);
	}


	@RequestMapping(value="/addBatteryLife",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> addBatteryLife(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="numberOfChildNodes") String numberOfChildNodes,
			@RequestParam(value="estimatedBatteryLifeInYears") String estimatedBatteryLifeInYears){

		if(numberOfChildNodes.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("numberofchildnodescantbenull"),HttpStatus.OK);
		}
		if(estimatedBatteryLifeInYears.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("batterylifecantbenull"),HttpStatus.OK);
		}

		if(Integer.parseInt(numberOfChildNodes) > 17 || Integer.parseInt(numberOfChildNodes) < 1){
			return new ResponseEntity<String>(new Gson().toJson("invalidvalue"),HttpStatus.OK);
		}

		boolean isChildNodesValid = userService.checkForChildNodes(numberOfChildNodes);

		if(!isChildNodesValid){
			return new ResponseEntity<String>(new Gson().toJson("numofchildnodesalreadyexist"),HttpStatus.OK);
		}

		User loggedUser = (User) request.getSession().getAttribute("currentUser");

		if(loggedUser.getRole().getRoleName().equalsIgnoreCase("admin")){

			BatteryLife batteryLife = new BatteryLife();
			batteryLife.setNumberOfChildNodes(Integer.parseInt(numberOfChildNodes));
			batteryLife.setEstimatedBatteryLifeInYears(Double.parseDouble(estimatedBatteryLifeInYears));

			userService.addNewBatteryLife(batteryLife);

			logger.info("Battery Life successfully added...");

			return new ResponseEntity<String>(new Gson().toJson("batterySuccessfullyadded"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("youCanNotAccessBatteryList"), HttpStatus.OK);
	}

	@RequestMapping(value="/deleteBattery",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteDUMeter(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam("batteryLifeId") String batteryLifeId){	

		if(batteryLifeId.matches("^-?\\d+$") && (batteryLifeId != null || !batteryLifeId.trim().isEmpty())){

			try{
				boolean isBatteryDeleted = userService.deleteBattery(Integer.parseInt(batteryLifeId));
				if(isBatteryDeleted){
					logger.info("Battery successfully deleted with id " + batteryLifeId);
					return new ResponseEntity<String>(new Gson().toJson("batterydeletedsuccessfully"), HttpStatus.OK);

				}else{
					logger.info("No such Battery found with Meter id " + batteryLifeId);
					return new ResponseEntity<String>(new Gson().toJson("No such Battery found"), HttpStatus.OK);
				}
			}catch(Exception e){
				logger.info("Error while deleting Battery with batteryLifeId " + batteryLifeId);
				return new ResponseEntity<String>(new Gson().toJson("batteryismapped"), HttpStatus.OK);
			}

		}

		logger.warn("error while parsing query parameter with MeterId " + batteryLifeId);
		return new ResponseEntity<String>(new Gson().toJson("Error Parsing Query Parameter"), HttpStatus.OK);
	}


	@RequestMapping(value = "/editBattery",method = RequestMethod.POST)
	public ResponseEntity<String> editBattery(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="batteryLifeId",required=false) String batteryLifeId) {

		if(batteryLifeId != null){

			BatteryLife batteryLife = userService.getBatteryLifeById(batteryLifeId);
			request.getSession().setAttribute("editBattery", batteryLife);

			return new ResponseEntity<String>(new Gson().toJson("editBatteryData"), HttpStatus.OK);
		}
		return new ResponseEntity<String>(new Gson().toJson("NoSuchBatteryFound"), HttpStatus.OK);
	}

	@RequestMapping(value="/getBatteryData",method=RequestMethod.POST)
	public ResponseEntity<String> getBatteryData(HttpServletRequest request,HttpServletResponse response){

		BatteryLife batteryLife = (BatteryLife) request.getSession().getAttribute("editBattery");

		List<JsonObject> objectList = new ArrayList<JsonObject>();

		if(batteryLife != null){

			JsonObject json = new JsonObject();
			json.addProperty("batteryLifeId", batteryLife.getBatteryLifeId());
			json.addProperty("numberOfChildNodes", batteryLife.getNumberOfChildNodes());
			json.addProperty("estimatedBatteryLifeInYears", batteryLife.getEstimatedBatteryLifeInYears());

			objectList.add(json);

			request.getSession().removeAttribute("editBattery");

			return new ResponseEntity<>(new Gson().toJson(objectList),HttpStatus.OK);
		}	

		return new ResponseEntity<>(new Gson().toJson("noBatteryFound"),HttpStatus.OK);
	}

	@RequestMapping(value = "/updateBattery",method = RequestMethod.POST)
	public ResponseEntity<String> updateBattery(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="batteryLifeId") String batteryLifeId,
			@RequestParam(value="numberOfChildNodes") String numberOfChildNodes,
			@RequestParam(value="estimatedBatteryLifeInYears") String estimatedBatteryLifeInYears) {

		if(numberOfChildNodes.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("numberofchildnodescantbenull"),HttpStatus.OK);
		}
		if(estimatedBatteryLifeInYears.trim().length() == 0){
			return new ResponseEntity<String>(new Gson().toJson("batterylifecantbenull"),HttpStatus.OK);
		}

		if(Integer.parseInt(numberOfChildNodes) > 17 || Integer.parseInt(numberOfChildNodes) < 1){
			return new ResponseEntity<String>(new Gson().toJson("invalidvalue"),HttpStatus.OK);
		}

		BatteryLife batteryLife = userService.getBatteryLifeById(batteryLifeId);

		if(batteryLife.getNumberOfChildNodes() == Double.parseDouble(numberOfChildNodes)){

			batteryLife.setEstimatedBatteryLifeInYears(Double.parseDouble(estimatedBatteryLifeInYears));
			userService.updateBatteryLife(batteryLife);

			return new ResponseEntity<String>(new Gson().toJson("batteryUpdatedSuccessfully"), HttpStatus.OK);

		}else{

			boolean isChildNodesValid = userService.checkForChildNodes(numberOfChildNodes);

			if(!isChildNodesValid){
				return new ResponseEntity<String>(new Gson().toJson("numofchildnodesalreadyexist"),HttpStatus.OK);
			}

			batteryLife.setNumberOfChildNodes(Integer.parseInt(numberOfChildNodes));
			batteryLife.setEstimatedBatteryLifeInYears(Double.parseDouble(estimatedBatteryLifeInYears));
			userService.updateBatteryLife(batteryLife);

			return new ResponseEntity<String>(new Gson().toJson("batteryUpdatedSuccessfully"), HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/updateBatteryByPercent",method = RequestMethod.POST)
	public ResponseEntity<String> updateBatteryByPercent(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="percentageUpDown") String percentageUpDown,
			@RequestParam(value="batteryPercentage") String batteryPercentage) {

		String operator = null;
		if(percentageUpDown.equalsIgnoreCase("up")){
			operator = "+";
		}else{
			operator = "-";
		}
		userService.updateBatteryEstimatedLife(operator,batteryPercentage);
		return new ResponseEntity<String>(new Gson().toJson("success"), HttpStatus.OK);

	}

	// Admin Pages Management (Separating of Template Controller)

	@RequestMapping(value = "/adminDashboard",method = RequestMethod.GET)
	public ModelAndView admindashboard(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("adminDashboard");
	}

	@RequestMapping(value = "/dataPlanManagement",method = RequestMethod.GET)
	public ModelAndView dataPlanManagement(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("dataPlanManagement");
	}

	@RequestMapping(value = "/spareDataCollectorPanel",method = RequestMethod.GET)
	public ModelAndView spareDataCollectorPanel(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("spareDataCollector");
	}

	@RequestMapping(value = "config",method = RequestMethod.GET)
	public ModelAndView config(HttpServletRequest request) {
		return new ModelAndView("portalConfig");
	}

	@RequestMapping(value = "countryManagement",method = RequestMethod.GET)
	public ModelAndView countryManagement(HttpServletRequest request) {
		return new ModelAndView("countryManagement");
	}

	@RequestMapping(value = "currencyManagement",method = RequestMethod.GET)
	public ModelAndView currencyManagement(HttpServletRequest request) {
		return new ModelAndView("currencyManagement");
	}

	@RequestMapping(value = "batteryManagement",method = RequestMethod.GET)
	public ModelAndView batteryManagement(HttpServletRequest request) {
		return new ModelAndView("batteryManagement");
	}

	@RequestMapping(value = "adminProfile",method = RequestMethod.GET)
	public ModelAndView adminProfile(HttpServletRequest request) {
		return new ModelAndView("adminProfile");
	}

	// Mapping that were added in other controller 


	@RequestMapping(value="/userDashboardData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> userDashboardData(HttpServletRequest request, HttpServletResponse response){
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
	}

	@RequestMapping(value="/editUser",method=RequestMethod.POST)
	public ResponseEntity<String> editUser(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="customerId") String customerId){

		Customer customer = userService.getCustomerDetailsByCustomerId(Integer.parseInt(customerId));

		if(customer != null ){

			JsonObject jObject = new JsonObject();
			jObject.addProperty("customerId", customer.getCustomerId());
			jObject.addProperty("customerCode", customer.getCustomerCode());
			jObject.addProperty("firstName", customer.getUser().getDetails().getFirstName());
			jObject.addProperty("lastName", customer.getUser().getDetails().getLastname());

			jObject.addProperty("userName", customer.getUser().getUserName());

			jObject.addProperty("phone", customer.getUser().getDetails().getCell_number1());
			jObject.addProperty("email", customer.getUser().getDetails().getEmail1());
			jObject.addProperty("address", customer.getUser().getDetails().getAddress1());

			jObject.addProperty("zipcode", customer.getUser().getDetails().getZipcode());
			jObject.addProperty("userId", customer.getUser().getUserId());
			jObject.addProperty("status", customer.getStatus());
			jObject.addProperty("selectedTimeZone", customer.getTimeZone());
			jObject.addProperty("selectedCountryId", customer.getCountry().getCountryId());
			jObject.addProperty("selectedCurrencyId", customer.getCurrency().getCurrencyId());
			jObject.addProperty("currentDataPlanId", customer.getDataPlan().getDataPlanId());

			//Setting 4 dates 
			SimpleDateFormat sdf =  new SimpleDateFormat("MM-dd-yyyy");
			jObject.addProperty("activeDate",  sdf.format(customer.getDataPlanActivatedDate()));
			jObject.addProperty("portalStartDate", sdf.format(customer.getPortalPlanStartDate()));
			jObject.addProperty("portalEndDate", sdf.format(customer.getPortalPlanExpiryDate()));
			jObject.addProperty("customerId", customer.getCustomerId());
			// setting country and currency
			List<Currency> currencyList = userService.getCurrencyList();
			List<JsonObject> currencyResponseList = new ArrayList<JsonObject>();

			currencyList.stream().forEach(n -> {
				JsonObject jObj = new JsonObject();
				jObj.addProperty("id", n.getCurrencyId());
				jObj.addProperty("name", n.getCurrencyName());
				currencyResponseList.add(jObj);
			});

			List<Country> countryList = userService.getCountryList();	
			List<JsonObject> countryResponseList = new ArrayList<JsonObject>();

			countryList.stream().forEach(n -> {
				JsonObject jObj = new JsonObject();
				jObj.addProperty("id", n.getCountryId());
				jObj.addProperty("name", n.getCountryName());
				countryResponseList.add(jObj);
			});


			jObject.addProperty("countryList", new Gson().toJson(countryResponseList));
			jObject.addProperty("currencyList", new Gson().toJson(currencyResponseList));

			/*// Adding default customers country and currency
			jObject.addProperty("selectedCountry", new Gson().toJson(customer.getCountry()));
			jObject.addProperty("selectedCurrency", new Gson().toJson(customer.getCurrency()));*/

			request.getSession().setAttribute("responseJsonObject", jObject);
			return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.DATAFETCHED),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson(ApplicationConstants.NOUSERFOUND),HttpStatus.OK);
	}

	@RequestMapping(value="/deleteUser",method=RequestMethod.POST)
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
	}


}
